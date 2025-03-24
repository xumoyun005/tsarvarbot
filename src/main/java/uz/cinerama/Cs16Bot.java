package uz.cinerama;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

public class Cs16Bot extends TelegramLongPollingBot {

    private final ServerParser parser = new ServerParser();
    private static final int LIMIT = 5;

    @Override
    public String getBotUsername() {
        return "tsarvarcombot";
    }

    @Override
    public String getBotToken() {
        return "8050005369:AAFsWSjs8FvDjbADG6Zdz19qCz0cVSKE4Qw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equals("/servers")) {
                sendServerList(update.getMessage().getChatId().toString());
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            String data = query.getData();

            int page = Integer.parseInt(data);
            Message message = (Message) query.getMessage();
            editServerList(message, page);
        }
    }

    private void sendServerList(String chatId) {
        List<Server> servers = parser.getServers(0, LIMIT);
        StringBuilder msg = new StringBuilder("*CS 1.6 Serverlar:*\n\n");
        for (Server s : servers) msg.append(s);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg.toString());
        message.enableMarkdown(true);
        message.enableMarkdownV2(false);
        message.enableHtml(true);
        message.setReplyMarkup(getPaginationMarkup(0));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editServerList(Message message, int page) {
        List<Server> servers = parser.getServers(page, LIMIT);
        StringBuilder msg = new StringBuilder("*CS 1.6 Serverlar:*\n\n");
        for (Server s : servers) msg.append(s);

        EditMessageText edit = new EditMessageText();
        edit.setChatId(message.getChatId().toString());
        edit.setMessageId(message.getMessageId());
        edit.setText(msg.toString());
        edit.enableMarkdown(true);
        edit.enableHtml(true);
        edit.setReplyMarkup(getPaginationMarkup(page));

        try {
            execute(edit);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getPaginationMarkup(int page) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        if (page > 0) {
            InlineKeyboardButton prevButton = new InlineKeyboardButton();
            prevButton.setText("⬅️ Oldingi");
            prevButton.setCallbackData(String.valueOf(page - 1));
            buttons.add(prevButton);
        }
        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("➡️ Keyingi");
        nextButton.setCallbackData(String.valueOf(page + 1));
        buttons.add(nextButton);

        rows.add(buttons);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        return markup;
    }


    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new Cs16Bot());
            System.out.println("✅ Bot ishga tushdi!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
