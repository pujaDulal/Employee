package employee;

/**
 * Abstract Employee class
 * Defines the common attributes and behaviors for all employees.
 * Subclasses: Manager, Intern, Regular
 */
public abstract class Employee {

    protected String id;             // Unique employee ID
    protected String name;           // Employee name
    protected String department;     // Department of employee
    protected double salary;         // Base salary
    protected int performanceRating; // Performance rating (1–5)

    /**
     * Constructor to initialize Employee attributes
     */
    public Employee(String id, String name, String department, double salary, int performanceRating) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.performanceRating = performanceRating;
    }

    /**
     * Abstract method: calculate salary
     * Each subclass provides its own implementation.
     */
    public abstract double calculateSalary();

    // ---------------- Getters and Setters ----------------
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public int getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(int performanceRating) { this.performanceRating = performanceRating; }

    /**
     * Convert employee details to string for display
     */
    @Override
    public String toString() {
        return "ID: " + id +
               ", Name: " + name +
               ", Dept: " + department +
               ", Base Salary: " + salary +
               ", Performance: " + performanceRating;
    }

    /**
     * Convert employee details to CSV format for saving into file
     */
    public String toCSV() {
        return this.getClass().getSimpleName() + "," + id + "," + name + "," +
               department + "," + salary + "," + performanceRating;
    }
}
