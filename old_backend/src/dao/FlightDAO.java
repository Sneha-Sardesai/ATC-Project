package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import util.DBConnection;

public class FlightDAO {

    public void addFlight(int flightId, String status, int aircraftId) {
        try (Connection con = DBConnection.getConnection()) {
            CallableStatement cs =
                con.prepareCall("{CALL AddFlight(?, ?, ?, NULL, NULL)}");
            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.setInt(3, aircraftId);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNormalFlight(int flightId, String airline) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps =
                con.prepareStatement("INSERT INTO NORMAL_FLIGHT VALUES (?, ?)");
            ps.setInt(1, flightId);
            ps.setString(2, airline);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void assignRunway(int flightId, int runwayId) {
        try (Connection con = DBConnection.getConnection()) {
            CallableStatement cs =
                con.prepareCall("{CALL AssignRunway(?, ?)}");
            cs.setInt(1, flightId);
            cs.setInt(2, runwayId);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFlightStatus(int flightId, String status) {
        try (Connection con = DBConnection.getConnection()) {
            CallableStatement cs =
                con.prepareCall("{CALL UpdateFlightStatus(?, ?)}");
            cs.setInt(1, flightId);
            cs.setString(2, status);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}