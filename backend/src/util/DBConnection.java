package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/ATC_DB";
        String user = "atc_user";
        String password = "atc123";

        return DriverManager.getConnection(url, user, password);
    }
}
