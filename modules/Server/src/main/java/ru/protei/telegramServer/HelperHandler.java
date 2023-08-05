package ru.protei.telegramServer;

import com.sun.net.httpserver.HttpExchange;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.protei.telegramServer.structure.PhoneRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Класс помощник отправляющий ответ и анализирующий запрос
 */
public class HelperHandler {

    private static final Logger logger = LoggerFactory.getLogger(HelperHandler.class);
    public void handleResponse(HttpExchange httpExchange, boolean result, String msisdn) throws IOException {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            String response;
            if (result) {
                response = "Ok";
                httpExchange.sendResponseHeaders(200, response.length());
                logger.debug("Отправлен ответ с кодом 200. " + response);
            } else {
                response = "Error: incorrect input parameter";
                httpExchange.sendResponseHeaders(400, response.length());
                logger.debug("Отправлен ответ с кодом 400. " + response);
            }
            outputStream.write(response.getBytes());
            outputStream.flush();

        }

        if(msisdn!=null) {
            sendHTTP(msisdn);
        }
    }

    private PhoneRecord getRecord(String msisdn) {
        for (Map.Entry<Long, Queue<PhoneRecord>> pairs : ServerApplication.getStorage().entrySet()) {
            for (PhoneRecord phoneRecord : pairs.getValue()) {
                if (phoneRecord.getMsisdn().equals(msisdn)) {
                    logger.debug(String.format("Номер %s. Запись получена",
                            msisdn));
                    return phoneRecord;
                }
            }
        }
        logger.debug("Аудиозаписи на данном телефонном номере не найдены");
        return null;
    }


    /**
     * Разбор URL и формирование List с извлеченными параметрами
     */
    public List<String> parserRequest(HttpExchange httpExchange) {
        final String str = httpExchange.getRequestURI().toString();
        String[] params =  str.contains("msisdn")
                ? str.split("\\?")[1].split("&")
                : null;
        if(params!=null) {
            final List<String> param = new ArrayList<>();
            Arrays.stream(params).forEach(p -> param.add(p.split("=")[1]));
            logger.debug("Сформирован список параметров из URL");
            return param;
        }
        logger.debug("Не найдены параметры переданные в URL");
        return null;
    }


    /**
     * Отправка HTTP запроса на сервер
     */
    private void sendHTTP(String msisdn) throws IOException {
        PhoneRecord record = getRecord(msisdn);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost upload = new HttpPost("https://api.telegram.org/bot" + "5880136068:AAE_iI6xZhCCfkix6q3R66cePijkk_5dJSo/sendAudio");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setLaxMode();
            builder.setCharset(StandardCharsets.UTF_8);
            builder.addTextBody("chat_id", String.valueOf(record.getChatId()), ContentType.create("text/plain", Charset.defaultCharset()));
            builder.addBinaryBody(record.getAudio().peekLast().getMediaName(), record.getAudio().peekLast().getNewMediaFile(), ContentType.APPLICATION_OCTET_STREAM, record.getAudio().peekLast().getMediaName());
            builder.addTextBody("audio", record.getAudio().removeLast().getAttachName(), ContentType.create("text/plain", Charset.defaultCharset()));
            HttpEntity multipart = builder.build();
            upload.setEntity(multipart);
            client.execute(upload);
            logger.debug("Запрос на сервер отправлен");
        }
    }
}
