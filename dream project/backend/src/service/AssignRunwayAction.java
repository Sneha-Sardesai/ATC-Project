package service;

import dao.FlightDAO;
import model.Flight;

/**
 * Action to assign a runway to a flight
 */
public class AssignRunwayAction implements FlightAction {
    private final int runwayId;
    private final FlightDAO flightDAO;

    public AssignRunwayAction(int runwayId, FlightDAO flightDAO) {
        this.runwayId = runwayId;
        this.flightDAO = flightDAO;
    }

    @Override
    public void execute(Flight flight) throws Exception {
        flightDAO.assignRunway(flight.getId(), runwayId);
        flight.setRunwayId(runwayId);
    }
}