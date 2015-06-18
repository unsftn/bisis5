package bisis5.storage;

import bisis5.auth.DemoUserFactory;
import bisis5.auth.User;
import com.mongodb.DBCollection;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserStorageTest {

  @BeforeClass
  public void setUp() {
    DBCollection useri = MongoDB.getInstance().getUsers();
    useri.drop();
    MongoDB.getInstance().getUsers(); // recreate collection
  }

  @Test
  public void testAddUser() {
    UserStorage storage = new UserStorage();
    User branko = DemoUserFactory.createBranko();
    String id = storage.add(branko);
    Assert.assertNotNull(id);
  }

  @Test(dependsOnMethods = "testAddUser")
  public void testAddUser2() {
    UserStorage storage = new UserStorage();
    User branko = DemoUserFactory.createBranko();
    String id = storage.add(branko);
    Assert.assertNull(id); // user vec postoji
  }

  @Test(dependsOnMethods = "testAddUser")
  public void testGetUser() {
    UserStorage storage = new UserStorage();
    User branko = DemoUserFactory.createBranko();
    User test = storage.get(branko.getUsername());
    Assert.assertNotNull(test);
  }

  @Test
  public void testGetUser2() {
    UserStorage storage = new UserStorage();
    User test = storage.get("mitar.miric");
    Assert.assertNull(test); // ne postoji user
  }

  @Test(dependsOnMethods = "testAddUser")
  public void testUpdateUser() {
    UserStorage storage = new UserStorage();
    User branko = DemoUserFactory.createBranko();
    branko.setPassword("oknarb");
    storage.update(branko);
    User test = storage.get(branko.getUsername());
    Assert.assertEquals(branko.getPassword(), test.getPassword());
    branko.setPassword("branko");
    storage.update(branko);
  }

  @Test
  public void testRemoveUser() {
    UserStorage storage = new UserStorage();
    User zika = DemoUserFactory.createZika();
    String id = storage.add(zika);
    Assert.assertNotNull(id);
    id = storage.remove(zika);
    Assert.assertNotNull(id);
  }

  @Test
  public void testRemoveUser2() {
    UserStorage storage = new UserStorage();
    User user = new User("mitar.miric", "xxx", "xxx");
    String id = storage.remove(user);
    Assert.assertNull(id); // ne postoji user
  }

  @Test
  public void testLogin() {
    UserStorage storage = new UserStorage();
    User branko = DemoUserFactory.createBranko();
    User test = storage.login(branko.getUsername(), branko.getPassword());
    Assert.assertNotNull(test); // morao je da ga pronadje
    Assert.assertEquals(branko, test);
  }


}
