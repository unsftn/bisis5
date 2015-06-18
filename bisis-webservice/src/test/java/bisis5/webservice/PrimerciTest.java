package bisis5.webservice;

import bisis5.records.DemoRecordFactory;
import bisis5.records.Primerak;
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

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class PrimerciTest {

  private final String ENDPOINT_ADDRESS = "local://testprimerci";
  private final String LIBRARY = "testprimerci";
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
    sf.setResourceClasses(Primerci.class);
    sf.setProviders(providers);
    sf.setResourceProvider(Primerci.class,
        new SingletonResourceProvider(new Primerci(), true));
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
  public void testGetPrimerak() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d/primerci/01000123456");
    Primerak p = client.get(Primerak.class);
    Assert.assertNotNull(p);
    Assert.assertEquals(p.getInvBroj(), "01000123456");
  }

  @Test(expectedExceptions = NotFoundException.class)
  public void testGetPrimerak2() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d/primerci/xyz"); // pogresan inv. broj
    client.get(Primerak.class);
  }

  @Test(dependsOnMethods = "testGetPrimerak")
  public void testGetPrimerci() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d/primerci");
    Primerak[] primerci = client.get(Primerak[].class);
    Assert.assertNotNull(primerci);
    Assert.assertEquals(primerci.length, 2);
    Assert.assertEquals(primerci[0].getInvBroj(), "01000123456");
  }

  @Test(expectedExceptions = NotFoundException.class)
  public void testGetPrimerci2() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/a546f35377c8d656fba84e8d/primerci"); // pogresan ID
    client.get(Primerak[].class);
  }

  @Test(dependsOnMethods = "testGetPrimerci")
  public void testAddPrimerak() {
    Primerak p = new Primerak(new Object[] {
        "primerakID", 3,
        "invBroj", "01000123458",
        "sigUDK", "72/76(031)",
        "odeljenje", "01",
        "status", "A",
        "povez", "m"});
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d/primerci");
    Response resp = client.post(p);
    Assert.assertEquals(resp.getStatus(), 201);
  }

  @Test(dependsOnMethods = "testGetPrimerci")
  public void testAddPrimerak2() {
    Primerak p = new Primerak(new Object[] {
        "primerakID", 3,
        "invBroj", "01000123458",
        "sigUDK", "72/76(031)",
        "odeljenje", "01",
        "status", "A",
        "povez", "m"});
    WebClient client = createClient();
    client.path(LIBRARY + "/records/a546f35377c8d656fba84e8d/primerci"); // pogresan ID
    Response resp = client.post(p);
    Assert.assertEquals(resp.getStatus(), 404);
  }

  @Test(dependsOnMethods = "testAddPrimerak")
  public void testPutPrimerak() {
    WebClient client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d/primerci/01000123456");
    Primerak updated = new Primerak(new Object[] {
        "primerakID", 1,
        "invBroj", "01000123456",
        "sigUDK", "72/76(031)",
        "odeljenje", "02", // update: odeljenje 01->02
        "status", "A",
        "povez", "t",      // update: povez m->t
        "cena", new BigDecimal(1350)});
    Response resp = client.put(updated);
    Assert.assertEquals(resp.getStatus(), 204);
    client = createClient();
    client.path(LIBRARY + "/records/54a6f35377c8d656fba84e8d/primerci/01000123456");
    Primerak test = client.get(Primerak.class);
    Assert.assertEquals(test.getOdeljenje(), "02");
    Assert.assertEquals(test.getPovez(), "t");
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
