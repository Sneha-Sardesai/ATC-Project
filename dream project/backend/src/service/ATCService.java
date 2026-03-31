package service;

import dao.FlightDAO;
import enums.EmergencyType;
import enums.FlightStatus;

public class ATCService {

    private final FlightDAO flightDAO = new FlightDAO();

    // SYSTEM creates flights, not user typing random data
    public void registerIncomingFlight(int flightId,
                                        int aircraftId,
                                        int runwayId,
                                        int gateId) {

        try {
            flightDAO.addFlight(
                    flightId,
                    FlightStatus.APPROACHING.name(),
                    aircraftId,
                    runwayId,
                    gateId
            );
        } catch (Exception e) {
            System.out.println("Flight registration failed.");
        }
    }

    // Controller action
    public void declareEmergency(int flightId,
                                 EmergencyType type,
                                 int priority) {

        try {
            flightDAO.declareEmergency(
                    flightId,
                    type.name(),
                    priority
            );

            // Also update flight status
            // (later you’ll use a procedure for this)
        } catch (Exception e) {
            System.out.println("Emergency declaration failed.");
        }
    }
}