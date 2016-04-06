import data.dao.FetchWiki;
import data.model.FreqEntity;
import data.model.WordInfo;

import java.net.URLEncoder;

public class Main {
    public static void main(String[] args) throws Exception {
        FreqEntity fe;
        WordInfo wordInfo;

//        fe = FreqCSVReader.getRandom();
//        System.out.println(fe);
        wordInfo = FetchWiki.findWord("пафос");
        System.out.println(wordInfo.toPublish());

        VK.wallPost(wordInfo.toPublish());
    }
}