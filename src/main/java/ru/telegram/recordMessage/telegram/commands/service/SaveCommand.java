package ru.telegram.recordMessage.telegram.commands.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.telegram.recordMessage.Utils;
import ru.telegram.recordMessage.telegram.Bot;
import ru.telegram.recordMessage.telegram.nonCommand.Pair;

import java.io.File;
import java.util.Map;
import java.util.Queue;

/**
 * Служебная команда загрузки и сохранения файла в структуре
 */
public class SaveCommand extends ServiceCommand {

    private final Logger logger = LoggerFactory.getLogger(SaveCommand.class);

    public SaveCommand(String identifier, String description) {
        super(identifier, description);
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));

        /**
         * Скачиваем файл пример и сохраняем в структуре под произвольным номером
         */
        InputFile audioFile = downloadAudio();
        if (save("89817062001", audioFile)) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Аудиозапись сохранена");
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Аудиозапись не сохранена, отсуствует подписчик для данного номера," +
                            " или такая запись уже сохранена");
        }
        logger.debug(String.format("Телефонный номер %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }

    /**
     * Создание InputFile объекта
     */
    private InputFile downloadAudio() {
        logger.debug("Создание InputFile объекта");
        return new InputFile(new File("sample.mp3"), String.format("%s.mp3", "sample"));
    }

    /**
     * Сохраняем в структуре под произвольным номером
     */
    public boolean save(String phone, InputFile audio) {
        logger.debug(String.format("Сохранение аудио файла под телефонным номером \"%s\"",
                phone));
        for (Map.Entry<Long, Queue<Pair>> entry : Bot.getStorage().entrySet()) {
            for (Pair pair : entry.getValue()) {
                if (pair.getMsisdn().equals(phone)) {
                    return pair.getAudio().offer(audio);
                }
            }
        }
        return false;
    }


}
