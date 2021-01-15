package com.reed.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

@SpringBootApplication
@PropertySource(value = "classpath:application.yaml")
public class App {

    public static void main(String[] args) throws TelegramApiException, SQLException {
        SpringApplication.run(App.class, args);
    }
}
