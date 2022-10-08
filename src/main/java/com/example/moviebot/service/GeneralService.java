package com.example.moviebot.service;

import com.example.moviebot.utill.CurrentMessage;
import com.example.moviebot.utill.MessageType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralService {


    public CurrentMessage handleStart(Long chatId){
        CurrentMessage currentMessage= new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Xush kelibsiz Movie botiga");
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }

    public CurrentMessage handleHelp(Long chatId){
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("How can we help you!");
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }

    public ReplyKeyboard getStartKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        return  null;
    }

    public ReplyKeyboard getHelpKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        return  null;
    }
}
