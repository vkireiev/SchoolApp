package ua.foxmided.foxstudent103852.schoolapp.dao.interfaces;

import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.model.Student;

public interface StudentDao {
    public Student add(Student student);

    public Optional<Student> get(Long id);

    public List<Student> getAll();

    public void update(Student student);

    public void delete(long id);

    public List<Student> getStudentsWithCourse(String courseName);
}
