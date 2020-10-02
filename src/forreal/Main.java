package forreal;


import forreal.DAO.AdresDAO;
import forreal.DAO.OVChipkaartDAO;
import forreal.DAO.ProductDAO;
import forreal.DAO.ReizigerDAO;
import forreal.Domein.Adres;
import forreal.Domein.OVChipkaart;
import forreal.Domein.Product;
import forreal.Domein.Reiziger;
import forreal.SQL.AdresDAOPsql;
import forreal.SQL.OVChipkaartDAOPsql;
import forreal.SQL.ProductDAOPsql;
import forreal.SQL.ReizigerDAOPsql;


import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private Connection conn;

    public static void main(String [] args) {

        try{
            // initialiseer de verbinding
            Connection conn = getConnection("localhost", "ovchip");
//             maak het DAO object en voeg de connectie toe
            ReizigerDAOPsql rdaop = new ReizigerDAOPsql(conn);
            AdresDAOPsql adaop = new AdresDAOPsql(conn, rdaop);
            OVChipkaartDAOPsql ovdaop = new OVChipkaartDAOPsql(conn, rdaop);
            ProductDAOPsql pdaop = new ProductDAOPsql(conn);
            testReizigerDAO(rdaop);
            testAdresDAO(adaop, rdaop);
            testOVChipkaartDAO(ovdaop, rdaop, pdaop);
            testProductDAO(ovdaop, pdaop, rdaop);
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

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum));
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", cal);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);

        // Haal alle reizigers op uit de database en toon deze op het scherm ter controle
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }

        // Test van de update functie. Gebortedatum sietske wordt aagepast.
        System.out.print("[Test] Eerst \n" + sietske.toString() + "\n na ReizigerDAO.update() \n");
        String gbdatum2 = "1981-03-12";
        cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum2));
        sietske.setGeboortedatum(cal);

        rdao.update(sietske);
        Reiziger sietskeUpdated = rdao.findById(sietske.getReizigerId());
        System.out.println(sietskeUpdated.toString());

        // Haal alle reizigers op uit de database en toon deze op het scherm ter controle.
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
    public static void testOVChipkaartDAO(OVChipkaartDAO ovdaop, ReizigerDAO rdao, ProductDAO pdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        System.out.println( );

        // Maak ook een reiziger aan voor test van relatie persistentie.
        System.out.println("Reiziger wordt aangemaakt en gepersisteerd zonder adres en ovchipkaart. ");
        String gbdatum = "1981-03-14";
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum));
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", cal);
        rdao.save(sietske);

        // Haal alle ovkaarten op uit de database en toon deze op het scherm ter controle.
        List<OVChipkaart> ovkaarten = ovdaop.findAll();
        System.out.println("[Test] OVDAO.findAll() geeft de volgende ovkaarten:");

        for (OVChipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }

        // haal de ov kaarten van de reiziger die we hebben angemaakt op vanuit de database
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        if(ovkaarten.size() == 0){
            System.out.println("Reiziger heeft geen ov kaarten");
        }
        for (OVChipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }


        // Maak een nieuwe OVKaart aan en persisteer deze in de database
        gbdatum = "2025-03-14";
        cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum));
        OVChipkaart ovkaart = new OVChipkaart(11, cal, 2, 55.60);
        sietske.addCard(ovkaart);
        ovkaart.setReiziger(sietske);
        ovdaop.save(ovkaart);
        // test of de persistentie gelukt is

        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        for (OVChipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        System.out.println(sietske.toString());

        System.out.println("En de test van de OVDAO.update functie. ");

        ovkaart.setKlasse(1);
        Product product = new Product(11,  "test", "product 1", 00.00);
        pdao.save(product);
        ovkaart.addProduct(product);
        System.out.println("kaart 1 wordt via OVDAO gepersisteerd. ");
        ovdaop.update(ovkaart);

        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        for (OVChipkaart kaart : ovkaarten) {

            System.out.println(kaart.toString());
        }
        System.out.println(sietske.toString());

        System.out.println("test waarbij ovchipkaart wordt verwijdert.");
        ovdaop.delete(ovkaart);
        sietske.removeCard(ovkaart);
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findByReiziger(sietske);
        System.out.println("[Test] OVDAO.findByReiziger() geeft de volgende ovkaarten:");
        for (OVChipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        System.out.println(rdao.findById(sietske.getReizigerId()));

        // reiziger cleanup
        rdao.delete(sietske);

        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        ovkaarten = ovdaop.findAll();
        System.out.println("[Test] OVDAO.findAll() geeft naverwijdering van sietske de volgende ovkaarten:");
        for (OVChipkaart kaart : ovkaarten) {
            System.out.println(kaart.toString());
        }
        pdao.delete(product);
    }
    public static void testProductDAO(OVChipkaartDAO ovdaop, ProductDAO pdao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");
        System.out.println("\n---------- Test Product OVkaart relatie opslag -------------");

        // Maak een Reiziger en OVChipkaart aan en sla deze op zodat producten kunnen worden toegevoegd.
        String gbdatum = "1981-03-14";
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum));
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", cal);

        gbdatum = "2025-03-14";
        cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum));
        OVChipkaart ovkaart = new OVChipkaart(11, cal, 2, 55.60);
        sietske.addCard(ovkaart);
        ovkaart.setReiziger(sietske);
        rdao.save(sietske);
        System.out.println();
        // Haal alle producten op uit de database en toon deze op het scherm ter controle.
        List<Product> producten = pdao.findAll();
        System.out.println("[Test] PDAO.findAll() geeft de volgende producten:");
        for (Product product1 : producten) {
            System.out.println(product1.toString());
        }
        System.out.println();
        // Maak een Product aan voor de opslagtest pdao.save().
        System.out.println("Product wordt aangemaakt en gepersisteerd zonder ovchipkaart. ");
        Product product1 = new Product(11, "test product DAO", "DAO test pdao.save()", 0.00);
