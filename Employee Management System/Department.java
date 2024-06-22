package EmployeeManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Department {
    private Connection connection;
    private Scanner scanner;

    public Department(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addDepartment() {
        try {
            System.out.print("Enter department name: ");
            String deptName = scanner.nextLine();
            System.out.print("Enter department location: ");
            String deptLocation = scanner.nextLine();

            String sql = "INSERT INTO departments (dept_name, dept_location) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, deptName);
            preparedStatement.setString(2, deptLocation);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Department added successfully.");
            } else {
                System.out.println("Failed to add department.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewDepartments() {
        String sql = "SELECT * FROM departments";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Department List:");
            System.out.println("-----------------------------------------");
            System.out.printf("| %-5s | %-20s | %-20s |\n", "ID", "Name", "Location");
            System.out.println("-----------------------------------------");
            while (resultSet.next()) {
                int deptId = resultSet.getInt("dept_id");
                String deptName = resultSet.getString("dept_name");
                String deptLocation = resultSet.getString("dept_location");

                System.out.printf("| %-5d | %-20s | %-20s |\n", deptId, deptName, deptLocation);
            }
            System.out.println("-----------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add update and delete methods similar to Employee if needed
}
