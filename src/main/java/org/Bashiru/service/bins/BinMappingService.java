package org.Bashiru.service.bins;

import org.Bashiru.enums.TransactionMethodType;
import org.Bashiru.enums.TransactionType;
import org.Bashiru.model.AccountInfo;
import org.Bashiru.model.BinMapping;
import org.Bashiru.model.User;

public interface BinMappingService {

    AccountInfo getAccountInfo(String accountNumber, TransactionMethodType transactionMethodType, TransactionType transactionType);

    BinMapping getBinTypeForCard(String accountNumber);

    boolean userCanUseAccount(User user, String accountNumber, TransactionMethodType transactionMethodType, TransactionType transactionType);

    void updateApprovedDeposit(String accountNumber);
}