//        product1.addOvkaart(ovkaart.getKaartnummer());
        pdao.save(product1);
        System.out.println();
        // Haal alle producten op uit de database en toon deze op het scherm ter controle.
        producten = pdao.findAll();
        System.out.println("[Test] PDAO.findAll() geeft na productopslag de volgende producten:");
        for (Product product : producten) {
            System.out.println(product.toString());
        }
        System.out.println();
        System.out.println("Product wordt aangepast en gepersisteerd ");

        ovkaart.addProduct(product1);
        ovdaop.update(ovkaart);
        product1.addOvkaart(ovkaart.getKaartnummer());
        pdao.update(product1);

        System.out.println();
        //controleer de persistentie
        producten = pdao.findByOVChipkaart(ovkaart.getKaartnummer());
        System.out.println("[Test] PDAO.findByOVChipkaart() geeft na update van product de volgende producten:");
        for (Product product : producten) {
            System.out.println(product.toString());
        }
        System.out.println();
        // Pas het product aan en sla het op met pdao.update().
        product1.setNaam("Update test pdao.update()");
        pdao.update(product1);
        System.out.println();
        // Haal alle producten op uit de database en toon deze op het scherm ter controle.
        producten = pdao.findAll();
        System.out.println("[Test] PDAO.findAll() geeft na update van product de volgende producten:");
        for (Product product : producten) {
            System.out.println(product.toString());
        }

        System.out.println();
        System.out.println("[Test] PDAO.delete()");
        // verwijder product van kaart.
        pdao.delete(product1);
        // Haal alle producten op uit de database en toon deze op het scherm ter controle.
        producten = pdao.findAll();
        System.out.println("[Test] PDAO.findAll() geeft na verwijdering van product de volgende producten:");
        for (Product product : producten) {
            System.out.println(product.toString());
        }
        System.out.println();
        // delete ovKaart
        ovdaop.delete(ovkaart);
        rdao.delete(sietske);

    }
    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {

        System.out.println("\n---------- Test AdresDAO -------------");
        System.out.println( );

        // Haal alle adressen op uit de database en toon deze op het scherm ter controle. Maak ook een reiziger aan voor test van relatie persistentie.
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] ADAO.findAll() geeft de volgende adressen:");
        for (Adres adres1 : adressen) {
            System.out.println(adres1.toString());
        }
        String gbdatum = "1981-03-14";
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(gbdatum));
        Reiziger sietske = new Reiziger(11, "S", "", "Boers", cal);
        rdao.save(sietske);


        // Maak een nieuwe adres aan en persisteer deze in de database
        Adres adres = new Adres(11, "3811 JH","136","Achter de kamp","Amersfoort", sietske.getReizigerId());

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

        //Test de delete functie. adres wordt verwijdert, sietske wordt weer verwijdert. Eindstand van adressen wordt weergeven.
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
