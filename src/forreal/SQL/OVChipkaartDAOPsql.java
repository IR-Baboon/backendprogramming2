package forreal.SQL;

import forreal.DAO.OVChipkaartDAO;
import forreal.Domein.Adres;
import forreal.Domein.OV_Chipkaart;
import forreal.Domein.Reiziger;
import forreal.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;

    public OVChipkaartDAOPsql(Connection conn){
        this.conn = conn;
    }


    public Boolean save(OV_Chipkaart kaart) {
        try{
            // mstel de query samen
            String Query = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, kaart.getKaartnummer());
            pst.setDate(2, Date.valueOf(kaart.getGeldig_tot()));
            pst.setInt(3, kaart.getKlasse());
            pst.setDouble(4, kaart.getSaldo());
            pst.setInt(5, kaart.getReiziger().getId());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();

            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public Boolean update(OV_Chipkaart kaart){
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
            pst.setDate(1, Date.valueOf(kaart.getGeldig_tot()));
            pst.setInt(2, kaart.getKlasse());
            pst.setDouble(3, kaart.getSaldo());
            pst.setInt(4, kaart.getReiziger_id());

            // Stuur de query naar de DB
            pst.executeUpdate();

            // sluit alles netjes af

            pst.close();
            return true;

        }catch(SQLException sql) {
            return Utils.errorHandler(sql, conn);
        }
    }

    public Boolean delete(OV_Chipkaart kaart){
        try{
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
    public List<OV_Chipkaart> findByReiziger(Reiziger reiziger){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM " +
                    "ov_chipkaart " +
                    "WHERE " +
                    "reiziger_id = ? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getId());

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            //pak resultaat uit
            List<OV_Chipkaart> resultaat = new ArrayList<>();

            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                int reiziger_id = rs.getInt("reiziger_id");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                LocalDate geldig_tot = LocalDate.parse(rs.getDate("geldig_tot").toString());
                OV_Chipkaart ovkaart = new OV_Chipkaart(kaartnummer, geldig_tot, klasse, saldo, reiziger_id);
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
    public List<OV_Chipkaart> findAll(){
        try{
            // mstel de query samen
            String Query = "SELECT * FROM ov_chipkaart";

            // maak een statement
            Statement st = conn.createStatement();

            // Stuur de query naar de DB
            ResultSet rs = st.executeQuery(Query);
            //pak resultaat uit
            List<OV_Chipkaart> resultaat = new ArrayList<>();

            while(rs.next()){
                int kaartnummer = rs.getInt("kaart_nummer");
                int reiziger_id = rs.getInt("reiziger_id");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                Date date = rs.getDate("geldig_tot");
                LocalDate geldig_tot = LocalDate.parse(date.toString());
                OV_Chipkaart ovkaart = new OV_Chipkaart(kaartnummer, geldig_tot, klasse, saldo, reiziger_id);
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
