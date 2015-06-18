package bisis5.webservice;

import bisis5.auth.User;

import javax.ws.rs.core.HttpHeaders;
import java.util.List;

/**
 * Utility metode za web servise.
 */
public class ServiceUtils {

  /**
   * Vraca User objekat za usera koji je poslao HTTP zahtev. User objekat
   * sadrzi samo username i password (ono sto se moze procitati iz Authorization
   * HTTP headera).
   * @param headers HttpHeaders objekat
   * @return Instancirani User sa username-om i password-om ili null
   */
  public static User getUser(HttpHeaders headers) {
    List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
    if (authHeaders == null)
      return null;
    if (authHeaders.size() == 0)
      return null;
    String line = authHeaders.get(0);
    User user = User.decode(line);
    return user;
  }

  /**
   * Vraca username za usera koji je poslao HTTP zahtev.
   * @param headers HttpHeaders objekat
   * @return Username ili null
   */
  public static String getUsername(HttpHeaders headers) {
    User user = getUser(headers);
    if (user == null)
      return null;
    return user.getUsername();
  }
}
