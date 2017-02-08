package com.clouway.bank.adapter.http;

import com.clouway.bank.core.*;
import com.google.inject.util.Providers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class UserSecurityImplTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private UserRepository userRepository = context.mock(UserRepository.class);
  private SessionRepository sessionRepository = context.mock(SessionRepository.class);
  private FakeHttpServletRequest request = new FakeHttpServletRequest();

  private UserSecurityImpl userSecurity = new UserSecurityImpl(userRepository, sessionRepository, Providers.of(request));

  @Test
  public void happyPath() throws Exception {
    request.addCookie(new Cookie("SID","value"));
    User user = new User("::any id::", "username", "::any pswd::");

    context.checking(new Expectations(){{
      oneOf(sessionRepository).findSessionAvailableAt(with(any(String.class)), with(any(LocalDateTime.class)));
      will(returnValue(Optional.of(new Session("value", 10,user.name))));
      oneOf(userRepository).findByUserName(user.name);
      will(returnValue(Optional.of(user)));
    }});

    User actual = userSecurity.currentUser();

    assertThat(actual,is(user));
  }

  @Test(expected = UserNotAuthorizedException.class)
  public void noPresentSession() throws Exception {
    request.addCookie(new Cookie("SID","id"));

    context.checking(new Expectations(){{
      oneOf(sessionRepository).findSessionAvailableAt(with(any(String.class)), with(any(LocalDateTime.class)));
      will(returnValue(Optional.empty()));
    }});

    userSecurity.currentUser();
  }

  @Test(expected = UserNotAuthorizedException.class)
  public void missingCookie() throws Exception {
    userSecurity.currentUser();
  }
}