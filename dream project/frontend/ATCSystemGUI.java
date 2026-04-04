import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class Flight {
    int id;
    String status;
    Integer runwayId;
    Integer gateId;

    Flight(int id, String status) {
        this.id = id;
        this.status = status;
    }
}

public class ATCSystemGUI {

    static ArrayList<Flight> flights = new ArrayList<>();
    static JFrame frame;
    static JTable table;
    static DefaultTableModel model;
    static JTextArea logArea;
    static int sessionId = -1;

    public static void main(String[] args) {

        // Sample Data
        flights.add(new Flight(101, "Approaching"));
        flights.add(new Flight(102, "Scheduled"));

        frame = new JFrame("ATC Dashboard");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showLogin();
        frame.setVisible(true);
    }

    // ---- LOGIN ----
    static void showLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18,18,25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);

        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn);

        gbc.gridx=0; gbc.gridy=0;
        panel.add(label("Controller ID"), gbc);
        gbc.gridx=1;
        panel.add(idField, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panel.add(label("Name"), gbc);
        gbc.gridx=1;
        panel.add(nameField, gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2;
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            if (idField.getText().equals("1") && nameField.getText().equalsIgnoreCase("Ria")) {
                sessionId = 1;
                log("Logged in as controller 1");
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Login");
            }
        });

        frame.setContentPane(panel);
        frame.revalidate();
    }

    // ---- DASHBOARD ----
    static void showDashboard() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(18,18,25));

        // Title
        JLabel title = new JLabel("ATC Controller Dashboard", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        main.add(title, BorderLayout.NORTH);

        // Table
        String[] cols = {"Flight", "Status", "Runway", "Gate"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setBackground(new Color(30,30,40));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.GRAY);

        JScrollPane scroll = new JScrollPane(table);
        main.add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(18,18,25));

        JButton view = new JButton("View Flights");
        JButton viewDetails = new JButton("View Details");
        JButton runway = new JButton("Assign Runway");
        JButton gate = new JButton("Assign Gate");
        JButton status = new JButton("Update Status");
        JButton refresh = new JButton("Refresh");

        styleButton(view);
        styleButton(viewDetails);
        styleButton(runway);
        styleButton(gate);
        styleButton(status);
        styleButton(refresh);

        btnPanel.add(view);
        btnPanel.add(viewDetails);
        btnPanel.add(runway);
        btnPanel.add(gate);
        btnPanel.add(status);
        btnPanel.add(refresh);

        main.add(btnPanel, BorderLayout.SOUTH);

        // Flight log area
        logArea = new JTextArea(6, 1);
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 30));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Flight Logs"));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(0, 120));

        JPanel content = new JPanel(new BorderLayout());
        content.add(main, BorderLayout.CENTER);
        content.add(logScroll, BorderLayout.SOUTH);

        // Actions
        view.addActionListener(e -> {
            refreshTable();
            log("Requested flight list refresh.");
        });

        viewDetails.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(frame, "Please select a flight first.");
                return;
            }
            int flightId = ((Number) table.getValueAt(selectedRow, 0)).intValue();
            showFlightDetails(flightId);
        });

        refresh.addActionListener(e -> deleteAllFlights());

        runway.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Flight ID"));
                int rw = Integer.parseInt(JOptionPane.showInputDialog("Enter Runway ID"));
                assignRunway(id, rw);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input");
            }
        });

        gate.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Flight ID"));
                int gt = Integer.parseInt(JOptionPane.showInputDialog("Enter Gate ID"));
                assignGate(id, gt);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input");
            }
        });

        status.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Flight ID"));
                String st = JOptionPane.showInputDialog("Enter New Status");
                updateStatus(id, st);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input");
            }
        });

        frame.setContentPane(content);
        frame.revalidate();
    }

    // ---- STYLE ----
    static JButton styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(108,99,255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.LIGHT_GRAY);
        return l;
    }

    // ---- FUNCTIONS ----
    static void refreshTable() {
        boolean fetched = fetchFlightsFromServer();
        model.setRowCount(0);
        for (Flight f : flights) {
            model.addRow(new Object[]{
                    f.id,
                    f.status,
                    f.runwayId != null ? f.runwayId : "N/A",
                    f.gateId != null ? f.gateId : "N/A"
            });
        }
        if (fetched) {
            log("Flight table updated with latest database data.");
        }
    }

    static boolean fetchFlightsFromServer() {
        if (sessionId < 0) {
            log("Cannot refresh flights without a session.");
            return false;
        }

        try {
            URL url = new URL("http://localhost:8080/flights");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Session-Id", String.valueOf(sessionId));
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                List<Flight> serverFlights = parseFlightArray(response);
                flights.clear();
                flights.addAll(serverFlights);
                conn.disconnect();
                return true;
            } else {
                log("Failed to load flights from server. Response code: " + responseCode);
            }
            conn.disconnect();
        } catch (Exception ex) {
            log("Error loading flights: " + ex.getMessage());
        }
        return false;
    }

    static void showFlightDetails(int flightId) {
        try {
            URL url = new URL("http://localhost:8080/flights/" + flightId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Session-Id", String.valueOf(sessionId));
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                conn.disconnect();
                Map<String, String> details = parseJsonObject(response);
                if (details.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Unable to parse flight details.");
                    return;
                }

                log("Viewing details for flight " + flightId);

                JDialog dialog = new JDialog(frame, "Flight Details - " + flightId, true);
                dialog.setSize(380, 260);
                dialog.setLocationRelativeTo(frame);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBackground(new Color(18,18,25));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;

                int row = 0;
                for (String key : new String[]{"flightId", "status", "aircraftId", "runwayId", "gateId"}) {
                    gbc.gridx = 0;
                    gbc.gridy = row;
                    JLabel keyLabel = label(key);
                    panel.add(keyLabel, gbc);
                    gbc.gridx = 1;
                    String value = details.getOrDefault(key, "N/A");
                    JLabel valueLabel = new JLabel(value);
                    valueLabel.setForeground(Color.WHITE);
                    panel.add(valueLabel, gbc);
                    row++;
                }

                JButton close = new JButton("Close");
                styleButton(close);
                close.addActionListener(e -> dialog.dispose());
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(close, gbc);

                dialog.add(panel);
                dialog.setVisible(true);
            } else if (responseCode == 401) {
                JOptionPane.showMessageDialog(frame, "Unauthorized. Please login again.");
            } else if (responseCode == 404) {
                JOptionPane.showMessageDialog(frame, "Flight not found.");
            } else {
                JOptionPane.showMessageDialog(frame, "Server error: " + responseCode);
            }
            conn.disconnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error fetching details: " + ex.getMessage());
        }
    }

    static String readResponse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    static List<Flight> parseFlightArray(String json) {
        List<Flight> list = new ArrayList<>();
        String trimmed = json.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            return list;
        }
        String body = trimmed.substring(1, trimmed.length() - 1).trim();
        if (body.isEmpty()) {
            return list;
        }
        String[] items = body.split("\\},\\{");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (!item.startsWith("{")) item = "{" + item;
            if (!item.endsWith("}")) item = item + "}";
            Integer id = parseIntField(item, "flightId");
            String status = extractJsonField(item, "status");
            Integer runwayId = parseNullableInt(extractJsonField(item, "runwayId"));
            Integer gateId = parseNullableInt(extractJsonField(item, "gateId"));
            if (id != null && status != null) {
                Flight flight = new Flight(id, status);
                flight.runwayId = runwayId;
                flight.gateId = gateId;
                list.add(flight);
            }
        }
        return list;
    }

    static Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new HashMap<>();
        String trimmed = json.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            return map;
        }
        String body = trimmed.substring(1, trimmed.length() - 1);
        String[] entries = body.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String entry : entries) {
            int colonIndex = entry.indexOf(":");
            if (colonIndex < 0) continue;
            String key = entry.substring(0, colonIndex).trim();
            String value = entry.substring(colonIndex + 1).trim();
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            map.put(key, value.equals("null") ? "N/A" : value);
        }
        return map;
    }

    static Integer parseIntField(String json, String fieldName) {
        String value = extractJsonField(json, fieldName);
        return parseNullableInt(value);
    }

    static String extractJsonField(String json, String fieldName) {
        String key = "\"" + fieldName + "\":";
        int index = json.indexOf(key);
        if (index < 0) {
            return null;
        }
        int start = index + key.length();
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        if (start >= json.length()) {
            return null;
        }
        if (json.charAt(start) == '"') {
            int end = json.indexOf('"', start + 1);
            if (end < 0) {
                return null;
            }
            return json.substring(start + 1, end);
        }
        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') {
            end++;
        }
        return json.substring(start, end).trim();
    }

    static Integer parseNullableInt(String value) {
        if (value == null || value.equals("null") || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    static void log(String message) {
        if (logArea != null) {
            logArea.append(new Date() + " - " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }

    static void assignRunway(int id, int rw) {
        for (Flight f : flights) {
            if (f.id == id) {
                f.runwayId = rw;
                refreshTable();
                log("Assigned runway " + rw + " to flight " + id);
                JOptionPane.showMessageDialog(frame, "Runway Assigned Successfully");
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Flight Not Found");
    }

    static void assignGate(int id, int gt) {
        for (Flight f : flights) {
            if (f.id == id) {
                f.gateId = gt;
                refreshTable();
                JOptionPane.showMessageDialog(frame, "Gate Assigned Successfully");
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Flight Not Found");
    }

    static void updateStatus(int id, String st) {
        for (Flight f : flights) {
            if (f.id == id) {
                f.status = st;
                refreshTable();
                JOptionPane.showMessageDialog(frame, "Status Updated Successfully");
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Flight Not Found");
    }

    static void deleteAllFlights() {
        try {
            URL url = new URL("http://localhost:8080/flights/deleteAll");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("X-Session-Id", String.valueOf(sessionId));
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                flights.clear();
                model.setRowCount(0);
                JOptionPane.showMessageDialog(frame, "All flights deleted successfully!");
            } else if (responseCode == 401) {
                JOptionPane.showMessageDialog(frame, "Unauthorized. Please login again.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete flights. Server error: " + responseCode);
            }
            conn.disconnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }
}