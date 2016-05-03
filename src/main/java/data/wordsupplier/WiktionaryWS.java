package data.wordsupplier;

import data.model.WikiWord;
import data.model.Word;

import java.sql.*;
import java.util.*;

/**
 * Frequency dictionary + Wikrionary
 */
public class WiktionaryWS implements WordSupplier {
    private static List<String> lemmas;
    private static Connection conn;
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList(new String[]{
    }));

    public static void main(String...args) throws SQLException {
        for (String freqEntity : lemmas)
            if (freqEntity.equalsIgnoreCase("канитель"))
                System.out.println(freqEntity);
    }

    @Override
    public Word nextWord() {
        WikiWord result;

        do {
            result = FetchWiki.findWord(nextLemma());
        } while (result == null);

        return result;
    }

    private String nextLemma() {
        String result;
        Random random = new Random();

        result = nextWantedLemma();
        if (result == null)
            result = lemmas.get(random.nextInt(lemmas.size()));

        return result;
    }

    static {
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");

            initDict();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConn() {
        Properties props = new java.util.Properties();
        props.put("separator", "\t");
        props.put("charset", "UTF-8");
        props.put("maxFileSize", 10000);
        try {
            if (conn == null || conn.isClosed())
                conn = DriverManager.getConnection("jdbc:relique:csv:" + "resources", props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;

    }

    private static void initDict() throws ClassNotFoundException, SQLException {
        lemmas = new LinkedList<>();

        Statement stmt = getConn().createStatement();

        ResultSet results = stmt.executeQuery("SELECT Lemma, PoS, Freq FROM freqrnc2011");

        while (results.next())
            if (results.getDouble(("Freq")) < 1.0)
                lemmas.add(results.getString("Lemma"));

        getConn().close();
    }

    /**
     * @return null only if empty
     */
    private String nextWantedLemma() {
        String result;

        do {
            result = wantedWords.poll();
            if (result != null && !result.isEmpty())
                break;
        } while (wantedWords.size() > 0);

        return result;
    }
}