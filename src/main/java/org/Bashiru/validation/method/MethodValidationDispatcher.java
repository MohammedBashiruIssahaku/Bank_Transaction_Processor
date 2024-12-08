package org.Bashiru.validation.method;

import org.Bashiru.enums.TransactionMethodType;
import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;

import java.util.Map;
import java.util.Optional;

import static org.Bashiru.enums.TransactionMethodType.CARD;
import static org.Bashiru.enums.TransactionMethodType.TRANSFER;

public class MethodValidationDispatcher {

    private final Map<TransactionMethodType, MethodSpecificValidator> validatorMap;

    public MethodValidationDispatcher() {
        this.validatorMap = Map.of(
                CARD, new CardMethodSpecificValidator(),
                TRANSFER, new TransferMethodValidator()
        );
    }

    public Optional<String> validateMethodSpecificTransaction(Transaction transaction, User user, AccountInfo accountInfo) {
        MethodSpecificValidator validator = validatorMap.get(TransactionMethodType.valueOf(transaction.getMethod()));

        if (validator != null) {
            return validator.validate(transaction, user, accountInfo);
        }

        return Optional.empty();
    }
}
