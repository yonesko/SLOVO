import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gleb on 31.03.16.
 */
public class FetchWiki {
    public static void main(String[] args) throws Exception {
        String page = getPage();

        //get content between two regexps
        String content = page.split("(?m)^Русский")[1].split("(?m)^Источник")[0];
        System.out.printf(content);
    }

    private static String getPage() throws IOException {
        StringBuilder sbPage = new StringBuilder();
        String cmds[] = {
                "/home/gleb/Documents/SLOVO/vkPost/resources/getPage.sh",
                "абонемент"
        };
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmds);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        String s = null;
        while ((s = stdInput.readLine()) != null) {
            sbPage.append(s).append('\n');
        }

        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.err.println(s);
        }

        return sbPage.toString();
    }

    private static void post(String title) throws Exception {

        String url = "https://ru.wiktionary.org/w/api.php?" +
                "action=query" +
                "&format=xml" +
                "&uselang=ru" +
                "&prop=revisions%7Cinfo" +
                "&titles=" + title +
                "&formatversion=latest" +
                "&rvprop=content" +
                "&inprop=url";




        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");

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
