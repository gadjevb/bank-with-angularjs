package com.clouway.bank.core;

import java.util.List;

/**
 * This {@code TransactionRepository} interface provides
 * the methods to be implemented for work with the
 * transfer collection from the database
 *
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface TransferRepository {

  /**
   * Saves the transfer in a database record
   *
   * @param transfer the transfer object to be saved in the database
   */
  void saveTransfer(Transfer transfer);

  /**
   * Retrieves all transfers records for the given user
   * from the transfer database
   *
   * @param userID used to match the records to be returned
   * @param startingFromCursor used to match the starting position
   * @param isNext used to specify if the next or previous page should
   *               be rendered to the user. If True the next page will
   *               be rendered, if False the previous page will be
   *               rendered
   * @param limit used to specify the maximum number of transfers
   *              that could be returned per page
   * @return List of TransferRecord objects that are result of the search
   */
  List<TransferRecord> retrieveTransfers(String userID, String startingFromCursor, Boolean isNext, Integer limit);
}
