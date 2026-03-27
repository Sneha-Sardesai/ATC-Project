
import dao.AircraftDAO;
import dao.FlightDAO;

public class App {
    public static void main(String[] args) {

        AircraftDAO a = new AircraftDAO();
        FlightDAO f = new FlightDAO();


// 1. Add a new flight
f.addFlight(500, "Scheduled", 1);

// 2. Mark it as normal OR emergency (choose one)
f.addnormalflight(500, "Indigo");
// f.addEmergency(400, "Medical", 1);

// 3. View that flight
f.getFlightById(500);

// 4. Update status (this will ALSO log automatically now)
f.updateFlightStatus(500, "Approaching");

// 5. Update again (to test multiple logs)
f.updateFlightStatus(500, "Landed");

// 6. Assign runway
f.assignRunway(500, 1);

// 7. View all flights
f.viewAllFlights();

// 8. View specific categories
f.viewNormalFlights();
f.viewEmergencyFlights();

        
        
    }

}
