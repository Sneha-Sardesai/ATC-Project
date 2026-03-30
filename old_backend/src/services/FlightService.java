package services;

import dao.FlightDAO;

public class FlightService {

    private FlightDAO flightDAO = new FlightDAO();

    public void scheduleNormalFlight(int flightId, int aircraftId, String airline) {
        flightDAO.addFlight(flightId, "Scheduled", aircraftId);
        flightDAO.addNormalFlight(flightId, airline);
        flightDAO.updateFlightStatus(flightId, "Scheduled");
    }

    public void landFlight(int flightId, int runwayId) {
        flightDAO.assignRunway(flightId, runwayId);
        flightDAO.updateFlightStatus(flightId, "Landed");
    }
}