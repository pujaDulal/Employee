/**
 * Employee Management System Module
 * Provides employee management functionality with both GUI and CUI interfaces
 */
module Employee {
    requires java.desktop;  // For GUI components (Swing, AWT)
    requires java.base;     // For basic Java functionality
    
    exports employee;       // Export the employee package
}