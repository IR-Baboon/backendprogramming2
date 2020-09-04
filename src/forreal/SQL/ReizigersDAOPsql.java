package forreal.SQL;

import forreal.DAO.AdresDAO;
import forreal.DAO.ReizigersDAO;
import forreal.Domein.Adres;
import forreal.Domein.Reiziger;
import forreal.Utils;

import java.sql.*;
import java.util.List;

public class ReizigersDAOPsql implements ReizigersDAO{
    private Connection conn;
    private AdresDAO adao;

    public ReizigersDAOPsql(Connection conn){
        this.conn = conn;
        this.adao = new AdresDAOPsql(conn);
    }

    public Boolean save(Reiziger reiziger) throws SQLException {
        try{
            // stel de query samen
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

            if(reiziger.getAdres() != null){
                if(adao.findById(reiziger.getAdres().getAdres_id()) != null){

                    adao.update(reiziger.getAdres());
                }else{

                    adao.save(reiziger.getAdres());
                }
            }

            // sluit alles netjes af

            pst.close();

            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public Boolean update(Reiziger reiziger){
        try{
            // mstel de query samen
             String Query = "UPDATE " +
                                "reiziger " +
                            "SET " +
                                "voorletters = ?, " +
                                "tussenvoegsel = ?, " +
                                "achternaam = ?, " +
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

            if(reiziger.getAdres() != null){
                if(adao.findById(reiziger.getAdres().getAdres_id()).getAdres_id() != 0){
                    adao.update(reiziger.getAdres());
                }else{
                    adao.save(reiziger.getAdres());
                }
            }
            // sluit alles netjes af

            pst.close();
            return true;
        }catch(SQLException sql) {
            return Utils.errorHandler(sql, conn);
        }
    }

    public Boolean delete(Reiziger reiziger){
        try{
            // stel de query samen
             String Query = "DELETE FROM " +
                                "reiziger " +
                            "WHERE " +
                                "reiziger_id = ?";

            // maak een statement
            if(reiziger.getAdres() != null){
                adao.delete(reiziger.getAdres());
            }
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getId());
         
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
    public Reiziger findById(int idx){
        try{
            // mstel de query samen
             String Query = "SELECT * FROM " +
                                "reiziger " +
                            "WHERE " +
                                "reiziger_id=? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, idx);
            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();

            // pak het resultaat uit
            Reiziger resultaat = Utils.returnSingleReiziger(rs);
            Adres adres = adao.findByReiziger(resultaat);

            if(adres.getAdres_id() != 0){
                resultaat.setAdres(adres);
            }

            // sluit alles netjes af
            rs.close();
            pst.close();

            // retourneer resultaat
            return resultaat;
            
        }catch(SQLException sql){
            // geef error terug in error console
            Utils.errorHandler(sql, conn);

            // return false
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
            for(Reiziger reiziger : resultaat){
                Adres adres = adao.findByReiziger(reiziger);
                if(adres.getAdres_id() != 0){
                    reiziger.setAdres(adres);
                }
            }
            // sluit alles netjes af
            rs.close();
            pst.close();
            
            return resultaat;

        }catch(SQLException sql){
            // geef error terug in error console
            Utils.errorHandler(sql, conn);

            // return false
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

            for(Reiziger reiziger : resultaat){
                Adres adres = adao.findByReiziger(reiziger);
                if(adres.getAdres_id() != 0){
                    reiziger.setAdres(adres);
                }
            }
            // sluit alles netjes af
            rs.close();
            st.close();

            return resultaat;

        }catch(SQLException sql){
            // geef error terug in error console
            Utils.errorHandler(sql, conn);

            // return false
            return null;
        }
    }
}
