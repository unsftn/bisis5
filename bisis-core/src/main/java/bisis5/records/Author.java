package bisis5.records;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Represents a record author (a creator or a modifier).
 *  
 * @author mbranko@uns.ns.ac.yu
 */
public class Author implements Serializable {

  /** the librarian username */
  private String username;

  /** the name of the librarian's institution */
  private String institution;

  /**
   * Defaut constructor 
   */
  public Author() {
    this("", "");
    
  }
  
  /**
   * Constructs an author with the given username and institution.
   * @param username korisnicko ime bibliotekara
   * @param institution internet domen biblioteke
   */
  public Author(String username, String institution) {
    this.username = username;
    this.institution = institution;
  }
  
  /**
   * Constructs an author with the given username and institution stored in a
   * compact form: username@institution.
   * @param compact kompaktna reprezentacija
   */
  public Author(String compact) {
    String[] pieces = compact.split("@");
    if (pieces.length != 2)
      return;
    username = pieces[0];
    institution = pieces[1];
  }
  
  /**
   * Retrieves the compact author representation: username@institution.
   */
  @JsonIgnore
  public String getCompact() {
    return username + "@" + institution;
  }

  @Override
  public boolean equals(Object o) {
    Author other = (Author)o;
    return this.getCompact().equalsIgnoreCase(other.getCompact());
  }

  @Override
  public int hashCode() {
    return getCompact().hashCode();
  }

  /**
   * @return Returns the institution.
   */
  public String getInstitution() {
    return institution;
  }
  /**
   * @param institution The institution to set.
   */
  public void setInstitution(String institution) {
    this.institution = institution;
  }
  /**
   * @return Returns the username.
   */
  public String getUsername() {
    return username;
  }
  /**
   * @param username The username to set.
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
