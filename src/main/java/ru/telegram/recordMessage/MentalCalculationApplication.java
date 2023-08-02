package ru.telegram.recordMessage;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.telegram.recordMessage.telegram.Bot;


public class MentalCalculationApplication {

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot("recordmessageproteibot", "5880136068:AAE_iI6xZhCCfkix6q3R66cePijkk_5dJSo"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}