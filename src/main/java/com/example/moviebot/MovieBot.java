package com.example.moviebot;

import com.example.moviebot.service.BotService;
import com.example.moviebot.utill.CurrentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MovieBot extends TelegramLongPollingBot {

    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.username}")
    private String username;
    @Autowired
    private BotService botService;


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
       CurrentMessage currentMessage = botService.handle(update);
       if (currentMessage != null && currentMessage .getMessageType() != null){
           executeMessage(currentMessage);
       }
    }

    public void executeMessage(CurrentMessage currentMessage) {
        try {
            switch (currentMessage.getMessageType()) {
                case SEND_MESSAGE:
                    execute(currentMessage.getSendMessage());
                    break;
                case SEND_PHOTO:
                    execute(currentMessage.getSendPhoto());
            }
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}
