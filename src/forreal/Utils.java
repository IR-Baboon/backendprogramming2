package forreal;

import forreal.Domein.Adres;
import forreal.Domein.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Reiziger returnSingleReiziger(ResultSet rs) throws SQLException {
        Reiziger reiziger = new Reiziger();
        while(rs.next()){
            int id = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = rs.getString("tussenvoegsel");
            String achternaam = rs.getString("achternaam");
            Date geboortedatum = rs.getDate("geboortedatum");



            reiziger.setId(id);
            reiziger.setVoorletters(voorletters);
            reiziger.setTussenvoegsel(tussenvoegsel);
            reiziger.setAchternaam(achternaam);
            reiziger.setGeboortedatum(geboortedatum.toString());

        };

        return reiziger;

    }

    public static Adres returnSingleAdres(ResultSet rs) throws SQLException {
        Adres adres = new Adres();
        while(rs.next()){

            int adres_id = rs.getInt("adres_id");
            int reiziger_id = rs.getInt("reiziger_id");
            String postcode = rs.getString("postcode");
            String huisnummer = rs.getString("huisnummer");
            String straat = rs.getString("straat");
            String woonplaats = rs.getString("woonplaats");



            adres.setAdres_id(adres_id);
            adres.setStraat(straat);
            adres.setHuisnummer(huisnummer);
            adres.setPostcode(postcode);
            adres.setWoonplaats(woonplaats);
            adres.setReiziger_id(reiziger_id);


        }
        return adres;
    }

    public static List<Reiziger> returnMultiReiziger(ResultSet rs) throws SQLException {
        List<Reiziger> resultlist = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = rs.getString("tussenvoegsel");
            String achternaam = rs.getString("achternaam");
            Date geboortedatum = rs.getDate("geboortedatum");

            Reiziger reiziger = new Reiziger();

            reiziger.setId(id);
            reiziger.setVoorletters(voorletters);
            reiziger.setTussenvoegsel(tussenvoegsel);
            reiziger.setAchternaam(achternaam);
            reiziger.setGeboortedatum(geboortedatum.toString());

            resultlist.add(reiziger);
        }
        return resultlist;
    }

    public static List<Adres> returnMultiAdres(ResultSet rs) throws SQLException {
        List<Adres> resultlist = new ArrayList<>();

        while(rs.next()){
            int adres_id = rs.getInt("adres_id");
            int reiziger_id = rs.getInt("reiziger_id");
            String postcode = rs.getString("postcode");
            String huisnummer = rs.getString("huisnummer");
            String straat = rs.getString("straat");
            String woonplaats = rs.getString("woonplaats");

            Adres adres = new Adres();

            adres.setAdres_id(adres_id);
            adres.setStraat(straat);
            adres.setHuisnummer(huisnummer);
            adres.setPostcode(postcode);
            adres.setWoonplaats(woonplaats);
            adres.setReiziger_id(reiziger_id);

            resultlist.add(adres);
        }
        return resultlist;
    }

    public static Boolean errorHandler(SQLException error, Connection conn){
        if (conn != null) {
            try {
                System.err.println("[SQL Error!] Query could not be completed " + error.getMessage());
                conn.rollback();
                return false;
            } catch (SQLException excep) {
                System.err.println("[SQL Error!] failed operation rollback " + excep.getMessage());
                return false;
            }
        }
        return false;
    }
}
