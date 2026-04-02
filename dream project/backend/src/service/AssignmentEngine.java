package service;

import dao.AssignmentDAO;
import dao.ControllerDAO;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignmentEngine {

    private ControllerDAO controllerDAO;
    private AssignmentDAO assignmentDAO;
    private static java.util.Set<Integer> activeControllerIds = new java.util.HashSet<>();

    public AssignmentEngine() {
        this.controllerDAO = new ControllerDAO();
        this.assignmentDAO = new AssignmentDAO();
    }

    public static void setActiveController(int controllerId, boolean active) {
        if (active) {
            activeControllerIds.add(controllerId);
        } else {
            activeControllerIds.remove(controllerId);
        }
    }

    public void dispatchFlight(int flightId) {
        try {
            if (activeControllerIds.isEmpty()) {
                System.out.println("No active controllers logged in, assigning flight " + flightId + " to controller 1 as fallback");
                assignmentDAO.assignController(flightId, 1);
                return;
            }

            int bestControllerId = getControllerWithLeastLoad(activeControllerIds);

            assignmentDAO.assignController(flightId, bestControllerId);
            System.out.println("Flight " + flightId + " successfully assigned to controller " + bestControllerId);
            
        } catch (Exception e) {
            System.err.println("Failed to dispatch flight: " + e.getMessage());
        }
    }

    private int getControllerWithLeastLoad(java.util.Set<Integer> activeIds) throws SQLException {
        int bestId = -1;
        int minLoad = Integer.MAX_VALUE;

        String placeholders = String.join(",", java.util.Collections.nCopies(activeIds.size(), "?"));
        String sql = "SELECT controller_id, COUNT(*) as active_flights FROM assignments a " +
                     "JOIN flights f ON a.flight_id = f.flight_id " +
                     "WHERE f.status NOT IN ('LANDED') AND controller_id IN (" + placeholders + ") " +
                     "GROUP BY controller_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            for (int id : activeIds) {
                ps.setInt(idx++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cId = rs.getInt("controller_id");
                    int load = rs.getInt("active_flights");
                    if (load < minLoad || (load == minLoad && bestId == -1)) {
                        minLoad = load;
                        bestId = cId;
                    }
                }
            }
        }

        // If no controllers have flights yet, pick the first active one
        if (bestId == -1 && !activeIds.isEmpty()) {
            bestId = activeIds.iterator().next();
        }
        
        return bestId;
    }
}
