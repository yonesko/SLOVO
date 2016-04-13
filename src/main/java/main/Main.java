package main;

import data.dao.FetchFreq;
import data.dao.FetchWiki;
import data.model.FreqEntity;
import data.model.WallPost;
import data.model.WordInfo;
import org.json.simple.parser.ParseException;
import util.NextPostAdjuster;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

/**
 * User specifies lastPostTime and the program generates PORTION_SIZE posts with hour {@link NextPostAdjuster delay}.<br>
 * If post at nextPostTime fails nextPostTime is moved forward
 */
public class Main {
    private static final long PORTION_SIZE = 5;
    private static List<FreqEntity> candidates;
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList(new String[]{

    }));

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        List<WallPost> published = new ArrayList<>();
        WordInfo nextPost;
        LocalDateTime lastPostTime, nextPostTime;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPosts("owner");


        lastPostTime = nextPostTime = LocalDateTime.of(2016, Month.APRIL, 14, 0, 0);
        System.out.println("lastPostTime is " + lastPostTime);

        //remove already published from candidates
        for (WallPost s : published)
            if (candidates.contains(FetchFreq.get(s.getWord())))
                candidates.remove(FetchFreq.get(s.getWord()));

        System.out.println("candidates size=" +candidates.size());

        nextPostTime = nextPostTime.with(new NextPostAdjuster());
        //publish portion
        for (int i = 0; i < PORTION_SIZE && candidates.size() > 0; i++) {
            System.out.println("---------" + i + "----------");

            nextPost = nextWord();

            if (nextPost != null) {
                if (nextPost.isPublishable()) {
                    System.out.println(String.format(
                            "Trying to publish %s to date %s",
                            nextPost.getName(),
                            nextPostTime));

                    boolean isPosted = VK.wallPost(nextPost.toPublish(), nextPostTime);

                    System.out.println("is posted :" + isPosted);

                    nextPostTime = nextPostTime.with(new NextPostAdjuster());
                } else {
                    i--;
                    System.out.println("NOT OK: word " + nextPost.getName() + " is not publishable");
                }
            }
            else {
                i--;
                System.out.println("NOT OK: word is absent from wiki");
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
/*
TODO
Фразеологизмы и устойчивые сочетания

  *  как бык поссал
  *  мало — на раз поссать
  *  поссать в бане
  *  поссать от души / от души поссать
  *



Пословицы и поговорки
 */