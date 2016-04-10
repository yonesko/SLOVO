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

/**
 * User specifies lastDateMils and the program generates six posts with 5 hour delay
 */
public class Main {
    private static final long DELAY_H = 5;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList("аа", "мафия"));
    private static long lastDateMils = 0;
    private static List<FreqEntity> candidates;

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        List<WallPost> published = new ArrayList<>();
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
            wordInfo = nextWord();

            if (wordInfo != null && wordInfo.isPublishable()) {
                System.out.println(String.format(
                        "Trying to publish %s to date %s",
                        wordInfo.getName(),
                        sdf.format(new Date(lastDateMils + TimeUnit.HOURS.toMillis(DELAY_H)))));
                if (VK.wallPost(wordInfo.toPublish(), TimeUnit.MILLISECONDS.toSeconds(lastDateMils) + TimeUnit.HOURS.toSeconds(DELAY_H))) {
//                    candidates.remove(fe);
                    System.out.println("OK");
                    lastDateMils += TimeUnit.HOURS.toMillis(DELAY_H);
                }
            }
            else {
                i--;
                System.out.println("is not publishable");
            }
            System.out.println("-------------------");
        }
    }
    private static WordInfo nextWord() {
        WordInfo result = null;
        Random random = new Random();
        FreqEntity fe;

        if (wantedWords != null && wantedWords.size() > 0)
            fe = FetchFreq.get(wantedWords.poll());
        else
            fe = candidates.get(random.nextInt(candidates.size()));

        if (fe != null)
            result = FetchWiki.findWord(fe.getWord());
        return result;
    }
}
//TODO advertisment
//TODO images
//TODO schedule