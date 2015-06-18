package bisis5.auth;

import bisis5.storage.MongoDB;
import bisis5.storage.UserStorage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

/**
 * Servlet filter za autentifikaciju korisnika.
 */
public class AuthServletFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException { }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpServletResponse resp = (HttpServletResponse)response;
    String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (auth == null) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Korisnik nije autentifikovan (1).");
      return;
    }
    User user = User.decode(auth);
    if (user == null) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Korisnik nije autentifikovan (2).");
      return;
    }
    UserStorage storage = new UserStorage();
    if (storage.login(user.getUsername(), user.getPassword()) == null) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Korisnik nije autentifikovan (3).");
      return;
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() { }

}
