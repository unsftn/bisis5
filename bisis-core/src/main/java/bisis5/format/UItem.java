package bisis5.format;

import java.io.Serializable;

/**
 * Element sifarnika. Ima sifru, vrednost i fleg.
 */
public class UItem implements Serializable {

  public UItem() {
  }

  public UItem(String code, String value) {
    this.code = code;
    this.value = value;
  }

  public UItem(String code, String value, boolean flag) {
    this.code = code;
    this.value = value;
    this.flag = flag;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Boolean getFlag() {
    return flag;
  }

  public void setFlag(Boolean flag) {
    this.flag = flag;
  }

  @Override
  public String toString() {
    return code+"-"+value;
  }

  private String code;
  private String value;
  private Boolean flag;
}
