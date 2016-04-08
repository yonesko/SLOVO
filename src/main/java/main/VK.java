package main;

import util.HttpConnectionAgent;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.h2.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/*
https://oauth.vk.com/authorize?client_id=5381172&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,offline&response_type=token&v=5.50
 */
public class VK {
    //prod
//    private final static String OWNER_ID = "118193284";
    //test
    private final static String OWNER_ID = "119022967";
    private final static String ACCES_TOKEN = "87582bf6f41a1bded77beede5c51a14b69c42669f7afdd86ef673bea2b0f731c406c10fff8d8b54d8e3b5";
//
    public static void main(String[] args) throws Exception {
        cleanUpTest();
    }
    public static void wallPost(String message, long date) throws Exception {
        List<String> query = new ArrayList<>();
        Map<String, String> pars = new HashMap<>();
        pars.put("owner_id", "-" + OWNER_ID);
        pars.put("from_group", "1");
        pars.put("access_token", ACCES_TOKEN);
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

    public static List<String> getPublished() {
        List<String> result = new LinkedList<>();
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/wall.get")
                .setParameter("owner_id", "-" + OWNER_ID)
                .setParameter("acces_token", ACCES_TOKEN);

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

            JSONParser parser   = new JSONParser();

            try {

                JSONObject jsonResp  = (JSONObject) parser.parse(content);
                JSONArray postsList = (JSONArray) jsonResp.get("response");
                JSONObject unicPost  = null;
                String text;

                for (int i=1; i < postsList.size(); i++) {
                    unicPost = (JSONObject) postsList.get(i);
                    text = (String) unicPost.get("text");
                    result.add(text.substring(0, text.indexOf("<br>")));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    private static List<String> getPublishedId() {
        List<String> result = new LinkedList<>();
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/wall.get")
                .setParameter("owner_id", "-" + 119022967)
                .setParameter("acces_token", ACCES_TOKEN)
                .setParameter("count", "10");

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

            JSONParser parser   = new JSONParser();

            try {

                JSONObject jsonResp  = (JSONObject) parser.parse(content);
                JSONArray postsList = (JSONArray) jsonResp.get("response");
                JSONObject unicPost  = null;
                long id;

                for (int i=1; i < postsList.size(); i++) {
                    unicPost = (JSONObject) postsList.get(i);
                    id = (long) unicPost.get("id");
                    result.add(String.valueOf(id));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
    //https://oauth.vk.com/authorize?client_id=119022967&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,offline&response_type=token&v=5.50
    private static void cleanUpTest() {
        for (String s : getPublishedId()) {
            cleanUpTest(s);
        }
    }
    private static void cleanUpTest(String post_id) {
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
