package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserAuthentication;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Post;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
@At("/login")
@Show("LoginPage.html")
public class LoginPage {
  private final HttpServletResponse response;
  private final UserAuthentication userAuthentication;
  private final SessionRepository sessionRepository;
  private boolean showErrorMessage = false;
  private String errorMessage;

  @Inject
  public LoginPage(Provider<HttpServletResponse> response, UserAuthentication userAuthentication, SessionRepository sessionRepository) {
    this.response = response.get();
    this.userAuthentication = userAuthentication;
    this.sessionRepository = sessionRepository;
  }

  public boolean isShowErrorMessage() {
    return showErrorMessage;
  }

  @Post
  public String login(Request request) {
    String name = request.param("name");
    String password = request.param("password");

    Optional<User> user = userAuthentication.authenticate(name);
    if (!user.isPresent()) {
      showErrorMessage = true;
      errorMessage = "Incorrect username.";
      return null;
    }

    if (!user.get().password.equals(password)) {
      showErrorMessage = true;
      errorMessage = "Incorrect password.";
      return null;
    }

    Session session = sessionRepository.startSession(LocalDateTime.now());
    Cookie cookie = new Cookie("SID", session.id());
    cookie.setMaxAge(session.durationInSeconds());

    response.addCookie(cookie);
    return "/";
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}