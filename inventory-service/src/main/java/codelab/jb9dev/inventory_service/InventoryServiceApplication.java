package codelab.jb9dev.inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	// TODO: Add bean to add inventory if in dev environment and if there isn't any added
	// the idea is using CommandLineRunner as the return type for a public method annotated with @Bean, that returns
	// a lambda, which ignores the args. InventoryService is received by the method parameter
}
