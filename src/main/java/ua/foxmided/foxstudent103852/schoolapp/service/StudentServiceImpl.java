package ua.foxmided.foxstudent103852.schoolapp.service;

import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.StudentDao;
import ua.foxmided.foxstudent103852.schoolapp.model.Course;
import ua.foxmided.foxstudent103852.schoolapp.model.Student;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.StudentService;

public class StudentServiceImpl implements StudentService {
    StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student add(Student student) {
        return studentDao.add(student);
    }

    @Override
    public void delete(long studentId) {
        studentDao.delete(studentId);
    }

    @Override
    public Optional<Student> get(long studentId) {
        return studentDao.get(studentId);
    }

    @Override
    public List<Student> getStudentsWithCourse(String courseName) {
        return studentDao.getStudentsWithCourse(courseName);
    }

    @Override
    public void addCourseForStudent(Student student, long courseId) {
        student.getCourses().add(new Course(courseId));
        studentDao.update(student);
    }

    @Override
    public void removeCourseForStudent(Student student, long courseId) {
        student.getCourses().removeIf(course -> course.getId() == courseId);
        studentDao.update(student);
    }
}
