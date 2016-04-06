import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
/*
https://oauth.vk.com/authorize?client_id=5381172&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,offline&response_type=token&v=5.50
 */
public class VK {
    private final static String OWNER_ID = "118193284";
    private final static String acces_token = "c380b3c1e25ddb95bd0a19f796a73d836b527fba9b9654dac485d6384ca815050f29a7a830d1f24ceb28e";

    public static void main(String[] args) throws Exception {
    }
    public static void wallPost(String message) throws Exception {

        String query = String.format(
                        "wall.post?" +
                        "owner_id=-%s" +
                        "&from_group=1" +
                        "&access_token=%s" +
                        "&message=%s",
                OWNER_ID,
                acces_token,
                message
        );

        URI uri = new URI(
                "https",
                "api.vk.com",
                "/method",
                query,
                null);
        String url = uri.toASCIIString();

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
