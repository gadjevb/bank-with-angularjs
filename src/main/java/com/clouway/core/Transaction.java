package com.clouway.core;

import java.time.LocalDate;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class Transaction {
  public final LocalDate date;
  public final String user;
  public final String type;
  public final Double amount;

  public Transaction(LocalDate date, String user, String type, Double amount) {
    this.date = date;
    this.user = user;
    this.type = type;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Transaction that = (Transaction) o;

    if (date != null ? !date.equals(that.date) : that.date != null) return false;
    if (user != null ? !user.equals(that.user) : that.user != null) return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;
    return amount != null ? amount.equals(that.amount) : that.amount == null;
  }

  @Override
  public int hashCode() {
    int result = date != null ? date.hashCode() : 0;
    result = 31 * result + (user != null ? user.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
