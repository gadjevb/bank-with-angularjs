package com.clouway.bank.adapter.persistence;

import com.clouway.bank.matchers.DatastoreRule;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class PersistentAccountRepositoryTest {

  @Rule
  public DatastoreRule datastoreRule = new DatastoreRule();
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private TransactionRepository transactionRepository = context.mock(TransactionRepository.class);

  private PersistentAccountRepository accountRepository;

  @Before
  public void setUp() {
     accountRepository = new PersistentAccountRepository(datastoreRule.getDatabase(), transactionRepository);
  }

  @Test
  public void findAccountThatWasRegistered() throws Exception {
    Account actual = accountRepository.register("A", 10d);
    Account expected = accountRepository.findUserAccount(actual.id).get();

    assertThat(actual, is(expected));
  }

  @Test
  public void accountWasNotFound() throws Exception {
    Optional<Account> actual = accountRepository.findUserAccount("507f1f77bcf86cd799439011");

    assertTrue(!actual.isPresent());
  }

  @Test
  public void updateAccountBalance() throws Exception {
    Account account = accountRepository.register("A", 10d);

    context.checking(new Expectations() {{
      oneOf(transactionRepository).registerTransaction(
              with(any(Transaction.class)));
    }});

    accountRepository.update(account.id, 5d, "withdraw", "5");

    Double balance = accountRepository.findUserAccount(account.id).get().balance;
    assertThat(balance, is(5d));
  }
}