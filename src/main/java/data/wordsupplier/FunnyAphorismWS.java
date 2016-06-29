package data.wordsupplier;

import data.model.AphorismWord;
import data.model.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gleb on 03.05.16.
 */
public class FunnyAphorismWS implements WordSupplier{
    static FunnyAphorismWS instance;
    private List<Word> dict;

    public static void main(String...args) throws Throwable {
    }

    @Override
    public Word nextWord() {
        if (dict.size() == 0)
            return null;

        int ind = new Random().nextInt(dict.size());

        return dict.remove(ind);
    }

    @Override
    public void resultOK() {

    }

    private FunnyAphorismWS() {
        dict = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(
                            Paths.get(
                                    ClassLoader.getSystemResource("veselie_aforizmi.txt").toURI()).toFile()));
            String line;
            StringBuilder buf = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (buf.length() > 0)
                        dict.add(new AphorismWord(buf.toString().trim()));
                    buf.delete(0, buf.length());
                } else {
                    buf.append(line);
                    buf.append('\n');
                }
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized FunnyAphorismWS getInstance() {
        if (instance == null)
            instance = new FunnyAphorismWS();
        return instance;
    }
}
