package frontend;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class ATCFrontend extends JFrame {

    private static String API_BASE = "http://localhost:8080";
    private HttpClient httpClient;
    // private Gson gson;
    private String sessionId = null;
    private String controllerName = null;

    // UI Components
    private JTextField loginNameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    private JButton switchToSignupButton;
    
    private JTextField signupNameField;
    private JPasswordField signupPasswordField;
    private JButton signupButton;
    private JButton switchToLoginButton;
    
    private JPanel loginPanel;
    private JPanel signupPanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JList<String> flightsList;
    private DefaultListModel<String> flightsModel;
    private JButton viewDetailsButton;
    private JButton refreshButton;
    private JButton logoutButton;

    // Flight Detail Components
    private JPanel detailPanel;
    private JLabel flightInfoLabel;
    private JButton declareEmergencyButton;
    private JButton assignRunwayButton;
    private JButton assignGateButton;
    private JButton updateStatusButton;
    private JButton backButton;

    private int currentFlightId = -1;

    public ATCFrontend() {
        httpClient = HttpClient.newHttpClient();
        findServerPort();
        // gson = new Gson();

        initializeUI();
        setTitle("ATC Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void findServerPort() {
        int foundPort = -1;
        int maxRetries = 3;

        for (int port = 8080; port < 8090; port++) {
            System.out.println("Trying backend on port " + port + "...");

            // Try multiple times for each port
            for (int retry = 0; retry < maxRetries; retry++) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/test"))
                        .GET()
                        .build();
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Received status " + response.statusCode() + " from /test on port " + port);
                    if (response.statusCode() == 200) {
                        foundPort = port;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Port " + port + " not available (attempt " + (retry + 1) + "/" + maxRetries + "): " + e.getMessage());
                    if (retry < maxRetries - 1) {
                        try {
                            Thread.sleep(1000); // Wait 1 second between retries
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            if (foundPort >= 0) {
                break;
            }
        }

        if (foundPort >= 0) {
            API_BASE = "http://localhost:" + foundPort;
            System.out.println("Connected to server on port " + foundPort + ", API_BASE set to " + API_BASE);
        } else {
            JOptionPane.showMessageDialog(null, "Could not connect to server. Please ensure the backend is running.");
        }
    }

    private void initializeUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Login Panel
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginNameField = new JTextField(15);
        loginPanel.add(loginNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPasswordField = new JPasswordField(15);
        loginPanel.add(loginPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        loginPanel.add(loginButton, gbc);
        
        gbc.gridy = 3;
        switchToSignupButton = new JButton("Don't have an account? Sign up");
        switchToSignupButton.setBorderPainted(false);
        switchToSignupButton.setContentAreaFilled(false);
        loginPanel.add(switchToSignupButton, gbc);

        // Signup Panel
        signupPanel = new JPanel(new GridBagLayout());
        signupPanel.setBorder(BorderFactory.createTitledBorder("Sign Up"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        signupPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        signupNameField = new JTextField(15);
        signupPanel.add(signupNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        signupPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        signupPasswordField = new JPasswordField(15);
        signupPanel.add(signupPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(200, 30));
        signupPanel.add(signupButton, gbc);
        
        gbc.gridy = 3;
        switchToLoginButton = new JButton("Already have an account? Login");
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setContentAreaFilled(false);
        signupPanel.add(switchToLoginButton, gbc);

        // Main Panel
        mainPanel = new JPanel(new BorderLayout());
        flightsModel = new DefaultListModel<>();
        flightsList = new JList<>(flightsModel);
        JScrollPane scrollPane = new JScrollPane(flightsList);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        viewDetailsButton = new JButton("View Details");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(new JLabel("Assigned Flights"), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Detail Panel
        detailPanel = new JPanel(new BorderLayout());
        flightInfoLabel = new JLabel("Flight Details");
        JPanel actionPanel = new JPanel(new FlowLayout());
        declareEmergencyButton = new JButton("Declare Emergency");
        assignRunwayButton = new JButton("Assign Runway");
        assignGateButton = new JButton("Assign Gate");
        updateStatusButton = new JButton("Update Status");
        backButton = new JButton("Back to Dashboard");
        actionPanel.add(declareEmergencyButton);
        actionPanel.add(assignRunwayButton);
        actionPanel.add(assignGateButton);
        actionPanel.add(updateStatusButton);
        actionPanel.add(backButton);

        detailPanel.add(flightInfoLabel, BorderLayout.CENTER);
        detailPanel.add(actionPanel, BorderLayout.SOUTH);

        // Add panels to card layout
        cardPanel.add(loginPanel, "login");
        cardPanel.add(signupPanel, "signup");
        cardPanel.add(mainPanel, "main");
        cardPanel.add(detailPanel, "detail");

        add(cardPanel, BorderLayout.CENTER);

        // Event Listeners
        loginButton.addActionListener(e -> login());
        switchToSignupButton.addActionListener(e -> showSignup());
        signupButton.addActionListener(e -> signup());
        switchToLoginButton.addActionListener(e -> showLogin());
        viewDetailsButton.addActionListener(e -> viewFlightDetails());
        refreshButton.addActionListener(e -> loadFlights());
        logoutButton.addActionListener(e -> logout());
        declareEmergencyButton.addActionListener(e -> declareEmergency());
        assignRunwayButton.addActionListener(e -> assignRunway());
        assignGateButton.addActionListener(e -> assignGate());
        updateStatusButton.addActionListener(e -> updateStatus());
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "main"));

        // Auto-refresh
        Timer timer = new Timer(10000, e -> {
            if (sessionId != null) {
                loadFlights();
            }
        });
        timer.start();
    }

    private void login() {
        try {
            String name = loginNameField.getText();
            String password = new String(loginPasswordField.getPassword());
            
            String json = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, String> data = parseSimpleJson(response.body());
                sessionId = data.get("sessionId");
                controllerName = data.get("controllerName");
                setTitle("ATC Management System - " + controllerName);
                showDashboard();
                loadFlights();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage());
        }
    }

    private void signup() {
        try {
            String name = signupNameField.getText().trim();
            String password = new String(signupPasswordField.getPassword()).trim();

            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
                return;
            }

            String json = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";
            System.out.println("Attempting signup for: " + name + " on " + API_BASE + "/signup");

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Signup response: " + response.statusCode() + " - " + response.body());

            if (response.statusCode() == 201) {
                JOptionPane.showMessageDialog(this, "Signup successful! Please login.");
                showLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Signup failed: " + response.statusCode() + " - " + (response.body().isEmpty() ? "No response" : response.body()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Signup error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showLogin() {
        System.out.println("Switching to login form");
        cardLayout.show(cardPanel, "login");
    }

    private void showSignup() {
        System.out.println("Switching to signup form");
        cardLayout.show(cardPanel, "signup");
    }

    private void showDashboard() {
        cardLayout.show(cardPanel, "main");
    }

    private void showDetails() {
        cardLayout.show(cardPanel, "detail");
    }

    private void loadFlights() {
        if (sessionId == null) return;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/flights"))
                .header("X-Session-Id", sessionId)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Flights response body: " + response.body());
                List<Map<String, Object>> flights = parseJsonArray(response.body());

                flightsModel.clear();
                for (Map<String, Object> flight : flights) {
                    System.out.println("Parsed flight: " + flight);
                    Object flightIdObj = flight.get("flightId");
                    Object aircraftIdObj = flight.get("aircraftId");
                    Object statusObj = flight.get("status");
                    if (flightIdObj == null || aircraftIdObj == null || statusObj == null) {
                        System.out.println("Skipping flight with null fields: flightId=" + flightIdObj + ", aircraftId=" + aircraftIdObj + ", status=" + statusObj);
                        continue;
                    }
                    String item = String.format("Flight %d - %s (Aircraft: %d)",
                        ((Number) flightIdObj).intValue(),
                        (String) statusObj,
                        ((Number) aircraftIdObj).intValue());
                    flightsModel.addElement(item);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading flights: " + ex.getMessage());
        }
    }

    private void viewFlightDetails() {
        String selected = flightsList.getSelectedValue();
        if (selected == null) return;

        // Extract flight ID from string
        String[] parts = selected.split(" ");
        int flightId = Integer.parseInt(parts[1]);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/flights/" + flightId))
                .header("X-Session-Id", sessionId)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> flight = parseSimpleJsonToObject(response.body());
                String info = String.format(
                    "<html>Flight ID: %d<br>Status: %s<br>Aircraft ID: %d<br>Runway ID: %s<br>Gate ID: %s</html>",
                    ((Number) flight.get("flightId")).intValue(),
                    (String) flight.get("status"),
                    ((Number) flight.get("aircraftId")).intValue(),
                    flight.get("runwayId") != null ? flight.get("runwayId").toString() : "Not assigned",
                    flight.get("gateId") != null ? flight.get("gateId").toString() : "Not assigned"
                );
                flightInfoLabel.setText(info);
                currentFlightId = flightId;
                showDetails();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading flight details: " + ex.getMessage());
        }
    }

    private void declareEmergency() {
        String type = JOptionPane.showInputDialog("Emergency type (MEDICAL, ENGINE_FAILURE, etc.):");
        String priorityStr = JOptionPane.showInputDialog("Priority (1-5):");
        if (type != null && priorityStr != null) {
            try {
                int priority = Integer.parseInt(priorityStr);
                String json = String.format("{\"flightId\":%d,\"type\":\"%s\",\"priority\":%d}",
                    currentFlightId, type, priority);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/flights/emergency"))
                    .header("Content-Type", "application/json")
                    .header("X-Session-Id", sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Emergency declared");
                    viewFlightDetails();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void assignRunway() {
        String runwayStr = JOptionPane.showInputDialog("Runway ID:");
        if (runwayStr != null) {
            try {
                int runwayId = Integer.parseInt(runwayStr);
                String json = String.format("{\"flightId\":%d,\"runwayId\":%d}", currentFlightId, runwayId);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/flights/runway"))
                    .header("Content-Type", "application/json")
                    .header("X-Session-Id", sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Runway assigned");
                    viewFlightDetails();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void assignGate() {
        String gateStr = JOptionPane.showInputDialog("Gate ID:");
        if (gateStr != null) {
            try {
                int gateId = Integer.parseInt(gateStr);
                String json = String.format("{\"flightId\":%d,\"gateId\":%d}", currentFlightId, gateId);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/flights/gate"))
                    .header("Content-Type", "application/json")
                    .header("X-Session-Id", sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Gate assigned");
                    viewFlightDetails();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void updateStatus() {
        String status = JOptionPane.showInputDialog("New status (APPROACHING, HOLDING, LANDED, etc.):");
        if (status != null) {
            try {
                String json = String.format("{\"flightId\":%d,\"status\":\"%s\"}", currentFlightId, status);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/flights/status"))
                    .header("Content-Type", "application/json")
                    .header("X-Session-Id", sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Status updated");
                    viewFlightDetails();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void logout() {
        sessionId = null;
        controllerName = null;
        setTitle("ATC Management System");
        flightsModel.clear();
        cardLayout.show(cardPanel, "login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ATCFrontend().setVisible(true);
        });
    }

    // Simple JSON parsing methods
    private Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim().replace("\"", "");
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private Map<String, Object> parseSimpleJsonToObject(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim();
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        map.put(key, value);
                    } else if (value.equals("null")) {
                        map.put(key, null);
                    } else {
                        try {
                            map.put(key, Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            map.put(key, value);
                        }
                    }
                }
            }
        }
        return map;
    }

    private List<Map<String, Object>> parseJsonArray(String json) {
        List<Map<String, Object>> list = new ArrayList<>();
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
            // Simple split by },{ assuming no nested objects
            String[] objects = json.split("\\},\\s*\\{");
            for (int i = 0; i < objects.length; i++) {
                String obj = objects[i];
                if (i == 0 && obj.startsWith("{")) obj = obj.substring(1);
                if (i == objects.length - 1 && obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);
                if (!obj.startsWith("{")) obj = "{" + obj;
                if (!obj.endsWith("}")) obj = obj + "}";
                list.add(parseSimpleJsonToObject(obj));
            }
        }
        return list;
    }
}