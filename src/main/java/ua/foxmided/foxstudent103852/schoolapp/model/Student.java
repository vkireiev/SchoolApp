package ua.foxmided.foxstudent103852.schoolapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Student {
    private Long id;
    private Group group;
    private String lastName;
    private String firstName;
    private List<Course> courses;

    public Student(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
        courses = new ArrayList<>();
    }

    public Student(Group group, String lastName, String firstName, List<Course> courses) {
        this.group = group;
        this.lastName = lastName;
        this.firstName = firstName;
        this.courses = courses;
    }

    public Student(Long id, Group group, String lastName, String firstName, List<Course> courses) {
        this.id = id;
        this.group = group;
        this.lastName = lastName;
        this.firstName = firstName;
        this.courses = courses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Group getGroup() {
        return this.group;
    }

    public List<Course> getCourses() {
        return this.courses;
    }

    public Long getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Student: (ID = " + id + ") ");
        result.append(lastName + " " + firstName + ", group = ");
        if (group != null) {
            result.append(group.getName());
        } else {
            result.append("n/a");
        }
        result.append(", courses: [");
        result.append(courses.stream()
                .map(course -> course.getName() + " (ID = " + course.getId() + ")")
                .collect(Collectors.joining(", ")));
        result.append("]");
        return result.toString();
    }
}
