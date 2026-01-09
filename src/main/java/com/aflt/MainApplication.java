package com.aflt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.aflt.store.entity",
        "com.aflt.order.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.aflt.store.repository",
        "com.aflt.order.repository"
})
public class MainApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(MainApplication.class, args);
        }catch (Exception exception){
            System.out.println(exception.getMessage());

        }

    }

}
