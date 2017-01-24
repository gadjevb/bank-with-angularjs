package com.clouway.bank.adapter.persistence;

import com.clouway.bank.core.TransactionRecord;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.matchers.DatastoreRule;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentTransactionRepositoryTest {

  @Rule
  public DatastoreRule datastoreRule = new DatastoreRule();

  private PersistentTransactionRepository transactionRepository;

  @Before
  public void setUp() {
    transactionRepository = new PersistentTransactionRepository(datastoreRule.getDatabase());
  }

  @Test
  public void retrieveFirstPage() {
    pretendThatExistingTransactionsAre(generateTransactions(27));

    List<TransactionRecord> firstPage = transactionRepository.retrieveTransactions("::any user id::", "", true, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
  }

  @Test
  public void retrieveSecondPage() {
    pretendThatExistingTransactionsAre(generateTransactions(27));

    List<TransactionRecord> firstPage = transactionRepository.retrieveTransactions("::any user id::", "", true, 20);
    List<TransactionRecord> secondPage = transactionRepository.retrieveTransactions("::any user id::", firstPage.get(firstPage.size() - 1).id, true, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
    assertThat(secondPage.size(), is(equalTo(7)));
  }

  @Test
  public void retrieveThirdPage() {
    pretendThatExistingTransactionsAre(generateTransactions(47));

    List<TransactionRecord> firstPage = transactionRepository.retrieveTransactions("::any user id::", "", true, 20);
    List<TransactionRecord> secondPage = transactionRepository.retrieveTransactions("::any user id::", firstPage.get(firstPage.size() - 1).id, true, 20);
    List<TransactionRecord> thirdPage = transactionRepository.retrieveTransactions("::any user id::", secondPage.get(firstPage.size() - 1).id, true, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
    assertThat(secondPage.size(), is(equalTo(20)));
    assertThat(thirdPage.size(), is(equalTo(7)));
  }

  @Test
  public void returnToPreviousPage() {
    pretendThatExistingTransactionsAre(generateTransactions(27));

    List<TransactionRecord> firstPage = transactionRepository.retrieveTransactions("::any user id::", "", true, 20);
    List<TransactionRecord> secondPage = transactionRepository.retrieveTransactions("::any user id::", firstPage.get(firstPage.size() - 1).id, true, 20);
    List<TransactionRecord> previousPage = transactionRepository.retrieveTransactions("::any user id::", secondPage.get(0).id, false, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
    assertThat(secondPage.size(), is(equalTo(7)));
    assertThat(previousPage.size(), is(equalTo(20)));
    assertThat(firstPage, is(equalTo(previousPage)));
  }

  private List<Transaction> generateTransactions(int count) {
    List<Transaction> result = Lists.newArrayList();
    for (int i = 0; i < count; i++) {
      result.add(new Transaction(
              null,
              new Date(new Timestamp(System.currentTimeMillis()).getTime()),
              "::any user id::",
              "Deposit",
              (double) i));
    }
    return result;
  }

  private void pretendThatExistingTransactionsAre(List<Transaction> transactions) {
    for (Transaction each : transactions) {
      transactionRepository.registerTransaction(each);
    }
  }

}
