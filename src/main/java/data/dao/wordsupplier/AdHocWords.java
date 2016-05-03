package data.dao.wordsupplier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gleb on 03.05.16.
 */
public class AdHocWords implements WordSupplier {
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList(new String[]{
        "54gv356b",
        "уд",
        "кот",
    }));
    @Override
    public String nextWord() {
        return wantedWords.poll();
    }
}
