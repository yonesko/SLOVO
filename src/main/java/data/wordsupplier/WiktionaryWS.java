package data.wordsupplier;

import data.model.WikiWord;
import data.model.Word;

import java.sql.SQLException;

/**
 * Frequency dictionary + Wiktionary
 */
public class WiktionaryWS implements WordSupplier {
    private WikiFetcher wikiFetcher;
    private LemmaFetcher lemmaFetcher;

    public static void main(String...args) throws SQLException {
        //TODO зябкий
    }

    public WiktionaryWS() {
        wikiFetcher = new WikiFetcher();
        lemmaFetcher = new LemmaFetcher();
    }

    @Override
    public Word nextWord() {
        WikiWord result;

        do {
            result = wikiFetcher.findWord(lemmaFetcher.nextLemma());
        } while (result == null);

        return result;
    }

    @Override
    public void resultOK() {

    }
}