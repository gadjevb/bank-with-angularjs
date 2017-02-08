package com.clouway.bank.adapter.persistence;

import com.clouway.bank.core.Transfer;
import com.clouway.bank.core.TransferRecord;
import com.clouway.bank.matchers.DatastoreRule;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentTransferRepositoryTest {

  @Rule
  public DatastoreRule datastoreRule = new DatastoreRule();

  private PersistentTransferRepository transferRepository;

  @Before
  public void setUp() {
    transferRepository = new PersistentTransferRepository(datastoreRule.getDatabase());
  }

  @Test
  public void retrieveFirstPage() {
    pretendThatExistingTransfersAre(generateTransactions(27));

    List<TransferRecord> firstPage = transferRepository.retrieveTransfers("::any user id::", "", true, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
  }

  @Test
  public void retrieveSecondPage() {
    pretendThatExistingTransfersAre(generateTransactions(27));

    List<TransferRecord> firstPage = transferRepository.retrieveTransfers("::any user id::", "", true, 20);
    List<TransferRecord> secondPage = transferRepository.retrieveTransfers("::any user id::", firstPage.get(firstPage.size() - 1).id, true, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
    assertThat(secondPage.size(), is(equalTo(7)));
  }

  @Test
  public void retrieveThirdPage() {
    pretendThatExistingTransfersAre(generateTransactions(47));

    List<TransferRecord> firstPage = transferRepository.retrieveTransfers("::any user id::", "", true, 20);
    List<TransferRecord> secondPage = transferRepository.retrieveTransfers("::any user id::", firstPage.get(firstPage.size() - 1).id, true, 20);
    List<TransferRecord> thirdPage = transferRepository.retrieveTransfers("::any user id::", secondPage.get(firstPage.size() - 1).id, true, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
    assertThat(secondPage.size(), is(equalTo(20)));
    assertThat(thirdPage.size(), is(equalTo(7)));
  }

  @Test
  public void returnToPreviousPage() {
    pretendThatExistingTransfersAre(generateTransactions(27));

    List<TransferRecord> firstPage = transferRepository.retrieveTransfers("::any user id::", "", true, 20);
    List<TransferRecord> secondPage = transferRepository.retrieveTransfers("::any user id::", firstPage.get(firstPage.size() - 1).id, true, 20);
    List<TransferRecord> previousPage = transferRepository.retrieveTransfers("::any user id::", secondPage.get(0).id, false, 20);

    assertThat(firstPage.size(), is(equalTo(20)));
    assertThat(secondPage.size(), is(equalTo(7)));
    assertThat(previousPage.size(), is(equalTo(20)));
    assertThat(firstPage, is(equalTo(previousPage)));
  }

  private List<Transfer> generateTransactions(int count) {
    List<Transfer> result = Lists.newArrayList();
    for (int i = 0; i < count; i++) {
      result.add(new Transfer(
              null,
              new Date(new Timestamp(System.currentTimeMillis()).getTime()),
              "::any user id::",
              "::any user id::",
              "::any user name::",
              (double) i));
    }
    return result;
  }

  private void pretendThatExistingTransfersAre(List<Transfer> transfers) {
    for (Transfer each : transfers) {
      transferRepository.saveTransfer(each);
    }
  }

}
