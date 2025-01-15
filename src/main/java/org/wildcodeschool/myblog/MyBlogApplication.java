package org.wildcodeschool.myblog;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBlogApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        String dbHost = dotenv.get("DB_HOST");
        String dbPort = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");
        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");

        if (dbUsername == null || dbHost == null || dbPassword == null || dbPort == null || dbName == null) {
            throw new IllegalStateException("One or more required environment variables are missing in the .env file");
        }

        System.setProperty("DB_HOST", dbHost);
        System.setProperty("DB_PORT", dbPort);
        System.setProperty("DB_NAME", dbName);
        System.setProperty("DB_USERNAME", dbUsername);
        System.setProperty("DB_PASSWORD", dbPassword);

        SpringApplication.run(MyBlogApplication.class, args);
    }

}
