package ua.foxmided.foxstudent103852.schoolapp.service.interfaces;

import java.util.List;

import ua.foxmided.foxstudent103852.schoolapp.model.Group;

public interface GroupService {
    List<Group> getAll();

    List<Group> getGroupsWithFewerStudents(long minStudentsQuantity);
}
