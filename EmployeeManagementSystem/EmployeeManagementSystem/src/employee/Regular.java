package employee;

/**
 * Regular Employee subclass
 * Regular employees receive their base salary with no modifications
 */
public class Regular extends Employee {

	public Regular(String id, String name, String department, double salary, int performanceRating) {
        super(id, name, department, salary, performanceRating);
    }

    // Regular employees get no automatic bonus
    @Override
    public double calculateSalary() {
        return salary;
    }
}
