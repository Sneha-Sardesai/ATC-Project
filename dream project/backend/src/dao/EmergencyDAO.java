package dao;

import db.DBConnection;
import model.enums.EmergencyType;

import java.sql.Connection;
import java.sql.CallableStatement;

public class EmergencyDAO {

    public void declareEmergency(
            int flightId,
            EmergencyType type,
            int priority
    ) throws Exception {

        String sql = "{CALL DeclareEmergency(?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, type.name());
            cs.setInt(3, priority);
            cs.execute();
        }
    }
}
