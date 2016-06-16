package data.wordsupplier;

import data.model.AphorismWord;
import data.model.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gleb on 03.05.16.
 */
public class FunnyAphorismWS implements WordSupplier{
    private static List<Word> dict = new ArrayList<>();//TODO to fetcher
    @Override
    public Word nextWord() {
       return dict.get(new Random().nextInt(dict.size()));
    }

    @Override
    public void resultOK() {

    }

    public static void main(String...args) throws Throwable {
        new FunnyAphorismWS().nextWord();
    }

    static {
        try {
            initDict();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initDict() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/veselie_aforizmi.txt"));
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

    }
}
