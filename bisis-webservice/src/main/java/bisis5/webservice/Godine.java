package bisis5.webservice;

import bisis5.records.Godina;
import bisis5.records.Record;
import bisis5.storage.RecordStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Veb servis za manipulaciju godinama u okviru jednog zapisa.
 */
@Path("{library}/records/{recordId}/godine")
public class Godine {

  /**
   * Vraca godinu sa datim inventarnim brojem.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param invBroj Inventarni broj trazene godine
   * @return Godina u JSON formatu ili HTTP 404 ako nije nadjena
   */
  @GET
  @Path("{invBroj}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGodina(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId,
      @PathParam("invBroj") String invBroj) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(recordId);
    Response.ResponseBuilder builder;
    if (rec == null) {
      builder = Response.status(404);
      return builder.build();
    }
    Godina g = rec.getGodina(invBroj);
    if (g == null) {
      builder = Response.status(404);
      return builder.build();
    }
    builder = Response.ok(g);
    builder.lastModified(rec.getLastEvent().getDate());
    return builder.build();
  }

  /**
   * Vraca sve godine datog zapisa.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @return Lista godina u JSON formatu ili HTTP 404
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGodine(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(recordId);
    Response.ResponseBuilder builder;
    if (rec == null) {
      builder = Response.status(404);
      return builder.build();
    }
    builder = Response.ok(rec.getGodine());
    builder.lastModified(rec.getLastEvent().getDate());
    return builder.build();
  }

  /**
   * Dodaje novu godinu u postojeci zapis.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param g Godina koja se dodaje
   * @return HTTP 201 sa URL-om nove godine ili HTTP 404 ako zapis nije pronadjen
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId,
      Godina g) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = new Record(recordId);
    String recId = storage.add(rec, g);
    Response.ResponseBuilder builder;
    if (recId == null) {
      builder = Response.status(404);
      return builder.build();
    }
    builder = Response.created(URI.create(library + "/bisis5/records/" + recordId + "/godine/" + g.getInvBroj()));
    return builder.build();
  }

  /**
   * Azurira datu godinu. Vraca HTTP 204 ako je bilo uspesno ili HTTP 404 ako primerak nije nadjen.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param invBroj Inventarni broj godine koja se azurira
   * @param g Nova vrednost godine
   */
  @PUT
  @Path("invBroj")
  @Consumes(MediaType.APPLICATION_JSON)
  public void put(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId,
      @PathParam("invBroj") String invBroj,
      Godina g) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(recordId);
    if (rec == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    Godina testG = rec.getGodina(invBroj);
    if (testG == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    rec.getGodine().remove(testG);
    rec.getGodine().add(g);
    storage.update(rec);
  }

  /**
   * Uklanja godinu iz zapisa. Vraca HTTP 204 ako je godina uklonjena ili HTTP 404 ako nije nadjena.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param invBroj Inventarni broj godine koja se uklanja
   */
  @DELETE
  @Path("{invBroj}")
  public void delete(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId,
      @PathParam("invBroj") String invBroj) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(recordId);
    if (rec == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    Godina testG = rec.getGodina(invBroj);
    if (testG == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    rec.getGodine().remove(testG);
    storage.update(rec);
  }

}
