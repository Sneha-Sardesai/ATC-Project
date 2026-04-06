import java.sql.*;

public class DebugLogin {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ATC_DB";
        String user = "root";
        String password = "roo123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected!\n");

            // Show all controllers
            System.out.println("=== ALL CONTROLLERS ===");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM CONTROLLER");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Controller_ID") +
                    ", Name: '" + rs.getString("Name") + "'" +
                    ", Role: " + rs.getString("Role") +
                    ", Status: " + rs.getString("Status"));
            }

            // Test validate query
            System.out.println("\n=== TEST LOGIN: ID=1, Name='Ria' ===");
            PreparedStatement ps = conn.prepareStatement(
                "SELECT Name FROM CONTROLLER WHERE Controller_ID = ? AND Name = ?");
            ps.setInt(1, 1);
            ps.setString(2, "Ria");
            ResultSet rs2 = ps.executeQuery();
            if (rs2.next()) {
                System.out.println("LOGIN SUCCESS! Name: " + rs2.getString("Name"));
            } else {
                System.out.println("LOGIN FAILED - no match found");
            }

            // Show all flights + assignments
            System.out.println("\n=== ALL FLIGHTS ===");
            ResultSet rs3 = conn.createStatement().executeQuery("SELECT * FROM FLIGHT");
            while (rs3.next()) {
                System.out.println("Flight: " + rs3.getInt("Flight_ID") +
                    ", Status: " + rs3.getString("Status"));
            }

            System.out.println("\n=== FLIGHT ASSIGNMENTS ===");
            ResultSet rs4 = conn.createStatement().executeQuery("SELECT * FROM FLIGHT_CONTROLLER_ASSIGNMENT");
            while (rs4.next()) {
                System.out.println("Assignment: " + rs4.getInt("Assignment_ID") +
                    ", Flight: " + rs4.getInt("Flight_ID") +
                    ", Controller: " + rs4.getInt("Controller_ID"));
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
