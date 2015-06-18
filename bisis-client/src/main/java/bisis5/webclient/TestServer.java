package bisis5.webclient;

import bisis5.webservice.BisisApplication;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton embedded web server za testiranje.
 */
public class TestServer {

  public static TestServer getInstance() {
    if (instance == null)
      instance = new TestServer();
    return instance;
  }

  private static TestServer instance = null;

  private TestServer() {
    // koristi Jackson JSON provider
    List<Object> providers = new ArrayList<>();
    providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider());

    // web servis
    JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
    List<Class<?>> resourceClasses = new ArrayList<>(new BisisApplication().getClasses());
    sf.setResourceClasses(resourceClasses);
    sf.setProviders(providers);
    for (Class<?> clazz : resourceClasses)
      try {
        sf.setResourceProvider(clazz,
            new SingletonResourceProvider(clazz.newInstance(), true));
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    sf.setAddress(TEST_ENDPOINT_ADDRESS);
    server = sf.create();

  }

  public Server getServer() {
    return server;
  }

  public void stop() {
    server.stop();
    server.destroy();
  }

  public final String TEST_ENDPOINT_ADDRESS = "local://testbisis";
  private Server server;
}
