package forreal.Domein;

import java.util.*;
import java.time.LocalDate;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<Integer> ovkaarten;

    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        ovkaarten = new ArrayList<>();
    }

    public List<Integer> getOvkaarten() {
        return ovkaarten;
    }
    public boolean addOvkaart(int ovChipkaart) {
        return ovkaarten.add(ovChipkaart);
    }
    public boolean removeOvkaart(int ovChipkaart) {
        return ovkaarten.removeIf(ovChipkaart1 -> ovChipkaart1 == ovChipkaart);
    }

    public boolean contains(int ovkaart){
        for(int ovChipkaart : ovkaarten){
            if(ovChipkaart == ovkaart){
                return true;
            }
        }
        return false;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return product_nummer == product.product_nummer &&
                Double.compare(product.prijs, prijs) == 0 &&
                naam.equals(product.naam) &&
                beschrijving.equals(product.beschrijving);
    }

    @Override
    public String toString() {
        return  "product: nummer = " + product_nummer +
                ", naam = '" + naam + '\'' +
                ", beschrijving = '" + beschrijving + '\'' +
                ", prijs = " + prijs +
                "\nOvKaarten: " + ovkaarten;
    }
}
