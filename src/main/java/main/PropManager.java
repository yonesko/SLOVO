package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Properties;


/**
 * Created by gleb on 20 June 2016.
 */
public class PropManager {
    private static Properties props;

    public static String getProp(String key) {
        return props.getProperty(key);
    }

    private static Properties defaultProps() {
        Properties result = new Properties();

        result.setProperty("VK.ownerName", "119022967");//test owner_id by default
        result.setProperty("postAlogo.portionSize", "5");
        result.setProperty("postAlogo.startDateTime", "1970-06-30T22:55:00");
        result.setProperty("postAlgo.nextPostDelay", "4");
        result.setProperty("postAlgo.wordChooser.algo.type", "1");
        result.setProperty("LemmaFetcher.wantedLemmas", "");

        return result;
    }

    private PropManager() {
    }

    public static Properties getProps() {
        return props;
    }

    public static void init(String propFile) {
        props = new Properties(defaultProps());
        FileInputStream in;

//        URL resource = ClassLoader.getSystemResource("SLOVO.properties");

        try {
//            in = new FileInputStream(Paths.get(resource.toURI()).toFile());
            in = new FileInputStream(Paths.get(propFile).toFile());
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        //convert from standard properties charset real charset
        String lemmas = PropManager.getProp("LemmaFetcher.wantedLemmas");
        try {
            lemmas = new String(lemmas.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            lemmas = "";
        }
        props.setProperty("LemmaFetcher.wantedLemmas", lemmas);
    }
}
