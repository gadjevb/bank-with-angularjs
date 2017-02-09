package com.clouway.bank.adapter.http;

import com.clouway.bank.core.*;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
@Service
@At("/v1/transfer")
public class TransferService {
  private final AccountRepository accountRepository;
  private final TransferRepository transferRepository;
  private UserSecurity security;
  private final String withdraw = "withdraw";
  private final String deposit = "deposit";

  @Inject
  public TransferService(UserSecurity security, AccountRepository accountRepository, TransferRepository transferRepository) {
    this.security = security;
    this.accountRepository = accountRepository;
    this.transferRepository = transferRepository;
  }

  @Get
  public Reply<?> returnCurrentUser() {
    User user = security.currentUser();
    Optional<Account> possibleAccount;

    possibleAccount = accountRepository.findAccountByID(user.id);

    if (possibleAccount.isPresent()) {
      return Reply.with(
              possibleAccount.get()
      ).as(Json.class);
    } else {
      return Reply.saying().unauthorized();
    }
  }

  @Post
  public Reply<?> executeTransfer(Request request) {
    Query query = request.read(Query.class).as(Json.class);

    Optional<User> sender = accountRepository.findByUserName(query.from);
    Optional<User> receiver = accountRepository.findByUserName(query.to);
    Optional<Account> senderAcc;
    Optional<Account> receiverAcc;

    if (sender.isPresent() && receiver.isPresent()) {
      senderAcc = accountRepository.findAccountByID(sender.get().id);
      receiverAcc = accountRepository.findAccountByID(receiver.get().id);
    } else {

      return Reply.saying().badRequest();

    }

    if (sender.isPresent() && receiver.isPresent() && (senderAcc.get().balance >= query.amount)) {

      Double senderNewBalance = senderAcc.get().balance - query.amount;
      Double receiverNewBalance = receiverAcc.get().balance + query.amount;

      transferRepository.saveTransfer(new Transfer(
              null,
              Date.from(Instant.now()),
              sender.get().id,
              receiver.get().id,
              receiver.get().name,
              query.amount
      ));

      accountRepository.update(sender.get().id, senderNewBalance, withdraw, query.amount.toString());
      accountRepository.update(receiver.get().id, receiverNewBalance, deposit, query.amount.toString());

      return Reply.with(
              new TransferResult(senderNewBalance)
      ).as(Json.class);

    } else {

      return Reply.saying().badRequest();

    }
  }

  public static class Query {
    public final String from;
    public final String to;
    public final Double amount;

    public Query(String from, String to, Double amount) {
      this.from = from;
      this.to = to;
      this.amount = amount;
    }
  }
}
