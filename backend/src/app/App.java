package app;

import ui.ATCDashboard;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ATCDashboard dashboard = new ATCDashboard();
            dashboard.setVisible(true);
        });
    }
}