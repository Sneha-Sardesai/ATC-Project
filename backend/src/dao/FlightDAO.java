package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightDAO extends BaseDAO {

    public void addFlight(int flightId, String status,
                          int aircraftId, int runwayId, int gateId)
            throws SQLException {

        String sql = "{CALL AddFlight(?,?,?,?,?)}";

        try (Connection conn = getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.setInt(3, aircraftId);
            cs.setInt(4, runwayId);
            cs.setInt(5, gateId);

            cs.execute();
        }
    }

    public ResultSet getEmergencyFlights() throws SQLException {
        String sql = "{CALL GetEmergencyFlights()}";
        Connection conn = getConnection();
        CallableStatement cs = conn.prepareCall(sql);
        return cs.executeQuery();
    }

    public void declareEmergency(int flightId, String type, int priority)
            throws SQLException {

        String sql = "{CALL DeclareEmergency(?,?,?)}";

        try (Connection conn = getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, type);
            cs.setInt(3, priority);
            cs.execute();
        }
    }
}