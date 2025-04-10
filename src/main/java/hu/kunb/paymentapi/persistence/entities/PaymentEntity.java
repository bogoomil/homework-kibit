package hu.kunb.paymentapi.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = {"clientId", "transactionId"}))
@Data
public class PaymentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private UUID transactionId;

  @Column(nullable = false)
  private UUID clientId;

  @Column(nullable = false)
  private Long amount;

}
