package services;

import dao.FlightDAO;

public class FlightService {

    private FlightDAO flightDAO = new FlightDAO();

    public void scheduleNormalFlight(int flightId, int aircraftId, String airline) {

        // Business rule: flight must be created first
        flightDAO.addFlight(flightId, "Scheduled", aircraftId);

        // Business rule: classify as normal
        flightDAO.addnormalflight(flightId, airline);

        // Business rule: initial status log
        flightDAO.updateFlightStatus(flightId, "Scheduled");
    }

    public void handleEmergency(int flightId, int aircraftId, String type, int priority) {

        flightDAO.addFlight(flightId, "Emergency", aircraftId);
        flightDAO.addEmergency(flightId, type, priority);

        flightDAO.updateFlightStatus(flightId, "Emergency Declared");
    }

    public void landFlight(int flightId, int runwayId) {

        flightDAO.assignRunway(flightId, runwayId);
        flightDAO.updateFlightStatus(flightId, "Landed");
    }
}