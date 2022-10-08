package com.example.moviebot.response;

import com.example.moviebot.model.Movie;
import com.example.moviebot.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Integer id;
    private Movie movie;
    private User user;
    private String content;
    private LocalDateTime createdAt;
}
