package ru.protei.telegram.recordMessage;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.protei.telegram.recordMessage.commands.httpcommands.SaveCommand;
import ru.protei.telegram.recordMessage.commands.commands.HelpCommand;
import ru.protei.telegram.recordMessage.commands.commands.StartCommand;
import ru.protei.telegram.recordMessage.commands.otherCommands.NonCommand;


/**
 * Собственно, бот
 */
public final class Bot extends TelegramLongPollingCommandBot {
    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final NonCommand nonCommand;


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
    @SneakyThrows
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