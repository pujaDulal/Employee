package employee;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Add Employee Dialog
 * Dialog for adding new employees to the system
 */
public class AddEmployeeDialog extends JDialog {
    
    private LinkedList<Employee> employees;
    private boolean employeeAdded = false;
    
    private JComboBox<String> typeComboBox;
    private JTextField idField;
    private JTextField nameField;
    private JTextField departmentField;
    private JTextField salaryField;
    private JSpinner ratingSpinner;
    
    public AddEmployeeDialog(JFrame parent, LinkedList<Employee> employees) {
        super(parent, "Add New Employee", true);
        this.employees = employees;
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
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Employee Type
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Employee Type:"), gbc);
        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"Manager", "Intern", "Regular"});
        mainPanel.add(typeComboBox, gbc);
        
        // Employee ID
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(25);
        idField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(idField, gbc);
        
        // Employee Name
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Employee Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(25);
        nameField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(nameField, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(25);
        departmentField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(departmentField, gbc);
        
        // Salary
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Base Salary:"), gbc);
        gbc.gridx = 1;
        salaryField = new JTextField(25);
        salaryField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(salaryField, gbc);
        
        // Performance Rating
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Performance Rating:"), gbc);
        gbc.gridx = 1;
        ratingSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        mainPanel.add(ratingSpinner, gbc);
        
        // Info label
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><small>Note: Managers get 10% bonus, Interns get 50% bonus, Regular employees get no bonus</small></html>");
        infoLabel.setForeground(Color.BLUE);
        mainPanel.add(infoLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Employee");
        JButton cancelButton = new JButton("Cancel");
        
        addButton.addActionListener(e -> addEmployee());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set default button
        getRootPane().setDefaultButton(addButton);
    }
    
    /**
     * Add employee to the system
     */
    private void addEmployee() {
        try {
            // Validate input
            if (!validateInput()) {
                return;
            }
            
            String type = (String) typeComboBox.getSelectedItem();
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String department = departmentField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());
            int rating = (Integer) ratingSpinner.getValue();
            
            // Check if ID already exists
            for (Employee emp : employees) {
                if (emp.getId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(this, "Employee with this ID already exists!", 
                                                "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Create employee based on type
            Employee emp = switch (type) {
                case "Manager" -> new Manager(id, name, department, salary, rating);
                case "Intern" -> new Intern(id, name, department, salary, rating);
                case "Regular" -> new Regular(id, name, department, salary, rating);
                default -> null;
            };
            
            if (emp != null) {
                employees.add(emp);
                employeeAdded = true;
                JOptionPane.showMessageDialog(this, "Employee added successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a valid number.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee ID is required!", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            idField.requestFocus();
            return false;
        }
        
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
     * Check if employee was added
     */
    public boolean isEmployeeAdded() {
        return employeeAdded;
    }
}
