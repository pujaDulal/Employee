package employee;

import java.io.*;
import java.util.LinkedList;

/**
 * FileHandler class
 * Handles file operations such as loading, saving employees and query results
 */
public class FileHandler {

    /**
     * Load employees from CSV file into a LinkedList
     */
    public static LinkedList<Employee> loadEmployeesLinked(String filename) {
        LinkedList<Employee> employees = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 6) continue; // Skip invalid lines

                String type = parts[0];
                String id = parts[1];
                String name = parts[2];
                String department = parts[3];
                double salary = Double.parseDouble(parts[4]);
                int rating = Integer.parseInt(parts[5]);

                // Create the correct Employee object based on type
                Employee emp = switch (type) {
                    case "Manager" -> new Manager(id, name, department, salary, rating);
                    case "Intern" -> new Intern(id, name, department, salary, rating);
                    case "Regular" -> new Regular(id, name, department, salary, rating);
                    default -> null;
                };

                if (emp != null) employees.add(emp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("⚠ File not found: " + filename);
        } catch (IOException e) {
            System.out.println("⚠ Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid number format in file: " + filename);
        }
        return employees;
    }

    /**
     * Save employees into CSV file
     */
    public static void saveEmployeesLinked(String filename, LinkedList<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Employee emp : employees) {
                writer.write(emp.toCSV());
                writer.newLine();
            }
            System.out.println("✅ Data saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("⚠ Error saving to file: " + filename);
        }
    }

    /**
     * Save query results into CSV file
     */
    public static void saveQueryResults(LinkedList<Employee> results, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Employee emp : results) {
                writer.write(emp.toCSV());
                writer.newLine();
            }
            System.out.println("✅ Query results saved to " + filename);
        } catch (IOException e) {
            System.out.println("⚠ Error saving query results to " + filename);
        }
    }
}
