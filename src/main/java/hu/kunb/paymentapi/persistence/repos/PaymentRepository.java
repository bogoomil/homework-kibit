package hu.kunb.paymentapi.persistence.repos;

import hu.kunb.paymentapi.persistence.entities.PaymentEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
  Optional<PaymentEntity> findByTransactionId(UUID transactionId);
}
