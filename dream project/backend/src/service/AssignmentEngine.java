package service;

import dao.AssignmentDAO;
import dao.ControllerDAO;
import model.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import db.DBConnection;

public class AssignmentEngine {

    private ControllerDAO controllerDAO;
    private AssignmentDAO assignmentDAO;

    public AssignmentEngine() {
        this.controllerDAO = new ControllerDAO();
        this.assignmentDAO = new AssignmentDAO();
    }

    public void dispatchFlight(int flightId) {
        try {
            List<Controller> activeControllers = controllerDAO.getAllControllers();
            if (activeControllers.isEmpty()) {
                System.err.println("No active controllers available to assign flight " + flightId);
                return;
            }

            int bestControllerId = getControllerWithLeastLoad(activeControllers);

            assignmentDAO.assignController(flightId, bestControllerId);
            System.out.println("Flight " + flightId + " successfully assigned to controller " + bestControllerId);
            
        } catch (Exception e) {
            System.err.println("Failed to dispatch flight: " + e.getMessage());
        }
    }

    private int getControllerWithLeastLoad(List<Controller> activeControllers) throws SQLException {
        int bestId = activeControllers.get(0).getControllerId();
        int minLoad = Integer.MAX_VALUE;

        String sql = "SELECT controller_id, COUNT(*) as active_flights FROM assignments a " +
                     "JOIN flights f ON a.flight_id = f.flight_id " +
                     "WHERE f.status NOT IN ('LANDED') " +
                     "GROUP BY controller_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cId = rs.getInt("controller_id");
                int load = rs.getInt("active_flights");
                if (load < minLoad) {
                    minLoad = load;
                    bestId = cId;
                }
            }
        }
        
        return bestId;
    }
}
