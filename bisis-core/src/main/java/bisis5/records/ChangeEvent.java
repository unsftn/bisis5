package bisis5.records;

import java.util.Date;

/**
 * Predstavlja stavku u istoriji izmena zapisa.
 */
public class ChangeEvent {

  /**
   * Bibliotekar koji je autor izmene
   */
  private Author author;

  /**
   * Datum i vreme izmene
   */
  private Date date;

  /**
   * Vrsta izmene
   */
  private ChangeType type;

  public ChangeEvent() { }

  public ChangeEvent(Author author, Date date, ChangeType type) {
    this.author = author;
    this.date = date;
    this.type = type;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public ChangeType getType() {
    return type;
  }

  public void setType(ChangeType type) {
    this.type = type;
  }

  public ChangeEvent copy() {
    return new ChangeEvent(new Author(author.getUsername(), author.getInstitution()),  new Date(date.getTime()), type);
  }

  @Override
  public boolean equals(Object o) {
    ChangeEvent other = (ChangeEvent)o;
    return this.author.equals(other.author) && this.date.equals(other.date) && this.type == other.type;
  }

  @Override
  public int hashCode() {
    return author.hashCode() + date.hashCode() + type.hashCode();
  }
}
