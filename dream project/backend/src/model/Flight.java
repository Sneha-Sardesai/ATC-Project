package model;

public class Flight implements Persistable {

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
    public int getAircraftId() { return aircraftId; }
    public int getRunwayId() { return runwayId; }
    public int getGateId() { return gateId; }

    public void setFlightId(int flightId) { this.flightId = flightId; }
    public void setStatus(FlightStatus status) { this.status = status; }
    public void setAircraftId(int aircraftId) { this.aircraftId = aircraftId; }
    public void setRunwayId(int runwayId) { this.runwayId = runwayId; }
    public void setGateId(int gateId) { this.gateId = gateId; }

    // Persistable interface implementation
    @Override
    public int getId() { return flightId; }

    @Override
    public void setId(int id) { this.flightId = id; }
}