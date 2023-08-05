package ru.protei.telegramServer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.protei.telegramServer.HelperHandler;
import ru.protei.telegramServer.ServerApplication;
import ru.protei.telegramServer.structure.PhoneRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;


/**
 * Обрабочик url /save_audio. Сохранение аудиозаписи в структуру
 */
public class SaveAudioHttpHandler extends HelperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean result = false;
        if ("GET".equals(httpExchange.getRequestMethod())) {
            InputFile audioFile = downloadAudio();
            result = saveAudio("89817062001", audioFile);
        }
        handleResponse(httpExchange, result, null);
    }

    /**
     * Создание InputFile объекта
     */
    private InputFile downloadAudio() {
        return new InputFile(new File("sample.mp3"), String.format("%s.mp3", "sample"));
    }

    /**
     * Сохраняем в структуре под произвольным номером
     */
    public boolean saveAudio(String phone, InputFile audio) {
        for (Map.Entry<Long, Queue<PhoneRecord>> entry : ServerApplication.getStorage().entrySet()) {
            for (PhoneRecord phoneRecord : entry.getValue()) {
                if (phoneRecord.getMsisdn().equals(phone)) {
                    return phoneRecord.getAudio().offer(audio);
                }
            }
        }
        return false;
    }

}
