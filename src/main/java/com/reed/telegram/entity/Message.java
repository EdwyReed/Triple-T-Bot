package com.reed.telegram.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String chatId;

    @Column(name = "user_message")
    @Getter
    @Setter
    private String userMessage;

    @Column(name = "bot_message", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String botMessage;
}
