package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.model.WallPost;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.HttpConnectionAgent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/*

https://oauth.vk.com/authorize?client_id=5381172&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,offline&response_type=token&v=5.50
 */
public class VK {
    //prod
//    private final static String OWNER_ID = "118193284";
    //test
    private final static String OWNER_ID = "119022967";
    private final static String ACCES_TOKEN = "7c615dcc1c06ead089152605fc31e1a9598d7b01f2c6f8e77cfe4468028bd1df542bc0bfa9a82c6618ac9";
//
    public static void main(String[] args) throws Exception {
        System.out.println(VK.getPosts("postponed"));
    }

    /**
     *
     * @param date epoch in seconds
     * @return true only if post_id in server's response exists
     */
    public static boolean wallPost(String message, LocalDateTime date) throws IOException, ParseException {
        List<String> query = new ArrayList<>();
        Map<String, String> pars = new HashMap<>();
        pars.put("owner_id", "-" + OWNER_ID);
        pars.put("from_group", "1");
        pars.put("access_token", ACCES_TOKEN);
        pars.put("message", URLEncoder.encode(message, "UTF-8"));
        pars.put("publish_date", String.valueOf(ZonedDateTime.of(date, ZoneId.systemDefault()).toEpochSecond()));

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
        StringBuilder response = new StringBuilder();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

        }
        //print result
        System.out.println(response.toString());
        return isWallPostSuccessful(response.toString());
    }
    private static boolean isWallPostSuccessful(String response) throws ParseException {
        boolean result = false;
        JSONParser parser   = new JSONParser();
        JSONObject jsonResp  = (JSONObject) parser.parse(response.toString());
        JSONObject post = (JSONObject) jsonResp.get("response");

        if (post == null)
            result = false;
        else
            result = post.get("post_id") != null;

        return result;
    }

    /**
     * filter определяет, какие типы записей на стене необходимо получить.<br> Возможны следующие значения параметра:<br>
     suggests — предложенные записи на стене сообщества (доступно только при вызове с передачей access_token);<br>
     postponed — отложенные записи (доступно только при вызове с передачей access_token);<br>
     owner — записи на стене от ее владельца;<br>
     others — записи на стене не от ее владельца;<br>
     all — все записи на стене (owner + others).<br>
     Если параметр не задан, то считается, что он равен all.
     */
    public static List<WallPost> getPosts(String...filters) throws IOException, URISyntaxException, ParseException {
        List<WallPost> result = new LinkedList<>();
        if (filters == null || filters.length == 0)
            result.addAll(getPosts("all"));
        else
            for (String filter : filters)
                result.addAll(getPosts(filter));
        return result;

    }

    private static List<WallPost> getPosts(String filter) throws IOException, URISyntaxException, ParseException {
        List<WallPost> result = new LinkedList<>();
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/wall.get")
                .setParameter("owner_id", "-" + OWNER_ID)
                .setParameter("filter", filter)
                .setParameter("acces_token", ACCES_TOKEN);

        HttpResponse response = HttpConnectionAgent.connectResponse(uriBuilder);
        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            StringWriter content = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), content);

            JSONParser parser   = new JSONParser();

            JSONObject jsonResp  = (JSONObject) parser.parse(content.toString());
            JSONArray postsList = (JSONArray) jsonResp.get("response");
            JSONObject unicPost  = null;
            ObjectMapper mapper = new ObjectMapper();
            WallPost post;

            for (int i=1; i < postsList.size(); i++) {
                unicPost = (JSONObject) postsList.get(i);
                post = mapper.readValue(unicPost.toJSONString(), WallPost.class);
                result.add(post);
            }
        }
        return result;

    }

    private static void cleanUpTest() {

    }
    private static void cleanUpTest(String post_id) throws IOException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/wall.delete")
                .setParameter("owner_id", "-" + 119022967)
                .setParameter("acces_token", ACCES_TOKEN)
                .setParameter("post_id", post_id);

        HttpResponse response = HttpConnectionAgent.connectResponse(uriBuilder);
        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            String content = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                IOUtils.copy(response.getEntity().getContent(), baos);
                content = baos.toString("UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            System.out.println(content);
        }
    }
}
