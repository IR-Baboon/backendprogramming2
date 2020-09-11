package forreal.Domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OV_Chipkaart {
    private int kaartnummer;
    private LocalDate geldig_tot;
    private int klasse;
    private double saldo;
    private int reiziger_id;
    private Reiziger reiziger;
    private List<Product> producten;

    public OV_Chipkaart(int kaartnummer, LocalDate geldig_tot, int klasse, double saldo, int reiziger_id) {
        this.kaartnummer = kaartnummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger_id = reiziger_id;
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

    public LocalDate getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(LocalDate geldig_tot) {
        this.geldig_tot = geldig_tot;
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

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        String kaart = "kaartnummer: " + kaartnummer + ", geldig tot " + geldig_tot + ", klasse: " + klasse + ", saldo: " + saldo  + "\n";
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
