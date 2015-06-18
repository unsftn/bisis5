package bisis5.webclient;

import bisis5.auth.User;
import bisis5.format.UCoderCollection;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.NotFoundException;

public class CodersClient extends AbstractClient {

  public CodersClient() {
    super();
  }

  public CodersClient(User user) {
    super(user);
  }

  public CodersClient(String url, User user) {
    super(url, user);
  }

  public UCoderCollection getHoldingsCoders() {
    WebClient client = createClient();
    client.path(makePath(user.getLibrary() + "/coders/holdings"));
    try {
      UCoderCollection coderCollection = client.get(UCoderCollection.class);
      return coderCollection;
    } catch (NotFoundException ex) {
      return null;
    }
  }
}
