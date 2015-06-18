package bisis5.records;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

/**
 * Predstavlja MARC zapis. Zapis sadrzi polja, primerke i godista, kao i metapodatke o kreiranju i poslednjoj izmeni.
 */
public class Record implements Serializable {

  /** Interni identifikator zapisa */
  private String recordID;

  /** Tip publikacije */
  private int pubType;

  /** Lista polja u zapisu */
  private List<Field> fields;

  /** Lista primeraka */
  private List<Primerak> primerci;

  /** Lista godista casopisa */
  private List<Godina> godine;

  /** Istorijat izmena nad zapisom */
  private List<ChangeEvent> events;

  /**
   * Konstruise prazan zapis.
   */
  public Record() {
    recordID = null;
    pubType = 0;
    fields = new ArrayList<>();
    primerci = new ArrayList<>();
    godine = new ArrayList<>();
    events = new ArrayList<>();
  }

  /**
   * Konstruise novi prazan zapis sa datim identifikatorom
   *
   * @param recordID Identifikator zapisa.
   */
  public Record(String recordID) {
    this.recordID = recordID;
    pubType = 0;
    fields = new ArrayList<>();
    primerci = new ArrayList<>();
    godine = new ArrayList<>();
    events = new ArrayList<>();
  }

  /**
   * Konstruise novi zapis sa datim identifikatorom, listom polja i listom primeraka.
   *
   * @param recordID Identifikator novog zapisa
   * @param fields   Lista polja
   * @param primerci Lista primeraka
   */
  public Record(String recordID, List<Field> fields, List<Primerak> primerci, List<Godina> godine) {
    this.recordID = recordID;
    this.pubType = 0;
    this.fields = fields;
    this.primerci = primerci;
    this.godine = godine;
    events = new ArrayList<>();
  }

  /**
   * Vraca broj polja u zapisu
   *
   * @return Broj polja u zapisu
   */
  @JsonIgnore
  public int getFieldCount() {
    return fields.size();
  }

  /**
   * Vraca polje sa datim rednim brojem u zapisu
   *
   * @param index Redni broj polja u zapisu
   * @return Polje sa datim rednim brojem, null ako ne postoji
   */
  public Field getField(int index) {
    if (index >= fields.size() || index < 0)
      return null;
    return fields.get(index);
  }

  /**
   * Vraca prvo polje sa datim imenom u zapisu.
   *
   * @param name Naziv polja
   * @return Prvo polje sa datim nazivom
   */
  public Field getField(String name) {
    for (Field field: fields)
      if (field.getName().equals(name))
        return field;
    return null;
  }

  /**
   * Vraca sva polja sa datim nazivom u zapisu.
   *
   * @param name Naziv polja
   * @return Lista svih polja sa datim nazivom
   */
  public List<Field> getFields(String name) {
    ArrayList<Field> retVal = new ArrayList<>();
    for (Field field: fields)
      if (field.getName().equals(name))
        retVal.add(field);
    return retVal;
  }

  /**
   * Vraca prvo potpolje prvog polja sa datim nazivom u zapisu. Ako prvo
   * nadjeno polje nema dato potpolje, nece ga traziti dalje.
   *
   * @param name Naziv potpolja
   * @return Pronadjeno potpolje ili null
   */
  public Subfield getSubfield(String name) {
    if (name.length() != 4)
      return null;
    String fieldName = name.substring(0, 3);
    Field f = getField(fieldName);
    if (f == null)
      return null;
    return f.getSubfield(name.charAt(3));
  }

  /**
   * Vraca sva potpolja sa datim nazivom u zapisu.
   *
   * @param name Naziv potpolja
   * @return Lista pronadjenih potpolja
   */
  public List<Subfield> getSubfields(String name) {
    List<Subfield> retVal = new ArrayList<>();
    if (name.length() != 4)
      return retVal;
    String fieldName = name.substring(0, 3);
    for (Field f : getFields(fieldName))
      for (Subfield sf : f.getSubfields(name.charAt(3)))
        retVal.add(sf);
    return retVal;
  }

  /**
   * Vraca sadrzaj prvog potpolja sa datim imenom.
   *
   * @param name Naziv prvog potpolja
   * @return Sadrzaj prvog potpolja ili null ako nije pronadjeno
   */
  public String getSubfieldContent(String name) {
    Subfield sf = getSubfield(name);
    if (sf == null)
      return null;
    else
      return sf.getContent();
  }

  /**
   * Vraca listu sadrzaja svih potpolja sa datim imenom.
   *
   * @param name Naziv potpolja
   * @return Lista sadrzaja svih potpolja sa datim imenom
   */
  public List<String> getSubfieldsContent(String name) {
    List<String> retVal = new ArrayList<>();
    for (Subfield sf : getSubfields(name))
      retVal.add(sf.getContent());
    return retVal;
  }

