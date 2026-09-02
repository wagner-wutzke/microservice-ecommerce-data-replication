package net.wowdev.ecommerce.datareplication.service;

import java.util.UUID;
import net.wowdev.ecommerce.domain.dto.CustomerDTO;

public interface CustomerReplicationServiceInterface {

  CustomerDTO findById(UUID id);

  CustomerDTO replicate(CustomerDTO customer);
}
