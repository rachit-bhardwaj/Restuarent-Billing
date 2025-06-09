import javax.swing.*;
import java.awt.*;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.awt.event.*;

public class Forgot {
    public Forgot() {

        JFrame frame = new JFrame("Forgot Page");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Employee Forgot Password Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(450, 50, 700, 50);
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

        JLabel repassLabel = new JLabel("Re-enter Password :");
        repassLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        repassLabel.setBounds(550, 280, 150, 30);
        frame.add(repassLabel);

        JPasswordField repassText = new JPasswordField();
        repassText.setFont(new Font("Arial", Font.PLAIN, 16));
        repassText.setBounds(700, 280, 250, 30);
        frame.add(repassText);

        JButton forgotbutton = new JButton("Forgot Password");
        forgotbutton.setFont(new Font("Arial", Font.BOLD, 20));
        forgotbutton.setBounds(658, 350, 250, 25);
        frame.add(forgotbutton);

        forgotbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = userText.getText().trim();
                String newPassword = PasswordUtils.hashPassword(new String(passText.getPassword()).trim());
                String confirmPassword = PasswordUtils.hashPassword(new String(repassText.getPassword()).trim());

                if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase db = client.getDatabase("BillingSystem");
                    MongoCollection<Document> users = db.getCollection("Users");

                    Document user = users.find(Filters.eq("username", username)).first();

                    if (user != null) {
                        users.updateOne(Filters.eq("username", username),
                                new Document("$set", new Document("password", newPassword)));
                        JOptionPane.showMessageDialog(frame, "Password updated successfully!");
                        new Login();
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
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
                    forgotbutton.doClick(); // Simulates button click
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(660, 420, 200, 25); 
        frame.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Login();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
