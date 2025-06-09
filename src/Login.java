import javax.swing.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.awt.event.*;
import java.awt.*;

public class Login {
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

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBounds(658, 300, 120, 25);
        frame.add(loginButton);

        JLabel newuserLabel = new JLabel("New User?  :");
        newuserLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        newuserLabel.setBounds(580, 360, 120, 25);
        frame.add(newuserLabel);

        JLabel registrationLabel = new JLabel("<HTML><U> Click here to Register </U></HTML>");
        registrationLabel.setForeground(Color.BLUE);
        registrationLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        registrationLabel.setBounds(700, 360, 200, 25);
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
        forgotLabel.setBounds(580, 420, 200, 25);
        frame.add(forgotLabel);

        JLabel forgotpasslabel = new JLabel("<HTML><U> Click here to Forgot Password </U></HTML>");
        forgotpasslabel.setForeground(Color.BLUE);
        forgotpasslabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotpasslabel.setFont(new Font("Arial", Font.PLAIN, 16));
        forgotpasslabel.setBounds(750, 420, 300, 25);
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

                // Admin Check

                if (username.equals("rachit") && password.equals("967554")) {
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
                    loginButton.doClick(); // Simulates button click
                }
            }
        });

        // Exit Food Section
        JButton exitbutton = new JButton("Exit");
        exitbutton.setFont(new Font("Arial", Font.BOLD, 20));
        exitbutton.setBounds(658, 480, 120, 25);
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
}
