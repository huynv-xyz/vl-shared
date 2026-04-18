package com.vlife.shared.jdbc.client;

import io.micronaut.transaction.TransactionDefinition;
import io.micronaut.transaction.TransactionOperations;
import io.micronaut.transaction.TransactionStatus;
import jakarta.inject.Singleton;

import java.sql.Connection;
import java.util.function.Supplier;

@Singleton
public class TxRunner {

    private final TransactionOperations<Connection> txOps;

    public TxRunner(TransactionOperations<Connection> txOps) {
        this.txOps = txOps;
    }

    public <T> T required(Supplier<T> supplier) {
        return txOps.execute(
                TransactionDefinition.DEFAULT,
                (TransactionStatus<Connection> status) -> supplier.get()
        );
    }
}
