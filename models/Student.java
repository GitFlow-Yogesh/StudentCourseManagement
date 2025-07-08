package models;
public class Student {
    private String name;
    private String email;
    private String password;

    public Student(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Convert to string for file saving
    public String toFileString() {
        return name + "," + email + "," + password;
    }

    // Convert from string read from file
    public static Student fromFileString(String line) {
        if (line == null || line.isEmpty()) return null;
        String[] parts = line.split(",");
        if (parts.length == 3) {
            return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
        return null;
    }
}
