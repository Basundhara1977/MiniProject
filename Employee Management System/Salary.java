package EmployeeManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Salary {
    private Connection connection;
    private Scanner scanner;

    public Salary(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addSalary() {
        try {
            System.out.print("Enter employee ID: ");
            int empId = scanner.nextInt();
            System.out.print("Enter salary amount: ");
            double salaryAmount = scanner.nextDouble();

            String sql = "INSERT INTO salary (emp_id, salary_amount) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);
            preparedStatement.setDouble(2, salaryAmount);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Salary added successfully.");
            } else {
                System.out.println("Failed to add salary.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewSalaries() {
        String sql = "SELECT s.*, e.emp_name FROM salary s JOIN employees e ON s.emp_id = e.emp_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Salary List:");
            System.out.println("---------------------------------------------------------------");
            System.out.printf("| %-5s | %-20s | %-20s |\n", "ID", "Employee Name", "Salary Amount");
            System.out.println("---------------------------------------------------------------");
            while (resultSet.next()) {
                int salaryId = resultSet.getInt("salary_id");
                String empName = resultSet.getString("emp_name");
                double salaryAmount = resultSet.getDouble("salary_amount");

                System.out.printf("| %-5d | %-20s | %-20.2f |\n", salaryId, empName, salaryAmount);
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add update and delete methods similar to other classes if needed
}
