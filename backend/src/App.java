import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

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
                    System.out.println("Add Flight – logic coming next");
                    break;

                case 2:
                    System.out.println("Declare Emergency – logic coming next");
                    break;

                case 3:
                    System.out.println("View Emergency Flights – logic coming next");
                    break;

                case 4:
                    System.out.println("Assign Runway – logic coming next");
                    break;

                case 5:
                    System.out.println("Assign Gate – logic coming next");
                    break;

                case 6:
                    System.out.println("Update Flight Status – logic coming next");
                    break;

                case 0:
                    System.out.println("Exiting system...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}