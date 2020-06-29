import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static spark.Spark.post;

public class TelegramAPI extends TelegramLongPollingBot {
    Main app = new Main();

    public TelegramAPI() {}

    @Override
    public String getBotUsername() {
        return "MergeRequestsNotificationsBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        String chatId = update.getMessage().getChatId().toString();
        if (text.equals("/start") && !app.currentUsers.containsKey(chatId)) {
            try {
                app.initUser(chatId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!app.currentUsers.containsKey(chatId)) {
            SendMessage sendMessage = createMessage(chatId, "Press /start");
            printMessage(sendMessage);
        } else {
            try {
                getNotification(chatId);
                makeCommand(chatId, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getNotification(String chatId) {
        post("/", (request, response) -> {
            JSONObject jsonObject = new JSONObject(request.body());
            String currentProjectId = jsonObject.getJSONObject("project").get("id").toString();
            if (app.currentUsers.get(chatId).projects.contains(currentProjectId)) {
                SendMessage message = createMessage(app.currentUsers.get(chatId).id,
                        "New merge request from " + jsonObject.getJSONObject("project").get("name").toString());
                printMessage(message);
            }
            return "";
        });
    }

    public void makeCommand(String chatId, String text) {
        String answer = app.currentUsers.get(chatId).bot.makeAction(text, app.currentUsers.get(chatId));
        List<String> buttonNames = app.currentUsers.get(chatId).bot.getNextCommands(app.currentUsers.get(chatId));
        SendMessage sendMessage = createMessage(chatId, answer);
        setButtons(sendMessage, buttonNames);
        printMessage(sendMessage);
    }

    public void printMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage createMessage(String chatId, String answer) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(answer);
        return sendMessage;
    }

    private void setButtons(SendMessage message, List<String> buttonNames) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        message.setReplyMarkup(replyKeyboardMarkup);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        for (String buttonName : buttonNames) {
            keyboardFirstRow.add(new KeyboardButton(buttonName));
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
