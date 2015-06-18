package bisis5.records;

/**
 * Predstavlja moguce operacije nad zapisom koje se cuvaju u istoriji izmena.
 */
public enum ChangeType {
  CREATE("CREATE"), MODIFY("MODIFY"), DELETE("DELETE"),
  PRIMERAK_ADD("PRIMERAK_ADD"), PRIMERAK_MODIFY("PRIMERAK_MODIFY"), PRIMERAK_DELETE("PRIMERAK_DELETE"),
  GODINA_ADD("GODINA_ADD"), GODINA_MODIFY("GODINA_MODIFY"), GODINA_DELETE("GODINA_DELETE");

  private ChangeType(String s) {
    this.name = s;
  }

  private String name;

  public String toString() {
    return name;
  }

  public static ChangeType get(String typeName) {
    if (CREATE.name.equalsIgnoreCase(typeName))
      return CREATE;
    else if (MODIFY.name.equalsIgnoreCase(typeName))
      return MODIFY;
    else if (DELETE.name.equalsIgnoreCase(typeName))
      return DELETE;
    else if (PRIMERAK_ADD.name.equalsIgnoreCase(typeName))
      return PRIMERAK_ADD;
    else if (PRIMERAK_MODIFY.name.equalsIgnoreCase(typeName))
      return PRIMERAK_MODIFY;
    else if (PRIMERAK_DELETE.name.equalsIgnoreCase(typeName))
      return PRIMERAK_DELETE;
    else if (GODINA_ADD.name.equalsIgnoreCase(typeName))
      return GODINA_ADD;
    else if (GODINA_MODIFY.name.equalsIgnoreCase(typeName))
      return GODINA_MODIFY;
    else if (GODINA_DELETE.name.equalsIgnoreCase(typeName))
      return GODINA_DELETE;
    return null;
  }
}
