package employee;

public class Manager extends Employee {

    public Manager(String id, String name, String department, double salary, int performanceRating) {
        super(id, name, department, salary, performanceRating);
    }

    // Managers automatically get 10% bonus
    @Override
    public double calculateSalary() {
        return salary + (salary * 0.10);
    }
}
