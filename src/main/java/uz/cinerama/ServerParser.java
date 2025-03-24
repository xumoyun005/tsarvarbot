package uz.cinerama;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerParser {

    public List<Server> getServers(int page, int limit) {
        List<Server> servers = new ArrayList<>();

        try {
            int sitePage = page * limit / 50; // 1 ta sayt sahifasida 50 ta server bo'lishi mumkin
            URL url = new URL("https://tsarvar.com/ajax/ru/servers/counter-strike-1.6?maxPlayersTo=32&page=" + sitePage);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
            String jsonResponse = scanner.useDelimiter("\\A").next();
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            String htmlContent = root.path("page").path("content").asText();

            Document doc = Jsoup.parse(htmlContent);
            Elements elements = doc.select(".serversList-item");

            int start = (page * limit) % 50;
            int end = Math.min(start + limit, elements.size());

            for (int i = start; i < end; i++) {
                Element el = elements.get(i);

                String name = Objects.requireNonNull(el.selectFirst(".serversList-itemName a")).text();
                String ip = Objects.requireNonNull(el.selectFirst(".serversList-itemAddressTblCText")).text();
                String players = Objects.requireNonNull(el.selectFirst(".serversList-itemPlayers")).text();
                String map = Objects.requireNonNull(el.selectFirst(".serversList-itemMap")).text();
                String countryCode = Objects.requireNonNull(el.selectFirst(".serversList-itemCountry")).text().trim();

                servers.add(new Server(name, ip, players, map, countryCode));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return servers;
    }

}
