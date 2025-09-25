import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class LostLinkPresentationUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String loggedUser = "";

    private ImageIcon icLogo, icUser, icLock, icEmail, icPhone, icUpload;

    public LostLinkPresentationUI() {
        super("LostLink - Campus Lost & Found");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(820, 600));

        loadIcons();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createSignupPanel(), "Signup");
        mainPanel.add(createLostItemPanel(), "LostItem");
        mainPanel.add(createFoundItemPanel(), "FoundItem");
        mainPanel.add(createConfirmationPanel(), "Confirm");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private void loadIcons() {
        icLogo = loadIcon("logo.png", 140, 140);
        icUser = loadIcon("user.png", 20, 20);
        icLock = loadIcon("lock.png", 20, 20);
        icEmail = loadIcon("email.png", 20, 20);
        icPhone = loadIcon("phone.png", 20, 20);
        icUpload = loadIcon("upload.png", 22, 22);
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(new Color(200,200,200));
            g.fillRect(0,0,w,h);
            g.dispose();
            return new ImageIcon(img);
        }
    }

    
    private void addPlaceholder(JTextComponent field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    
    private void setupImageUpload(JButton uploadBtn, JPanel parent) {
        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                        JLabel preview = new JLabel(new ImageIcon(scaled));
                        preview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        parent.add(preview, BorderLayout.EAST);
                        parent.revalidate();
                        parent.repaint();
                        uploadBtn.setText("Change Image");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static class RoundedPanel extends JPanel {
        private Color bg;
        private int radius;
        private boolean shadow;

        public RoundedPanel(Color bg, int radius, boolean shadow) {
            this.bg = bg;
            this.radius = radius;
            this.shadow = shadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            if (shadow) {
                g2.setColor(new Color(0,0,0,45));
                g2.fillRoundRect(8, 8, Math.max(0, w-16), Math.max(0, h-16), radius, radius);
            }
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w, h, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JTextField styledTextField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(Color.BLACK);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        return f;
    }

    private JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(Color.BLACK);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        return f;
    }

    private JTextArea styledTextArea(int rows, int cols) {
        JTextArea a = new JTextArea(rows, cols);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        a.setForeground(Color.BLACK);
        a.setBackground(Color.WHITE);
        a.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return a;
    }

    private JPanel iconField(ImageIcon icon, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(icon);
        l.setBorder(new EmptyBorder(0, 6, 0, 0));
        p.add(l, BorderLayout.WEST);

        RoundedPanel rp = new RoundedPanel(new Color(255,255,255,235), 12, false);
        rp.setLayout(new BorderLayout());
        rp.setBorder(new EmptyBorder(4,4,4,4));
        rp.add(field, BorderLayout.CENTER);
        p.add(rp, BorderLayout.CENTER);
        return p;
    }

    private JButton flatButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 40));
        b.setBorder(new CompoundBorder(new LineBorder(bg.darker(), 2, true), new EmptyBorder(6,12,6,12)));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { b.setBackground(bg); }
        });
        return b;
    }

    private JLabel headline(String text, int size, Color fg) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, size));
        l.setForeground(fg == null ? Color.WHITE : fg);
        return l;
    }

    // ---------- Pages ----------

    private JPanel createLoginPanel() {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(68,0,119), getWidth(), getHeight(), new Color(12,12,30));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JLabel heading = headline("LostLink", 44, Color.WHITE);
        JLabel subtitle = new JLabel("Campus lost & found — fast, safe, and simple", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(235,235,235));

        JLabel logo = new JLabel(icLogo);

        JTextField userField = styledTextField();
        addPlaceholder(userField, "Enter username");

        JPasswordField passField = styledPasswordField();
        addPlaceholder(passField, "Enter password");

        JPanel inputCard = new JPanel(new GridBagLayout());
        inputCard.setOpaque(false);
        GridBagConstraints bgb = new GridBagConstraints();
        bgb.gridx = 0; bgb.insets = new Insets(10,6,10,6);

        bgb.gridy = 0; inputCard.add(iconField(icUser, userField), bgb);
        bgb.gridy = 1; inputCard.add(iconField(icLock, passField), bgb);

        bgb.gridy = 2;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        JButton loginBtn = flatButton("Login", new Color(0,150,76));
        JButton signupBtn = flatButton("Sign Up", new Color(33,123,243));
        btnRow.add(loginBtn); btnRow.add(signupBtn);
        inputCard.add(btnRow, bgb);

        bgb.gridy = 3;
        JLabel forgot = new JLabel("<HTML><U>Forgot password?</U></HTML>", SwingConstants.CENTER);
        forgot.setForeground(new Color(220,220,220));
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputCard.add(forgot, bgb);

        GridBagConstraints cgc = new GridBagConstraints();
        cgc.gridy = 0; center.add(heading, cgc);
        cgc.gridy = 1; center.add(logo, cgc);
        cgc.gridy = 2; center.add(subtitle, cgc);
        cgc.gridy = 3; center.add(inputCard, cgc);

        bg.add(center);

        loginBtn.addActionListener(e -> cardLayout.show(mainPanel, "LostItem"));
        signupBtn.addActionListener(e -> cardLayout.show(mainPanel, "Signup"));

        return bg;
    }

    private JPanel createSignupPanel() {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(255,200,0), getWidth(), getHeight(), new Color(255,120,0));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        RoundedPanel card = new RoundedPanel(new Color(250,245,240,245), 18, true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(720, 520));

        JLabel title = headline("Create Account", 28, Color.BLACK);

        JTextField name = styledTextField(); addPlaceholder(name, "Enter your name");
        JTextField email = styledTextField(); addPlaceholder(email, "Enter email address");
        JTextField phone = styledTextField(); addPlaceholder(phone, "Enter phone number");
        JPasswordField pass = styledPasswordField(); addPlaceholder(pass, "Enter password");
        JPasswordField confirm = styledPasswordField(); addPlaceholder(confirm, "Confirm password");

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints fgb = new GridBagConstraints();
        fgb.gridx = 0; fgb.insets = new Insets(10,8,10,8);

        fgb.gridy = 0; fields.add(iconField(icUser, name), fgb);
        fgb.gridy = 1; fields.add(iconField(icEmail, email), fgb);
        fgb.gridy = 2; fields.add(iconField(icPhone, phone), fgb);
        fgb.gridy = 3; fields.add(iconField(icLock, pass), fgb);
        fgb.gridy = 4; fields.add(iconField(icLock, confirm), fgb);

        fgb.gridy = 5;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        JButton register = flatButton("Register", new Color(0,140,120));
        JButton back = flatButton("Back to Login", new Color(96,125,139));
        btnRow.add(register); btnRow.add(back);
        fields.add(btnRow, fgb);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; card.add(title, gbc);
        gbc.gridy = 1; card.add(fields, gbc);

        bg.add(card);

        register.addActionListener(e -> cardLayout.show(mainPanel, "LostItem"));
        back.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        return bg;
    }

    private JPanel createLostItemPanel() {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(0,200,230), getWidth(), getHeight(), new Color(72,200,240));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bg.setLayout(new BorderLayout(24, 24));
        bg.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel top = headline("Report a Lost Item", 30, Color.BLACK);

        RoundedPanel form = new RoundedPanel(new Color(255,255,255,245), 16, true);
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(24, 30, 24, 30));
        form.setLayout(new BorderLayout(12,12)); // allow EAST preview placement

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);

        JTextField name = styledTextField(); addPlaceholder(name, "Item name");
        JTextField category = styledTextField(); addPlaceholder(category, "Category");
        JTextField location = styledTextField(); addPlaceholder(location, "Last seen location");
        JTextArea desc = styledTextArea(4, 36); addPlaceholder(desc, "Describe the item...");

        JButton upload = flatButton("Upload Image", new Color(40,120,220));
        JButton next = flatButton("Next — Found Item", new Color(230,90,50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(12,12,12,12);
        gbc.gridy = 0; inner.add(iconField(icUser, name), gbc);
        gbc.gridy = 1; inner.add(iconField(icUpload, category), gbc);
        gbc.gridy = 2; inner.add(iconField(icUpload, location), gbc);

        gbc.gridy = 3;
        JPanel descWrap = new JPanel(new BorderLayout());
        descWrap.setOpaque(false);
        JLabel dlabel = new JLabel("Description:"); dlabel.setForeground(Color.BLACK);
        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setBorder(null);
        descScroll.setPreferredSize(new Dimension(380, 110));
        descWrap.add(dlabel, BorderLayout.NORTH);
        descWrap.add(descScroll, BorderLayout.CENTER);
        inner.add(descWrap, gbc);

        gbc.gridy = 4;
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        row.setOpaque(false);
        row.add(upload); row.add(next);
        inner.add(row, gbc);

        form.add(inner, BorderLayout.CENTER);

        bg.add(top, BorderLayout.NORTH);
        bg.add(form, BorderLayout.CENTER);

        setupImageUpload(upload, form);
        next.addActionListener(e -> cardLayout.show(mainPanel, "FoundItem"));

        return bg;
    }

    private JPanel createFoundItemPanel() {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(255,110,180), getWidth(), getHeight(), new Color(140,0,200));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bg.setLayout(new BorderLayout(18,18));
        bg.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel top = headline("Found Item Report", 30, Color.BLACK);

        RoundedPanel card = new RoundedPanel(new Color(255,255,255,245), 16, true);
        card.setLayout(new BorderLayout(12,12));
        card.setBorder(new EmptyBorder(24,24,24,24));

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);

        JTextField name = styledTextField(); addPlaceholder(name, "Item name");
        JTextField loc = styledTextField(); addPlaceholder(loc, "Location found");
        JTextArea desc = styledTextArea(4,36); addPlaceholder(desc, "Describe the found item...");

        JButton upload = flatButton("Upload Photo", new Color(200, 20, 120));
        JButton submit = flatButton("Submit Report", new Color(120, 30, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(10,12,10,12);
        gbc.gridy = 0; inner.add(iconField(icUser, name), gbc);
        gbc.gridy = 1; inner.add(iconField(icUpload, loc), gbc);

        gbc.gridy = 2;
        JPanel descWrap = new JPanel(new BorderLayout());
        descWrap.setOpaque(false);
        JLabel dlabel = new JLabel("Description:"); dlabel.setForeground(Color.BLACK);
        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setBorder(null);
        descScroll.setPreferredSize(new Dimension(380, 110));
        descWrap.add(dlabel, BorderLayout.NORTH);
        descWrap.add(descScroll, BorderLayout.CENTER);
        inner.add(descWrap, gbc);

        gbc.gridy = 3;
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        row.setOpaque(false);
        row.add(upload); row.add(submit);
        inner.add(row, gbc);

        card.add(inner, BorderLayout.CENTER);
        bg.add(top, BorderLayout.NORTH);
        bg.add(card, BorderLayout.CENTER);

        setupImageUpload(upload, card);
        submit.addActionListener(e -> cardLayout.show(mainPanel, "Confirm"));

        return bg;
    }

    private JPanel createConfirmationPanel() {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Color c1 = new Color(102, 255, 178);
                Color c2 = new Color(0, 180, 100);
                GradientPaint gp = new GradientPaint(0,0,c1, getWidth(), getHeight(), c2);
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);

        JPanel checkCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                int size = Math.min(getWidth(), getHeight());
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,230));
                g2.fillOval(0,0,getWidth(),getHeight());
                g2.setStroke(new BasicStroke(Math.max(8, size/18), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(0,120,0));
                int w = getWidth(), h = getHeight();
                g2.drawLine(w/4, h/2, w/2, 3*h/4);
                g2.drawLine(w/2, 3*h/4, 3*w/4, h/4);
            }
        };
        checkCircle.setPreferredSize(new Dimension(180,180));
        checkCircle.setOpaque(false);

        JLabel msg = new JLabel("<html><center><b>Report Submitted!</b><br/>Thanks — the community will be notified.</center></html>", SwingConstants.CENTER);
        msg.setFont(new Font("Segoe UI", Font.BOLD, 26));
        msg.setForeground(Color.white);

        JButton finish = flatButton("Back to Login", new Color(220,80,20));
        finish.setFont(new Font("Segoe UI", Font.BOLD, 16));
        finish.addActionListener(e -> {
            loggedUser = "";
            cardLayout.show(mainPanel, "Login");
        });

        gbc.gridy = 0; bg.add(checkCircle, gbc);
        gbc.gridy = 1; bg.add(msg, gbc);
        gbc.gridy = 2; bg.add(finish, gbc);

        return bg;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            LostLinkPresentationUI ui = new LostLinkPresentationUI();
            ui.setVisible(true);
        });
    }
}

