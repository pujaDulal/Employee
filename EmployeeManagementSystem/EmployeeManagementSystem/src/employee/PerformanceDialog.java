package employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Performance Management Dialog
 * Dialog for managing employee performance and salary adjustments
 */
public class PerformanceDialog extends JDialog {
    
    private Employee employee;
    private boolean performanceUpdated = false;
    
    private JLabel employeeInfoLabel;
    private JLabel currentSalaryLabel;
    private JLabel bonusLabel;
    private JLabel totalSalaryLabel;
    private JLabel performanceLetterLabel;
    
    public PerformanceDialog(JFrame parent, Employee employee) {
        super(parent, "Performance Management", true);
        this.employee = employee;
        initializeDialog();
    }
    
    /**
     * Initialize the dialog components
     */
    private void initializeDialog() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Employee Information
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        employeeInfoLabel = new JLabel();
        employeeInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        mainPanel.add(employeeInfoLabel, gbc);
        
        // Current Salary Information
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        currentSalaryLabel = new JLabel();
        currentSalaryLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        mainPanel.add(currentSalaryLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        bonusLabel = new JLabel();
        bonusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        mainPanel.add(bonusLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        totalSalaryLabel = new JLabel();
        totalSalaryLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        totalSalaryLabel.setForeground(Color.BLUE);
        mainPanel.add(totalSalaryLabel, gbc);
        
        // Performance Letter
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        performanceLetterLabel = new JLabel();
        performanceLetterLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        mainPanel.add(performanceLetterLabel, gbc);
        
        // Action Panel
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel actionPanel = new JPanel(new FlowLayout());
        
        JButton applyBonusButton = new JButton("💰 Apply Automatic Bonus");
        JButton applyFineButton = new JButton("⚠️ Apply Fine");
        JButton updateRatingButton = new JButton("📊 Update Rating");
        
        actionPanel.add(applyBonusButton);
        actionPanel.add(applyFineButton);
        actionPanel.add(updateRatingButton);
        
        mainPanel.add(actionPanel, gbc);
        
        // Add action listeners
        applyBonusButton.addActionListener(e -> applyAutomaticBonus());
        applyFineButton.addActionListener(e -> applyFine());
        updateRatingButton.addActionListener(e -> updatePerformanceRating());
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize display
        updateDisplay();
    }
    
    /**
     * Update the display with current employee information
     */
    private void updateDisplay() {
        // Employee basic info
        employeeInfoLabel.setText("<html><b>Employee:</b> " + employee.getName() + 
                                 " (" + employee.getId() + ") - " + 
                                 employee.getClass().getSimpleName() + "</html>");
        
        // Salary calculations
        double baseSalary = employee.getSalary();
        double totalSalary = employee.calculateSalary();
        double automaticBonus = totalSalary - baseSalary;
        
        currentSalaryLabel.setText("<html><b>Base Salary:</b> $" + String.format("%.2f", baseSalary) + "</html>");
        bonusLabel.setText("<html><b>Automatic Bonus:</b> $" + String.format("%.2f", automaticBonus) + "</html>");
        totalSalaryLabel.setText("<html><b>Total Salary:</b> $" + String.format("%.2f", totalSalary) + "</html>");
        
        // Performance letter
        String performanceLetter = getPerformanceLetter();
        performanceLetterLabel.setText(performanceLetter);
    }
    
    /**
     * Get performance letter based on rating
     */
    private String getPerformanceLetter() {
        int rating = employee.getPerformanceRating();
        String letter;
        Color color;
        
        if (rating <= 2) {
            letter = "⚠️ Warning Letter issued to " + employee.getName();
            color = Color.RED;
        } else if (rating >= 4) {
            letter = "🎉 Appreciation Letter issued to " + employee.getName();
            color = Color.GREEN;
        } else {
            letter = employee.getName() + "'s performance is satisfactory.";
            color = Color.BLUE;
        }
        
        performanceLetterLabel.setForeground(color);
        return letter;
    }
    
    /**
     * Apply automatic bonus
     */
    private void applyAutomaticBonus() {
        double baseSalary = employee.getSalary();
        double totalSalary = employee.calculateSalary();
        double bonus = totalSalary - baseSalary;
        
        String message = String.format(
            "<html><h3>Automatic Bonus Applied</h3>" +
            "<p><b>Employee:</b> %s</p>" +
            "<p><b>Base Salary:</b> $%.2f</p>" +
            "<p><b>Automatic Bonus:</b> $%.2f</p>" +
            "<p><b>Total Salary:</b> $%.2f</p>" +
            "</html>",
            employee.getName(),
            baseSalary,
            bonus,
            totalSalary
        );
        
        JOptionPane.showMessageDialog(this, message, "Bonus Applied", JOptionPane.INFORMATION_MESSAGE);
        performanceUpdated = true;
    }
    
    /**
     * Apply fine to employee
     */
    private void applyFine() {
        String fineInput = JOptionPane.showInputDialog(this, 
            "Enter fine amount:", "Apply Fine", JOptionPane.QUESTION_MESSAGE);
        
        if (fineInput != null && !fineInput.trim().isEmpty()) {
            try {
                double fine = Double.parseDouble(fineInput.trim());
                
                if (fine < 0) {
                    JOptionPane.showMessageDialog(this, "Fine amount cannot be negative!", 
                                                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double baseSalary = employee.getSalary();
                double totalSalary = employee.calculateSalary();
                double salaryAfterFine = totalSalary - fine;
                
                String message = String.format(
                    "<html><h3>Fine Applied</h3>" +
                    "<p><b>Employee:</b> %s</p>" +
                    "<p><b>Base Salary:</b> $%.2f</p>" +
                    "<p><b>Automatic Bonus:</b> $%.2f</p>" +
                    "<p><b>Total Salary:</b> $%.2f</p>" +
                    "<p><b>Fine Applied:</b> $%.2f</p>" +
                    "<p><b>Final Salary:</b> $%.2f</p>" +
                    "</html>",
                    employee.getName(),
                    baseSalary,
                    totalSalary - baseSalary,
                    totalSalary,
                    fine,
                    salaryAfterFine
                );
                
                JOptionPane.showMessageDialog(this, message, "Fine Applied", JOptionPane.INFORMATION_MESSAGE);
                performanceUpdated = true;
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid fine amount. Please enter a valid number.", 
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Update performance rating
     */
    private void updatePerformanceRating() {
        String ratingInput = JOptionPane.showInputDialog(this, 
            "Enter new performance rating (1-5):", "Update Rating", JOptionPane.QUESTION_MESSAGE);
        
        if (ratingInput != null && !ratingInput.trim().isEmpty()) {
            try {
                int newRating = Integer.parseInt(ratingInput.trim());
                
                if (newRating < 1 || newRating > 5) {
                    JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5!", 
                                                "Invalid Rating", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int oldRating = employee.getPerformanceRating();
                employee.setPerformanceRating(newRating);
                
                String message = String.format(
                    "<html><h3>Performance Rating Updated</h3>" +
                    "<p><b>Employee:</b> %s</p>" +
                    "<p><b>Old Rating:</b> %d/5</p>" +
                    "<p><b>New Rating:</b> %d/5</p>" +
                    "</html>",
                    employee.getName(),
                    oldRating,
                    newRating
                );
                
                JOptionPane.showMessageDialog(this, message, "Rating Updated", JOptionPane.INFORMATION_MESSAGE);
                updateDisplay();
                performanceUpdated = true;
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number between 1 and 5.", 
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Check if performance was updated
     */
    public boolean isPerformanceUpdated() {
        return performanceUpdated;
    }
}
