import javax.swing.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import java.awt.*;
import java.awt.event.*;
import java.util.function.BiConsumer;

public class Employee {
    public Employee() {

        // MongoDB setup
        MongoClient mongoClient = MongoClients.create(); // localhost:27017
        MongoDatabase db = mongoClient.getDatabase("BillingSystem");
        MongoCollection<Document> users = db.getCollection("Users");

        JFrame frame = new JFrame("Employee Details");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Employee Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(550, 50, 600, 50);
        frame.add(titleLabel);

        // Panel 1 – Employee Table
        JPanel panel1 = new JPanel();
        panel1.setBounds(40, 120, 900, 600);
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel1.setLayout(null);
        frame.add(panel1);

        JLabel titlepanel1 = new JLabel("Employee List");
        titlepanel1.setFont(new Font("Arial", Font.BOLD, 25));
        titlepanel1.setBounds(370, 30, 250, 30);
        panel1.add(titlepanel1);

        JTextField searchText = new JTextField();
        searchText.setFont(new Font("Arial", Font.PLAIN, 16));
        searchText.setBounds(50, 80, 350, 30);
        panel1.add(searchText);

        JButton searchbuttton = new JButton("Search Employee Name");
        searchbuttton.setFont(new Font("Arial", Font.BOLD, 16));
        searchbuttton.setBounds(420, 80, 250, 30);
        panel1.add(searchbuttton);

        // Scrollable subpanel for list
        JPanel subPanel = new JPanel(null);
        subPanel.setPreferredSize(new Dimension(850, 1000)); // Can be adjusted dynamically

        JScrollPane scrollPane = new JScrollPane(
                subPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        scrollPane.setBounds(25, 130, 850, 420); // Matches panel1 layout
        panel1.add(scrollPane);

        // Header labels
        String[] headers = { "S.No", "Name", "Username", "Mobile No.", "Email", "Address" };
        int[] xPositions = { 20, 80, 220, 360, 520, 710 };
        int[] widths = { 50, 150, 150, 150, 180, 250 };
        Font headerFont = new Font("Arial", Font.BOLD, 16);

        for (int j = 0; j < headers.length; j++) {
            JLabel label = new JLabel(headers[j]);
            label.setFont(headerFont);
            label.setBounds(xPositions[j], 10, widths[j], 25);
            subPanel.add(label);
        }

        // Clear utility
        Runnable clearPanel = () -> {
            for (Component comp : subPanel.getComponents()) {
                if (comp instanceof JLabel && comp.getY() >= 40) {
                    subPanel.remove(comp);
                }
            }
            subPanel.revalidate();
            subPanel.repaint();
        };

        // Add row function
        BiConsumer<Document, Integer> addRow = (doc, index) -> {
            int y = 40 + (index - 1) * 35;
            Font rowFont = new Font("Arial", Font.PLAIN, 15);

            JLabel sno = new JLabel(String.valueOf(index));
            JLabel name = new JLabel(doc.getString("name"));
            JLabel username = new JLabel(doc.getString("username"));
            JLabel mobile = new JLabel(doc.getString("mobile"));
            JLabel email = new JLabel(doc.getString("email"));
            JLabel address = new JLabel(doc.getString("address"));

            JLabel[] labels = { sno, name, username, mobile, email, address };

            for (int j = 0; j < labels.length; j++) {
                labels[j].setFont(rowFont);
                labels[j].setBounds(xPositions[j], y, widths[j], 25);
                subPanel.add(labels[j]);
            }
        };

        // Load all initially
        int index = 1;
        for (Document doc : users.find()) {
            addRow.accept(doc, index++);
        }

        // Search functionality
        searchbuttton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPanel.run(); // Clear old rows, retain header

                String keyword = searchText.getText().trim().toLowerCase();
                int i = 1;

                for (Document doc : users.find()) {
                    String name = doc.getString("name");
                    if (name != null && name.toLowerCase().contains(keyword)) {
                        addRow.accept(doc, i++);
                    }
                }

                subPanel.setPreferredSize(new Dimension(850, Math.max(420, i * 35 + 50)));
                subPanel.revalidate();
                subPanel.repaint();
            }
        });

        searchText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchbuttton.doClick(); // Simulates button click
                }
            }
        });

        // Panel 2 – Delete Employee

        JPanel panel2 = new JPanel();
        panel2.setBounds(950, 120, 500, 600);
        panel2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel2.setLayout(null);
        frame.add(panel2);

        JLabel titlepanel2 = new JLabel("Delete Employee ID");
        titlepanel2.setFont(new Font("Arial", Font.BOLD, 25));
        titlepanel2.setBounds(135, 30, 250, 30);
        panel2.add(titlepanel2);

        JLabel userLabel = new JLabel("Username :");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setBounds(50, 180, 120, 30);
        panel2.add(userLabel);

        JTextField userText = new JTextField();
        userText.setFont(new Font("Arial", Font.PLAIN, 16));
        userText.setBounds(230, 180, 250, 30);
        panel2.add(userText);

        JLabel confirmuserLabel = new JLabel("Confirm Username :");
        confirmuserLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmuserLabel.setBounds(50, 240, 150, 30);
        panel2.add(confirmuserLabel);

        JTextField confirmuserText = new JTextField();
        confirmuserText.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmuserText.setBounds(230, 240, 250, 30);
        panel2.add(confirmuserText);

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 20));
        deleteButton.setBounds(170, 310, 220, 40);
        panel2.add(deleteButton);

        userText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmuserText.requestFocusInWindow();
                }
            }
        });

        confirmuserText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    deleteButton.doClick(); // Simulates button click
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText().trim();
                String confirmUsername = confirmuserText.getText().trim();

                if (username.isEmpty() || confirmUsername.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Both username fields are required.", "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!username.equals(confirmUsername)) {
                    JOptionPane.showMessageDialog(frame, "Username and Confirm Username do not match.", "Mismatch",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Document query = new Document("username", username);
                DeleteResult result = users.deleteOne(query);

                if (result.getDeletedCount() > 0) {
                    JOptionPane.showMessageDialog(frame, "Employee deleted successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    userText.setText("");
                    confirmuserText.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(655, 750, 200, 50);
        frame.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Admin();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
