package net.wowdev.ecommerce.datareplication.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.wowdev.ecommerce.datareplication.exceptions.PaymentMethodNotFoundException;
import net.wowdev.ecommerce.datareplication.repository.PaymentMethodRepository;
import net.wowdev.ecommerce.domain.dto.PaymentMethodDTO;
import net.wowdev.ecommerce.domain.entity.PaymentMethodEntity;
import net.wowdev.ecommerce.domain.mapper.PaymentMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PaymentMethodReplicationService implements
    PaymentMethodReplicationServiceInterface {

  private final PaymentMethodRepository paymentMethodRepository;

  @Autowired
  public PaymentMethodReplicationService(PaymentMethodRepository paymentMethodRepository) {
    this.paymentMethodRepository = paymentMethodRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public PaymentMethodDTO findById(final UUID id) {
    return PaymentMethodMapper.toDto(
        paymentMethodRepository
            .findById(id)
            .orElseThrow(
                () -> new PaymentMethodNotFoundException(id)));
  }

  @Override
  @Transactional
  public PaymentMethodDTO replicate(PaymentMethodDTO paymentMethodDTO) {
    final PaymentMethodEntity replicatingEntity = PaymentMethodMapper.toEntity(paymentMethodDTO);
    final PaymentMethodEntity entityToSave =
        paymentMethodRepository
            .findById(paymentMethodDTO.getId())
            .map(
                existingEntity -> {
                  final PaymentMethodEntity updatedEntity =
                      updatePaymentMethodEntity(existingEntity, replicatingEntity);
                  log.debug(">>>> Updating replica for PaymentMethod record...");
                  return updatedEntity;
                })
            .orElseGet(
                () -> {
                  log.debug(">>>> Creating replica for PaymentMethod record...");
                  return replicatingEntity;
                });
    final PaymentMethodEntity replicatedEntity = paymentMethodRepository.save(entityToSave);
    log.debug(">>>> Saved replica for PaymentMethod record: {}", replicatedEntity.getId());
    return PaymentMethodMapper.toDto(replicatedEntity);
  }

  protected static PaymentMethodEntity updatePaymentMethodEntity(
      PaymentMethodEntity existingEntity, PaymentMethodEntity toReplicateEntity) {
    existingEntity.setOwnerName(toReplicateEntity.getOwnerName());
    existingEntity.setCardName(toReplicateEntity.getCardName());
    existingEntity.setExpiration(toReplicateEntity.getExpiration());
    existingEntity.setCardNumber(toReplicateEntity.getCardNumber());
    existingEntity.setCvv(toReplicateEntity.getCvv());
    existingEntity.setCustomerId(toReplicateEntity.getCustomerId());
    return existingEntity;
  }
}
