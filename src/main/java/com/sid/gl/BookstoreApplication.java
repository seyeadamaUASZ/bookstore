package com.sid.gl;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = "com.*")
public class BookstoreApplication{

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}


}
