import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import com.mongodb.client.*;
import org.bson.Document;

public class Adminmain {
    public Adminmain() {

        JFrame frame = new JFrame("Food Details");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Food Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(630, 15, 350, 40);
        frame.add(titleLabel);

        // Food Panel
        JPanel panel = new JPanel(null); // Still use null layout
        panel.setPreferredSize(new Dimension(1370, 2000)); // Large height for scrolling
        panel.setBackground(new Color(240, 240, 240)); // Match frame color

        JScrollPane scrollPane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(70, 180, 1390, 500); // Position scroll area in frame
        scrollPane.getViewport().setBackground(new Color(240, 240, 240)); // Match background
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scroll

        frame.add(scrollPane);

        // MongoDB connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("FoodDB");
        MongoCollection<Document> collection = database.getCollection("Foods");

        // Add Food Section
        JButton addbutton = new JButton("Add Food");
        addbutton.setFont(new Font("Arial", Font.BOLD, 20));
        addbutton.setBounds(70, 700, 190, 50);
        frame.add(addbutton);

        addbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Adminchange();
                frame.dispose();
            }
        });

        // Update Food Section
        JButton updatebutton = new JButton("Update Food");
        updatebutton.setFont(new Font("Arial", Font.BOLD, 20));
        updatebutton.setBounds(270, 700, 190, 50);
        frame.add(updatebutton);

        updatebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Adminchange();
                frame.dispose();
            }
        });

        // Delete Food Section
        JButton deletebutton = new JButton("Delete Food");
        deletebutton.setFont(new Font("Arial", Font.BOLD, 20));
        deletebutton.setBounds(470, 700, 190, 50);
        frame.add(deletebutton);

        deletebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Adminchange();
                frame.dispose();
            }
        });

        // Order Food Section
        JButton orderbutton = new JButton("Order Food");
        orderbutton.setFont(new Font("Arial", Font.BOLD, 20));
        orderbutton.setBounds(670, 700, 190, 50);
        frame.add(orderbutton);

        orderbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Order();
                frame.dispose();
            }
        });

        // Total Food Order Section
        JButton totalbutton = new JButton("Total Orders");
        totalbutton.setFont(new Font("Arial", Font.BOLD, 20));
        totalbutton.setBounds(870, 700, 190, 50);
        frame.add(totalbutton);

        totalbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AdminTotal();
                frame.dispose();
            }
        });

        // Exit Food Section
        JButton exitbutton = new JButton("Back");
        exitbutton.setFont(new Font("Arial", Font.BOLD, 20));
        exitbutton.setBounds(1070, 700, 380, 50);
        frame.add(exitbutton);

        exitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Admin();
                frame.dispose();
            }
        });

        Runnable clearPanel = () -> {
            Component[] components = panel.getComponents();
            for (int j = components.length - 1; j >= 0; j--) {
                if (components[j] instanceof JLabel && ((JLabel) components[j]).getY() >= 50) {
                    panel.remove(components[j]);
                }
            }
            panel.revalidate();
            panel.repaint();
        };

        // Search by Name
        JTextField searchnameText = new JTextField();
        searchnameText.setFont(new Font("Arial", Font.PLAIN, 16));
        searchnameText.setBounds(70, 100, 190, 50);
        frame.add(searchnameText);

        JButton searchnamebutton = new JButton("Search By Name");
        searchnamebutton.setFont(new Font("Arial", Font.BOLD, 16));
        searchnamebutton.setBounds(270, 100, 190, 50);
        frame.add(searchnamebutton);

        searchnamebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPanel.run(); // Clear previous search results

                String keyword = searchnameText.getText().trim().toLowerCase();

                int yPos = 50;
                int index = 1;

                for (Document doc : collection.find()) {
                    String name = doc.getString("name");

                    if (name != null && name.toLowerCase().contains(keyword)) {
                        addRow(panel, doc, index++, yPos);
                        yPos += 40;
                    }
                }
            }
        });

        searchnameText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchnamebutton.doClick(); // Simulates button click
                }
            }
        });

        // Search by Category
        String[] category = { "", "Coffee", "Tea", "Soft Drink", "Shake", "Snack", "Indian Chaat", "Meal", "Others" };
        JComboBox<String> categorybox = new JComboBox<>(category);
        categorybox.setFont(new Font("Arial", Font.PLAIN, 16));
        categorybox.setBounds(570, 100, 190, 50);
        frame.add(categorybox);

        JButton searchcategorybutton = new JButton("Search By Category");
        searchcategorybutton.setFont(new Font("Arial", Font.BOLD, 16));
        searchcategorybutton.setBounds(770, 100, 190, 50);
        frame.add(searchcategorybutton);

        searchcategorybutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPanel.run();
                String selectedCategory = categorybox.getSelectedItem().toString();

                int yPos = 50;
                int index = 1;

                for (Document doc : collection.find()) {
                    if (doc.getString("category").equalsIgnoreCase(selectedCategory)) {
                        addRow(panel, doc, index++, yPos);
                        yPos += 40;
                    }
                }
            }
        });

        // Search by Price
        JTextField searchpriceText = new JTextField();
        searchpriceText.setFont(new Font("Arial", Font.PLAIN, 16));
        searchpriceText.setBounds(1070, 100, 190, 50);
        frame.add(searchpriceText);

        JButton searchpricebutton = new JButton("Search By Price");
        searchpricebutton.setFont(new Font("Arial", Font.BOLD, 16));
        searchpricebutton.setBounds(1270, 100, 190, 50);
        frame.add(searchpricebutton);

        searchpricebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPanel.run();
                try {
                    double maxPrice = Double.parseDouble(searchpriceText.getText().trim());

                    int yPos = 50;
                    int index = 1;

                    for (Document doc : collection.find()) {
                        double price = Double.parseDouble(doc.get("price").toString());
                        if (price <= maxPrice) {
                            addRow(panel, doc, index++, yPos);
                            yPos += 40;
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Enter a valid number for price.");
                }
            }
        });

        searchpriceText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchpricebutton.doClick(); // Simulates button click
                }
            }
        });

        // Header labels
        String[] headers = { "S.No", "Food ID", "Food Name", "Category", "Price" };
        int[] xPositions = { 20, 150, 350, 800, 1250 };
        int[] widths = { 50, 150, 370, 370, 200 };
        Font headerFont = new Font("Arial", Font.BOLD, 20);

        for (int j = 0; j < headers.length; j++) {
            JLabel label = new JLabel(headers[j]);
            label.setFont(headerFont);
            label.setBounds(xPositions[j], 10, widths[j], 30);
            panel.add(label);
        }

        // Load food items
        int y = 50;
        int i = 1;
        Font rowFont = new Font("Arial", Font.PLAIN, 16);

        for (Document doc : collection.find()) {
            JLabel sno = new JLabel(String.valueOf(i));
            JLabel fid = new JLabel(String.valueOf(doc.get("foodid")));
            JLabel fname = new JLabel(doc.getString("name"));
            JLabel fcat = new JLabel(doc.getString("category"));
            JLabel fprice = new JLabel(String.valueOf(doc.get("price")));

            JLabel[] labels = { sno, fid, fname, fcat, fprice };

            for (int j = 0; j < labels.length; j++) {
                labels[j].setFont(rowFont);
                labels[j].setBounds(xPositions[j], y, widths[j], 30);
                panel.add(labels[j]);
            }

            y += 40;
            i++;
        }

        frame.setVisible(true);
    }

    private void addRow(JPanel panel, Document doc, int index, int y) {
        int[] xPositions = { 20, 150, 350, 800, 1250 };
        int[] widths = { 50, 150, 370, 370, 200 };
        Font rowFont = new Font("Arial", Font.PLAIN, 16);

        JLabel sno = new JLabel(String.valueOf(index));
        JLabel fid = new JLabel(String.valueOf(doc.get("foodid")));
        JLabel fname = new JLabel(doc.getString("name"));
        JLabel fcat = new JLabel(doc.getString("category"));
        JLabel fprice = new JLabel(String.valueOf(doc.get("price")));

        JLabel[] labels = { sno, fid, fname, fcat, fprice };

        for (int j = 0; j < labels.length; j++) {
            labels[j].setFont(rowFont);
            labels[j].setBounds(xPositions[j], y, widths[j], 30);
            panel.add(labels[j]);
        }

        panel.revalidate();
        panel.repaint();
    }
}
