package forreal.Domein;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaartnummer;
    private Calendar geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    private List<Product> producten;

    public OVChipkaart(int kaartnummer, Calendar geldigTot, int klasse, double saldo) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.producten = new ArrayList<>();
    }

    public List<Product> getProducten() {
        return producten;
    }
    public boolean addProduct(Product product) {
        return producten.add(product);
    }
    public boolean removeProduct(Product product) {
       return producten.removeIf(product1 -> product1.getProduct_nummer() == product.getProduct_nummer());
    }

    public boolean contains(Product product){
        for(Product product1: producten){
            if(product.getProduct_nummer() == product1.getProduct_nummer()){
                return true;
            }
        }
        return false;
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public Calendar getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Calendar geldigTot) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OVChipkaart)) return false;
        OVChipkaart ovkaart = (OVChipkaart) o;
        return kaartnummer == ovkaart.getKaartnummer() &&
                klasse == ovkaart.klasse;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        String kaart = "kaartnummer: " + kaartnummer + ", geldig tot " + geldigTot.getTime().toString() + ", klasse: " + klasse + ", saldo: " + saldo  + " ";
        if(reiziger!=null){
           kaart +=  "en staat op naam van: " + reiziger.getNaam() + "\n";
        }
        String productbegin = "Deze kaart bevat de volgende producten: \n";
        String producten = "";

        for(Product product : this.producten){
            producten += "  " + product.getProduct_nummer() + ": " + product.getNaam() + ", " + product.getBeschrijving() + ", Prijs: " + product.getPrijs() + "\n";
        }
        if(producten == ""){
            producten = "   --: Deze kaart heeft geen producten. \n";
        }
        return kaart + productbegin + producten;

    }
}
