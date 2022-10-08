package com.example.moviebot.service;

import com.example.moviebot.utill.CurrentMessage;
import com.example.moviebot.utill.UserDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class LibraryService {
    private final UserDataService userDataService;
    private final UserService userService;

    public LibraryService(UserDataService userDataService, UserService userService) {
        this.userDataService = userDataService;
        this.userService = userService;
    }

    public CurrentMessage getHistory(long chatId) {
        Integer userId = userDataService.getUserInfo(chatId).getId();
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        RestTemplate restTemplate = new RestTemplate();

        return null;
    }

//    public CurrentMessage watchMovie(Long chatId, Integer movieId) {
//        User user = userDataService.getUserInfo(chatId);
//        User u = userService.getUserByEmail(user.getEmail());
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<>
//    }
}
