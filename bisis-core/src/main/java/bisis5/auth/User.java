package bisis5.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Predstavlja korisnika u BISIS sistemu.
 */
public class User {

  private String username;
  private String password;
  private String library;
  private String[] roles;

  public User(String username, String password, String library) {
    this.username = username;
    this.password = password;
    this.library = library;
    this.roles = new String[0];
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.roles = new String[0];
  }

  public User() {
  }

  /**
   * Konvertuje Base64-kodirani string username:password u instancu Usera
   * koja ima inicijalizovane username i password atribute.
   * @param basicAuth Base-64 kodirani string oblika username:password
   * @return Instancirani User objekat
   */
  public static User decode(String basicAuth) {
    String decoded = new String(Base64.decodeBase64(basicAuth));
    if (decoded.length() < 3)
      return null;
    String[] parts = decoded.split(":");
    if (parts.length != 2)
      return null;
    return new User(parts[0], parts[1]);
  }

  /**
   * Generise Base64-kodirani string username:password od sadrzaja atributa
   * @return Base64-kodirani string
   */
  public String encode() {
    String auth = username + ":" + password;
    return new String(Base64.encodeBase64(auth.getBytes()));
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLibrary() {
    return library;
  }

  public void setLibrary(String library) {
    this.library = library;
  }

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (obj instanceof User) {
      User other = (User)obj;
      return new EqualsBuilder()
          .append(username, other.username)
          .append(password, other.password)
          .append(library, other.library)
          .append(roles, other.roles)
          .isEquals();
    } else
      return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(username)
        .append(password)
        .append(library)
        .toHashCode();
  }
}
