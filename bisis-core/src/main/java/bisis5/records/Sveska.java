package bisis5.records;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Sveska implements Serializable {

  private int sveskaID;
  private String invBroj;
  private String status;
  private Date datumStatusa;
  private int stanje;
  private String signatura;
  private BigDecimal cena;
  private String brojSveske;
  private String knjiga;
  private String inventator;
  private Godina parent;
  private int version;

  public Sveska() {
    version = 0;
  }

  /**
   * Konstruise Svesku iz niza ciji elementi su naziv-propertija, vrednost-propertija, ...
   *
   * @param properties Niz vrednosti
   */
  public Sveska(Object[] properties) {
    this();
    for (int i = 0; i < properties.length; i += 2) {
      String propertyName = (String) properties[i];
      Object propertyValue = properties[i + 1];
      try {
        BeanUtils.copyProperty(this, propertyName, propertyValue);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public Sveska copy() {
    Sveska svRet = new Sveska();
    svRet.setBrojSveske(getBrojSveske());
    svRet.setCena(getCena());
    svRet.setDatumStatusa(getDatumStatusa());
    svRet.setInvBroj(getInvBroj());
    svRet.setParent(getParent());
    svRet.setSignatura(getSignatura());
    svRet.setStanje(getStanje());
    svRet.setStatus(getStatus());
    svRet.setSveskaID(getSveskaID());
    svRet.setVersion(getVersion());
    svRet.setKnjiga(getKnjiga());
    svRet.setInventator(getInventator());
    return svRet;
  }

  public String getBrojSveske() {
    return brojSveske;
  }

  public void setBrojSveske(String brojSveske) {
    this.brojSveske = brojSveske;
  }

  public BigDecimal getCena() {
    return cena;
  }

  public void setCena(BigDecimal cena) {
    this.cena = cena;
  }

  public String getInvBroj() {
    return invBroj;
  }

  public void setInvBroj(String invBroj) {
    this.invBroj = invBroj;
  }

  public String getSignatura() {
    return signatura;
  }

  public void setSignatura(String signatura) {
    this.signatura = signatura;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getDatumStatusa() {
    return datumStatusa;
  }

  public void setDatumStatusa(Date datumStatusa) {
    this.datumStatusa = datumStatusa;
  }

  @JsonIgnore
  public Godina getParent() {
    return parent;
  }

  @JsonIgnore
  public void setParent(Godina parent) {
    this.parent = parent;
  }

  public int getSveskaID() {
    return sveskaID;
  }

  public void setSveskaID(int sveskaID) {
    this.sveskaID = sveskaID;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public int getStanje() {
    return stanje;
  }

  public void setStanje(int stanje) {
    this.stanje = stanje;
  }

  public String getKnjiga() {
    return knjiga;
  }

  public void setKnjiga(String knjiga) {
    this.knjiga = knjiga;
  }

  public String getInventator() {
    return inventator;
  }

  public void setInventator(String inventator) {
    this.inventator = inventator;
  }

  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}


