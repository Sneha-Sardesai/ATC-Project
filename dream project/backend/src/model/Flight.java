package model;

import enums.FlightStatus;

public class Flight {

    private int flightId;
    private FlightStatus status;
    private int aircraftId;
    private int runwayId;
    private int gateId;

    public Flight(int flightId, FlightStatus status,
                  int aircraftId, int runwayId, int gateId) {
        this.flightId = flightId;
        this.status = status;
        this.aircraftId = aircraftId;
        this.runwayId = runwayId;
        this.gateId = gateId;
    }

    public int getFlightId() { return flightId; }
    public FlightStatus getStatus() { return status; }
}