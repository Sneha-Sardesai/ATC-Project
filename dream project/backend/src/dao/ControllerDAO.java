package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db.DBConnection;
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
