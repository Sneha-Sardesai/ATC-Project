package dao;

import db.DBConnection;
import model.enums.FlightStatus;

import java.sql.Connection;
import java.sql.CallableStatement;

public class FlightDAO {

    public void addFlight(int flightId, int aircraftId) throws Exception {
        String sql = "{CALL AddFlight(?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, FlightStatus.APPROACHING.name());
            cs.setInt(3, aircraftId);

            cs.execute();
        }
    }

    public void updateFlightStatus(int flightId, FlightStatus status) throws Exception {
        String sql = "{CALL UpdateFlightStatus(?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setString(2, status.name());
            cs.execute();
        }
    }
}