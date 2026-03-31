package app;

import model.EmergencyType;
import service.ATCService;
import service.ControllerSession;

public class App {

    public static void main(String[] args) {

        // Simulated controller login
        ControllerSession session = new ControllerSession(1, "Amit");

        ATCService service = new ATCService(session);

        System.out.println("Welcome Controller " + session.getControllerName());

        // SYSTEM adds flight automatically (not user-typed random junk)
        service.systemAddFlight(501, 1);

        // Controller declares emergency
        service.declareEmergency(501, EmergencyType.MEDICAL, 1);

        // Controller actions
        service.assignRunway(501, 2);
        service.assignGate(501, 5);

        System.out.println("Operations completed successfully.");
    }
}