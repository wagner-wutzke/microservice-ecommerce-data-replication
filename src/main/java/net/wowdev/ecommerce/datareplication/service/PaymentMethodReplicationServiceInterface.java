package net.wowdev.ecommerce.datareplication.service;

import java.util.UUID;
import net.wowdev.ecommerce.domain.dto.PaymentMethodDTO;

public interface PaymentMethodReplicationServiceInterface {

  PaymentMethodDTO findById(UUID id);

  PaymentMethodDTO replicate(PaymentMethodDTO paymentMethodDTO);
}
