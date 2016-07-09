package data.wordsupplier;

import main.PropManager;
import org.relique.jdbc.csv.CsvDriver;

import java.sql.*;
import java.util.*;

/**
 * Created by gleb on 16.06.16.
 */
public class LemmaFetcher {
    private static LemmaFetcher instance;
    private ArrayList<String> lemmas;
    private Queue<String> wantedLemmas = new LinkedList<>(
            Arrays.asList(
                    PropManager.getProp("LemmaFetcher.wantedLemmas").split(",")));

    private Connection conn;

    /**
     * @return lemma or null if words is over
     */
    String nextLemma() {
        String result;

        result = nextWantedLemma();

        if (result == null) {
            if (lemmas.size() == 0)
                return null;
            int ind = new Random().nextInt(lemmas.size());
            result = lemmas.remove(ind);
        }

        return result;
    }

    public static synchronized LemmaFetcher getInstance() {
        if (instance == null)
            instance = new LemmaFetcher();
        return instance;
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

    private LemmaFetcher() {
        Statement stmt;
        ResultSet resultSet;
        lemmas = new ArrayList<>(53_000);
        try {
            stmt = getConn().createStatement();
            resultSet = stmt.executeQuery("SELECT Lemma, PoS, Freq FROM freqrnc2011");

            while (resultSet.next())
                lemmas.add(resultSet.getString("Lemma"));

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

        try {
            if (conn == null || conn.isClosed())
                conn = DriverManager.getConnection("jdbc:relique:csv:class:" + FileInZipReader.class.getName(), props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
