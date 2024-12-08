package org.Bashiru.validation.method;

import org.Bashiru.model.AccountInfo;
import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;

import java.util.Optional;

public interface MethodSpecificValidator {
    Optional<String> validate(Transaction transaction, User user, AccountInfo accountInfo);
}

