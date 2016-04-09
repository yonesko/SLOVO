package main;

import data.dao.FetchWiki;
import data.dao.FetchFreq;
import data.model.FreqEntity;
import data.model.WordInfo;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long SIX_H = TimeUnit.HOURS.toSeconds(6);

    public static void main(String[] args) {
        List<FreqEntity> candidates = new LinkedList<>();
        List<String> published = new ArrayList<>();
        Random random = new Random();
        FreqEntity fe;
        WordInfo wordInfo;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPublished();

        //remove published
        for (String s : published)
            if (candidates.contains(FetchFreq.get(s)))
                candidates.remove(FetchFreq.get(s));

        //publish portion
        for (int i = 0; i < 6 && candidates.size() > 0; i++) {
            System.out.println("-------------------");
            fe = candidates.get(random.nextInt(candidates.size()));
            System.out.println(fe);
            wordInfo = FetchWiki.findWord(fe.getWord());
            System.out.println(wordInfo);
            try {
                if (wordInfo != null && wordInfo.isPublishable())
                    VK.wallPost(wordInfo.toPublish(), System.currentTimeMillis() / 1_000L + SIX_H * i);
                else {
                    i--;
                    System.out.println("is not publishable");
                }
            } catch (Exception e) {
                i--;
                e.printStackTrace();
            } finally {
                candidates.remove(fe);
            }
            System.out.println("-------------------");
        }
    }
}
//TODO advertisment
//TODO images
//TODO schedule