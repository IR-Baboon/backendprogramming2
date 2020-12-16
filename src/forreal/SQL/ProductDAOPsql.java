package forreal.SQL;

import forreal.DAO.OVChipkaartDAO;
import forreal.DAO.ProductDAO;
import forreal.Domein.OVChipkaart;
import forreal.Domein.Product;
import forreal.Utils;
import org.checkerframework.checker.units.qual.C;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public ProductDAOPsql(Connection conn){
        this.conn = conn;
    }


    public boolean save(Product product){
        try{

            // mstel de query samen
            String Query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());

            // Stuur de query naar de DB
            pst.executeUpdate();
            pst.close();
            for(int ovChipkaart : product.getOvkaarten()){
                Query = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
                // maak een statement
                pst = conn.prepareStatement(Query);
                // voeg statement variabelen toe
                pst.setInt(2, product.getProduct_nummer());
                pst.setInt(1, ovChipkaart);
                // Stuur de query naar de DB
                pst.executeUpdate();
                // sluit alles netjes af
                pst.close();
            }
            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public boolean update(Product product){
        try{

            // stel de query samen
            String Query =  "UPDATE " +
                                "product " +
                            "SET " +
                                "naam = ?," +
                                "beschrijving = ?," +
                                "prijs = ?" +
                            "WHERE " +
                                "product_nummer = ?";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(4, product.getProduct_nummer());
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());

            // Stuur de query naar de DB
            pst.executeUpdate();
            for(int ovkaart : product.getOvkaarten()){
                List<Product> lijst = this.findByOVChipkaart(ovkaart);
                if(lijst.contains(product)){
                    Query = "UPDATE ov_chipkaart_product SET last_update = ? WHERE kaart_nummer = ? AND product_nummer = ?";
                    // maak een statement
                    pst = conn.prepareStatement(Query);
                    // voeg statement variabelen toe
                    pst.setInt(3, product.getProduct_nummer());
                    pst.setInt(2, ovkaart);
                    pst.setDate(1, Date.valueOf("2020-10-10"));
                }else{
                    Query = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES (?, ?)";
                    // maak een statement
                    pst = conn.prepareStatement(Query);
                    // voeg statement variabelen toe
                    pst.setInt(2, product.getProduct_nummer());
                    pst.setInt(1, ovkaart);
                }
                pst.executeUpdate();

                // sluit alles netjes af
                pst.close();
            }
            // sluit alles netjes af

            pst.close();
            return true;

        }catch(SQLException sql) {
            return Utils.errorHandler(sql, conn);
        }
    }
    public boolean delete(Product product){
        try{
            for(int ovkaart : product.getOvkaarten()){
                String Query = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ? AND product_nummer = ?";
                // maak een statement
                PreparedStatement pst = conn.prepareStatement(Query);
                // voeg statement variabelen toe
                pst.setInt(2, product.getProduct_nummer());
                pst.setInt(1, ovkaart);
                // Stuur de query naar de DB
                pst.executeUpdate();
                // sluit alles netjes af
                pst.close();
            }
                // stel de query samen
                String Query = "DELETE FROM " +
                        "product " +
                        "WHERE " +
                        "product_nummer = ?";

                // maak een statement
                PreparedStatement pst = conn.prepareStatement(Query);

                // voeg statement variabelen toe
                pst.setInt(1, product.getProduct_nummer());

                // Stuur de query naar de DB
                pst.executeUpdate();
                pst.close();

                // sluit alles netjes af
                pst.close();

                // return boolean waarde
                return true;


        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public List<Product> findByOVChipkaart(int kaart){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM " +
                    "ov_chipkaart_product op " +
                    "LEFT JOIN product p " +
                    "ON op.product_nummer = p.product_nummer " +
                    "WHERE " +
                    "kaart_nummer = ? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, kaart);

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            //pak resultaat uit
            List<Product> resultaat = new ArrayList<>();

            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                int productnummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                double prijs = rs.getDouble("prijs");

                String beschrijving = rs.getString("beschrijving");
                Product product = new Product(productnummer, naam, beschrijving, prijs);
                if(resultaat.contains(product)){
                    if(kaartnummer != 0){
                        resultaat.get(resultaat.indexOf(product)).addOvkaart(kaartnummer);
                    }
                }else{
                    if(productnummer != 0){
                        product.addOvkaart(kaartnummer);
                    }
                    resultaat.add(product);
                }

            }

            // sluit alles netjes af
            rs.close();
            pst.close();

            return resultaat;
        }catch(SQLException sql){
            Utils.errorHandler(sql, conn);
            return null;
        }
    }
    public List<Product> findAll(){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM product p LEFT JOIN ov_chipkaart_product o ON p.product_nummer = o.product_nummer ORDER BY p.product_nummer";

            // maak een statement
            Statement st = conn.createStatement();

            // Stuur de query naar de DB
            ResultSet rs = st.executeQuery(Query);
            //pak resultaat uit

            List<Product> resultaat = new ArrayList<>();

            while(rs.next()){
                int productnummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                double prijs = rs.getDouble("prijs");
                String beschrijving = rs.getString("beschrijving");
                Product product = new Product(productnummer, naam, beschrijving, prijs);

                if(resultaat.contains(product)){
                    resultaat.get(resultaat.indexOf(product)).addOvkaart(rs.getInt("kaart_nummer"));
                }else{
                    product.addOvkaart(rs.getInt("kaart_nummer"));
                    resultaat.add(product);
                }
            }

            // sluit alles netjes af
            rs.close();
            st.close();

            return resultaat;

        }catch(SQLException sql){
            Utils.errorHandler(sql, conn);
            return null;
        }
    }
}
