package bisis5.webclient;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility metode za webservis klijente.
 */
public class ClientUtils {

  public static String getLastPart(String uri) {
    return getLastPart(uri, "/");
  }

  public static String getLastPart(String text, String delimiter) {
    String clean = StringUtils.strip(text, "/");
    String[] parts = StringUtils.split(clean, delimiter);
    if (parts.length == 0)
      return null;
    return parts[parts.length - 1];
  }

  public static String getNthPart(String uri, int n) {
    return getNthPart(uri, "/", n);
  }

  public static String getNthPart(String text, String delimiter, int n) {
    String clean = StringUtils.strip(text, "/");
    String[] parts = StringUtils.split(clean, delimiter);
    if (parts.length <= n)
      return null;
    return parts[n];
  }
}
