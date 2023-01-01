package my.uum;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * This class is for running the main method and the program which are including the telegram bot APIs.
 *
 * @author Wong Seow Ting
 */
public class App {

    /**
     * This main method is for calling telegram bot APIs and all the method.
     *
     * @param args An array of command-line parameters for the program.
     */
    public static void main(String[] args) {

        // Instantiate Telegram Bots API
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            telegramBotsApi.registerBot(new s278263_A221_bot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
