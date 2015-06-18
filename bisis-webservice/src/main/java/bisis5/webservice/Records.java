package bisis5.webservice;

import bisis5.records.Author;
import bisis5.records.Record;
import bisis5.storage.RecordStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Veb servis za manipulaciju zapisima.
 */
@Path("/{library}/records")
public class Records {

  @Context
  HttpHeaders httpHeaders;

  /**
   * Citanje zapisa iz baze.
   * @param library Oznaka biblioteke
   * @param id Identifikator zapisa koji se cita
   * @return Procitani zapis u JSON formatu ili HTTP 404 ako nije nadjen
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("library") String library, @PathParam("id") String id) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(id);
    Response.ResponseBuilder builder;
    if (rec == null) {
      builder = Response.status(404);
    } else {
      builder = Response.ok(rec);
      builder.lastModified(rec.getLastEvent().getDate());
    }
    return builder.build();
  }

  /**
   * Dodavanje novog zapisa.
   * @param library Oznaka biblioteke
   * @param rec Zapis koji se dodaje.
   * @return HTTP 201 sa URL-om novog zapisa ili HTTP 500 ako je greska
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(@PathParam("library") String library, Record rec) {
    RecordStorage storage = new RecordStorage(library);
    rec.addCreator(new Author(ServiceUtils.getUsername(httpHeaders), library));
    String id = storage.add(rec);
    Response.ResponseBuilder builder;
    if (id == null) {
      builder = Response.serverError();
    } else {
      builder = Response.created(URI.create(library + "/records/" + id));
    }
    return builder.build();
  }

  /**
   * Azuriranje postojeceg zapisa. Vraca HTTP 204 (uspesno) ili 404 (nije nadjen zapis).
   * @param library Oznaka biblioteke
   * @param id Identifikator zapisa koji se azurira
   * @param rec Nova verzija zapisa
   */
  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void put(@PathParam("library") String library, @PathParam("id") String id, Record rec) {
    rec.setRecordID(id);
    RecordStorage storage = new RecordStorage(library);
    rec.addModifier(new Author(ServiceUtils.getUsername(httpHeaders), library));
    String storedId = storage.update(rec);
    if (storedId == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }

  /**
   * Brisanje zapisa. Vraca HTTP 204 (uspesno) ili 404 (nije nadjen zapis).
   * @param library Oznaka biblioteke
   * @param id Identifikator zapisa koji se brise
   */
  @DELETE
  @Path("{id}")
  public void delete(@PathParam("library") String library, @PathParam("id") String id) {
    Record rec = new Record();
    rec.setRecordID(id);
    RecordStorage storage = new RecordStorage(library);
    String deletedId = storage.remove(rec);
    if (deletedId == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
}
