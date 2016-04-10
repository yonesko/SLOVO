package main;

import data.dao.FetchFreq;
import data.dao.FetchWiki;
import data.model.FreqEntity;
import data.model.WallPost;
import data.model.WordInfo;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long SIX_H = TimeUnit.HOURS.toSeconds(6);
    private static long lastDate = 0;

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
        List<FreqEntity> candidates = new LinkedList<>();
        List<WallPost> published = new ArrayList<>();
        Random random = new Random();
        FreqEntity fe;
        WordInfo wordInfo;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPosts("owner");
        lastDate = getLastDate();

        //remove published
        for (WallPost s : published)
            if (candidates.contains(FetchFreq.get(s.getWord())))
                candidates.remove(FetchFreq.get(s.getWord()));

        //publish portion
        for (int i = 0; i < 6 && candidates.size() > 0; i++) {
            System.out.println("---------" + i + "----------");
            fe = candidates.get(random.nextInt(candidates.size()));
            System.out.println(fe);

            wordInfo = FetchWiki.findWord(fe.getWord());

            if (wordInfo != null && wordInfo.isPublishable()) {
                if (VK.wallPost(wordInfo.toPublish(), lastDate + SIX_H * (i + 1)))
                    candidates.remove(fe);
            }
            else {
                i--;
                System.out.println("is not publishable");
            }
            System.out.println("-------------------");
        }
    }
    private static long getLastDate() throws ParseException, IOException, URISyntaxException {
        List<WallPost> wall = VK.getPosts("owner");
        long result = 0;

        if (wall == null || wall.size() == 0)
            result = 0;
        else
            for (WallPost post : wall)
                if (post.getDate() > result)
                    result = post.getDate();

        return result;
    }
}
//TODO advertisment
//TODO images
//TODO schedule