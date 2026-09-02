package net.wowdev.ecommerce.datareplication.exceptions;

import java.util.UUID;

public class PaymentMethodNotFoundException extends RuntimeException {

  public PaymentMethodNotFoundException(final String message) {
    super(message);
  }

  public PaymentMethodNotFoundException(final UUID id) {
    super("Payment Method not found for id: " + id);
  }
}
