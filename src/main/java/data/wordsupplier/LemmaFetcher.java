package data.wordsupplier;

import main.PropManager;
import org.relique.jdbc.csv.CsvDriver;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

/**
 * Created by gleb on 16.06.16.
 */
public class LemmaFetcher {
    private ResultSet lemmas;
    private Queue<String> wantedLemmas = new LinkedList<>(
            Arrays.asList(
                    PropManager.getProp("LemmaFetcher.wantedLemmas").split(",")));

    private Connection conn;

    /**
     *
     * @return lemma or null if words is over
     */
    String nextLemma() {
        String result;

        result = nextWantedLemma();

        if (result == null) {
            do {
                try {
                    if (lemmas.next())
                        result = lemmas.getString("Lemma");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } while (result == null);
        }

        return result;
    }

    /**
     * @return null only if the list of wanted lemmas is empty
     */
    private String nextWantedLemma() {
        String result;

        do {
            result = wantedLemmas.poll();
            if (result != null && !result.isEmpty())
                break;
        } while (wantedLemmas.size() > 0);

        return result;
    }

    public LemmaFetcher() {
        Statement stmt;
        try {
            stmt = getConn().createStatement();
            lemmas = stmt.executeQuery("SELECT Lemma, PoS, Freq FROM freqrnc2011");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            DriverManager.registerDriver(new CsvDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConn() {
        Properties props = new Properties();
        props.put("separator", "\t");
        props.put("charset", "UTF-8");
        props.put("maxFileSize", 10000);

        String folder = null;
        try {
            folder = Paths.get(ClassLoader.getSystemResource("freqrnc2011.csv").toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            if (conn == null || conn.isClosed())
                conn = DriverManager.getConnection("jdbc:relique:csv:" + folder, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
