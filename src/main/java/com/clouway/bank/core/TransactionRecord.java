package com.clouway.bank.core;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class TransactionRecord {
  public final String id;
  public final String date;
  public final String type;
  public final Double amount;

  public TransactionRecord(String id, String date, String type, Double amount) {
    this.id = id;
    this.date = date;
    this.type = type;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TransactionRecord records = (TransactionRecord) o;

    if (id != null ? !id.equals(records.id) : records.id != null) return false;
    if (date != null ? !date.equals(records.date) : records.date != null) return false;
    if (type != null ? !type.equals(records.type) : records.type != null) return false;
    return amount != null ? amount.equals(records.amount) : records.amount == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
