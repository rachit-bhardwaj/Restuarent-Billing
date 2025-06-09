import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AdminTotal {
    public AdminTotal() {
        JFrame frame = new JFrame("Total Orders List");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Total Orders List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(850, 50, 600, 40);
        frame.add(titleLabel);

        JTextField searchnameText = new JTextField();
        searchnameText.setFont(new Font("Arial", Font.PLAIN, 16));
        searchnameText.setBounds(70, 50, 190, 50);
        frame.add(searchnameText);

        JButton searchnamebutton = new JButton("Search By Name");
        searchnamebutton.setFont(new Font("Arial", Font.BOLD, 16));
        searchnamebutton.setBounds(270, 50, 190, 50);
        frame.add(searchnamebutton);

        JPanel panel = new JPanel();
        panel.setBounds(40, 120, 1450, 600);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setLayout(null);
        frame.add(panel);

        JPanel subpanel1 = new JPanel(null);
        subpanel1.setPreferredSize(new Dimension(1430, 600));

        JScrollPane scrollPane = new JScrollPane(subpanel1);
        scrollPane.setBounds(10, 10, 1430, 580);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);

        String[] headers = { "Name", "Mobile No.", "Food Name", "Quantity", "Subtotal", "Grand Total", "Date", "Time" };
        int[] xPositions = { 30, 230, 380, 630, 770, 930, 1130, 1300 };
        int[] widths = { 120, 140, 180, 100, 120, 120, 140, 140 };
        Font headerFont = new Font("Arial", Font.BOLD, 18);
        for (int j = 0; j < headers.length; j++) {
            JLabel label = new JLabel(headers[j]);
            label.setFont(headerFont);
            label.setBounds(xPositions[j], 10, widths[j], 25);
            subpanel1.add(label);
        }

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("BillingSystem");
        MongoCollection<Document> collection = database.getCollection("Bills");

        // Helper method to display bills
        Runnable displayAllBills = () -> {
            subpanel1.removeAll();
            for (int j = 0; j < headers.length; j++) {
                JLabel label = new JLabel(headers[j]);
                label.setFont(headerFont);
                label.setBounds(xPositions[j], 10, widths[j], 25);
                subpanel1.add(label);
            }
            int yPos = 40;
            Font rowFont = new Font("Arial", Font.PLAIN, 16);
            for (Document bill : collection.find().sort(Sorts.descending("date", "time"))) {
                yPos = addBillRows(subpanel1, bill, xPositions, widths, rowFont, yPos);
            }
            subpanel1.setPreferredSize(new Dimension(1430, yPos + 20));
            subpanel1.revalidate();
            subpanel1.repaint();
        };

        displayAllBills.run(); // Initial load

        // Search by name button
        searchnamebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchName = searchnameText.getText().trim().toLowerCase();
                if (searchName.isEmpty()) {
                    displayAllBills.run(); // Show all if empty
                    return;
                }

                subpanel1.removeAll();
                for (int j = 0; j < headers.length; j++) {
                    JLabel label = new JLabel(headers[j]);
                    label.setFont(headerFont);
                    label.setBounds(xPositions[j], 10, widths[j], 25);
                    subpanel1.add(label);
                }

                int yPos = 40;
                Font rowFont = new Font("Arial", Font.PLAIN, 16);
                for (Document bill : collection.find()) {
                    String billName = bill.getString("name");
                    if (billName != null && billName.toLowerCase().contains(searchName)) {
                        yPos = addBillRows(subpanel1, bill, xPositions, widths, rowFont, yPos);
                    }
                }

                subpanel1.setPreferredSize(new Dimension(1430, yPos + 20));
                subpanel1.revalidate();
                subpanel1.repaint();
            }
        });

        searchnameText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchnamebutton.doClick();
                }
            }
        });

        JButton printButton = new JButton("Print");
        printButton.setFont(new Font("Arial", Font.BOLD, 20));
        printButton.setBounds(450, 750, 200, 50);
        frame.add(printButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(820, 750, 200, 50);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Admin();
                frame.dispose();
            }
        });
        frame.add(backButton);

        frame.setVisible(true);
    }

    private int addBillRows(JPanel panel, Document bill, int[] xPositions, int[] widths, Font font, int yPos) {
        String name = bill.getString("name");
        String mobile = bill.getString("mobile");
        String date = bill.getString("date");
        String time = bill.getString("time");

        double grandTotal = bill.get("grandTotal") instanceof Number ? ((Number) bill.get("grandTotal")).doubleValue()
                : 0;

        Object itemsObj = bill.get("items");
        List<Document> items = new ArrayList<>();
        if (itemsObj instanceof List<?>) {
            for (Object obj : (List<?>) itemsObj) {
                if (obj instanceof Document)
                    items.add((Document) obj);
            }
        }

        for (Document item : items) {
            String foodName = item.getString("name");
            int quantity = item.getInteger("quantity", 0);
            double price = item.get("price") instanceof Number ? ((Number) item.get("price")).doubleValue() : 0;
            double subtotal = price * quantity;

            JLabel[] labels = {
                    new JLabel(name),
                    new JLabel(mobile),
                    new JLabel(foodName),
                    new JLabel(String.valueOf(quantity)),
                    new JLabel(String.format("%.2f", subtotal)),
                    new JLabel(String.format("%.2f", grandTotal)),
                    new JLabel(date),
                    new JLabel(time)
            };

            for (int j = 0; j < labels.length; j++) {
                labels[j].setFont(font);
                labels[j].setBounds(xPositions[j], yPos, widths[j], 20);
                panel.add(labels[j]);
            }
            yPos += 30;
        }
        return yPos;
    }
}
