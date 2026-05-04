import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentFullGUI extends JFrame {

    JTextField idField, nameField, ageField, genderField, courseIdField;
    JTextField subjectField, marksField;
    JTextField courseNameField, durationField;
    JTextField dateField, statusField;

    JTable table;
    DefaultTableModel model;

    Connection con;

    public StudentFullGUI() {
        connectDB();

        setTitle("Student Management System");
        setSize(900, 600);
        setLayout(new FlowLayout());

        // Student
        add(new JLabel("Student ID")); idField = new JTextField(5); add(idField);
        add(new JLabel("Name")); nameField = new JTextField(10); add(nameField);
        add(new JLabel("Age")); ageField = new JTextField(5); add(ageField);
        add(new JLabel("Gender")); genderField = new JTextField(8); add(genderField);
        add(new JLabel("Course ID")); courseIdField = new JTextField(5); add(courseIdField);

        // Marks
        add(new JLabel("Subject")); subjectField = new JTextField(10); add(subjectField);
        add(new JLabel("Marks")); marksField = new JTextField(5); add(marksField);

        // Course
        add(new JLabel("Course Name")); courseNameField = new JTextField(10); add(courseNameField);
        add(new JLabel("Duration")); durationField = new JTextField(10); add(durationField);

        // Attendance
        add(new JLabel("Date (YYYY-MM-DD)")); dateField = new JTextField(10); add(dateField);
        add(new JLabel("Status")); statusField = new JTextField(10); add(statusField);

        // Buttons
        JButton addStudentBtn = new JButton("Add Student");
        JButton addMarksBtn = new JButton("Add Marks");
        JButton addCourseBtn = new JButton("Add Course");
        JButton addAttendanceBtn = new JButton("Add Attendance");
        JButton viewBtn = new JButton("View All");

        add(addStudentBtn);
        add(addMarksBtn);
        add(addCourseBtn);
        add(addAttendanceBtn);
        add(viewBtn);

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table));

        // Actions
        addStudentBtn.addActionListener(e -> addStudent());
        addMarksBtn.addActionListener(e -> addMarks());
        addCourseBtn.addActionListener(e -> addCourse());
        addAttendanceBtn.addActionListener(e -> addAttendance());
        viewBtn.addActionListener(e -> viewData());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_db",
                "root",
                ""
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void addStudent() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Student VALUES (?, ?, ?, ?, ?)"
            );
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.setString(2, nameField.getText());
            ps.setInt(3, Integer.parseInt(ageField.getText()));
            ps.setString(4, genderField.getText());
            ps.setInt(5, Integer.parseInt(courseIdField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addMarks() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Marks(student_id, subject, marks) VALUES (?, ?, ?)"
            );
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.setString(2, subjectField.getText());
            ps.setInt(3, Integer.parseInt(marksField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Marks Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addCourse() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Course VALUES (?, ?, ?)"
            );
            ps.setInt(1, Integer.parseInt(courseIdField.getText()));
            ps.setString(2, courseNameField.getText());
            ps.setString(3, durationField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Course Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addAttendance() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Attendance(student_id, date, status) VALUES (?, ?, ?)"
            );
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.setDate(2, Date.valueOf(dateField.getText()));
            ps.setString(3, statusField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void viewData() {
        try {
            model.setRowCount(0);
            model.setColumnIdentifiers(
                new String[]{"ID", "Name", "Course", "Subject", "Marks", "Date", "Status"}
            );

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT s.student_id, s.name, c.course_name, m.subject, m.marks, a.date, a.status " +
                "FROM Student s " +
                "LEFT JOIN Course c ON s.course_id = c.course_id " +
                "LEFT JOIN Marks m ON s.student_id = m.student_id " +
                "LEFT JOIN Attendance a ON s.student_id = a.student_id"
            );

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getString(7)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new StudentFullGUI();
    }
}