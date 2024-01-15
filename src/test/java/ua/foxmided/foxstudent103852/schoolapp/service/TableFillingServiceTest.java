package ua.foxmided.foxstudent103852.schoolapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class TableFillingServiceTest {
    @BeforeEach
    void init() {
    }

    @Order(1)
    @Test
    void dropTablesFromFile_WhenFailToReadSQLFile_ThenRuntimeException() {
        String fileName = "file_not_exists.txt";
        FileService fileService = new FileService();
        RuntimeException objException = Assertions.assertThrows(RuntimeException.class,
                () -> fileService.readFile(fileName), "RuntimeException was expected");
        Assertions.assertEquals("Can't read file: " + fileName, objException.getMessage());
    }
}
