package service;

import dao.FlightDAO;
import dao.EmergencyDAO;
import model.FlightStatus;
import model.EmergencyType;

public class ATCService {

    private FlightDAO flightDAO = new FlightDAO();
    private EmergencyDAO emergencyDAO = new EmergencyDAO();

    private ControllerSession session;

    public ATCService(ControllerSession session) {
        this.session = session;
    }

    // ===============================
    // SYSTEM creates flights automatically
    // ===============================
    public void systemAddFlight(int flightId, int aircraftId) {

        flightDAO.addFlight(
                flightId,
                FlightStatus.APPROACHING.name(),
                aircraftId,
                null,   // runwayId (Integer)
                null    // gateId (Integer)
        );

        System.out.println("System added flight " + flightId);
    }

    // ===============================
    // Controller declares emergency
    // ===============================
    public void declareEmergency(int flightId, EmergencyType type, int priority) {

        emergencyDAO.declareEmergency(
                flightId,
                type.name(),
                priority
        );

        flightDAO.updateFlightStatus(
                flightId,
                FlightStatus.EMERGENCY.name()
        );

        System.out.println("Emergency declared for flight " + flightId);
    }

    // ===============================
    // Controller assigns runway
    // ===============================
    public void assignRunway(int flightId, int runwayId) {

        flightDAO.assignRunway(flightId, runwayId);

        System.out.println("Runway " + runwayId + " assigned to flight " + flightId);
    }

    // ===============================
    // Controller assigns gate
    // ===============================
    public void assignGate(int flightId, int gateId) {

        flightDAO.assignGate(flightId, gateId);

        System.out.println("Gate " + gateId + " assigned to flight " + flightId);
    }
}