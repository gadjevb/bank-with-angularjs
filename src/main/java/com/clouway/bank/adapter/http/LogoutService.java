package com.clouway.bank.adapter.http;

import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserSecurity;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

import java.util.Optional;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
@Service
@At("/logout")
public class LogoutService {
    private final SessionRepository sessionRepository;
    private UserSecurity security;

    @Inject
    public LogoutService(SessionRepository sessionRepository, UserSecurity security) {
      this.sessionRepository = sessionRepository;
      this.security = security;
    }

    @Get
    public Reply<?> logout() {
      Optional<User> possibleUser = security.currentUser();
      Boolean status;

        if (possibleUser.isPresent()) {
          status = sessionRepository.terminateUserSession(possibleUser.get());
        } else {
          return Reply.saying().ok();
        }

        if (status) {
            return Reply.saying().ok();
        } else {
            return Reply.saying().status(400);
        }
    }

}
