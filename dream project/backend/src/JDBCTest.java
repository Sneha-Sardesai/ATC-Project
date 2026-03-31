import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class JDBCTest {
 public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/ATC_DB";
        String user = "root";
        String password = "OpenEveryDoor2386*";  // your mysql password

        try {

            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create Connection
            Connection con = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to MySQL Database!");

            // Create Statement
            Statement stmt = con.createStatement();

            // Execute Query
            ResultSet rs = stmt.executeQuery("SELECT * FROM aircraft");

            // Display Data
            while (rs.next()) {
                int id = rs.getInt("Aircraft_ID");
                String model = rs.getString("model");
                int capacity = rs.getInt("capacity");
                String type = rs.getString("type");

                System.out.println(id + " " + model + " " + capacity + " " + type);
            }

            // Close Connection
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}

