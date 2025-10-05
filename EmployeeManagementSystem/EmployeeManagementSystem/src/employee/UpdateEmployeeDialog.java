package employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Update Employee Dialog
 * Dialog for updating existing employee information
 */
public class UpdateEmployeeDialog extends JDialog {
    
    private Employee employee;
    private boolean employeeUpdated = false;
    
    private JTextField nameField;
    private JTextField departmentField;
    private JTextField salaryField;
    private JSpinner ratingSpinner;
    
    public UpdateEmployeeDialog(JFrame parent, Employee employee) {
        super(parent, "Update Employee", true);
        this.employee = employee;
        initializeDialog();
    }
    
    /**
     * Initialize the dialog components
     */
    private void initializeDialog() {
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Employee Info (Read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(employee.getId());
        idField.setEditable(false);
        idField.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Employee Type:"), gbc);
        gbc.gridx = 1;
        JTextField typeField = new JTextField(employee.getClass().getSimpleName());
        typeField.setEditable(false);
        typeField.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(typeField, gbc);
        
        // Employee Name
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Employee Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(employee.getName(), 25);
        nameField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(nameField, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(employee.getDepartment(), 25);
        departmentField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(departmentField, gbc);
        
        // Salary
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Base Salary:"), gbc);
        gbc.gridx = 1;
        salaryField = new JTextField(String.valueOf(employee.getSalary()), 25);
        salaryField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(salaryField, gbc);
        
        // Performance Rating
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Performance Rating:"), gbc);
        gbc.gridx = 1;
        ratingSpinner = new JSpinner(new SpinnerNumberModel(employee.getPerformanceRating(), 1, 5, 1));
        mainPanel.add(ratingSpinner, gbc);
        
        // Current salary info
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        double currentTotalSalary = employee.calculateSalary();
        JLabel salaryInfoLabel = new JLabel("<html><small>Current Total Salary: $" + String.format("%.2f", currentTotalSalary) + 
                                          " (Base: $" + String.format("%.2f", employee.getSalary()) + 
                                          " + Bonus: $" + String.format("%.2f", currentTotalSalary - employee.getSalary()) + ")</small></html>");
        salaryInfoLabel.setForeground(Color.BLUE);
        mainPanel.add(salaryInfoLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("Update Employee");
        JButton cancelButton = new JButton("Cancel");
        
        updateButton.addActionListener(e -> updateEmployee());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set default button
        getRootPane().setDefaultButton(updateButton);
    }
    
    /**
     * Update employee information
     */
    private void updateEmployee() {
        try {
            // Validate input
            if (!validateInput()) {
                return;
            }
            
            String name = nameField.getText().trim();
            String department = departmentField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());
            int rating = (Integer) ratingSpinner.getValue();
            
            // Update employee information
            employee.setName(name);
            employee.setDepartment(department);
            employee.setSalary(salary);
            employee.setPerformanceRating(rating);
            
            employeeUpdated = true;
            JOptionPane.showMessageDialog(this, "Employee updated successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a valid number.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee name is required!", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        if (departmentField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Department is required!", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            departmentField.requestFocus();
            return false;
        }
        
        if (salaryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Salary is required!", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            salaryField.requestFocus();
            return false;
        }
        
        try {
            double salary = Double.parseDouble(salaryField.getText().trim());
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary cannot be negative!", 
                                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                salaryField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format!", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            salaryField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if employee was updated
     */
    public boolean isEmployeeUpdated() {
        return employeeUpdated;
    }
}
