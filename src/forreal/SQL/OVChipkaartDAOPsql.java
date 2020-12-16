package forreal.SQL;

import forreal.DAO.OVChipkaartDAO;
import forreal.DAO.ProductDAO;
import forreal.DAO.ReizigerDAO;
import forreal.Domein.OVChipkaart;
import forreal.Domein.Product;
import forreal.Domein.Reiziger;
import forreal.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;
    private ProductDAO pdao;


    public OVChipkaartDAOPsql(Connection conn){
        this.conn = conn;
    }

    public OVChipkaartDAOPsql(Connection conn, ReizigerDAO rdao, ProductDAO pdao) {
        this.conn = conn;
        this.rdao = rdao;
        this.pdao = pdao;
    }

    public boolean save(OVChipkaart kaart) {
        try{
            // mstel de query samen
            String Query = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, kaart.getKaartnummer());
            pst.setDate(2, new Date(kaart.getGeldigTot().getTimeInMillis()));
            pst.setInt(3, kaart.getKlasse());
            pst.setDouble(4, kaart.getSaldo());
            pst.setInt(5, kaart.getReiziger().getReizigerId());

            // Stuur de query naar de DB
            pst.executeUpdate();
            for(Product product : kaart.getProducten()){
                Query = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
                // maak een statement
                pst = conn.prepareStatement(Query);
                // voeg statement variabelen toe
                pst.setInt(2, product.getProduct_nummer());
                pst.setInt(1, kaart.getKaartnummer());
                // Stuur de query naar de DB
                pst.executeUpdate();
                // sluit alles netjes af
            }
            // sluit alles netjes af
            pst.close();

            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public boolean update(OVChipkaart kaart){
        try{

            // mstel de query samen
            String Query =
                    "UPDATE " +
                            "ov_chipkaart " +
                            "SET " +
                            "geldig_tot = ?, " +
                            "klasse = ?, " +
                            "saldo = ?, " +
                            "reiziger_id = ?" +
                            "WHERE " +
                            "kaart_nummer = ? ";


            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(5, kaart.getKaartnummer());
            pst.setDate(1, new java.sql.Date(kaart.getGeldigTot().getTimeInMillis()));
            pst.setInt(2, kaart.getKlasse());
            pst.setDouble(3, kaart.getSaldo());
            pst.setInt(4, kaart.getReiziger().getReizigerId());

            // Stuur de query naar de DB
            pst.executeUpdate();

            for(Product product : kaart.getProducten()){
                Query = "INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES (?, ?) ;" +
                        "UPDATE ov_chipkaart_product SET last_update = ? WHERE kaart_nummer = ? AND product_nummer = ?;";
                // maak een statement
                pst = conn.prepareStatement(Query);
                // voeg statement variabelen toe
                pst.setInt(1, kaart.getKaartnummer());
                pst.setInt(2, product.getProduct_nummer());
                pst.setDate(3, Date.valueOf("2020-10-10"));
                pst.setInt(4, kaart.getKaartnummer());
                pst.setInt(5, product.getProduct_nummer());

                // Stuur de query naar de DB
                pst.executeUpdate();
                // sluit alles netjes af
            }

            // sluit alles netjes af

            pst.close();
            return true;

        }catch(SQLException sql) {
            return Utils.errorHandler(sql, conn);
        }
    }

    public boolean delete(OVChipkaart kaart){
        try{
            for(Product product : kaart.getProducten()){
                String Query = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ? AND product_nummer = ? ";
                // maak een statement
                PreparedStatement pst = conn.prepareStatement(Query);
                // voeg statement variabelen toe
                pst.setInt(1, kaart.getKaartnummer());
                pst.setInt(2, product.getProduct_nummer());

                // Stuur de query naar de DB
                pst.executeUpdate();
                // sluit alles netjes af
                pst.close();
            }
            // stel de query samen
            String Query = "DELETE FROM " +
                    "ov_chipkaart " +
                    "WHERE " +
                    "kaart_nummer = ?";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, kaart.getKaartnummer());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af
            pst.close();

            // return boolean waarde
            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public List<OVChipkaart> findByReiziger(Reiziger reiziger){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM " +
                    "ov_chipkaart ov " +
                    "LEFT JOIN ov_chipkaart_product ovp ON ov.kaart_nummer = ovp.kaart_nummer " +
                    "LEFT JOIN product p ON ovp.product_nummer = p.product_nummer " +
                    "WHERE " +
                    "reiziger_id = ? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getReizigerId());

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            //pak resultaat uit
            List<OVChipkaart> resultaat = new ArrayList<>();

            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                int reiziger_id = rs.getInt("reiziger_id");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                Date geldig_tot = rs.getDate("geldig_tot");
                Calendar cal = Calendar.getInstance();
                cal.setTime(geldig_tot);
                OVChipkaart ovkaart = new OVChipkaart(kaartnummer, cal, klasse, saldo);

                int productnummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");

                if(rdao != null){
                    ovkaart.setReiziger(rdao.findById(reiziger_id));
                }
                if(resultaat.contains(ovkaart)){
                    if(productnummer != 0){
                        resultaat.get(resultaat.indexOf(ovkaart)).addProduct(new Product(productnummer, naam, beschrijving, prijs));
                    }
                }else{
                    if(productnummer != 0){
                        ovkaart.addProduct(new Product(productnummer, naam, beschrijving, prijs));
                        resultaat.add(ovkaart);
                    }
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
    public List<OVChipkaart> findAll(){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM ov_chipkaart ov " +
                    "LEFT JOIN ov_chipkaart_product ovp ON ov.kaart_nummer = ovp.kaart_nummer " +
                    "LEFT JOIN product p ON ovp.product_nummer = p.product_nummer " +
                    "ORDER BY ov.kaart_nummer";

            // maak een statement
            Statement st = conn.createStatement();

            // Stuur de query naar de DB
            ResultSet rs = st.executeQuery(Query);
            //pak resultaat uit

            List<OVChipkaart> resultaat = new ArrayList<>();

            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                int reiziger_id = rs.getInt("reiziger_id");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                Date geldig_tot = rs.getDate("geldig_tot");
                Calendar cal = Calendar.getInstance();
                cal.setTime(geldig_tot);
                OVChipkaart ovkaart = new OVChipkaart(kaartnummer, cal, klasse, saldo);
                int productnummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");
                if(rdao != null){
                    ovkaart.setReiziger(rdao.findById(reiziger_id));
                }
                if(resultaat.contains(ovkaart)){
                    if(productnummer != 0){
                        resultaat.get(resultaat.indexOf(ovkaart)).addProduct(new Product(productnummer, naam, beschrijving, prijs));
                    }
                }else{
                    if(productnummer != 0){
                        ovkaart.addProduct(new Product(productnummer, naam, beschrijving, prijs));
                        resultaat.add(ovkaart);
                    }
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
