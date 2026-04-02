package app;

import api.SimpleServer;
import service.ATCService;
import service.ControllerSession;
import simulator.FlightSimulator;

public class App {

    public static void main(String[] args) {

        System.out.println("====== ATC SYSTEM INITIALIZING ======");

        // Start the flight simulator (system-driven flight generation)
        ControllerSession dummySession = new ControllerSession(0, "System");
        ATCService systemService = new ATCService(dummySession);
        FlightSimulator simulator = new FlightSimulator(systemService);
        simulator.start();

        // Start the API server
        try {
            SimpleServer.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}