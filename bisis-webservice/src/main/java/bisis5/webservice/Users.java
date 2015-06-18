package bisis5.webservice;

import bisis5.storage.MongoDB;
import bisis5.auth.User;
import bisis5.storage.MongoUserUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class Users {

  @Context
  HttpHeaders httpHeaders;

  @GET
  @Path("{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("username") String username) {
    Response.ResponseBuilder builder;

    // dopustamo citanje samo podataka o samom sebi
    String login = ServiceUtils.getUsername(httpHeaders);
    // ako je login null onda smo dosli od lokalnog klijenta
    // u produkciji bi servlet filter presekao zahteve koji
    // nemaju ispravan Authorization header
    if (login != null && !login.equals(username)) {
      builder = Response.status(404);
      return builder.build();
    }

    DBCollection users = MongoDB.getInstance().getUsers();
    BasicDBObject query = new BasicDBObject().append("username", username);
    DBObject found = users.findOne(query);
    if (found == null) {
      builder = Response.status(404);
      return builder.build();
    }
    User user = MongoUserUtils.convert(found);
    builder = Response.ok(user);
    return builder.build();
  }
}
