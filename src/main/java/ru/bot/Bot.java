package ru.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import ru.server.configure.Configuration;

public class Bot {

    private TelegramBot bot;
    private final Configuration configuration;

    public Bot(Configuration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        bot = new TelegramBot(configuration.getBotToken());
    }

    public void sendMessage(String message) {
        sendMessage(Long.parseLong(configuration.getTelegramChatId()), message, ParseMode.Markdown);
    }

    public void sendMessage(long chatId, String message, ParseMode parseMode) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.parseMode(parseMode);
        bot.execute(sendMessage);
    }

}
