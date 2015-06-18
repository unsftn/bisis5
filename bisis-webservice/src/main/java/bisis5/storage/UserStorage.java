package bisis5.storage;

import bisis5.auth.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;

/**
 * Implementira vezu usera prema MongoDB=u.
 */
public class UserStorage {

  /**
   * Ucitava usera iz baze na osnovu username-a.
   * @param username Username trazenog usera
   * @return Ucitani user ili null ako nije nadjen
   */
  public User get(String username) {
    DBCollection useri = MongoDB.getInstance().getUsers();
    BasicDBObject query = new BasicDBObject("username", username);
    DBObject testUser = useri.findOne(query);
    if (testUser == null)
      return null;
    return MongoUserUtils.convert(testUser);
  }

  /**
   * Ucitava usera iz baze na osnovu MongoDB identifikatora.
   * @param id Identifikator trazenog usera
   * @return Ucitani user ili null ako nije nadjen
   */
  public User getById(String id) {
    DBCollection useri = MongoDB.getInstance().getUsers();
    BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
    DBObject testUser = useri.findOne(query);
    if (testUser == null)
      return null;
    return MongoUserUtils.convert(testUser);
  }

  /**
   * Dodaje novog usera u bazu. Proverava da li vec postoji user sa istim username-om.
   * @param user User koji se dodaje u bazu
   * @return Identifikator novog usera ili null ako je username zauzet
   */
  public String add(User user) {
    User testUser = get(user.getUsername());
    if (testUser != null)
      return null;
    DBCollection useri = MongoDB.getInstance().getUsers();
    BasicDBObject obj = MongoUserUtils.convert(user);
    useri.insert(obj);
    ObjectId id = (ObjectId)obj.get("_id");
    return id.toHexString();
  }

  /**
   * Azurira korisnika u bazi. Korisnik se trazi po username-u.
   * @param user Novi podaci za usera
   * @return Identifikator azuriranog usera ili null ako azuriranje nije obavljeno
   */
  public String update(User user) {
    DBCollection useri = MongoDB.getInstance().getUsers();
    BasicDBObject testQuery = new BasicDBObject("username", user.getUsername());
    DBObject testObj = useri.findOne(testQuery);
    if (testObj == null)
      return null;
    ObjectId id = (ObjectId) testObj.get("_id");
    BasicDBObject updateObj = MongoUserUtils.convert(user);
    BasicDBObject updateQuery = new BasicDBObject("_id", id);
    WriteResult result = useri.update(updateQuery, updateObj);
    if (result.getN() == 0)
      return null;
    return id.toHexString();
  }

  /**
   * Uklanja usera iz baze na osnovu username-a.
   * @param user User koji se uklanja (bitan je samo username property)
   * @return Identifikator uklonjenog usera ili null ako uklanjanje nije obavljeno
   */
  public String remove(User user) {
    DBCollection useri = MongoDB.getInstance().getUsers();
    BasicDBObject testQuery = new BasicDBObject("username", user.getUsername());
    DBObject testObj = useri.findOne(testQuery);
    if (testObj == null)
      return null;
    ObjectId id = (ObjectId) testObj.get("_id");
    BasicDBObject removeQuery = new BasicDBObject("_id", id);
    WriteResult result = useri.remove(removeQuery);
    if (result.getN() == 0)
      return null;
    return id.toHexString();
  }

  /**
   * Trazi korisnika u bazi na osnovu username-a i password-a.
   * @param username Username korisnika
   * @param password Password korisnika
   * @return Instancirani user ili null ako nije nadjen
   */
  public User login(String username, String password) {
    DBCollection useri = MongoDB.getInstance().getUsers();
    BasicDBObject query = new BasicDBObject().append("username", username).append("password", password);
    DBObject found = useri.findOne(query);
    if (found == null)
      return null;
    return MongoUserUtils.convert(found);
  }
}
