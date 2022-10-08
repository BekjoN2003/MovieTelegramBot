package com.example.moviebot.service;

import com.example.moviebot.model.Comment;
import com.example.moviebot.model.Movie;
import com.example.moviebot.model.User;
import com.example.moviebot.response.CommentResponse;
import com.example.moviebot.utill.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;


@Service
public class CommentService {
    private final UserDataService userDataService;
    private final UserService userService;

    Integer movieId;
    @Value("${movieService.url}")
    private String Url;

    public CommentService(UserDataService userDataService, UserService userService) {
        this.userDataService = userDataService;
        this.userService = userService;
    }

    public CurrentMessage commentProcess(Long chatId, Integer movieId) {
        this.movieId = movieId;
        userDataService.updateUserState(chatId, UserState.INPUT_COMMENT);
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please, input your comment");
        sendMessage.setChatId(chatId);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        currentMessage.setSendMessage(sendMessage);
        return currentMessage;
    }

    public CurrentMessage setComment(Message message) {
        Long chatId = message.getChatId();
        User user = userDataService.getUserInfo(chatId);
        Integer userId = userService.getUserByEmail(user.getEmail()).getId();
        Comment comment = new Comment();
        comment.setContent(message.getText());
        comment.setMovieId(movieId);
        comment.setUserId(userId);
        String url = Url + "comment/create/";
        HttpEntity<Comment> entity = new HttpEntity<>(comment, HeaderUtil.headers(user.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(response.getBody());
        sendMessage.setChatId(chatId);
        CurrentMessage currentMessage = new CurrentMessage();
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        userDataService.updateUserState(chatId, UserState.INPUT_DATA);
        return currentMessage;
    }

    public CurrentMessage getByUser(Long chatId) {
        User user = userDataService.getUserInfo(chatId);
        String url = Url + "comment/getMyComments/";
        HttpEntity<Void> entity = new HttpEntity<>(HeaderUtil.headers(user.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CommentResponse[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, CommentResponse[].class);
        SendMessage sendMessage = new SendMessage();
        if(response.getStatusCodeValue() != 200){
            sendMessage.setText(response.getBody().toString());
        }else {
            List<CommentResponse> commentList = List.of(response.getBody());
            sendMessage.setParseMode("HTML");
            sendMessage.setText("<b>Your comments: </b>" + commentList.size());
            for (CommentResponse comment:commentList) {
                Movie movie = comment.getMovie();
                sendMessage.setText(sendMessage.getText() + "\n\n"
                        + "<b>1) Time: </b>" + comment.getCreatedAt()
                        + "\n<b>Movie - </b>" + movie.getName()
                        + "\n<b>Comment - </b>" + comment.getContent());
            }
        }
        sendMessage.setChatId(chatId);
        CurrentMessage currentMessage = new CurrentMessage();
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }
}

