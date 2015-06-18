package bisis5.storage;

import bisis5.records.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

/**
 * Sadrzi metode za konverziju objektnog modela zapisa u MongoDB model i obrnuto.
 */
public class MongoRecordUtils {

  /**
   * Konvertuje MongoDB objekat u zapis.
   * @param obj Objekat koji se konvertuje
   * @return Konvertovani zapis
   */
  public static Record asRecord(BasicDBObject obj) {
    Record rec = new Record();
    rec.setRecordID(obj.getString("_id"));
    rec.setPubType(obj.getInt("pubType"));

    BasicDBList fields = (BasicDBList)obj.get("fields");
    for (BasicDBObject field : fields.toArray(new BasicDBObject[0]))
      rec.add(asField(field));

    BasicDBList primerci = (BasicDBList)obj.get("primerci");
    for (BasicDBObject primerak : primerci.toArray(new BasicDBObject[0]))
      rec.add(asPrimerak(primerak));

    BasicDBList godine = (BasicDBList)obj.get("godine");
    for (BasicDBObject godina : godine.toArray(new BasicDBObject[0]))
      rec.add(asGodina(godina));

    BasicDBList events = (BasicDBList)obj.get("events");
    for (BasicDBObject event : events.toArray(new BasicDBObject[0]))
      rec.add(asEvent(event));

    return rec;
  }

  /**
   * Konvertuje MongoDB objekat u polje.
   * @param obj Objekat koji se konvertuje
   * @return Konvertovano polje
   */
  public static Field asField(BasicDBObject obj) {
    Field f = new Field();
    f.setName(obj.getString("name"));
    f.setInd1(obj.getString("ind1").charAt(0));
    f.setInd2(obj.getString("ind2").charAt(0));
    BasicDBList subfields = (BasicDBList)obj.get("subfields");
    for (BasicDBObject subfield : subfields.toArray(new BasicDBObject[0]))
      f.add(asSubfield(subfield));
    return f;
  }

  /**
   * Konvertuje MongoDB objekat u potpolje
   * @param obj Objekat koji se konvertuje
   * @return Konvertovano potpolje
   */
  public static Subfield asSubfield(BasicDBObject obj) {
    Subfield sf = new Subfield();
    sf.setName(obj.getString("name").charAt(0));
    sf.setContent(obj.getString("content"));
    BasicDBList subsubfields = (BasicDBList)obj.get("subsubfields");
    if (subsubfields != null && subsubfields.size() > 0) {
      for (BasicDBObject subsubfield : subsubfields.toArray(new BasicDBObject[0]))
        sf.add(asSubsubfield(subsubfield));
    }
    BasicDBObject secField = (BasicDBObject)obj.get("secField");
    if (secField != null)
      sf.setSecField(asField(secField));
    return sf;
  }

  /**
   * Konvertuje MongoDB objekat u potpotpolje
   * @param obj Objekat koji se konvertuje
   * @return Konvertovano potpotpolje
   */
  public static Subsubfield asSubsubfield(BasicDBObject obj) {
    Subsubfield ssf = new Subsubfield();
    ssf.setName(obj.getString("name").charAt(0));
    ssf.setContent(obj.getString("content"));
    return ssf;
  }

  /**
   * Konvertuje MongoDB objekat u primerak.
   * @param obj Objekat koji se konvertuje
   * @return Konvertovani primerak
   */
  public static Primerak asPrimerak(BasicDBObject obj) {
    Primerak p = new Primerak();
    p.setPrimerakID(obj.getInt("primerakID"));
    p.setBrojRacuna(obj.getString("brojRacuna"));
    p.setDatumInventarisanja(obj.getDate("datumInventarisanja"));
    p.setDatumRacuna(obj.getDate("datumRacuna"));
    p.setDatumStatusa(obj.getDate("datumStatusa"));
    p.setDobavljac(obj.getString("dobavljac"));
    p.setDostupnost(obj.getString("dostupnost"));
    p.setFinansijer(obj.getString("finansijer"));
    p.setInvBroj(obj.getString("invBroj"));
    p.setInventator(obj.getString("inventator"));
    p.setNacinNabavke(obj.getString("nacinNabavke"));
    p.setCena(getBigDecimal(obj, "cena"));
    p.setNapomene(obj.getString("napomene"));
    p.setOdeljenje(obj.getString("odeljenje"));
    p.setPovez(obj.getString("povez"));
    p.setSigDublet(obj.getString("sigDublet"));
    p.setSigFormat(obj.getString("sigFormat"));
    p.setSigPodlokacija(obj.getString("sigPodlokacija"));
    p.setSigNumerusCurens(obj.getString("sigNumerusCurens"));
    p.setSigUDK(obj.getString("sigUDK"));
    p.setSigIntOznaka(obj.getString("sigIntOznaka"));
    p.setStanje(obj.getInt("stanje"));
    p.setStatus(obj.getString("status"));
    p.setUsmeravanje(obj.getString("usmeravanje"));
    p.setVersion(obj.getInt("version"));
    return p;
  }

