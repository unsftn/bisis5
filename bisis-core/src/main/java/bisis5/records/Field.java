package bisis5.records;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Predstavlja polje u MARC zapisu. Polje ima prvi i drugi indikator i listu potpolja.
 */
public class Field implements Serializable {

  /** Naziv polja */
  private String name;

  /** Vrednost prvog indikatora */
  private char ind1;

  /** Vrednost drugog indikatora */
  private char ind2;

  /** Lista potpolja */
  private List<Subfield> subfields;

  /**
   * Default constructor.
   */
  public Field() {
    this("   ", ' ', ' ');
  }

  /**
   * Kreira polje sa datim imenom.
   * @param name Ime polja
   */
  public Field(String name) {
    this(name, ' ', ' ');
  }

  /**
   * Kreira polje sa datim imenom i identifikatorima.
   * @param name Naziv polja
   * @param ind1 Vrednost prvog identifikatora
   * @param ind2 Vrednost drugog identifikatora
   */
  public Field(String name, char ind1, char ind2) {
    this.name = name;
    this.ind1 = ind1;
    this.ind2 = ind2;
    subfields = new ArrayList<>();
  }

  /**
   * Vraca broj potpolja u ovom polju.
   * @return Broj potpolja
   */
  @JsonIgnore
  public int getSubfieldCount() {
    return subfields.size();
  }

  /**
   * Vraca potpolje sa datim rednim brojem.
   * @param index Redni broj potpolja
   * @return Trazeno potpolje ili null ako nije nadjeno
   */
  public Subfield getSubfield(int index) {
    if (index < subfields.size())
      return subfields.get(index);
    else
      return null;
  }

  /**
   * Vraca prvo potpolje sa datim nazivom.
   * @param name Naziv trazenog potpolja
   * @return Prvo potpolje sa datim nazivom ili null ako nije pronadjeno
   */
  public Subfield getSubfield(char name) {
    for (Subfield sf: subfields)
      if (sf.getName() == name)
        return sf;
    return null;
  }

  /**
   * Vraca sadrzaj prvog potpolja sa datim nazivom.
   * @param name Naziv trazenog potpolja
   * @return Sadrzaj prvog potpolja sa datim nazivom ili null ako nije pronadjeno
   */
  public String getSubfieldContent(char name) {
    if (getSubfield(name) != null)
      return getSubfield(name).getContent();
    return null;
  }

  /**
   * Vraca listu svih potpolja sa datim nazivom.
   *
   * @param name Naziv potpolja
   * @return Lista pronadjenih potpolja sa datim nazivom
   */
  public List<Subfield> getSubfields(char name) {
    List<Subfield> retVal = new ArrayList<>();
    for (Subfield sf: subfields)
      if (sf.getName() == name)
        retVal.add(sf);
    return retVal;
  }

  /**
   * Ispituje da li ovo polje sadrzi sekundarno polje u nekom od svojih potpolja.
   * @return true ako sadrzi sekundarno polje
   */
  @JsonIgnore
  public boolean containsSecondaryFields() {
    for (Subfield sf: subfields)
      if (sf.getSecField() != null)
        return true;
    return false;
  }

  /**
   * Vraca niz slova koja su nazivi potpolja, ukljucujuci i potpolja sekundarnog polja
   * @return Niz naziva potpolja
   */
  @JsonIgnore
  public String getSubfieldNames() {
    StringBuilder retVal = new StringBuilder();
    for (Subfield sf: subfields) {
      retVal.append(sf.getName());
      if (sf.getSecField() != null)
        retVal.append(sf.getSecField().getSubfieldNames());
    }
    return retVal.toString();
  }

  /**
   * Dodaje novo potpolje na kraj liste potpolja.
   * @param sf Potpolje koje se dodaje
   * @return this
   */
  public Field add(Subfield sf) {
    subfields.add(sf);
    return this;
  }

  /**
   * Uklanja potpolje iz liste.
   * @param sf Potpolje koje se uklanja
   * @return this
   */
  public Field remove(Subfield sf) {
    subfields.remove(sf);
    return this;
  }

  /**
   * Uklanja prvo potpolje sa datim imenom iz polja.
   * @param sfName Naziv potpolja koje se uklanja.
   */
  public void removeSubfield(char sfName) {
    Subfield sf = getSubfield(sfName);
    if (sf != null)
      remove(sf);
  }

  /**
   * Sortira potpolja u ovom polju.
   */
  public void sort() {
    for (int i = 1; i < subfields.size(); i++) {
      for (int j = 0; j < subfields.size() - i; j++) {
        Subfield sf1 = subfields.get(j);
        Subfield sf2 = subfields.get(j + 1);
        if (sf1.getName() > sf2.getName()) {
          subfields.set(j, sf2);
          subfields.set(j + 1, sf1);
        }
      }
    }
    for (Subfield sf: subfields)
      sf.sort();
  }

  /**
   * Uklanja sva prazna potpolja iz ovog polja.
   */
  public void pack() {
    Iterator<Subfield> it = subfields.iterator();
    while (it.hasNext()) {
      Subfield sf = it.next();
      sf.pack();
      if (sf.getSecField() == null && sf.getSubsubfieldCount() == 0 &&
          sf.getContent().trim().equals(""))
        it.remove();
    }
  }

  /**
   * Trimuje sadrzaj svih potpolja i potpotpolja.
   */
  public void trim() {
    for (Subfield sf: subfields)
      sf.trim();
  }

  /**
   * Vraca printabilnu reprezentaciju polja.
   */
  public String toString() {
    StringBuilder retVal = new StringBuilder();
    retVal.append("{");
    retVal.append(name);
    retVal.append(":");
    retVal.append(ind1);
    retVal.append(ind2);
    retVal.append(":");
    for (Subfield sf: subfields) {
      retVal.append(sf.toString());
    }
    retVal.append("}");
    return retVal.toString();
  }


  /**
   * @return Returns the ind1.
   */
  public char getInd1() {
    return ind1;
  }

  /**
   * @param ind1 The ind1 to set.
   */
  public void setInd1(char ind1) {
    this.ind1 = ind1;
  }

  /**
   * @return Returns the ind2.
   */
  public char getInd2() {
    return ind2;
  }

  /**
   * @param ind2 The ind2 to set.
   */
  public void setInd2(char ind2) {
    this.ind2 = ind2;
  }

  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return Returns the subfields.
   */
  public List<Subfield> getSubfields() {
    return subfields;
  }

  /**
   * @param subfields The subfields to set.
   */
  public void setSubfields(List<Subfield> subfields) {
    this.subfields = subfields;
  }

  /**
   * Vraca kopiju polja.
   * @return Kopija polja
   */
  public Field copy() {
    Field f = new Field(name);
    f.setInd1(ind1);
    f.setInd2(ind2);
    for (Subfield sf : subfields)
      f.add(sf.copy());
    return f;
  }
}