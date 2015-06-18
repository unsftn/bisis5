package bisis5.webservice;

import bisis5.storage.MongoDB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("{library}/coders")
public class Coders {

  @GET
  @Path("holdings")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("library") String library) {
    DBCollection holdingsCoders = MongoDB.getInstance().getHoldingsCoders(library);
    DBObject all = holdingsCoders.findOne();
    if (all == null) {
      return Response.status(404).build();
    }
    return Response.ok(all).build();
  }

}
