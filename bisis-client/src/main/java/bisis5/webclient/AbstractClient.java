package bisis5.webclient;

import bisis5.auth.User;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.local.LocalConduit;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClient {

  protected boolean testMode;
  protected String url;
  protected User user;

  public AbstractClient(boolean testMode) {
    setTestMode(testMode);
  }

  public AbstractClient() {
    setTestMode(true);
  }

  public AbstractClient(User user) {
    setTestMode(true);
    setUser(user);
  }

  public AbstractClient(String url, User user) {
    setTestMode(false);
    setUrl(url);
    setUser(user);
  }

  public void setUrl(String url) {
    if (url == null)
      return;
    if (!url.endsWith("/"))
      url += "/";
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean isTestMode() {
    return testMode;
  }

  public void setTestMode(boolean testMode) {
    this.testMode = testMode;
  }

  protected String makePath(String path) {
    if (testMode)
      return path;
    else
      return url + path;
  }

  protected WebClient createClient() {
    if (testMode)
      return createTestClient();
    else
      return createRemoteClient();
  }

  protected WebClient createRemoteClient() {
    List<Object> providers = new ArrayList<>();
    providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider());
    WebClient client = WebClient.create(url, providers);
    client.accept("application/json");
    client.type("application/json");
    client.header("Authorization", user.encode());
    return client;
  }

  protected WebClient createTestClient() {
    TestServer testServer = TestServer.getInstance();
    List<Object> providers = new ArrayList<>();
    providers.add(new com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider());
    WebClient client = WebClient.create(testServer.TEST_ENDPOINT_ADDRESS, providers);
    WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);
    client.accept("application/json");
    client.type("application/json");
    return client;
  }
}
