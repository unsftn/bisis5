package bisis5.webclient;

import bisis5.auth.DemoUserFactory;
import bisis5.auth.User;
import bisis5.format.UCoderCollection;
import bisis5.storage.MongoDB;
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

/**
 * Created by branko on 1/5/15.
 */
public class CodersClientTest {

  CodersClient client;
  User loggedIn;

  @BeforeClass
  public void setUp() {
    // pokusaj da se prijavis
    User tryUser = DemoUserFactory.createBranko();
    UsersClient usersClient = new UsersClient();
    loggedIn = usersClient.get(tryUser);
    loggedIn.setLibrary("testcodersclient");

    DBCollection coll = MongoDB.getInstance().getHoldingsCoders(loggedIn.getLibrary());
    if (coll != null)
      coll.drop();
    coll = MongoDB.getInstance().getHoldingsCoders(loggedIn.getLibrary());
    try {
      String testHoldings = FileUtils.readFileToString(new File("src/test/resources/holdings-coders.json"), "UTF8");
      DBObject obj = (DBObject) JSON.parse(testHoldings);
      coll.insert(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }

    client = new CodersClient(loggedIn);
  }

  @AfterClass
  public void tearDown() {
    MongoDB.getInstance().getHoldingsCoders(loggedIn.getLibrary()).drop();
  }

  @Test
  public void testGetHoldingsCoders() {
    UCoderCollection coderCollection = client.getHoldingsCoders();
    Assert.assertNotNull(coderCollection);
    Assert.assertTrue(coderCollection.getCoders().size() > 0);
    Assert.assertTrue(coderCollection.getCoder(0).getName().equals("odeljenje"));
  }
}
