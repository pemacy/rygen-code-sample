package dev.rygen.intersectionlightcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IntersectionLightControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntersectionLightControllerApplication.class, args);
	}

}
