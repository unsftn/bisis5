package bisis5.format;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UCoderCollection {

  private List<UCoder> coders;

  @JsonIgnore
  private Map<String, UCoder> nameMap;

  @JsonIgnore
  private Map<Integer, UCoder> typeMap;

  @JsonIgnore
  private String _id;

  public UCoderCollection() {
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public UCoder getCoder(String name) {
    return nameMap.get(name);
  }

  public UCoder getCoder(int type) {
    return typeMap.get(type);
  }

  public List<UCoder> getCoders() {
    return coders;
  }

  public void setCoders(List<UCoder> coders) {
    this.coders = coders;
    setUp();
  }

  public void setUp() {
    nameMap = new HashMap<>();
    typeMap = new HashMap<>();
    for (UCoder coder : coders) {
      nameMap.put(coder.getName(), coder);
      typeMap.put(coder.getType(), coder);
    }
  }

}
