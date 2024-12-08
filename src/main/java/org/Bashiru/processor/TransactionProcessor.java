package org.Bashiru.processor;

import org.Bashiru.enums.TransactionMethodType;
import org.Bashiru.enums.TransactionType;
import org.Bashiru.model.AccountInfo;
import org.Bashiru.model.Transaction;
import org.Bashiru.model.User;
import org.Bashiru.service.bins.BinMappingService;
import org.Bashiru.service.event.EventService;
import org.Bashiru.service.transaction.TransactionService;
import org.Bashiru.service.users.UserService;
import org.Bashiru.validation.method.MethodValidationDispatcher;
import org.Bashiru.validation.method.MethodValidator;
import org.Bashiru.validation.type.TypeValidationDispatcher;

import java.util.List;
import java.util.Optional;

public class TransactionProcessor {

    private final UserService userService;

    private final TransactionService transactionService;

    private final EventService eventService;

    private final BinMappingService binMappingService;

    private final TypeValidationDispatcher typeValidationDispatcher;

    private final MethodValidationDispatcher methodValidationDispatcher;

    public TransactionProcessor(UserService userService, TransactionService transactionService, EventService eventService, BinMappingService binMappingService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.eventService = eventService;
        this.binMappingService = binMappingService;
        this.typeValidationDispatcher = new TypeValidationDispatcher(userService);
        this.methodValidationDispatcher = new MethodValidationDispatcher();
    }

    public void processTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        for (Transaction transaction : transactions) {
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        if (checkTransactionProcessed(transaction)) return;

        Optional<User> optionalUser = userService.findUserById(transaction.getUserId());
        if (optionalUser.isEmpty()) {
            eventService.addUserNotValidMessage(transaction.getTransactionId(), transaction.getUserId());
            transaction.setFailed();
            return;
        }

        User user = optionalUser.get();
        TransactionMethodType transactionMethod = TransactionMethodType.valueOf(transaction.getMethod());
        AccountInfo accountInfo = binMappingService.getAccountInfo(transaction.getAccountNumber(), transactionMethod, transaction.getType());

        if (user.isFrozen()) {
            eventService.addUserFrozenMessage(transaction.getTransactionId(), transaction.getUserId());
            transaction.setFailed();
            return;
        }

        Optional<String> methodValidationError = MethodValidator.validateMethod(transaction);
        if (methodValidationError.isPresent()) {
            eventService.addFailureEvent(transaction.getTransactionId(), methodValidationError.get());
            transaction.setFailed();
            return;
        }

        Optional<String> typeSpecificError = typeValidationDispatcher.validateTypeSpecificTransaction(transaction, user);
        if (typeSpecificError.isPresent()) {
            eventService.addTransactionTypeSpecificMessage(transaction.getTransactionId(), typeSpecificError.get());
            transaction.setFailed();
            return;
        }

        Optional<String> paymentPropertiesError = methodValidationDispatcher.validateMethodSpecificTransaction(transaction, user, accountInfo);
        if (paymentPropertiesError.isPresent()) {
            eventService.addFailureEvent(transaction.getTransactionId(), paymentPropertiesError.get());
            transaction.setFailed();
            return;
        }

        if (!binMappingService.userCanUseAccount(user, transaction.getAccountNumber(), transactionMethod, transaction.getType())) {
            eventService.addAccountInUse(transaction.getTransactionId(), transaction.getAccountNumber());
            transaction.setFailed();
            return;
        }

        if (transaction.getType().equals(TransactionType.WITHDRAW) && !userService.accountHasUsedBeforeForDeposit(user, transaction.getAccountNumber()))
        {
            eventService.addNoPreviousDeposit(transaction.getTransactionId(), transaction.getAccountNumber());
            transaction.setFailed();
            return;
        }