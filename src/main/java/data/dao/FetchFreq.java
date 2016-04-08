package data.dao;

import data.model.FreqEntity;
import org.relique.jdbc.csv.CsvDriver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by gleb on 03.04.16.
 */
public class FetchFreq {
    private static List<FreqEntity> freqDict;

    public static void main(String...args) {
    }

    static {
        initDict();
    }

    private static void initDict() {
        freqDict = new LinkedList<>();
        try {
            Properties props = new java.util.Properties();

            props.put("separator", "\t");                // separator is a bar
            props.put("charset", "UTF-8");         // file encoding is "ISO-8859-2"
            props.put("maxFileSize", 10000);            // max size of files in bytes.

            Class.forName("org.relique.jdbc.csv.CsvDriver");

            Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + "resources", props);

            Statement stmt = conn.createStatement();

            ResultSet results = stmt.executeQuery("SELECT Lemma, PoS, Freq FROM freqrnc2011");

            while (results.next())
                freqDict.add(new FreqEntity(
                    results.getString("Lemma"),
                    results.getString("PoS"),
                    results.getDouble(("Freq"))));

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FetchFreq() {}

    public static FreqEntity getRandom() {
        Random random = new Random();
        return freqDict.get(random.nextInt(freqDict.size()));
    }
}
