package EmployeeManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeManagementSystem {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Step 1: Loading the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Step 2: Establishing a database connection
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EMPLOYEEJDBC_DB", "root", "Manibasu08");

        // Step 3: Creating a Scanner object to take input from the user
        Scanner scanner = new Scanner(System.in);

        // Step 4: Creating instances of Employee, Department, Salary, and Project classes
        Employee employee = new Employee(connection, scanner);
        Department department = new Department(connection, scanner);
        Salary salary = new Salary(connection, scanner);
        Project project = new Project(connection, scanner);

        // Main menu for Employee Management System
        while (true) {
            System.out.println("**** EMPLOYEE MANAGEMENT SYSTEM ****");
            System.out.println("1. Add Employee");
            System.out.println("2. Retrieve Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Add Department");
            System.out.println("6. Retrieve Departments");
            System.out.println("7. Add Salary");
            System.out.println("8. Update Salary");
            System.out.println("9. View Salaries");
            System.out.println("10. Add Project");
            System.out.println("11. View Projects");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    employee.addEmployee();
                    break;
                case 2:
                    employee.viewEmployees();
                    break;
                case 3:
                    employee.updateEmployee();
                    break;
                case 4:
                    employee.deleteEmployee();
                    break;
                case 5:
                    department.addDepartment();
                    break;
                case 6:
                    department.viewDepartments();
                    break;
                case 7:
                    salary.addSalary();
                    break;
                case 8:
                    // Implement update salary functionality in Salary class if needed
                    break;
                case 9:
                    salary.viewSalaries();
                    break;
                case 10:
                    project.addProject();
                    break;
                case 11:
                    project.viewProjects();
                    break;
                case 12:
                    System.out.println("Exiting Employee Management System. Goodbye!");
                    scanner.close();
                    connection.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }
}
