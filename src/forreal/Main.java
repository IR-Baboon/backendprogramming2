package forreal;

import forreal.DAO.ReizigersDAO;
import forreal.Domein.Reiziger;
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
            ReizigersDAOPsql rdaop = new ReizigersDAOPsql(conn);

            testReizigerDAO(rdaop);
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

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(84, "S", "", "Boers", gbdatum);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
}
