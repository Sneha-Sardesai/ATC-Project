import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
        JButton runway = new JButton("Assign Runway");
        JButton gate = new JButton("Assign Gate");
        JButton status = new JButton("Update Status");

        styleButton(view);
        styleButton(runway);
        styleButton(gate);
        styleButton(status);

        btnPanel.add(view);
        btnPanel.add(runway);
        btnPanel.add(gate);
        btnPanel.add(status);

        main.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        view.addActionListener(e -> refreshTable());

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

        frame.setContentPane(main);
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
        model.setRowCount(0);
        for (Flight f : flights) {
            model.addRow(new Object[]{
                    f.id,
                    f.status,
                    f.runwayId != null ? f.runwayId : "N/A",
                    f.gateId != null ? f.gateId : "N/A"
            });
        }
    }

    static void assignRunway(int id, int rw) {
        for (Flight f : flights) {
            if (f.id == id) {
                f.runwayId = rw;
                refreshTable();
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
}