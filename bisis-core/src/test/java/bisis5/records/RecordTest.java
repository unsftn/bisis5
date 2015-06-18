package bisis5.records;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class RecordTest {

  Record rec1;

  @BeforeClass
  public void setUp() {
    rec1 = DemoRecordFactory.createMonografski1();
  }

  @Test
  public void testToJson() {
    String json = rec1.toPrettyJSON();
    Assert.assertTrue(json.length() > 0, "Record nije konvertovan u JSON");
  }

  @Test(dependsOnMethods = {"testToJson"})
  public void testFromJsonToJson() {
    String json = rec1.toJSON();
    Record recTest = Record.fromJson(json);
    Assert.assertTrue(rec1.getRecordID().equals(recTest.getRecordID()), "Recordi nemaju isti ID");
    Assert.assertTrue(rec1.getEvents().size() == recTest.getEvents().size(), "Recordi nemaju istu istoriju izmena");
    Assert.assertTrue(rec1.getPrimerci().size() == recTest.getPrimerci().size(), "Recordi nemaju istu listu primeraka");
  }

  @Test
  public void testFromJsonFile() {
    File file = new File(getClass().getResource("/demo-monografska-1.json").getPath());
    try {
      Record recTest = Record.fromJson(FileUtils.readFileToString(file, "UTF8"));
      Assert.assertTrue(recTest.getRecordID() != null, "Record ID je null");
      Assert.assertTrue(recTest.getPrimerci().size() > 0, "Lista primeraka je prazna");
    } catch (Exception e) {
      e.printStackTrace();
      Assert.assertFalse(false, e.toString());
    }
  }
}
