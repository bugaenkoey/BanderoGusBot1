package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//Bandero77GusBot
//6281123038:AAG20ucvcXosPG6Nli-UefSXylOdmzfC-pE
public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    @Override
    public String getBotUsername() {
        return "Bandero77GusBot";
    }

    @Override
    public String getBotToken() {
        return "6281123038:AAG20ucvcXosPG6Nli-UefSXylOdmzfC-pE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
       SendMessage message = createMessage("Hello *Yevhen Buhaienko*");
        message.setChatId(chatId);
        sendApiMethodAsync(message);
    }

    public long getChatId(Update update){
        if (update.hasMessage()){
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()){
            return update.getCallbackQuery().getFrom().getId();
        }
        return Long.parseLong(null);
    }
    public SendMessage createMessage(String text){
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setParseMode("markdown");
        return  message;
    }

}
