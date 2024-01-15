package ua.foxmided.foxstudent103852.schoolapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.StudentDao;
import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolapp.model.Course;
import ua.foxmided.foxstudent103852.schoolapp.model.Group;
import ua.foxmided.foxstudent103852.schoolapp.model.Student;
import ua.foxmided.foxstudent103852.schoolapp.util.ConnectionUtil;

public class StudentDaoImpl implements StudentDao {
    @Override
    public Student add(Student student) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            student = insertStudentData(student, connection);
            insertStudentCoursesData(student, connection);
            connection.commit();
            return student;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DataProcessingException("Unable to add student to database", e1);
            }
            throw new DataProcessingException("Unable to add student to database", e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataProcessingException("Unable to close database connection", e);
            }
        }
    }

    @Override
    public Optional<Student> get(Long id) {
        String getStudentSQL = "SELECT st.id, st.last_name, st.first_name, st.group_id, gr.name AS group_name "
                + "FROM students AS st "
                + "LEFT OUTER JOIN groups AS gr ON st.group_id = gr.id "
                + "WHERE st.id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getStudentSQL)) {
            Student student = null;
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                student = getStudentFromResultSet(resultSet);
                student.setCourses(getStudentCourses(student.getId(), connection));
            }
            return Optional.ofNullable(student);
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get student (id = " + id + ") from database", e);
        }
    }

    @Override
    public List<Student> getAll() {
        String getStudentsSQL = "SELECT st.id, st.last_name, st.first_name, st.group_id, gr.name AS group_name "
                + "FROM students AS st "
                + "LEFT OUTER JOIN groups AS gr ON st.group_id = gr.id ";
        List<Student> result = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getStudentsSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Student student = getStudentFromResultSet(resultSet);
                student.setCourses(getStudentCourses(student.getId(), connection));
                result.add(student);
            }
            return result;
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get students from database", e);
        }
    }

    @Override
    public void update(Student student) {
        if (student.getId() == null) {
            throw new DataProcessingException("Unable to update student data in database: " + student);
        }
        Connection connection = ConnectionUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            updateStudentData(student, connection);
            deleteStudentCoursesData(student.getId(), connection);
            insertStudentCoursesData(student, connection);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DataProcessingException("Unable to update student data in database: " + student, e1);
            }
            throw new DataProcessingException("Unable to update student data in database: " + student, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataProcessingException("Unable to close database connection", e);
            }
        }
    }

    @Override
    public void delete(long id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            deleteStudentCoursesData(id, connection);
            deleteStudentData(id, connection);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DataProcessingException("Unable to delete student (id = " + id + ") from database", e1);
            }
            throw new DataProcessingException("Unable to delete student (id = " + id + ") from database", e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataProcessingException("Unable to close database connection", e);
            }
        }
    }

    @Override
    public List<Student> getStudentsWithCourse(String courseName) {
        String getStudentsSQL = "SELECT st.*, gr.name AS group_name "
                + "FROM courses as cs, student_course AS stc "
                + "LEFT OUTER JOIN students as st ON st.id = stc.student_id "
                + "LEFT OUTER JOIN groups as gr ON gr.id = st.group_id "
                + "WHERE UPPER(cs.name) = UPPER(?) "
                + "AND cs.id = stc.course_id;";
        List<Student> result = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getStudentsSQL)) {
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Student student = getStudentFromResultSet(resultSet);
                student.setCourses(getStudentCourses(student.getId(), connection));
                result.add(student);
            }
            return result;
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get students from database", e);
        }
    }

    private Student getStudentFromResultSet(ResultSet resultSet) throws SQLException {
        Long studentId = resultSet.getLong("id");
        String lastName = resultSet.getString("last_name");
        String firstName = resultSet.getString("first_name");
        List<Course> courses = null;
        Group group = null;
        Long groupId = resultSet.getObject("group_id", Long.class);
        if (!resultSet.wasNull()) {
            String groupName = resultSet.getString("group_name");
            group = new Group(groupId, groupName);
        }
        return new Student(studentId, group, lastName, firstName, courses);
    }

    private List<Course> getStudentCourses(Long studentId, Connection connection) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String getStudentCoursesSQL = "SELECT cs.id, cs.name, cs.description "
                + "FROM student_course AS stcs "
                + "LEFT OUTER JOIN courses AS cs ON stcs.course_id = cs.id "
                + "WHERE stcs.student_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(getStudentCoursesSQL)) {
            statement.setLong(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long courseId = resultSet.getObject("id", Long.class);
                String courseName = resultSet.getString("name");
                String courseDescription = resultSet.getString("description");
                courses.add(new Course(courseId, courseName, courseDescription));
            }
        }
        return courses;
    }

    private Student insertStudentData(Student student, Connection connection) throws SQLException {
        String addStudentSQL = "INSERT INTO students(group_id, last_name, first_name) VALUES(?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(addStudentSQL,
                Statement.RETURN_GENERATED_KEYS)) {
            if (student.getGroup() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setLong(1, student.getGroup().getId());
            }
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getFirstName());
            if ((statement.executeUpdate() != 0) && (statement.getGeneratedKeys().next())) {
                student.setId(statement.getGeneratedKeys().getObject(1, Long.class));
                return student;
            }
            throw new SQLException("Unable to add student to database: " + student);
        }
    }

    private void updateStudentData(Student student, Connection connection) throws SQLException {
        String updateStudentSQL = "UPDATE students SET group_id = ?, last_name = ?, first_name = ? "
                + " WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(updateStudentSQL)) {
            if (student.getGroup() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setLong(1, student.getGroup().getId());
            }
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getFirstName());
            statement.setLong(4, student.getId());
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Unable to update student data in database: " + student);
            }
        }
    }

    private void deleteStudentData(long studentId, Connection connection) throws SQLException {
        String deleteStudentSQL = "DELETE FROM students WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteStudentSQL)) {
            statement.setLong(1, studentId);
            statement.executeUpdate();
        }
    }

    private void insertStudentCoursesData(Student student, Connection connection) throws SQLException {
        String addStudentCoursesSQL = "INSERT INTO student_course(student_id, course_id) VALUES(?, ?);";
        for (Course course : student.getCourses()) {
            try (PreparedStatement statement = connection.prepareStatement(addStudentCoursesSQL)) {
                statement.setLong(1, student.getId());
                statement.setLong(2, course.getId());
                if (statement.executeUpdate() == 0) {
                    throw new SQLException("Unable to add course (id = " + course.getId()
                            + ") for student (id = " + student.getId() + "): " + student + ", " + course);
                }
            }
        }
    }

    private void deleteStudentCoursesData(long studentId, Connection connection) throws SQLException {
        String deleteStudentCoursesSQL = "DELETE FROM student_course WHERE student_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteStudentCoursesSQL)) {
            statement.setLong(1, studentId);
            statement.executeUpdate();
        }
    }
}
