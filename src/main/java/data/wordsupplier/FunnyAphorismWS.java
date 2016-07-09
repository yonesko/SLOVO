package data.wordsupplier;

import data.model.AphorismWord;
import data.model.Word;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
            LineNumberReader reader = new LineNumberReader(
                    new InputStreamReader(this.getClass().getResourceAsStream("/veselie_aforizmi.txt")));

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized FunnyAphorismWS getInstance() {
        if (instance == null)
            instance = new FunnyAphorismWS();
        return instance;
    }
}
