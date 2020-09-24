package forreal.Domein;

import java.util.*;
import java.time.LocalDate;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private int kaart_nummer;
    private String status;
    private Calendar last_update;


    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Calendar getLast_update() {
        return last_update;
    }

    public void setLast_update(Calendar last_update) {
        this.last_update = last_update;
    }


    public boolean equalsProduct(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return product_nummer == product.product_nummer &&
                Double.compare(product.prijs, prijs) == 0 &&
                Objects.equals(naam, product.naam) &&
                Objects.equals(beschrijving, product.beschrijving);
    }

    public boolean equalsOvProduct(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return product_nummer == product.product_nummer &&
                Double.compare(product.prijs, prijs) == 0 &&
                Objects.equals(naam, product.naam) &&
                Objects.equals(beschrijving, product.beschrijving) &&
                Objects.equals(status, product.status) &&
                Objects.equals(last_update, product.last_update);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_nummer, naam, beschrijving, prijs);
    }

    @Override
    public String toString() {
        return  "product: nummer = " + product_nummer +
                ", naam = '" + naam + '\'' +
                ", beschrijving = '" + beschrijving + '\'' +
                ", prijs = " + prijs;
    }
}
