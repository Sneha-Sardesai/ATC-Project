package app;

import service.ATCService;
import service.ControllerSession;
import simulator.FlightSimulator;
import model.EmergencyType;
import model.FlightStatus;

public class App {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("====== ATC SYSTEM INITIALIZING ======");

        // 1. Controller Authenticaton
        ControllerSession session = ATCService.loginController(1); // Amit's ID
        if (session == null) {
            System.err.println("Login failed. Exiting...");
            return;
        }
        
        System.out.println("Welcome Controller " + session.getControllerName() + "!");
        
        ATCService service = new ATCService(session);

        // 2. Start Background System Flight Generator
        FlightSimulator simulator = new FlightSimulator(service);
        simulator.start();

        // 3. Let the system run and generate some flights for a little while
        System.out.println("System is running. Watching radar...");
        Thread.sleep(20000); // Wait 20 seconds, should generate 1 or 2 flights
        
        // 4. Controller receives word that flight 500 (pre-seeded) has a medical emergency
        System.out.println("\n--- ALARM ---");
        System.out.println("Pilot of Flight 500 declares MEDICAL emergency!");
        service.declareEmergency(500, EmergencyType.MEDICAL, 2);

        // Controller takes action on emergency flight
        service.assignRunway(500, 1);
        service.assignGate(500, 2);
        
        Thread.sleep(15000); // Let it run a bit more to see background system generate another flight
        
        simulator.stop();
        System.out.println("====== ATC SYSTEM SHUTDOWN ======");
    }
}