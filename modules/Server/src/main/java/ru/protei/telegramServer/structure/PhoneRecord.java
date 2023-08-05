package ru.protei.telegramServer.structure;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.util.Deque;
import java.util.LinkedList;


/**
 * Объект хранящий индентификатор чата к которому привязан номер, номер абонента и множество аудиозаписей
 */
public class PhoneRecord {

    @Getter
    private final Long chatId;

    @Getter
    private final String msisdn;

    @Getter
    private final Deque<InputFile> audio = new LinkedList<>();

    public PhoneRecord(Long chatId, String msisdn) {
        this.chatId = chatId;
        this.msisdn = msisdn;
    }
}
