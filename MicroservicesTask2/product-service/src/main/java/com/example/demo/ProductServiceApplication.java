package com.example.demo;

import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.command.api.exception.ProductServiceEventsErrorHandler;

@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	public void configure(EventProcessingConfigurer configurer) {
		configurer.registerListenerInvocationErrorHandler("products",
				configuration -> new ProductServiceEventsErrorHandler());
	}
	
//	@Bean
//	public CommandGateway commandGateway() {
//		return  new CommandGateway();
//	}
}
