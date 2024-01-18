package esg.global.filereaderapp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileReaderAppApplicationTest {

    @Mock
    private static WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Mono<ResponseEntity<Customer>> monoResponse;

    @Test
    public void testSendCustomerRecord(){
        // given
        Customer customer = new Customer(
                "ref-jd123",
                "John Deen",
                "No: 21",
                "Whitton Road",
                "Whitton",
                "London",
                "UK",
                "TW2 7AB");
        FileReaderAppApplication app = new FileReaderAppApplication(webClient);
        ResponseEntity<Customer> responseEntity = new ResponseEntity<>(customer, HttpStatus.OK);

        // when
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(customer)).thenReturn(requestHeadersSpec);
        when(requestBodyUriSpec.uri("/customer/save")).thenReturn(requestBodySpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(Customer.class)).thenReturn(monoResponse);
        when(monoResponse.block()).thenReturn(responseEntity);


        ResponseEntity<Customer> actualResponse = app.sendCustomerData(customer);

        // then
        Assertions.assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResponse.getBody().customerRef()).isEqualTo("ref-jd123");

    }
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