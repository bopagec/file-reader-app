package esg.global.filereaderapp;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.*;
import java.net.http.HttpHeaders;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class FileReaderAppApplication implements CommandLineRunner {

	@Value("${filePath}")
	private String filePath;

	private final WebClient webClient;

	public static void main(String[] args) {
		SpringApplication.run(FileReaderAppApplication.class, args);
	}

	@Override
	public void run(String... args){
		List<Customer> recordList = readCSVFile(filePath);

		for(Customer customer : recordList) {
			Customer customerMono = webClient.post()
					.uri("/customer/save")
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(customer)
					.retrieve()
					.bodyToMono(Customer.class)
							.block();
			log.info("customer successfully created! {}", customerMono);
		}

	}
	public static List<Customer> readCSVFile(String filePath) {
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
