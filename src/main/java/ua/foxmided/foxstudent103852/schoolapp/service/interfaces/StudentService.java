package ua.foxmided.foxstudent103852.schoolapp.service.interfaces;

import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.model.Student;

public interface StudentService {
    public Student add(Student student);

    public void delete(long studentId);

    public Optional<Student> get(long studentId);

    public List<Student> getStudentsWithCourse(String courseName);

    public void addCourseForStudent(Student student, long courseId);

    public void removeCourseForStudent(Student student, long courseId);
}
