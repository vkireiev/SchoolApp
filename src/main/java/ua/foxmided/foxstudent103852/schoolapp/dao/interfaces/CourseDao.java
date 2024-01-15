package ua.foxmided.foxstudent103852.schoolapp.dao.interfaces;

import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.model.Course;

public interface CourseDao {
    public Course add(Course course);

    public Optional<Course> get(Long id);

    public List<Course> getAll();
}
