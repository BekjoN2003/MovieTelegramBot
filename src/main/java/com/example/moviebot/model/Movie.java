package com.example.moviebot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Movie {
    private Integer id;
    private String name;
    private String description;
    private User user;

}
