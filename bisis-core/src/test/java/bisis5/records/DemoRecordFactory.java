package bisis5.records;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Kreira demo zapise za testiranje.
 */
public class DemoRecordFactory {

  public static Record createMonografski1() {
    Record rec = new Record();
    rec.setRecordID("54a6f35377c8d656fba84e8d");
    rec.setPubType(1);
    rec.add(new ChangeEvent(new Author("mbranko", "uns"), new Date(), ChangeType.CREATE));
    rec.add(new Field("001")
        .add(new Subfield('7', "ba"))
        .add(new Subfield('a', "c"))
        .add(new Subfield('b', "a"))
        .add(new Subfield('c', "m"))
        .add(new Subfield('d', "0"))
        .add(new Subfield('e', "157659")));
    rec.add(new Field("010")
        .add(new Subfield('a', "86-83305-14-7")));
    rec.add(new Field("100")
        .add(new Subfield('c', "2004"))
        .add(new Subfield('h', "scr")));
    rec.add(new Field("102")
        .add(new Subfield('a', "yug")));
    rec.add(new Field("105")
        .add(new Subfield('a', "a")));
    rec.add(new Field("200", '0', ' ')
        .add(new Subfield('a', "Pojmovnik"))
        .add(new Subfield('e', "arhitektura, enterijer, dizajn, primenjena umetnost"))
        .add(new Subfield('f', "Radmila Milosavljevi\u0107, Marijana Milosavljevi\u0107")));
    rec.add(new Field("210")
        .add(new Subfield('a', "Beograd"))
        .add(new Subfield('c', "Orion Art"))
        .add(new Subfield('d', "2004")));
    rec.add(new Field("215")
        .add(new Subfield('a', "III, 163 str."))
        .add(new Subfield('c', "ilustr."))
        .add(new Subfield('d', "22 cm")));
    rec.add(new Field("320")
        .add(new Subfield('a', "Bibliografija: str. 162-163")));
    rec.add(new Field("606")
        .add(new Subfield('a', "ARHITEKTURA"))
        .add(new Subfield('w', "Leksikoni")));
    rec.add(new Field("606")
        .add(new Subfield('a', "UNUTRA\u0160NJA ARHITEKTURA"))
        .add(new Subfield('w', "Leksikoni")));
    rec.add(new Field("606")
        .add(new Subfield('a', "DIZAJN"))
        .add(new Subfield('w', "Leksikoni")));
    rec.add(new Field("606")
        .add(new Subfield('a', "PRIMENJENA UMETNOST"))
        .add(new Subfield('w', "Leksikoni")));
    rec.add(new Field("675")
        .add(new Subfield('a', "72/76(031)"))
        .add(new Subfield('b', "72")));
    rec.add(new Field("700", ' ', '1')
        .add(new Subfield('4', "070"))
        .add(new Subfield('a', "MILOSAVLJEVI\u010c"))
        .add(new Subfield('w', "Radmila")));
    rec.add(new Field("701", '1', '1')
        .add(new Subfield('4', "071"))
        .add(new Subfield('a', "MILOSAVLJEVI\u010c"))
        .add(new Subfield('w', "Marijana")));
    rec.add(new Primerak(new Object[] {
        "primerakID", 1,
        "invBroj", "01000123456",
        "sigUDK", "72/76(031)",
        "odeljenje", "01",
        "status", "A",
        "povez", "m",
        "cena", new BigDecimal(1350)}));
    rec.add(new Primerak(new Object[] {
        "primerakID", 2,
        "invBroj", "01000123457",
        "sigUDK", "72/76(031)",
        "odeljenje", "01",
        "status", "A",
        "povez", "m",
        "cena", new BigDecimal(1350)}));
    return rec;
  }

}
