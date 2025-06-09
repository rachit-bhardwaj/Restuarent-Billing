import javax.swing.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.*;

public class Order {
    public Order() {
        class OrderedItem {

            String name;
            int quantity;
            double price;

            OrderedItem(String name, int quantity, double price) {
                this.name = name;
                this.quantity = quantity;
                this.price = price;
            }
        }

        // MongoDB connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("FoodDB");
        MongoCollection<Document> collection = database.getCollection("Foods");

        JFrame frame = new JFrame("Billing Section");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Order / Billing Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(530, 50, 600, 50);
        frame.add(titleLabel);

        // Panel 1 (Total Food)
        JPanel panel1 = new JPanel();
        panel1.setBounds(50, 150, 450, 600);
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel1.setLayout(null);
        frame.add(panel1);

        JLabel titlepanel1 = new JLabel("Total Foods");
        titlepanel1.setFont(new Font("Arial", Font.BOLD, 25));
        titlepanel1.setBounds(150, 20, 150, 30);
        panel1.add(titlepanel1);

        JTextField searchText = new JTextField();
        searchText.setFont(new Font("Arial", Font.PLAIN, 16));
        searchText.setBounds(30, 70, 250, 30);
        panel1.add(searchText);

        JButton searchbuttton = new JButton("Search");
        searchbuttton.setFont(new Font("Arial", Font.BOLD, 16));
        searchbuttton.setBounds(290, 70, 130, 30);
        panel1.add(searchbuttton);

        JPanel subpanel1 = new JPanel(null);
        subpanel1.setPreferredSize(new Dimension(380, 2000)); // Large height to enable scrolling
        subpanel1.setBackground(new Color(240, 240, 240)); // Match frame color if needed

        JScrollPane scrollPane1 = new JScrollPane(subpanel1,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setBounds(25, 120, 400, 460);
        scrollPane1.getViewport().setBackground(new Color(240, 240, 240));
        scrollPane1.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        panel1.add(scrollPane1);

        // Header for subpanel1
        String[] headers1 = { "S.No", "ID", "Name", "Price" };
        int[] xPositions1 = { 20, 90, 160, 340 };
        int[] widths1 = { 40, 60, 150, 80 };
        Font headerFont1 = new Font("Arial", Font.BOLD, 16);

        for (int j = 0; j < headers1.length; j++) {
            JLabel label = new JLabel(headers1[j]);
            label.setFont(headerFont1);
            label.setBounds(xPositions1[j], 10, widths1[j], 25);
            subpanel1.add(label);
        }

        // Utility to refresh subpanel1 content
        Runnable refreshSubpanel1 = () -> {
            Component[] comps = subpanel1.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JLabel && comp.getY() >= 40) {
                    subpanel1.remove(comp);
                }
            }
            subpanel1.revalidate();
            subpanel1.repaint();
        };

        // Load all foods initially in subpanel1
        int yPos1 = 40;
        int index1 = 1;
        Font rowFont1 = new Font("Arial", Font.PLAIN, 15);

        for (Document doc : collection.find()) {
            JLabel sno = new JLabel(String.valueOf(index1));
            JLabel fid = new JLabel(String.valueOf(doc.get("foodid")));
            JLabel fname = new JLabel(doc.getString("name"));
            JLabel fprice = new JLabel(String.valueOf(doc.get("price")));

            JLabel[] labels = { sno, fid, fname, fprice };

            for (int j = 0; j < labels.length; j++) {
                labels[j].setFont(rowFont1);
                labels[j].setBounds(xPositions1[j], yPos1, widths1[j], 20);
                subpanel1.add(labels[j]);
            }

            yPos1 += 30;
            index1++;
        }

        searchbuttton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshSubpanel1.run(); // Clears old data

                String keyword = searchText.getText().trim().toLowerCase();

                int y = 40;
                int i = 1;

