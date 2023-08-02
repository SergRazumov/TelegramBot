package ru.telegram.recordMessage.telegram.commands.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Суперкласс для получения аудиозаписей
 */
abstract class ActionCommand extends BotCommand {

    private final Logger logger = LoggerFactory.getLogger(ActionCommand.class);


    public ActionCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    /**
     * Отправка ответа пользователю
     */
    void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, InputFile inputFile) {
        SendAudio audio = new SendAudio();
        audio.setChatId(chatId.toString());
        audio.setAudio(inputFile);
        try {
            absSender.execute(audio);
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Команда %s. Пользователь: %s", e.getMessage(), commandName, userName));
            e.printStackTrace();
        }
    }
}
