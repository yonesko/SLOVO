package main;

import data.dao.FetchWiki;
import data.dao.wordsupplier.AdHocWords;
import data.dao.wordsupplier.FetchFreq;
import data.dao.wordsupplier.WordSupplier;
import data.model.WordInfo;

/**
 * Implements logic of choosing word from word suppliers.
 * Created by gleb on 03.05.16.
 */
public class WordChooser {
    /**
     * Word suppliers array represents priority.
     * 0 index for most priority.
     */
    private WordSupplier WSupps[] = {
            new AdHocWords(),
            new FetchFreq()
    };
    /**
     * @return null only if suppliers are empty
     */
    public WordInfo nextWordInfo() {
        WordInfo result = null;
        String word;

        while (result == null) {
            word = nextWord();
            if (word == null)
                break;
            else
                result = FetchWiki.findWord(word);
        }

        return result;
    }
    /**
     * @return null only if suppliers are empty
     */
    private String nextWord() {
        String result = null;
        for (WordSupplier ws : WSupps) {
            result = ws.nextWord();
            if (result != null)
                break;
        }
        return result;
    }
}
