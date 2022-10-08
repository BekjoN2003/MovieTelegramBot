package com.example.moviebot.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Login {
    @Email(message = "This is not email type")
    @NotBlank(message = "Email cannot be null and empty")
    private String email;
    @Size(min = 8, message = "Minimum size for password 8")
    @NotBlank(message = "Password cannot be null and empty")
    private String password;
}
