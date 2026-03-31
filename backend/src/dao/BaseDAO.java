package dao;

import java.sql.Connection;
import java.sql.SQLException;
import db.DBConnection;

public abstract class BaseDAO {

    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
}
