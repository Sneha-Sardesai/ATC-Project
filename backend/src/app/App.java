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
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Flight ID: ");
                    int fid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Status: ");
                    String status = sc.nextLine();

                    System.out.print("Aircraft ID: ");
                    int aid = sc.nextInt();

                    System.out.print("Runway ID: ");
                    int rid = sc.nextInt();

                    System.out.print("Gate ID: ");
                    int gid = sc.nextInt();

                    service.addNewFlight(fid, status, aid, rid, gid);
                    break;

                case 2:
                    System.out.print("Flight ID: ");
                    int efid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Emergency Type: ");
                    String type = sc.nextLine();

                    System.out.print("Priority Level: ");
                    int priority = sc.nextInt();

                    service.declareEmergency(efid, type, priority);
                    break;

                case 3:
                    service.viewEmergencyFlights();
                    break;

                case 4:
                    System.out.print("Flight ID: ");
                    service.assignRunway(sc.nextInt(), sc.nextInt());
                    break;

                case 5:
                    System.out.print("Flight ID: ");
                    service.assignGate(sc.nextInt(), sc.nextInt());
                    break;

                case 6:
                    System.out.print("Flight ID: ");
                    int ufid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New Status: ");
                    service.updateFlightStatus(ufid, sc.nextLine());
                    break;

                case 0:
                    System.out.println("Exiting system...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}