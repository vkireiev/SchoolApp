package ua.foxmided.foxstudent103852.schoolapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;

public class FileService {
    public List<String> readFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new DataProcessingException("Can't read file: " + filePath, e);
        }
    }
}
