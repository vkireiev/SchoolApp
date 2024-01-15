package ua.foxmided.foxstudent103852.schoolapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolapp.model.Course;
import ua.foxmided.foxstudent103852.schoolapp.model.Group;
import ua.foxmided.foxstudent103852.schoolapp.model.Student;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.StudentService;

public class MenuService {
    private CourseService courseService;
    private GroupService groupService;
    private StudentService studentService;

    public MenuService(CourseService courseService, GroupService groupService, StudentService studentService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentService = studentService;
    }

    public void openMenu() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String response;
            while (!(response = getResponseInMainMenu(reader)).equalsIgnoreCase("exit")) {
                if (response.equalsIgnoreCase("a")) {
                    printGroupsWithFewerStudents(reader);
                }
                if (response.equalsIgnoreCase("b")) {
                    printStudentsWithCourse(reader);
                }
                if (response.equalsIgnoreCase("c")) {
                    addNewStudent(reader);
                }
                if (response.equalsIgnoreCase("d")) {
                    deleteStudentById(reader);
                }
                if (response.equalsIgnoreCase("e")) {
                    addCourseForStudent(reader);
                }
                if (response.equalsIgnoreCase("f")) {
                    removeCourseForStudent(reader);
                }
                getResponseToReturnInMainMenu(reader);
            }
            System.out.println("Goodbye!");
        } catch (IOException e) {
            throw new DataProcessingException(e.getMessage(), e);
        }
    }

    private void printMainMenu() {
        System.out.println("Main menu:");
        System.out.println("a. Find all groups with less or equal students’ number");
        System.out.println("b. Find all students related to the course with the given name");
        System.out.println("c. Add a new student");
        System.out.println("d. Delete a student by the STUDENT_ID");
        System.out.println("e. Add a student to the course (from a list)");
        System.out.println("f. Remove the student from one of their courses");
    }

    private void getResponseToReturnInMainMenu(BufferedReader reader) throws IOException {
        System.out.println("Press enter to return in main menu");
        String response = reader.readLine();
    }

    private String readString(String message, BufferedReader reader) throws IOException {
        String result = "";
        while (result.length() == 0) {
            System.out.println(message);
            result = reader.readLine();
        }
        return result;
    }

    private long readNumber(String message, BufferedReader reader) throws IOException {
        long result;
        while (true) {
            try {
                System.out.println(message);
                result = Long.parseLong(reader.readLine());
                return result;
            } catch (NumberFormatException e) {
                System.out.println("The entered value must be an integer");
            }
        }
    }

    private long readNumber(String message, BufferedReader reader, Set<Long> values) throws IOException {
        if (!values.isEmpty()) {
            while (true) {
                Long result = readNumber(message, reader);
                if (values.contains(result)) {
                    return result;
                } else {
                    System.out.println("The entered value must be from the list " + values + ". Try againt");
                }
            }
        }
        return readNumber(message, reader);
    }

    private void printStudents(List<Student> students) {
        System.out.println("Students [" + students.size() + "]:");
        students.forEach(System.out::println);
    }

    private void printGroups(List<Group> groups) {
        System.out.println("Groups [" + groups.size() + "]:");
        groups.forEach(System.out::println);
    }

    private void printCourses(List<Course> courses) {
        System.out.println("Courses [" + courses.size() + "]: ");
        courses.forEach(System.out::println);
    }

    private String getResponseInMainMenu(BufferedReader reader) throws IOException {
        Set<String> menuResponses = new HashSet<>(Arrays.asList("exit", "a", "b", "c", "d", "e", "f"));
        while (true) {
            printMainMenu();
            String response = readString("Select the menu item by typing the appropriate letter or type exit", reader);
            if (menuResponses.contains(response)) {
                return response;
            }
            System.out.println("Select the correct menu item [a, b, c, d, e, f] or type exit!");
        }
    }

    private void printGroupsWithFewerStudents(BufferedReader reader) throws IOException {
        System.out.println(">> a. Find all groups with less or equal students’ number");
        long minStudentsQuantity = readNumber("Enter the number of students in the group", reader);
        printGroups(groupService.getGroupsWithFewerStudents(minStudentsQuantity));
    }

    private void printStudentsWithCourse(BufferedReader reader) throws IOException {
        System.out.println(">> b. Find all students related to the course with the given name");
        printCourses(courseService.getAll());
        String courseName = readString("Enter course name", reader);
        printStudents(studentService.getStudentsWithCourse(courseName));
    }

    private void addNewStudent(BufferedReader reader) throws IOException {
        System.out.println(">> c. Add a new student");
        String lastName = readString("Enter last name: ", reader);
        String firstName = readString("Enter first name: ", reader);
        Student newStudent = new Student(lastName, firstName);
        newStudent = studentService.add(newStudent);
        System.out.println("Student added to database successfully.");
        System.out.println(newStudent);
    }

    private void deleteStudentById(BufferedReader reader) throws IOException {
        System.out.println(">> d. Delete a student by the STUDENT_ID");
        long studentId = readNumber("Enter STUDENT_ID", reader);
        Optional<Student> student = studentService.get(studentId);
        if (student.isPresent()) {
            System.out.println(student.get());
            studentService.delete(studentId);
            System.out.println("Student with STUDENT_ID = " + studentId + " deleted successfully.");
        } else {
            System.out.println("Student with STUDENT_ID = " + studentId + " not exists.");
        }
    }

    private void addCourseForStudent(BufferedReader reader) throws IOException {
        System.out.println(">> e. Add a student to the course (from a list)");
        long studentId = readNumber("Enter STUDENT_ID", reader);
        Optional<Student> student = studentService.get(studentId);
        if (student.isPresent()) {
            System.out.println(student.get());
            List<Course> courses = courseService.getAll();
            courses.removeAll(student.get().getCourses());
            printCourses(courses);
            Set<Long> coursesId = courses.stream().map(Course::getId).collect(Collectors.toSet());
            long courseId = readNumber("Enter COURSE_ID you want to add for the student", reader, coursesId);
            studentService.addCourseForStudent(student.get(), courseId);
            System.out.println("The student has been successfully assigned to this course.");
            System.out.println(studentService.get(studentId).get());
        } else {
            System.out.println("Student with STUDENT_ID = " + studentId + " not exists.");
        }
    }

    private void removeCourseForStudent(BufferedReader reader) throws IOException {
        System.out.println(">> f. Remove the student from one of their courses");
        long studentId = readNumber("Enter STUDENT_ID", reader);
        Optional<Student> student = studentService.get(studentId);
        if (student.isPresent()) {
            System.out.println(student.get());
            List<Course> courses = student.get().getCourses();
            printCourses(courses);
            Set<Long> coursesId = courses.stream().map(Course::getId).collect(Collectors.toSet());
            long courseId = readNumber("Enter COURSE_ID you want to remove for the student", reader, coursesId);
            studentService.removeCourseForStudent(student.get(), courseId);
            System.out.println("The student has been successfully removed from this course.");
            System.out.println(studentService.get(studentId).get());
        } else {
            System.out.println("Student with STUDENT_ID = " + studentId + " not exists.");
        }
    }
}
