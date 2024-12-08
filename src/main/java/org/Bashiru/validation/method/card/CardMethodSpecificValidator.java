package org.Bashiru.validation.method.card;

import org.Bashiru.model.AccountInfo;
import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;
import org.Bashiru.validation.CountryCodeValidator;
import org.Bashiru.validation.method.MethodSpecificValidator;

import java.util.Locale;
import java.util.Optional;

public class CardMethodSpecificValidator implements MethodSpecificValidator {

    private final CountryCodeValidator countryCodeValidator = new CountryCodeValidator();

    @Override
    public Optional<String> validate(Transaction transaction, User user, AccountInfo accountInfo) {
        String convertedAccountCountryCode = countryCodeValidator.convertIso3ToIso2(accountInfo.getCountry());

        if (!convertedAccountCountryCode.equalsIgnoreCase(user.getCountry())) {
            return Optional.of(
                    EventMessages.TRANSACTION_FAILED_INVALID_CARD_COUNTRY.formatted(accountInfo.getCountry(), user.getCountry(), Locale.of("", user.getCountry()).getISO3Country())
            );
        }

        return Optional.empty();
    }

}
