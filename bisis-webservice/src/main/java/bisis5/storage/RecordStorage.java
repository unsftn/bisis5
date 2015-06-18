package bisis5.storage;

import bisis5.records.Godina;
import bisis5.records.Primerak;
import bisis5.records.Record;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;

/**
 * Implementira vezu zapisa prema MongoDB-u.
 */
public class RecordStorage {

  /** Naziv kolekcije dodeljene datoj biblioteci u MongoDB bazi. */
  private String biblioteka;

  /**
   * Konstruise objekat za pristup podacima iz date biblioteke.
   * @param biblioteka oznaka biblioteke
   */
  public RecordStorage(String biblioteka) {
    this.biblioteka = biblioteka;
  }

  /**
   * Ucitava zapis iz baze na osnovu internog MongoDB identifikatora u hex formatu
   * @param id Identifikator zapisa koji treba ucitati
   * @return Ucitani zapis ili null
   */
  public Record get(String id) {
    ObjectId objId = new ObjectId(id);
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    BasicDBObject recObj = (BasicDBObject)coll.findOne(new BasicDBObject("_id", objId));
    if (recObj == null)
      return null;
    Record rec = MongoRecordUtils.asRecord(recObj);
    return rec;
  }

  /**
   * Dodaje novi zapis u bazi
   * @param rec Zapis koji treba dodati
   * @return Interni MongoDB identifikator novog zapisa u hex formatu
   */
  public String add(Record rec) {
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    BasicDBObject obj = MongoRecordUtils.convert(rec);
    coll.insert(obj);
    ObjectId id = (ObjectId)obj.get("_id");
    String hexId = id.toHexString();
    rec.setRecordID(hexId);
    return hexId;
  }

  /**
   * Azurira CEO ZAPIS u bazi, na osnovu identifikatora u zapisu koji se azurira
   * @param rec Zapis koji se azurira
   * @return Identifikator azuriranog zapisa ili null ako nije azuriran
   */
  public String update(Record rec) {
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    ObjectId id = new ObjectId(rec.getRecordID());
    BasicDBObject query = new BasicDBObject("_id", id);
    BasicDBObject obj = MongoRecordUtils.convert(rec);
    WriteResult result = coll.update(query, obj);
    if (result.getN() == 0)
      return null;
    return id.toHexString();
  }

  /**
   * Brise zapis iz baze, na osnovu identifikatora u datom zapisu. Na brisanje se ne
   * mora slati ceo zapis, dovoljno je da u njemu stoji samo recordID.
   * @param rec Zapis koji treba obrisati
   * @return Identifikator obrisanog zapisa ili null ako nije obrisan
   */
  public String remove(Record rec) {
    ObjectId id = new ObjectId(rec.getRecordID());
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    BasicDBObject obj = new BasicDBObject("_id", id);
    WriteResult result = coll.remove(obj);
    if (result.getN() == 0)
      return null;
    return id.toHexString();
  }

  /**
   * Dodaje dati primerak u postojeci zapis. Ne mora se slati ceo zapis, dovoljno
   * je da u njemu stoji samo recordID.
   * @param rec Zapis u koji se dodaje primerak
   * @param p Primerak koji se dodaje
   * @return Identifikator azuriranog zapisa ili null ako nije azuriran
   */
  public String add(Record rec, Primerak p) {
    BasicDBObject query = new BasicDBObject("_id", new ObjectId(rec.getRecordID()));
    BasicDBObject update = new BasicDBObject("$addToSet", new BasicDBObject("primerci", MongoRecordUtils.convert(p)));
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    WriteResult result = coll.update(query, update);
    if (result.getN() == 0)
      return null;
    return rec.getRecordID();
  }

  /**
   * Uklanja primerak sa datim ID-jem iz datog zapisa. Ne mora se slati ceo zapis,
   * niti ceo primerak, vec samo ID-evi.
   * @param rec Zapis u kome se nalazi primerak koji treba ukloniti
   * @param p Primerak koji treba ukloniti
   * @return Identifikator azuriranog zapisa ili null ako nije azuriran
   */
  public String remove(Record rec, Primerak p) {
    BasicDBObject query = new BasicDBObject("_id", new ObjectId(rec.getRecordID()));
    BasicDBObject update = new BasicDBObject("$pull", new BasicDBObject("primerci", new BasicDBObject("invBroj", p.getInvBroj())));
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    WriteResult result = coll.update(query, update);
    if (result.getN() == 0)
      return null;
    return rec.getRecordID();
  }

  /**
   * Dodaje datu godinu u postojeci zapis. Ne mora se slati ceo zapis, dovoljno
   * je da u njemu stoji samo recordID.
   * @param rec Zapis u koji se dodaje godina
   * @param g Godina koji se dodaje
   * @return Identifikator azuriranog zapisa ili null ako nije azuriran
   */
  public String add(Record rec, Godina g) {
    BasicDBObject query = new BasicDBObject("_id", new ObjectId(rec.getRecordID()));
    BasicDBObject update = new BasicDBObject("$addToSet", new BasicDBObject("godine", MongoRecordUtils.convert(g)));
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    WriteResult result = coll.update(query, update);
    if (result.getN() == 0)
      return null;
    return rec.getRecordID();
  }

  /**
   * Uklanja godinu sa datim ID-jem iz datog zapisa. Ne mora se slati ceo zapis,
   * niti cela godina, vec samo ID-evi.
   * @param rec Zapis u kome se nalazi godina koju treba ukloniti
   * @param g Godina koju treba ukloniti
   * @return Identifikator azuriranog zapisa ili null ako nije azuriran
   */
  public String remove(Record rec, Godina g) {
    BasicDBObject query = new BasicDBObject("_id", new ObjectId(rec.getRecordID()));
    BasicDBObject update = new BasicDBObject("$pull", new BasicDBObject("godine", new BasicDBObject("invBroj", g.getInvBroj())));
    DBCollection coll = MongoDB.getInstance().getZapisi(biblioteka);
    WriteResult result = coll.update(query, update);
    if (result.getN() == 0)
      return null;
    return rec.getRecordID();
  }

  /**
   * Zatvara bazu.
   */
  public void close() {
    MongoDB.getInstance().close();
  }

}
