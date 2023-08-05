package ru.protei.telegram.recordMessage.commands.httpcommands;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.protei.telegram.recordMessage.Utils;
import ru.protei.telegram.recordMessage.commands.otherCommands.ServiceCommand;

import static ru.protei.telegram.recordMessage.Utils.sendHTTP;
/**
 * Служебная команда отправки HTTP запроса для сохранения аудиофайла в структуре
 */
public class SaveCommand extends ServiceCommand {

    private final Logger logger = LoggerFactory.getLogger(SaveCommand.class);

    public SaveCommand(String identifier, String description) {
        super(identifier, description);
    }


    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        String answer;
        int answerCode = sendHTTP("/save_audio");
        if(answerCode==200) {
            logger.debug(String.format("Пользователь %s. Аудиозапись сохранена. Код ответа от сервера %s",
                    userName, answerCode));
            answer = String.format("Аудиозапись сохранена. Код ответа от сервера %s",
                    answerCode);
        } else {
            logger.debug(String.format("Пользователь %s. Аудиозапись сохранена. Код ответа от сервера %s",
                    userName, answerCode));
            answer = String.format("Аудиозапись не сохранена, отсуствует подписчик для данного номера, " +
                            "или такая запись уже сохранена. Код ответа от сервера %s",
                    answerCode);
        }
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, answer);
        logger.debug(String.format("Телефонный номер %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
