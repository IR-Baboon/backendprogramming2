package forreal;

import forreal.Domein.Reiziger;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Reiziger returnSingleReiziger(ResultSet rs) throws SQLException {
        int id = 0;
        String voorletters = "";
        String tussenvoegsel ="";
        String achternaam = "";
        Date geboortedatum = Date.valueOf("");

        while(rs.next()){
            id = rs.getInt("reiziger_id");
            voorletters = rs.getString("voorletters");
            tussenvoegsel = rs.getString("tussenvoegsel");
            achternaam = rs.getString("achternaam");
            geboortedatum = rs.getDate("geboortedatum");

        }
        return new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum.toString());
    }

    public static List<Reiziger> returnMultiReiziger(ResultSet rs) throws SQLException {
        int id;
        String voorletters;
        String tussenvoegsel;
        String achternaam;
        Date geboortedatum;
        List<Reiziger> resultlist = new ArrayList<>();

        while(rs.next()){
            id = rs.getInt("reiziger_id");
            voorletters = rs.getString("voorletters");
            tussenvoegsel = rs.getString("tussenvoegsel");
            achternaam = rs.getString("achternaam");
            geboortedatum = rs.getDate("geboortedatum");
            Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum.toString());
            resultlist.add(reiziger);
        }
        return resultlist;
    }

}
