package forreal;


import forreal.DAO.AdresDAO;
import forreal.DAO.OVChipkaartDAO;
import forreal.DAO.ReizigersDAO;
import forreal.Domein.Adres;
import forreal.Domein.OV_Chipkaart;
import forreal.Domein.Product;
import forreal.Domein.Reiziger;
import forreal.SQL.AdresDAOPsql;
import forreal.SQL.OVChipkaartDAOPsql;
import forreal.SQL.ReizigersDAOPsql;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private Connection conn;

    public static void main(String [] args) {

        try{
            // initialiseer de verbinding
            Connection conn = getConnection("localhost", "ovchip");

//            Reiziger reiziger = new Reiziger(11, "a", "", "Comes", LocalDate.parse("1990-04-24"));
//            Adres adres = new Adres(11, "3811JH", "136", "Achter de kamp", "Amersforot", reiziger.getId());
//            OV_Chipkaart ovkaart = new OV_Chipkaart(11, LocalDate.parse("2025-06-30"), 2, 55.60, reiziger.getId());
//            OV_Chipkaart ovkaart2 = new OV_Chipkaart(12, LocalDate.parse("2025-07-30"), 1, 75.60, reiziger.getId());
//            Product weekendreis = new Product(11, "WeekendreisAntiKorting", "Met dit product reist u in het weekend met een toelage van 10000 euro.", 0.01);

//            System.out.println(reiziger);
//            reiziger.setAdres(adres);
//            adres.setReiziger(reiziger);
//            reiziger.addCard(ovkaart);
//            ovkaart.setReiziger(reiziger);
//            reiziger.addCard(ovkaart2);
//            ovkaart2.setReiziger(reiziger);
//
//            System.out.println(reiziger.toString());
//            System.out.println(ovkaart.toString());




//             maak het DAO object en voeg de connectie toe
            AdresDAOPsql adaop = new AdresDAOPsql(conn);
            ReizigersDAOPsql rdaop = new ReizigersDAOPsql(conn);
            OVChipkaartDAOPsql ovdaop = new OVChipkaartDAOPsql(conn);

            testReizigerDAO(rdaop);
            testAdresDAO(adaop, rdaop);
            testOVChipkaartDAO(ovdaop, rdaop);
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
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", LocalDate.parse(gbdatum));
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

        // Haal alle reizigers op uit de database en toon deze op het scherm ter controle.
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
    public static void testOVChipkaartDAO(OVChipkaartDAO ovdaop, ReizigersDAO rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        System.out.println( );

        // Maak ook een reiziger aan voor test van relatie persistentie.
        System.out.println("Reiziger wordt aangemaakt en gepersisteerd zonder adres en ovchipkaart. ");
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", LocalDate.parse(gbdatum));
        rdao.save(sietske);

        // Haal alle ovkaarten op uit de database en toon deze op het scherm ter controle.
        List<OV_Chipkaart> ovkaarten = ovdaop.findAll();
        System.out.println("[Test] OVDAO.findAll() geeft de volgende ovkaarten:");
        if(ovkaarten.size() == 0){
            System.out.println("Reiziger heeft geen ov kaarten");
        }
        for (OV_Chipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        if(ovkaarten.size() == 0){
            System.out.println("Reiziger heeft geen ov kaarten");
        }
        for (OV_Chipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }


        // Maak een nieuwe OVKaart aan en persisteer deze in de database
        OV_Chipkaart ovkaart = new OV_Chipkaart(11, LocalDate.parse("2025-06-30"), 2, 55.60, sietske.getId());
        OV_Chipkaart ovkaart2 = new OV_Chipkaart(12, LocalDate.parse("2025-07-30"), 1, 75.60, sietske.getId());
        sietske.addCard(ovkaart);
        ovkaart.setReiziger(sietske);
        sietske.addCard(ovkaart2);
        ovkaart2.setReiziger(sietske);

        rdao.update(sietske);
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        for (OV_Chipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        System.out.println(sietske.toString());

        System.out.println("En de test van de OVDAO.update functie. ");
        ovkaart2.setSaldo(0.55);
        ovkaart.setKlasse(1);

        System.out.println("kaart 1 wordt via OVDAO gepersisteerd en de ander middels de reizigersDAO.");
        ovdaop.update(ovkaart);
        rdao.update(sietske);

        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        for (OV_Chipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        System.out.println(sietske.toString());
        System.out.println("test waarbij ovchipkaarten worden verwijdert. 1 via de ovdao en 1 samen met de reiziger.");
        ovdaop.delete(ovkaart);
        sietske.removeCard(ovkaart);
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        for (OV_Chipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        System.out.println(sietske.toString());

        System.out.println("test wijst uit dat er nog maar 1 ovChipkaart hoort bij deze reiziger. ");
        rdao.delete(sietske);

        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findAll();
        System.out.println("[Test] OVDAO.findAll() geeft naverwijdering van sietske de volgende ovkaarten:");
        for (OV_Chipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
    }

    private static void testAdresDAO(AdresDAO adao, ReizigersDAO rdao) throws SQLException {

        System.out.println("\n---------- Test AdresDAO -------------");
        System.out.println( );

        // Haal alle adressen op uit de database en toon deze op het scherm ter controle. Maak ook een reiziger aan voor test van relatie persistentie.
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] ADAO.findAll() geeft de volgende adressen:");
        for (Adres adres1 : adressen) {
            System.out.println(adres1.toString());
        }
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", LocalDate.parse(gbdatum));
        rdao.save(sietske);


        // Maak een nieuwe adres aan en persisteer deze in de database
        Adres adres = new Adres(11, "3811 JH","136","Achter de kamp","Amersfoort", sietske.getId());

        adres.setReiziger(sietske);
        sietske.setAdres(adres);


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

        Adres AdresNieuw = adao.findByReiziger(sietske);
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
