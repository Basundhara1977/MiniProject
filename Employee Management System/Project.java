package EmployeeManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Project {
    private Connection connection;
    private Scanner scanner;

    public Project(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addProject() {
        try {
            System.out.print("Enter project name: ");
            String projectName = scanner.nextLine();
            System.out.print("Enter project description: ");
            String projectDescription = scanner.nextLine();

            String sql = "INSERT INTO projects (project_name, project_description) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, projectDescription);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Project added successfully.");

                // Retrieve generated project ID
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int projectId = generatedKeys.getInt(1);
                    System.out.println("New Project ID: " + projectId);
                }
            } else {
                System.out.println("Failed to add project.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewProjects() {
        String sql = "SELECT * FROM projects";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("Project List:");
            System.out.println("------------------------------------------------");
            System.out.printf("| %-5s | %-30s | %-30s |\n", "ID", "Name", "Description");
            System.out.println("------------------------------------------------");
            while (resultSet.next()) {
                int projectId = resultSet.getInt("project_id");
                String projectName = resultSet.getString("project_name");
                String projectDescription = resultSet.getString("project_description");

                System.out.printf("| %-5d | %-30s | %-30s |\n", projectId, projectName, projectDescription);
            }
            System.out.println("------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add update and delete methods if needed
}
