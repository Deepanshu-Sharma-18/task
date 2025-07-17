package com.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TaskApplication {


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TaskApplication.class, args);
	}

}
