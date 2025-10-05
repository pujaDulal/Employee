package employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

/**
 * Employee Management GUI
 * Provides a graphical interface for the Employee Management System
 */
public class EmployeeManagementGUI extends JFrame {
    
    private LinkedList<Employee> employees = new LinkedList<>();
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    
    public EmployeeManagementGUI() {
        initializeGUI();
        loadSampleData();
    }
    
    /**
     * Initialize the GUI components
     */
    private void initializeGUI() {
        setTitle("Employee Management System - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set look and feel - using default
        
        createMenuBar();
        createMainPanel();
        createStatusBar();
        
        setVisible(true);
    }
    
    /**
     * Create the menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load from File");
        JMenuItem saveItem = new JMenuItem("Save to File");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        loadItem.addActionListener(e -> loadFromFile());
        saveItem.addActionListener(e -> saveToFile());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Employee Menu
        JMenu employeeMenu = new JMenu("Employee");
        JMenuItem addItem = new JMenuItem("Add Employee");
        JMenuItem updateItem = new JMenuItem("Update Employee");
        JMenuItem deleteItem = new JMenuItem("Delete Employee");
        
        addItem.addActionListener(e -> showAddEmployeeDialog());
        updateItem.addActionListener(e -> showUpdateEmployeeDialog());
        deleteItem.addActionListener(e -> deleteSelectedEmployee());
        
        employeeMenu.add(addItem);
        employeeMenu.add(updateItem);
        employeeMenu.add(deleteItem);
        
        // Search & Sort Menu
        JMenu searchSortMenu = new JMenu("Search & Sort");
        JMenuItem advancedSearchItem = new JMenuItem("Advanced Search");
        JMenuItem quickSortItem = new JMenuItem("Quick Sort");
        JMenuItem mergeSortItem = new JMenuItem("Merge Sort");
        JMenuItem heapSortItem = new JMenuItem("Heap Sort");
        JMenuItem insertionSortItem = new JMenuItem("Insertion Sort");
        
        advancedSearchItem.addActionListener(e -> showAdvancedSearchDialog());
        quickSortItem.addActionListener(e -> performQuickSort());
        mergeSortItem.addActionListener(e -> performMergeSort());
        heapSortItem.addActionListener(e -> performHeapSort());
        insertionSortItem.addActionListener(e -> performInsertionSort());
        
        searchSortMenu.add(advancedSearchItem);
        searchSortMenu.addSeparator();
        searchSortMenu.add(quickSortItem);
        searchSortMenu.add(mergeSortItem);
        searchSortMenu.add(heapSortItem);
        searchSortMenu.add(insertionSortItem);
        
        // Performance Menu
        JMenu performanceMenu = new JMenu("Performance");
        JMenuItem manageItem = new JMenuItem("Manage Performance");
        manageItem.addActionListener(e -> showPerformanceDialog());
        performanceMenu.add(manageItem);
        
        menuBar.add(fileMenu);
        menuBar.add(employeeMenu);
        menuBar.add(searchSortMenu);
        menuBar.add(performanceMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Create the main panel with employee table
     */
    private void createMainPanel() {
        setLayout(new BorderLayout());
        
        // Create table
        String[] columnNames = {"S.N.", "Select", "Type", "ID", "Name", "Department", "Base Salary", "Rating", "Total Salary"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only checkbox column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) return Boolean.class; // Checkbox column
                return super.getColumnClass(column);
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        employeeTable.setRowHeight(25);
        
        // Set column widths
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // S.N.
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(60);     // Select
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // Type
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(60);     // ID
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(150);   // Name
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(100);   // Department
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(100);   // Base Salary
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(60);     // Rating
        employeeTable.getColumnModel().getColumn(8).setPreferredWidth(100);   // Total Salary
        
        // Create search panel above the table
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel searchLabel = new JLabel("Search:");
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"All", "ID", "Name", "Department", "Rating"});
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("🔍 Search");
        JButton clearButton = new JButton("🔄 Clear");
        
