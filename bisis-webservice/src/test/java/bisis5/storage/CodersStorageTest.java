package bisis5.storage;

import bisis5.format.UCoderCollection;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class CodersStorageTest {

  private final String ENDPOINT_ADDRESS = "local://testholdingsstorage";
  private final String LIBRARY = "testholdingscoders";

  @BeforeClass
  public void init() {
    // insertuj test dokument u test kolekciju
    DBCollection coll = MongoDB.getInstance().getHoldingsCoders(LIBRARY);
    if (coll != null)
      coll.drop();
    coll = MongoDB.getInstance().getHoldingsCoders(LIBRARY);
    try {
      String testHoldings = FileUtils.readFileToString(new File("src/test/resources/holdings-coders.json"), "UTF8");
      DBObject obj = (DBObject) JSON.parse(testHoldings);
      coll.insert(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public void destroy() {
    DBCollection coll = MongoDB.getInstance().getHoldingsCoders(LIBRARY);
    coll.drop();
  }

  @Test
  public void testGetHoldingsCoders() {
    CodersStorage storage = new CodersStorage(LIBRARY);
    UCoderCollection coderCollection = storage.getHoldingsCoders();
    Assert.assertNotNull(coderCollection);
    Assert.assertNotNull(coderCollection.getCoder(0));
    Assert.assertNotNull(coderCollection.getCoder("odeljenje"));
  }
}
