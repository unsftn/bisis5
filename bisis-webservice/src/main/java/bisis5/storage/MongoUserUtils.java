package bisis5.storage;

import bisis5.auth.User;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Sadrzi metode za konverziju objektnog modela korisnika u MongoDB model i obrnuto.
 */
public class MongoUserUtils {

  public static BasicDBObject convert(User user) {
    BasicDBObject obj = new BasicDBObject();
    obj.append("username", user.getUsername());
    obj.append("password", user.getPassword());
    obj.append("library", user.getLibrary());
    BasicDBList roles = new BasicDBList();
    for (String role: user.getRoles())
      roles.add(role);
    obj.append("roles", roles);
    return obj;
  }

  public static User convert(DBObject obj) {
    User user = new User();
    user.setUsername((String)obj.get("username"));
    user.setPassword((String)obj.get("password"));
    user.setLibrary((String)obj.get("library"));
    if (obj.get("roles") != null)
      user.setRoles(((BasicDBList)obj.get("roles")).toArray(new String[0]));
    return user;
  }
}
