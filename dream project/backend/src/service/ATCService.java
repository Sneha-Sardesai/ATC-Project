package service;

import dao.EmergencyDAO;
import dao.FlightDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.EmergencyType;
import model.Flight;
import model.FlightStatus;

public class ATCService {

    private FlightDAO flightDAO;
    private EmergencyDAO emergencyDAO;
    private AssignmentEngine assignmentEngine;
    private ControllerSession session;
    private scheduler.EmergencyScheduler emergencyScheduler;

    public ATCService(ControllerSession session) {
        this.session = session;
        this.flightDAO = new FlightDAO();
        this.emergencyDAO = new EmergencyDAO();
        this.assignmentEngine = new AssignmentEngine();
        this.emergencyScheduler = new scheduler.EmergencyScheduler();
    }

    public static ControllerSession loginController(int controllerId) {
        try {
            dao.ControllerDAO cDAO = new dao.ControllerDAO();
            model.Controller c = cDAO.login(controllerId);
            if (c != null) {
                return new ControllerSession(c.getControllerId(), c.getName());
            }
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }
        return null;
    }

    public static ControllerSession loginController(String name, String password) {
        try {
            dao.ControllerDAO cDAO = new dao.ControllerDAO();
            model.Controller c = cDAO.login(name, password);
            if (c != null) {
                return new ControllerSession(c.getControllerId(), c.getName());
            }
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }
        return null;
    }

    public static boolean signupController(String name, String password) {
        try {
            dao.ControllerDAO cDAO = new dao.ControllerDAO();
            return cDAO.signup(name, password);
        } catch (Exception e) {
            System.err.println("Signup failed: " + e.getMessage());
            return false;
        }
    }

    // SYSTEM creates flight (controller does NOT type everything)
    public void systemAddFlight(int flightId, String status, int aircraftId, Integer runwayId, Integer gateId) {
        try {
            flightDAO.addFlight(
                    flightId,
                    status,
                    aircraftId,
                    runwayId,
                    gateId
            );
            System.out.println("Flight " + flightId + " added by system.");
            assignmentEngine.dispatchFlight(flightId);
        } catch (SQLException e) {
            System.out.println("Failed to add flight.");
            e.printStackTrace();
        }
    }

    // Controller declares emergency
    public void declareEmergency(int flightId, EmergencyType type, int priority) {
        try {
            emergencyDAO.declareEmergency(flightId, type.name(), priority);
            emergencyScheduler.addEmergency(flightId, priority);
            System.out.println("Emergency declared for flight " + flightId + " [Priority: " + priority + "]");

            if (session != null) {
                System.out.println("Controller " + session.getControllerName() + " is handling the emergency. Stalling other normal flights...");
                stallNormalFlights(session.getControllerId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Controller assigns runway
    public void assignRunway(int flightId, int runwayId) {
        try {
            flightDAO.assignRunway(flightId, runwayId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Controller assigns gate
    public void assignGate(int flightId, int gateId) {
        try {
            // Use direct UPDATE to ensure database is always updated
            flightDAO.assignGateDirect(flightId, gateId);
            System.out.println("Gate " + gateId + " assigned to flight " + flightId);
        } catch (SQLException e) {
            System.err.println("Error assigning gate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Controller updates flight status
    public void updateFlightStatus(int flightId, FlightStatus status) {
        try {
            flightDAO.updateFlightStatus(flightId, status.name());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get assigned flights for controller
    public List<Flight> getAssignedFlights() {
        try {
            return flightDAO.getFlightsForController(session.getControllerId());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get flight details
    public Flight getFlightDetails(int flightId) {
        try {
            return flightDAO.getFlightById(flightId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setControllerActive(int controllerId, boolean active) {
        AssignmentEngine.setActiveController(controllerId, active);
    }

    private void stallNormalFlights(int controllerId) {
        try {
            flightDAO.stallApproachingFlightsForController(controllerId);
            System.out.println("All other approaching flights assigned to Controller ID " + controllerId + " are now HOLDING.");
        } catch (SQLException e) {
            System.err.println("Failed to stall flights.");
            e.printStackTrace();
        }
    }

    // Delete all flights
    public void deleteAllFlights() {
        try {
            flightDAO.deleteAllFlights();
            System.out.println("All flights have been deleted.");
        } catch (SQLException e) {
            System.err.println("Failed to delete all flights.");
            e.printStackTrace();
        }
    }
}