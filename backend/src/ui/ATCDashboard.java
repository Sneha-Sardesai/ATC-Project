package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import dao.FlightDAO;
import model.FlightInfo;

/**
 * ATC Controller Dashboard — Premium Java Swing UI
 * Dark-themed, glassmorphism-inspired, with custom-painted components.
 */
public class ATCDashboard extends JFrame {

    // ======================== DESIGN TOKENS ========================
    static final Color BG_PRIMARY    = new Color(8, 9, 14);
    static final Color BG_SECONDARY  = new Color(15, 17, 24);
    static final Color BG_CARD       = new Color(18, 20, 30);
    static final Color BG_CARD_HOVER = new Color(25, 28, 42);
    static final Color BG_INPUT      = new Color(30, 34, 50);
    static final Color GLASS_BORDER  = new Color(255, 255, 255, 15);
    static final Color ACCENT        = new Color(108, 99, 255);
    static final Color ACCENT_LIGHT  = new Color(167, 139, 250);
    static final Color EMERGENCY     = new Color(255, 77, 106);
    static final Color SUCCESS       = new Color(52, 211, 153);
    static final Color WARNING       = new Color(245, 158, 11);
    static final Color INFO          = new Color(56, 189, 248);
    static final Color TEXT_PRIMARY  = new Color(232, 234, 237);
    static final Color TEXT_SECONDARY= new Color(139, 143, 163);
    static final Color TEXT_MUTED    = new Color(85, 89, 112);

