package ua.foxmided.foxstudent103852.schoolapp.dao.interfaces;

import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.model.Group;

public interface GroupDao {
    public Group add(Group group);

    public Optional<Group> get(Long id);

    public List<Group> getAll();

    public List<Group> getGroupsWithFewerStudents(long minStudentsQuantity);
}
