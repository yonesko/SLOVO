package main;

import data.dao.FetchWiki;
import data.dao.FetchFreq;
import data.model.FreqEntity;
import data.model.WallPost;
import data.model.WordInfo;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long SIX_H = TimeUnit.HOURS.toSeconds(6);

    public static void main(String[] args) {
        List<FreqEntity> candidates = new LinkedList<>();
        List<WallPost> published = new ArrayList<>();
        Random random = new Random();
        FreqEntity fe;
        WordInfo wordInfo;
        WallPost lastPost;
        long lastPostDate;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPublished();
        lastPost = getLast(published);
        lastPostDate = lastPost == null ? System.currentTimeMillis()/1000L : lastPost.getDate();

        //remove published
        for (WallPost s : published)
            if (candidates.contains(FetchFreq.get(s.getWord())))
                candidates.remove(FetchFreq.get(s.getWord()));

        //publish portion
        for (int i = 0; i < 6 && candidates.size() > 0; i++) {
            System.out.println("-------------------");
            fe = candidates.get(random.nextInt(candidates.size()));
            System.out.println(fe);
            wordInfo = FetchWiki.findWord(fe.getWord());
            try {
                if (wordInfo != null && wordInfo.isPublishable())
                    VK.wallPost(wordInfo.toPublish(), lastPostDate + SIX_H * (i + 1));
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
    private static WallPost getLast(List<WallPost> wall) {
        WallPost result = null;
        long max = Long.MIN_VALUE;
        for (WallPost post : wall)
            if (post.getDate() > max)
                result = post;
        return result;
    }
}
//TODO advertisment
//TODO images
//TODO schedule