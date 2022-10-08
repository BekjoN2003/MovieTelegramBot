package com.example.moviebot.service;

import com.example.moviebot.model.User;
import com.example.moviebot.utill.CurrentMessage;
import com.example.moviebot.utill.MessageType;
import com.example.moviebot.utill.UserDataService;
import com.example.moviebot.utill.UserState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class BotService {
    private MovieService movieService;

    private GeneralService generalService;


    private UserDataService userDataService;
    private LibraryService libraryService;


    public CurrentMessage handle(Update update) {
        if (update.hasMessage()) {
            return handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            return handleCallbackQuery(update.getCallbackQuery());
        }
        return null;
    }
    private CurrentMessage handleMessage(Message message) {
        CurrentMessage currentMessage = new CurrentMessage();
        long chatId = message.getChatId();
        UserState state = userDataService.getUserState(chatId);
        if (state != null){
            if (state.equals(UserState.SING_UP_PROCESS)){
                SendMessage sendMessage =new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Input your password");
                userDataService.updateUserState(chatId, UserState.INPUT_PASSWORD);
                currentMessage.setSendMessage(sendMessage);
                currentMessage.setMessageType(MessageType.SEND_MESSAGE);
                return currentMessage;
            }
        }
        if (message.hasText()) {
            String inputMessage = message.getText();
            switch (inputMessage) {
                case "/start":
                    currentMessage = generalService.handleStart(chatId);
                    break;
                case "/help":
                    currentMessage = generalService.handleHelp(chatId);
                    break;
                case "/movies":
                    currentMessage = movieService.getAllMovies(chatId);
                    break;
                case "/history":
                    currentMessage = libraryService.getHistory(chatId);
                    break;
                case "/comment":

                    break;
                case "/rate":
                    break;
            }
        }
        return currentMessage;
    }


    private CurrentMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        CurrentMessage currentMessage = new CurrentMessage();
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        switch (data) {
            case "prev":
                currentMessage = movieService.prev(callbackQuery);
                break;
            case "next":
                currentMessage = movieService.next(callbackQuery);
                break;
        }
        if (data.startsWith("get_movie/")) {
            Integer movieId = Integer.valueOf(data.split("/")[1]);
            return movieService.getMovieById(movieId, chatId);
        }
        User userInfo = userDataService.getUserInfo(chatId);
        if (data.startsWith("/like/")) {
            if (userInfo == null) {
               return signUp(chatId);
            } else if (userInfo.getStatus() == false) {
               return singIn(chatId);
            }else {

            }
        }
        if (data.startsWith("/comment/")) {
            if (userInfo == null) {
               return signUp(chatId);
            } else if (userInfo.getStatus() == false) {
               return singIn(chatId);
            }else {

            }
        }
        return currentMessage;
    }


    private CurrentMessage singIn(Long chatId) {
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        userDataService.updateUserState(chatId, UserState.LOGIN_PROCESS);
        sendMessage.setText("Login process\n please input your email");
        sendMessage.setChatId(chatId);
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }

    private CurrentMessage signUp(Long chatId) {
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        userDataService.updateUserState(chatId,UserState.SING_UP_PROCESS);
        sendMessage.setText("Sign-Up process\n Please input your email");
        sendMessage.setChatId(chatId);
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }

    private boolean checkStatus(Long chatId) {
        User user = userDataService.getUserInfo(chatId);
        if (user == null || !user.getStatus()) {
            return false;
        }
        return true;
    }
}
