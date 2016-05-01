package data.dao;

import data.model.FreqEntity;

import java.sql.*;
import java.util.*;

/**
 * Created by gleb on 03.04.16.
 */
public class FetchFreq {
    private static List<FreqEntity> freqDict;
    private static Connection conn;

    public static void main(String...args) throws SQLException {
        for (FreqEntity freqEntity : freqDict)
            if (freqEntity.getWord().equalsIgnoreCase("канитель"))
                System.out.println(freqEntity);

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

    public static List<FreqEntity> getFreqDict() {
        return new ArrayList<>(freqDict);
    }

    /**
     * Searches in whole file
     * @return word or null if doesn't exists
     */
    public static FreqEntity getDirect(String word) {
        FreqEntity result = null;
        Statement stmt = null;
        try {
            stmt = getConn().createStatement();
            ResultSet data = stmt.executeQuery(String.format("SELECT Lemma, PoS, Freq FROM freqrnc2011 " +
                    "WHERE LOWER('%s') = LOWER(Lemma)",
                    word));

            while (data.next())
                result = new FreqEntity(
                        data.getString("Lemma"),
                        data.getString("PoS"),
                        data.getDouble(("Freq")));

            getConn().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Searches in list which cat be filtered (by Freq for example)
     * @return word or null if doesn't exists
     */
    public static FreqEntity get(String word) {
        for (FreqEntity freqEntity : freqDict)
            if (freqEntity.getWord().equalsIgnoreCase(word))
                return freqEntity;
        return null;
    }

    private static void initDict() throws ClassNotFoundException, SQLException {
        freqDict = new LinkedList<>();

        Statement stmt = getConn().createStatement();

        ResultSet results = stmt.executeQuery("SELECT Lemma, PoS, Freq FROM freqrnc2011");

        while (results.next())
            if (results.getDouble(("Freq")) < 1.0)
                freqDict.add(new FreqEntity(
                        results.getString("Lemma"),
                        results.getString("PoS"),
                        results.getDouble(("Freq"))));

        getConn().close();
    }

    private FetchFreq() {}

    public static FreqEntity getRandom() {
        Random random = new Random();
        return freqDict.get(random.nextInt(freqDict.size()));
    }
}
