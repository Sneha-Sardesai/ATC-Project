package dao;

import db.DBConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Flight;
import model.FlightStatus;

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

    // =========================
    // STALL APPROACHING FLIGHTS
    // =========================
    public void stallApproachingFlightsForController(int controllerId) throws SQLException {
        // Find all APPROACHING flights assigned to this controller and change their status to HOLDING
        String sql = "UPDATE flights f JOIN assignments a ON f.flight_id = a.flight_id " +
                     "SET f.status = 'HOLDING' " +
                     "WHERE a.controller_id = ? AND f.status = 'APPROACHING'";

        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, controllerId);
            ps.executeUpdate();
        }
    }

    // =========================
    // GET FLIGHTS FOR CONTROLLER
    // =========================
    public List<Flight> getFlightsForController(int controllerId) throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.flight_id, f.status, f.aircraft_id, f.runway_id, f.gate_id " +
                     "FROM flights f JOIN assignments a ON f.flight_id = a.flight_id " +
                     "WHERE a.controller_id = ?";

        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, controllerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight(
                    rs.getInt("flight_id"),
                    FlightStatus.valueOf(rs.getString("status")),
                    rs.getInt("aircraft_id"),
                    rs.getInt("runway_id"),
                    rs.getInt("gate_id")
                );
                flights.add(flight);
            }
        }
        return flights;
    }

    // =========================
    // GET MAX FLIGHT ID
    // =========================
    public int getMaxFlightId() throws SQLException {
        String sql = "SELECT MAX(flight_id) FROM flights";

        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // =========================
    // GET FLIGHT BY ID
    // =========================
    public Flight getFlightById(int flightId) throws SQLException {
        String sql = "SELECT flight_id, status, aircraft_id, runway_id, gate_id FROM flights WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Flight(
                    rs.getInt("flight_id"),
                    FlightStatus.valueOf(rs.getString("status")),
                    rs.getInt("aircraft_id"),
                    rs.getInt("runway_id"),
                    rs.getInt("gate_id")
                );
            }
        }
        return null;
    }

    // =========================
    // GET ALL FLIGHTS
    // =========================
    public List<Flight> getAllFlights() throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT flight_id, status, aircraft_id, runway_id, gate_id FROM flights ORDER BY flight_id";

        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Flight flight = new Flight(
                    rs.getInt("flight_id"),
                    FlightStatus.valueOf(rs.getString("status")),
                    rs.getInt("aircraft_id"),
                    rs.getInt("runway_id"),
                    rs.getInt("gate_id")
                );
                flights.add(flight);
            }
        }
        return flights;
    }

    // =========================
    // DELETE ALL FLIGHTS
    // =========================
    public void deleteAllFlights() throws SQLException {
        String sql = "DELETE FROM flights";

        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }
}