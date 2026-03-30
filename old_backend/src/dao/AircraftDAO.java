package dao;

import java.sql.*;
import util.DBConnection;

public class AircraftDAO {

    public void viewAllAircraft() {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM AIRCRAFT");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("Aircraft_ID") + " " +
                    rs.getString("Model") + " " +
                    rs.getInt("Capacity") + " " +
                    rs.getString("Type")
                );
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}