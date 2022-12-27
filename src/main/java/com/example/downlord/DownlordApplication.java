package com.example.downlord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DownlordApplication{

	public static void main(String[] args){
		SpringApplication.run(DownlordApplication.class, args);
	}

}
