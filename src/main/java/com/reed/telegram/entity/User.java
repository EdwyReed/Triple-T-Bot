package com.reed.telegram.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank
    @Getter
    @Setter
    private String username;

    @Column(name = "last_message_at", nullable = false)
    @NotNull
    @Getter
    @Setter
    private Integer lastMessageAt;

    @Column(name = "chat_id", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String chatId;

    public User (String firstName, String lastName, String username, String chatId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.chatId = chatId;
    }

    public User (String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public User() {

    }
}
