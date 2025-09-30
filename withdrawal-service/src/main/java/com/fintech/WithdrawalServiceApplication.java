package com.fintech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class WithdrawalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithdrawalServiceApplication.class, args);
	}

}
