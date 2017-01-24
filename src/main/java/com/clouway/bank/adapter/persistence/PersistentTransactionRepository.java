package com.clouway.bank.adapter.persistence;

import com.clouway.bank.core.TransactionRecord;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;
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
@SuppressWarnings("unchecked")
public class PersistentTransactionRepository implements TransactionRepository {
  private final Provider<MongoDatabase> db;

  private MongoCollection<Document> transactions() {
    return db.get().getCollection("transactions");
  }

  @Inject
  public PersistentTransactionRepository(Provider<MongoDatabase> db) {
    this.db = db;
  }

  @Override
  public void registerTransaction(Transaction transaction) {
    Document document = new Document();

    document.put("date", transaction.date);
    document.put("userID", transaction.userID);
    document.put("type", transaction.type);
    document.put("amount", transaction.amount);

    transactions().insertOne(document);
  }

  @Override
  public List<TransactionRecord> retrieveTransactions(String userID, String startingFromCursor, Boolean isNext, Integer limit) {
    List<TransactionRecord> records = new ArrayList();

    MongoCursor<Document> docCursor;

    if(startingFromCursor.equals("")) {
      docCursor = transactions()
              .find(new Document("userID", userID))
              .sort(new Document("_id", -1))
              .limit(limit).iterator();

    } else if(isNext) {
      docCursor = transactions()
              .find(new Document("userID", userID))
              .filter(new Document("_id", new Document("$lt", new ObjectId(startingFromCursor))))
              .sort(new Document("_id", -1))
              .limit(limit).iterator();

    } else {
      docCursor = transactions()
              .find(new Document("userID", userID))
              .filter(new Document("_id", new Document("$gt", new ObjectId(startingFromCursor))))
              .sort(new Document("_id", 1))
              .limit(limit).iterator();
    }

    while (docCursor.hasNext()) {
      Document document = docCursor.next();
      records.add(new TransactionRecord(
              document.getObjectId("_id").toHexString(),
              new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(document.getDate("date")),
              document.getString("type"),
              document.getDouble("amount")
      ));
    }

    if (!isNext && !startingFromCursor.equals("")) {
      Collections.reverse(records);
    }

    return records;
  }

}
