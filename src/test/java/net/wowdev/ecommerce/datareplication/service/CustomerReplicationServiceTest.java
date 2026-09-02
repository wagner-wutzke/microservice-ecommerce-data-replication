package net.wowdev.ecommerce.datareplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import net.wowdev.ecommerce.datareplication.repository.CustomerRepository;
import net.wowdev.ecommerce.domain.dto.CustomerDTO;
import net.wowdev.ecommerce.domain.entity.CustomerEntity;
import net.wowdev.ecommerce.domain.enums.CustomerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerReplicationServiceTest {
  @Mock private CustomerRepository repository;
  private CustomerReplicationService service;

  @BeforeEach
  void setUp() { service = new CustomerReplicationService(repository); }

  @Test
  void findByIdReturnsMappedCustomer() {
    CustomerEntity entity = customerEntity();
    when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
    assertThat(service.findById(entity.getId()).getEmail()).isEqualTo(entity.getEmail());
  }

  @Test
  void findByIdThrowsWhenCustomerDoesNotExist() {
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.findById(id)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void replicateCreatesAndReturnsCustomer() {
    CustomerDTO dto = customerDto();
    CustomerEntity saved = customerEntity();
    when(repository.findById(dto.getId())).thenReturn(Optional.empty());
    when(repository.save(any(CustomerEntity.class))).thenReturn(saved);
    assertThat(service.replicate(dto).getId()).isEqualTo(saved.getId());
    verify(repository).save(any(CustomerEntity.class));
  }

  @Test
  void replicateUpdatesExistingCustomer() {
    CustomerDTO dto = customerDto();
    CustomerEntity existing = customerEntity();
    when(repository.findById(dto.getId())).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);
    service.replicate(dto);
    assertThat(existing.getFirstName()).isEqualTo(dto.getFirstName());
    assertThat(existing.getAddressLine1()).isEqualTo(dto.getAddressLine1());
  }

  @Test
  void updateEntityCopiesEveryReplicatedField() {
    CustomerEntity updated = CustomerReplicationService.updateCustomerEntity(new CustomerEntity(), customerEntity());
    assertThat(updated.getFirstName()).isEqualTo("Jane");
    assertThat(updated.getLastName()).isEqualTo("Doe");
    assertThat(updated.getEmail()).isEqualTo("jane@example.com");
    assertThat(updated.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE);
    assertThat(updated.getAddressLine1()).isEqualTo("Main Street 1");
    assertThat(updated.getAddressLine2()).isEqualTo("Apt 2");
    assertThat(updated.getCity()).isEqualTo("Sao Paulo");
    assertThat(updated.getStateProvince()).isEqualTo("SP");
    assertThat(updated.getPostalCode()).isEqualTo("01000-000");
    assertThat(updated.getCountry()).isEqualTo("Brazil");
  }

  private static CustomerDTO customerDto() {
    CustomerDTO dto = new CustomerDTO();
    dto.setId(UUID.randomUUID()); dto.setFirstName("Jane"); dto.setLastName("Doe");
    dto.setEmail("jane@example.com"); dto.setCustomerStatus(CustomerStatus.ACTIVE);
    dto.setAddressLine1("Main Street 1"); dto.setAddressLine2("Apt 2"); dto.setCity("Sao Paulo");
    dto.setStateProvince("SP"); dto.setPostalCode("01000-000"); dto.setCountry("Brazil");
    return dto;
  }

  private static CustomerEntity customerEntity() {
    CustomerEntity entity = new CustomerEntity();
    entity.setId(customerDto().getId()); entity.setFirstName("Jane"); entity.setLastName("Doe");
    entity.setEmail("jane@example.com"); entity.setCustomerStatus(CustomerStatus.ACTIVE);
    entity.setAddressLine1("Main Street 1"); entity.setAddressLine2("Apt 2"); entity.setCity("Sao Paulo");
    entity.setStateProvince("SP"); entity.setPostalCode("01000-000"); entity.setCountry("Brazil");
    return entity;
  }
}
