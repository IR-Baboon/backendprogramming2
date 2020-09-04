package forreal.Domein;

import java.time.LocalDate;


public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;

    public Reiziger() {
    }

    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(String geboortedatum) {
        this.geboortedatum = LocalDate.parse(geboortedatum);
    }

    public String getNaam(){
        return this.voorletters +(tussenvoegsel != null ? " " + this.tussenvoegsel + " " : " " )+ this.achternaam;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("Reiziger #");
        str.append(getId());
        str.append(" met naam: ");
        str.append(getNaam());
        str.append(",");
        str.append(" is geboren op: ");
        str.append(getGeboortedatum());
        if(adres != null){
            str.append(" en woont op: ");
            str.append(getAdres().getStraat());
            str.append(getAdres().getHuisnummer());
            str.append(", ");
            str.append(getAdres().getPostcode());
            str.append(" ");
            str.append(getAdres().getWoonplaats());
            str.append(".");
        }

        return str.toString();
    }
}
