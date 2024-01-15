package ua.foxmided.foxstudent103852.schoolapp.model;

import java.util.Objects;

public class Group {
    private Long id;
    private String name;

    public Group(String name) {
        this.name = name;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
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
        Group other = (Group) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return " * " + name + " (ID = " + id + ")";
    }
}
