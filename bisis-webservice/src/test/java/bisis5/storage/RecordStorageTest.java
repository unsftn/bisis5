package bisis5.storage;

import bisis5.records.DemoRecordFactory;
import bisis5.records.Godina;
import bisis5.records.Primerak;
import bisis5.records.Record;
import com.mongodb.DBCollection;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RecordStorageTest {

  Record rec1;

  final String LIBRARY = "teststorage";

  @BeforeClass
  public void setUp() {
    DBCollection coll = MongoDB.getInstance().getZapisi(LIBRARY);
    coll.drop();
  }

  @Test
  public void testAddRecord() {
    rec1 = DemoRecordFactory.createMonografski1();
    RecordStorage storage = new RecordStorage(LIBRARY);
    storage.add(rec1);
    Assert.assertTrue(rec1.getRecordID() != null, "Nije azuriran recordID prilikom dodavanja.");
  }

  @Test(dependsOnMethods = "testAddRecord")
  public void testGetRecord() {
    RecordStorage storage = new RecordStorage(LIBRARY);
    Record testRec = storage.get(rec1.getRecordID());
    Assert.assertTrue(testRec.getFieldCount() > 0, "Polja nisu ucitana.");
    Assert.assertTrue(testRec.getPrimerci().size() > 0, "Primerci nisu ucitani.");
    Assert.assertTrue(testRec.getEvents().size() > 0, "Istorija izmena nije ucitana.");
  }

  @Test(dependsOnMethods = "testAddRecord")
  public void testUpdateRecord() {
    rec1.getEvents().get(0).getAuthor().setUsername("branko.milosavljevic");
    RecordStorage storage = new RecordStorage(LIBRARY);
    storage.update(rec1);
    Record testRec = storage.get(rec1.getRecordID());
    Assert.assertTrue(testRec.getEvents().get(0).getAuthor().getUsername().equals("branko.milosavljevic"));
  }

  @Test(dependsOnMethods = "testUpdateRecord")
  public void testAddPrimerak() {
    RecordStorage storage = new RecordStorage(LIBRARY);
    Primerak p = new Primerak(new Object[] {
        "primerakID", 3,
        "invBroj", "01000123458",
        "sigUDK", "72/76(031)",
        "odeljenje", "01",
        "status", "A",
        "povez", "m"});
    storage.add(rec1, p);
    Record testRec = storage.get(rec1.getRecordID());
    Assert.assertTrue(testRec.getPrimerci().size() == 3);
  }

  @Test(dependsOnMethods = "testAddPrimerak")
  public void testRemovePrimerak() {
    RecordStorage storage = new RecordStorage(LIBRARY);
    String recID = storage.remove(rec1, new Primerak(new Object[] { "invBroj", "01000123458"}));
    Record testRec = storage.get(recID);
    Assert.assertNotNull(testRec);
    Assert.assertTrue(testRec.getPrimerci().size() == 2);
  }

  @Test(dependsOnMethods = "testRemovePrimerak")
  public void testAddGodina() {
    RecordStorage storage = new RecordStorage(LIBRARY);
    Godina g = new Godina(new Object[] {
        "godinaID", 1,
        "invBroj", "02000123456",
        "sigUDK", "72/76(031)",
        "odeljenje", "02",
        "status", "A",
        "povez", "m"});
    storage.add(rec1, g);
    Record testRec = storage.get(rec1.getRecordID());
    Assert.assertTrue(testRec.getGodine().size() == 1);
  }

  @Test(dependsOnMethods = "testAddGodina")
  public void testRemoveGodina() {
    RecordStorage storage = new RecordStorage(LIBRARY);
    String recID = storage.remove(rec1, new Godina(new Object[] { "invBroj", "02000123456"}));
    Record testRec = storage.get(recID);
    Assert.assertNotNull(testRec);
    Assert.assertTrue(testRec.getGodine().size() == 0);
  }

  @Test(dependsOnMethods = "testRemoveGodina")
  public void testRemoveRecord() {
    RecordStorage storage = new RecordStorage(LIBRARY);
    Record toBeRemoved = new Record();
    toBeRemoved.setRecordID(rec1.getRecordID());
    storage.remove(toBeRemoved);
    Record testRec = storage.get(rec1.getRecordID());
    Assert.assertNull(testRec);
  }
}
