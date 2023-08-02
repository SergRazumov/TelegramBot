package ru.telegram.recordMessage.telegram.nonCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.telegram.recordMessage.exceptions.IllegalPhoneException;
import ru.telegram.recordMessage.telegram.Bot;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    private final Logger logger = LoggerFactory.getLogger(NonCommand.class);


    public String subscribe(Long chatId, String userName, String text) {
        logger.debug(String.format("Пользователь %s. Начата обработка сообщения \"%s\", который должен являться телефонным" +
                        "номером",
                userName, text));
        String answer;
        try {
            logger.debug(String.format("Пользователь %s. Проверяем является ли данный текст номером телефона \"%s\"",
                    userName, text));
            if (!validatePhone(text)) {
                throw new IllegalPhoneException("Получен некорректный телефонный номер");
            }
            savePhone(chatId, text);
            logger.debug(String.format("Пользователь %s. Телефон \"%s\" сохранён",
                    userName, text));
        } catch (IllegalPhoneException e) {
            logger.debug(String.format("Пользователь %s. Получен некорректный телефонный номер \"%s\"",
                    userName, text));
            answer = "Получен некорректный телефонный номер. " +
                    "Возможно, Вам поможет /help";
            return answer;
        }
        logger.debug(String.format("Пользователь %s. Завершена обработка сообщения \"%s\", не являющегося командой",
                userName, text));
        answer = "Номер телефона сохранен.";
        return answer;
    }

    /**
     * Проверка номера телефона на соответствие регулярному выражению
     */
    boolean validatePhone(String phone) {
        final String regex = "\\+?[0-9]{0,1}\\s?[0-9]{3}[-\\s]?[0-9]{3}[-\\s]?[0-9]{2}[-\\s]?[0-9]{2}";;
        return phone.matches(regex);
    }

    /**
     * Сохранить телефонный номер в структуру
     */
    void savePhone(Long chatId, String phone) {
        final Queue<Pair> list = Bot.getStorage().get(chatId) == null ? new LinkedList<>() : Bot.getStorage().get(chatId);
        if (phone.matches("\\d{10}")) {
            list.add(new Pair("8" + phone));
        } else if (phone.matches("[+]7\\d{10}")) {
            list.add(new Pair("8" + phone.substring(2)));
        } else if (phone.matches("8\\d{10}")) {
            list.add(new Pair(phone));
        } else if (phone.matches("7\\d{10}")) {
            list.add(new Pair("8" + phone.substring(1)));
        }
        Bot.getStorage().put(chatId, list);
    }
}

