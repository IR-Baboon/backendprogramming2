package forreal.SQL;

import forreal.DAO.AdresDAO;
import forreal.DAO.OVChipkaartDAO;
import forreal.DAO.ReizigerDAO;
import forreal.Domein.Adres;
import forreal.Domein.OVChipkaart;
import forreal.Domein.Reiziger;
import forreal.Utils;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;
    private OVChipkaartDAO ovdao;

    public ReizigerDAOPsql(Connection conn){
        this.conn = conn;
        this.adao = new AdresDAOPsql(conn);
        this.ovdao = new OVChipkaartDAOPsql(conn);
    }

    public boolean save(Reiziger reiziger) throws SQLException {
        try{
            // stel de query samen
            String Query = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                    "VALUES (?, ?, ?, ?, ?)";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getReizigerId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, new java.sql.Date(reiziger.getGeboortedatum().getTimeInMillis()));

            // Stuur de query naar de DB
            pst.executeUpdate();

            if(reiziger.getAdres() != null){
               adao.save(reiziger.getAdres());
            }
            if(reiziger.getOvkaarten().size() > 0){
              for(OVChipkaart ovkaart : reiziger.getOvkaarten()){
                 ovdao.save(ovkaart);
              }
            }

            // sluit alles netjes af
            pst.close();
            return true;

        }catch(SQLException sql){
            return Utils.errorHandler(sql, conn);
        }
    }
    public boolean update(Reiziger reiziger){
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
            pst.setInt(5, reiziger.getReizigerId());
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, new java.sql.Date(reiziger.getGeboortedatum().getTimeInMillis()));

            // Stuur de query naar de DB
            pst.executeUpdate();

            if(reiziger.getAdres() != null){
                if(adao.findByReiziger(reiziger) != null){
                    adao.update(reiziger.getAdres());
                }else{
                    adao.save(reiziger.getAdres());
                }
            }

            if(reiziger.getOvkaarten().size() > 0){
                List<OVChipkaart> kaarten = reiziger.getOvkaarten();
                List<OVChipkaart> dbKaarten = ovdao.findByReiziger(reiziger);
                boolean flag = false;
                for(OVChipkaart ovkaart : kaarten){
                    flag = false;
                    for(OVChipkaart dbKaart : dbKaarten){
                        if(ovkaart.getKaartnummer() == dbKaart.getKaartnummer()){
                            ovdao.update(ovkaart);
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        ovdao.save(ovkaart);
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

    public boolean delete(Reiziger reiziger){
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
            if(reiziger.getOvkaarten().size() > 0){
                for(OVChipkaart ovkaart : reiziger.getOvkaarten()){
                    ovdao.delete(ovkaart);
                }
            }
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setInt(1, reiziger.getReizigerId());
         
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
            String voorletters = "";
            String tussenvoegsel = "";
            String achternaam = "";
            Date geboortedatum = null;
            int id = 0;
            Calendar cal = Calendar.getInstance();

            while(rs.next()){
                id = rs.getInt("reiziger_id");
                voorletters = rs.getString("voorletters");
                tussenvoegsel = rs.getString("tussenvoegsel");
                achternaam = rs.getString("achternaam");
                geboortedatum = rs.getDate("geboortedatum");

                cal.setTime(geboortedatum);
            };

            Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, cal);
            Adres adres = adao.findByReiziger(reiziger);
            List<OVChipkaart> ovkaarten = ovdao.findByReiziger(reiziger);

            if(ovkaarten.size() > 0){
                for(OVChipkaart ovkaart : ovkaarten){
                    reiziger.addCard(ovkaart);
                    ovkaart.setReiziger(reiziger);
                }
            }
            if(adres != null ){
                reiziger.setAdres(adres);
            }
            // sluit alles netjes af
            rs.close();
            pst.close();

            // retourneer resultaat
            return reiziger;
            
        }catch(SQLException sql){
            // geef error terug in error console
            Utils.errorHandler(sql, conn);

            // return false
            return null;
        }

    }
    public List<Reiziger> findByGbdatum(Date datum){
        try{
            // mstel de query samen
             String Query = "SELECT FROM " +
                                "reiziger " +
                            "WHERE " +
                                "geboortedatum=? ";

            // maak een statement
            PreparedStatement pst = conn.prepareStatement(Query);

            // voeg statement variabelen toe
            pst.setDate(1, datum);

            // Stuur de query naar de DB
            ResultSet rs = pst.executeQuery();
            // pak resultaat uit
            List<Reiziger> resultlist = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");
                Calendar cal = Calendar.getInstance();
                cal.setTime(geboortedatum);
                Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, cal);
                resultlist.add(reiziger);
            }
            for(Reiziger reiziger : resultlist){
                Adres adres = adao.findByReiziger(reiziger);
                List<OVChipkaart> ovkaarten = ovdao.findByReiziger(reiziger);

                if(ovkaarten.size() > 0){
                    for(OVChipkaart ovkaart : ovkaarten){
                        reiziger.addCard(ovkaart);
                        ovkaart.setReiziger(reiziger);
                    }
                }
                if(adres.getAdres_id() != 0){
                    reiziger.setAdres(adres);
                }
            }
            // sluit alles netjes af
            rs.close();
            pst.close();
            
            return resultlist;

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
            List<Reiziger> resultlist = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");
                Calendar cal = Calendar.getInstance();
                cal.setTime(geboortedatum);
                Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, cal);
                resultlist.add(reiziger);
            }

            for(Reiziger reiziger : resultlist){
                Adres adres = adao.findByReiziger(reiziger);
                List<OVChipkaart> ovkaarten = ovdao.findByReiziger(reiziger);

                if(ovkaarten.size() > 0){
                    for(OVChipkaart ovkaart : ovkaarten){
                        reiziger.addCard(ovkaart);
                        ovkaart.setReiziger(reiziger);
                    }
                }
                if(adres != null){
                    reiziger.setAdres(adres);
                    adres.setReiziger(reiziger);
                }
            }
            // sluit alles netjes af
            rs.close();
            st.close();

            return resultlist;

        }catch(SQLException sql){
            // geef error terug in error console
            Utils.errorHandler(sql, conn);

            // return false
            return null;
        }
    }
}
