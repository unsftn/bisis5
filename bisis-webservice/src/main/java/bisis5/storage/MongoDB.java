package bisis5.storage;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Singleton veza sa MongoDB bazom.
 */
public class MongoDB {

  public static MongoDB getInstance() { return instance; }

  private static MongoDB instance;
  private MongoClient mongoClient;
  private DB db;

  static {
    instance = new MongoDB();
  }

  private MongoDB() {
    try {
      mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017)));
      db = mongoClient.getDB("bisis");
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    mongoClient.close();
  }

  public DBCollection getZapisi(String biblioteka) {
    return db.getCollection(biblioteka + ".bisis5.records");
  }

  public DBCollection getHoldingsCoders(String biblioteka) {
    return db.getCollection(biblioteka + ".coders.holdings");
  }

  public DBCollection getUsers() {
    return db.getCollection("users");
  }

}
