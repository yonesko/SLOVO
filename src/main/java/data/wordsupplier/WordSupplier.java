package data.wordsupplier;

import data.model.Word;

/**
 * Created by gleb on 03.05.16.
 */
public interface WordSupplier {
    /**
     * @return null only if supplier is empty
     */
    Word nextWord();
}
