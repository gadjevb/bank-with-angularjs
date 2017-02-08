package com.clouway.bank.adapter.http;

import com.clouway.bank.adapter.http.TransferService.Query;
import com.clouway.bank.core.*;
import com.clouway.bank.matchers.JsonBuilder;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.clouway.bank.matchers.SitebricksMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class TransferServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private UserSecurity security = context.mock(UserSecurity.class);
  private AccountRepository accountRepository = context.mock(AccountRepository.class);
  private TransferRepository transferRepository = context.mock(TransferRepository.class);

  private TransferService service = new TransferService(security, accountRepository, transferRepository);

  @Test
  public void validCurrentUser() {
    context.checking(new Expectations() {{
      oneOf(security).currentUser();
      will(returnValue(new User("123", "testUser", "pass")));

      oneOf(accountRepository).findAccountByID("123");
      will(returnValue(Optional.of(new Account("123", "testUser", 40d))));
    }});

    Reply<?> actual = service.returnCurrentUser();

    assertThat(actual, isOk());
  }

  @Test (expected = IllegalStateException.class)
  public void invalidCurrentUser() {
    context.checking(new Expectations() {{
      oneOf(security).currentUser();
      will(returnValue(throwException(new UserNotAuthorizedException())));
    }});

    Reply<?> actual = service.returnCurrentUser();

    assertThat(actual, isUnauthorized());
  }

  @Test
  public void successfulTransfer() {
    Query query = new Query("John", "Bob", 10d);
    FakeRequest request = new FakeRequest(query);

    context.checking(new Expectations() {{
      oneOf(accountRepository).findByUserName("John");
      will(returnValue(Optional.of(new User("123", "John", "pass"))));

      oneOf(accountRepository).findByUserName("Bob");
      will(returnValue(Optional.of(new User("321", "Bob", "ssap"))));

      oneOf(accountRepository).findAccountByID("123");
      will(returnValue(Optional.of(new Account("123", "John", 50d))));

      oneOf(accountRepository).findAccountByID("321");
      will(returnValue(Optional.of(new Account("321", "Bob", 50d))));

      oneOf(transferRepository).saveTransfer(with(any(Transfer.class)));

      oneOf(accountRepository).update("123", 40d, "withdraw", "10.0");
      oneOf(accountRepository).update("321", 60d, "deposit", "10.0");
    }});

    Reply<?> actual = service.executeTransfer(request);

    assertThat(actual, isOk());
    assertThat(actual, containsJson(JsonBuilder.aNewJson().add("balance", 40d)));
  }

  @Test
  public void unsuccessfulTransfer() {
    Query query = new Query("John", "Bob", 10d);
    FakeRequest request = new FakeRequest(query);

    context.checking(new Expectations() {{
      oneOf(accountRepository).findByUserName("John");
      will(returnValue(Optional.of(new User("123", "John", "pass"))));

      oneOf(accountRepository).findByUserName("Bob");
      will(returnValue(Optional.empty()));
    }});

    Reply<?> actual = service.executeTransfer(request);

    assertThat(actual, isBadRequest());
  }
}
