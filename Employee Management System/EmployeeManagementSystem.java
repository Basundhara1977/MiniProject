package EmployeeManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeManagementSystem {

	public static void main(String[] args) {
	    Scanner scanner = null; // Declare scanner outside the try block
	    
	    try {
	        // Step 1: Loading the MySQL JDBC driver
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        // Step 2: Establishing a database connection
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/EMPLOYEE_JDBC_DB", "root", "Manibasu08");

	        // Step 3: Creating a Scanner object to take input from the user
	        scanner = new Scanner(System.in);

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
	            System.out.println("8. View Salaries");
	            System.out.println("9. Add Project");
	            System.out.println("10. View Projects");
	            System.out.println("11. Exit");
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
	                    salary.viewSalaries();
	                    break;
	                case 9:
	                    System.out.print("Enter employee ID for the project: ");
	                    int empIdForProject = scanner.nextInt();
	                    scanner.nextLine(); // Consume newline
	                    project.addProject(empIdForProject);
	                    break;
	                case 10:
	                    project.viewProjects();
	                    break;
	                case 11:
	                    System.out.println("Exiting Employee Management System. Goodbye!");
	                    return; // Exit the main method
	                default:
	                    System.out.println("Invalid choice. Please enter a valid option.");
	                    break;
	            }
	        }
	    } catch (ClassNotFoundException e) {
	        System.err.println("Error: MySQL JDBC driver not found");
	        e.printStackTrace();
	    } catch (SQLException e) {
	        System.err.println("Error: Failed to connect to the database");
	        e.printStackTrace();
	    } finally {
	        if (scanner != null) {
	            scanner.close(); // Close the scanner in the finally block
	        }
	    }
	}
}
