import java.util.*;

// ---- Controller Class ----
class Controller {
    int id;
    String name;
    String role;

    Controller(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}

// ---- Aircraft Class ----
class Aircraft {
    int id;
    String model;
    String type;

    Aircraft(int id, String model, String type) {
        this.id = id;
        this.model = model;
        this.type = type;
    }
}

// ---- Flight Class ----
class Flight {
    int id;
    String status;
    int aircraftId;
    Integer runwayId;
    Integer gateId;

    Flight(int id, String status, int aircraftId) {
        this.id = id;
        this.status = status;
        this.aircraftId = aircraftId;
        this.runwayId = null;
        this.gateId = null;
    }
}

// ---- Runway Class ----
class Runway {
    int id;
    String code;
    String status;

    Runway(int id, String code, String status) {
        this.id = id;
        this.code = code;
        this.status = status;
    }
}

// ---- Gate Class ----
class Gate {
    int id;
    String number;

    Gate(int id, String number) {
        this.id = id;
        this.number = number;
    }
}

// ---- Main System ----
public class ATCSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Controller> controllers = new ArrayList<>();
    static ArrayList<Aircraft> aircrafts = new ArrayList<>();
    static ArrayList<Flight> flights = new ArrayList<>();
    static ArrayList<Runway> runways = new ArrayList<>();
    static ArrayList<Gate> gates = new ArrayList<>();

    static Controller currentController = null;

    public static void main(String[] args) {

        loadData();

        if (login()) {
            dashboard();
        } else {
            System.out.println("Invalid Login!");
        }
    }

    // ---- Load Sample Data ----
    static void loadData() {
        controllers.add(new Controller(1, "Ria", "Senior"));

        aircrafts.add(new Aircraft(1, "Boeing 737", "Passenger"));
        aircrafts.add(new Aircraft(2, "Airbus A320", "Passenger"));

        flights.add(new Flight(101, "Approaching", 1));
        flights.add(new Flight(102, "Scheduled", 2));

        runways.add(new Runway(1, "RW-09L", "Available"));
        runways.add(new Runway(2, "RW-27R", "Available"));

        gates.add(new Gate(1, "G1"));
        gates.add(new Gate(2, "G2"));
    }

    // ---- Login ----
    static boolean login() {
        System.out.print("Enter Controller ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        for (Controller c : controllers) {
            if (c.id == id && c.name.equalsIgnoreCase(name)) {
                currentController = c;
                return true;
            }
        }
        return false;
    }

    // ---- Dashboard ----
    static void dashboard() {
        while (true) {
            System.out.println("\n===== ATC DASHBOARD =====");
            System.out.println("1. View Flights");
            System.out.println("2. Assign Runway");
            System.out.println("3. Assign Gate");
            System.out.println("4. Update Flight Status");
            System.out.println("5. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1: viewFlights(); break;
                case 2: assignRunway(); break;
                case 3: assignGate(); break;
                case 4: updateStatus(); break;
                case 5: return;
            }
        }
    }

    // ---- View Flights ----
    static void viewFlights() {
        for (Flight f : flights) {
            System.out.println("Flight #" + f.id + " | Status: " + f.status);
        }
    }

    // ---- Assign Runway ----
    static void assignRunway() {
        System.out.print("Enter Flight ID: ");
        int id = sc.nextInt();

        Flight f = getFlight(id);
        if (f == null) return;

        System.out.println("Available Runways:");
        for (Runway r : runways) {
            System.out.println(r.id + ". " + r.code);
        }

        System.out.print("Select Runway ID: ");
        int rwId = sc.nextInt();

        f.runwayId = rwId;
        System.out.println("Runway assigned successfully!");
    }

    // ---- Assign Gate ----
    static void assignGate() {
        System.out.print("Enter Flight ID: ");
        int id = sc.nextInt();

        Flight f = getFlight(id);
        if (f == null) return;

        System.out.println("Available Gates:");
        for (Gate g : gates) {
            System.out.println(g.id + ". " + g.number);
        }

        System.out.print("Select Gate ID: ");
        int gId = sc.nextInt();

        f.gateId = gId;
        System.out.println("Gate assigned successfully!");
    }

    // ---- Update Status ----
    static void updateStatus() {
        System.out.print("Enter Flight ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Flight f = getFlight(id);
        if (f == null) return;

        System.out.print("Enter New Status: ");
        String status = sc.nextLine();

        f.status = status;
        System.out.println("Status Updated!");
    }

    // ---- Helper ----
    static Flight getFlight(int id) {
        for (Flight f : flights) {
            if (f.id == id) return f;
        }
        System.out.println("Flight not found!");
        return null;
    }
}