package data.dao.wordsupplier;

/**
 * Created by gleb on 03.05.16.
 */
public interface WordSupplier {
    /**
     * @return null only if supplier is empty
     */
    String nextWord();
}
