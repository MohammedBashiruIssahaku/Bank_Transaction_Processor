package org.Bashiru.validation.type.deposit;

import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;
import org.Bashiru.service.users.UserService;
import org.Bashiru.validation.type.TypeSpecificValidator;

public class DepositValidator implements TypeSpecificValidator {

    private final UserService userService;

    public DepositValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int validate(Transaction transaction, User user) {
        // Validate if the deposit amount is within the defined limits
        return userService.amountInWithinLimits(transaction.getAmount(), user.getDepositMin(), user.getDepositMax());
    }
}
