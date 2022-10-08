package com.example.moviebot.utill;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@Setter
public class CurrentMessage {
    private SendMessage sendMessage;
    // SendPhoto, SendCallBack, SendFile
   private MessageType messageType;
   private SendPhoto sendPhoto;


}
