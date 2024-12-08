package org.Bashiru.validation.type.withdraw;

import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;
import org.Bashiru.service.users.UserService;
import org.Bashiru.validation.type.TypeSpecificValidator;

public class WithdrawalValidator implements TypeSpecificValidator {

    private final UserService userService;

    public WithdrawalValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int validate(Transaction transaction, User user) {
        // Validate if the withdrawal amount is within the defined limits
        return userService.amountInWithinLimits(transaction.getAmount(), user.getWithdrawMin(), user.getWithdrawMax());
    }
}

