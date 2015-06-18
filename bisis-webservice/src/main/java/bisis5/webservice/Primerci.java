package bisis5.webservice;

import bisis5.records.Primerak;
import bisis5.records.Record;
import bisis5.storage.RecordStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Veb servis za manipulaciju primercima u okviru jednog zapisa.
 */
@Path("{library}/records/{recordId}/primerci")
public class Primerci {

  /**
   * Vraca primerak sa datim inventarnim brojem.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param invBroj Inventarni broj primerka
   * @return Primerak u JSON formatu ili HTTP 404 ako nije nadjen
   */
  @GET
  @Path("{invBroj}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPrimerak(
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
    Primerak p = rec.getPrimerak(invBroj);
    if (p == null) {
      builder = Response.status(404);
      return builder.build();
    }
    builder = Response.ok(p);
    builder.lastModified(rec.getLastEvent().getDate());
    return builder.build();
  }

  /**
   * Vraca sve primerke datog zapisa.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @return Lista primeraka u JSON formatu ili HTTP 404
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPrimerci(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(recordId);
    Response.ResponseBuilder builder;
    if (rec == null) {
      builder = Response.status(404);
      return builder.build();
    }
    builder = Response.ok(rec.getPrimerci());
    builder.lastModified(rec.getLastEvent().getDate());
    return builder.build();
  }

  /**
   * Dodaje novi primerak u postojeci zapis.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param p Primerak koji se dodaje
   * @return HTTP 201 sa URL-om novog primerka ili HTTP 404 ako zapis nije pronadjen
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId,
      Primerak p) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = new Record(recordId);
    String recId = storage.add(rec, p);
    Response.ResponseBuilder builder;
    if (recId == null) {
      builder = Response.status(404);
      return builder.build();
    }
    builder = Response.created(URI.create(library + "/records/" + recordId + "/primerci/" + p.getInvBroj()));
    return builder.build();
  }

  /**
   * Azurira dati primerak. Vraca HTTP 204 ako je bilo uspesno ili HTTP 404 ako primerak nije nadjen.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param invBroj Inventarni broj primerka koji se azurira
   * @param p Nova vrednost primerka
   */
  @PUT
  @Path("{invBroj}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void put(
      @PathParam("library") String library,
      @PathParam("recordId") String recordId,
      @PathParam("invBroj") String invBroj,
      Primerak p) {
    RecordStorage storage = new RecordStorage(library);
    Record rec = storage.get(recordId);
    if (rec == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    Primerak testP = rec.getPrimerak(invBroj);
    if (testP == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    rec.getPrimerci().remove(testP);
    rec.getPrimerci().add(p);
    storage.update(rec);
  }

  /**
   * Uklanja primerak iz zapisa. Vraca HTTP 204 ako je primerak uklonjen ili HTTP 404 ako nije nadjen.
   * @param library Oznaka biblioteke
   * @param recordId Identifikator zapisa
   * @param invBroj Inventarni broj primerka koji se uklanja
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
    Primerak testP = rec.getPrimerak(invBroj);
    if (testP == null)
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    rec.getPrimerci().remove(testP);
    storage.update(rec);
  }
}
