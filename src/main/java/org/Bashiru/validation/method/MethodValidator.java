package org.Bashiru.validation.method;

import org.Bashiru.enums.TransactionMethodType;

import java.util.Optional;

public class MethodValidator {

    private MethodValidator() {
    }

    public static Optional<String> validateMethod(Transaction transaction) {
        if (isMethodAbsent(transaction.getMethod())) {
            return Optional.of(
                    EventMessages.TRANSACTION_FAILED_MISSING_PAYMENT_METHOD
            );
        }

        if (!isKnownMethodType(transaction.getMethod())) {
            return Optional.of(
                    EventMessages.TRANSACTION_FAILED_UNKNOWN_PAYMENT_METHOD
            );
        }

        return Optional.empty();
    }

    private static boolean isMethodAbsent(String method) {
        return method == null || method.trim().isEmpty();
    }

    private static boolean isKnownMethodType(String method) {
        for (TransactionMethodType transactionMethodType : TransactionMethodType.values()) {
            if (transactionMethodType.name().equals(method)) {
                return true;
            }
        }
        return false;
    }
}