  /**
   * Konvertuje MongoDB objekat u godinu.
   * @param obj Objekat koji se konvertuje
   * @return Konvertovana godina
   */
  public static Godina asGodina(BasicDBObject obj) {
    Godina g = new Godina();
    g.setGodinaID(obj.getInt("godinaID"));
    g.setBrojRacuna(obj.getString("brojRacuna"));
    g.setCena(getBigDecimal(obj, "cena"));
    g.setDatumInventarisanja(obj.getDate("datumInventarisanja"));
    g.setDatumRacuna(obj.getDate("datumRacuna"));
    g.setDobavljac(obj.getString("dobavljac"));
    g.setDostupnost(obj.getString("dostupnost"));
    g.setFinansijer(obj.getString("finansijer"));
    g.setInvBroj(obj.getString("invBroj"));
    g.setInventator(obj.getString("inventator"));
    g.setNacinNabavke(obj.getString("nacinNabavke"));
    g.setNapomene(obj.getString("napomene"));
    g.setOdeljenje(obj.getString("odeljenje"));
    g.setPovez(obj.getString("povez"));
    g.setSigDublet(obj.getString("sigDublet"));
    g.setSigFormat(obj.getString("sigFormat"));
    g.setSigPodlokacija(obj.getString("sigPodlokacija"));
    g.setSigNumerusCurens(obj.getString("sigNumerusCurens"));
    g.setSigUDK(obj.getString("sigUDK"));
    g.setSigIntOznaka(obj.getString("sigIntOznaka"));
    g.setGodina(obj.getString("godina"));
    g.setGodiste(obj.getString("godiste"));
    g.setBroj(obj.getString("broj"));

    BasicDBList sveske = (BasicDBList)obj.get("sveske");
    for (BasicDBObject sveska : sveske.toArray(new BasicDBObject[0]))
      g.add(asSveska(sveska));
    
    return g;
  }

  /**
   * Konvertuje MongoDB objekat u svesku.
   * @param obj Objekat koji se konvertuje
   * @return Konvertovana sveska
   */
  public static Sveska asSveska(BasicDBObject obj) {
    Sveska s = new Sveska();
    s.setSveskaID(obj.getInt("sveskaID"));
    s.setCena(getBigDecimal(obj, "cena"));
    s.setDatumStatusa(obj.getDate("datumStatusa"));
    s.setInvBroj(obj.getString("invBroj"));
    s.setInventator(obj.getString("inventator"));
    s.setStanje(obj.getInt("stanje"));
    s.setStatus(obj.getString("status"));
    s.setVersion(obj.getInt("version"));
    s.setBrojSveske(obj.getString("brojSveske"));
    s.setKnjiga(obj.getString("knjiga"));
    s.setSignatura(obj.getString("signatura"));
    return s;
  }

  /**
   * Konvertuje MongoDB objekat u element istorije izmena.
   * @param obj Objekat koji se konvertuje
   * @return Konvertovani element istorije izmena
   */
  public static ChangeEvent asEvent(BasicDBObject obj) {
    ChangeEvent event = new ChangeEvent();
    Author author = new Author();
    BasicDBObject objAuthor = (BasicDBObject)obj.get("author");
    author.setUsername(objAuthor.getString("username"));
    author.setInstitution(objAuthor.getString("institution"));
    event.setAuthor(author);
    event.setDate(obj.getDate("date"));
    event.setType(ChangeType.get(obj.getString("type")));
    return event;
  }

