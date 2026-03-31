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

    // SYSTEM creates flights (not user typing random stuff)
    public void systemAddFlight(int flightId, int aircraftId) {
        flightDAO.addFlight(
            flightId,
            FlightStatus.APPROACHING.name(),
            aircraftId,
            null,
            null
        );
    }

    // Controller declares emergency
    public void declareEmergency(
            int flightId,
            EmergencyType type,
            int priority) {

        emergencyDAO.declareEmergency(
            flightId,
            type.name(),
            priority
        );

        flightDAO.updateFlightStatus(
            flightId,
            FlightStatus.EMERGENCY.name()
        );
    }

    // Controller assigns runway
    public void assignRunway(int flightId, int runwayId) {
        flightDAO.assignRunway(flightId, runwayId);
    }

    // Controller assigns gate
    public void assignGate(int flightId, int gateId) {
        flightDAO.assignGate(flightId, gateId);
    }

    // What THIS controller sees
    public void viewMyFlights() {
        flightDAO.getFlightsForController(session.getControllerId());
    }
}