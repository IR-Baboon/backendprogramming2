package forreal.SQL;

import forreal.DAO.AdresDAO;
import forreal.DAO.ReizigersDAO;
import forreal.Domein.Adres;
import forreal.Domein.Reiziger;
import forreal.Utils;

import java.sql.*;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
    private Connection conn;
    private ReizigersDAO rdao;

    public AdresDAOPsql(Connection conn){
        this.conn = conn;
//        this.rdao = new ReizigersDAOPsql(conn);
    }

    public Boolean save(Adres adres) {
        try{
            // mstel de query samen
            String Query = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, adres.getAdres_id());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger_id());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();

            return true;

        }catch(SQLException sql){
           return Utils.errorHandler(sql, conn);
        }
    }
    public Boolean update(Adres adres){
        try{
            // mstel de query samen
             String Query =
                     "UPDATE " +
                         "adres " +
                     "SET " +
                         "postcode = ?, " +
                         "huisnummer = ?, " +
                         "straat = ?, " +
                         "woonplaats = ?, " +
                         "reiziger_id = ?" +
                     "WHERE " +
                         "adres_id = ? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(6, adres.getAdres_id());
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger_id());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();
            return true;

        }catch(SQLException sql) {
            System.out.println("test1");
            return Utils.errorHandler(sql, conn);
        }
    }

    public Boolean delete(Adres adres){
        try{
            // stel de query samen
            String Query = "DELETE FROM " +
                    "adres " +
                    "WHERE " +
                    "adres_id = ?";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, adres.getAdres_id());

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
    public Adres findById(int idx){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM " +
                    "adres " +
                    "WHERE " +
                    "adres_id=? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, idx);
            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();

            // pak het resultaat uit
            Adres resultaat = Utils.returnSingleAdres(rs);

            // sluit alles netjes af
            rs.close();
            pst.close();

            // retourneer resultaat
            return resultaat;

        }catch(SQLException sql){
            Utils.errorHandler(sql, conn);
            return null;
        }

    }
    public Adres findByReiziger(Reiziger reiziger){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM " +
                    "adres " +
                    "WHERE " +
                    "reiziger_id = ? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getId());

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            // pak resultaat uit

            Adres resultaat = Utils.returnSingleAdres(rs);

            // sluit alles netjes af
            rs.close();
            pst.close();

            return resultaat;

        }catch(SQLException sql){
            System.out.println("test");
            Utils.errorHandler(sql, conn);
            return null;
        }
    }
    public List<Adres> findAll(){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM adres";

            // maak een statement
            Statement st = conn.createStatement();

            // Stuur de query naar de DB
            ResultSet rs = st.executeQuery(Query);
            //pak resultaat uit
            List<Adres> resultaat = Utils.returnMultiAdres(rs);

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
