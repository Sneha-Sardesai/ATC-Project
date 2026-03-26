import java.sql.*;

public class CreateDB {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/ATC_DB";
        String user = "root";
        String password = "Yunijoseph@05";   // your MySQL password

        try {
            // Create Connection
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL Database!");

            // Call stored procedure
            CallableStatement cs = con.prepareCall("{CALL GetEmergencyFlights()}");

            ResultSet rs = cs.executeQuery();

            // Display Data
            System.out.println("Emergency Flights:");
            while (rs.next()) {
                int id = rs.getInt("Flight_ID");
                String type = rs.getString("Emergency_Type");
                int priority = rs.getInt("Priority_Level");

                System.out.println(id + " | " + type + " | Priority: " + priority);
            }

            // Close connection
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

