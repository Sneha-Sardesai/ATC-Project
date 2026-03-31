package db;

public class DBConfig {

    public static final String URL =
        "jdbc:mysql://localhost:3306/ATC_DB?useSSL=false&serverTimezone=UTC";

    public static final String USER = "root";
    public static final String PASSWORD = "your_mysql_password_here";

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
}