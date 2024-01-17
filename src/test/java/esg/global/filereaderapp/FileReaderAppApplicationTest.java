package esg.global.filereaderapp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderAppApplicationTest {

    @Test
    @DisplayName("given csv  when reads  then returns correct customer list")
    public void givenCSVFile_whenReadsCSVFile_thenReturnsCorrectListOfCustomers(){
        // given
        String validPath = "/Users/pohodhika.bopage/MyRepos/file-reader-app/customers.csv";
        // when
        List<Customer> actual = FileReaderAppApplication.readCSVFile(validPath);
        // then
        Assertions.assertThat(actual.size()).isEqualTo(4);
    }

    @Test()
    @DisplayName("given none existence file when reads then throws RuntimeException")
    public void givenNoneExistenceFile_whenReadsCSVFile_thenThrowsRuntimeException(){
        // given
        String invalidPath = "/Users/pohodhika.bopage/MyRepos/file-reader-app/wrong-file.csv";
        // when
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FileReaderAppApplication.readCSVFile(invalidPath));
        // then
        Assertions.assertThat(runtimeException.getMessage()).isEqualTo("file not found");
    }

    @Test
    @DisplayName("given invalid csv file_when reads csv file_then throws Exception")
    public void givenInvalidCSVFile_whenReadsCSVFile_thenThrowException(){
        // given
        String invalidCSV = "/Users/pohodhika.bopage/MyRepos/file-reader-app/HELP.md";
        // when
        assertThrows(RuntimeException.class, () ->FileReaderAppApplication.readCSVFile(invalidCSV));
        // then
    }

}