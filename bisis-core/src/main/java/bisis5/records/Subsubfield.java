package bisis5.records;

import java.io.Serializable;

/**
 * Predstavlja potpotpolje u MARC zapisu. Sastoji se iz naziva i sadrzaja.
 */
public class Subsubfield implements Serializable {

  /**
   * Naziv potpotpolja
   */
  private char name;

  /**
   * Sadrzaj potpotpolja
   */
  private String content;

  /**
   * Default constructor.
   */
  public Subsubfield() {
    name = ' ';
    content = "";
  }

  /**
   * Kreira potpotpolje sa datim nazivom i praznim sadrzajem.
   *
   * @param name Naziv potpotpolja
   */
  public Subsubfield(char name) {
    this.name = name;
    content = "";
  }

  /**
   * Vraca printabilnu reprezentaciju ovog potpotpolja.
   */
  public String toString() {
    StringBuilder retVal = new StringBuilder();
    retVal.append("<");
    retVal.append(name);
    retVal.append(">");
    retVal.append(content);
    return retVal.toString();
  }

  /**
   * Trimuje sadrzaj potpotpolja.
   */
  public void trim() {
    content = content.trim();
  }

  /**
   * @return Returns the content.
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content The content to set.
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return Returns the name.
   */
  public char getName() {
    return name;
  }

  /**
   * @param name The name to set.
   */
  public void setName(char name) {
    this.name = name;
  }
}