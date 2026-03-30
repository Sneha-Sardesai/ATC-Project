package app;
import java.util.Scanner;
import db.DBConnection;   // ✅ THIS LINE FIXES THE RED ERROR

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Test DB connection
        DBConnection.getConnection();

        while (true) {
            System.out.println("\n===== ATC Management System =====");
            System.out.println("1. Add New Flight");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            if (choice == 0) {
                System.out.println("Exiting...");
                sc.close();
                System.exit(0);
            }
        }
    }
}