package app;
import java.util.Scanner;
import service.ATCService;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ATCService service = new ATCService();

        while (true) {
            System.out.println("\n===== ATC Management System =====");
            System.out.println("1. Add New Flight");
            System.out.println("2. Declare Emergency");
            System.out.println("3. View Emergency Flights");
            System.out.println("4. Assign Runway");
            System.out.println("5. Assign Gate");
            System.out.println("6. Update Flight Status");
            System.out.println("7. Assign Controller");
            System.out.println("8. Add Status Log");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Flight ID: ");
                    int fId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status: ");
                    String status = sc.nextLine();

                    System.out.print("Aircraft ID: ");
                    int aId = sc.nextInt();

                    System.out.print("Runway ID: ");
                    int rId = sc.nextInt();

                    System.out.print("Gate ID: ");
                    int gId = sc.nextInt();

                    service.addNewFlight(fId, status, aId, rId, gId);
                    break;

                case 2:
                    System.out.print("Flight ID: ");
                    int efId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Emergency Type: ");
                    String type = sc.nextLine();

                    System.out.print("Priority Level: ");
                    int priority = sc.nextInt();

                    service.declareEmergency(efId, type, priority);
                    break;

                case 3:
                    service.viewEmergencyFlights();
                    break;

                case 4:
                    System.out.print("Flight ID: ");
                    int frId = sc.nextInt();

                    System.out.print("Runway ID: ");
                    int runId = sc.nextInt();

                    service.assignRunway(frId, runId);
                    break;

                case 5:
                    System.out.print("Flight ID: ");
                    int fgId = sc.nextInt();

                    System.out.print("Gate ID: ");
                    int gateId = sc.nextInt();

                    service.assignGate(fgId, gateId);
                    break;

                case 6:
                    System.out.print("Flight ID: ");
                    int fsId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New Status: ");
                    String newStatus = sc.nextLine();

                    service.updateFlightStatus(fsId, newStatus);
                    break;

                case 7:
                    System.out.print("Assignment ID: ");
                    int asgId = sc.nextInt();

                    System.out.print("Flight ID: ");
                    int flId = sc.nextInt();

                    System.out.print("Controller ID: ");
                    int cId = sc.nextInt();

                    service.assignController(asgId, flId, cId);
                    break;

                case 8:
                    System.out.print("Log ID: ");
                    int logId = sc.nextInt();

                    System.out.print("Flight ID: ");
                    int lfId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status: ");
                    String logStatus = sc.nextLine();

                    service.addStatusLog(logId, lfId, logStatus);
                    break;

                case 0:
                    System.out.println("Exiting system.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}