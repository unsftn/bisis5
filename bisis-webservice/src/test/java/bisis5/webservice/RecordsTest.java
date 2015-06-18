package bisis5.webservice;

import bisis5.records.DemoRecordFactory;
import bisis5.records.Record;
import bisis5.storage.MongoDB;
import bisis5.storage.RecordStorage;
import com.mongodb.DBCollection;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class RecordsTest {

  private final String ENDPOINT_ADDRESS = "local://testzapisi";
  private final String LIBRARY = "testzapisi";
  private Server server;

  private Record rec1;

  @BeforeClass
  public void init() {
    DBCollection coll = MongoDB.getInstance().getZapisi(LIBRARY);
    if (coll != null)
      coll.drop();
    // snimi jedan demo zapis u bazu
    rec1 = DemoRecordFactory.createMonografski1();
    RecordStorage storage = new RecordStorage(LIBRARY);
    storage.add(rec1);

    // koristi Jackson JSON provider
    List<Object> providers = new ArrayList<>();
    providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider());

    // web servis
    JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
    sf.setResourceClasses(Records.class);
    sf.setProviders(providers);
    sf.setResourceProvider(Records.class,
      new SingletonResourceProvider(new Records(), true));
    sf.setAddress(ENDPOINT_ADDRESS);
    server = sf.create();
  }

  @AfterClass
  public void destroy() {
    server.stop();
    server.destroy();
    DBCollection coll = MongoDB.getInstance().getZapisi(LIBRARY);
    coll.drop();
  }

  @Test
  public void testGet() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d");
    Record rec = client.get(Record.class);
    Assert.assertNotNull(rec);
  }

  @Test
  public void testPost() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records");
    Record rec2 = DemoRecordFactory.createMonografski1();
    rec2.setRecordID(null);
    Response resp = client.post(rec2);
    Assert.assertEquals(resp.getStatus(), 201);
  }

  @Test
  public void testPut() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d");
    Record rec2 = DemoRecordFactory.createMonografski1();
    rec2.getSubfield("200a").setContent("Promenjen naslov");
    Response resp = client.put(rec2);
    Assert.assertEquals(resp.getStatus(), 204);
  }

  @Test(dependsOnMethods = {"testPost", "testPut"})
  public void testDelete() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d");
    Response resp = client.delete();
    Assert.assertEquals(resp.getStatus(), 204);
  }

  private WebClient createClient() {
    // koristi Jackson JSON provider
    List<Object> providers = new ArrayList<>();
    providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider());
    WebClient client = WebClient.create(ENDPOINT_ADDRESS, providers);
    WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);
    client.accept("application/json");
    client.type("application/json");
    return client;
  }
}
