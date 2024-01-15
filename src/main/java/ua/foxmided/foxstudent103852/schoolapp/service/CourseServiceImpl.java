package ua.foxmided.foxstudent103852.schoolapp.service;

import java.util.List;

import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.CourseDao;
import ua.foxmided.foxstudent103852.schoolapp.model.Course;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.CourseService;

public class CourseServiceImpl implements CourseService {
    private CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public List<Course> getAll() {
        return courseDao.getAll();
    }
}
