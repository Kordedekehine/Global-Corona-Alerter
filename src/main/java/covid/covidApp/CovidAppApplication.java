package covid.covidApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovidAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(CovidAppApplication.class, args);
		System.out.println("Finished running Covid application");
	}

}
