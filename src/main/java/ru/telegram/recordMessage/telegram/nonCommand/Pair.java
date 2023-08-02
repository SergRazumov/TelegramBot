package ru.telegram.recordMessage.telegram.nonCommand;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.Deque;
import java.util.LinkedList;




/**
 * Пара хранящая номер абонента и множество аудиозаписей
 */
public class Pair {

    @Getter
    private final String msisdn;

    @Getter
    private final Deque<InputFile> audio = new LinkedList<>();

    public Pair(String msisdn) {
        this.msisdn = msisdn;
    }
}
