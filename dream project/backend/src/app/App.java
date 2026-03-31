package app;

import service.ATCService;
import service.ControllerSession;
import model.EmergencyType;

public class App {

    public static void main(String[] args) {

        // Simulated login
        ControllerSession session =
                new ControllerSession(1, "Amit");

        ATCService service = new ATCService(session);

        System.out.println("Welcome Controller " + session.getControllerName());

        // Example flow
        service.systemAddFlight(501, 1);

        service.declareEmergency(
                501,
                EmergencyType.MEDICAL,
                1
        );

        service.assignRunway(501, 2);
        service.assignGate(501, 5);

        service.viewMyFlights();
    }
}