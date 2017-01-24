package com.clouway.bank.core;

import java.util.List;

/**
 * This {@code TransactionRepository} interface provides the methods
 * to be implemented for work with the transactions collection from
 * the database
 *
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface TransactionRepository {

  /**
   * Registers a new transaction record in the transaction
   * collection in the database
   *
   * @param transaction the transaction to be inserted
   */
  void registerTransaction(Transaction transaction);

  /**
   * Retrieves all transactions for the given user from the
   * transactions collection in the database
   *
   * @param userID used to match the transactions to be returned
   * @param startingFromCursor used to match the record from which to begin with
   * @param isNext used to specify if the next or previous page should
   *               be rendered to the user. If True the next page will
   *               be rendered, if False the previous page will be
   *               rendered
   * @param limit used to specify the maximum number of transactions
   *              that could be returned per page
   * @return List of Record objects that are result of the search
   */
  List<TransactionRecord> retrieveTransactions(String userID, String startingFromCursor, Boolean isNext, Integer limit);
}
