package forreal.SQL;

import forreal.DAO.ReizigersDAO;
import forreal.Domein.Reiziger;
import forreal.Utils;
import forreal.Utils.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigersDAOPsql implements ReizigersDAO {
    private Connection conn;

    public ReizigersDAOPsql(Connection conn){
        this.conn = conn;
    }

    public Boolean save(Reiziger reiziger) throws SQLException {
        try{
            // mstel de query samen
            String Query = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                    "VALUES (?, ?, ?, ?, ?)";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, Date.valueOf(reiziger.getGeboortedatum()));

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();

            return true;

        }catch(SQLException sql){
            System.err.println("[SQLSAVEError!] Query could not be completed" + sql.getMessage());
            sql.printStackTrace();
            return false;
        }

    }
    public Boolean update(Reiziger reiziger){
        try{
            // mstel de query samen
             String Query = "UPDATE " +
                                "reiziger " +
                            "SET " +
                                "voorletters = ? " +
                                "tussenvoegsel = ? " +
                                "achternaam = ? " +
                                "geboortedatum = ? " +
                            "WHERE " +
                                "reiziger_id = ? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(5, reiziger.getId());
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, Date.valueOf(reiziger.getGeboortedatum()));

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();
            return true;
        }catch(SQLException sql){
            System.err.println("[SQLUPError!] Query could not be completed " + sql.getMessage());
            return false;
        }

    }
    public Boolean delete(Reiziger reiziger){
        try{
            // mstel de query samen
             String Query = "DELETE FROM " +
                                "reiziger " +
                            "WHERE " +
                                "reiziger_id = ?";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getId());
         
            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();

            return true;
            
        }catch(SQLException sql){
            System.err.println("[SQLDELError!] Query could not be completed " + sql.getMessage());
            return false;
        }

    }
    public Reiziger findById(int idx){
        try{
            // mstel de query samen
             String Query = "SELECT FROM " +
                                "reiziger " +
                            "WHERE " +
                                "reiziger_id=? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, idx);
            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();

            Reiziger resultaat = Utils.returnSingleReiziger(rs);
            // sluit alles netjes af
            rs.close();
            pst.close();
            
            return resultaat;
            
        }catch(SQLException sql){
            System.err.println("[SQLError!] Query could not be completed" + sql.getMessage());
            return null;
        }

    }
    public List<Reiziger> findByGbdatum(String datum){
        try{
            // mstel de query samen
             String Query = "SELECT FROM " +
                                "reiziger " +
                            "WHERE " +
                                "geboortedatum=? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setDate(1, Date.valueOf(datum));
            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            // pak resultaat uit
            List<Reiziger> resultaat = Utils.returnMultiReiziger(rs);
            // sluit alles netjes af
            rs.close();
            pst.close();
            
            return resultaat;

        }catch(SQLException sql){
            System.err.println("[SQLError!] Query could not be completed" + sql.getMessage());
            return null;
        }
    }
    public List<Reiziger> findAll(){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM reiziger";

            // maak een statement
            Statement st = conn.createStatement();

            // Stuur de query naar de DB
            ResultSet rs = st.executeQuery(Query);
            //pak resultaat uit
            List<Reiziger> resultaat = Utils.returnMultiReiziger(rs);

            // sluit alles netjes af
            rs.close();
            st.close();

            return resultaat;

        }catch(SQLException sql){
            System.err.println("[SQLError!] Query could not be completed" + sql.getMessage());
            return null;
        }
    }
}
