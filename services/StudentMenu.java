package services;

import java.io.*;
import java.util.*;

import models.Student;

public class StudentMenu {
    public static void handle(Scanner sc, Student loggedInStudent) {
        boolean studentMenu = true;

        while (studentMenu) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. View My Enrollments");
            System.out.println("4. Unenroll from a Course");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewCourses();
                case 2 -> enrollInCourse(sc, loggedInStudent);
                case 3 -> viewMyEnrollments(loggedInStudent);
                case 4 -> unenrollFromCourse(sc, loggedInStudent);
                case 5 -> {
                    studentMenu = false;
                    System.out.println("Logging out...");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
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

    private static void enrollInCourse(Scanner sc, Student student) {
        System.out.print("Enter course name to enroll: ");
        String course = sc.nextLine();

        try {
            File file = new File("data/enrollments.txt");
            file.createNewFile();
            Scanner scanner = new Scanner(file);
            boolean alreadyEnrolled = false;

            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equalsIgnoreCase(student.getEmail() + "," + course)) {
                    alreadyEnrolled = true;
                    break;
                }
            }
            scanner.close();

            if (alreadyEnrolled) {
                System.out.println("You are already enrolled in this course.");
            } else {
                FileWriter writer = new FileWriter(file, true);
                writer.write(student.getEmail() + "," + course + "\n");
                writer.close();
                System.out.println("Enrolled successfully!");
            }
        } catch (IOException e) {
            System.out.println("Error enrolling: " + e.getMessage());
        }
    }

    private static void viewMyEnrollments(Student student) {
        System.out.println("--- Your Enrollments ---");
        try (Scanner scanner = new Scanner(new File("data/enrollments.txt"))) {
            int count = 1;
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 2 && parts[0].equalsIgnoreCase(student.getEmail())) {
                    System.out.println(count + ". " + parts[1]);
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrollments: " + e.getMessage());
        }
    }

    private static void unenrollFromCourse(Scanner sc, Student student) {
        ArrayList<String> myEnrollments = new ArrayList<>();
        ArrayList<String> allLines = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("data/enrollments.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                allLines.add(line);
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equalsIgnoreCase(student.getEmail())) {
                    myEnrollments.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrollments.");
            return;
        }

        if (myEnrollments.isEmpty()) {
            System.out.println("You are not enrolled in any course.");
            return;
        }

        System.out.println("--- Your Enrollments ---");
        for (int i = 0; i < myEnrollments.size(); i++) {
            System.out.println((i + 1) + ". " + myEnrollments.get(i));
        }

        System.out.print("Enter number of course to unenroll: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > myEnrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String courseToRemove = myEnrollments.get(choice - 1);

        try (FileWriter writer = new FileWriter("data/enrollments.txt", false)) {
            for (String line : allLines) {
                if (!line.equalsIgnoreCase(student.getEmail() + "," + courseToRemove)) {
                    writer.write(line + "\n");
                }
            }
            System.out.println("Successfully unenrolled from: " + courseToRemove);
        } catch (IOException e) {
            System.out.println("Error saving updated enrollments.");
        }
    }
}
