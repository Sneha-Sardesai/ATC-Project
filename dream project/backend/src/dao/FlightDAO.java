package dao;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

import db.DBConnection;

public class FlightDAO {

    // 1. Add Flight
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

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // 2. Declare Emergency
    public void declareEmergency(int flightId, String type, int priority) {
        try {
            Connection conn = DBConnection.getConnection();

            CallableStatement cs = conn.prepareCall("{CALL DeclareEmergency(?, ?, ?)}");
            cs.setInt(1, flightId);
            cs.setString(2, type);
            cs.setInt(3, priority);

            cs.execute();
            System.out.println("Emergency declared!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Get Emergency Flights
    public void getEmergencyFlights() {
        try {
            Connection conn = DBConnection.getConnection();

            CallableStatement cs = conn.prepareCall("{CALL GetEmergencyFlights()}");
            ResultSet rs = cs.executeQuery();

            System.out.println("\n--- Emergency Flights ---");
            while (rs.next()) {
                int id = rs.getInt("Flight_ID");
                String type = rs.getString("Emergency_Type");
                int priority = rs.getInt("Priority_Level");

                System.out.println("Flight ID: " + id +
                                   ", Type: " + type +
                                   ", Priority: " + priority);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Assign Runway
    public void assignRunway(int flightId, int runwayId) {
        try {
            Connection conn = DBConnection.getConnection();

            CallableStatement cs = conn.prepareCall("{CALL AssignRunway(?, ?)}");
            cs.setInt(1, flightId);
            cs.setInt(2, runwayId);

            cs.execute();
            System.out.println("Runway assigned!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. Assign Gate
    public void assignGate(int flightId, int gateId) {
        try {
            Connection conn = DBConnection.getConnection();

            CallableStatement cs = conn.prepareCall("{CALL AssignGate(?, ?)}");
            cs.setInt(1, flightId);
            cs.setInt(2, gateId);

            cs.execute();
            System.out.println("Gate assigned!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. Update Flight Status
    public void updateFlightStatus(int flightId, String status) {
        try {
            Connection conn = DBConnection.getConnection();

            CallableStatement cs = conn.prepareCall("{CALL UpdateFlightStatus(?, ?)}");
            cs.setInt(1, flightId);
            cs.setString(2, status);

            cs.execute();
            System.out.println("Flight status updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
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

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

}