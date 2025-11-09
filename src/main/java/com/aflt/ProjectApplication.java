package com.aflt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.aflt.store.entity")
public class ProjectApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(ProjectApplication.class, args);
        }catch (Exception exception){
            System.out.println(exception.getMessage());

        }

    }

}
