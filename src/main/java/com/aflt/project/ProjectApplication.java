package com.aflt.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(ProjectApplication.class, args);
        }catch (Exception exception){
            System.out.println(exception.getMessage());

        }
    }

}
