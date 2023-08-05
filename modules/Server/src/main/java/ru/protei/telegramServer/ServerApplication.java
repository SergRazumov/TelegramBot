package ru.protei.telegramServer;

import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import ru.protei.telegramServer.handler.SaveAudioHttpHandler;
import ru.protei.telegramServer.handler.SendAudioHttpHandler;
import ru.protei.telegramServer.handler.SubscribePhoneHttpHandler;
import ru.protei.telegramServer.structure.PhoneRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerApplication {

    /**
     * Хранилище аудиозаписей. Где ключ - уникальный id чата, значение пара - номер телефона, аудиозапись.
     */
    @Getter
    private static final TreeMap<Long, Queue<PhoneRecord>> storage = new TreeMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    public static void main(String[] args) throws IOException {
        logger.debug("Запуск ServerApplication");
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.createContext("/send_audio", new SendAudioHttpHandler());
        server.createContext("/save_audio", new SaveAudioHttpHandler());
        server.createContext("/subscribe_phone", new SubscribePhoneHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        logger.debug("Приложение ServerApplication запущено");
    }
}