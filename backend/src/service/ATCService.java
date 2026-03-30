package service;

import dao.FlightDAO;

public class ATCService {

    private FlightDAO flightDAO;

    public ATCService() {
        flightDAO = new FlightDAO();
    }

    // 1. Add new flight
    public void addNewFlight(int flightId, String status,
                             int aircraftId, int runwayId, int gateId) {
        flightDAO.addFlight(flightId, status, aircraftId, runwayId, gateId);
        System.out.println("Flight added successfully.");
    }

    // 2. Declare emergency
    public void declareEmergency(int flightId, String type, int priority) {
        flightDAO.declareEmergency(flightId, type, priority);
        System.out.println("Emergency declared.");
    }

    // 3. View emergency flights
    public void viewEmergencyFlights() {
        flightDAO.getEmergencyFlights();
    }

    // 4. Assign runway
    public void assignRunway(int flightId, int runwayId) {
        flightDAO.assignRunway(flightId, runwayId);
        System.out.println("Runway assigned.");
    }

    // 5. Assign gate
    public void assignGate(int flightId, int gateId) {
        flightDAO.assignGate(flightId, gateId);
        System.out.println("Gate assigned.");
    }

    // 6. Update flight status
    public void updateFlightStatus(int flightId, String status) {
        flightDAO.updateFlightStatus(flightId, status);
        System.out.println("Flight status updated.");
    }
}