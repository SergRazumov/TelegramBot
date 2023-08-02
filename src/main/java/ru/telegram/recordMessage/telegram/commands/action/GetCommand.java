package ru.telegram.recordMessage.telegram.commands.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.telegram.recordMessage.Utils;
import ru.telegram.recordMessage.telegram.Bot;
import ru.telegram.recordMessage.telegram.nonCommand.Pair;

/**
 * Команда "Получить"
 */
public class GetCommand extends ActionCommand {

    private final Logger logger = LoggerFactory.getLogger(GetCommand.class);

    public GetCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));

        InputFile inputFile = getAudioFile(chat.getId());

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, inputFile
                );
        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }

    /**
     * Поиск аудиозаписей по идентификатору чата и отдача их с наиболее поздней
     */
    private InputFile getAudioFile(Long id) {
        logger.debug("Поиск аудиофайлов");
        for (Pair pair : Bot.getStorage().get(id)) {
           return pair.getAudio().removeLast();
        }
        return null;
    }

}
