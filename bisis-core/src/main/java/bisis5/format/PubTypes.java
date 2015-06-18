package bisis5.format;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bisis5.util.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides access to UNIMARC format and publication type definitions.
 */
public class PubTypes {

  /**
   * Returns the UNIMARC format definition.
   */
  public static UFormat getFormat() {
    return format;
  }
  
  /**
   * Returns the number of publication type definitions.
   */
  public static int getPubTypeCount() {
    return pubTypes.size();
  }
  
  /** 
   * Returns the publication type definition. 
   */
  public static UFormat getPubType(int index) {	  
    return (UFormat)pubTypes.get(new Integer(index));
  }
  
  /** the list of publication type definitions */
  private static Map pubTypes = new HashMap();
  /** unimarc format definition */
  private static UFormat format;
  
  private static Log log = LogFactory.getLog(PubTypes.class.getName());

  static {
    try {
      log.info("Loading UNIMARC format definition");
      format = FormatFactory.getFormat(PubTypes.class.getResource(
          "/format/unimarc_sr.xml").openStream());
      log.info("Loading publication type definitions");
      String dirName = "/format";
      String[] files = FileUtils.listFiles(PubTypes.class, dirName);
      for (int i = 0; i < files.length; i++) {
        if (files[i].endsWith(".xml") && !files[i].contains("unimarc_")) {
          UFormat pubType = FormatFactory.getPubType(
              PubTypes.class.getResource(files[i]).openStream());
          pubTypes.put(new Integer(pubType.getPubType()), pubType);
          log.info("Publication type " + pubType.getName() + 
              " ("+pubType.getPubType()+") loaded.");
        }
      }
    } catch (IOException ex) {
      log.fatal(ex);
    }
  }
  
}
