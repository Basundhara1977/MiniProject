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

            String sql = "INSERT INTO employees (emp_name, emp_age, emp_designation, emp_department_id, emp_salary) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, empName);
            preparedStatement.setInt(2, empAge);
            preparedStatement.setString(3, empDesignation);
            preparedStatement.setInt(4, empDepartmentId);
            preparedStatement.setDouble(5, empSalary);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewEmployees() {
        String sql = "SELECT e.emp_id, e.emp_name, e.emp_age, e.emp_designation, d.dept_name, e.emp_salary " +
                     "FROM employees e JOIN departments d ON e.emp_department_id = d.dept_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("Employee List:");
            System.out.println("--------------------------------------------------------");
            System.out.printf("| %-5s | %-20s | %-5s | %-20s | %-20s | %-10s |\n",
                    "ID", "Name", "Age", "Designation", "Department", "Salary");
            System.out.println("--------------------------------------------------------");
            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String empName = resultSet.getString("emp_name");
                int empAge = resultSet.getInt("emp_age");
                String empDesignation = resultSet.getString("emp_designation");
                String empDepartment = resultSet.getString("dept_name");
                double empSalary = resultSet.getDouble("emp_salary");

                System.out.printf("| %-5d | %-20s | %-5d | %-20s | %-20s | %-10.2f |\n",
                        empId, empName, empAge, empDesignation, empDepartment, empSalary);
            }
            System.out.println("--------------------------------------------------------");
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

            String sql = "UPDATE employees SET emp_name = ?, emp_age = ?, emp_designation = ?, " +
                    "emp_department_id = ?, emp_salary = ? WHERE emp_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, empName);
            preparedStatement.setInt(2, empAge);
            preparedStatement.setString(3, empDesignation);
            preparedStatement.setInt(4, empDepartmentId);
            preparedStatement.setDouble(5, empSalary);
            preparedStatement.setInt(6, empId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Failed to update employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee() {
        try {
            System.out.print("Enter employee ID to delete: ");
            int empId = scanner.nextInt();

            String sql = "DELETE FROM employees WHERE emp_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Failed to delete employee.");
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
