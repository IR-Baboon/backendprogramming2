package forreal.SQL;

import forreal.DAO.AdresDAO;
import forreal.DAO.ReizigerDAO;
import forreal.Domein.Adres;
import forreal.Domein.Reiziger;
import forreal.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn){
        this.conn = conn;
    }

    public AdresDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
    }

    public boolean save(Adres adres) {
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
            pst.setInt(6, adres.getReiziger().getReizigerId());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();

            return true;

        }catch(SQLException sql){
           return Utils.errorHandler(sql, conn);
        }
    }
    public boolean update(Adres adres){
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
            pst.setInt(5, adres.getReiziger().getReizigerId());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();
            return true;

        }catch(SQLException sql) {
            return Utils.errorHandler(sql, conn);
        }
    }

    public boolean delete(Adres adres){
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
            pst.setInt(1, reiziger.getReizigerId());

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            // pak resultaat uit

            int reiziger_id = 0;
            String postcode ="";
            String huisnummer ="";
            String straat = "";
            String woonplaats = "";
            int adres_id = 0;

            while(rs.next()){
                adres_id = rs.getInt("adres_id");
                reiziger_id = rs.getInt("reiziger_id");
                postcode = rs.getString("postcode");
                huisnummer = rs.getString("huisnummer");
                straat = rs.getString("straat");
                woonplaats = rs.getString("woonplaats");

            }
            // maak adres aan als input een geldige waarde heeft
            if(adres_id != 0){
                Adres adres =  new Adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id);
                adres.setReiziger(reiziger);
                rs.close();
                pst.close();
                return adres;
            }else{
                return null;
            }
        }catch(SQLException sql){
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
            List<Adres> resultaat = new ArrayList<>();

            while(rs.next()){
                int adres_id = rs.getInt("adres_id");
                int reiziger_id = rs.getInt("reiziger_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                Adres adres =  new Adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id);
                if(rdao != null){
                    adres.setReiziger(rdao.findById(reiziger_id));
                }
                resultaat.add(adres);
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
