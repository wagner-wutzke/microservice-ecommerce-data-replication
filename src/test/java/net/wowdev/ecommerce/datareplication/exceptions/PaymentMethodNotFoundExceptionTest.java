package net.wowdev.ecommerce.datareplication.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaymentMethodNotFoundExceptionTest {

  @Test
  void stringConstructorPreservesMessage() {
    assertThat(new PaymentMethodNotFoundException("missing").getMessage()).isEqualTo("missing");
  }

  @Test
  void uuidConstructorBuildsExpectedMessage() {
    UUID id = UUID.randomUUID();
    assertThat(new PaymentMethodNotFoundException(id).getMessage())
        .isEqualTo("Payment Method not found for id: " + id);
  }
}
