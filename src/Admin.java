import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Admin {
    public Admin() {
        JFrame frame = new JFrame("Food Details");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Admin Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(630, 15, 350, 40);
        frame.add(titleLabel);

        // Add Food Section
        JButton addbutton = new JButton("Add Food");
        addbutton.setFont(new Font("Arial", Font.BOLD, 20));
        addbutton.setBounds(500, 200, 190, 50);
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
        updatebutton.setBounds(500, 300, 190, 50);
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
        deletebutton.setBounds(500, 400, 190, 50);
        frame.add(deletebutton);

        deletebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Adminchange();
                frame.dispose();
            }
        });

        // Main Food Section
        JButton mainbutton = new JButton("Food Details");
        mainbutton.setFont(new Font("Arial", Font.BOLD, 20));
        mainbutton.setBounds(500, 500, 190, 50);
        frame.add(mainbutton);

        mainbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Adminmain();
                frame.dispose();
            }
        });

        // Employee Food Section
        JButton employeebutton = new JButton("Employee Details");
        employeebutton.setFont(new Font("Arial", Font.BOLD, 18));
        employeebutton.setBounds(900, 200, 190, 50);
        frame.add(employeebutton);

        employeebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Employee();
                frame.dispose();
            }
        });

        // Total Food Order Section
        JButton totalbutton = new JButton("Total Orders");
        totalbutton.setFont(new Font("Arial", Font.BOLD, 20));
        totalbutton.setBounds(900, 300, 190, 50);
        frame.add(totalbutton);

        totalbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AdminTotal();
                frame.dispose();
            }
        });

        // Logout Food Section
        JButton logoutbutton = new JButton("Logout");
        logoutbutton.setFont(new Font("Arial", Font.BOLD, 20));
        logoutbutton.setBounds(900, 400, 190, 50);
        frame.add(logoutbutton);

        logoutbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    new Login();
                    frame.dispose();
                    JOptionPane.showMessageDialog(frame, "Logout Successfully!");
                }
            }
        });

        // Exit Food Section
        JButton exitbutton = new JButton("Exit");
        exitbutton.setFont(new Font("Arial", Font.BOLD, 20));
        exitbutton.setBounds(900, 500, 190, 50);
        frame.add(exitbutton);

        exitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.setVisible(true);

    }
}
