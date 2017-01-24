package com.clouway.bank.adapter.http;

import com.clouway.bank.adapter.http.TransactionHistoryService.Query;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserSecurity;
import com.clouway.bank.matchers.JsonBuilder;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static com.clouway.bank.matchers.SitebricksMatchers.containsJson;
import static com.clouway.bank.matchers.SitebricksMatchers.isOk;
import static org.junit.Assert.assertThat;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
@SuppressWarnings("unchecked")
public class TransactionHistoryServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private TransactionRepository transactionRepository = context.mock(TransactionRepository.class);
  private UserSecurity security = context.mock(UserSecurity.class);
  private TransactionHistoryService service = new TransactionHistoryService(transactionRepository, security);

  private final String startingFromCursor = "12345";
  private final Boolean isNext = false;
  private final Integer limit = 20;
  private final Date date = new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime());

  @Test
  public void retrieveUserTransactions() {
    Query query = new Query(startingFromCursor, isNext);
    FakeRequest request = new FakeRequest(query);

    context.checking(new Expectations() {{
      oneOf(security).currentUser();
      will(returnValue(new User("123", "testUser", "pass")));

      oneOf(transactionRepository).retrieveTransactions("123", startingFromCursor, isNext, limit);
      will(returnValue(new ArrayList<Transaction>() {{
        add(new Transaction(startingFromCursor, date, "123", "deposit", 100d));
        add(new Transaction(startingFromCursor, date, "123", "deposit", 105d));
      }}));
    }});

    Reply<?> actual = service.retrieveTransactionHistory(request);

    assertThat(actual, isOk());
    assertThat(actual, containsJson(JsonBuilder.aNewJsonArray().withElements(
            JsonBuilder.aNewJson().add("cursor", startingFromCursor).add("date", date.toString()).add("type", "deposit").add("amount", 100d),
            JsonBuilder.aNewJson().add("cursor", startingFromCursor).add("date", date.toString()).add("type", "deposit").add("amount", 105d)
    )));
  }
}
