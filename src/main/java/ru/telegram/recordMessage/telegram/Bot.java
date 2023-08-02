package ru.telegram.recordMessage.telegram;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.telegram.recordMessage.Utils;
import ru.telegram.recordMessage.telegram.commands.action.GetCommand;
import ru.telegram.recordMessage.telegram.commands.service.HelpCommand;
import ru.telegram.recordMessage.telegram.commands.service.SaveCommand;
import ru.telegram.recordMessage.telegram.commands.service.StartCommand;
import ru.telegram.recordMessage.telegram.nonCommand.NonCommand;
import ru.telegram.recordMessage.telegram.nonCommand.Pair;

import java.util.*;

/**
 * Собственно, бот
 */
public final class Bot extends TelegramLongPollingCommandBot {
    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final NonCommand nonCommand;

    /**
     * Хранилище аудиозаписей. Где ключ - уникальный id чата, значение пара - номер телефона, аудиозапись.
     */
    @Getter
    @Setter
    private static TreeMap<Long, Queue<Pair>> storage;

    public Bot(String botName, String botToken) {
        super();
        logger.debug("Конструктор суперкласса отработал");
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        logger.debug("Имя и токен присвоены");

        this.nonCommand = new NonCommand();
        logger.debug("Класс обработки сообщения, не являющегося командой, создан");

        register(new StartCommand("start", "Старт"));
        logger.debug("Команда start создана");

        register(new HelpCommand("help", "Помощь"));
        logger.debug("Команда help создана");

        register(new SaveCommand("save", "Служебная команда, сохранить аудиозапись из кода"));
        logger.debug("Команда save создана");

        register(new GetCommand("get", "Получить"));
        logger.debug("Команда get создана");

        storage = new TreeMap<>();

        logger.info("Бот создан!");
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
     * Ответ на сохранение номера телефона от пользователя
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = Utils.getUserName(msg);
        String answer = nonCommand.subscribe(chatId, userName, msg.getText());
        setMessageAnswer(chatId, userName, answer);
    }

    /**
     * Отправка текстового ответа
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param text     текст ответа
     */
    private void setMessageAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
            e.printStackTrace();
        }
    }
}