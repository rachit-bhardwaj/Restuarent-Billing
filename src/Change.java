import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.DeleteResult;

public class Change {
    public Change() {
        // Mongo Setup
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("FoodDB");
        MongoCollection<Document> collection = database.getCollection("Foods");

        // Frame
        JFrame frame = new JFrame("Food Details");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Add / Get / Update / Delete");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(530, 50, 600, 40);
        frame.add(titleLabel);

        // Fields
        JLabel foodidLabel = new JLabel("Food ID :");
        foodidLabel.setFont(new Font("Arial", Font.BOLD, 16));
        foodidLabel.setBounds(520, 200, 120, 30);
        frame.add(foodidLabel);

        JTextField foodidttext = new JTextField();
        foodidttext.setFont(new Font("Arial", Font.PLAIN, 16));
        foodidttext.setBounds(750, 200, 250, 30);
        frame.add(foodidttext);

        JLabel foodNameLabel = new JLabel("Food Name :");
        foodNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        foodNameLabel.setBounds(520, 250, 120, 30);
        frame.add(foodNameLabel);

        JTextField foodNameText = new JTextField();
        foodNameText.setFont(new Font("Arial", Font.PLAIN, 16));
        foodNameText.setBounds(750, 250, 250, 30);
        frame.add(foodNameText);

        JLabel foodCategoryLabel = new JLabel("Food Category :");
        foodCategoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        foodCategoryLabel.setBounds(520, 300, 160, 30);
        frame.add(foodCategoryLabel);

        String[] categories = { "", "Coffee", "Tea", "Soft Drink", "Shake", "Snack", "Indian Chaat", "Meal", "Others" };
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 16));
        categoryBox.setBounds(750, 300, 250, 30);
        frame.add(categoryBox);

        JLabel foodPriceLabel = new JLabel("Food Price :");
        foodPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        foodPriceLabel.setBounds(520, 350, 120, 30);
        frame.add(foodPriceLabel);

        JTextField foodPriceField = new JTextField();
        foodPriceField.setFont(new Font("Arial", Font.PLAIN, 16));
        foodPriceField.setBounds(750, 350, 250, 30);
        frame.add(foodPriceField);

        // Add Food
        JButton addButton = new JButton("Add Food");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setBounds(270, 470, 190, 50);
        frame.add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String foodID = foodidttext.getText().trim();
                String foodName = foodNameText.getText().trim();
                String category = (String) categoryBox.getSelectedItem();
                String price = foodPriceField.getText().trim();

                if (foodID.isEmpty() || foodName.isEmpty() || category.isEmpty() || price.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int fid = Integer.parseInt(foodID);
                    double foodPrice = Double.parseDouble(price);

                    Document existing = collection.find(Filters.eq("foodid", fid)).first();
                    if (existing != null) {
                        JOptionPane.showMessageDialog(frame, "Food ID already exists!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Document doc = new Document("foodid", fid)
                            .append("name", foodName)
                            .append("category", category)
                            .append("price", foodPrice);
                    collection.insertOne(doc);

                    JOptionPane.showMessageDialog(frame, "Food Added Successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Get Food
        JButton getButton = new JButton("Get Food");
        getButton.setFont(new Font("Arial", Font.BOLD, 20));
        getButton.setBounds(536, 470, 190, 50);
        frame.add(getButton);

        getButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String foodID = foodidttext.getText().trim();
                if (foodID.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter Food ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Document doc = collection.find(Filters.eq("foodid", Integer.parseInt(foodID))).first();
                if (doc != null) {
                    foodNameText.setText(doc.getString("name"));
                    categoryBox.setSelectedItem(doc.getString("category"));
                    foodPriceField.setText(String.valueOf(doc.getDouble("price")));
                } else {
                    JOptionPane.showMessageDialog(frame, "Food not found");
                }
            }
        });

        // Update Food
        JButton updateButton = new JButton("Update Food");
        updateButton.setFont(new Font("Arial", Font.BOLD, 20));
        updateButton.setBounds(802, 470, 190, 50);
        frame.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int fid = Integer.parseInt(foodidttext.getText().trim());
                    String name = foodNameText.getText().trim();
                    String category = (String) categoryBox.getSelectedItem();
                    double price = Double.parseDouble(foodPriceField.getText().trim());

                    UpdateResult result = collection.updateOne(Filters.eq("foodid", fid),
                            combine(set("name", name), set("category", category), set("price", price)));

                    if (result.getMatchedCount() > 0) {
                        JOptionPane.showMessageDialog(frame, "Updated Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Food ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete Food
        JButton deleteButton = new JButton("Delete Food");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 20));
        deleteButton.setBounds(1070, 470, 190, 50);
        frame.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int fid = Integer.parseInt(foodidttext.getText().trim());
                    DeleteResult result = collection.deleteOne(Filters.eq("foodid", fid));
                    if (result.getDeletedCount() > 0) {
                        foodNameText.setText("");
                        categoryBox.setSelectedIndex(0);
                        foodPriceField.setText("");
                        JOptionPane.showMessageDialog(frame, "Deleted Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Food ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Food ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(669, 570, 190, 50);
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
