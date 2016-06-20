package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;


/**
 * Created by gleb on 20 June 2016.
 */
public class PropManager {
    private static Properties props;

    static {
        props = new Properties(defaultProps());
        FileInputStream in;

        URL resource = ClassLoader.getSystemResource("SLOVO.properties");

        try {
            in = new FileInputStream(Paths.get(resource.toURI()).toFile());
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static String getProp(String key) {
        return props.getProperty(key);
    }

    private static Properties defaultProps() {
        Properties result = new Properties();

        result.setProperty("postAlogo.portionSize", "5");
        result.setProperty("VK.ownerName", "119022967");//test owner_id by default
        result.setProperty("postAlgo.nextPostDelay", "4");
        result.setProperty("LemmaFetcher.wantedLemmas", "");
        result.setProperty("LemmaFetcher.wantedLemmas", "");
        result.setProperty("postAlgo.wordChooser.algo.type", "1");

        return result;
    }

    public static Properties getProps() {
        return props;
    }
}
