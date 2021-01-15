package com.reed.telegram.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "domains")
@ToString
@JsonIgnoreProperties
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "domainname", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String domainname;

    @Column(name = "hotness", nullable = false)
    @NotNull
    @Getter
    @Setter
    private int hotness;

    @Column(name = "price", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private int price;

    @Column(name = "x_value", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private int x_value;

    @Column(name = "yandex_tic", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private int yandex_tic;

    @Column(name = "links", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private int links;

    @Column(name = "visitors", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private int visitors;

    @Column(name = "registrar", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String registrar;

    @Column(name = "old", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private float old;

    @Column(name = "delete_date", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private String delete_date;

    @Column(name = "rkn", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private boolean rkn;

    @Column(name = "judicial", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private boolean judicial;

    @Column(name = "block", nullable = false)
    @NotBlank
    @Getter
    @Setter
    private boolean block;
}
