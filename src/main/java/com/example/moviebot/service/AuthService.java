package com.example.moviebot.service;

import com.example.moviebot.model.Login;
import com.example.moviebot.model.LoginResult;
import com.example.moviebot.model.SignUp;
import com.example.moviebot.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    public User login(User user) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/v1/auth/login/";
        Login login = new Login();
        login.setEmail(user.getEmail());
        login.setPassword(user.getPassword());
        ResponseEntity<LoginResult> response = restTemplate.postForEntity(url, login, LoginResult.class);
        LoginResult loginResult = response.getBody();
        assert loginResult != null;
        String token = loginResult.getToken();
        user.setToken(token);
        return user;
    }

    public String signUp(User user) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/v1/auth/sign-up/";
        SignUp signUp = new SignUp();
        signUp.setName(user.getName());
        signUp.setAge(user.getAge());
        signUp.setEmail(user.getEmail());
        signUp.setPassword(user.getPassword());
        ResponseEntity<String> response = restTemplate.postForEntity(url, signUp, String.class);
        String result = response.getBody();
        return result;
    }
}
