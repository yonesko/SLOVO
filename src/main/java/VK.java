import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
https://oauth.vk.com/authorize?client_id=5381172&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,offline&response_type=token&v=5.50
 */
public class VK {
    //prod
//    private final static String OWNER_ID = "118193284";
    //test
    private final static String OWNER_ID = "119022967";
    private final static String acces_token = "87582bf6f41a1bded77beede5c51a14b69c42669f7afdd86ef673bea2b0f731c406c10fff8d8b54d8e3b5";
//
    public static void main(String[] args) throws Exception {
    }
    public static void wallPost(String message, long date) throws Exception {
        List<String> query = new ArrayList<>();
        Map<String, String> pars = new HashMap<>();
        pars.put("owner_id", "-" + OWNER_ID);
        pars.put("from_group", "1");
        pars.put("access_token", acces_token);
        pars.put("message", URLEncoder.encode(message, "UTF-8"));
        pars.put("publish_date", String.valueOf(date));

        for (Map.Entry<String, String> e : pars.entrySet())
            query.add(e.getKey() + "=" + e.getValue());

        URL obj = new URL("https://api.vk.com/method/wall.post");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(String.join("&", query));
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        //print result
        System.out.println(response.toString());
    }
}
