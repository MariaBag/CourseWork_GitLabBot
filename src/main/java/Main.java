import java.util.HashMap;
import java.util.Map;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    Map<String, User> currentUsers;

    public Main() {
        currentUsers = new HashMap<>();
    }

    public void initUser(String token) {
        User user = new User(token);
        currentUsers.put(token, user);
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TelegramAPI());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
