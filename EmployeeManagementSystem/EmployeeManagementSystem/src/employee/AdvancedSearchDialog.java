package employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advanced Search Dialog
 * Provides comprehensive search capabilities using various algorithms
 */
public class AdvancedSearchDialog extends JDialog {
    
    private final LinkedList<Employee> employees;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;
    private JLabel statusLabel;
    private JComboBox<String> algorithmCombo;
    private JComboBox<SortingSearchingAlgorithms.SortCriteria> sortCriteriaCombo;
    private JComboBox<SortingSearchingAlgorithms.SortOrder> sortOrderCombo;
    private JComboBox<String> sortAlgorithmCombo;
    
    public AdvancedSearchDialog(JFrame parent, LinkedList<Employee> employees) {
        super(parent, "Advanced Search & Sort", true);
        this.employees = employees;
        initializeDialog();
    }
    
    /**
     * Initialize the dialog components
     */
    private void initializeDialog() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        createSearchPanel();
        createSortPanel();
        createResultsTable();
        createButtonPanel();
        createStatusBar();
        
        // Load all employees initially
        displayResults(employees);
    }
    
    /**
     * Create search panel with advanced options
     */
    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Advanced Search"));
        
        // Search criteria selection
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        searchPanel.add(new JLabel("Search Criteria:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<SortingSearchingAlgorithms.SortCriteria> searchCriteriaCombo = 
            new JComboBox<>(SortingSearchingAlgorithms.SortCriteria.values());
        searchPanel.add(searchCriteriaCombo, gbc);
        
        // Search term input
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Search Term:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField, gbc);
        
        // Algorithm selection
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Search Algorithm:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        algorithmCombo = new JComboBox<>(new String[]{"Linear Search", "Binary Search", "Advanced Search", "Fuzzy Search", "Hybrid Search"});
        searchPanel.add(algorithmCombo, gbc);
        
        // Fuzzy search distance (only for fuzzy search)
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        JLabel distanceLabel = new JLabel("Max Distance:");
        searchPanel.add(distanceLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JSpinner distanceSpinner = new JSpinner(new SpinnerNumberModel(2, 0, 10, 1));
        searchPanel.add(distanceSpinner, gbc);
        
        // Search buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton searchButton = new JButton("🔍 Search");
        JButton clearButton = new JButton("🔄 Clear");
        JButton showAllButton = new JButton("📋 Show All");
        
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(showAllButton);
        searchPanel.add(buttonPanel, gbc);
        
        // Add action listeners
        searchButton.addActionListener(e -> performAdvancedSearch(searchCriteriaCombo, searchField, distanceSpinner));
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchCriteriaCombo.setSelectedIndex(0);
            algorithmCombo.setSelectedIndex(0);
            displayResults(employees);
            updateStatus("Search cleared. Showing all employees.");
        });
        showAllButton.addActionListener(e -> {
            displayResults(employees);
            updateStatus("Showing all employees.");
        });
        
        add(searchPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create sorting panel
     */
    private void createSortPanel() {
        JPanel sortPanel = new JPanel(new FlowLayout());
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sorting Options"));
        
        sortPanel.add(new JLabel("Sort by:"));
        sortCriteriaCombo = new JComboBox<>(SortingSearchingAlgorithms.SortCriteria.values());
        sortPanel.add(sortCriteriaCombo);
        
        sortPanel.add(new JLabel("Order:"));
        sortOrderCombo = new JComboBox<>(SortingSearchingAlgorithms.SortOrder.values());
        sortPanel.add(sortOrderCombo);
        
        sortPanel.add(new JLabel("Algorithm:"));
        sortAlgorithmCombo = new JComboBox<>(new String[]{"Quick Sort", "Merge Sort", "Heap Sort", "Insertion Sort"});
        sortPanel.add(sortAlgorithmCombo);
        
        JButton sortButton = new JButton("🔄 Sort");
        JButton sortMetricsButton = new JButton("📊 Sort with Metrics");
        
        sortButton.addActionListener(e -> performSorting());
        sortMetricsButton.addActionListener(e -> performSortingWithMetrics());
        
        sortPanel.add(sortButton);
        sortPanel.add(sortMetricsButton);
        
        add(sortPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create results table
     */
    private void createResultsTable() {
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
    }
    
    /**
     * Create button panel
     */
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton viewDetailsButton = new JButton("👁️ View Details");
        JButton saveResultsButton = new JButton("💾 Save Results");
        JButton rangeSearchButton = new JButton("📊 Range Search");
        JButton closeButton = new JButton("Close");
        
        viewDetailsButton.addActionListener(e -> viewEmployeeDetails());
        saveResultsButton.addActionListener(e -> saveSearchResults());
        rangeSearchButton.addActionListener(e -> showRangeSearchDialog());
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(saveResultsButton);
        buttonPanel.add(rangeSearchButton);
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create status bar
     */
    private void createStatusBar() {
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Perform advanced search based on selected algorithm
     */
    private void performAdvancedSearch(JComboBox<SortingSearchingAlgorithms.SortCriteria> criteriaCombo, 
                                     JTextField searchField, JSpinner distanceSpinner) {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", 
                                        "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SortingSearchingAlgorithms.SortCriteria criteria = 
            (SortingSearchingAlgorithms.SortCriteria) criteriaCombo.getSelectedItem();
        String algorithm = (String) algorithmCombo.getSelectedItem();
        
        List<Employee> results = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        
        switch (algorithm) {
            case "Linear Search":
                results = SortingSearchingAlgorithms.linearSearch(employees, searchTerm, criteria);
                break;
            case "Binary Search":
                // First sort the list for binary search
                List<Employee> sortedEmployees = new ArrayList<>(employees);
                SortingSearchingAlgorithms.quickSort(sortedEmployees, criteria, SortingSearchingAlgorithms.SortOrder.ASCENDING);
                
                Employee found = SortingSearchingAlgorithms.binarySearch(sortedEmployees, searchTerm, criteria);
                if (found != null) {
                    results.add(found);
                }
                break;
            case "Advanced Search":
                Map<SortingSearchingAlgorithms.SortCriteria, String> searchCriteria = new HashMap<>();
                searchCriteria.put(criteria, searchTerm);
                results = SortingSearchingAlgorithms.advancedSearch(employees, searchCriteria);
                break;
            case "Fuzzy Search":
                int maxDistance = (Integer) distanceSpinner.getValue();
                results = SortingSearchingAlgorithms.fuzzySearch(employees, searchTerm, maxDistance);
                break;
            case "Hybrid Search":
                results = SortingSearchingAlgorithms.hybridSearch(employees, searchTerm, criteria);
                break;
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        displayResults(results);
        updateStatus(String.format("Search completed in %dms. Found %d result(s) using %s.", 
                                 executionTime, results.size(), algorithm));
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees found matching the search criteria.", 
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Perform sorting
     */
    private void performSorting() {
        SortingSearchingAlgorithms.SortCriteria criteria = 
            (SortingSearchingAlgorithms.SortCriteria) sortCriteriaCombo.getSelectedItem();
        SortingSearchingAlgorithms.SortOrder order = 
            (SortingSearchingAlgorithms.SortOrder) sortOrderCombo.getSelectedItem();
        String algorithm = (String) sortAlgorithmCombo.getSelectedItem();
        
        List<Employee> sortedEmployees = new ArrayList<>(employees);
        
        switch (algorithm) {
            case "Quick Sort":
                SortingSearchingAlgorithms.quickSort(sortedEmployees, criteria, order);
                break;
            case "Merge Sort":
                SortingSearchingAlgorithms.mergeSort(sortedEmployees, criteria, order);
                break;
            case "Heap Sort":
                SortingSearchingAlgorithms.heapSort(sortedEmployees, criteria, order);
                break;
            case "Insertion Sort":
                SortingSearchingAlgorithms.insertionSort(sortedEmployees, criteria, order);
                break;
        }
        
        displayResults(sortedEmployees);
        updateStatus(String.format("Sorted by %s (%s) using %s", criteria, order, algorithm));
    }
    
    /**
     * Perform sorting with performance metrics
     */
    private void performSortingWithMetrics() {
        SortingSearchingAlgorithms.SortCriteria criteria = 
            (SortingSearchingAlgorithms.SortCriteria) sortCriteriaCombo.getSelectedItem();
        SortingSearchingAlgorithms.SortOrder order = 
            (SortingSearchingAlgorithms.SortOrder) sortOrderCombo.getSelectedItem();
        String algorithm = (String) sortAlgorithmCombo.getSelectedItem();
        
        List<Employee> sortedEmployees = new ArrayList<>(employees);
        SortingSearchingAlgorithms.SortingMetrics metrics = 
            SortingSearchingAlgorithms.sortWithMetrics(sortedEmployees, criteria, order, algorithm);
        
        displayResults(sortedEmployees);
        updateStatus(String.format("Sorted by %s (%s) using %s", criteria, order, algorithm));
        
        // Show metrics dialog
        JOptionPane.showMessageDialog(this, 
            String.format("Sorting Performance Metrics:\n\n%s\n\nSorted %d employees", 
                         metrics.toString(), employees.size()),
            "Sorting Metrics", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show range search dialog
     */
    private void showRangeSearchDialog() {
        JDialog rangeDialog = new JDialog(this, "Range Search", true);
        rangeDialog.setSize(400, 200);
        rangeDialog.setLocationRelativeTo(this);
        rangeDialog.setLayout(new GridLayout(4, 2, 10, 10));
        
        rangeDialog.add(new JLabel("Search Criteria:"));
        JComboBox<SortingSearchingAlgorithms.SortCriteria> rangeCriteriaCombo = 
            new JComboBox<>(new SortingSearchingAlgorithms.SortCriteria[]{
                SortingSearchingAlgorithms.SortCriteria.SALARY,
                SortingSearchingAlgorithms.SortCriteria.PERFORMANCE_RATING,
                SortingSearchingAlgorithms.SortCriteria.TOTAL_SALARY
            });
        rangeDialog.add(rangeCriteriaCombo);
        
        rangeDialog.add(new JLabel("Minimum Value:"));
        JTextField minField = new JTextField();
        rangeDialog.add(minField);
        
        rangeDialog.add(new JLabel("Maximum Value:"));
        JTextField maxField = new JTextField();
        rangeDialog.add(maxField);
        
        JButton searchButton = new JButton("Search");
        JButton cancelButton = new JButton("Cancel");
        
        searchButton.addActionListener(e -> {
            try {
                double minValue = Double.parseDouble(minField.getText());
                double maxValue = Double.parseDouble(maxField.getText());
                
                SortingSearchingAlgorithms.SortCriteria criteria = 
                    (SortingSearchingAlgorithms.SortCriteria) rangeCriteriaCombo.getSelectedItem();
                
                List<Employee> results = SortingSearchingAlgorithms.rangeSearch(employees, criteria, minValue, maxValue);
                displayResults(results);
                updateStatus(String.format("Range search found %d employee(s) between %.2f and %.2f", 
                                         results.size(), minValue, maxValue));
                rangeDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(rangeDialog, "Please enter valid numeric values.", 
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> rangeDialog.dispose());
        
        rangeDialog.add(searchButton);
        rangeDialog.add(cancelButton);
        
        rangeDialog.setVisible(true);
    }
    
    /**
     * Display search results in the table
     */
    private void displayResults(List<Employee> results) {
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
        
        String empId = (String) searchTableModel.getValueAt(selectedRow, 2);
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
        List<Employee> currentResults = new ArrayList<>();
        
        // Get current displayed results
        for (int i = 0; i < searchTableModel.getRowCount(); i++) {
            String empId = (String) searchTableModel.getValueAt(i, 2);
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
            LinkedList<Employee> resultsLinkedList = new LinkedList<>(currentResults);
            FileHandler.saveQueryResults(resultsLinkedList, filename);
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
    
    /**
     * Update status message
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
