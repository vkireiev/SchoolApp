package ua.foxmided.foxstudent103852.schoolapp.model;

import java.util.Objects;

public class Course {
    private Long id;
    private String name;
    private String description;

    public Course(Long id) {
        this.id = id;
    }

    public Course(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return " * " + name + ", " + description + " (ID = " + id + ")";
    }
}
