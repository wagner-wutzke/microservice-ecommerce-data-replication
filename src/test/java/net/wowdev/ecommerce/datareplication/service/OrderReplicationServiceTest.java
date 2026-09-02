package net.wowdev.ecommerce.datareplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.wowdev.ecommerce.datareplication.repository.OrderRepository;
import net.wowdev.ecommerce.domain.dto.OrderDTO;
import net.wowdev.ecommerce.domain.dto.OrderLineDTO;
import net.wowdev.ecommerce.domain.entity.OrderEntity;
import net.wowdev.ecommerce.domain.entity.OrderLineEntity;
import net.wowdev.ecommerce.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderReplicationServiceTest {
  @Mock private OrderRepository repository;
  private OrderReplicationService service;

  @BeforeEach
  void setUp() { service = new OrderReplicationService(repository); }

  @Test
  void findByIdReturnsMappedOrder() {
    OrderEntity entity = orderEntity();
    when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
    assertThat(service.findById(entity.getId()).getOrderNumber()).isEqualTo("ORD-1");
  }

  @Test
  void findByIdThrowsWhenOrderDoesNotExist() {
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.findById(id)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void replicateCreatesOrder() {
    OrderDTO dto = orderDto();
    OrderEntity saved = orderEntity();
    when(repository.findById(dto.getId())).thenReturn(Optional.empty());
    when(repository.save(any(OrderEntity.class))).thenReturn(saved);
    assertThat(service.replicate(dto).getOrderNumber()).isEqualTo("ORD-1");
    verify(repository).save(any(OrderEntity.class));
  }

  @Test
  void replicateUpdatesExistingOrder() {
    OrderDTO dto = orderDto();
    OrderEntity existing = orderEntity();
    when(repository.findById(dto.getId())).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);
    service.replicate(dto);
    assertThat(existing.getOrderStatus()).isEqualTo(dto.getOrderStatus());
    assertThat(existing.getTotalAmount()).isEqualTo(dto.getTotalAmount());
  }

  @Test
  void updateEntityCopiesOrderFieldsAndProcessesLines() {
    OrderEntity source = orderEntity();
    OrderEntity existing = new OrderEntity();
    existing.setOrderLines(List.of(source.getOrderLines().get(0)));
    OrderEntity result = OrderReplicationService.updateOrderEntity(existing, source);
    assertThat(result.getCustomerId()).isEqualTo(source.getCustomerId());
    assertThat(result.getOrderAmount()).isEqualTo(source.getOrderAmount());
    assertThat(result.getDiscountAmount()).isEqualTo(source.getDiscountAmount());
    assertThat(result.getTaxAmount()).isEqualTo(source.getTaxAmount());
    assertThat(result.getTotalAmount()).isEqualTo(source.getTotalAmount());
    assertThat(result.getShippingAmount()).isEqualTo(source.getShippingAmount());
    assertThat(result.getOrderNumber()).isEqualTo(source.getOrderNumber());
    assertThat(result.getPaymentMethodId()).isEqualTo(source.getPaymentMethodId());
    assertThat(result.getOrderStatus()).isEqualTo(source.getOrderStatus());
  }

  private static OrderDTO orderDto() {
    OrderDTO dto = new OrderDTO();
    dto.setId(UUID.randomUUID()); dto.setCustomerId(UUID.randomUUID()); dto.setPaymentMethodId(UUID.randomUUID());
    dto.setOrderStatus(OrderStatus.CONFIRMED); dto.setOrderAmount(new BigDecimal("100.00"));
    dto.setDiscountAmount(new BigDecimal("5.00")); dto.setTaxAmount(new BigDecimal("10.00"));
    dto.setTotalAmount(new BigDecimal("110.00")); dto.setShippingAmount(new BigDecimal("5.00"));
    dto.setOrderNumber("ORD-1");
    OrderLineDTO line = new OrderLineDTO(); line.setId(UUID.randomUUID());
    line.setProductId(UUID.randomUUID()); line.setQuantity(2); line.setPrice(new BigDecimal("50.00"));
    dto.setOrderLines(List.of(line));
    return dto;
  }

  private static OrderEntity orderEntity() {
    OrderEntity entity = new OrderEntity();
    entity.setId(UUID.randomUUID()); entity.setCustomerId(UUID.randomUUID());
    entity.setPaymentMethodId(UUID.randomUUID()); entity.setOrderStatus(OrderStatus.CONFIRMED);
    entity.setOrderAmount(new BigDecimal("100.00")); entity.setDiscountAmount(new BigDecimal("5.00"));
    entity.setTaxAmount(new BigDecimal("10.00")); entity.setTotalAmount(new BigDecimal("110.00"));
    entity.setShippingAmount(new BigDecimal("5.00")); entity.setOrderNumber("ORD-1");
    OrderLineEntity line = new OrderLineEntity(); line.setId(UUID.randomUUID());
    line.setProductId(UUID.randomUUID()); line.setQuantity(2); line.setPrice(new BigDecimal("50.00"));
    entity.setOrderLines(List.of(line));
    return entity;
  }
}
