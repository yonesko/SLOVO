import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VKPublisher {
    private final String OWNER_ID = "118193284";

    public static void main(String[] args) throws Exception {
        String acces_token = "42eb4230195d9646cabd87371d0f3a6d18409215d7e2c140a34dbcadfd05587bfe9f7d5bf752ab168c174";
        new VKPublisher().post(acces_token, "uhg");
    }
    private void post(String acces_token, String message) throws Exception {

        String url = String.format(
                "https://api.vk.com/method/wall.post?owner_id=-%s&message=%s&access_token=%s",
                OWNER_ID,
                message,
                acces_token
        );



        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }
}
