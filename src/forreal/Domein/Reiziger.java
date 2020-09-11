package forreal.Domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;
    private List<OV_Chipkaart> ovkaarten;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, LocalDate geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.ovkaarten = new ArrayList<>();
    }

    public List<OV_Chipkaart> getOvkaarten() {
        return ovkaarten;
    }

    public void addCard(OV_Chipkaart ovkaart){
        this.ovkaarten.add(ovkaart);
    };
    public void removeCard(OV_Chipkaart ovkaart){
        this.ovkaarten.remove(ovkaart);
    };

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
        return this.voorletters + (" " + this.tussenvoegsel + " ") + this.achternaam;
    }

    public String toString(){
        String reiziger = "Reiziger: #" + getId() + ", naam: " + getNaam() + ", geboren op: " + getGeboortedatum() + ". ";
        if(adres != null){
            reiziger = reiziger + "Woont op: " + getAdres().getStraat() + " " + getAdres().getHuisnummer() + ", " + getAdres().getPostcode() + " te " + getAdres().getWoonplaats();
        }
        String ovkaartBegin = "\nReiziger " + getNaam() + " bezit de volgende OV-Chip kaarten: \n";
        String kaarten = "";
        for(OV_Chipkaart kaart : ovkaarten){
            kaarten += "kaart " + kaart.getKaartnummer() + ": reist in klasse: " + kaart.getKlasse() + ", heeft saldo: " + kaart.getSaldo() + " euro en is geldig tot: " + kaart.getGeldig_tot() + ".\n";
        }
        if(kaarten == ""){
            kaarten = "--: Reiziger heeft nog geen kaarten in bezit. \n";
        }
        return reiziger + ovkaartBegin + kaarten;
    }
}
