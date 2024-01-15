package ua.foxmided.foxstudent103852.schoolapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import ua.foxmided.foxstudent103852.schoolapp.dao.SqlFileExecutor;
import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.CourseDao;
import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.GroupDao;
import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.StudentDao;
import ua.foxmided.foxstudent103852.schoolapp.model.Course;
import ua.foxmided.foxstudent103852.schoolapp.model.Group;
import ua.foxmided.foxstudent103852.schoolapp.model.Student;

public class TestDataGenerator {
    private static final String DROP_SQL_FILE = "src/main/resources/drop_tables.sql";
    private static final String CREATE_SQL_FILE = "src/main/resources/create_tables.sql";
    private static final String COURSES_SQL_FILE = "src/main/resources/courses.sql";
    private static final String LAST_NAMES_FILE = "src/main/resources/last_names.txt";
    private static final String FIRST_NAMES_FILE = "src/main/resources/first_names.txt";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final int GROUPS_COUNT = 10;
    private static final int MIN_STUDENTS_IN_GROUP = 10;
    private static final int MAX_STUDENTS_IN_GROUP = 30;
    private static final int STUDENTS_COUNT = 200;
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 3;
    private ThreadLocalRandom random = ThreadLocalRandom.current();
    private CourseDao courseDao;
    private GroupDao groupDao;
    private StudentDao studentDao;
    private SqlFileExecutor sqlFileExecutor;

    public TestDataGenerator(CourseDao courseDao, GroupDao groupDao, StudentDao studentDao,
            SqlFileExecutor sqlFileExecutor) {
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.sqlFileExecutor = sqlFileExecutor;
    }

    public void generateTestData() {
        initDbStructure();
        Set<Group> groups = getRandomGroups();
        groups.forEach(groupDao::add);
        sqlFileExecutor.executeSqlFile(COURSES_SQL_FILE);
        List<Course> courses = courseDao.getAll();
        Set<Student> students = getRandomStudents(courses, groups);
        students.forEach(studentDao::add);
        System.out.println("Random data saved successfully.");
    }

    private void initDbStructure() {
        sqlFileExecutor.executeSqlFile(DROP_SQL_FILE);
        System.out.println("Tables droped successfully.");
        sqlFileExecutor.executeSqlFile(CREATE_SQL_FILE);
        System.out.println("Tables created successfully.");
    }

    private Set<Group> getRandomGroups() {
        Set<Group> result = new HashSet<>();
        while (result.size() < GROUPS_COUNT) {
            String group = "";
            group += CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            group += CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            group += "-";
            group += DIGITS.charAt(random.nextInt(DIGITS.length()));
            group += DIGITS.charAt(random.nextInt(DIGITS.length()));
            result.add(new Group(group));
        }
        return result;
    }

    private Set<Student> getRandomStudents(List<Course> courses, Set<Group> groups) {
        FileService fileService = new FileService();
        List<String> lastNames = fileService.readFile(LAST_NAMES_FILE);
        List<String> firstNames = fileService.readFile(FIRST_NAMES_FILE);
        Set<Student> result = new HashSet<>();
        while (result.size() < STUDENTS_COUNT) {
            result.add(new Student(lastNames.get(random.nextInt(lastNames.size())),
                    firstNames.get(random.nextInt(firstNames.size()))));
        }
        result = assignCoursesForStudents(result, courses);
        result = assignGroupsForStudents(result, groups);
        return result;
    }

    private Set<Student> assignCoursesForStudents(Set<Student> students, List<Course> courses) {
        students.forEach(student -> {
            Collections.shuffle(courses);
            student.setCourses(courses.stream()
                    .limit(random.nextInt(MIN_COURSES_PER_STUDENT, (MAX_COURSES_PER_STUDENT + 1)))
                    .collect(Collectors.toList()));
        });
        return students;
    }

    private Set<Student> assignGroupsForStudents(Set<Student> students, Set<Group> groups) {
        List<Student> studentsCopy = new ArrayList<>(students);
        Collections.shuffle(studentsCopy);
        for (Group group : groups) {
            int studentsInGroup = random.nextInt((MIN_STUDENTS_IN_GROUP - 1), (MAX_STUDENTS_IN_GROUP + 1));
            if (studentsInGroup >= MIN_STUDENTS_IN_GROUP && studentsCopy.size() >= studentsInGroup) {
                for (int i = 0; i < studentsInGroup; i++) {
                    Student student = studentsCopy.remove(0);
                    student.setGroup(group);
                }
            }
        }
        return students;
    }
}