    static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 28);
    static final Font FONT_HEADING   = new Font("Segoe UI", Font.BOLD, 18);
    static final Font FONT_SUBHEAD   = new Font("Segoe UI", Font.BOLD, 15);
    static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_SMALL     = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font FONT_TINY      = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font FONT_MONO      = new Font("Consolas", Font.BOLD, 32);
    static final Font FONT_MONO_SM   = new Font("Consolas", Font.PLAIN, 12);
    static final Font FONT_LABEL     = new Font("Segoe UI", Font.BOLD, 11);

    private FlightDAO dao = new FlightDAO();
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int controllerId;
    private String controllerName;
    private String controllerRole;

    // Dashboard widgets
    private JLabel lblTotal, lblEmergency, lblRunways, lblGates;
    private JLabel lblTotalSub, lblEmergencySub, lblRunwaysSub, lblGatesSub;
    private JPanel emergencyListPanel, normalListPanel;
    private JLabel lblEmergencyCount, lblNormalCount;
    private JLabel navNameLabel, navRoleLabel, navAvatarLabel;

    public ATCDashboard() {
        setTitle("ATC — Controller Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 650));

        // Enable antialiasing globally
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Ambient gradient orbs
                g2.setPaint(new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.5f, -getHeight() * 0.1f),
                    getWidth() * 0.6f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(108, 99, 255, 22), new Color(108, 99, 255, 0)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setPaint(new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.8f, getHeight() * 1.1f),
                    getWidth() * 0.5f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(255, 77, 106, 14), new Color(255, 77, 106, 0)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBackground(BG_PRIMARY);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    // ======================== ROUNDED PANEL ========================
    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        private Color borderColor;
        private Color leftAccent;

        RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius;
            this.bgColor = bg;
            this.borderColor = border;
            setOpaque(false);
        }

        void setLeftAccent(Color c) { this.leftAccent = c; }
        void setBgColor(Color c) { this.bgColor = c; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

            // Border
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, radius, radius));
            }

            // Left accent bar
            if (leftAccent != null) {
                g2.setColor(leftAccent);
                g2.fill(new RoundRectangle2D.Float(0, 0, 4, getHeight(), 4, 4));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ======================== GRADIENT BUTTON ========================
    static class GradientButton extends JButton {
        private Color startColor, endColor;
        private boolean hovered = false;

        GradientButton(String text, Color start, Color end) {
            super(text);
            this.startColor = start;
            this.endColor = end;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

            // Glow on hover
            if (hovered) {
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
            }

            // Shadow
            g2.setColor(new Color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), 60));
            g2.fill(new RoundRectangle2D.Float(2, getHeight() - 4, getWidth() - 4, 6, 10, 10));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ======================== FLAT STYLED BUTTON ========================
    static class FlatButton extends JButton {
        private Color bgColor, fgColor, borderColor;
        private boolean hovered = false;

        FlatButton(String text, Color bg, Color fg, Color border) {
            super(text);
            this.bgColor = bg;
            this.fgColor = fg;
            this.borderColor = border;
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = hovered ? brighter(bgColor, 30) : bgColor;
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
            }

            g2.dispose();
            super.paintComponent(g);
        }

        private Color brighter(Color c, int amount) {
            return new Color(
                Math.min(255, c.getRed() + amount),
                Math.min(255, c.getGreen() + amount),
                Math.min(255, c.getBlue() + amount),
                c.getAlpha()
            );
        }
    }

    // ======================== STYLED TEXT FIELD ========================
    static class StyledTextField extends JTextField {
        private String placeholder;

        StyledTextField(String placeholder) {
            this.placeholder = placeholder;
            setFont(FONT_BODY);
            setBackground(BG_INPUT);
            setForeground(TEXT_PRIMARY);
            setCaretColor(TEXT_PRIMARY);
            setOpaque(false);
            setBorder(new EmptyBorder(13, 16, 13, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

            // Border
            Color bc = isFocusOwner() ? ACCENT : new Color(255, 255, 255, 18);
            g2.setColor(bc);
            g2.setStroke(new BasicStroke(isFocusOwner() ? 2f : 1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 16, 16));

            // Focus glow
            if (isFocusOwner()) {
                g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 40));
                g2.setStroke(new BasicStroke(4f));
                g2.draw(new RoundRectangle2D.Float(-1, -1, getWidth() + 2, getHeight() + 2, 18, 18));
            }

            g2.dispose();
            super.paintComponent(g);

            // Placeholder
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D pg = (Graphics2D) g.create();
                pg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                pg.setColor(TEXT_MUTED);
                pg.setFont(getFont());
                FontMetrics fm = pg.getFontMetrics();
                pg.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                pg.dispose();
            }
        }
    }

    // ======================== TAG LABEL ========================
    static JLabel createTag(String text, Color color) {
        JLabel tag = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 35));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(color);
        tag.setBorder(new EmptyBorder(3, 8, 3, 8));
        tag.setOpaque(false);
        return tag;
    }


    // ================================================================
    //  LOGIN PANEL
    // ================================================================
    private JPanel createLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        // --- Brand ---
        JLabel icon = new JLabel("\u2708\uFE0F", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(icon);
        wrapper.add(Box.createVerticalStrut(8));

        JLabel brandTitle = new JLabel("ATC System") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), 0, ACCENT_LIGHT);
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                g2.drawString(getText(), x, fm.getAscent());
                g2.dispose();
            }
        };
        brandTitle.setFont(FONT_TITLE);
        brandTitle.setPreferredSize(new Dimension(300, 36));
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(brandTitle);

        JLabel brandSub = new JLabel("Air Traffic Control — Controller Dashboard");
        brandSub.setFont(FONT_SMALL);
        brandSub.setForeground(TEXT_SECONDARY);
        brandSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(brandSub);
        wrapper.add(Box.createVerticalStrut(28));

        // --- Login Card ---
        RoundedPanel card = new RoundedPanel(20, BG_CARD, GLASS_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(36, 36, 36, 36));
        card.setPreferredSize(new Dimension(400, 340));
        card.setMaximumSize(new Dimension(400, 340));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Error label (hidden initially)
        JLabel errorLabel = new JLabel("Invalid credentials. Please try again.");
        errorLabel.setFont(FONT_SMALL);
        errorLabel.setForeground(EMERGENCY);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setVisible(false);
        errorLabel.setBorder(new EmptyBorder(0, 0, 12, 0));
        card.add(errorLabel);

        // Controller ID
        JLabel lblId = new JLabel("CONTROLLER ID");
        lblId.setFont(FONT_LABEL);
        lblId.setForeground(TEXT_SECONDARY);
        lblId.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblId);
        card.add(Box.createVerticalStrut(6));

        StyledTextField tfId = new StyledTextField("Enter your Controller ID");
        tfId.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfId.setMaximumSize(new Dimension(328, 48));
        card.add(tfId);
        card.add(Box.createVerticalStrut(16));

        // Name
        JLabel lblName = new JLabel("NAME");
        lblName.setFont(FONT_LABEL);
        lblName.setForeground(TEXT_SECONDARY);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblName);
        card.add(Box.createVerticalStrut(6));

        StyledTextField tfName = new StyledTextField("Enter your name");
        tfName.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfName.setMaximumSize(new Dimension(328, 48));
        card.add(tfName);
        card.add(Box.createVerticalStrut(24));

        // Login button
        GradientButton btnLogin = new GradientButton("Sign In", ACCENT, new Color(139, 92, 246));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(328, 46));
        btnLogin.setPreferredSize(new Dimension(328, 46));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(16));

        // Hint
        JLabel hint = new JLabel("Demo:  ID = 1  |  Name = Ria");
        hint.setFont(FONT_MONO_SM);
        hint.setForeground(TEXT_MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);

        wrapper.add(card);

        // --- Login Action ---
        ActionListener loginAction = e -> {
            try {
                int id = Integer.parseInt(tfId.getText().trim());
                String name = tfName.getText().trim();
                if (name.isEmpty()) {
                    errorLabel.setText("Please enter your name.");
                    errorLabel.setVisible(true);
                    return;
                }
                String validated = dao.validateController(id, name);
                if (validated != null) {
                    controllerId = id;
                    controllerName = validated;
                    controllerRole = dao.getControllerRole(id);
                    errorLabel.setVisible(false);
                    showDashboard();
                } else {
                    errorLabel.setText("Invalid credentials. Check ID and Name.");
                    errorLabel.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid numeric Controller ID.");
                errorLabel.setVisible(true);
            }
        };
        btnLogin.addActionListener(loginAction);
        tfName.addActionListener(loginAction);
        tfId.addActionListener(loginAction);

        outer.add(wrapper);
        return outer;
    }


    // ================================================================
    //  DASHBOARD PANEL
    // ================================================================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // ---- TOP NAV ----
        JPanel nav = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(8, 9, 14, 216));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(GLASS_BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(0, 24, 0, 24));
        nav.setPreferredSize(new Dimension(0, 64));

        // Left side
        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 18));
        navLeft.setOpaque(false);

        JLabel logo = new JLabel("\u2708\uFE0F ATC") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), 0, ACCENT_LIGHT);
                g2.setPaint(gp);
                g2.setFont(getFont());
                g2.drawString(getText(), 0, g2.getFontMetrics().getAscent());
                g2.dispose();
            }
        };
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        navLeft.add(logo);

        JLabel separator = new JLabel("  |  ");
        separator.setForeground(new Color(255, 255, 255, 20));
        navLeft.add(separator);

        JLabel pageTitle = new JLabel("Controller Dashboard");
        pageTitle.setFont(FONT_BODY);
        pageTitle.setForeground(TEXT_SECONDARY);
        navLeft.add(pageTitle);

        nav.add(navLeft, BorderLayout.WEST);

        // Right side
        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        navRight.setOpaque(false);

        // Live badge
        JLabel liveBadge = new JLabel("\u25CF  LIVE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(52, 211, 153, 25));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(new Color(52, 211, 153, 40));
                g2.setStroke(new BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        liveBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        liveBadge.setForeground(SUCCESS);
        liveBadge.setBorder(new EmptyBorder(5, 14, 5, 14));
        navRight.add(liveBadge);

        // Controller badge
        RoundedPanel controllerBadge = new RoundedPanel(40, new Color(255, 255, 255, 8), GLASS_BORDER);
        controllerBadge.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controllerBadge.setBorder(new EmptyBorder(2, 4, 2, 12));

        navAvatarLabel = new JLabel("A") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), getHeight(), ACCENT_LIGHT);
                g2.setPaint(gp);
                g2.fillOval(0, 0, 28, 28);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        navAvatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        navAvatarLabel.setForeground(Color.WHITE);
        navAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        navAvatarLabel.setPreferredSize(new Dimension(28, 28));
        controllerBadge.add(navAvatarLabel);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        navNameLabel = new JLabel("Controller");
        navNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        navNameLabel.setForeground(TEXT_PRIMARY);
        namePanel.add(navNameLabel);
        navRoleLabel = new JLabel("Role");
        navRoleLabel.setFont(FONT_TINY);
        navRoleLabel.setForeground(TEXT_MUTED);
        namePanel.add(navRoleLabel);
        controllerBadge.add(namePanel);
        navRight.add(controllerBadge);

        // Refresh button
        FlatButton btnRefresh = new FlatButton("↻ Refresh",
            new Color(56, 189, 248, 30), INFO, new Color(56, 189, 248, 50));
        btnRefresh.setPreferredSize(new Dimension(90, 34));
        btnRefresh.addActionListener(e -> refreshFlights());
        navRight.add(btnRefresh);

        // Logout button
        FlatButton btnLogout = new FlatButton("Logout",
            new Color(255, 255, 255, 8), TEXT_SECONDARY, new Color(255, 255, 255, 25));
        btnLogout.setPreferredSize(new Dimension(80, 34));
        btnLogout.addActionListener(e -> {
            cardLayout.show(mainPanel, "LOGIN");
        });
        navRight.add(btnLogout);

        nav.add(navRight, BorderLayout.EAST);
        panel.add(nav, BorderLayout.NORTH);

        // ---- SCROLLABLE CONTENT ----
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 28, 28, 28));

        // --- STATS ROW ---
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTotal = new JLabel("0");
        lblTotalSub = new JLabel("Assigned to you");
        statsRow.add(createStatCard("Total Flights", "\uD83D\uDEEB", lblTotal, lblTotalSub, ACCENT, null));

        lblEmergency = new JLabel("0");
        lblEmergencySub = new JLabel("Requires attention");
        statsRow.add(createStatCard("Emergencies", "\uD83D\uDEA8", lblEmergency, lblEmergencySub, EMERGENCY, EMERGENCY));

        lblRunways = new JLabel("0");
        lblRunwaysSub = new JLabel("Currently in use");
        statsRow.add(createStatCard("Active Runways", "\uD83D\uDEEB", lblRunways, lblRunwaysSub, INFO, null));

        lblGates = new JLabel("0");
        lblGatesSub = new JLabel("Ready for assignment");
        statsRow.add(createStatCard("Available Gates", "\uD83D\uDEAA", lblGates, lblGatesSub, SUCCESS, null));

        content.add(statsRow);
        content.add(Box.createVerticalStrut(20));

        // --- FLIGHT SECTIONS ---
        JPanel sectionsRow = new JPanel(new GridLayout(1, 2, 20, 0));
        sectionsRow.setOpaque(false);
        sectionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Emergency section
        emergencyListPanel = new JPanel();
        emergencyListPanel.setLayout(new BoxLayout(emergencyListPanel, BoxLayout.Y_AXIS));
        emergencyListPanel.setOpaque(false);
        lblEmergencyCount = new JLabel("0");
        sectionsRow.add(createFlightSection("\uD83D\uDEA8 Emergency Flights", lblEmergencyCount,
            emergencyListPanel, EMERGENCY, "Sorted by priority"));

        // Normal section
        normalListPanel = new JPanel();
        normalListPanel.setLayout(new BoxLayout(normalListPanel, BoxLayout.Y_AXIS));
        normalListPanel.setOpaque(false);
        lblNormalCount = new JLabel("0");
        sectionsRow.add(createFlightSection("\uD83D\uDEEB Normal Flights", lblNormalCount,
            normalListPanel, ACCENT, "By scheduled time"));

        content.add(sectionsRow);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    // ======================== STAT CARD ========================
    private RoundedPanel createStatCard(String label, String icon, JLabel valueLabel,
                                         JLabel subLabel, Color color, Color leftAccent) {
        RoundedPanel card = new RoundedPanel(14, BG_CARD, GLASS_BORDER);
        if (leftAccent != null) card.setLeftAccent(leftAccent);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        top.setOpaque(false);
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        top.add(iconLbl);
        JLabel labelLbl = new JLabel(label.toUpperCase());
        labelLbl.setFont(FONT_LABEL);
        labelLbl.setForeground(TEXT_MUTED);
        top.add(labelLbl);
        card.add(top, BorderLayout.NORTH);

        valueLabel.setFont(FONT_MONO);
        valueLabel.setForeground(color);
        valueLabel.setBorder(new EmptyBorder(6, 2, 2, 0));
        card.add(valueLabel, BorderLayout.CENTER);

        subLabel.setFont(FONT_TINY);
        subLabel.setForeground(TEXT_SECONDARY);
        card.add(subLabel, BorderLayout.SOUTH);

        // Hover
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBgColor(BG_CARD_HOVER); }
            public void mouseExited(MouseEvent e)  { card.setBgColor(BG_CARD); }
        });

        return card;
    }


    // ======================== FLIGHT SECTION ========================
    private RoundedPanel createFlightSection(String title, JLabel countBadge,
                                              JPanel listPanel, Color accentColor, String sortNote) {
        RoundedPanel section = new RoundedPanel(20, BG_CARD, GLASS_BORDER);
        section.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 8));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(GLASS_BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerLeft.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_SUBHEAD);
        titleLbl.setForeground(TEXT_PRIMARY);
        headerLeft.add(titleLbl);

        countBadge.setFont(FONT_MONO_SM);
        countBadge.setForeground(accentColor);
        countBadge.setOpaque(false);
        JLabel badgeWrapper = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 35));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badgeWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 1));
        badgeWrapper.setPreferredSize(new Dimension(32, 22));
        badgeWrapper.add(countBadge);
        headerLeft.add(badgeWrapper);
        header.add(headerLeft, BorderLayout.WEST);

        JLabel sortLbl = new JLabel(sortNote);
        sortLbl.setFont(FONT_TINY);
        sortLbl.setForeground(TEXT_MUTED);
        header.add(sortLbl, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // List in scroll
        JScrollPane sp = new JScrollPane(listPanel);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(new EmptyBorder(10, 10, 10, 10));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(0, 420));
        section.add(sp, BorderLayout.CENTER);

        return section;
    }


    // ======================== FLIGHT CARD ========================
    private RoundedPanel createFlightCard(FlightInfo fi) {
        boolean isEmergency = fi.isEmergency;
        Color accentColor = isEmergency ? EMERGENCY : ACCENT;

        RoundedPanel card = new RoundedPanel(14, new Color(255, 255, 255, 5), null);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icon box
        JLabel iconBox = new JLabel(isEmergency ? "\uD83D\uDEA8" : "\uD83D\uDEEB", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconBox.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconBox.setPreferredSize(new Dimension(42, 42));
        card.add(iconBox, BorderLayout.WEST);

        // Meta info
        JPanel meta = new JPanel();
        meta.setLayout(new BoxLayout(meta, BoxLayout.Y_AXIS));
        meta.setOpaque(false);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        titleRow.setOpaque(false);
        JLabel flightTitle = new JLabel("Flight #" + fi.flightId);
        flightTitle.setFont(FONT_SUBHEAD);
        flightTitle.setForeground(TEXT_PRIMARY);
        titleRow.add(flightTitle);
        JLabel modelLbl = new JLabel(fi.aircraftModel != null ? fi.aircraftModel : "");
        modelLbl.setFont(FONT_MONO_SM);
        modelLbl.setForeground(TEXT_MUTED);
        titleRow.add(modelLbl);
        meta.add(titleRow);

        JPanel tagsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        tagsRow.setOpaque(false);

        if (isEmergency) {
            tagsRow.add(createTag(fi.emergencyType, EMERGENCY));
            tagsRow.add(createTag("P" + fi.priorityLevel, WARNING));
        }
        tagsRow.add(createTag(fi.status, INFO));
        meta.add(tagsRow);

        card.add(meta, BorderLayout.CENTER);

        // Right peek
        JPanel rightPeek = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 12));
        rightPeek.setOpaque(false);
        String peekText = fi.runwayCode != null ? fi.runwayCode : "—";
        JLabel peekLbl = new JLabel(peekText);
        peekLbl.setFont(FONT_SMALL);
        peekLbl.setForeground(TEXT_MUTED);
        rightPeek.add(peekLbl);
        JLabel arrow = new JLabel("→");
        arrow.setFont(FONT_BODY);
        arrow.setForeground(TEXT_MUTED);
        rightPeek.add(arrow);
        card.add(rightPeek, BorderLayout.EAST);

        // Hover + Click
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBgColor(isEmergency ?
                    new Color(255, 77, 106, 12) : new Color(255, 255, 255, 12));
                arrow.setForeground(accentColor);
            }
            public void mouseExited(MouseEvent e) {
                card.setBgColor(new Color(255, 255, 255, 5));
                arrow.setForeground(TEXT_MUTED);
            }
            public void mouseClicked(MouseEvent e) {
                showFlightDetail(fi);
            }
        });

        return card;
    }


    // ======================== FLIGHT DETAIL DIALOG ========================
    private void showFlightDetail(FlightInfo fi) {
        JDialog dialog = new JDialog(this, "Flight #" + fi.flightId, true);
        dialog.setSize(560, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        // Main modal panel
        RoundedPanel modal = new RoundedPanel(20, BG_SECONDARY, GLASS_BORDER);
        modal.setLayout(new BorderLayout());

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(GLASS_BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 26, 20, 26));

        JLabel headerTitle = new JLabel("\u2708\uFE0F Flight #" + fi.flightId);
        headerTitle.setFont(FONT_HEADING);
        headerTitle.setForeground(TEXT_PRIMARY);
        headerPanel.add(headerTitle, BorderLayout.WEST);

        FlatButton closeBtn = new FlatButton("✕",
            new Color(255, 255, 255, 8), TEXT_SECONDARY, new Color(255, 255, 255, 15));
        closeBtn.setPreferredSize(new Dimension(32, 32));
        closeBtn.addActionListener(e -> dialog.dispose());
        headerPanel.add(closeBtn, BorderLayout.EAST);
        modal.add(headerPanel, BorderLayout.NORTH);

        // --- Body ---
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(22, 26, 22, 26));

        // Detail grid 2x4
        String[][] details = {
            {"Flight ID", "#" + fi.flightId},
            {"Status", fi.status},
            {"Emergency", fi.isEmergency ? fi.emergencyType + " (P" + fi.priorityLevel + ")" : "None"},
            {"Aircraft", fi.aircraftModel != null ? fi.aircraftModel : "—"},
            {"Runway", fi.runwayCode != null ? fi.runwayCode : "Not assigned"},
            {"Gate", fi.gateNumber != null ? fi.gateNumber + " (" + fi.terminal + ")" : "Not assigned"},
            {"Scheduled", fi.scheduledTime != null ? fi.scheduledTime : "—"}
        };

        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(520, 220));

        for (String[] d : details) {
            RoundedPanel cell = new RoundedPanel(8, new Color(255, 255, 255, 4), GLASS_BORDER);
            cell.setLayout(new BorderLayout());
            cell.setBorder(new EmptyBorder(10, 14, 10, 14));

            JLabel dlabel = new JLabel(d[0].toUpperCase());
            dlabel.setFont(FONT_LABEL);
            dlabel.setForeground(TEXT_MUTED);
            cell.add(dlabel, BorderLayout.NORTH);

            JLabel dval = new JLabel(d[1]);
            dval.setFont(new Font("Segoe UI", Font.BOLD, 14));
            dval.setForeground(TEXT_PRIMARY);
            dval.setBorder(new EmptyBorder(4, 0, 0, 0));
            cell.add(dval, BorderLayout.CENTER);

            grid.add(cell);
        }
        // Fill last cell if odd
        if (details.length % 2 != 0) {
            grid.add(new JLabel()); // empty cell
        }

        body.add(grid);
        body.add(Box.createVerticalStrut(22));

        // --- Actions Section ---
        JLabel actionsTitle = new JLabel("CONTROLLER ACTIONS");
        actionsTitle.setFont(FONT_LABEL);
        actionsTitle.setForeground(TEXT_MUTED);
        actionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(actionsTitle);
        body.add(Box.createVerticalStrut(12));

        // Action rows
        JPanel actionsGrid = new JPanel(new GridLayout(3, 1, 0, 10));
        actionsGrid.setOpaque(false);
        actionsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsGrid.setMaximumSize(new Dimension(520, 180));

        // Runway action
        JComboBox<String> cbRunway = createStyledCombo();
        cbRunway.addItem("Select Runway");
        for (String[] r : dao.getRunways()) {
            cbRunway.addItem(r[0] + " — " + r[1] + " (" + r[2] + ")");
        }
        actionsGrid.add(createActionRow("Assign Runway", cbRunway, "Assign", INFO, e -> {
            if (cbRunway.getSelectedIndex() > 0) {
                int rId = Integer.parseInt(((String) cbRunway.getSelectedItem()).split(" — ")[0].trim());
                dao.assignRunway(fi.flightId, rId);
                showToast(dialog, "Runway assigned successfully!", SUCCESS);
                dialog.dispose();
                refreshFlights();
            }
        }));

        // Gate action
        JComboBox<String> cbGate = createStyledCombo();
        cbGate.addItem("Select Gate");
        for (String[] g : dao.getGates()) {
            cbGate.addItem(g[0] + " — " + g[1] + " (" + g[2] + ", " + g[3] + ")");
        }
        actionsGrid.add(createActionRow("Assign Gate", cbGate, "Assign", INFO, e -> {
            if (cbGate.getSelectedIndex() > 0) {
                int gId = Integer.parseInt(((String) cbGate.getSelectedItem()).split(" — ")[0].trim());
                dao.assignGate(fi.flightId, gId);
                showToast(dialog, "Gate assigned successfully!", SUCCESS);
                dialog.dispose();
                refreshFlights();
            }
        }));

        // Status action
        JComboBox<String> cbStatus = createStyledCombo();
        String[] statuses = {"Select Status", "Approaching", "Holding", "Landing", "Landed", "Taxiing", "At Gate", "Departed"};
        for (String s : statuses) cbStatus.addItem(s);
        actionsGrid.add(createActionRow("Update Status", cbStatus, "Update", SUCCESS, e -> {
            if (cbStatus.getSelectedIndex() > 0) {
                dao.updateFlightStatus(fi.flightId, (String) cbStatus.getSelectedItem());
                showToast(dialog, "Status updated to " + cbStatus.getSelectedItem() + "!", SUCCESS);
                dialog.dispose();
                refreshFlights();
            }
        }));

        body.add(actionsGrid);

        JScrollPane bodyScroll = new JScrollPane(body);
        bodyScroll.setOpaque(false);
        bodyScroll.getViewport().setOpaque(false);
        bodyScroll.setBorder(null);
        bodyScroll.getVerticalScrollBar().setUnitIncrement(16);
        modal.add(bodyScroll, BorderLayout.CENTER);

        // --- Footer ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(GLASS_BORDER);
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        footer.setOpaque(false);

        FlatButton footerClose = new FlatButton("Close",
            new Color(255, 255, 255, 8), TEXT_SECONDARY, new Color(255, 255, 255, 25));
        footerClose.setPreferredSize(new Dimension(80, 34));
        footerClose.addActionListener(e -> dialog.dispose());
        footer.add(footerClose);
        modal.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(modal);
        dialog.setVisible(true);
    }

    private JPanel createActionRow(String label, JComboBox<String> combo, String btnText,
                                    Color btnColor, ActionListener action) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setPreferredSize(new Dimension(110, 34));
        row.add(lbl, BorderLayout.WEST);

        row.add(combo, BorderLayout.CENTER);

        FlatButton btn = new FlatButton(btnText,
            new Color(btnColor.getRed(), btnColor.getGreen(), btnColor.getBlue(), 30),
            btnColor,
            new Color(btnColor.getRed(), btnColor.getGreen(), btnColor.getBlue(), 50));
        btn.setPreferredSize(new Dimension(80, 34));
        btn.addActionListener(action);
        row.add(btn, BorderLayout.EAST);

        return row;
    }  

    private JComboBox<String> createStyledCombo() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_SMALL);
        cb.setBorder(new EmptyBorder(2, 4, 2, 4));
        return cb;
    }


    // ======================== SHOW DASHBOARD ========================
    private void showDashboard() {
        navNameLabel.setText(controllerName);
        navRoleLabel.setText(controllerRole != null ? controllerRole + " Controller" : "Controller");
        navAvatarLabel.setText(controllerName.substring(0, 1).toUpperCase());
        refreshFlights();
        cardLayout.show(mainPanel, "DASHBOARD");
    }


    // ======================== REFRESH DATA ========================
    private void refreshFlights() {
        List<FlightInfo> flights = dao.getControllerFlights(controllerId);

        emergencyListPanel.removeAll();
        normalListPanel.removeAll();

        int eCount = 0;
        int rwCount = 0;

        for (FlightInfo fi : flights) {
            RoundedPanel card = createFlightCard(fi);
            if (fi.isEmergency) {
                emergencyListPanel.add(card);
                emergencyListPanel.add(Box.createVerticalStrut(6));
                eCount++;
            } else {
                normalListPanel.add(card);
                normalListPanel.add(Box.createVerticalStrut(6));
            }
            if (fi.runwayId > 0) rwCount++;
        }

        int normalCount = flights.size() - eCount;

        // Empty states
        if (eCount == 0) {
            JLabel empty = new JLabel("No emergency flights");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            empty.setForeground(TEXT_MUTED);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(40, 0, 0, 0));
            emergencyListPanel.add(empty);
        }
        if (normalCount == 0) {
            JLabel empty = new JLabel("No normal flights");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            empty.setForeground(TEXT_MUTED);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(40, 0, 0, 0));
            normalListPanel.add(empty);
        }

        // Update stats
        lblTotal.setText(String.valueOf(flights.size()));
        lblEmergency.setText(String.valueOf(eCount));
        lblRunways.setText(String.valueOf(rwCount));

        // Count available gates
        int availableGates = 0;
        try {
            for (String[] g : dao.getGates()) {
                if ("Available".equalsIgnoreCase(g[3])) availableGates++;
            }
        } catch (Exception ignored) {}
        lblGates.setText(String.valueOf(availableGates));

        lblEmergencyCount.setText(String.valueOf(eCount));
        lblNormalCount.setText(String.valueOf(normalCount));

        emergencyListPanel.revalidate();
        emergencyListPanel.repaint();
        normalListPanel.revalidate();
        normalListPanel.repaint();
    }


    // ======================== TOAST ========================
    private void showToast(Component parent, String message, Color color) {
        JDialog toast = new JDialog(SwingUtilities.getWindowAncestor(parent));
        toast.setUndecorated(true);
        toast.setBackground(new Color(0, 0, 0, 0));
        toast.setAlwaysOnTop(true);

        JLabel lbl = new JLabel(message) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 35));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
                g2.setStroke(new BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(color);
        lbl.setBorder(new EmptyBorder(12, 20, 12, 20));
        lbl.setOpaque(false);

        toast.add(lbl);
        toast.pack();

        // Position at top-right of main frame
        Point loc = getLocationOnScreen();
        toast.setLocation(loc.x + getWidth() - toast.getWidth() - 30, loc.y + 80);
        toast.setVisible(true);

        Timer timer = new Timer(2500, e -> toast.dispose());
        timer.setRepeats(false);
        timer.start();
    }
}
