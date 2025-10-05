package employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;

/**
 * Search Employee Dialog
 * Dialog for searching and viewing employee details
 */
public class SearchEmployeeDialog extends JDialog {
    
    private LinkedList<Employee> employees;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;
    
    public SearchEmployeeDialog(JFrame parent, LinkedList<Employee> employees) {
        super(parent, "Search Employees", true);
        this.employees = employees;
        initializeDialog();
    }
    
    /**
     * Initialize the dialog components
     */
    private void initializeDialog() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        
        JLabel searchLabel = new JLabel("Search by:");
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"ID", "Name", "Rating"});
        JTextField searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("🔍 Search");
        JButton showAllButton = new JButton("📋 Show All");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Create results table
        String[] columnNames = {"S.N.", "Type", "ID", "Name", "Department", "Base Salary", "Rating", "Total Salary"};
        searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        searchTable = new JTable(searchTableModel);
        searchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchTable.setRowHeight(25);
        
        // Set column widths
        searchTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // S.N.
        searchTable.getColumnModel().getColumn(1).setPreferredWidth(80);   // Type
        searchTable.getColumnModel().getColumn(2).setPreferredWidth(60);   // ID
        searchTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // Name
        searchTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Department
        searchTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Base Salary
        searchTable.getColumnModel().getColumn(6).setPreferredWidth(60);    // Rating
        searchTable.getColumnModel().getColumn(7).setPreferredWidth(100);   // Total Salary
        
        JScrollPane scrollPane = new JScrollPane(searchTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton viewDetailsButton = new JButton("👁️ View Details");
        JButton saveResultsButton = new JButton("💾 Save Results");
        JButton closeButton = new JButton("Close");
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(saveResultsButton);
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        searchButton.addActionListener(e -> performSearch(searchTypeCombo, searchField));
        showAllButton.addActionListener(e -> showAllEmployees());
        viewDetailsButton.addActionListener(e -> viewEmployeeDetails());
        saveResultsButton.addActionListener(e -> saveSearchResults());
        closeButton.addActionListener(e -> dispose());
        
        // Set default button
        getRootPane().setDefaultButton(searchButton);
        
        // Load all employees initially
        showAllEmployees();
    }
    
    /**
     * Perform search based on selected criteria
     */
    private void performSearch(JComboBox<String> searchTypeCombo, JTextField searchField) {
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", 
                                        "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LinkedList<Employee> results = new LinkedList<>();
        
        switch (searchType) {
            case "ID":
                for (Employee emp : employees) {
                    if (emp.getId().toLowerCase().contains(searchTerm.toLowerCase())) {
                        results.add(emp);
                    }
                }
                break;
                
            case "Name":
                for (Employee emp : employees) {
                    if (emp.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        results.add(emp);
                    }
                }
                break;
                
            case "Rating":
                try {
                    int minRating = Integer.parseInt(searchTerm);
                    for (Employee emp : employees) {
                        if (emp.getPerformanceRating() >= minRating) {
                            results.add(emp);
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid rating number (1-5).", 
                                                "Invalid Rating", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
        }
        
        displaySearchResults(results);
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees found matching the search criteria.", 
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Show all employees
     */
    private void showAllEmployees() {
        displaySearchResults(employees);
    }
    
    /**
     * Display search results in the table
     */
    private void displaySearchResults(LinkedList<Employee> results) {
        searchTableModel.setRowCount(0);
        int serialNumber = 1;
        for (Employee emp : results) {
            Object[] row = {
                serialNumber++,                    // S.N.
                emp.getClass().getSimpleName(),   // Type
                emp.getId(),                       // ID
                emp.getName(),                     // Name
                emp.getDepartment(),              // Department
                String.format("$%.2f", emp.getSalary()), // Base Salary
                emp.getPerformanceRating(),        // Rating
                String.format("$%.2f", emp.calculateSalary()) // Total Salary
            };
            searchTableModel.addRow(row);
        }
    }
    
    /**
     * View detailed information about selected employee
     */
    private void viewEmployeeDetails() {
        int selectedRow = searchTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to view details.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empId = (String) searchTableModel.getValueAt(selectedRow, 2); // ID is now column 2
        Employee emp = findEmployeeById(empId);
        
        if (emp != null) {
            showEmployeeDetails(emp);
        }
    }
    
    /**
     * Show detailed employee information
     */
    private void showEmployeeDetails(Employee emp) {
        double totalSalary = emp.calculateSalary();
        double bonus = totalSalary - emp.getSalary();
        
        String details = String.format(
            "<html><h3>Employee Details</h3>" +
            "<table border='0' cellpadding='5'>" +
            "<tr><td><b>Type:</b></td><td>%s</td></tr>" +
            "<tr><td><b>ID:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Name:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Department:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Base Salary:</b></td><td>$%.2f</td></tr>" +
            "<tr><td><b>Performance Rating:</b></td><td>%d/5</td></tr>" +
            "<tr><td><b>Automatic Bonus:</b></td><td>$%.2f</td></tr>" +
            "<tr><td><b>Total Salary:</b></td><td>$%.2f</td></tr>" +
            "</table></html>",
            emp.getClass().getSimpleName(),
            emp.getId(),
            emp.getName(),
            emp.getDepartment(),
            emp.getSalary(),
            emp.getPerformanceRating(),
            bonus,
            totalSalary
        );
        
        JOptionPane.showMessageDialog(this, details, "Employee Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Save search results to file
     */
    private void saveSearchResults() {
        LinkedList<Employee> currentResults = new LinkedList<>();
        
        // Get current displayed results
        for (int i = 0; i < searchTableModel.getRowCount(); i++) {
            String empId = (String) searchTableModel.getValueAt(i, 2); // ID is now column 2
            Employee emp = findEmployeeById(empId);
            if (emp != null) {
                currentResults.add(emp);
            }
        }
        
        if (currentResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results to save.", 
                                        "Empty Results", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv", "txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            FileHandler.saveQueryResults(currentResults, filename);
            JOptionPane.showMessageDialog(this, "Search results saved successfully!", 
                                        "Save Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Find employee by ID
     */
    private Employee findEmployeeById(String id) {
        for (Employee emp : employees) {
            if (emp.getId().equalsIgnoreCase(id)) {
                return emp;
            }
        }
        return null;
    }
}
