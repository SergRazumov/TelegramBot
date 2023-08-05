package ru.protei.telegramServer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.protei.telegramServer.HelperHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.List;


/**
 * Обрабочик url /send_audio?msisdn=<string> Получение последней аудиозаписи из структуры по указанному номеру
 */
public class SendAudioHttpHandler extends HelperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<String> requestParamValue = null;

        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = parserRequest(httpExchange);
        }
        handleResponse(httpExchange, requestParamValue != null, requestParamValue!=null ? requestParamValue.get(0): null);
    }
}
