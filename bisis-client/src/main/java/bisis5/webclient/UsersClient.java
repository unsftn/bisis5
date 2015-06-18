package bisis5.webclient;

import bisis5.auth.User;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.NotFoundException;

/**
 * Klijent za web servis za pristup korisnicima.
 */
public class UsersClient extends AbstractClient {

  /**
   * Kreira klijenta za pristup Users servisu u test modu.
   */
  public UsersClient() {
    super();
  }

  /**
   * Kreira klijenta za pristup Users servisu u test modu, sa
   * definisanim korisnikom za identifikaciju.
   * @param user Korisnik za identifikaciju
   */
  public UsersClient(User user) {
    super(user);
  }

  /**
   * Kreira klijenta za pristup Users servisu u remote modu.
   * @param url Adresa servera
   * @param user Korisnik za identifikaciju
   */
  public UsersClient(String url, User user) {
    super(url, user);
  }

  /**
   * Ucitava podatke o korisniku. Od podataka je neophodan samo username.
   * @param user User objekat sa inicijalizovanim username-om
   * @return Ucitani user objekat iz baze ili null ako nije nadjen
   */
  public User get(User user) {
    setUser(user);
    WebClient client = createClient();
    client.path(makePath("/users/" + user.getUsername()));
    try {
      User found = client.get(User.class);
      return found;
    } catch (NotFoundException ex) {
      return null;
    }
  }
}
