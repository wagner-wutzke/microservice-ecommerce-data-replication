package net.wowdev.ecommerce.datareplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import net.wowdev.ecommerce.datareplication.repository.PaymentMethodRepository;
import net.wowdev.ecommerce.domain.dto.PaymentMethodDTO;
import net.wowdev.ecommerce.domain.entity.PaymentMethodEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentMethodReplicationServiceTest {
  @Mock private PaymentMethodRepository repository;
  private PaymentMethodReplicationService service;

  @BeforeEach
  void setUp() { service = new PaymentMethodReplicationService(repository); }

  @Test
  void findByIdReturnsMappedPaymentMethod() {
    PaymentMethodEntity entity = paymentMethodEntity();
    when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
    assertThat(service.findById(entity.getId()).getCardName()).isEqualTo("Visa");
  }

  @Test
  void findByIdThrowsWhenPaymentMethodDoesNotExist() {
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.findById(id)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void replicateCreatesPaymentMethod() {
    PaymentMethodDTO dto = paymentMethodDto();
    PaymentMethodEntity saved = paymentMethodEntity();
    when(repository.findById(dto.getId())).thenReturn(Optional.empty());
    when(repository.save(any(PaymentMethodEntity.class))).thenReturn(saved);
    assertThat(service.replicate(dto).getCardNumber()).isEqualTo(saved.getCardNumber());
    verify(repository).save(any(PaymentMethodEntity.class));
  }

  @Test
  void replicateUpdatesExistingPaymentMethod() {
    PaymentMethodDTO dto = paymentMethodDto();
    PaymentMethodEntity existing = paymentMethodEntity();
    when(repository.findById(dto.getId())).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);
    service.replicate(dto);
    assertThat(existing.getOwnerName()).isEqualTo(dto.getOwnerName());
    assertThat(existing.getCvv()).isEqualTo(dto.getCvv());
  }

  @Test
  void updateEntityCopiesEveryReplicatedField() {
    PaymentMethodEntity result = PaymentMethodReplicationService.updatePaymentMethodEntity(
        new PaymentMethodEntity(), paymentMethodEntity());
    assertThat(result.getOwnerName()).isEqualTo("Jane Doe");
    assertThat(result.getCardName()).isEqualTo("Visa");
    assertThat(result.getExpiration()).isEqualTo("12/30");
    assertThat(result.getCardNumber()).isEqualTo("4111111111111111");
    assertThat(result.getCvv()).isEqualTo(123);
    assertThat(result.getCustomerId()).isNotNull();
  }

  private static PaymentMethodDTO paymentMethodDto() {
    PaymentMethodDTO dto = new PaymentMethodDTO();
    dto.setId(UUID.randomUUID()); dto.setCustomerId(UUID.randomUUID()); dto.setCardNumber("4111111111111111");
    dto.setOwnerName("Jane Doe"); dto.setExpiration("12/30"); dto.setCvv(123); dto.setCardName("Visa");
    return dto;
  }

  private static PaymentMethodEntity paymentMethodEntity() {
    PaymentMethodEntity entity = new PaymentMethodEntity();
    entity.setId(UUID.randomUUID()); entity.setCustomerId(UUID.randomUUID());
    entity.setCardNumber("4111111111111111"); entity.setOwnerName("Jane Doe");
    entity.setExpiration("12/30"); entity.setCvv(123); entity.setCardName("Visa");
    return entity;
  }
}
