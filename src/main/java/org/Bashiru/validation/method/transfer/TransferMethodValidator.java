package org.Bashiru.validation.method.transfer;

import org.Bashiru.model.AccountInfo;
import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;
import org.Bashiru.validation.CountryCodeValidator;
import org.Bashiru.validation.IbanValidator;
import org.Bashiru.validation.method.MethodSpecificValidator;

import java.util.Optional;

public class TransferMethodValidator implements MethodSpecificValidator {

    @Override
    public Optional<String> validate(Transaction transaction, User user, AccountInfo accountInfo) {
        if (!IbanValidator.validate(transaction.getAccountNumber())) {
            return Optional.of(
                    EventMessages.TRANSACTION_FAILED_IBAN_INVALID.formatted(transaction.getAccountNumber())
            );
        }

        if (!CountryCodeValidator.transferCountryValid(user, accountInfo)) {
            return Optional.of(
                    EventMessages.TRANSACTION_FAILED_INVALID_ACCOUNT_COUNTRY.formatted(accountInfo.getCountry(), user.getCountry())
            );
        }

        return Optional.empty();
    }
}
