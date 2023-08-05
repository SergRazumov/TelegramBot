package ru.protei.telegram.recordMessage.commands.otherCommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.protei.telegram.recordMessage.Utils;


import java.io.IOException;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    private final Logger logger = LoggerFactory.getLogger(NonCommand.class);


    public String subscribe(Long chatId, String userName, String text) throws IOException {
        logger.debug(String.format("Пользователь %s. Начата обработка сообщения \"%s\", который должен являться телефонным" +
                        "номером",
                userName, text));
        String answer;
        logger.debug(String.format("Пользователь %s. Проверяем является ли данный текст номером телефона \"%s\"",
                userName, text));
        if (!validatePhone(text)) {
            answer = "Получен некорректный телефонный номер. Возможно, Вам поможет /help\"";
            return answer;
        }
        String msisdn = correctPhone(text);
        int answerCode = Utils.sendHTTP("/subscribe_phone?msisdn=" + msisdn + "&chatId=" + chatId);
        logger.debug(String.format("Пользователь %s. Телефон \"%s\" сохранён. Код ответа от сервера %s",
                userName, text, answerCode));
        logger.debug(String.format("Пользователь %s. Завершена обработка сообщения \"%s\", не являющегося командой",
                userName, text));
        answer = String.format("Номер телефона сохранен. Код ответа от сервера %s", answerCode);
        return answer;
    }

    /**
     * Проверка номера телефона на соответствие регулярному выражению
     */
    public boolean validatePhone(String phone) {
        final String regex = "\\+?[0-9]{0,1}\\s?[0-9]{3}[-\\s]?[0-9]{3}[-\\s]?[0-9]{2}[-\\s]?[0-9]{2}";
        return phone.matches(regex);
    }



    /**
     * Корректировка телефонного номера, приведение к виду с 8
     */
    public String correctPhone(String phone) {
        if (phone.matches("\\d{10}")) {
            return "8" + phone;
        } else if(phone.matches("[+]7\\d{10}")) {
            return "8" + phone.substring(2);
        } else if (phone.matches("8\\d{10}")) {
            return phone;
        } else if (phone.matches("7\\d{10}")) {
            return "8" + phone.substring(1);
        }
        return null;
    }
}

