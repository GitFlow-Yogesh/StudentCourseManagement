package services;

import java.io.*;
import java.util.*;

import main.Main;

public class AdminMenu {
    public static void handle(Scanner sc) {
        boolean adminMenu = true;

        while (adminMenu) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Delete Course");
            System.out.println("4. View Student Enrollments");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addCourse(sc);
                case 2 -> viewCourses();
                case 3 -> deleteCourse(sc);
                case 4 -> viewStudentEnrollments();
                case 5 -> {
                    adminMenu = false;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addCourse(Scanner sc) {
        System.out.print("Enter course name: ");
        String name = sc.nextLine();
        System.out.print("Enter course description: ");
        String desc = sc.nextLine();

        try (FileWriter writer = new FileWriter("data/courses.txt", true)) {
            writer.write(name + "," + desc + "\n");
            System.out.println("Course added successfully!");
        } catch (IOException e) {
            System.out.println("Error saving course: " + e.getMessage());
        }
    }

    private static void viewCourses() {
        System.out.println("--- Available Courses ---");
        try (Scanner scanner = new Scanner(new File("data/courses.txt"))) {
            int count = 1;
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 2) {
                    System.out.println(count + ". " + parts[0] + " - " + parts[1]);
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courses: " + e.getMessage());
        }
    }

    private static void deleteCourse(Scanner sc) {
        List<String> courses = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("data/courses.txt"))) {
            while (scanner.hasNextLine()) {
                courses.add(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error reading courses.");
            return;
        }

        if (courses.isEmpty()) {
            System.out.println("No courses available to delete.");
            return;
        }

        System.out.println("--- Courses List ---");
        for (int i = 0; i < courses.size(); i++) {
            String[] parts = courses.get(i).split(",");
            if (parts.length >= 2) {
                System.out.println((i + 1) + ". " + parts[0] + " - " + parts[1]);
            }
        }

        System.out.print("Enter the number of the course to delete: ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index < 1 || index > courses.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String removedCourse = courses.remove(index - 1);

        try (FileWriter writer = new FileWriter("data/courses.txt", false)) {
            for (String course : courses) {
                writer.write(course + "\n");
            }
            System.out.println("Deleted: " + removedCourse.split(",")[0]);
        } catch (IOException e) {
            System.out.println("Error saving updated courses.");
        }
    }

    private static void viewStudentEnrollments() {
        System.out.println("--- Student Enrollments ---");
        try (Scanner scanner = new Scanner(new File("data/enrollments.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 2) {
                    String email = parts[0];
                    String course = parts[1];
                    String name = Main.getStudentNameByEmail(email);
                    if (name != null) {
                        System.out.println(name + " (" + email + ") → " + course);
                    } else {
                        System.out.println(email + " → " + course);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrollments.");
        }
    }
}
