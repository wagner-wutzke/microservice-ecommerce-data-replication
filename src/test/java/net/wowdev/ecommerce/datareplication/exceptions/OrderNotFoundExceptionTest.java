package net.wowdev.ecommerce.datareplication.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderNotFoundExceptionTest {

  @Test
  void stringConstructorPreservesMessage() {
    assertThat(new OrderNotFoundException("missing").getMessage()).isEqualTo("missing");
  }

  @Test
  void uuidConstructorBuildsExpectedMessage() {
    UUID id = UUID.randomUUID();
    assertThat(new OrderNotFoundException(id).getMessage()).isEqualTo("Order not found for id: " + id);
  }
}
