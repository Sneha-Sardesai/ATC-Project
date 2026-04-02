package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Controller;

public class ControllerDAO {
    
    public Controller login(int controllerId) throws SQLException {
        String sql = "SELECT * FROM controllers WHERE controller_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, controllerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Controller(rs.getInt("controller_id"), rs.getString("name"));
            }
        }
        return null;
    }

    public Controller login(String name, String password) throws SQLException {
        String sql = "SELECT * FROM controllers WHERE name = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Controller(rs.getInt("controller_id"), rs.getString("name"));
            }
        }
        return null;
    }

    public boolean signup(String name, String password) throws SQLException {
        String sql = "INSERT INTO controllers (name, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Fallback for databases where controller_id is not AUTO_INCREMENT
            if (e.getMessage().toLowerCase().contains("controller_id") && e.getMessage().toLowerCase().contains("default")) {
                System.err.println("Signup fallback (manual ID generation) due to missing AUTO_INCREMENT: " + e.getMessage());
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement seq = conn.prepareStatement("SELECT MAX(controller_id) AS max_id FROM controllers")) {
                    int nextId = 1;
                    try (ResultSet rs = seq.executeQuery()) {
                        if (rs.next()) {
                            nextId = rs.getInt("max_id") + 1;
                        }
                    }
                    String fallbackSql = "INSERT INTO controllers (controller_id, name, password) VALUES (?, ?, ?)";
                    try (PreparedStatement ps2 = conn.prepareStatement(fallbackSql)) {
                        ps2.setInt(1, nextId);
                        ps2.setString(2, name);
                        ps2.setString(3, password);
                        return ps2.executeUpdate() > 0;
                    }
                }
            }
            throw e;
        }
    }

    public List<Controller> getAllControllers() throws SQLException {
        List<Controller> list = new ArrayList<>();
        String sql = "SELECT * FROM controllers";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Controller(rs.getInt("controller_id"), rs.getString("name")));
            }
        }
        return list;
    }
}
