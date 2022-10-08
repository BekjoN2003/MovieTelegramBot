package com.example.moviebot.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MovieCreateDto {
    @NotBlank
    private String name;
    @NotEmpty
    private String description;

    private Integer creatorId;
}
