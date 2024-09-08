package nl.hu.dp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/ovchip?user=hugodeheus&password=root");
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(conn);

        testReizigerDAO(reizigerDAO);

        conn.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
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
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Test findById
        System.out.println("[Test] ReizigerDAO.findById(77) geeft de volgende reiziger:");
        System.out.println(rdao.findById(77) + "\n");

        // Test findByGbdatum
        reizigers = rdao.findByGbdatum("1981-03-14");
        System.out.println("[Test] ReizigerDAO.findByGbdatum(\"1981-03-14\") geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Test update
        sietske.setAchternaam("Boersma");
        System.out.print("[Test] Voor update: " + rdao.findById(77));
        rdao.update(sietske);
        System.out.println(", na update: " + rdao.findById(77) + "\n");

        // Test delete
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println("[Test] Na delete: " + reizigers.size() + " reizigers");
    }
}
