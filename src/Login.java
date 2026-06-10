import javax.swing.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.awt.event.*;
import java.util.Random;
import java.awt.*;
import java.awt.image.BufferedImage;
// import java.util.Random;

public class Login {

    private String captchaText;
    private JLabel captchaLabel;
    private JTextField captchaInput;

    public Login() {

        JFrame frame = new JFrame("Login Page");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Admin / Employee Login Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(480, 50, 700, 50);
        frame.add(titleLabel);

        JLabel userLabel = new JLabel("Username :");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setBounds(550, 170, 120, 30);
        frame.add(userLabel);

        JTextField userText = new JTextField();
        userText.setFont(new Font("Arial", Font.PLAIN, 16));
        userText.setBounds(700, 170, 250, 30);
        frame.add(userText);

        JLabel passLabel = new JLabel("Password :");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passLabel.setBounds(550, 220, 120, 30);
        frame.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setFont(new Font("Arial", Font.PLAIN, 16));
        passText.setBounds(700, 220, 250, 30);
        frame.add(passText);

        //Captcha Label (Image)
        captchaLabel = new JLabel();
        captchaLabel.setBounds(550, 270, 150, 50);
        frame.add(captchaLabel);

        // Captcha Input Field
        captchaInput = new JTextField();
        captchaInput.setFont(new Font("Arial", Font.PLAIN, 16));
        captchaInput.setBounds(740, 280, 150, 30);
        frame.add(captchaInput);

        // Refresh Captcha Button
        JButton refreshBtn = new JButton("↻");
        refreshBtn.setBounds(900, 280, 50, 30);
        frame.add(refreshBtn);
        refreshBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        generateCaptchaImage();
    }
});


        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBounds(660, 350, 120, 25);
        frame.add(loginButton);

        // Generate first captcha
        generateCaptchaImage();

        JLabel newuserLabel = new JLabel("New User?  :");
        newuserLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        newuserLabel.setBounds(580, 410, 120, 25);
        frame.add(newuserLabel);

        JLabel registrationLabel = new JLabel("<HTML><U> Click here to Register </U></HTML>");
        registrationLabel.setForeground(Color.BLUE);
        registrationLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        registrationLabel.setBounds(700, 410, 200, 25);
        frame.add(registrationLabel);

        // Add MouseListener to make label clickable
        registrationLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Registration();
                frame.dispose();
            }
        });

        JLabel forgotLabel = new JLabel("Forgot Password?  :");
        forgotLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        forgotLabel.setBounds(580, 480, 200, 25);
        frame.add(forgotLabel);

        JLabel forgotpasslabel = new JLabel("<HTML><U> Click here to Forgot Password </U></HTML>");
        forgotpasslabel.setForeground(Color.BLUE);
        forgotpasslabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotpasslabel.setFont(new Font("Arial", Font.PLAIN, 16));
        forgotpasslabel.setBounds(750, 480, 300, 25);
        frame.add(forgotpasslabel);

        // Add MouseListener to make label clickable
        forgotpasslabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Forgot();
                frame.dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText().trim();
                String password = new String(passText.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username and Password required!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //  Captcha Validation
                String enteredCaptcha = captchaInput.getText().trim();
                if (!enteredCaptcha.equalsIgnoreCase(captchaText)) {
                    JOptionPane.showMessageDialog(frame, "Invalid Captcha! Try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    generateCaptchaImage();
                    captchaInput.setText("");
                    return;
                }

                // Admin Check

                if (username.equals("rachit") && password.equals("12345")) {
                    new Admin();
                    frame.dispose();
                    JOptionPane.showMessageDialog(frame, "Admin login successful!");
                    return;
                }

                // Employee (MongoDB) Check

                try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase db = client.getDatabase("BillingSystem");
                    MongoCollection<Document> users = db.getCollection("Users");

                    Document user = users.find(Filters.and(
                            Filters.eq("username", username),
                            Filters.eq("password", PasswordUtils.hashPassword(password)))).first();

                    if (user != null) {
                        new Main();
                        frame.dispose();
                        JOptionPane.showMessageDialog(frame, "Login successful!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid username or password!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Enter Button Workable

        userText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passText.requestFocusInWindow();
                }
            }
        });

        passText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    captchaInput.requestFocusInWindow(); 
                }
            }
        });

        captchaInput.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loginButton.doClick(); // Simulates login button click
        }
    }
});

        // Exit Food Section
        JButton exitbutton = new JButton("Exit");
        exitbutton.setFont(new Font("Arial", Font.BOLD, 20));
        exitbutton.setBounds(658, 540, 120, 25);
        ;
        frame.add(exitbutton);

        exitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    //  Generate Captcha Image
    private void generateCaptchaImage() {
        int width = 150, height = 50;
        BufferedImage captchaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = captchaImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        captchaText = randomText(5);
        g2d.setFont(new Font("Verdana", Font.BOLD, 28));
        Random rand = new Random();

        // draw noise
        for (int i = 0; i < 8; i++) {
            g2d.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            g2d.drawLine(rand.nextInt(width), rand.nextInt(height), rand.nextInt(width), rand.nextInt(height));
        }

        // draw text
        for (int i = 0; i < captchaText.length(); i++) {
            g2d.setColor(new Color(rand.nextInt(150), rand.nextInt(150), rand.nextInt(150)));
            int x = 20 + i * 25;
            int y = 30 + rand.nextInt(10);
            g2d.drawString(String.valueOf(captchaText.charAt(i)), x, y);
        }

        g2d.dispose();
        captchaLabel.setIcon(new ImageIcon(captchaImage));
    }

    // Random Captcha Text Generator
    private String randomText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
