package Sword.Group.FirstTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@EntityScan("model")
//@EnableJpaRepositories(basePackages = ("Sword.Group.FirstTask.repository"))
public class FirstTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstTaskApplication.class, args);
	}

}
