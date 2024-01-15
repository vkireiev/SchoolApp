package ua.foxmided.foxstudent103852.schoolapp;

import ua.foxmided.foxstudent103852.schoolapp.dao.CourseDaoImpl;
import ua.foxmided.foxstudent103852.schoolapp.dao.GroupDaoImpl;
import ua.foxmided.foxstudent103852.schoolapp.dao.SqlFileExecutor;
import ua.foxmided.foxstudent103852.schoolapp.dao.StudentDaoImpl;
import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.CourseDao;
import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.GroupDao;
import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.StudentDao;
import ua.foxmided.foxstudent103852.schoolapp.service.CourseServiceImpl;
import ua.foxmided.foxstudent103852.schoolapp.service.GroupServiceImpl;
import ua.foxmided.foxstudent103852.schoolapp.service.MenuService;
import ua.foxmided.foxstudent103852.schoolapp.service.StudentServiceImpl;
import ua.foxmided.foxstudent103852.schoolapp.service.TestDataGenerator;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.StudentService;

public class Main {
    public static void main(String[] args) {
        CourseDao courseDao = new CourseDaoImpl();
        GroupDao groupDao = new GroupDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        SqlFileExecutor sqlFileExecutor = new SqlFileExecutor();
        TestDataGenerator testDataGenerator = new TestDataGenerator(courseDao, groupDao, studentDao, sqlFileExecutor);
        testDataGenerator.generateTestData();
        CourseService courseService = new CourseServiceImpl(courseDao);
        GroupService groupService = new GroupServiceImpl(groupDao);
        StudentService studentService = new StudentServiceImpl(studentDao);
        MenuService menuService = new MenuService(courseService, groupService, studentService);
        menuService.openMenu();
    }
}
