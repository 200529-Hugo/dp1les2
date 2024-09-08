package nl.hu.dp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private final Connection conn;

    // Constructor
    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    // Sla een reiziger op in de database
    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ps.setString(2, reiziger.getVoorletters());
            ps.setString(3, reiziger.getTussenvoegsel());
            ps.setString(4, reiziger.getAchternaam());
            ps.setDate(5, reiziger.getGeboortedatum());
            return ps.executeUpdate() > 0;
        }
    }

    // Update een reiziger in de database
    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, reiziger.getVoorletters());
            ps.setString(2, reiziger.getTussenvoegsel());
            ps.setString(3, reiziger.getAchternaam());
            ps.setDate(4, reiziger.getGeboortedatum());
            ps.setInt(5, reiziger.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Verwijder een reiziger uit de database
    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Haal een reiziger op uit de database door een id
    @Override
    public Reiziger findById(int id) throws SQLException {
        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractReizigerFromResultSet(rs);
            }
        }
        return null;
    }

    // Haal een lijst van reizigers op uit de database door een geboortedatum
    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(datum));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reiziger reiziger = extractReizigerFromResultSet(rs);
                reizigers.add(reiziger);
            }
        }
        return reizigers;
    }

    // Haal een lijst van reizigers op uit de database
    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        String query = "SELECT * FROM reiziger";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reiziger reiziger = extractReizigerFromResultSet(rs);
                reizigers.add(reiziger);
            }
        }
        return reizigers;
    }

//    Convert een result set naar een reiziger object
    private Reiziger extractReizigerFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("reiziger_id");
        String voorletters = rs.getString("voorletters");
        String tussenvoegsel = rs.getString("tussenvoegsel");
        String achternaam = rs.getString("achternaam");
        Date geboortedatum = rs.getDate("geboortedatum");

        return new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
    }
}

