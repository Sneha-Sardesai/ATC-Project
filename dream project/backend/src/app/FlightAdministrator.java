package app;

import service.ATCService;
import service.ControllerSession;
import dao.FlightDAO;
import model.Flight;
import model.FlightStatus;
import model.EmergencyType;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;

public class FlightAdministrator {

    private ATCService atcService;
    private FlightDAO flightDAO;
    private Scanner scanner;
    private int nextFlightId;

    public FlightAdministrator(ATCService atcService) {
        this.atcService = atcService;
        this.flightDAO = new FlightDAO();
        this.scanner = new Scanner(System.in);
        
        // Get max flight ID from database
        try {
            this.nextFlightId = flightDAO.getMaxFlightId() + 1;
        } catch (Exception e) {
            this.nextFlightId = 1000;
        }
    }

    public void startMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n========================================");
            System.out.println("       FLIGHT ADMINISTRATION MENU");
            System.out.println("========================================");
            System.out.println("1. Add New Flight");
            System.out.println("2. View All Flights");
            System.out.println("3. Update Flight Status");
            System.out.println("4. Declare Emergency");
            System.out.println("5. Assign Runway to Flight");
            System.out.println("6. Assign Gate to Flight");
            System.out.println("7. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice (1-7): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        addNewFlight();
                        break;
                    case 2:
                        viewAllFlights();
                        break;
                    case 3:
                        updateFlightStatus();
                        break;
                    case 4:
                        declareEmergency();
                        break;
                    case 5:
                        assignRunway();
                        break;
                    case 6:
                        assignGate();
                        break;
                    case 7:
                        running = false;
                        System.out.println("\nShutting down Flight Administration System...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void addNewFlight() {
        System.out.println("\n--- ADD NEW FLIGHT ---");
        
        // Auto-assign flight ID
        System.out.println("Flight ID will be auto-assigned: " + nextFlightId);
        
        // Get aircraft ID
        System.out.print("Enter Aircraft ID (101, 102, or 103): ");
        int aircraftId = scanner.nextInt();
        scanner.nextLine();
        
        // Get initial status
        System.out.println("\nAvailable Flight Statuses:");
        System.out.println("1. APPROACHING");
        System.out.println("2. HOLDING");
        System.out.println("3. TAXIING");
        System.out.println("4. GATE_ASSIGNED");
        System.out.println("5. LANDED");
        System.out.println("6. EMERGENCY");
        System.out.print("Choose status (1-6): ");
        
        String status;
        int statusChoice = scanner.nextInt();
        scanner.nextLine();
        
        switch (statusChoice) {
            case 1: status = "APPROACHING"; break;
            case 2: status = "HOLDING"; break;
            case 3: status = "TAXIING"; break;
            case 4: status = "GATE_ASSIGNED"; break;
            case 5: status = "LANDED"; break;
            case 6: status = "EMERGENCY"; break;
            default: status = "APPROACHING";
        }
        
        try {
            atcService.systemAddFlight(nextFlightId, status, aircraftId, null, null);
            System.out.println("✓ Flight " + nextFlightId + " added successfully!");
            System.out.println("  - Aircraft ID: " + aircraftId);
            System.out.println("  - Initial Status: " + status);
            nextFlightId++;
            
            // Ask if user wants to declare emergency
            if (status.equals("EMERGENCY")) {
                System.out.print("\nDeclare emergency now? (y/n): ");
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    declareEmergencyForFlight(nextFlightId - 1);
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error adding flight: " + e.getMessage());
        }
    }

    private void viewAllFlights() {
        System.out.println("\n--- ALL FLIGHTS ---");
        try {
            List<Flight> flights = flightDAO.getAllFlights();
            
            if (flights.isEmpty()) {
                System.out.println("No flights in the system.");
                return;
            }
            
            System.out.println(String.format("%-10s %-15s %-12s %-10s %-10s", 
                "Flight ID", "Aircraft ID", "Status", "Runway", "Gate"));
            System.out.println("================================================================");
            
            for (Flight f : flights) {
                String runwayStr = f.getRunwayId() == 0 ? "N/A" : String.valueOf(f.getRunwayId());
                String gateStr = f.getGateId() == 0 ? "N/A" : String.valueOf(f.getGateId());
                System.out.println(String.format("%-10d %-15d %-12s %-10s %-10s",
                    f.getFlightId(), 
                    f.getAircraftId(), 
                    f.getStatus(),
                    runwayStr,
                    gateStr));
            }
        } catch (SQLException e) {
            System.out.println("✗ Error retrieving flights: " + e.getMessage());
        }
    }

    private void updateFlightStatus() {
        System.out.println("\n--- UPDATE FLIGHT STATUS ---");
        System.out.print("Enter Flight ID: ");
        int flightId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("\nAvailable Statuses:");
        System.out.println("1. APPROACHING");
        System.out.println("2. HOLDING");
        System.out.println("3. TAXIING");
        System.out.println("4. GATE_ASSIGNED");
        System.out.println("5. LANDED");
        System.out.println("6. EMERGENCY");
        System.out.print("Choose new status (1-6): ");
        
        String newStatus;
        int statusChoice = scanner.nextInt();
        scanner.nextLine();
        
        switch (statusChoice) {
            case 1: newStatus = "APPROACHING"; break;
            case 2: newStatus = "HOLDING"; break;
            case 3: newStatus = "TAXIING"; break;
            case 4: newStatus = "GATE_ASSIGNED"; break;
            case 5: newStatus = "LANDED"; break;
            case 6: newStatus = "EMERGENCY"; break;
            default: newStatus = "APPROACHING";
        }
        
        try {
            atcService.updateFlightStatus(flightId, FlightStatus.valueOf(newStatus));
            System.out.println("✓ Flight " + flightId + " status updated to " + newStatus);
        } catch (Exception e) {
            System.out.println("✗ Error updating status: " + e.getMessage());
        }
    }

    private void declareEmergency() {
        System.out.println("\n--- DECLARE EMERGENCY ---");
        System.out.print("Enter Flight ID: ");
        int flightId = scanner.nextInt();
        scanner.nextLine();
        
        declareEmergencyForFlight(flightId);
    }

    private void declareEmergencyForFlight(int flightId) {
        System.out.println("\nAvailable Emergency Types:");
        EmergencyType[] types = EmergencyType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Choose emergency type (1-" + types.length + "): ");
        
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (typeChoice < 1 || typeChoice > types.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        EmergencyType emergencyType = types[typeChoice - 1];
        
        System.out.print("Enter Priority (1-5): ");
        int priority = scanner.nextInt();
        scanner.nextLine();
        
        if (priority < 1 || priority > 5) {
            priority = 3; // Default to medium priority
        }
        
        try {
            atcService.declareEmergency(flightId, emergencyType, priority);
            System.out.println("✓ Emergency declared for flight " + flightId);
            System.out.println("  - Type: " + emergencyType);
            System.out.println("  - Priority: " + priority);
        } catch (Exception e) {
            System.out.println("✗ Error declaring emergency: " + e.getMessage());
        }
    }

    private void assignRunway() {
        System.out.println("\n--- ASSIGN RUNWAY ---");
        System.out.print("Enter Flight ID: ");
        int flightId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter Runway ID: ");
        int runwayId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            atcService.assignRunway(flightId, runwayId);
            System.out.println("✓ Runway " + runwayId + " assigned to Flight " + flightId);
        } catch (Exception e) {
            System.out.println("✗ Error assigning runway: " + e.getMessage());
        }
    }

    private void assignGate() {
        System.out.println("\n--- ASSIGN GATE ---");
        System.out.print("Enter Flight ID: ");
        int flightId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter Gate ID: ");
        int gateId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            // First update status to LANDED to allow gate assignment
            atcService.updateFlightStatus(flightId, FlightStatus.LANDED);
            atcService.assignGate(flightId, gateId);
            System.out.println("✓ Gate " + gateId + " assigned to Flight " + flightId);
        } catch (Exception e) {
            System.out.println("✗ Error assigning gate: " + e.getMessage());
        }
    }
}
