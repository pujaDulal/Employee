package employee;

/**
 * Intern subclass of Employee
 * Interns earn 50% of the base salary
 */
public class Intern extends Employee {

	public Intern(String id, String name, String department, double salary, int performanceRating) {
        super(id, name, department, salary, performanceRating);
    }

    // Interns automatically get 50% of base as bonus
    @Override
    public double calculateSalary() {
        return salary + (salary * 0.50);
    }
}
