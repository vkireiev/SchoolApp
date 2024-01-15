package ua.foxmided.foxstudent103852.schoolapp.service;

import java.util.List;

import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.GroupDao;
import ua.foxmided.foxstudent103852.schoolapp.model.Group;
import ua.foxmided.foxstudent103852.schoolapp.service.interfaces.GroupService;

public class GroupServiceImpl implements GroupService {
    private GroupDao groupDao;

    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    @Override
    public List<Group> getGroupsWithFewerStudents(long minStudentsQuantity) {
        return groupDao.getGroupsWithFewerStudents(minStudentsQuantity);
    }
}