                for (Document doc : collection.find()) {
                    String name = doc.getString("name");
                    if (name != null && name.toLowerCase().contains(keyword)) {
                        JLabel sno = new JLabel(String.valueOf(i));
                        JLabel fid = new JLabel(String.valueOf(doc.get("foodid")));
                        JLabel fname = new JLabel(doc.getString("name"));
                        JLabel fprice = new JLabel(String.valueOf(doc.get("price")));

                        JLabel[] labels = { sno, fid, fname, fprice };

                        for (int j = 0; j < labels.length; j++) {
                            labels[j].setFont(rowFont1);
                            labels[j].setBounds(xPositions1[j], y, widths1[j], 20);
                            subpanel1.add(labels[j]);
                        }

                        y += 30;
                        i++;
                    }
                }
                subpanel1.setPreferredSize(new Dimension(380, y + 100));
                subpanel1.revalidate();
                subpanel1.repaint();
            }
        });

        searchText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchbuttton.doClick(); // Simulates button click
                }
            }
        });

        // Panel 2 (Order Food)
        JPanel panel2 = new JPanel();
        panel2.setBounds(535, 150, 450, 600);
        panel2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel2.setLayout(null);
        frame.add(panel2);

        JLabel titlepanel2 = new JLabel("Order Food");
        titlepanel2.setFont(new Font("Arial", Font.BOLD, 25));
        titlepanel2.setBounds(150, 20, 150, 30);
        panel2.add(titlepanel2);

        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBounds(50, 70, 120, 30);
        panel2.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBounds(200, 70, 200, 30);
        panel2.add(nameField);

        JLabel numberLabel = new JLabel("Mobile Number :");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        numberLabel.setBounds(50, 100, 150, 30);
        panel2.add(numberLabel);

        JTextField numberField = new JTextField();
        numberField.setFont(new Font("Arial", Font.PLAIN, 16));
        numberField.setBounds(200, 100, 200, 30);
        panel2.add(numberField);

        JLabel foodidLabel = new JLabel("Food ID :");
        foodidLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        foodidLabel.setBounds(50, 130, 120, 30);
        panel2.add(foodidLabel);

        JTextField foodidttext = new JTextField();
        foodidttext.setFont(new Font("Arial", Font.PLAIN, 16));
        foodidttext.setBounds(200, 130, 200, 30);
        panel2.add(foodidttext);

        JLabel quantityLabel = new JLabel("Quantity :");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityLabel.setBounds(50, 160, 120, 30);
        panel2.add(quantityLabel);

        JTextField quantityText = new JTextField();
        quantityText.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityText.setBounds(200, 160, 200, 30);
        panel2.add(quantityText);

        JButton addbutton = new JButton("Add");
        addbutton.setFont(new Font("Arial", Font.BOLD, 16));
        addbutton.setBounds(120, 200, 250, 20);
        panel2.add(addbutton);

        // Sub Panel 2

        JPanel subpanel2 = new JPanel(null);
        subpanel2.setPreferredSize(new Dimension(380, 2000)); // large height for scrolling
        subpanel2.setBackground(new Color(245, 245, 245)); // optional, makes it cleaner

        JScrollPane scrollPane2 = new JScrollPane(subpanel2,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setBounds(25, 230, 400, 310);
        scrollPane2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane2.getVerticalScrollBar().setUnitIncrement(16); // smooth scrolling
        panel2.add(scrollPane2);

        // Header for subpanel2
        String[] headers2 = { "Food Name", "Category", "Quantity", "Amount" };
        int[] xPositions2 = { 20, 150, 240, 320 }; // Adjust X positions to fit properly
        int[] widths2 = { 120, 80, 80, 80 }; // Column widths similar to panel 1
        Font headerFont2 = new Font("Arial", Font.BOLD, 16);

        for (int i = 0; i < headers2.length; i++) {
            JLabel header = new JLabel(headers2[i]);
            header.setFont(headerFont2);
            header.setBounds(xPositions2[i], 10, widths2[i], 25);
            subpanel2.add(header);
        }

        ArrayList<OrderedItem> orderedItems = new ArrayList<>();
        java.util.List<JLabel[]> addedRows = new ArrayList<>();

        addbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int t = addedRows.size() * 30 + 40;
                String name = nameField.getText().trim();
                String mobile = numberField.getText().trim();
                String foodIdText = foodidttext.getText().trim();
                String quantitystr = quantityText.getText().trim();

                // Basic validation
                if (name.isEmpty() || mobile.isEmpty() || foodIdText.isEmpty() || quantitystr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required.");
                    return;
                }

                if (!mobile.matches("^[6-9]\\d{9}$")) {
                    JOptionPane.showMessageDialog(frame, "Enter a valid 10-digit mobile number(6-9).");
                    return;
                }

                int foodId;
                int quantity;

                try {
                    foodId = Integer.parseInt(foodIdText);
                    quantity = Integer.parseInt(quantitystr);
                    if (quantity <= 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Food ID and Quantity must be positive integers.");
                    return;
                }

                // Check food ID in MongoDB
                Document food = collection.find(eq("foodid", foodId)).first();
                if (food == null) {
                    JOptionPane.showMessageDialog(frame, "Food ID not found.");
                    return;
                }

                // Extract details
                String foodName = food.getString("name");
                String category = food.getString("category");
                double price = Double.parseDouble(food.get("price").toString());
                double totalPrice = price * quantity;

                // Add row to subpanel2
                String[] values = { foodName, category, String.valueOf(quantity), String.valueOf(totalPrice) };
                int y = addedRows.size() * 30 + 40;
                Font rowFont = new Font("Arial", Font.PLAIN, 15);

                JLabel[] row = new JLabel[4];
                for (int i = 0; i < 4; i++) {
                    row[i] = new JLabel(values[i]);
                    row[i].setFont(rowFont);
                    row[i].setBounds(xPositions2[i], y, widths2[i], 25);
                    subpanel2.add(row[i]);
                }

                addedRows.add(row);
                orderedItems.add(new OrderedItem(foodName, quantity, totalPrice));
                subpanel2.setPreferredSize(new Dimension(380, t + 50));
                subpanel2.revalidate();
                subpanel2.repaint();
            }
        });

        nameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    numberField.requestFocusInWindow();
                }
            }
        });

        numberField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    foodidttext.requestFocusInWindow();
                }
            }
        });

        foodidttext.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    quantityText.requestFocusInWindow();
                }
            }
        });

        quantityText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addbutton.doClick(); // Simulates button click
                }
            }
        });

        // Panel 3 (Billing)
        JPanel panel3 = new JPanel();
        panel3.setBounds(1020, 150, 450, 600);
        panel3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel3.setLayout(null); // Using absolute layout
        frame.add(panel3);

        // Title for panel 3
        JLabel titlepanel3 = new JLabel("Bill");
        titlepanel3.setFont(new Font("Arial", Font.BOLD, 25));
        titlepanel3.setBounds(200, 20, 100, 30);
        panel3.add(titlepanel3);

        // Sub Panel 3 for bill display
        JPanel subpanel3 = new JPanel();
        subpanel3.setLayout(new BoxLayout(subpanel3, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(subpanel3);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(25, 70, 400, 470); // Use setBounds since panel3 uses null layout
        panel3.add(scrollPane);

        JButton billbutton = new JButton("Bill");
        billbutton.setFont(new Font("Arial", Font.BOLD, 16));
        billbutton.setBounds(100, 550, 100, 40);
        panel2.add(billbutton);

        billbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subpanel3.removeAll();
                subpanel3.setLayout(new BoxLayout(subpanel3, BoxLayout.Y_AXIS));

                // Get current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                String currentDate = now.format(dateFormat);
                String currentTime = now.format(timeFormat);

                // Get name and mobile from fields
                String name = nameField.getText().trim();
                String mobile = numberField.getText().trim();

                // Helper to add left-aligned label
                Consumer<String> addLeftLabel = (text) -> {
                    JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel label = new JLabel(text);
                    label.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    linePanel.add(label);
                    linePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    subpanel3.add(linePanel);
                };

                // Header
                JLabel headerLabel = new JLabel("              RACHIT INDIAN RESTAURANT");
                headerLabel.setFont(new Font("Serif", Font.BOLD, 18)); // Increase font size here
                headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                subpanel3.add(headerLabel);
                addLeftLabel.accept("              Pital Nagri, Moradabad");
                addLeftLabel.accept("------------------- Bill Receipt--------------------");
                subpanel3.add(Box.createVerticalStrut(10)); // Spacer
                addLeftLabel.accept(String.format("Name: %-20s  Mobile No: %s", name, mobile));
                addLeftLabel.accept(String.format("Date: %-24s   Time: %s", currentDate, currentTime));
                addLeftLabel.accept("----------------------------------------------------");

                // Table header
                addLeftLabel.accept("Item             Price          Qty          Amount");
                addLeftLabel.accept("----------------------------------------------------");

                // Display item
                double grandTotal = 0;
                for (OrderedItem item : orderedItems) {
                    double price1 = item.price / item.quantity;
                    double amount = price1 * item.quantity;
                    String line = String.format("%-18s %-10s %6s %13s",
                            item.name,
                            String.format("₹%.2f", price1),
                            item.quantity,
                            String.format("₹%.2f", amount));

                    addLeftLabel.accept(line);
                    grandTotal += amount;
                }

                addLeftLabel.accept("----------------------------------------------------");

                // Total
                String totalLine = String.format("%40s ₹%.2f", "Total:", grandTotal);
                addLeftLabel.accept(totalLine);
                addLeftLabel.accept("----------------------------------------------------");
                addLeftLabel.accept("             THANKS FOR YOUR KIND VISIT");

                subpanel3.revalidate();
                subpanel3.repaint();
            }
        });

        JButton cancelbutton = new JButton("Cancel");
        cancelbutton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelbutton.setBounds(260, 550, 100, 40);
        panel2.add(cancelbutton);

        cancelbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!addedRows.isEmpty() && !orderedItems.isEmpty()) {
                    JLabel[] lastRow = addedRows.remove(addedRows.size() - 1);
                    for (JLabel label : lastRow) {
                        subpanel2.remove(label);
                    }
                    orderedItems.remove(orderedItems.size() - 1); // Remove corresponding item from order list
                    subpanel2.revalidate();
                    subpanel2.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "No more entries to cancel.");
                }
            }
        });

        JButton printbutton = new JButton("Print");
        printbutton.setFont(new Font("Arial", Font.BOLD, 16));
        printbutton.setBounds(100, 550, 100, 40);
        panel3.add(printbutton);

        printbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String mobile = numberField.getText().trim();

                if (name.isEmpty() || mobile.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both name and mobile number.");
                    return;
                }

                // Get current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                String currentDate = now.format(dateFormatter);
                String currentTime = now.format(timeFormatter);

                // Prepare bill data
                double grandTotal = 0;
                List<Document> items = new ArrayList<>();
                for (OrderedItem item : orderedItems) {
                    double unitPrice = item.price / item.quantity;
                    double total = unitPrice * item.quantity;
                    grandTotal += total;

                    Document itemDoc = new Document("name", item.name)
                            .append("price", unitPrice)
                            .append("quantity", item.quantity)
                            .append("total", total);
                    items.add(itemDoc);
                }

                // Final bill document
                Document billDoc = new Document("name", name)
                        .append("mobile", mobile)
                        .append("date", currentDate)
                        .append("time", currentTime)
                        .append("items", items)
                        .append("grandTotal", grandTotal);

                try {
                    MongoClient mongoClient = MongoClients.create();
                    MongoDatabase db = mongoClient.getDatabase("BillingSystem");
                    MongoCollection<Document> bills = db.getCollection("Bills");
                    bills.insertOne(billDoc);
                    JOptionPane.showMessageDialog(frame, "Bill saved successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error saving bill: " + ex.getMessage());
                }
            }
        });

        JButton clearbutton = new JButton("Clear");
        clearbutton.setFont(new Font("Arial", Font.BOLD, 16));
        clearbutton.setBounds(260, 550, 100, 40);
        panel3.add(clearbutton);

        clearbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subpanel3.removeAll();
                subpanel3.revalidate();
                subpanel3.repaint();

                orderedItems.clear(); // Clear the local list so bill doesn't regenerate

                String name = nameField.getText().trim();
                String mobile = numberField.getText().trim();

                if (!name.isEmpty() && !mobile.isEmpty()) {
                    try {
                        MongoClient mongoClient = MongoClients.create();
                        MongoDatabase db = mongoClient.getDatabase("BillingSystem");
                        MongoCollection<Document> bills = db.getCollection("Bills");

                        // Remove all matching bills (you can change to .deleteOne() if only the latest
                        // should be deleted)
                        Document query = new Document("name", name).append("mobile", mobile);
                        bills.deleteMany(query);

                        JOptionPane.showMessageDialog(frame, "Bill cleared successfully.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error clearing bill from database.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter name and mobile to clear the bill.");
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(669, 765, 190, 50);
        frame.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Main();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
