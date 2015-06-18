package bisis5.webservice;


import bisis5.format.UCoderCollection;
import bisis5.storage.MongoDB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodersTest {

  private final String ENDPOINT_ADDRESS = "local://testholdingscoders";
  private final String LIBRARY = "testholdingscoders";
  private Server server;

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

    // koristi Jackson JSON provider
    List<Object> providers = new ArrayList<>();
    providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider());

    // web servis
    JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
    sf.setResourceClasses(Coders.class);
    sf.setProviders(providers);
    sf.setResourceProvider(Coders.class,
        new SingletonResourceProvider(new Coders(), true));
    sf.setAddress(ENDPOINT_ADDRESS);
    server = sf.create();
  }

  @AfterClass
  public void destroy() {
    server.stop();
    server.destroy();
    MongoDB.getInstance().getHoldingsCoders(LIBRARY).drop();
  }

  @Test
  public void testGet() {
    WebClient client = createClient();
    client.path(LIBRARY + "/coders/holdings");
    UCoderCollection coderCollection = client.get(UCoderCollection.class);
    Assert.assertNotNull(coderCollection);
    Assert.assertTrue(coderCollection.getCoders().size() > 0);
    Assert.assertEquals(coderCollection.getCoder(0).getName(), "odeljenje");
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
