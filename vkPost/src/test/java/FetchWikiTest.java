import data.dao.FetchWiki;
import data.model.WordInfo;
import org.junit.Test;

/**
 * Created by gleb on 04.04.16.
 */
public class FetchWikiTest {

    @Test
    public void findWord() throws Exception {
        WordInfo wordInfo = FetchWiki.findWord("поношение");
        System.out.println(wordInfo);
    }
}