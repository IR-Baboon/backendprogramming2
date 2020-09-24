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
        this.pdao = new ProductDAOPsql(conn);
    }

    public OVChipkaartDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
        this.pdao = new ProductDAOPsql(conn);
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
            if(kaart.getProducten().size() > 0){
                for(Product product : kaart.getProducten()){
                    pdao.save(product);
                }
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
            if(kaart.getProducten().size() > 0){
                List<Product> dblijst = pdao.findByOVChipkaart(kaart);
                List<Product> kaartlijst = kaart.getProducten();

                for(Product product : dblijst){
                    for(Product ovproduct : kaartlijst){
                        if(product.equalsProduct(ovproduct)){
                            if(!ovproduct.equalsOvProduct(product)){
                                pdao.update(ovproduct);
                            }
                            kaartlijst.remove(ovproduct);
                        }
                    }
                }
               if(kaartlijst.size() > 0){
                   for(Product product : kaartlijst){
                       pdao.save(product);
                   }
               }
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
            List<Product> lijst = pdao.findByOVChipkaart(kaart);
            if(lijst.size() > 0){
                for (Product product : lijst) {
                    pdao.delete(product);
                }
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
                    "ov_chipkaart " +
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
                for(Product product : pdao.findByOVChipkaart(ovkaart)){
                    ovkaart.addProduct(product);
                }
                if(rdao != null){
                    ovkaart.setReiziger(rdao.findById(reiziger_id));
                }
                resultaat.add(ovkaart);
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
            String Query = "SELECT * FROM ov_chipkaart";

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
                if(rdao != null){
                    ovkaart.setReiziger(rdao.findById(reiziger_id));
                }
                for(Product product : pdao.findByOVChipkaart(ovkaart)){
                    ovkaart.addProduct(product);
                }
                resultaat.add(ovkaart);
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
