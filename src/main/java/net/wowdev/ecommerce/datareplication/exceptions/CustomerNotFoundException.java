package net.wowdev.ecommerce.datareplication.exceptions;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException(final String message) {
    super(message);
  }

  public CustomerNotFoundException(final UUID id) {
    super("Customer not found for id: " + id);
  }
}
