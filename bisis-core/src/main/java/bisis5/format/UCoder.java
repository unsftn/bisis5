package bisis5.format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UCoder implements Serializable {

  private int type;
  private String name;
  private List<UItem> items = new ArrayList<>();
  private boolean external;

  public UCoder() {
  }
  
  public UCoder(int type, String name, boolean external) {
    this.type = type;
    this.name = name;
    this.external = external;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public List<UItem> getItems() {
    return items;
  }
  public void setItems(List<UItem> values) {
    this.items = values;
  }
  public boolean isExternal() {
    return external;
  }
  public void setExternal(boolean external) {
    this.external = external;
  }
  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Retrieves a value by its code.
   * 
   * @param code The code to look up.
   * @return The corresponding value; null if not found.
   */
  public String getValue(String code) {
    String retVal = null;
    for (int i = 0; i < items.size(); i++) {
      UItem item = items.get(i);
      if (item.getCode().equals(code)) {
        retVal = item.getValue();
        break;
      }
    }
    return retVal;
  }
  
  /**
   * Retrieves a code by its value.
   * 
   * @param value The value to look up.
   * @return The corresponding code; null if not found.
   */
  public String getCode(String value) {
    String retVal = null;
    for (int i = 0; i < items.size(); i++) {
      UItem item = items.get(i);
      if (item.getValue().equals(value)) {
        retVal = item.getCode();
        break;
      }
    }
    return retVal;
  }

  /**
   * Retrieves a coder item by its code.
   * 
   * @param code The code to look up.
   * @return The corresponding coder item; null if not found.
   */
  public UItem getItem(String code) {
    UItem retVal = null;
    for (int i = 0; i < items.size(); i++) {
      UItem item = items.get(i);
      if (item.getCode().equals(code)) {
        retVal = item;
        break;
      }
    }
    return retVal;
  }

  /**
   * Adds an item to the coder list.
   * 
   * @param item The item to be added.
   */
  public void addItem(UItem item) {
    items.add(item);
  }

}
