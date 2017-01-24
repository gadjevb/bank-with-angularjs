package com.clouway.bank.core;

import java.util.Date;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class Transaction {
  public final String id;
  public final Date date;
  public final String userID;
  public final String type;
  public final Double amount;

  public Transaction(String id, Date date, String userID, String type, Double amount) {
    this.id = id;
    this.date = date;
    this.userID = userID;
    this.type = type;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Transaction that = (Transaction) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (date != null ? !date.equals(that.date) : that.date != null) return false;
    if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;
    return amount != null ? amount.equals(that.amount) : that.amount == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (userID != null ? userID.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
