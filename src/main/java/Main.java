import data.dao.FetchWiki;
import data.dao.FreqCSVReader;
import data.model.FreqEntity;
import data.model.WordInfo;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final long SIX_H = TimeUnit.HOURS.toSeconds(6);

    public static void main(String[] args) {
        FreqEntity fe;
        WordInfo wordInfo;

        for (int i = 0; i < 6; i++) {
            System.out.println("-------------------");
            fe = FreqCSVReader.getRandom();
            System.out.println(fe);
            wordInfo = FetchWiki.findWord(fe.getWord());
            System.out.println(wordInfo);
            if (wordInfo != null && wordInfo.isPublishable()) {
                try {
                    VK.wallPost(wordInfo.toPublish(), System.currentTimeMillis()/1_000L + SIX_H * i);
                } catch (Exception e) {
                    i--;
                    e.printStackTrace();
                }
            } else {
                i--;
                System.out.println("is not publishable");
            }
            System.out.println("-------------------");
        }
    }
}
//TODO history of post
//TODO images
//TODO log4j + mailto