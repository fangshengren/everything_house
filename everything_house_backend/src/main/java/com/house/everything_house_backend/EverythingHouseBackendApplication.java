package com.house.everything_house_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("")
public class EverythingHouseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EverythingHouseBackendApplication.class, args);
	}

}
