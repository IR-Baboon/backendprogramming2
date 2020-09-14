package forreal.Domein;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    private List<Product> producten;

    public OVChipkaart(int kaartnummer, Date geldigTot, int klasse, double saldo) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.producten = new ArrayList<>();
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void addProduct(Product product) {
        this.producten.add(product);
    }
    public void removeProduct(Product product) {
        this.producten.remove(product);
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }


    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        String kaart = "kaartnummer: " + kaartnummer + ", geldig tot " + geldigTot + ", klasse: " + klasse + ", saldo: " + saldo  + "\n";
        if(reiziger!=null){
           kaart +=  " en staat op naam van: " + reiziger.getNaam() + "\n";
        }
        String productbegin = "Deze kaart bevat de volgende producten: \n";
        String producten = "";

        for(Product product : this.producten){
            producten += product.getProduct_nummer() + ": " + product.getNaam() + ", \n" + product.getBeschrijving() + "\nStatus: " + product.getStatus() + ". Last update:" + product.getLast_update() + ". Prijs: " + product.getPrijs();
        }
        if(producten == ""){
            producten = "--: Deze kaart heeft geen producten. \n";
        }
        return kaart + productbegin + producten;

    }
}
