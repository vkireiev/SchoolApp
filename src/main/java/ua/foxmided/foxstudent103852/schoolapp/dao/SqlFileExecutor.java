package ua.foxmided.foxstudent103852.schoolapp.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

import org.apache.ibatis.jdbc.ScriptRunner;

import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolapp.util.ConnectionUtil;

public class SqlFileExecutor {
    public void executeSqlFile(String fileName) {
        Connection connection = ConnectionUtil.getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
        scriptRunner.setStopOnError(true);
        try (Reader reader = new BufferedReader(new FileReader(fileName))) {
            scriptRunner.runScript(reader);
        } catch (IOException e) {
            throw new DataProcessingException("Unable to execute SQL-query in " + fileName, e);
        }
    }
}
