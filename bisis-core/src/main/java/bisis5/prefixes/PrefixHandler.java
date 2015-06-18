package bisis5.prefixes;

import bisis5.records.Field;
import bisis5.records.Subfield;

/**
 * Represents a prefix handler responsible for handling details about
 * conversion of records to prefixes.
 * 
 * @author mbranko@uns.ns.ac.yu
 */
public interface PrefixHandler {

  /**
   * Concatenates subsubfields in a single string so that they can be
   * displayed as a prefix.
   * 
   * @param subfield Subfield to be processed.
   * @return The concatenated subsubfields.
   */
  public String concatenateSubsubfields(Subfield subfield);
  
  /**
   * Creates contents of the author (AU) prefix for the given field.
   * 
   * @param field Field to be processed.
   * @return The concatenated elements representing the name of the author.
   */
  public String [] createAuthor(Field field);
}
