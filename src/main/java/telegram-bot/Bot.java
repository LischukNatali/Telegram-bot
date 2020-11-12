
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
//прием сообщений, поучение обновлений через лонг пул
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()){
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                default:
                    try{
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Город не найден");
                    }
            }
        }
    }

    public void setButtons(SendMessage sendMessage) {       //add keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); //инициализирует клавиатуру
        sendMessage.setReplyMarkup(replyKeyboardMarkup);                     //установить разметку на клавиатуру и связать сообщение с нашей клавиатурой
        replyKeyboardMarkup.setSelective(true);                               //выводить клавиатуру всем или отдельным пользователям
        replyKeyboardMarkup.setResizeKeyboard(true);//указать клиенту подгонку клавиатуры, сколько кнопок, больше или меньше
        replyKeyboardMarkup.setOneTimeKeyboard(false);        //скрывать клавиатуру после наатя кнопки или нет

        List<KeyboardRow> keyboardRowList = new ArrayList<>();          //лист рядов клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();               //создаем строку клав

        keyboardFirstRow.add(new KeyboardButton("/help"));  //добавляем кнопку
        keyboardFirstRow.add(new KeyboardButton("/setting"));  //добавляем кнопку

        keyboardRowList.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();        //создаем обьект sendMessage
        sendMessage.enableMarkdown(true);                   //включить возможность разметки
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        try {
            execute(sendMessage.setText(text));
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //для возвращения имя бота, указаного при регстрации
    public String getBotUsername() {
        return "MyTest_Vebinar_bot";
    }

    //токен, какой мы получили от BotFather
    public String getBotToken() {
        return "1403588265:AAGWgVrTUaoMgFLeZOwSaW4I2pBKX8gEQRA";
    }
}