  /**
   * Dodaje polje na kraj zapisa.
   * @param field Polje koje treba dodati
   * @return this
   */
  public Record add(Field field) {
    fields.add(field);
    return this;
  }

  /**
   * Uklanja polje iz zapisa.
   * @param field Polje koje treba ukloniti
   * @return this
   */
  public Record remove(Field field) {
    fields.remove(field);
    return this;
  }

  /**
   * Dodaje primerak na kraj liste.
   * @param p Primerak koji se dodaje
   * @return this
   */
  public Record add(Primerak p) {
    primerci.add(p);
    return this;
  }

  /**
   * Uklanja primerak iz liste.
   * @param p Primerak koji se uklanja
   * @return this
   */
  public Record remove(Primerak p) {
    primerci.remove(p);
    return this;
  }

  /**
   * Dodaje godiste na kraj liste.
   * @param g Godiste koje se dodaje
   * @return this
   */
  public Record add(Godina g) {
    godine.add(g);
    return this;
  }

  /**
   * Uklanja godiste iz liste.
   * @param g Godiste koje se uklanja
   * @return this
   */
  public Record remove(Godina g) {
    godine.remove(g);
    return this;
  }

  /**
   * Sortira polja, potpolja i potpotpolja u zapisu.
   */
  public void sort() {
    for (int i = 1; i < fields.size(); i++) {
      for (int j = 0; j < fields.size() - i; j++) {
        Field f1 = fields.get(j);
        Field f2 = fields.get(j + 1);
        if (f1.getName().compareTo(f2.getName()) > 0) {
          fields.set(j, f2);
          fields.set(j + 1, f1);
        }
      }
    }
    for (Field f: fields)
      f.sort();
  }


  /**
   * Sortira polja u zapisu. Ne sortira potpolja u okviru polja.
   */
  public void sortFields() {
    for (int i = 1; i < getFields().size(); i++) {
      for (int j = 0; j < getFields().size() - i; j++) {
        Field f1 = getFields().get(j);
        Field f2 = getFields().get(j + 1);
        if (f1.getName().compareTo(f2.getName()) > 0) {
          getFields().set(j, f2);
          getFields().set(j + 1, f1);
        }
      }
    }
  }

  /**
   * Uklanja prazna polja, potpolja ili potpotpolja iz zapisa.
   */
  public void pack() {
    Iterator it = fields.iterator();
    while (it.hasNext()) {
      Field f = (Field) it.next();
      f.pack();
      if (f.getSubfieldCount() == 0)
        it.remove();
    }
  }

  /**
   * Uklanja whitespace iz svih sadrzaja u zapisu.
   *
   * @return Isti zapis sa trimovanim sadrzajima
   */
  public Record trim() {
    for (Field f: fields)
      f.trim();
    return this;
  }

  /**
   * Dodaje novi element u istoriji izmena.
   * @param event Novi element u istoriji izmena
   * @return this
   */
  public Record add(ChangeEvent event) {
    events.add(event);
    return this;
  }

  /**
   * Vraca printabilnu verziju zapisa
   */
  public String toString() {
    StringBuilder retVal = new StringBuilder();
    for (Field f : fields)
      retVal.append(f.toString());
    for (Primerak p : primerci)
      retVal.append(p.toString());
    for (Godina g : godine)
      retVal.append(g.toString());
    return retVal.toString();
  }

