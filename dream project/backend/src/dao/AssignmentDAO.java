package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.CallableStatement;

public class AssignmentDAO {

    public void assignController(int flightId, int controllerId) throws Exception {
        String sql = "{CALL AssignController(?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, flightId);
            cs.setInt(2, controllerId);
            cs.execute();
        }
    }
}