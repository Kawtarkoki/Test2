package com.sentrysoftware.Test2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.sentrysoftware.Test2")
@ComponentScan(basePackages = {
		"com.sentrysoftware.Test2.processor.controllers",
		"com.sentrysoftware.Test2.processor"
})
public class Test2Application {
	public static void main(String[] args) {
		SpringApplication.run(Test2Application.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}



