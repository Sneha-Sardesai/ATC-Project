package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import db.DBConnection;

public class FlightDAO {

    // =========================
    // ADD FLIGHT (SYSTEM)
    // =========================
    public void addFlight(
            int flightId,
            String status,
            int aircraftId,
            Integer runwayId,
            Integer gateId
    ) throws SQLException {

        String sql = "{CALL AddFlight(?, ?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.setInt(3, aircraftId);

            if (runwayId == null)
                cs.setNull(4, java.sql.Types.INTEGER);
            else
                cs.setInt(4, runwayId);

            if (gateId == null)
                cs.setNull(5, java.sql.Types.INTEGER);
            else
                cs.setInt(5, gateId);

            cs.execute();
        }
    }

    // =========================
    // ASSIGN RUNWAY
    // =========================
    public void assignRunway(int flightId, int runwayId) throws SQLException {
        String sql = "{CALL AssignRunway(?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setInt(2, runwayId);
            cs.execute();
        }
    }

    // =========================
    // ASSIGN GATE
    // =========================
    public void assignGate(int flightId, int gateId) throws SQLException {
        String sql = "{CALL AssignGate(?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setInt(2, gateId);
            cs.execute();
        }
    }

    // =========================
    // UPDATE FLIGHT STATUS
    // =========================
    public void updateFlightStatus(int flightId, String status) throws SQLException {
        String sql = "{CALL UpdateFlightStatus(?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.execute();
        }
    }
}