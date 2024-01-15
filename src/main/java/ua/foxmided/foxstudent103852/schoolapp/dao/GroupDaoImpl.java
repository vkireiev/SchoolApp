package ua.foxmided.foxstudent103852.schoolapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolapp.dao.interfaces.GroupDao;
import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolapp.model.Group;
import ua.foxmided.foxstudent103852.schoolapp.util.ConnectionUtil;

public class GroupDaoImpl implements GroupDao {
    @Override
    public Group add(Group group) {
        String addGroupSQL = "INSERT INTO groups(name) VALUES(?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(addGroupSQL,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, group.getName());
            if ((statement.executeUpdate() != 0) && (statement.getGeneratedKeys().next())) {
                group.setId(statement.getGeneratedKeys().getObject(1, Long.class));
                return group;
            }
            throw new DataProcessingException("Unable to add group to database: " + group);
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to add group to database: " + group, e);
        }
    }

    @Override
    public Optional<Group> get(Long id) {
        String getGroupSQL = "SELECT * FROM groups "
                + "WHERE id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getGroupSQL)) {
            Group group = null;
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                group = getGroupFromResultSet(resultSet);
            }
            return Optional.ofNullable(group);
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get group (id = " + id + ") from database", e);
        }
    }

    @Override
    public List<Group> getAll() {
        List<Group> result = new ArrayList<>();
        String getGroupsSQL = "SELECT * FROM groups;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getGroupsSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(getGroupFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get groups from database", e);
        }
        return result;
    }

    @Override
    public List<Group> getGroupsWithFewerStudents(long minStudentsQuantity) {
        List<Group> result = new ArrayList<>();
        String getGroupsSQL = "SELECT gr.*, COUNT(st.id) "
                + "FROM groups AS gr "
                + "LEFT JOIN students AS st ON gr.id = st.group_id "
                + "GROUP BY gr.id "
                + "HAVING COALESCE(COUNT(st.id), 0) <= ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getGroupsSQL)) {
            statement.setLong(1, minStudentsQuantity);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(getGroupFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to get groups from database", e);
        }
        return result;
    }

    private Group getGroupFromResultSet(ResultSet resultSet) throws SQLException {
        Long groupId = resultSet.getObject("id", Long.class);
        String groupName = resultSet.getString("name");
        return new Group(groupId, groupName);
    }
}
