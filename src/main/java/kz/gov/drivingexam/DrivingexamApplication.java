package kz.gov.drivingexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DrivingexamApplication {
	public static void main(String[] args) {
		SpringApplication.run(DrivingexamApplication.class, args);
	}
}