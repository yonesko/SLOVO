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
        lemmaFetcher = LemmaFetcher.getInstance();
    }

    @Override
    public Word nextWord() {
        WikiWord result;

        do {
            String lemma = lemmaFetcher.nextLemma();

            if (lemma == null) {
                result = null;
                break;
            }

            result = wikiFetcher.findWord(lemma);
        } while (result == null);

        return result;
    }

    @Override
    public void resultOK() {

    }
}