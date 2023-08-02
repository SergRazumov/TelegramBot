package ru.telegram.recordMessage.telegram.commands.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.telegram.recordMessage.Utils;

/**
 * Команда "Помощь"
 */
public class HelpCommand extends ServiceCommand {
    private final Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                """
                            Я бот, который поможет подключить ваш автоответчик к telegram
                            
                            Для привязки номера к telegram просто введите его без разделителей и скобок,
                            вы можете привязать несколько телефонных номеров к одному аккаунту
                            
                            ❗*Список команд*
                            /get - получить все файлы сообщений для зарегистрированных вами номеров
                            /save - служебная хардкод команда, сохраняющая определенную аудиозапись 
                            по номеру 89817062001 
                            /help - помощь
                        """);
        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}