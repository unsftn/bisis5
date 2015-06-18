package bisis5.webclient;

import bisis5.records.Record;
import bisis5.auth.User;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * Klijent za web servis za manipulaciju zapisima.
 */
public class RecordsClient extends AbstractClient {

  /**
   * Kreira klijenta u test modu sa datim userom za identifikaciju.
   * @param user User za identifikaciju
   */
  public RecordsClient(User user) { super (user); }

  /**
   * Kreira klijenta u remote modu sa datim userom za identifikaciju.
   * @param url Adresa servera
   * @param user User za identifikaciju
   */
  public RecordsClient(String url, User user) { super(url, user); }

  /**
   * Ucitava zapis na osnovu identifikatora.
   * @param id Identifikator zapisa
   * @return Ucitani zapis ili null ako ucitavanje nije uspesno
   */
  public Record get(String id) {
    WebClient client = createClient();
    client.path(makePath(user.getLibrary() + "/records/" + id));
    try {
      Record rec = client.get(Record.class);
      return rec;
    } catch (NotFoundException ex) {
      return null;
    }
  }

  /**
   * Dodaje novi zapis u bazu.
   * @param rec Novi zapis koji se dodaje
   * @return Identifikator novog zapisa ili null ako dodavanje nije uspesno
   */
  public String add(Record rec) {
    WebClient client = createClient();
    client.path(makePath(user.getLibrary() + "/records"));
    rec.setRecordID(null);
    Response resp = client.post(rec);
    if (resp.getStatus() != 201)
      return null;
    String location = resp.getStringHeaders().getFirst("Location");
    String id = ClientUtils.getLastPart(location);
    rec.setRecordID(id);
    return id;
  }

  /**
   * Azurira zapis u bazi na osnovu njegovog identifikatora
   * @param rec Zapis koji se azurira
   * @return true ako je azuriranje uspesno
   */
  public boolean update(Record rec) {
    WebClient client = createClient();
    client.path(makePath(user.getLibrary() + "/records/" + rec.getRecordID()));
    Response resp = client.put(rec);
    return resp.getStatus() == 204;
  }

  /**
   * Uklanja zapis iz baze na osnovu njegovog identifikatora. Dovoljno je da u
   * datom zapisu pise samo recordID.
   * @param rec Zapis koji se uklanja
   * @return true ako je uklanjanje uspesno
   */
  public boolean remove(Record rec) {
    WebClient client = createClient();
    client.path(makePath(user.getLibrary() + "/records/" + rec.getRecordID()));
    Response resp = client.delete();
    return resp.getStatus() == 204;
  }
}
