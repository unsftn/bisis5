package bisis5.webservice;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Registruje web servis komponente na serveru.
 */
@ApplicationPath("/")
public class BisisApplication extends Application {

  private Set<Class<?>> classes = new HashSet<>();

  public BisisApplication() {
    classes.add(Root.class);
    classes.add(Users.class);
    classes.add(Records.class);
    classes.add(Primerci.class);
    classes.add(Godine.class);
    classes.add(Coders.class);
  }

  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }
}
