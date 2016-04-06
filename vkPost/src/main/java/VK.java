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
    private final static String acces_token = "87582bf6f41a1bded77beede5c51a14b69c42669f7afdd86ef673bea2b0f731c406c10fff8d8b54d8e3b5";

    public static void main(String[] args) throws Exception {
    }
    public static void wallPost(String message) throws Exception {

        String query = String.format(
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
                "/method/wall.post",
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
