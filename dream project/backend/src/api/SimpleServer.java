package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import service.ATCService;
import service.ControllerSession;
import model.EmergencyType;
import model.FlightStatus;

import java.io.*;
import java.net.InetSocketAddress;

public class SimpleServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        System.out.println("Server running on http://localhost:8080");

        // Dummy session (replace later with real login)
        ControllerSession session = new ControllerSession(1, "Amit");
        ATCService service = new ATCService(session);

        // =========================
        // GET FLIGHTS (for now test)
        // =========================
        server.createContext("/test", exchange -> {
            String response = "ATC Backend Running!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        // =========================
        // DECLARE EMERGENCY
        // =========================
        server.createContext("/emergency", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {

                service.declareEmergency(501, EmergencyType.MEDICAL, 1);
                service.updateFlightStatus(501, FlightStatus.EMERGENCY);

                String response = "Emergency triggered!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        // =========================
        // ASSIGN RUNWAY
        // =========================
        server.createContext("/runway", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {

                service.assignRunway(501, 2);

                String response = "Runway assigned!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        // =========================
        // ASSIGN GATE
        // =========================
        server.createContext("/gate", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {

                service.assignGate(501, 5);

                String response = "Gate assigned!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.start();
    }
}
