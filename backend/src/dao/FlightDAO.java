package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.DBConnection;
import model.FlightInfo;

public class FlightDAO {

    // ===== EXISTING METHODS (kept) =====

    public boolean addFlight(int flightId, String status, int aircraftId, int runwayId, int gateId) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL AddFlight(?, ?, ?, ?, ?)}");
            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.setInt(3, aircraftId);
            cs.setInt(4, runwayId);
            cs.setInt(5, gateId);
            cs.execute();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public void declareEmergency(int flightId, String type, int priority) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL DeclareEmergency(?, ?, ?)}");
            cs.setInt(1, flightId);
            cs.setString(2, type);
            cs.setInt(3, priority);
            cs.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void getEmergencyFlights() {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL GetEmergencyFlights()}");
            ResultSet rs = cs.executeQuery();
            System.out.println("\n--- Emergency Flights ---");
            while (rs.next()) {
                System.out.println("Flight ID: " + rs.getInt("Flight_ID") +
                    ", Type: " + rs.getString("Emergency_Type") +
                    ", Priority: " + rs.getInt("Priority_Level"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void assignRunway(int flightId, int runwayId) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL AssignRunway(?, ?)}");
            cs.setInt(1, flightId);
            cs.setInt(2, runwayId);
            cs.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void assignGate(int flightId, int gateId) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL AssignGate(?, ?)}");
            cs.setInt(1, flightId);
            cs.setInt(2, gateId);
            cs.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateFlightStatus(int flightId, String status) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL UpdateFlightStatus(?, ?)}");
            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.execute();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean assignController(int assignmentId, int flightId, int controllerId) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL AssignController(?, ?, ?)}");
            cs.setInt(1, assignmentId);
            cs.setInt(2, flightId);
            cs.setInt(3, controllerId);
            cs.execute();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean addStatusLog(int logId, int flightId, String status) {
        try {
            Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall("{CALL AddStatusLog(?, ?, ?)}");
            cs.setInt(1, logId);
            cs.setInt(2, flightId);
            cs.setString(3, status);
            cs.execute();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ===== NEW METHODS FOR SWING UI =====

    // Validate controller login
    public String validateController(int controllerId, String name) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT Name FROM CONTROLLER WHERE Controller_ID = ? AND Name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, controllerId);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("Name");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Get controller role
    public String getControllerRole(int controllerId) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT Role FROM CONTROLLER WHERE Controller_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, controllerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("Role");
        } catch (Exception e) { e.printStackTrace(); }
        return "Controller";
    }

    // Get flights assigned to a controller (with all details)
    public List<FlightInfo> getControllerFlights(int controllerId) {
        List<FlightInfo> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT f.Flight_ID, f.Status, f.Scheduled_Time, " +
                "f.Aircraft_ID, a.Model, f.Runway_ID, r.Runway_Code, " +
                "f.Gate_ID, g.Gate_Number, g.Terminal, " +
                "e.Emergency_Type, e.Priority_Level " +
                "FROM FLIGHT f " +
                "JOIN FLIGHT_CONTROLLER_ASSIGNMENT fca ON f.Flight_ID = fca.Flight_ID " +
                "LEFT JOIN AIRCRAFT a ON f.Aircraft_ID = a.Aircraft_ID " +
                "LEFT JOIN RUNWAY r ON f.Runway_ID = r.Runway_ID " +
                "LEFT JOIN GATE g ON f.Gate_ID = g.Gate_ID " +
                "LEFT JOIN EMERGENCY_FLIGHT e ON f.Flight_ID = e.Flight_ID " +
                "WHERE fca.Controller_ID = ? " +
                "ORDER BY e.Priority_Level IS NULL, e.Priority_Level ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, controllerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FlightInfo fi = new FlightInfo(rs.getInt("Flight_ID"), rs.getString("Status"));
                fi.scheduledTime = rs.getString("Scheduled_Time");
                fi.aircraftId = rs.getInt("Aircraft_ID");
                fi.aircraftModel = rs.getString("Model");
                fi.runwayId = rs.getInt("Runway_ID");
                fi.runwayCode = rs.getString("Runway_Code");
                fi.gateId = rs.getInt("Gate_ID");
                fi.gateNumber = rs.getString("Gate_Number");
                fi.terminal = rs.getString("Terminal");
                String eType = rs.getString("Emergency_Type");
                if (eType != null) {
                    fi.isEmergency = true;
                    fi.emergencyType = eType;
                    fi.priorityLevel = rs.getInt("Priority_Level");
                }
                list.add(fi);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Get all runways
    public List<String[]> getRunways() {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT Runway_ID, Runway_Code, Status FROM RUNWAY");
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("Runway_ID")),
                    rs.getString("Runway_Code"),
                    rs.getString("Status")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Get all gates
    public List<String[]> getGates() {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT Gate_ID, Gate_Number, Terminal, Status FROM GATE");
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("Gate_ID")),
                    rs.getString("Gate_Number"),
                    rs.getString("Terminal"),
                    rs.getString("Status")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}