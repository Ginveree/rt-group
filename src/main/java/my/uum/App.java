package my.uum;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * This is the main class to run telegram.
 *
 * @author Ginveree Kong May Cheng 279045
 */
public class App {
    /**
     * This is the main method to run the system.
     *
     * @param args To hold command line arguments and in the form of String value.
     */
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new GinvereeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
