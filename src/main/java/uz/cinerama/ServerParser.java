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

    private static final String BASE_URL = "https://tsarvar.com/ajax/ru/servers/counter-strike-1.6?maxPlayersTo=32&page=";

    public List<Server> getServers(int page, int limitPerPage) {
        List<Server> servers = new ArrayList<>();

        try {
            URL url = new URL(BASE_URL + page);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
            String jsonResponse = scanner.useDelimiter("\\A").next();
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            // HTML tarkibini olish (bu json ichidagi `page.content`)
            String htmlContent = root.path("page").path("content").asText();
            Document doc = Jsoup.parse(htmlContent);

            Elements serverElements = doc.select(".serversList-item");

            int count = 0;
            for (Element element : serverElements) {
                if (count >= limitPerPage) break;

                String name = element.select(".serversList-itemName a").text();
                String ip = element.select(".serversList-itemAddressTblCText").text();
                String players = element.select(".serversList-itemPlayersCur").text() +
                        "/" + element.select(".serversList-itemPlayersMax").text();
                String map = element.select(".serversList-itemMap").text();
                String countryCode = element.select(".serversList-itemCountry").text(); // eg: RU, FR, etc.

                servers.add(new Server(name, ip, players, map, countryCode));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return servers;
    }
}
