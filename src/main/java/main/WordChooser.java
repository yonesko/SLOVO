package main;

import data.model.Word;
import data.wordsupplier.AphorismWS;
import data.wordsupplier.WiktionaryWS;
import data.wordsupplier.WordSupplier;

import java.util.Random;

/**
 * Implements logic of choosing word from word suppliers.
 * Created by gleb on 03.05.16.
 */
public class WordChooser {
    private WordSupplier ws[] = {
            new WiktionaryWS(),
            new AphorismWS()
    };
    /**
     * @return null only if suppliers are empty
     */
    public Word nextWord() {
        Random ran = new Random();
        Word result = null;

        while (result == null)
            result = ws[ran.nextInt(ws.length)].nextWord();

        return result;
    }
}
