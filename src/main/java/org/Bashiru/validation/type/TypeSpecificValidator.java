package org.Bashiru.validation.type;

import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;

public interface TypeSpecificValidator {
    int validate(Transaction transaction, User user);
}

