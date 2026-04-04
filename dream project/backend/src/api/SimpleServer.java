package api;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Map;
import model.EmergencyType;
import model.FlightStatus;
import service.ATCService;
import service.ControllerSession;
import util.JSONUtil;
import util.SimpleJSONParser;

public class SimpleServer {

    private static Map<Integer, ATCService> sessions = new java.util.HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Starting ATC Server...");

        int port = 8080;
        HttpServer server = null;
        while (server == null && port < 8090) {
            try {
                System.out.println("Trying port " + port + "...");
                server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server created on port " + port);
            } catch (Exception e) {
                System.out.println("Port " + port + " in use, trying " + (port + 1) + ": " + e.getMessage());
                port++;
            }
        }

        if (server == null) {
            System.err.println("Could not find available port");
            return;
        }

        System.out.println("ATC Server running on http://localhost:" + port);

        // =========================
        // SIGNUP
        // =========================
        server.createContext("/signup", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                Map<String, Object> request = SimpleJSONParser.parse(body);
                String name = (String) request.get("name");
                String password = (String) request.get("password");

                boolean success = ATCService.signupController(name, password);
                if (success) {
                    String response = "{\"message\":\"Signup successful\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(201, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
        });

        // =========================
        // LOGIN
        // =========================
        server.createContext("/login", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                Map<String, Object> request = SimpleJSONParser.parse(body);

                ControllerSession session = null;
                if (request.containsKey("controllerId")) {
                    // Legacy login by ID
                    int controllerId = ((Number) request.get("controllerId")).intValue();
                    session = ATCService.loginController(controllerId);
                } else {
                    // New login by name/password
                    String name = (String) request.get("name");
                    String password = (String) request.get("password");
                    session = ATCService.loginController(name, password);
                }

                if (session != null) {
                    ATCService service = new ATCService(session);
                    sessions.put(session.getControllerId(), service);
                    service.setControllerActive(session.getControllerId(), true);
                    String response = JSONUtil.toJson(java.util.Map.of(
                            "sessionId", String.valueOf(session.getControllerId()),
                            "controllerName", session.getControllerName()));
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // GET ASSIGNED FLIGHTS
        // =========================
        server.createContext("/flights", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String sessionId = exchange.getRequestHeaders().getFirst("X-Session-Id");
                if (sessionId != null) {
                    int controllerId = Integer.parseInt(sessionId);
                    ATCService service = sessions.get(controllerId);
                    if (service != null) {
                        var flights = service.getAssignedFlights();
                        String response = JSONUtil.toJson(flights);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // GET FLIGHT DETAILS
        // =========================
        server.createContext("/flights/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            if (parts.length == 3 && "GET".equals(exchange.getRequestMethod())) {
                int flightId = Integer.parseInt(parts[2]);
                String sessionId = exchange.getRequestHeaders().getFirst("X-Session-Id");
                if (sessionId != null) {
                    int controllerId = Integer.parseInt(sessionId);
                    ATCService service = sessions.get(controllerId);
                    if (service != null) {
                        var flight = service.getFlightDetails(flightId);
                        if (flight != null) {
                            String response = JSONUtil.toJson(flight);
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            exchange.sendResponseHeaders(200, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } else {
                            exchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // DECLARE EMERGENCY
        // =========================
        server.createContext("/flights/emergency", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                Map<String, Object> request = SimpleJSONParser.parse(body);
                int flightId = (Integer) request.get("flightId");
                String type = (String) request.get("type");
                int priority = (Integer) request.get("priority");
                String sessionId = exchange.getRequestHeaders().getFirst("X-Session-Id");
                if (sessionId != null) {
                    int controllerId = Integer.parseInt(sessionId);
                    ATCService service = sessions.get(controllerId);
                    if (service != null) {
                        service.declareEmergency(flightId, EmergencyType.valueOf(type), priority);
                        service.updateFlightStatus(flightId, FlightStatus.EMERGENCY);
                        String response = "{\"message\":\"Emergency declared\"}";
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // ASSIGN RUNWAY
        // =========================
        server.createContext("/flights/runway", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                Map<String, Object> request = SimpleJSONParser.parse(body);
                int flightId = (Integer) request.get("flightId");
                int runwayId = (Integer) request.get("runwayId");
                String sessionId = exchange.getRequestHeaders().getFirst("X-Session-Id");
                if (sessionId != null) {
                    int controllerId = Integer.parseInt(sessionId);
                    ATCService service = sessions.get(controllerId);
                    if (service != null) {
                        service.assignRunway(flightId, runwayId);
                        String response = "{\"message\":\"Runway assigned\"}";
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // ASSIGN GATE
        // =========================
        server.createContext("/flights/gate", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                Map<String, Object> request = SimpleJSONParser.parse(body);
                int flightId = (Integer) request.get("flightId");
                int gateId = (Integer) request.get("gateId");
                String sessionId = exchange.getRequestHeaders().getFirst("X-Session-Id");
                if (sessionId != null) {
                    int controllerId = Integer.parseInt(sessionId);
                    ATCService service = sessions.get(controllerId);
                    if (service != null) {
                        service.assignGate(flightId, gateId);
                        String response = "{\"message\":\"Gate assigned\"}";
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // UPDATE FLIGHT STATUS
        // =========================
        server.createContext("/flights/status", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                Map<String, Object> request = SimpleJSONParser.parse(body);
                int flightId = (Integer) request.get("flightId");
                String status = (String) request.get("status");
                String sessionId = exchange.getRequestHeaders().getFirst("X-Session-Id");
                if (sessionId != null) {
                    int controllerId = Integer.parseInt(sessionId);
                    ATCService service = sessions.get(controllerId);
                    if (service != null) {
                        service.updateFlightStatus(flightId, FlightStatus.valueOf(status));
                        String response = "{\"message\":\"Status updated\"}";
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
            }
        });

        // =========================
        // TEST ENDPOINT (for frontend port detection)
        // =========================
        server.createContext("/test", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "{\"status\":\"ok\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.start();
    }
}
