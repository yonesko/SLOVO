package data.wordsupplier;

import org.relique.io.TableReader;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

/**
 * Created by gleb on 09 July 2016.
 */
public class FileInZipReader implements TableReader {
    @Override
    public Reader getReader(Statement statement, String tableName) throws SQLException {
        return new InputStreamReader(this.getClass().getResourceAsStream(String.format("/%s.csv", tableName)));
    }

    @Override
    public List<String> getTableNames(Connection connection) throws SQLException {
        return Collections.singletonList("freqrnc2011.csv");
    }
}
