package com.example.moviebot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Long chatId;
    private String token;
    private Boolean status;

    private String password;
}
