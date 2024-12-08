package org.Bashiru.service.transaction;

import org.Bashiru.model.Transaction;

import java.nio.file.Path;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final List<Transaction> transactions;

    private static final String EXPECTED_CSV_HEADER = "TRANSACTION_ID,USER_ID,TYPE,AMOUNT,METHOD,ACCOUNT_NUMBER";

    public TransactionServiceImpl(Path filePath) {
        CsvReader<Transaction> transactionReader = new CsvReader<>(new TransactionCsvParser());
        this.transactions = transactionReader.read(filePath, EXPECTED_CSV_HEADER);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

}
