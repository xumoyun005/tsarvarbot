package uz.cinerama;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AjaxDataFetcher {
    public static void main(String[] args) {
        try {
            // So'rov URL manzili
            String url = "https://tsarvar.com/ajax/ru/servers/counter-strike-1.6?maxPlayersTo=32";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // So'rov metodi
            con.setRequestMethod("GET");

            // Zarur headerlarni qo'shish
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            System.out.println("Javob kodi: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // Javob muvaffaqiyatli bo'lsa
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Olingan JSON ma'lumotlarini konsolga chiqarish
                System.out.println("Olingan ma'lumotlar: " + response.toString());

                // Bu yerda JSON ma'lumotlarini parse qilish va ulardan foydalanish mumkin
            } else {
                System.out.println("So'rov muvaffaqiyatsiz bo'ldi.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}