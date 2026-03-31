package service;

import dao.FlightDAO;
import dao.EmergencyDAO;
import model.EmergencyType;
import model.FlightStatus;

import java.sql.SQLException;

public class ATCService {

    private FlightDAO flightDAO;
    private EmergencyDAO emergencyDAO;
    private ControllerSession session;

    public ATCService(ControllerSession session) {
        this.session = session;
        this.flightDAO = new FlightDAO();
        this.emergencyDAO = new EmergencyDAO();
    }

    // SYSTEM creates flight (controller does NOT type everything)
    public void systemAddFlight(int flightId, int aircraftId) {
        try {
            flightDAO.addFlight(
                    flightId,
                    FlightStatus.APPROACHING.name(),
                    aircraftId,
                    null,
                    null
            );
            System.out.println("Flight " + flightId + " added by system.");
        } catch (SQLException e) {
            System.out.println("Failed to add flight.");
            e.printStackTrace();
        }
    }

    // Controller declares emergency
    public void declareEmergency(int flightId, EmergencyType type, int priority) {
        try {
            emergencyDAO.declareEmergency(flightId, type.name(), priority);
            System.out.println("Emergency declared for flight " + flightId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Controller assigns runway
    public void assignRunway(int flightId, int runwayId) {
        try {
            flightDAO.assignRunway(flightId, runwayId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Controller assigns gate
    public void assignGate(int flightId, int gateId) {
        try {
            flightDAO.assignGate(flightId, gateId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Controller updates flight status
    public void updateFlightStatus(int flightId, FlightStatus status) {
        try {
            flightDAO.updateFlightStatus(flightId, status.name());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}