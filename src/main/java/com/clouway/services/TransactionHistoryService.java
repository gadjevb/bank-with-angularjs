package com.clouway.services;

import com.clouway.core.Transaction;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
@At("/history")
public class TransactionHistoryService {
  private List<Transaction> transactionList;

  @Get
  public Reply<?> retrieveTransactionHistory() {
    transactionList = generateList(); //todo remove the unnecessary generateList method and implement DAO from which the list will be retrieved

    return Reply.with(transactionList).as(Json.class);
  }

  private List<Transaction> generateList() {
    List<Transaction> transactions = new ArrayList<>();

    for (int i = 0; i < 2; i++) {
      transactions.add(new Transaction(LocalDate.of(2017,1,1), "testUser", "Deposit", 100d));
    }

    return transactions;
  }

}