  /**
   * Konvertuje zapis u MongoDB objekat.
   * @param rec Zapis koji se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Record rec) {
    BasicDBObject obj = new BasicDBObject();
    if (rec.getRecordID() != null)
      obj.append("_id", new ObjectId(rec.getRecordID()));
    obj.append("pubType", rec.getPubType());

    // dodavanje polja/potpolja/potpotpolja
    BasicDBList fields = new BasicDBList();
    for (Field f: rec.getFields())
      fields.add(convert(f));
    obj.append("fields", fields);

    // dodavanje primeraka
    BasicDBList primerci = new BasicDBList();
    for (Primerak p : rec.getPrimerci())
      primerci.add(convert(p));
    obj.append("primerci", primerci);

    // dodavanje godina
    BasicDBList godine = new BasicDBList();
    for (Godina g : rec.getGodine())
      godine.add(convert(g));
    obj.append("godine", godine);

    // dodavanje istorije izmena
    BasicDBList events = new BasicDBList();
    for (ChangeEvent e : rec.getEvents())
      events.add(convert(e));
    obj.append("events", events);
    return obj;
  }

  /**
   * Konvertuje polje u MongoDB objekat.
   * @param f Polje koje se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Field f) {
    BasicDBObject field = new BasicDBObject();
    field.append("name", f.getName());
    field.append("ind1", f.getInd1());
    field.append("ind2", f.getInd2());
    BasicDBList subfields = new BasicDBList();
    for (Subfield sf : f.getSubfields())
      subfields.add(convert(sf));
    field.append("subfields", subfields);
    return field;
  }

  /**
   * Konvertuje potpolje u MongoDB objekat.
   * @param sf Potpolje koje se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Subfield sf) {
    BasicDBObject subfield = new BasicDBObject();
    subfield.append("name", sf.getName());
    subfield.append("content", sf.getContent());
    if (sf.getSecField() != null) {
      BasicDBObject secField = convert(sf.getSecField());
      subfield.append("secField", secField);
    }
    if (sf.getSubsubfieldCount() > 0) {
      BasicDBList subsubfields = new BasicDBList();
      for (Subsubfield ssf : sf.getSubsubfields())
        subsubfields.add(convert(ssf));
      subfield.append("subsubfields", subsubfields);
    }
    return subfield;
  }

  /**
   * Konvertuje potpotpolje u MongoDB objekat.
   * @param ssf Potpotpolje koje se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Subsubfield ssf) {
    BasicDBObject subsubfield = new BasicDBObject();
    subsubfield.append("name", ssf.getName());
    subsubfield.append("content", ssf.getContent());
    return subsubfield;
  }

  /**
   * Konvertuje primerak u MongoDB objekat.
   * @param p Primerak koji se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Primerak p) {
    BasicDBObject obj = new BasicDBObject();
    obj.append("primerakID", p.getPrimerakID());
    if (p.getInvBroj() != null)
      obj.append("invBroj", p.getInvBroj());
    if (p.getDatumRacuna() != null)
      obj.append("datumRacuna", p.getDatumRacuna());
    if (p.getBrojRacuna() != null)
      obj.append("brojRacuna", p.getBrojRacuna());
    if (p.getDobavljac() != null)
      obj.append("dobavljac", p.getDobavljac());
    if (p.getCena() != null)
      setBigDecimal(obj, "cena", p.getCena());
    if (p.getFinansijer() != null)
      obj.append("finansijer", p.getFinansijer());
    if (p.getUsmeravanje() != null)
      obj.append("usmeravanje", p.getUsmeravanje());
    if (p.getDatumInventarisanja() != null)
      obj.append("datumInventarisanja", p.getDatumInventarisanja());
    if (p.getSigFormat() != null)
      obj.append("sigFormat", p.getSigFormat());
    if (p.getSigPodlokacija() != null)
      obj.append("sigPodlokacija", p.getSigPodlokacija());
    if (p.getSigIntOznaka() != null)
      obj.append("sigIntOznaka", p.getSigIntOznaka());
    if (p.getSigNumerusCurens() != null)
      obj.append("sigNumerusCurens", p.getSigNumerusCurens());
    if (p.getSigDublet() != null)
      obj.append("sigDublet", p.getSigDublet());
    if (p.getSigUDK() != null)
      obj.append("sigUDK", p.getSigUDK());
    if (p.getPovez() != null)
      obj.append("povez", p.getPovez());
    if (p.getNacinNabavke() != null)
      obj.append("nacinNabavke", p.getNacinNabavke());
    if (p.getOdeljenje() != null)
      obj.append("odeljenje", p.getOdeljenje());
    if (p.getStatus() != null)
      obj.append("status", p.getStatus());
    if (p.getDatumStatusa() != null)
      obj.append("datumStatusa", p.getDatumStatusa());
    if (p.getInventator() != null)
      obj.append("inventator", p.getInventator());
    if (p.getDatumStatusa() != null)
      obj.append("datumStatusa", p.getDatumStatusa());
    obj.append("stanje", p.getStanje());
    if (p.getDostupnost() != null)
      obj.append("dostupnost", p.getDostupnost());
    if (p.getNapomene() != null)
      obj.append("napomene", p.getNapomene());
    obj.append("version", p.getVersion());
    return obj;
  }

  /**
   * Konvertuje Godinu u MongoDB objekat.
   * @param g Godina koja se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Godina g) {
    BasicDBObject obj = new BasicDBObject();
    obj.append("godinaID", g.getGodinaID());
    if (g.getInvBroj() != null)
      obj.append("invBroj", g.getInvBroj());
    if (g.getDatumRacuna() != null)
      obj.append("datumRacuna", g.getDatumRacuna());
    if (g.getBrojRacuna() != null)
      obj.append("brojRacuna", g.getBrojRacuna());
    if (g.getDobavljac() != null)
      obj.append("dobavljac", g.getDobavljac());
    if (g.getCena() != null)
      setBigDecimal(obj, "cena", g.getCena());
    if (g.getFinansijer() != null)
      obj.append("finansijer", g.getFinansijer());
    if (g.getDatumInventarisanja() != null)
      obj.append("datumInventarisanja", g.getDatumInventarisanja());
    if (g.getSigFormat() != null)
      obj.append("sigFormat", g.getSigFormat());
    if (g.getSigPodlokacija() != null)
      obj.append("sigPodlokacija", g.getSigPodlokacija());
    if (g.getSigIntOznaka() != null)
      obj.append("sigIntOznaka", g.getSigIntOznaka());
    if (g.getSigNumerusCurens() != null)
      obj.append("sigNumerusCurens", g.getSigNumerusCurens());
    if (g.getSigDublet() != null)
      obj.append("sigDublet", g.getSigDublet());
    if (g.getSigUDK() != null)
      obj.append("sigUDK", g.getSigUDK());
    if (g.getPovez() != null)
      obj.append("povez", g.getPovez());
    if (g.getNacinNabavke() != null)
      obj.append("nacinNabavke", g.getNacinNabavke());
    if (g.getOdeljenje() != null)
      obj.append("odeljenje", g.getOdeljenje());
    if (g.getInventator() != null)
      obj.append("inventator", g.getInventator());
    if (g.getDostupnost() != null)
      obj.append("dostupnost", g.getDostupnost());
    if (g.getNapomene() != null)
      obj.append("napomene", g.getNapomene());
    if (g.getGodiste() != null)
      obj.append("godiste", g.getGodiste());
    if (g.getGodina() != null)
      obj.append("godina", g.getGodina());
    if (g.getBroj() != null)
      obj.append("broj", g.getBroj());
    BasicDBList sveske = new BasicDBList();
    for (Sveska s : g.getSveske())
      sveske.add(convert(s));
    obj.append("sveske", sveske);
    return obj;
  }

  /**
   * Konvertuje svesku u MongoDB objekat.
   * @param s Sveska koja se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(Sveska s) {
    BasicDBObject obj = new BasicDBObject();
    obj.append("sveskaID", s.getSveskaID());
    if (s.getInvBroj() != null)
      obj.append("invBroj", s.getInvBroj());
    if (s.getStatus() != null)
      obj.append("status", s.getStatus());
    if (s.getDatumStatusa() != null)
      obj.append("datumStatusa", s.getDatumStatusa());
    obj.append("stanje", s.getStanje());
    if (s.getSignatura() != null)
      obj.append("signatura", s.getSignatura());
    if (s.getCena() != null)
      setBigDecimal(obj, "cena", s.getCena());
    if (s.getBrojSveske() != null)
      obj.append("brojSveske", s.getBrojSveske());
    if (s.getKnjiga() != null)
      obj.append("knjiga", s.getKnjiga());
    if (s.getInventator() != null)
      obj.append("inventator", s.getInventator());
    obj.append("version", s.getVersion());
    return obj;
  }

  /**
   * Konvertuje element istorije izmena u MongoDB objekat.
   * @param e Element istorije izmena koji se konvertuje
   * @return Konvertovani objekat
   */
  public static BasicDBObject convert(ChangeEvent e) {
    BasicDBObject obj = new BasicDBObject();
    obj.append("author", new BasicDBObject().append("username", e.getAuthor().getUsername()).append("institution", e.getAuthor().getInstitution()));
    obj.append("date", e.getDate());
    obj.append("type", e.getType().toString());
    return obj;
  }

  /**
   * Smesta BigDecimal vrednost u MongoDB objekat pod datim kljucem.
   * @param obj Objekat u koji se dodaje
   * @param key Naziv atributa
   * @param value Vrednost koja se upisuje
   */
  public static void setBigDecimal(BasicDBObject obj, String key, BigDecimal value) {
    if (value != null)
      obj.append(key, value.movePointRight(2).intValue());
  }

  /**
   * Cita vrednost iz MongoDB objekta i konvertuje u BigDecimal.
   * @param obj Objekat iz koga se cita
   * @param key Naziv atributa
   * @return Konvertovana BigDecimal vrednost
   */
  public static BigDecimal getBigDecimal(BasicDBObject obj, String key) {
    try {
      int value = obj.getInt(key);
      return (new BigDecimal(value)).movePointLeft(2);
    } catch (Exception ex) {
      return null;
    }
  }
}
