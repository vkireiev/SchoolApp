package ua.foxmided.foxstudent103852.schoolapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.CourseDao;
import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolapp.model.Course;
import ua.foxmided.foxstudent103852.schoolapp.util.ConnectionUtil;

public class CourseDaoImpl implements CourseDao {
    @Override
    public Course add(Course course) {
        String addCourseSQL = "INSERT INTO courses(name, description) VALUES(?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(addCourseSQL,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            if ((statement.executeUpdate() != 0) && (statement.getGeneratedKeys().next())) {
                course.setId(statement.getGeneratedKeys().getObject(1, Long.class));
                return course;
            }
            throw new DataProcessingException("Unable to add course to database: " + course);
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to add course to database: " + course, e);
        }
    }

    @Override
    public Optional<Course> get(Long id) {
        String getCourseSQL = "SELECT * FROM courses "
                + "WHERE id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getCourseSQL)) {
            Course course = null;
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                course = getCourseFromResultSet(resultSet);
            }
            return Optional.ofNullable(course);
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get course with id = " + id + " from database", e);
        }
    }

    @Override
    public List<Course> getAll() {
        List<Course> result = new ArrayList<>();
        String getCoursesSQL = "SELECT * FROM courses;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getCoursesSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(getCourseFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get courses from database", e);
        }
        return result;
    }

    private Course getCourseFromResultSet(ResultSet resultSet) throws SQLException {
        Long courseId = resultSet.getObject("id", Long.class);
        String courseName = resultSet.getString("name");
        String courseDescription = resultSet.getString("description");
        return new Course(courseId, courseName, courseDescription);
    }
}
