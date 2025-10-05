package employee;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * EmployeeManagementSystem
 * Main class containing the menu-driven interface
 * Allows user to manage employees with CRUD, search, file handling, and salary management
 */
public class EmployeeManagementSystem {

    private static LinkedList<Employee> employees = new LinkedList<>(); // Employee list
    private static Scanner scanner = new Scanner(System.in);            // Scanner for input

    public static void main(String[] args) {
        // Ask user to choose between GUI and CUI
        System.out.println("===== Employee Management System =====");
        System.out.println("Choose Interface:");
        System.out.println("1. GUI (Graphical User Interface)");
        System.out.println("2. CUI (Command Line Interface)");
        System.out.print("👉 Choose option (1/2): ");
        
        String interfaceChoice = scanner.nextLine().trim();
        
        if ("1".equals(interfaceChoice)) {
            // Launch GUI
            System.out.println("Launching GUI...");
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    new EmployeeManagementGUI();
                    System.out.println("GUI launched successfully!");
                } catch (Exception e) {
                    System.out.println("Error launching GUI: " + e.getMessage());
                    e.printStackTrace();
                    System.out.println("Falling back to CUI...");
                    runCUI();
                }
            });
            
            // Keep the main thread alive for GUI
            try {
                Thread.sleep(1000); // Give GUI time to initialize
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            // Run CUI
            runCUI();
        }
    }
    
    /**
     * Run the Command Line Interface
     */
    private static void runCUI() {
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> loadFromFile();
                case "2" -> addEmployee();
                case "3" -> updateEmployee();
                case "4" -> deleteEmployee();
                case "5" -> viewEmployee();
                case "6" -> saveToFile();
                case "7" -> {  // Manage Performance/Salary
                    System.out.print("Enter Employee ID: ");
                    String id = scanner.nextLine().trim();
                    Employee emp = findEmployeeById(id);
                    if (emp != null) {
                        managePerformance(emp);  // Pass the Employee object
                    } else {
                        System.out.println("⚠ Employee not found.");
                    }
                }

                case "8" -> {
                    System.out.println("👋 Exiting program. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("⚠ Invalid option. Please try again.");
            }
        }
    }

    /**
     * Show the main menu
     */
    private static void showMenu() {
        System.out.println("\n===== Employee Management System =====");
        System.out.println("1. Load employee records from file");
        System.out.println("2. Add new employee");
        System.out.println("3. Update employee information");
        System.out.println("4. Delete employee");
        System.out.println("5. View employee details");
        System.out.println("6. Save employee records to file");
        System.out.println("7. Manage Performance/Salary");
        System.out.println("8. Exit");
        System.out.print("👉 Choose option: ");
    }

    /**
     * Load employees from file
     */
    private static void loadFromFile() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();
        employees = FileHandler.loadEmployeesLinked(filename);
        System.out.println("✅ Loaded " + employees.size() + " employee(s).");
    }

    /**
     * Save employees to file
     */
    private static void saveToFile() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();
        FileHandler.saveEmployeesLinked(filename, employees);
    }

    /**
     * Add new employee
     */
    private static void addEmployee() {
        try {
            System.out.print("Enter type (Manager/Intern/Regular): ");
            String type = scanner.nextLine().trim();

            System.out.print("Enter ID: ");
            String id = scanner.nextLine().trim();
            if (findEmployeeById(id) != null) {
                System.out.println("⚠ Employee with this ID already exists!");
                return;
            }

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter department: ");
            String dept = scanner.nextLine().trim();

            System.out.print("Enter salary: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine().trim());
            if (rating < 1 || rating > 5) {
                System.out.println("⚠ Rating must be between 1 and 5.");
                return;
            }

            Employee emp = switch (type.toLowerCase()) {
                case "manager" -> new Manager(id, name, dept, salary, rating);
                case "intern" -> new Intern(id, name, dept, salary, rating);
                case "regular" -> new Regular(id, name, dept, salary, rating);
                default -> null;
            };

            if (emp == null) {
                System.out.println("⚠ Invalid employee type.");
                return;
            }

            employees.add(emp);
            System.out.println("✅ Employee added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid input. Salary and rating must be numbers.");
        }
    }

    /**
     * Update existing employee
     */
    private static void updateEmployee() {
        System.out.print("Enter employee ID to update: ");
        Employee emp = findEmployeeById(scanner.nextLine().trim());
        if (emp == null) {
            System.out.println("⚠ Employee not found.");
            return;
        }

        System.out.print("New name (blank to keep): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) emp.setName(name);

        System.out.print("New department (blank to keep): ");
        String dept = scanner.nextLine().trim();
        if (!dept.isEmpty()) emp.setDepartment(dept);

        System.out.print("New salary (blank to keep): ");
        String sal = scanner.nextLine().trim();
        if (!sal.isEmpty()) {
            try {
                emp.setSalary(Double.parseDouble(sal));
            } catch (NumberFormatException e) {
                System.out.println("⚠ Invalid salary. Keeping old value.");
            }
        }

        System.out.print("New rating 1-5 (blank to keep): ");
        String rat = scanner.nextLine().trim();
        if (!rat.isEmpty()) {
            try {
                int r = Integer.parseInt(rat);
                if (r >= 1 && r <= 5) emp.setPerformanceRating(r);
                else System.out.println("⚠ Invalid rating. Keeping old value.");
            } catch (NumberFormatException e) {
                System.out.println("⚠ Invalid input. Keeping old value.");
            }
        }
        System.out.println("✅ Employee updated successfully.");
    }

    /**
     * Delete employee
     */
    private static void deleteEmployee() {
        System.out.print("Enter ID to delete: ");
        Employee emp = findEmployeeById(scanner.nextLine().trim());
        if (emp == null) {
            System.out.println("⚠ Employee not found.");
            return;
        }
        employees.remove(emp);
        System.out.println("✅ Employee deleted successfully.");
    }

    /**
     * View/Search employees
     */
    private static void viewEmployee() {
        System.out.println("Search by: 1-ID  2-Name  3-Rating");
        System.out.print("Option: ");
        String opt = scanner.nextLine().trim();

        LinkedList<Employee> results = new LinkedList<>();

        switch (opt) {
            case "1" -> {
                System.out.print("Enter Employee ID: ");
                Employee emp = findEmployeeById(scanner.nextLine().trim());
                if (emp != null) {
                    printEmployee(emp);
                    results.add(emp);
                    printEmployee(emp);
                    
                    // Encapsulation demo
                    System.out.println("Salary before bonus: " + emp.getSalary());
                    emp.setSalary(emp.getSalary() + 500);  // example bonus
                    System.out.println("Salary after bonus: " + emp.getSalary());
                } else System.out.println("⚠ Employee not found.");
            }
            case "2" -> {
                System.out.print("Enter Employee Name (keyword): ");
                String name = scanner.nextLine().trim().toLowerCase();
                for (Employee emp : employees) {
                    if (emp.getName().toLowerCase().contains(name)) {
                        printEmployee(emp);
                        results.add(emp);
                    }
                }
                if (results.isEmpty()) System.out.println("⚠ No employees found.");
            }
            case "3" -> {
                System.out.print("Enter minimum performance rating (1-5): ");
                try {
                    int rating = Integer.parseInt(scanner.nextLine().trim());
                    for (Employee emp : employees) {
                        if (emp.getPerformanceRating() >= rating) {
                            printEmployee(emp);
                            results.add(emp);
                        }
                    }
                    if (results.isEmpty()) System.out.println("⚠ No employees found.");
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Invalid rating input.");
                }
            }
            default -> {
                System.out.println("⚠ Invalid option.");
                return;
            }
        }

        // Save query results to file
        if (!results.isEmpty()) {
            System.out.print("Save results to file? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("Enter filename: ");
                String filename = scanner.nextLine().trim();
                FileHandler.saveQueryResults(results, filename);
            }
        }
    }

    /**
     * Manage performance and salary of an employee
     */
    private static void managePerformance(Employee emp) {
        // Display basic employee info
        System.out.println("Employee: " + emp.getName() + ", Base Salary: " + emp.getSalary());

        // Calculate automatic bonus
        double autoBonus = emp.calculateSalary() - emp.getSalary();
        double totalSalary = emp.getSalary() + autoBonus;

        // Issue performance letter
        if(emp.getPerformanceRating() <= 2) {
            System.out.println("⚠ Warning Letter issued to " + emp.getName());
        } else if(emp.getPerformanceRating() >= 4) {
            System.out.println("🎉 Appreciation Letter issued to " + emp.getName());
        } else {
            System.out.println(emp.getName() + "'s performance is satisfactory.");
        }

        // Ask user for bonus or fine
        System.out.println("Choose action: 1 - Apply Automatic Bonus | 2 - Apply Fine");
        String choice = scanner.nextLine().trim();

        switch(choice) {
            case "1": // Bonus applied automatically
                System.out.println("💰 Automatic bonus applied: " + autoBonus);
                System.out.println("💰 Total Salary (Base + Bonus): " + totalSalary);
                break;
            case "2": // Fine manually applied
                try {
                    System.out.print("Enter fine amount: ");
                    double fine = Double.parseDouble(scanner.nextLine().trim());
                    double salaryAfterFine = totalSalary - fine;
                    System.out.println("⚠ Fine applied: " + fine);
                    System.out.println("💰 Total Salary after fine: " + salaryAfterFine);
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Invalid amount entered.");
                }
                break;
            default:
                System.out.println("⚠ Invalid option.");
        }
    }





    

    /**
     * Find employee by ID
     */
    private static Employee findEmployeeById(String id) {
        for (Employee emp : employees) {
            if (emp.getId().equalsIgnoreCase(id)) return emp;
        }
        return null;
    }

    /**
     * Print employee details
     */
    private static void printEmployee(Employee emp) {
        // Calculate total salary using automatic bonus logic from subclass
        double calculatedSalary = emp.calculateSalary();
        double autoBonus = calculatedSalary - emp.getSalary(); // automatic bonus only

        // Print employee details, automatic bonus, and total calculated salary
        System.out.println(
            emp.toString() + 
            ", Automatic Bonus: " + autoBonus + 
            ", Total Calculated Salary: " + calculatedSalary
        );
    }

}
