package dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import db.DBConnection;

public class EmergencyDAO {

    public void declareEmergency(int flightId, String type, int priority) {
        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement stmt =
                conn.prepareCall("{CALL DeclareEmergency(?, ?, ?)}");

            stmt.setInt(1, flightId);
            stmt.setString(2, type);
            stmt.setInt(3, priority);

            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}