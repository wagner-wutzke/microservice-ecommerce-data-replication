package net.wowdev.ecommerce.datareplication.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerNotFoundExceptionTest {

  @Test
  void stringConstructorPreservesMessage() {
    assertThat(new CustomerNotFoundException("missing").getMessage()).isEqualTo("missing");
  }

  @Test
  void uuidConstructorBuildsExpectedMessage() {
    UUID id = UUID.randomUUID();
    assertThat(new CustomerNotFoundException(id).getMessage()).isEqualTo("Customer not found for id: " + id);
  }
}
