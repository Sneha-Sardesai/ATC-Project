package service;

import dao.FlightDAO;
import model.Flight;

/**
 * Action to assign a gate to a flight
 */
public class AssignGateAction implements FlightAction {
    private final int gateId;
    private final FlightDAO flightDAO;

    public AssignGateAction(int gateId, FlightDAO flightDAO) {
        this.gateId = gateId;
        this.flightDAO = flightDAO;
    }

    @Override
    public void execute(Flight flight) throws Exception {
        flightDAO.assignGateDirect(flight.getId(), gateId);
        flight.setGateId(gateId);
        flight.setStatus(model.FlightStatus.GATE_ASSIGNED);
    }
}