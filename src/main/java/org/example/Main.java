package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Main extends TelegramLongPollingBot {
    public class LevelMassege {
        public int level;
        public String img;
        public String msg;
        public List<String> btn;
        public Map<String, String> buttons;

        LevelMassege() {
            this.level = 1;
            this.img = null;
            this.msg = null;
            this.buttons = null;
        }

        LevelMassege(int level, String msg, Map<String, String> buttons) {
            this.level = level;
            this.img = "level-" + level + ".gif";
            this.msg = msg;
            this.buttons = buttons;
        }

        public Map<String, String> getButtons() {

            List keys = new ArrayList(buttons.keySet());
            Collections.shuffle(keys);
            Map<String, String> newBtn = new HashMap<>();
            int i = 0;
            for (Object o : keys) {
                i++;
                newBtn.put(o.toString(), buttons.get(o));
                if (i >= 3) {
                    break;
                }
            }
            return newBtn;
        }
    }

    public List<LevelMassege> masseges2 = List.of(
//    public LevelMassege l1 =
            new LevelMassege(
                    1,
                    "Ґа-ґа-ґа!\n" +
                            "Вітаємо у боті біолабораторії «Батько наш Бандера».\n" +
                            "\n" +
                            "Ти отримуєш гусака №71\n" +
                            "\n" +
                            "Цей бот ми створили для того, щоб твій гусак прокачався з рівня звичайної свійської худоби " +
                            "до рівня біологічної зброї, здатної нищити ворога. \n" +
                            "\n" +
                            "Щоб звичайний гусак перетворився на бандерогусака, тобі необхідно:\n" +
                            "✔️виконувати завдання\n" +
                            "✔️переходити на наступні рівні\n" +
                            "✔️заробити достатню кількість монет, щоб придбати Джавеліну і зробити свєрхтра-та-та.\n" +
                            "\n" +
                            "*Гусак звичайний.* Стартовий рівень.\n" +
                            "Бонус: 5 монет.\n" +
                            "Обери завдання, щоб перейти на наступний рівень",
                    Map.of(
                            "Сплести маскувальну сітку (+15 монет)", "level-1",
                            "Зібрати кошти патріотичними піснями (+15 монет)", "level-1",
                            "Вступити в Міністерство Мемів України (+15 монет)", "level-1",
                            "Запустити волонтерську акцію (+15 монет)", "level-1",
                            "Вступити до лав тероборони (+15 монет)", "level-1")),

            new LevelMassege(
                    2,
                    "*Вітаємо на другому рівні! Твій гусак - біогусак.*\n" +
                            "Баланс: 20 монет. \n" +
                            "Обери завдання, щоб перейти на наступний рівень",
                    Map.of("Зібрати комарів для нової біологічної зброї (+15 монет)", "level-2",
                            "Пройти курс молодого бійця (+15 монет)", "level-2",
                            "Задонатити на ЗСУ (+15 монет)", "level-2",
                            "Збити дрона банкою огірків (+15 монет)", "level-2",
                            "Зробити запаси коктейлів Молотова (+15 монет)", "level-2")),

            new LevelMassege(
                    3,
                    "*Вітаємо на третьому рівні! Твій гусак - бандеростажер.*\n" +
                            "Баланс: 35 монет. \n" +
                            "Обери завдання, щоб перейти на наступний рівень\n",
                    Map.of("Злітати на тестовий рейд по чотирьох позиціях (+15 монет)", "level-3",
                            "Відвезти гуманітарку на передок (+15 монет)", "level-3",
                            "Знайти зрадника та здати в СБУ (+15 монет)", "level-3",
                            "Навести арту на орків (+15 монет)", "level-3",
                            "Притягнути танк трактором (+15 монет)", "level-3")),

            new LevelMassege(
                    4,
                    "*Вітаємо на останньому рівні! Твій гусак - готова біологічна зброя - бандерогусак.*\n" +
                            "Баланс: 50 монет. \n" +
                            "Тепер ти можеш придбати Джавелін і глушити чмонь",
                    Map.of("Купити Джавелін (50 монет)", "level-4")),
            new LevelMassege(
                    5,
                    "*Джавелін твій. Повний вперед!*",
                    null
            )
    );

    private LevelMassege item;
    private Map<Long, Integer> levels = new HashMap<>();

    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    public String getBotProperty(String key) {

        String appConfigPath = "src/main/resources/app.properties";

        Properties appProps = new Properties();
        try {

            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();

        }
        return appProps.getProperty(key);

    }

    @Override
    public String getBotUsername() {
        return getBotProperty("BOTNAME");
    }

    @Override
    public String getBotToken() {
        return getBotProperty("TELEGRAMBOTTOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            setLevel(chatId, 1);
            item = (LevelMassege) masseges2.toArray()[0];
            sendMsg(chatId, item);
        }

        if (update.hasCallbackQuery()
                && update.getCallbackQuery().getData().equals(("level-" + (getLevel(chatId))))
//                && getLevel(chatId) == item.level
        ) {
            setLevel(chatId, item.level + 1);
            item = (LevelMassege) masseges2.toArray()[getLevel(chatId)-1];

            sendMsg(chatId, item);
        }
    }

    private void sendMsg(Long chatId, LevelMassege item) {
        sendImage(item.img, chatId);
        SendMessage message = createMessage(item.msg + "\n level - " + getLevel(chatId));
        message.setChatId(chatId);
        attachButtons(message, (Map<String, String>) item.getButtons());

        sendApiMethodAsync(message);
    }

    public long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return Long.parseLong(null);
    }

    public SendMessage createMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        return message;
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String buttonName : buttons.keySet()) {
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(buttonValue);
            keyboard.add(Arrays.asList(button));
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

    }

    public void sendImage(String name, Long chatId) {
        SendAnimation animation = new SendAnimation();
        InputFile inputFile = new InputFile();
//        inputFile.setMedia(new File("images/" + name + ".gif"));

        inputFile.setMedia(new File("images/" + name));
        animation.setAnimation((inputFile));
        animation.setChatId(chatId);

        executeAsync(animation);
    }

    public int getLevel(Long chatId) {
        return levels.getOrDefault(chatId, 1);
    }

    public void setLevel(Long chatId, int level) {
        levels.put(chatId, level);
    }
}
