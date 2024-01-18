package esg.global.filereaderapp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class FileReaderAppApplication implements CommandLineRunner {
	private final WebClient webClient;

	@Value("${filePath}")
	private String filePath;
	public static void main(String[] args) {
		SpringApplication.run(FileReaderAppApplication.class, args);
	}

	@Override
	public void run(String... args){
		if(filePath == null || filePath.isBlank()){
			log.error("file name not provided");
			return;
		};

		log.info("file name: {}", filePath);

		List<Customer> recordList = readCSVFile(filePath);

		for(Customer customer : recordList) {
			ResponseEntity<Customer> response = sendCustomerData(customer);

			if(response.getStatusCode().is2xxSuccessful()) {
				log.info("customer successfully created! {}", response.getBody());
			}else{
				log.error("customer not created. response code {}", response.getStatusCode());
			}
		}
	}

	public ResponseEntity<Customer> sendCustomerData(Customer customer){
		return webClient.post()
				.uri("/customer/save")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(customer)
				.retrieve()
				.toEntity(Customer.class)
				.block();
	}

	static List<Customer> readCSVFile(String filePath) {
		try (Reader reader = new FileReader(filePath);
			 CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT)) {

			return  parser.stream().skip(1)
					.map(record ->
							new Customer(
									record.get(0),
									record.get(1),
									record.get(2),
									record.get(3),
									record.get(4),
									record.get(5),
									record.get(6),
									record.get(7)))
					.toList();

		} catch (FileNotFoundException e) {
			log.error("File not found: {}", filePath);
			throw new RuntimeException("file not found", e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
