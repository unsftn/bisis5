package bisis5.auth;

/**
 * Proizvodi demo usere.
 */
public class DemoUserFactory {

  public static User createBranko() {
    User user = new User("branko", "branko", "teststorage");
    user.setRoles(new String[] { "editor", "circ", "reports", "admin"});
    return user;
  }

  public static User createZika() {
    User user = new User("zika", "zika", "teststorage");
    user.setRoles(new String[] { "editor", "circ", "reports", "admin"});
    return user;
  }
}
