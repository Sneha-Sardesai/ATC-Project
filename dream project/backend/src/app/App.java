package app;

import api.SimpleServer;
import java.util.Scanner;
import java.util.List;
import model.EmergencyType;
import model.FlightStatus;
import model.Flight;
import service.ATCService;
import service.ControllerSession;

public class App {

    private static Scanner scanner;
    private static ATCService atcService;

    public static void main(String[] args) {
        // Start the API server in a background thread
        Thread serverThread = new Thread(() -> {
            try {
                SimpleServer.main(args);
            } catch (Exception e) {
                System.err.println("Failed to start server: " + e.getMessage());
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        // Give server a moment to start
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scanner = new Scanner(System.in);
        System.out.println("=== ATC Flight Management System ===");
        System.out.println("API Server started in background on port 8080+");

        // Initialize ATC Service with a dummy session (no authentication needed for flight management)
        ControllerSession dummySession = new ControllerSession(1, "System");
        atcService = new ATCService(dummySession);

        // Main menu loop
        while (true) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addFlight();
                    break;
                case 2:
                    viewAllFlights();
                    break;
                case 3:
                    viewFlightDetails();
                    break;
                case 4:
                    assignRunway();
                    break;
                case 5:
                    assignGate();
                    break;
                case 6:
                    updateFlightStatus();
                    break;
                case 7:
                    declareEmergency();
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== FLIGHT MANAGEMENT MENU ===");
        System.out.println("1. Add New Flight");
        System.out.println("2. View All Flights");
        System.out.println("3. View Flight Details");
        System.out.println("4. Assign Runway");
        System.out.println("5. Assign Gate");
        System.out.println("6. Update Flight Status");
        System.out.println("7. Declare Emergency");
        System.out.println("8. Exit");
    }

    private static void addFlight() {
        System.out.println("\n=== ADD NEW FLIGHT ===");

        // Auto-assign flight ID
        int flightId = getNextFlightId();
        System.out.println("Auto-assigned Flight ID: " + flightId);

        // Get flight status
        System.out.println("Available statuses:");
        for (FlightStatus status : FlightStatus.values()) {
            System.out.println("- " + status.name());
        }
        System.out.print("Enter flight status: ");
        String statusInput = scanner.nextLine().toUpperCase();
        FlightStatus status;
        try {
            status = FlightStatus.valueOf(statusInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Using APPROACHING as default.");
            status = FlightStatus.APPROACHING;
        }

        // Get aircraft ID
        int aircraftId = getIntInput("Enter aircraft ID (101-103): ");

        // Get runway ID (optional)
        int runwayId = getIntInput("Enter runway ID (or 0 for none): ");
        Integer runwayIdNullable = (runwayId == 0) ? null : runwayId;

        // Get gate ID (optional)
        int gateId = getIntInput("Enter gate ID (or 0 for none): ");
        Integer gateIdNullable = (gateId == 0) ? null : gateId;

        // Add the flight
        atcService.systemAddFlight(flightId, status.name(), aircraftId, runwayIdNullable, gateIdNullable);
        System.out.println("Flight added successfully!");
    }

    private static void viewAllFlights() {
        System.out.println("\n=== ALL FLIGHTS ===");
        // For a system-wide view, we need to get all flights, not just assigned ones
        // Since we don't have authentication, we'll show all flights in the system
        try {
            dao.FlightDAO flightDAO = new dao.FlightDAO();
            List<Flight> allFlights = flightDAO.getAllFlights();

            if (allFlights.isEmpty()) {
                System.out.println("No flights in the system.");
                return;
            }

            System.out.printf("%-10s %-15s %-12s %-10s %-10s%n", "Flight ID", "Status", "Aircraft ID", "Runway", "Gate");
            System.out.println("-------------------------------------------------------------");

            for (Flight flight : allFlights) {
                System.out.printf("%-10d %-15s %-12d %-10s %-10s%n",
                    flight.getFlightId(),
                    flight.getStatus().name(),
                    flight.getAircraftId(),
                    flight.getRunwayId() == 0 ? "N/A" : String.valueOf(flight.getRunwayId()),
                    flight.getGateId() == 0 ? "N/A" : String.valueOf(flight.getGateId())
                );
            }
        } catch (Exception e) {
            System.out.println("Error retrieving flights: " + e.getMessage());
        }
    }

    private static void viewFlightDetails() {
        System.out.println("\n=== VIEW FLIGHT DETAILS ===");
        int flightId = getIntInput("Enter Flight ID: ");

        Flight flight = atcService.getFlightDetails(flightId);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.println("Flight Details:");
        System.out.println("ID: " + flight.getFlightId());
        System.out.println("Status: " + flight.getStatus().name());
        System.out.println("Aircraft ID: " + flight.getAircraftId());
        System.out.println("Runway ID: " + (flight.getRunwayId() == 0 ? "Not assigned" : flight.getRunwayId()));
        System.out.println("Gate ID: " + (flight.getGateId() == 0 ? "Not assigned" : flight.getGateId()));
    }

    private static void assignRunway() {
        System.out.println("\n=== ASSIGN RUNWAY ===");
        int flightId = getIntInput("Enter Flight ID: ");
        int runwayId = getIntInput("Enter Runway ID: ");

        atcService.assignRunway(flightId, runwayId);
        System.out.println("Runway assigned successfully!");
    }

    private static void assignGate() {
        System.out.println("\n=== ASSIGN GATE ===");
        int flightId = getIntInput("Enter Flight ID: ");
        int gateId = getIntInput("Enter Gate ID: ");

        atcService.assignGate(flightId, gateId);
        System.out.println("Gate assigned successfully!");
    }

    private static void updateFlightStatus() {
        System.out.println("\n=== UPDATE FLIGHT STATUS ===");
        int flightId = getIntInput("Enter Flight ID: ");

        System.out.println("Available statuses:");
        for (FlightStatus status : FlightStatus.values()) {
            System.out.println("- " + status.name());
        }
        System.out.print("Enter new status: ");
        String statusInput = scanner.nextLine().toUpperCase();

        try {
            FlightStatus status = FlightStatus.valueOf(statusInput);
            atcService.updateFlightStatus(flightId, status);
            System.out.println("Status updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status.");
        }
    }

    private static void declareEmergency() {
        System.out.println("\n=== DECLARE EMERGENCY ===");
        int flightId = getIntInput("Enter Flight ID: ");

        System.out.println("Emergency Types:");
        for (EmergencyType type : EmergencyType.values()) {
            System.out.println("- " + type.name());
        }
        System.out.print("Enter emergency type: ");
        String typeInput = scanner.nextLine().toUpperCase();

        int priority = getIntInput("Enter priority (1-5): ");

        try {
            EmergencyType type = EmergencyType.valueOf(typeInput);
            atcService.declareEmergency(flightId, type, priority);
            atcService.updateFlightStatus(flightId, FlightStatus.EMERGENCY);
            System.out.println("Emergency declared successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid emergency type.");
        }
    }

    private static int getNextFlightId() {
        try {
            dao.FlightDAO flightDAO = new dao.FlightDAO();
            return flightDAO.getMaxFlightId() + 1;
        } catch (Exception e) {
            return 1000; // fallback
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Legacy method for flight simulator (keeping for compatibility)
    private static void startFlightSimulator() {
        System.out.println("Flight Simulator functionality has been replaced with manual flight entry.");
        System.out.println("Use option 1 to add flights manually.");
    }
}