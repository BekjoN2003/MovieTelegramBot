package com.example.moviebot.service;

import com.example.moviebot.model.Movie;
import com.example.moviebot.utill.MessageType;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.example.moviebot.utill.CurrentMessage;
import org.springframework.web.client.RestTemplate;
import com.example.moviebot.model.MovieListResponse;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

@Service
public class MovieService {

    private List<Movie> movieList;
    private static long page = 0L;

    private static Integer size = 4;
    private static long moviesCount;

    public CurrentMessage getAllMovies(Long chatId) {

        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();

        RestTemplate restTemplate = new RestTemplate();
        //       String Url = "http://localhost:8080/api/v1/movies/getAll?page=" + page + "&size="+ size;
        String Url = String.format("http://localhost:8080/api/v1/movies/getAll?page=%s&size=%s", page, size);
        ResponseEntity<MovieListResponse> response = restTemplate.getForEntity(Url, MovieListResponse.class);
        if (response.getStatusCode().value() == 200) {
            sendMessage.setText("Our Movies");
            MovieListResponse result = response.getBody();
            List<Movie> movies = result.getDtoList();
            moviesCount = result.getCount();
            if (movies.size() < 1) {
                return null;
            }
            Url = String.format("http://localhost:8080/api/v1/movies/getAll?page=%s&size=%s", page + 1, size);
            response = restTemplate.getForEntity(Url, MovieListResponse.class);
            if (response.getBody() != null && response.getBody().getDtoList().size() >= 1) {
                sendMessage.setReplyMarkup(generateMoviesButton(movies, true));
            } else sendMessage.setReplyMarkup(generateMoviesButton(movies, false));

            sendMessage.setChatId(chatId);
            currentMessage.setSendMessage(sendMessage);
            currentMessage.setMessageType(MessageType.SEND_MESSAGE);
            return currentMessage;
        }
        return null;
    }

    public InlineKeyboardMarkup generateMoviesButton(List<Movie> movieLIst, boolean next) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        List<InlineKeyboardButton> row = new LinkedList<>();

        for (int i = 0; i < movieLIst.size(); i++) {
            Movie m = movieLIst.get(i);
            if (movieLIst.size() < 3) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(String.format("%s) %s", m.getId(), m.getName()));
                button.setCallbackData(String.format("get_movie/%d", m.getId()));
                row.add(button);
            }
            if (movieLIst.size() >= 3) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(String.format("%s) %s", m.getId(), m.getName()));
                button.setCallbackData(String.format("get_movie/%d", m.getId()));
                row.add(button);
                if (i == 1) {
                    rowList.add(row);
                    row = new LinkedList<>();
                }
            }
        }
        rowList.add(row);
        row = new LinkedList<>();
        if (page != 0) {
            InlineKeyboardButton prev = new InlineKeyboardButton();
            prev.setText("<");
            prev.setCallbackData("prev");
            row.add(prev);
        }

        if (next) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText(">");
            nextButton.setCallbackData("next");
            row.add(nextButton);
        }
        if (!row.isEmpty()) {
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public CurrentMessage prev(CallbackQuery callbackQuery) {
        page -= 1;
        return getAllMovies(callbackQuery.getMessage().getChatId());
    }

    public CurrentMessage next(CallbackQuery callbackQuery) {
        page += 1;
        return getAllMovies(callbackQuery.getMessage().getChatId());
    }

    public CurrentMessage getMovieById(Integer movieId, Long chatId) {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://localhost:8080/api/v1/movies/" + movieId;
        ResponseEntity<Movie> response = restTemplate.getForEntity(Url, Movie.class);
        Movie movie = response.getBody();

        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.setText(String.format("<b>%s</b>\n%s\n\ncreated by %s", movie.getName(),
                movie.getDescription(), movie.getUser().getName()));
        sendMessage.setReplyMarkup(getMarkupForMovie(movieId, chatId));
        sendMessage.setChatId(chatId);
        currentMessage.setSendMessage(sendMessage);
        currentMessage.setMessageType(MessageType.SEND_MESSAGE);
        return currentMessage;
    }

    private ReplyKeyboard getMarkupForMovie(Integer movieId, Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        List<InlineKeyboardButton> row = new LinkedList<>();
        InlineKeyboardButton like = new InlineKeyboardButton();
        like.setText("like");
        like.setCallbackData(String.format("like/%s%s", chatId, movieId));

        InlineKeyboardButton comment = new InlineKeyboardButton();
        comment.setText("comment");
        comment.setCallbackData(String.format("comment/%s%s", chatId, movieId));
        row.add(like);
        row.add(comment);
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    /*public CurrentMessage createMovie(Long chatId){
        CurrentMessage currentMessage = new CurrentMessage();
        SendMessage sendMessage = new SendMessage();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/v1/movies/create/";
        ResponseEntity<MovieCreateDto> response = restTemplate.getForEntity(url, MovieCreateDto.class);

    }*/
}