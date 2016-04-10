package main;

import data.dao.FetchFreq;
import data.dao.FetchWiki;
import data.model.FreqEntity;
import data.model.WallPost;
import data.model.WordInfo;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long DELAY_H = 5;
    private static long lastDateMils = 0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        List<FreqEntity> candidates = new LinkedList<>();
        List<WallPost> published = new ArrayList<>();
        Random random = new Random();
        FreqEntity fe;
        WordInfo wordInfo;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPosts("owner");
        lastDateMils = sdf.parse("10/04/2016 22:55:00").getTime();
        System.out.println("lastDateMils is " + sdf.format(new Date(lastDateMils)));

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
                if (VK.wallPost(wordInfo.toPublish(), TimeUnit.MILLISECONDS.toSeconds(lastDateMils) + TimeUnit.HOURS.toSeconds(DELAY_H))) {
                    candidates.remove(fe);
                    lastDateMils += TimeUnit.HOURS.toSeconds(DELAY_H);
                }
            }
            else {
                i--;
                System.out.println("is not publishable");
            }
            System.out.println("-------------------");
        }
    }
//    private static long getLastDate() throws ParseException, IOException, URISyntaxException {
//        List<WallPost> wall = VK.getPosts("owner");
//        long result = 0;
//
//        if (wall == null || wall.size() == 0)
//            result = 0;
//        else
//            for (WallPost post : wall)
//                if (post.getDate() > result)
//                    result = post.getDate();
//
//        return result;
//    }
}
//TODO advertisment
//TODO images
//TODO schedule