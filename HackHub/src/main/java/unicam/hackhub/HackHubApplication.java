package unicam.hackhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackHubApplication.class, args);
	}

}
