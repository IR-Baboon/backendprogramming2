package forreal.Domein;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


public class Reiziger {
    private int reizigerId;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovkaarten;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reizigerId = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.ovkaarten = new ArrayList<>();
    }

    public List<OVChipkaart> getOvkaarten() {
        return ovkaarten;
    }

    public void addCard(OVChipkaart ovkaart){
        this.ovkaarten.add(ovkaart);
    };
    public void removeCard(OVChipkaart ovkaart){
        this.ovkaarten.remove(ovkaart);
    };

    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
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

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getNaam(){
        return this.voorletters + (" " + this.tussenvoegsel + " ") + this.achternaam;
    }

    public String toString(){
        String reiziger = "Reiziger: #" + getReizigerId() + ", naam: " + getNaam() + ", geboren op: " + getGeboortedatum() + ". ";
        if(adres != null){
            reiziger = reiziger + "Woont op: " + getAdres().getStraat() + " " + getAdres().getHuisnummer() + ", " + getAdres().getPostcode() + " te " + getAdres().getWoonplaats();
        }
        String ovkaartBegin = "\nReiziger " + getNaam() + " bezit de volgende OV-Chip kaarten: \n";
        String kaarten = "";
        for(OVChipkaart kaart : ovkaarten){
            kaarten += "kaart " + kaart.getKaartnummer() + ": reist in klasse: " + kaart.getKlasse() + ", heeft saldo: " + kaart.getSaldo() + " euro en is geldig tot: " + kaart.getGeldigTot() + ".\n";
        }
        if(kaarten == ""){
            kaarten = "--: Reiziger heeft nog geen kaarten in bezit. \n";
        }
        return reiziger + ovkaartBegin + kaarten;
    }
}