  /**
   * Vraca RN identifikator zapisa (potpolje 001e).
   *
   * @return RN identifikator zapisa
   */
  @JsonIgnore
  public int getRN() {
    try {
      return Integer.parseInt(getSubfieldContent("001e"));
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Postavlja vrednost RN identifikatora zapisa (potpolje 001e).
   *
   * @param rn nova vrednost RN identifikatora
   */
  @JsonIgnore
  public void setRN(int rn) {
    Field f001;
    Subfield sfRN;
    if (getField("001") == null) {
      f001 = new Field("001");
      add(f001);
    } else
      f001 = getField("001");
    if (f001.getSubfield('e') == null) {
      sfRN = new Subfield('e');
      f001.add(sfRN);
    } else
      sfRN = f001.getSubfield('e');
    sfRN.setContent(String.valueOf(rn));
  }

  /**
   * Vraca vrednost MR identifikatora (potpolje 4741).
   *
   * @return Vrednost MR identifikatora
   */
  @JsonIgnore
  public int getMR() {
    try {
      return Integer.parseInt(getSubfieldContent("4741"));
    } catch (Exception e) {
      return 0;
    }

  }

  /**
   * Postavlja vrednost MR identifikatora (potpolje 4741).
   *
   * @param mr Nova vrednost MR identifikatora
   */
  @JsonIgnore
  public void setMR(int mr) {
    Field f474 = getField("474");
    if (f474 == null) {
      f474 = new Field("474");
      add(f474);
    }
    if (f474.getSubfield('1') == null)
      f474.add(new Subfield('1', String.valueOf(mr)));
    else
      f474.getSubfield('1').setContent(String.valueOf(mr));
  }

  /**
   * Vraca primerak sa datim inventarnim brojem.
   *
   * @param invBroj inventarni broj trazenog primerka
   * @return Primerak sa datim inventarnim brojem ili null
   */
  public Primerak getPrimerak(String invBroj) {
    for (Primerak p : primerci)
      if (p.getInvBroj().equals(invBroj))
        return p;
    return null;
  }

  /**
   * Vraca godiste casopisa za dati inventarni broj sveske.
   *
   * @param invBrojSveske inventarni broj sveske
   * @return Trazeno godiste casopisa
   */
  public Godina getGodinaForInvBRSveske(String invBrojSveske) {
    for (Godina g : godine) {
      for (Sveska s : g.getSveske())
        if (s.getInvBroj().equals(invBrojSveske))
          return g;
    }
    return null;
  }

  /**
   * Vraca godiste casopisa za dati inventarni broj godista.
   *
   * @param invBroj inventarni broj godista
   * @return Trazeno godiste casopisa
   */
  public Godina getGodina(String invBroj) {
    for (Godina g : godine) {
      if (g.getInvBroj().equals(invBroj))
        return g;
    }
    return null;
  }

  /**
   * Vraca poslednji dogadjaj u istoriji izmena.
   * @return Poslednji dogadjaj u istoriji izmena ili null ako je istorija prazna
   */
  @JsonIgnore
  public ChangeEvent getLastEvent() {
    if (events.size() == 0)
      return null;
    int last = events.size() - 1;
    return events.get(last);
  }

  /**
   * Dodaje create event u istoriju izmena
   * @param author Autor izmene
   */
  public void addCreator(Author author) {
    events.add(new ChangeEvent(author, new Date(), ChangeType.CREATE));
  }

  /**
   * Dodaje modify event u istoriju izmena
   * @param author Autor izmene
   */
  public void addModifier(Author author) {
    events.add(new ChangeEvent(author, new Date(), ChangeType.MODIFY));
  }

  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> fields) {
    this.fields = fields;
  }

  public int getPubType() {
    return pubType;
  }

  public void setPubType(int pubType) {
    this.pubType = pubType;
  }

  public String getRecordID() {
    return recordID;
  }

  public void setRecordID(String recordID) {
    this.recordID = recordID;
  }

  public List<Primerak> getPrimerci() {
    return primerci;
  }

  public void setPrimerci(List<Primerak> primerci) {
    this.primerci = primerci;
  }

  public List<Godina> getGodine() {
    return godine;
  }

  public void setGodine(List<Godina> godine) {
    this.godine = godine;
  }

  public List<ChangeEvent> getEvents() { return events; }

  public void setEvents(List<ChangeEvent> events) { this.events = events; }

  /**
   * Kreira kopiju celog zapisa.
   *
   * @return Nova kopija zapisa
   */
  public Record copy() {
    Record rec = new Record();
    rec.setPubType(pubType);
    for (ChangeEvent e: events)
      rec.add(e.copy());
    for (Field f : fields)
      rec.add(f.copy());
    for (Primerak p : primerci)
      rec.getPrimerci().add(p.copy());
    for (Godina g : godine)
      rec.getGodine().add(g.copy());
    return rec;
  }

  /**
   * Kreira kopiju zapisa bez polja i inventara.
   *
   * @return Nova kopija zapisa
   */
  public Record shallowCopy() {
    Record rec = new Record();
    rec.setPubType(pubType);
    for (ChangeEvent e: events)
      rec.add(e.copy());
    return rec;
  }

  /**
   * Kreira kopiju zapisa bez inventara.
   *
   * @return Nova kopija zapisa
   */
  public Record copyWithoutHoldings() {
    Record rec = new Record();
    rec.setPubType(pubType);
    for (ChangeEvent e: events)
      rec.add(e.copy());
    for (Field f : fields)
      rec.add(f.copy());
    return rec;
  }

  /**
   * Vraca JSON reprezentaciju zapisa.
   * @return JSON reprezentacija zapisa
   */
  public String toJSON() {
    try {
      return jsonWriter.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "<error>";
    }
  }

  /**
   * Vraca JSON reprezentaciju zapisa pogodnu za citanje
   * @return JSON reprezentacija zapisa pogodna za citanje
   */
  public String toPrettyJSON() {
    try {
      return jsonPrettyWriter.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * Kreira zapis na osnovu JSON reprezentacije
   * @param json JSON reprezentacija zapisa
   * @return Kreirani zapis
   */
  public static Record fromJson(String json) {
    try {
      return jsonReader.readValue(json);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

  }

  private static ObjectReader jsonReader;
  private static ObjectWriter jsonWriter;
  private static ObjectWriter jsonPrettyWriter;

  static {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    jsonWriter = mapper.writer();
    jsonReader = mapper.reader(Record.class);
    jsonPrettyWriter = mapper.writer(SerializationFeature.INDENT_OUTPUT);
  }

}
