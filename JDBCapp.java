import java.sql.*;
import java.util.Scanner;

class JDBCapp {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/School_DB", "root", "WJ28@krhps")) {
            while (true) {
                System.out.println("\n----- School Student Database Menu -----");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Delete Student by ID");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                
                int choice = s.nextInt();
                s.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addStudent(con, s);
                    case 2 -> viewStudent(con);
                    case 3 -> deleteStudent(con, s);
                    case 4 -> {
                        System.out.println("✅ Exiting the Database. Goodbye!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice. Please enter 1 to 4.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent(Connection con, Scanner s) throws SQLException {
        System.out.print("Enter ID: ");
        int id = s.nextInt();
        s.nextLine(); // Consume newline

        System.out.print("Enter Name: ");
        String name = s.nextLine();

        System.out.print("Enter Age: ");
        int age = s.nextInt();
        s.nextLine();

        System.out.print("Enter Course: ");
        String course = s.nextLine();

        try (PreparedStatement ps = con.prepareStatement("INSERT INTO STUDENT (Id, Name, Age, Course) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, course);
            ps.executeUpdate();
            System.out.println("✅ Student details added successfully!");
        }
    }

    public static void viewStudent(Connection con) throws SQLException {
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM STUDENT")) {
            System.out.println("ID | Name | Age | Course\n----------------------------");
            while (rs.next()) {
                System.out.printf("%d | %s | %d | %s\n",
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getInt("Age"),
                        rs.getString("Course"));
            }
        }
    }

    public static void deleteStudent(Connection con, Scanner s) throws SQLException {
        System.out.print("Enter the ID of the student to delete: ");
        int id = s.nextInt();

        try (PreparedStatement ps = con.prepareStatement("DELETE FROM STUDENT WHERE Id = ?")) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Student with ID " + id + " was deleted successfully.");
            } else {
                System.out.println("⚠️ No student found with ID " + id + ".");
            }
        }
    }
}
