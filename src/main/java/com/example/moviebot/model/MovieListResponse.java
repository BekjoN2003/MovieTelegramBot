package com.example.moviebot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieListResponse {
    private List<Movie> dtoList;
    private Long count;

}