        searchField.setPreferredSize(new Dimension(200, 30));
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        // Add search functionality
        searchButton.addActionListener(e -> performSearch(searchTypeCombo, searchField));
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchTypeCombo.setSelectedIndex(0);
            refreshTable();
            updateStatus("Search cleared. Showing all employees.");
        });
        
        // Add real-time search as user types
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch(searchTypeCombo, searchField);
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch(searchTypeCombo, searchField);
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch(searchTypeCombo, searchField);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton addBtn = new JButton("➕ Add Employee");
        JButton updateBtn = new JButton("✏️ Update");
        JButton deleteBtn = new JButton("🗑️ Delete");
        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton performanceBtn = new JButton("📊 Performance");
        JButton advancedSearchBtn = new JButton("🔍 Advanced Search");
        JButton sortBtn = new JButton("🔄 Sort");
        
        addBtn.addActionListener(e -> showAddEmployeeDialog());
        updateBtn.addActionListener(e -> showUpdateEmployeeDialog());
        deleteBtn.addActionListener(e -> deleteSelectedEmployee());
        refreshBtn.addActionListener(e -> refreshTable());
        performanceBtn.addActionListener(e -> showPerformanceDialog());
        advancedSearchBtn.addActionListener(e -> showAdvancedSearchDialog());
        sortBtn.addActionListener(e -> showSortDialog());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(performanceBtn);
        buttonPanel.add(advancedSearchBtn);
        buttonPanel.add(sortBtn);
        
        // Add components to main layout
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
     * Load sample data for demonstration
     */
    private void loadSampleData() {
        employees.add(new Manager("M001", "John Smith", "IT", 80000, 4));
        employees.add(new Intern("I001", "Alice Johnson", "HR", 30000, 3));
        employees.add(new Regular("R001", "Bob Wilson", "Finance", 60000, 5));
        refreshTable();
        updateStatus("Sample data loaded. Total employees: " + employees.size());
    }
    
    /**
     * Refresh the employee table
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        int serialNumber = 1;
        for (Employee emp : employees) {
            Object[] row = {
                serialNumber++,                    // S.N.
                false,                             // Select checkbox
                emp.getClass().getSimpleName(),   // Type
                emp.getId(),                       // ID
                emp.getName(),                     // Name
                emp.getDepartment(),              // Department
                String.format("$%.2f", emp.getSalary()), // Base Salary
                emp.getPerformanceRating(),        // Rating
                String.format("$%.2f", emp.calculateSalary()) // Total Salary
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Update status message
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    
    /**
     * Show add employee dialog
     */
    private void showAddEmployeeDialog() {
        AddEmployeeDialog dialog = new AddEmployeeDialog(this, employees);
        dialog.setVisible(true);
        if (dialog.isEmployeeAdded()) {
            refreshTable();
            updateStatus("Employee added successfully. Total employees: " + employees.size());
        }
    }
    
    /**
     * Show update employee dialog
     */
    private void showUpdateEmployeeDialog() {
        int selectedRow = getSelectedEmployeeRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empId = (String) tableModel.getValueAt(selectedRow, 3); // ID is now column 3
        Employee emp = findEmployeeById(empId);
        
        if (emp != null) {
            UpdateEmployeeDialog dialog = new UpdateEmployeeDialog(this, emp);
            dialog.setVisible(true);
            if (dialog.isEmployeeUpdated()) {
                refreshTable();
                updateStatus("Employee updated successfully.");
            }
        }
    }
    
    /**
     * Delete selected employee
     */
    private void deleteSelectedEmployee() {
        int selectedRow = getSelectedEmployeeRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empId = (String) tableModel.getValueAt(selectedRow, 3); // ID is now column 3
        String empName = (String) tableModel.getValueAt(selectedRow, 4); // Name is now column 4
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete employee: " + empName + " (ID: " + empId + ")?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Employee emp = findEmployeeById(empId);
            if (emp != null) {
                employees.remove(emp);
                refreshTable();
                updateStatus("Employee deleted successfully. Total employees: " + employees.size());
            }
        }
    }
    
    /**
     * Perform search and filter the main table
     */
    private void performSearch(JComboBox<String> searchTypeCombo, JTextField searchField) {
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        // If search field is empty, show all employees
        if (searchTerm.isEmpty()) {
            refreshTable();
            updateStatus("Showing all employees.");
            return;
        }
        
        // Filter employees based on search criteria
        LinkedList<Employee> filteredEmployees = new LinkedList<>();
        
        switch (searchType) {
            case "All":
                for (Employee emp : employees) {
                    if (emp.getId().toLowerCase().contains(searchTerm) ||
                        emp.getName().toLowerCase().contains(searchTerm) ||
                        emp.getDepartment().toLowerCase().contains(searchTerm) ||
                        String.valueOf(emp.getPerformanceRating()).contains(searchTerm)) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
                
            case "ID":
                for (Employee emp : employees) {
                    if (emp.getId().toLowerCase().contains(searchTerm)) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
                
            case "Name":
                for (Employee emp : employees) {
                    if (emp.getName().toLowerCase().contains(searchTerm)) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
                
            case "Department":
                for (Employee emp : employees) {
                    if (emp.getDepartment().toLowerCase().contains(searchTerm)) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
                
            case "Rating":
                try {
                    int rating = Integer.parseInt(searchTerm);
                    for (Employee emp : employees) {
                        if (emp.getPerformanceRating() >= rating) {
                            filteredEmployees.add(emp);
                        }
                    }
                } catch (NumberFormatException e) {
                    // If not a number, search for exact rating match
                    for (Employee emp : employees) {
                        if (String.valueOf(emp.getPerformanceRating()).equals(searchTerm)) {
                            filteredEmployees.add(emp);
                        }
                    }
                }
                break;
        }
        
        // Update table with filtered results
        updateTableWithResults(filteredEmployees);
        
        if (filteredEmployees.isEmpty()) {
            updateStatus("No employees found matching: '" + searchTerm + "'");
        } else {
            updateStatus("Found " + filteredEmployees.size() + " employee(s) matching: '" + searchTerm + "'");
        }
    }
    
    /**
     * Update table with filtered results
     */
    private void updateTableWithResults(LinkedList<Employee> filteredEmployees) {
        tableModel.setRowCount(0);
        int serialNumber = 1;
        for (Employee emp : filteredEmployees) {
            Object[] row = {
                serialNumber++,                    // S.N.
                false,                             // Select checkbox
                emp.getClass().getSimpleName(),   // Type
                emp.getId(),                       // ID
                emp.getName(),                     // Name
                emp.getDepartment(),              // Department
                String.format("$%.2f", emp.getSalary()), // Base Salary
                emp.getPerformanceRating(),        // Rating
                String.format("$%.2f", emp.calculateSalary()) // Total Salary
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Show performance management dialog
     */
    private void showPerformanceDialog() {
        int selectedRow = getSelectedEmployeeRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to manage performance.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empId = (String) tableModel.getValueAt(selectedRow, 3); // ID is now column 3
        Employee emp = findEmployeeById(empId);
        
        if (emp != null) {
            PerformanceDialog dialog = new PerformanceDialog(this, emp);
            dialog.setVisible(true);
            if (dialog.isPerformanceUpdated()) {
                refreshTable();
                updateStatus("Performance updated successfully.");
            }
        }
    }
    
    /**
     * Load employees from file
     */
    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv", "txt"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            employees = FileHandler.loadEmployeesLinked(filename);
            refreshTable();
            updateStatus("Loaded " + employees.size() + " employees from " + filename);
        }
    }
    
    /**
     * Save employees to file
     */
    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv", "txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            FileHandler.saveEmployeesLinked(filename, employees);
            updateStatus("Saved " + employees.size() + " employees to " + filename);
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
     * Get the first selected employee row using checkboxes
     */
    private int getSelectedEmployeeRow() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 1); // Checkbox column
            if (isSelected != null && isSelected) {
                return i;
            }
        }
        return -1; // No employee selected
    }
    
    
    /**
     * Show advanced search dialog
     */
    private void showAdvancedSearchDialog() {
        AdvancedSearchDialog dialog = new AdvancedSearchDialog(this, employees);
        dialog.setVisible(true);
    }
    
    /**
     * Show sort dialog
     */
    private void showSortDialog() {
        JDialog sortDialog = new JDialog(this, "Sort Employees", true);
        sortDialog.setSize(400, 300);
        sortDialog.setLocationRelativeTo(this);
        sortDialog.setLayout(new GridLayout(4, 2, 10, 10));
        
        sortDialog.add(new JLabel("Sort by:"));
        JComboBox<SortingSearchingAlgorithms.SortCriteria> criteriaCombo = 
            new JComboBox<>(SortingSearchingAlgorithms.SortCriteria.values());
        sortDialog.add(criteriaCombo);
        
        sortDialog.add(new JLabel("Order:"));
        JComboBox<SortingSearchingAlgorithms.SortOrder> orderCombo = 
            new JComboBox<>(SortingSearchingAlgorithms.SortOrder.values());
        sortDialog.add(orderCombo);
        
        sortDialog.add(new JLabel("Algorithm:"));
        JComboBox<String> algorithmCombo = 
            new JComboBox<>(new String[]{"Quick Sort", "Merge Sort", "Heap Sort", "Insertion Sort"});
        sortDialog.add(algorithmCombo);
        
        JButton sortButton = new JButton("Sort");
        JButton cancelButton = new JButton("Cancel");
        
        sortButton.addActionListener(e -> {
            SortingSearchingAlgorithms.SortCriteria criteria = 
                (SortingSearchingAlgorithms.SortCriteria) criteriaCombo.getSelectedItem();
            SortingSearchingAlgorithms.SortOrder order = 
                (SortingSearchingAlgorithms.SortOrder) orderCombo.getSelectedItem();
            String algorithm = (String) algorithmCombo.getSelectedItem();
            
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
            
            // Update the main employees list
            employees.clear();
            employees.addAll(sortedEmployees);
            refreshTable();
            updateStatus(String.format("Sorted by %s (%s) using %s", criteria, order, algorithm));
            sortDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> sortDialog.dispose());
        
        sortDialog.add(sortButton);
        sortDialog.add(cancelButton);
        
        sortDialog.setVisible(true);
    }
    
    /**
     * Perform Quick Sort
     */
    private void performQuickSort() {
        SortingSearchingAlgorithms.SortCriteria criteria = showSortCriteriaDialog();
        if (criteria != null) {
            List<Employee> sortedEmployees = new ArrayList<>(employees);
            SortingSearchingAlgorithms.quickSort(sortedEmployees, criteria, SortingSearchingAlgorithms.SortOrder.ASCENDING);
            
            employees.clear();
            employees.addAll(sortedEmployees);
            refreshTable();
            updateStatus("Quick Sort completed by " + criteria);
        }
    }
    
    /**
     * Perform Merge Sort
     */
    private void performMergeSort() {
        SortingSearchingAlgorithms.SortCriteria criteria = showSortCriteriaDialog();
        if (criteria != null) {
            List<Employee> sortedEmployees = new ArrayList<>(employees);
            SortingSearchingAlgorithms.mergeSort(sortedEmployees, criteria, SortingSearchingAlgorithms.SortOrder.ASCENDING);
            
            employees.clear();
            employees.addAll(sortedEmployees);
            refreshTable();
            updateStatus("Merge Sort completed by " + criteria);
        }
    }
    
    /**
     * Perform Heap Sort
     */
    private void performHeapSort() {
        SortingSearchingAlgorithms.SortCriteria criteria = showSortCriteriaDialog();
        if (criteria != null) {
            List<Employee> sortedEmployees = new ArrayList<>(employees);
            SortingSearchingAlgorithms.heapSort(sortedEmployees, criteria, SortingSearchingAlgorithms.SortOrder.ASCENDING);
            
            employees.clear();
            employees.addAll(sortedEmployees);
            refreshTable();
            updateStatus("Heap Sort completed by " + criteria);
        }
    }
    
    /**
     * Perform Insertion Sort
     */
    private void performInsertionSort() {
        SortingSearchingAlgorithms.SortCriteria criteria = showSortCriteriaDialog();
        if (criteria != null) {
            List<Employee> sortedEmployees = new ArrayList<>(employees);
            SortingSearchingAlgorithms.insertionSort(sortedEmployees, criteria, SortingSearchingAlgorithms.SortOrder.ASCENDING);
            
            employees.clear();
            employees.addAll(sortedEmployees);
            refreshTable();
            updateStatus("Insertion Sort completed by " + criteria);
        }
    }
    
    /**
     * Show sort criteria selection dialog
     */
    private SortingSearchingAlgorithms.SortCriteria showSortCriteriaDialog() {
        JDialog criteriaDialog = new JDialog(this, "Select Sort Criteria", true);
        criteriaDialog.setSize(300, 150);
        criteriaDialog.setLocationRelativeTo(this);
        criteriaDialog.setLayout(new GridLayout(3, 1, 10, 10));
        
        criteriaDialog.add(new JLabel("Select criteria to sort by:", JLabel.CENTER));
        
        JComboBox<SortingSearchingAlgorithms.SortCriteria> criteriaCombo = 
            new JComboBox<>(SortingSearchingAlgorithms.SortCriteria.values());
        criteriaDialog.add(criteriaCombo);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> criteriaDialog.dispose());
        criteriaDialog.add(okButton);
        
        criteriaDialog.setVisible(true);
        
        return (SortingSearchingAlgorithms.SortCriteria) criteriaCombo.getSelectedItem();
    }
}
