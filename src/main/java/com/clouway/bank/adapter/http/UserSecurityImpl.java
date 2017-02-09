package com.clouway.bank.adapter.http;

import com.clouway.bank.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class UserSecurityImpl implements UserSecurity {
  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  private final Provider<HttpServletRequest> requestProvider;

  @Inject
  public UserSecurityImpl(UserRepository userRepository, SessionRepository sessionRepository, Provider<HttpServletRequest> requestProvider) {
    this.userRepository = userRepository;
    this.requestProvider = requestProvider;
    this.sessionRepository = sessionRepository;
  }

  @Override
  public User currentUser() {
    Cookie currentCookie = null;
    for (Cookie cookie : requestProvider.get().getCookies()) {
      if (cookie.getName().equals("SID")) {
        currentCookie = cookie;
      }
    }

    if (currentCookie == null) {
      throw new UserNotAuthorizedException();
    }

    Optional<Session> session = sessionRepository.findSessionAvailableAt(currentCookie.getValue(), LocalDateTime.now());
    if (!session.isPresent()) {
      throw new UserNotAuthorizedException();
    }

    Optional<User> possibleUser = userRepository.findByUserName(session.get().getUsername());

    if (!possibleUser.isPresent()) {
      throw new UserNotAuthorizedException();
    }

    return possibleUser.get();
  }
}
