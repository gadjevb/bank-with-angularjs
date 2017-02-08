package com.clouway.bank.core;

import java.util.Date;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class Transfer {
  public final String id;
  public final Date date;
  public final String senderId;
  public final String receiverId;
  public final String receiverUserName;
  public final Double amount;

  public Transfer(String id, Date date, String senderId, String receiverId, String receiverUserName, Double amount) {
    this.id = id;
    this.date = date;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.receiverUserName = receiverUserName;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Transfer transfer = (Transfer) o;

    if (id != null ? !id.equals(transfer.id) : transfer.id != null) return false;
    if (date != null ? !date.equals(transfer.date) : transfer.date != null) return false;
    if (senderId != null ? !senderId.equals(transfer.senderId) : transfer.senderId != null) return false;
    if (receiverId != null ? !receiverId.equals(transfer.receiverId) : transfer.receiverId != null) return false;
    if (receiverUserName != null ? !receiverUserName.equals(transfer.receiverUserName) : transfer.receiverUserName != null)
      return false;
    return amount != null ? amount.equals(transfer.amount) : transfer.amount == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
    result = 31 * result + (receiverId != null ? receiverId.hashCode() : 0);
    result = 31 * result + (receiverUserName != null ? receiverUserName.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
