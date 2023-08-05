package ru.protei.telegramServer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.protei.telegramServer.HelperHandler;
import ru.protei.telegramServer.ServerApplication;
import ru.protei.telegramServer.structure.PhoneRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Обрабочик url /subscribe_phone. Сохранение телефонного номера в структуру
 */
public class SubscribePhoneHttpHandler extends HelperHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<String> requestParamValue = null;

        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = parserRequest(httpExchange);
            if(requestParamValue!=null) {
                savePhone(Long.valueOf(requestParamValue.get(1)) ,requestParamValue.get(0));
            }
        }
        handleResponse(httpExchange, requestParamValue != null, null);
    }

    /**
     * Сохранить телефонный номер в структуру
     */
    void savePhone(Long chatId, String phone) {
        final Queue<PhoneRecord> list = ServerApplication.getStorage().get(chatId) == null ? new LinkedList<>() : ServerApplication.getStorage().get(chatId);
        list.add(new PhoneRecord(chatId, phone));
        ServerApplication.getStorage().put(chatId, list);
    }
}
