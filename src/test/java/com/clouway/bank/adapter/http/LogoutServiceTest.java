package com.clouway.bank.adapter.http;

import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserSecurity;
import com.google.sitebricks.headless.Reply;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.clouway.bank.matchers.SitebricksMatchers.isBadRequest;
import static com.clouway.bank.matchers.SitebricksMatchers.isOk;
import static org.junit.Assert.assertThat;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class LogoutServiceTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private SessionRepository sessionRepository = context.mock(SessionRepository.class);
  private UserSecurity security = context.mock(UserSecurity.class);
  private LogoutService logoutService = new LogoutService(sessionRepository, security);

  @Test
  public void userTerminatesSession() {
    context.checking(new Expectations() {{
      oneOf(security).currentUser();
      will(returnValue(Optional.of(new User("123", "Borislav", "123456"))));

      oneOf(sessionRepository).terminateUserSession(with(any(User.class)));
      will(returnValue(Boolean.TRUE));
    }});

    Reply<?> actual = logoutService.logout();
    assertThat(actual, isOk());
  }

  @Test
  public void failAtTerminatingSession() {
    context.checking(new Expectations() {{
      oneOf(security).currentUser();
      will(returnValue(Optional.of(new User("123", "Borislav", "123456"))));

      oneOf(sessionRepository).terminateUserSession(with(any(User.class)));
      will(returnValue(Boolean.FALSE));
    }});

    Reply<?> actual = logoutService.logout();
    assertThat(actual, isBadRequest());
  }
}
