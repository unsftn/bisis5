package bisis5.webclient;

import bisis5.auth.DemoUserFactory;
import bisis5.records.DemoRecordFactory;
import bisis5.records.Record;
import bisis5.storage.MongoDB;
import bisis5.storage.MongoRecordUtils;
import bisis5.auth.User;
import bisis5.storage.MongoUserUtils;
import com.mongodb.DBCollection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RecordsClientTest {

  RecordsClient client;
  Record rec1;

  @BeforeClass
  public void setUp() {
    // dodaj usera (preskace ako user postoji)
    DBCollection useri = MongoDB.getInstance().getUsers();
    User branko = DemoUserFactory.createBranko();
    useri.insert(MongoUserUtils.convert(branko));

    // dodaj zapis
    DBCollection zapisi = MongoDB.getInstance().getZapisi(branko.getLibrary());
    rec1 = DemoRecordFactory.createMonografski1();
    try {
      zapisi.insert(MongoRecordUtils.convert(rec1));
    } catch (Exception ex) {
      // drugi test je vec insertovao ovaj zapis
    }

    // pokusaj da se prijavis
    User tryUser = DemoUserFactory.createBranko();
    UsersClient usersClient = new UsersClient();
    User loggedIn = usersClient.get(tryUser);

    // zakaci se na zapise
    client = new RecordsClient(loggedIn);
  }

  @AfterClass
  public void tearDown() {
    User branko = DemoUserFactory.createBranko();
    DBCollection zapisi = MongoDB.getInstance().getZapisi(branko.getLibrary());
    zapisi.drop();
  }

  @Test
  public void testGet() {
    Record rec = client.get(rec1.getRecordID());
    Assert.assertNotNull(rec);
    Assert.assertEquals(rec.getRecordID(), rec1.getRecordID());
  }

  @Test
  public void testAdd() {
    Record rec = DemoRecordFactory.createMonografski1();
    String id = client.add(rec);
    Assert.assertNotNull(id);
  }

  @Test(dependsOnMethods = "testGet")
  public void testUpdate() {
    rec1.getSubfield("200a").setContent("Izmenjen naslov");
    boolean success = client.update(rec1);
    Assert.assertTrue(success);
    Record rec = client.get(rec1.getRecordID());
    Assert.assertEquals(rec1.getSubfieldContent("200a"), rec.getSubfieldContent("200a"));
  }

  @Test(dependsOnMethods = "testAdd")
  public void testRemove() {
    Record rec = DemoRecordFactory.createMonografski1();
    String id = client.add(rec);
    Assert.assertNotNull(id);
    Record rec2 = new Record(id);
    boolean success = client.remove(rec2);
    Assert.assertTrue(success);
  }

}
