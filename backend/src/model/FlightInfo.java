package model;

public class FlightInfo {
    public int flightId;
    public String status;
    public String scheduledTime;
    public int aircraftId;
    public String aircraftModel;
    public int runwayId;
    public String runwayCode;
    public int gateId;
    public String gateNumber;
    public String terminal;
    public boolean isEmergency;
    public String emergencyType;
    public int priorityLevel;

    public FlightInfo(int flightId, String status) {
        this.flightId = flightId;
        this.status = status;
    }
}
