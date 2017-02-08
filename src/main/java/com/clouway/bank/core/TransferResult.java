package com.clouway.bank.core;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class TransferResult {
  public final Double balance;

  public TransferResult(Double balance) {
    this.balance = balance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TransferResult that = (TransferResult) o;

    return balance != null ? balance.equals(that.balance) : that.balance == null;
  }

  @Override
  public int hashCode() {
    return balance != null ? balance.hashCode() : 0;
  }
}
