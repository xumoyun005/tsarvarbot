package uz.cinerama;

import lombok.Data;

@Data
public class Server {
    private final String name;
    private final String ip;
    private final String players;
    private final String map;
    private final String country;

    public Server(String name, String ip, String players, String map, String country) {
        this.name = name;
        this.ip = ip;
        this.players = players;
        this.map = map;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getPlayers() {
        return players;
    }

    public String getMap() {
        return map;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return String.format("Server: %s\nIP: %s\nPlayers: %s\nMap: %s\nCountry: %s\n\n",
                name, ip, players, map, country);
    }
}
