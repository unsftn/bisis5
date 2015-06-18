package bisis5.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Sluzi samo da se TomEE ne buni sto nema klase koja hendla "/".
 */
@Path("/")
public class Root {

  @GET
  public Response get() {
    Response.ResponseBuilder builder = Response.ok("Root context ne vraca nista.");
    return builder.build();
  }
}
