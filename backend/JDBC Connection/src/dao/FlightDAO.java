package dao;

import java.sql.*;
import util.DBConnection;

public class FlightDAO {

    public void viewAllFlights() {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM FLIGHT");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("Flight_ID") + " " +
                    rs.getString("Status") + " " +
                    rs.getInt("Aircraft_ID")
                );
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addFlight(int flightId, String status, int aircraftId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO FLIGHT (Flight_ID, Status, Aircraft_ID) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, flightId);
            ps.setString(2, status);
            ps.setInt(3, aircraftId);

            ps.executeUpdate();

            System.out.println("Flight added successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addEmergency(int FLight_ID, String Emergency_Type, int Priority_Level ){
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO EMERGENCY_FLIGHT (Flight_ID, Emergency_Type, Priority_Level) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, FLight_ID);
            ps.setString(2, Emergency_Type);
            ps.setInt(3, Priority_Level);

            ps.executeUpdate();

            System.out.println("Emergency Flight Added Successfully");

            con.close();

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addnormalflight(int Flight_id, String Airline){
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO NORMAL_FLIGHT (Flight_ID, Airline) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Flight_id);
            ps.setString(2, Airline);

            ps.executeUpdate();
            System.out.println("Normal Flight Added Successfully");

            con.close();

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewNormalFlights(){
        try{
            Connection con = DBConnection.getConnection();

            String query = "SELECT f.Flight_ID, f.Status, n.Airline " +
                           "FROM FLIGHT f JOIN NORMAL_FLIGHT n " +
                           "ON f.Flight_ID = n.Flight_ID";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                System.out.println(
                    rs.getInt("Flight_ID") + " " +
                    rs.getString("Status") + " " +
                    rs.getString("Airline")
                ); 
            }

            con.close();

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewEmergencyFlights() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT f.Flight_ID, f.Status, e.Priority_Level " +
                           "FROM FLIGHT f JOIN EMERGENCY_FLIGHT e " +
                           "ON f.Flight_ID = e.Flight_ID";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(
                    rs.getInt("Flight_ID") + " " +
                    rs.getString("Status") + " " +
                    rs.getInt("Priority_Level")
                );
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateFlightStatus(int flight_Id, String newStatus){
        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE FLIGHT SET Status = ? WHERE Flight_ID = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newStatus);
            ps.setInt(2, flight_Id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Flight status updated!");
                logStatus(flight_Id, newStatus);

            } else {
                System.out.println("Flight not found.");
            }

            con.close();

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void logStatus(int flightId, String status) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO FLIGHT_STATUS_LOG (Flight_ID, Status, Timestamp) VALUES (?, ?, NOW())";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, flightId);
            ps.setString(2, status);

            ps.executeUpdate();

            System.out.println("Status logged successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void getFlightById(int flightId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM FLIGHT WHERE Flight_ID = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, flightId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println(
                    rs.getInt("Flight_ID") + " " +
                    rs.getString("Status") + " " +
                    rs.getInt("Aircraft_ID")
                );
            } else {
                System.out.println("Flight not found");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteFlight(int flightId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM FLIGHT WHERE Flight_ID = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, flightId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Flight deleted");
            } else {
                System.out.println("Flight not found");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void assignRunway(int flightId, int runwayId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE FLIGHT SET Runway_ID = ? WHERE Flight_ID = ?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, runwayId);
            ps.setInt(2, flightId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Runway assigned");
            } else {
                System.out.println("Flight not found");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}