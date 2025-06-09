import javax.swing.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.awt.*;
import java.awt.event.*;

public class Registration {
    public Registration() {

        JFrame regFrame = new JFrame("Billing System");
        regFrame.setSize(450, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        regFrame.setSize(screenSize.width, screenSize.height);
        regFrame.setLayout(null);

        JLabel titleLabel = new JLabel("Admin / Employee Registration Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(480, 50, 700, 50);
        regFrame.add(titleLabel);

        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBounds(550, 130, 120, 30);
        regFrame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBounds(700, 130, 250, 30);
        regFrame.add(nameField);

        JLabel userLabel = new JLabel("Username :");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setBounds(550, 180, 120, 30);
        regFrame.add(userLabel);

        JTextField userText = new JTextField();
        userText.setFont(new Font("Arial", Font.PLAIN, 16));
        userText.setBounds(700, 180, 250, 30);
        regFrame.add(userText);

        JLabel passLabel = new JLabel("Password :");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passLabel.setBounds(550, 230, 120, 30);
        regFrame.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setFont(new Font("Arial", Font.PLAIN, 16));
        passText.setBounds(700, 230, 250, 30);
        regFrame.add(passText);

        JLabel repassLabel = new JLabel("Re-enter Password :");
        repassLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        repassLabel.setBounds(550, 280, 150, 30);
        regFrame.add(repassLabel);

        JPasswordField repassText = new JPasswordField();
        repassText.setFont(new Font("Arial", Font.PLAIN, 16));
        repassText.setBounds(700, 280, 250, 30);
        regFrame.add(repassText);

        JLabel addressLabel = new JLabel("Address : ");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        addressLabel.setBounds(550, 330, 120, 30);
        regFrame.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setFont(new Font("Arial", Font.PLAIN, 16));
        addressField.setBounds(700, 330, 250, 30);
        regFrame.add(addressField);

        JLabel numberLabel = new JLabel("Mobile Number :");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        numberLabel.setBounds(550, 380, 120, 30);
        regFrame.add(numberLabel);

        JTextField numberField = new JTextField();
        numberField.setFont(new Font("Arial", Font.PLAIN, 16));
        numberField.setBounds(700, 380, 250, 30);
        regFrame.add(numberField);

        JLabel emailLabel = new JLabel("E-mail :");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setBounds(550, 430, 120, 30);
        regFrame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setBounds(700, 430, 250, 30);
        regFrame.add(emailField);

        JButton RegisterButton = new JButton("Register");
        RegisterButton.setFont(new Font("Arial", Font.BOLD, 20));
        RegisterButton.setBounds(660, 500, 150, 35);
        regFrame.add(RegisterButton);

        RegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String username = userText.getText().trim();
                String password = new String(passText.getPassword()).trim();
                String rePassword = new String(repassText.getPassword()).trim();
                String mobile = numberField.getText().trim();
                String email = emailField.getText().trim();
                String address = addressField.getText().trim();

                // Basic validations (you already have)
                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || rePassword.isEmpty() ||
                        mobile.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(regFrame, "All fields are required!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!mobile.matches("^[6-9]\\d{9}$")) {
                    JOptionPane.showMessageDialog(regFrame, "Enter a valid 10-digit mobile number starting with 6-9!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(rePassword)) {
                    JOptionPane.showMessageDialog(regFrame, "Passwords do not match!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // MongoDB connection
                try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase db = client.getDatabase("BillingSystem");
                    MongoCollection<Document> users = db.getCollection("Users");

                    // Check if username already exists
                    Document existingUser = users.find(Filters.eq("username", username)).first();
                    if (existingUser != null) {
                        JOptionPane.showMessageDialog(regFrame, "Username already exists. Please choose another.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Insert new user
                    Document newUser = new Document("name", name)
                            .append("username", username)
                            .append("password", PasswordUtils.hashPassword(password))
                            .append("mobile", mobile)
                            .append("email", email)
                            .append("address", address);
                    users.insertOne(newUser);

                    JOptionPane.showMessageDialog(regFrame, "Registration successful!");
                    new Login();
                    regFrame.dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(regFrame, "Database error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Enter Button Workable

        nameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    userText.requestFocusInWindow();
                }
            }
        });

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
                    repassText.requestFocusInWindow();
                }
            }
        });

        repassText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addressField.requestFocusInWindow();
                }
            }
        });

        addressField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    numberField.requestFocusInWindow();
                }
            }
        });

        numberField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    emailField.requestFocusInWindow();
                }
            }
        });

        emailField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    RegisterButton.doClick(); // Simulates button click
                }
            }
        });

        JLabel alreadyuserLabel = new JLabel("Already User?  :");
        alreadyuserLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        alreadyuserLabel.setBounds(580, 560, 120, 25);
        regFrame.add(alreadyuserLabel);

        JLabel backloginLabel = new JLabel("<HTML><U> Click here to Login </U></HTML>");
        backloginLabel.setForeground(Color.BLUE);
        backloginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backloginLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        backloginLabel.setBounds(700, 560, 200, 25);
        regFrame.add(backloginLabel);

        // Add MouseListener to make label clickable
        backloginLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(regFrame, "Open Login Form");
                new Login();
                regFrame.dispose();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(660, 620, 150, 35);
        regFrame.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Login();
                regFrame.dispose();
            }
        });

        regFrame.setVisible(true);
    }
}
