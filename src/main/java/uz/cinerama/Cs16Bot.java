package uz.cinerama;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

public class Cs16Bot extends TelegramLongPollingBot {

    private final ServerParser parser = new ServerParser();
    private final Map<String, Integer> userPages = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "tsarvarcombot"; // Bot nomingiz
    }

    @Override
    public String getBotToken() {
        return "8050005369:AAFsWSjs8FvDjbADG6Zdz19qCz0cVSKE4Qw"; // Tokeningizni shu yerga joylang
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            if (text.equals("/servers")) {
                userPages.put(chatId, 1);
                sendServers(chatId, 1);
            } else {
                sendMsg(chatId, "Serverlar ro'yxati uchun /servers buyrug'ini kiriting.");
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();

            if (data.startsWith("prev_") || data.startsWith("next_")) {
                int page = Integer.parseInt(data.split("_")[1]);
                userPages.put(chatId, page);
                sendServers(chatId, page);
            }
        }
    }

    private void sendServers(String chatId, int page) {
        List<Server> servers = parser.getServers(page, 3); // Har sahifada 3 ta server

        StringBuilder msg = new StringBuilder("<b>🔥 CS 1.6 Serverlari (Sahifa " + page + "):</b>\n\n");

        if (!servers.isEmpty()) {
            for (Server server : servers) {
                msg.append("🎮 <b>Nomi:</b> ").append(server.getName()).append("\n")
                        .append("🌐 <b>IP:</b> <code>").append(server.getIp()).append("</code>\n")
                        .append("👥 <b>O‘yinchilar:</b> ").append(server.getPlayers()).append("\n")
                        .append("🗺 <b>Xarita:</b> ").append(server.getMap()).append("\n")
                        .append("🌍 <b>Davlat:</b> ").append(getCountryFlag(server.getCountry())).append(" ").append(server.getCountry()).append("\n\n");
            }
        } else {
            msg.append("❌ Serverlar topilmadi yoki xatolik yuz berdi!");
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg.toString());
        message.enableHtml(true);

        // Inline tugmalar
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        if (page > 1) {
            InlineKeyboardButton prev = new InlineKeyboardButton();
            prev.setText("⬅️ Oldingi");
            prev.setCallbackData("prev_" + (page - 1));
            row.add(prev);
        }

        InlineKeyboardButton next = new InlineKeyboardButton();
        next.setText("Keyingi ➡️");
        next.setCallbackData("next_" + (page + 1));
        row.add(next);

        keyboard.add(row);
        markup.setKeyboard(keyboard);

        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String chatId, String message) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(message);
        msg.enableHtml(true);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getCountryFlag(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) return "";
        countryCode = countryCode.toUpperCase();

        int firstChar = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondChar = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;

        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Cs16Bot());
            System.out.println("🤖 Bot ishga tushdi!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
