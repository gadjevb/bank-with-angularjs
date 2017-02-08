package com.clouway.bank.adapter.persistence;

import com.clouway.bank.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentTransferRepository implements TransferRepository {
  private final Provider<MongoDatabase> db;

  private MongoCollection<Document> transfers() {
    return db.get().getCollection("transfers");
  }

  @Inject
  public PersistentTransferRepository(Provider<MongoDatabase> db) {
    this.db = db;
  }

  @Override
  public void saveTransfer(Transfer transfer) {
    Document document = new Document();

    document.put("date", transfer.date);
    document.put("senderId", transfer.senderId);
    document.put("receiverId", transfer.receiverId);
    document.put("receiverUserName", transfer.receiverUserName);
    document.put("amount", transfer.amount);

    transfers().insertOne(document);
  }

  @Override
  public List<TransferRecord> retrieveTransfers(String userID, String startingFromCursor, Boolean isNext, Integer limit) {
    List<TransferRecord> records = new ArrayList();

    MongoCursor<Document> docCursor;

    //todo Refactor the duplicating code between this method and retrieveTransactions() method in PersistentTransactionRepository
    if(startingFromCursor.equals("")) {
      docCursor = transfers()
              .find(new Document("senderId", userID))
              .sort(new Document("_id", -1))
              .limit(limit).iterator();

    } else if(isNext) {
      docCursor = transfers()
              .find(new Document("senderId", userID))
              .filter(new Document("_id", new Document("$lt", new ObjectId(startingFromCursor))))
              .sort(new Document("_id", -1))
              .limit(limit).iterator();

    } else {
      docCursor = transfers()
              .find(new Document("senderId", userID))
              .filter(new Document("_id", new Document("$gt", new ObjectId(startingFromCursor))))
              .sort(new Document("_id", 1))
              .limit(limit).iterator();
    }

    while (docCursor.hasNext()) {
      Document document = docCursor.next();
      records.add(new TransferRecord(
              document.getObjectId("_id").toHexString(),
              new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(document.getDate("date")),
              document.getString("receiverUserName"),
              document.getDouble("amount")
      ));
    }

    if (!isNext && !startingFromCursor.equals("")) {
      Collections.reverse(records);
    }

    return records;
  }

}
