package hu.kunb.paymentapi.core.model;

import java.util.UUID;

public record PaymentRecord(UUID clientId, UUID transactionId, Long amount) {

}
