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
            if(product.getKaart_nummer() != 0){
                String Query = "INSERT INTO ov_chipkaart_product (product_nummer, kaart_nummer, status, last_update) VALUES (?, ?, ?, ?)";

                // maak een statement
                PreparedStatement pst = conn.prepareStatement(Query);

                // voeg statement variabelen toe
                pst.setInt(1, product.getProduct_nummer());
                pst.setInt(2, product.getKaart_nummer());
                pst.setString(3, product.getStatus());
                pst.setDate(4, new Date(product.getLast_update().getTimeInMillis()));

                // Stuur de query naar de DB
                pst.executeUpdate();
                pst.close();
                return true;
            }
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

            // sluit alles netjes af
            pst.close();

            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public boolean update(Product product){
        try{
            if(product.getKaart_nummer() != 0){
                String Query = "UPDATE ov_chipkaart_product SET status = ?, last_update = ? WHERE kaart_nummer = ? AND product_nummer = ?";

                // maak een statement
                PreparedStatement pst = conn.prepareStatement(Query);

                // voeg statement variabelen toe
                pst.setInt(4, product.getProduct_nummer());
                pst.setInt(3, product.getKaart_nummer());
                pst.setString(1, product.getStatus());
                System.out.println(new Date(product.getLast_update().getTimeInMillis()));
                pst.setDate(2, new Date(product.getLast_update().getTimeInMillis()));

                // Stuur de query naar de DB
                pst.executeUpdate();
                pst.close();
                return true;
            }
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

            // sluit alles netjes af

            pst.close();
            return true;

        }catch(SQLException sql) {
            return Utils.errorHandler(sql, conn);
        }
    }
    public boolean delete(Product product){
        try{
            if(product.getKaart_nummer() != 0){
                String Query = "DELETE FROM " +
                        "ov_chipkaart_product " +
                        "WHERE " +
                        "product_nummer = ? AND kaart_nummer = ?";

                // maak een statement
                PreparedStatement pst = conn.prepareStatement(Query);

                // voeg statement variabelen toe
                pst.setInt(1, product.getProduct_nummer());
                pst.setInt(2, product.getKaart_nummer());

                // Stuur de query naar de DB
                pst.executeUpdate();

                // sluit alles netjes af
                pst.close();

                // return boolean waarde
                return true;
            }else{
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

                // sluit alles netjes af
                pst.close();

                // return boolean waarde
                return true;
            }


        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public List<Product> findByOVChipkaart(OVChipkaart kaart){
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
            pst.setInt(1, kaart.getKaartnummer());

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            //pak resultaat uit
            List<Product> resultaat = new ArrayList<>();

            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                int productnummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String status = rs.getString("status");
                double prijs = rs.getDouble("prijs");
                Date date = rs.getDate("last_update");
                String beschrijving = rs.getString("beschrijving");
                Product product = new Product(productnummer, naam, beschrijving, prijs);
                product.setKaart_nummer(kaartnummer);
                product.setStatus(status);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                product.setLast_update(cal);

                resultaat.add(product);
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
            String Query = "SELECT * FROM product";

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

                resultaat.add(product);
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
