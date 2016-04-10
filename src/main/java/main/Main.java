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
    private static final long PORTION_NUM = 5;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList("аа", "мафия"));
    private static List<FreqEntity> candidates;

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        List<WallPost> published = new ArrayList<>();
        WordInfo nextPost;
        long nextPostDateMils, lastDateMils;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPosts("owner");
        lastDateMils = sdf.parse("10/04/2016 22:55:00").getTime();
        System.out.println("lastDateMils is " + sdf.format(new Date(lastDateMils)));

        //remove already published from candidates
        for (WallPost s : published)
            if (candidates.contains(FetchFreq.get(s.getWord())))
                candidates.remove(FetchFreq.get(s.getWord()));

        //publish portion
        for (int i = 0; i < PORTION_NUM && candidates.size() > 0; i++) {
            System.out.println("---------" + i + "----------");
            nextPost = nextWord();

            if (nextPost != null)
                if (nextPost.isPublishable()) {
                    nextPostDateMils = lastDateMils + TimeUnit.HOURS.toMillis(DELAY_H);
                    System.out.println(String.format(
                            "Trying to publish %s to date %s",
                            nextPost.getName(),
                            sdf.format(new Date(nextPostDateMils))));
                    if (VK.wallPost(nextPost.toPublish(), TimeUnit.MILLISECONDS.toSeconds(nextPostDateMils))) {
//                    candidates.remove(fe);
                        System.out.println("OK");
                        lastDateMils += TimeUnit.HOURS.toMillis(DELAY_H);
                    } else
                        System.out.println("NOT OK");
                } else {
                    i--;
                    System.out.println(nextPost.getName() + " is not publishable");
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