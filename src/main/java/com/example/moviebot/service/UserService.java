package com.example.moviebot.service;


import com.example.moviebot.model.User;
import org.jvnet.hk2.annotations.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    public User getUserByEmail(String email){
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:8080/api/v1/users/getByEmail/%s", email);
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
        return response.getBody();
    }
}
