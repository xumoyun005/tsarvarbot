package uz.cinerama;

import lombok.Data;

@Data
public class Server {
    private String name;
    private String ip;
    private String players;
    private String map;
    private String country;

    public Server(String name, String ip, String players, String map, String country) {
        this.name = name;
        this.ip = ip;
        this.players = players;
        this.map = map;
        this.country = country;
    }

    public String getFlagEmoji() {
        if (country == null || country.length() != 2) return "";
        int first = Character.codePointAt(country.toUpperCase(), 0) - 0x41 + 0x1F1E6;
        int second = Character.codePointAt(country.toUpperCase(), 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(first)) + new String(Character.toChars(second));
    }

    @Override
    public String toString() {
        return String.format(
                "🎮 <b>Nomi:</b> %s\n" +
                        "🌐 <b>IP:</b> <code>%s</code>\n" +
                        "👥 <b>O‘yinchilar:</b> %s\n" +
                        "🗺 <b>Xarita:</b> %s\n" +
                        "🌍 <b>Davlat:</b> %s %s\n\n",
                name, ip, players, map, country, getFlagEmoji()
        );
    }
}
