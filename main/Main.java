package main;

import java.util.*;
import java.io.*;
import models.Student;
import services.StudentMenu;
import services.AdminMenu;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Student Course Management System ===");
            System.out.println("1. Register as Student");
            System.out.println("2. Login as Student");
            System.out.println("3. Login as Admin");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = -1;

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // consume invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    registerStudent(sc);
                    break;
                case 2:
                    loginStudent(sc);
                    break;
                case 3:
                    loginAdmin(sc);
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        sc.close();
    }

    // Student Registration
    public static void registerStudent(Scanner sc) {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your email: ");
        String email = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        Student student = new Student(name, email, password);

        try {
            FileWriter writer = new FileWriter("data/students.txt", true);
            writer.write(student.toFileString() + "\n");
            writer.close();
            System.out.println("Student registered successfully!");
        } catch (IOException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }

    // Student Login and Menu
    public static void loginStudent(Scanner sc) {
        System.out.print("Enter your email: ");
        String loginEmail = sc.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = sc.nextLine();

        boolean loginSuccess = false;
        Student loggedInStudent = null;

        try {
            Scanner fileScanner = new Scanner(new File("data/students.txt"));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Student s = Student.fromFileString(line);
                if (s != null && s.getEmail().equals(loginEmail) && s.getPassword().equals(loginPassword)) {
                    loginSuccess = true;
                    loggedInStudent = s;
                    break;
                }
            }
            fileScanner.close();

            if (loginSuccess) {
                System.out.println("Login successful! Welcome, " + loggedInStudent.getName());
                StudentMenu.handle(sc, loggedInStudent);
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (IOException e) {
            System.out.println("Error reading student data: " + e.getMessage());
        }
    }

    // Admin Login and Menu
    public static void loginAdmin(Scanner sc) {
        System.out.print("Enter admin email: ");
        String adminEmail = sc.nextLine();

        System.out.print("Enter admin password: ");
        String adminPassword = sc.nextLine();

        String validAdminEmail = "admin@scms.com";
        String validAdminPassword = "admin123";

        if (adminEmail.equals(validAdminEmail) && adminPassword.equals(validAdminPassword)) {
            System.out.println("Admin login successful!");
            AdminMenu.handle(sc);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    // Utility: Get student name from email
    public static String getStudentNameByEmail(String email) {
        try {
            Scanner sc = new Scanner(new File("data/students.txt"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Student s = Student.fromFileString(line);
                if (s != null && s.getEmail().equalsIgnoreCase(email)) {
                    sc.close();
                    return s.getName();
                }
            }
            sc.close();
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
