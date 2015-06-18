package bisis5.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Predstavlja potpolje u MARC zapisu. Potpolje ima svoj naziv i sadrzaj.
 * Opciono, potpolje moze sadrzati potpotpolja.
 * Opciono, potpolje moze sadrzati sekundarno polje.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Subfield implements Serializable {

  /** Naziv potpolja */
  private char name;

  /** Sadrzaj potpolja; prazan string ako je potpolje prazno */
  private String content;

  /** Lista opcionih otpotpolja */
  private List<Subsubfield> subsubfields;

  /** Sekundarno polje sadrzano u ovom potpolju */
  private Field secField;

  /**
   * Default constructor.
   */
  public Subfield() {
    name = ' ';
    content = "";
    subsubfields = new ArrayList<>();
    secField = null;
  }

  /**
   * Kreira potpolje sa datim imenom i praznim sadrzajem.
   * @param name Ime potpolja
   */
  public Subfield(char name) {
    this.name = name;
    content = "";
    subsubfields = new ArrayList<>();
    secField = null;
  }

  /**
   * Kreira potpolje sa datim imenom i sadrzajem.
   * @param name Ime potpolja
   * @param content Sadrzaj potpolja
   */
  public Subfield(char name, String content) {
    this.name = name;
    this.content = content;
    subsubfields = new ArrayList<>();
    secField = null;
  }

  /**
   * Vraca broj potpotpolja u ovom potpolju.
   * @return Broj potpotpolja
   */
  @JsonIgnore
  public int getSubsubfieldCount() {
    return subsubfields.size();
  }

  /**
   * Vraca potpotpolje sa datim rednim brojem.
   * @param index Redni broj potpotpolja
   * @return Potpotpolje sa datim rednim brojem, ili null ako nije nadjeno
   */
  public Subsubfield getSubsubfield(int index) {
    if (index < subsubfields.size())
      return subsubfields.get(index);
    else
      return null;
  }

  /**
   * Vraca prvo potpotpolje sa datim imenom
   * @param name Ime potpotpolja
   * @return Prvo potpotpolje sa datim imenom ili null ako nije nadjeno
   */
  public Subsubfield getSubsubfield(char name) {
    for (Subsubfield ssf: subsubfields)
      if (ssf.getName() == name)
        return ssf;
    return null;
  }
  
  /**
   * Dodaje potpotpolje na kraj liste.
   * @param ssf Potpotpolje koje se dodaje
   * @return this
   */
  public Subfield add(Subsubfield ssf) {
    subsubfields.add(ssf);
    return this;
  }
  
  /**
   * Uklanja potpotpolje iz liste.
   * @param ssf Potpotpolje koje se uklanja
   * @return this
   */
  public Subfield remove(Subsubfield ssf) {
    subsubfields.remove(ssf);
    return this;
  }

  /**
   * Sortira listu potpotpolja.
   */
  public void sort() {
    if (subsubfields.size() > 0) {
      for (int i = 1; i < subsubfields.size(); i++) {
        for (int j = 0; j < subsubfields.size() - i; j++) {
          Subsubfield ssf1 = subsubfields.get(j);
          Subsubfield ssf2 = subsubfields.get(j+1);
          if (ssf1.getName() > ssf2.getName()) {
            subsubfields.set(j, ssf2);
            subsubfields.set(j+1, ssf1);
          }
        }
      }
    }
  }
  
  /**
   * Uklanja prazne sadrzaje iz sekundarnog polja ili potpotpolja.
   */
  public void pack() {
    if (secField != null) {
      secField.pack();
      if (secField.getSubfieldCount() == 0)
        secField = null;
    } else if (subsubfields.size() > 0) {
      Iterator<Subsubfield> it = subsubfields.iterator();
      while (it.hasNext()) {
        Subsubfield ssf = it.next();
        if (ssf.getContent().trim().equals(""))
          it.remove();
      }
    }
  }

  /**
   * Trimuje sadrzaj ovog potpolja ili sadrzanog sekundarnog polja ili potpotpolja.
   */
  public void trim() {
    if (secField != null) {
      secField.trim();
    } else if (subsubfields.size() > 0) {
      for (Subsubfield sf: subsubfields)
        sf.trim();
    } else {
      content = content.trim();
    }
  }

  /**
   * Vraca printabilnu reprezentaciju ovog potpolja.
   */
  public String toString() {
    StringBuilder retVal = new StringBuilder();
    retVal.append("[");
    retVal.append(name);
    retVal.append("]");
    if (secField != null) {
      retVal.append(secField.toString());
    } else if (subsubfields.size() > 0) {
      for (Subsubfield ssf: subsubfields) {
        retVal.append(ssf.toString());
      }
    } else {
      retVal.append(content);
    }
    return retVal.toString();
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
  /**
   * @return Returns the secField.
   */
  public Field getSecField() {
    return secField;
  }
  /**
   * @param secField The secField to set.
   */
  public void setSecField(Field secField) {
    this.secField = secField;
  }
  /**
   * @return Returns the subsubfields.
   */
  public List<Subsubfield> getSubsubfields() {
    return subsubfields;
  }
  /**
   * @param subsubfields The subsubfields to set.
   */
  public void setSubsubfields(List<Subsubfield> subsubfields) {
    this.subsubfields = subsubfields;
  }

  /**
   * Kreira kopiju potpolja.
   * @return Nova kopija potpolja
   */
  public Subfield copy(){
  	Subfield sf = new Subfield(name);
  	sf.setContent(content);
  	if(secField!=null)
  		sf.setSecField(secField.copy());
  	return sf;
  }
  
}