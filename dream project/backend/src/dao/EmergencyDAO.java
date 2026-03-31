package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import db.DBConnection;

public class EmergencyDAO {

    public void declareEmergency(
            int flightId,
            String emergencyType,
            int priority
    ) throws SQLException {

        String sql = "{CALL DeclareEmergency(?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, emergencyType);
            cs.setInt(3, priority);
            cs.execute();
        }
    }
}