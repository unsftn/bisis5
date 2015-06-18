package bisis5.storage;

import bisis5.format.UCoderCollection;
import bisis5.util.JongoUtils;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.io.IOException;

/**
 * Pristup sifarnicima koji stoje u MongoDB-u.
 * TODO: nije zavrseno
 */
public class CodersStorage {

  public CodersStorage(String library) {
    this.library = library;
  }

  public UCoderCollection getHoldingsCoders() {
    DBCollection coll = MongoDB.getInstance().getHoldingsCoders(library);
    DBObject obj = coll.findOne();
    if (obj == null)
      return null;
    try {
      UCoderCollection coderCollection = JongoUtils.getPojo(obj, UCoderCollection.class);
      return coderCollection;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public String getLibrary() {
    return library;
  }

  public void setLibrary(String library) {
    this.library = library;
  }

  private String library;
}
