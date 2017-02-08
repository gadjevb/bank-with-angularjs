package com.clouway.bank.core;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class TransferRecord {
  public final String id;
  public final String date;
  public final String receiverUserName;
  public final Double amount;

  public TransferRecord(String id, String date, String receiverUserName, Double amount) {
    this.id = id;
    this.date = date;
    this.receiverUserName = receiverUserName;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TransferRecord that = (TransferRecord) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (date != null ? !date.equals(that.date) : that.date != null) return false;
    if (receiverUserName != null ? !receiverUserName.equals(that.receiverUserName) : that.receiverUserName != null)
      return false;
    return amount != null ? amount.equals(that.amount) : that.amount == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (receiverUserName != null ? receiverUserName.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
