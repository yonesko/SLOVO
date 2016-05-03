package data.wordsupplier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gleb on 03.05.16.
 */
public class AdHocWS implements WordSupplier {
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList(new String[]{
        "этуаль",
        "шкода",
        "помпезный",
        "мерин",
        "",
        ""
    }));
    @Override
    public String nextWord() {
        String result;

        do {
            result = wantedWords.poll();
            if (result != null && !result.isEmpty())
                break;
        } while (wantedWords.size() > 0);

        return result;
    }
}
