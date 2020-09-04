package forreal;


import forreal.DAO.AdresDAO;
import forreal.DAO.ReizigersDAO;
import forreal.Domein.Adres;
import forreal.Domein.Reiziger;
import forreal.SQL.AdresDAOPsql;
import forreal.SQL.ReizigersDAOPsql;

import java.sql.*;
import java.util.List;

public class Main {
    private Connection conn;

    public static void main(String [] args) {

        try{
            // initialiseer de verbinding
            Connection conn = getConnection("localhost", "ovchip");

            // maak het DAO object en voeg de connectie toe
            AdresDAOPsql adaop = new AdresDAOPsql(conn);
            ReizigersDAOPsql rdaop = new ReizigersDAOPsql(conn);


            testReizigerDAO(rdaop);
            testAdresDAO(adaop, rdaop);
            // sluit de verbinding
            closeConnection(conn);

        }catch (SQLException e) {
            // print een nette foutmelding
            System.err.println("[SQL EXCEPTION] Query could not be performed. " + e.getMessage());
        }

    }
    private static Connection getConnection(String host, String db) throws SQLException {
        String uri = "jdbc:postgresql://"+ host + "/"+ db +"?user=postgres&password=Bloempot1";
         return DriverManager.getConnection(uri);
    }

    private static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }
    private static void testReizigerDAO(ReizigersDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database en toon deze op het scherm ter controle
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger();
        sietske.setId(11);
        sietske.setVoorletters("S");
        sietske.setTussenvoegsel("");
        sietske.setAchternaam("Boers");
        sietske.setGeboortedatum(gbdatum);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);

        // Haal alle reizigers op uit de database en toon deze op het scherm ter controle
        reizigers = rdao.findAll();

        // toon size van reizigers
        System.out.println(reizigers.size() + " reizigers");
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }

        // Test van de update functie. Gebortedatum sietske wordt aagepast.
        System.out.print("[Test] Eerst \n" + sietske.toString() + "\n na ReizigerDAO.update() \n");
        String gbdatum2 = "1981-03-12";
        sietske.setGeboortedatum(gbdatum2);

        rdao.update(sietske);
        Reiziger sietskeUpdated = rdao.findById(sietske.getId());
        System.out.println(sietskeUpdated.toString());

        // Haal alle reizigers op uit de database en toon deze op het scherm ter controle
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void testAdresDAO(AdresDAO adao, ReizigersDAO rdao) throws SQLException {

        System.out.println("\n---------- Test AdresDAO -------------");
        System.out.println( );

        // Haal alle adressen op uit de database en toon deze op het scherm ter controle
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] ADAO.findAll() geeft de volgende adressen:");
        for (Adres adres1 : adressen) {
            System.out.println(adres1.toString());
        }
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger();
        sietske.setId(11);
        sietske.setVoorletters("S");
        sietske.setTussenvoegsel("");
        sietske.setAchternaam("Boers");
        sietske.setGeboortedatum(gbdatum);
        rdao.save(sietske);


        // Maak een nieuwe adres aan en persisteer deze in de database
        Adres adres = new Adres();
        adres.setAdres_id(11);
        adres.setWoonplaats("Amersfoort");
        adres.setPostcode("3811 JH");
        adres.setStraat("Achter de kamp");
        adres.setHuisnummer("136");
        adres.setReiziger_id(sietske.getId());



        System.out.println();
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(adres);

        // Haal alle adressen op uit de database en toon deze op het scherm ter controle
        adressen = adao.findAll();
        // toon lengte adressen
        System.out.println(adressen.size());

        System.out.println("[Test] ADAO.findAll() geeft de volgende adressen:");
        for (Adres adres1 : adressen) {
            System.out.println(adres1.toString());
        }
        System.out.println();

        // Test de update functie. Woonplaats wordt aangepast
        System.out.print("[Test] Eerst \n" + adres.toString() + "\n na AdresDAO.update(): ");

        adres.setWoonplaats("Amsterdam");
        adao.update(adres);

        Adres AdresNieuw = adao.findById(adres.getAdres_id());
        System.out.println(AdresNieuw.toString());

        // Haal alle adressen op uit de database en toon deze op het scherm ter controle
        adressen = adao.findAll();
        // weergeef lengte adressen
        System.out.println(adressen.size());

        System.out.println("[Test] ADAO.findAll() geeft de volgende adressen:");
        for (Adres adres1 : adressen) {
            System.out.println(adres1.toString());
        }
        System.out.println();

        //Test de delete functie. adres wordt verwijdert, sietske wordt weer verwijdert
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete(): ");
        adao.delete(adres);
        rdao.delete(sietske);
        adressen = adao.findAll();
        System.out.println(adressen.size());

        System.out.println("[Test] ADAO.findAll() geeft de volgende adressen:");
        for (Adres adres1 : adressen) {
            System.out.println(adres1.toString());
        }

    }
}
