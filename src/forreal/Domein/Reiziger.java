package forreal.Domein;

import forreal.SQL.ReizigersDAOPsql;

import java.sql.Date;
import java.time.LocalDate;


public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, String geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = LocalDate.parse(geboortedatum);
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
        return this.voorletters + " " + this.tussenvoegsel + " " + this.achternaam;
    }

    public String toString(){
        return "Reiziger: " + getId() + " met naam: " + getNaam() + " is geboren op: " + (this.geboortedatum != null ? this.geboortedatum : "not defined");
    }
}
