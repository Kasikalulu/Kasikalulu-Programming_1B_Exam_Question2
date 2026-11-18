/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends JFrame {

   
    // Q.2.5 THE INTERFACE & CLASS
    
    public interface IProductCalculations {
        double GetVat();
        double GetCommission();
        double GetTotal();
    }

    public static class ProductCalculations implements IProductCalculations {
        private double price;

        public ProductCalculations(double price) {
            this.price = price;
        }

        @Override
        public double GetVat() {
            return price * 0.15; // 15% VAT
        }

        @Override
        public double GetCommission() {
            return price * 0.05; // 5% Commission
        }

        @Override
        public double GetTotal() {
            return price + GetVat() + GetCommission();
        }
    }

    // ==========================================
    // GUI COMPONENTS (Q.2.2)
    // ==========================================
    private JTextField txtCustomerName;
    private JComboBox<String> cmbProduct;
    private JTextField txtPrice;
    private JButton btnProcess;

    public Main() {
        // Setup the main window
        setTitle("Product App");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout());

        // Setup Menu Bar (Q.2.4)
        setupMenuBar();

        // Setup Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Labels and Text Fields
        formPanel.add(new JLabel("Customer Name:"));
        txtCustomerName = new JTextField();
        formPanel.add(txtCustomerName);

        formPanel.add(new JLabel("Select Product:"));
        String[] products = {"Cozy Nights Monarch", "Restonic Barbados", "Sealy Phuket"};
        cmbProduct = new JComboBox<>(products);
        formPanel.add(cmbProduct);

        formPanel.add(new JLabel("Enter Price:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        // Process Button
        btnProcess = new JButton("PROCESS");
        formPanel.add(new JLabel("")); // Empty space for alignment
        formPanel.add(btnProcess);

        add(formPanel, BorderLayout.CENTER);

        // Action Listener for the Process Button
        btnProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSale();
            }
        });
    }


    // MENU SETUP (Q.2.4)
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0)); // Closes application
        fileMenu.add(exitItem);

        // Tools Menu
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        JMenuItem clearItem = new JMenuItem("Clear");

        processItem.addActionListener(e -> processSale()); // Calls same logic as button

        clearItem.addActionListener(e -> {
            txtCustomerName.setText("");
            cmbProduct.setSelectedIndex(0);
            txtPrice.setText("");
        });

        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    
    // PROCESS LOGIC (Q.2.1, Q.2.3)
    
    private void processSale() {
        String customerName = txtCustomerName.getText().trim();
        String productName = cmbProduct.getSelectedItem().toString();
        String priceStr = txtPrice.getText().trim();

        // Basic validation
        if (customerName.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            ProductCalculations calc = new ProductCalculations(price);

            double vat = calc.GetVat();
            double commission = calc.GetCommission();
            double total = calc.GetTotal();

            // 1. Show Message Dialog (Screenshot 2)
            String message = String.format(
                "Customer Name: %s\n" +
                "Product: %s\n" +
                "VAT: R %.1f\n" +
                "COMMISSION: R %.1f\n" +
                "TOTAL: R %.1f",
                customerName, productName, vat, commission, total
            );
            JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);

            // 2. Save to data.txt (Screenshot 2)
            saveToFile(customerName, productName, vat, commission, total);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to save data
    private void saveToFile(String name, String product, double vat, double commission, double total) {
        // Using append mode (true) so it doesn't overwrite previous data
        try (PrintWriter out = new PrintWriter(new FileWriter("data.txt", true))) {
            out.println("DATA LOG");
            out.println("******************************");
            out.println("Customer Name: " + name);
            out.println("Product: " + product);
            out.printf("VAT: R %.1f\n", vat);
            out.printf("COMMISSION: R %.1f\n", commission);
            out.printf("TOTAL: R %.1f\n", total);
            out.println("******************************");
            out.println(); // Empty line for spacing between logs
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // MAIN ENTRY POINT
    // ==========================================
    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (Best Practice for Swing)
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}

   
