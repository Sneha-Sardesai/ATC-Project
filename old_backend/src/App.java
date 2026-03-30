import services.FlightService;

public class App {
    public static void main(String[] args) {
        FlightService service = new FlightService();

        service.scheduleNormalFlight(500, 1, "Indigo");
        service.landFlight(500, 1);
    }
}

