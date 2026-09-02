package net.wowdev.ecommerce.datareplication.exceptions;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(final String message) {
    super(message);
  }

  public OrderNotFoundException(final UUID id) {
    super("Order not found for id: " + id);
  }
}
