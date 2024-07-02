package EmployeeManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Employee {
    private Connection connection;
    private Scanner scanner;

    public Employee(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addEmployee() {
        try {
            System.out.print("Enter employee name: ");
            String empName = scanner.nextLine();
            System.out.print("Enter employee age: ");
            int empAge = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter employee designation: ");
            String empDesignation = scanner.nextLine();
            System.out.print("Enter employee department: ");
            String empDepartment = scanner.nextLine();
            System.out.print("Enter employee salary: ");
            double empSalary = scanner.nextDouble();

            // Retrieve department ID based on department name
            int empDepartmentId = getDepartmentId(empDepartment);

            // Insert into employees table
            String sqlInsertEmployee = "INSERT INTO employees (emp_name, emp_age, emp_designation, emp_department_id, emp_salary) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatementEmployee = connection.prepareStatement(sqlInsertEmployee, Statement.RETURN_GENERATED_KEYS);
            preparedStatementEmployee.setString(1, empName);
            preparedStatementEmployee.setInt(2, empAge);
            preparedStatementEmployee.setString(3, empDesignation);
            preparedStatementEmployee.setInt(4, empDepartmentId);
            preparedStatementEmployee.setDouble(5, empSalary);

            int rowsAffectedEmployee = preparedStatementEmployee.executeUpdate();
            if (rowsAffectedEmployee > 0) {
                System.out.println("Employee added successfully.");

                // Retrieve generated employee ID
                ResultSet generatedKeysEmployee = preparedStatementEmployee.getGeneratedKeys();
                int empId = -1;
                if (generatedKeysEmployee.next()) {
                    empId = generatedKeysEmployee.getInt(1);
                    System.out.println("New Employee ID: " + empId);
                }

                // Insert into salary table
                String sqlInsertSalary = "INSERT INTO salary (emp_id, salary_amount) VALUES (?, ?)";
                PreparedStatement preparedStatementSalary = connection.prepareStatement(sqlInsertSalary);
                preparedStatementSalary.setInt(1, empId);
                preparedStatementSalary.setDouble(2, empSalary); // Set initial salary

                int rowsAffectedSalary = preparedStatementSalary.executeUpdate();
                if (rowsAffectedSalary > 0) {
                    System.out.println("Salary added successfully for Employee ID: " + empId);
                } else {
                    System.out.println("Failed to add salary for Employee ID: " + empId);
                }
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewEmployees() {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT e.emp_id, e.emp_name, e.emp_age, e.emp_designation, d.dept_name, e.emp_salary " +
                         "FROM employees e " +
                         "JOIN departments d ON e.emp_department_id = d.dept_id";
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Employee List:");
            System.out.println("---------------------------------------------------------------------");
            System.out.printf("| %-5s | %-20s | %-5s | %-20s | %-20s | %-10s |\n", "ID", "Name", "Age", "Designation", "Department", "Salary");
            System.out.println("---------------------------------------------------------------------");
            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String empName = resultSet.getString("emp_name");
                int empAge = resultSet.getInt("emp_age");
                String empDesignation = resultSet.getString("emp_designation");
                String empDepartment = resultSet.getString("dept_name");
                double empSalary = resultSet.getDouble("emp_salary");

                System.out.printf("| %-5d | %-20s | %-5d | %-20s | %-20s | %-10.2f |\n", empId, empName, empAge, empDesignation, empDepartment, empSalary);
            }
            System.out.println("---------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee() {
        try {
            System.out.print("Enter employee ID to update: ");
            int empId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter new employee name: ");
            String empName = scanner.nextLine();
            System.out.print("Enter new employee age: ");
            int empAge = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new employee designation: ");
            String empDesignation = scanner.nextLine();
            System.out.print("Enter new employee department: ");
            String empDepartment = scanner.nextLine();
            System.out.print("Enter new employee salary: ");
            double empSalary = scanner.nextDouble();

            // Retrieve department ID based on department name
            int empDepartmentId = getDepartmentId(empDepartment);

            // Update employee in the database
            String sqlUpdateEmployee = "UPDATE employees SET emp_name = ?, emp_age = ?, emp_designation = ?, emp_department_id = ?, emp_salary = ? WHERE emp_id = ?";
            PreparedStatement preparedStatementEmployee = connection.prepareStatement(sqlUpdateEmployee);
            preparedStatementEmployee.setString(1, empName);
            preparedStatementEmployee.setInt(2, empAge);
            preparedStatementEmployee.setString(3, empDesignation);
            preparedStatementEmployee.setInt(4, empDepartmentId);
            preparedStatementEmployee.setDouble(5, empSalary);
            preparedStatementEmployee.setInt(6, empId);

            int rowsAffected = preparedStatementEmployee.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee with ID " + empId + " updated successfully.");
            } else {
                System.out.println("Failed to update employee with ID " + empId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee() {
        try {
            System.out.print("Enter employee ID to delete: ");
            int empId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Delete related salary records first
            String sqlDeleteSalary = "DELETE FROM salary WHERE emp_id = ?";
            PreparedStatement preparedStatementSalary = connection.prepareStatement(sqlDeleteSalary);
            preparedStatementSalary.setInt(1, empId);
            int rowsAffectedSalary = preparedStatementSalary.executeUpdate();
            if (rowsAffectedSalary > 0) {
                System.out.println(rowsAffectedSalary + " salary record(s) deleted for Employee ID: " + empId);
            }

            // Then delete the employee
            String sqlDeleteEmployee = "DELETE FROM employees WHERE emp_id = ?";
            PreparedStatement preparedStatementEmployee = connection.prepareStatement(sqlDeleteEmployee);
            preparedStatementEmployee.setInt(1, empId);

            int rowsAffectedEmployee = preparedStatementEmployee.executeUpdate();
            if (rowsAffectedEmployee > 0) {
                System.out.println("Employee with ID " + empId + " deleted successfully.");
            } else {
                System.out.println("Failed to delete employee with ID " + empId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Helper method to retrieve department ID based on department name
    private int getDepartmentId(String deptName) throws SQLException {
        String sql = "SELECT dept_id FROM departments WHERE dept_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, deptName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("dept_id");
            } else {
                throw new SQLException("Department not found: " + deptName);
            }
        }
    }
}
