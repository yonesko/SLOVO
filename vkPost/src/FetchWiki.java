import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gleb on 31.03.16.
 */
public class FetchWiki {
    public static void main(String[] args) throws Exception {
            post();
    }

    private static void post() throws Exception {

        String url = "https://ru.wiktionary.org/w/api.php?action=query&format=xml&uselang=ru&prop=revisions%7Cinfo&titles=%D1%8F%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE&formatversion=latest&rvprop=content&inprop=url";




        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("POST");

        //add request header
//        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
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

/*
https://ru.wiktionary.org/w/api.php?action=query&format=json&uselang=ru&prop=revisions%7Cinfo&titles=pizza&formatversion=latest&rvprop=content&inprop=url
 */
