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
        Statement stmt = getConn().createStatement();

        ResultSet results = stmt.executeQuery("SELECT Lemma, PoS, Freq FROM freqrnc2011 order by Freq ");

        while (results.next())
            System.out.println(results.getString("Lemma")+" "+
                    results.getString("PoS")+" "+
                    results.getDouble(("Freq")));
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
