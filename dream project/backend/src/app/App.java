package app;

import service.ATCService;
import service.ControllerSession;
import model.EmergencyType;
import model.FlightStatus;

public class App {

    public static void main(String[] args) {

        System.out.println("====== ATC SYSTEM INITIALIZING ======");

        // Simulated login
        ControllerSession session =
                new ControllerSession(1, "Amit");

        ATCService service = new ATCService(session);

        System.out.println("Welcome Controller " +
                session.getControllerName() + "!");

        try {

            // ===== SYSTEM FLOW =====
            service.systemAddFlight(501, 1);

            // ===== EMERGENCY FLOW =====
            System.out.println("--- ALARM ---");
            System.out.println("Pilot of Flight 501 declares MEDICAL emergency!");

            service.declareEmergency(
                    501,
                    EmergencyType.MEDICAL,
                    1
            );

            // IMPORTANT: update status BEFORE assignments
            service.updateFlightStatus(
                    501,
                    FlightStatus.EMERGENCY
            );

            // ===== CONTROLLER ACTIONS =====
            service.assignRunway(501, 2);
            service.assignGate(501, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("====== ATC SYSTEM SHUTDOWN ======");
    }
}