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
    private static final long PORTION_SIZE = 10;
    private static List<String> candidates;
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList(new String[]{
    }));

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        WordInfo nextPost;
        LocalDateTime lastPostTime, nextPostTime;

        //set most recent post time
        lastPostTime = nextPostTime = LocalDateTime.of(2016, Month.MAY, 4, 0, 0);
        System.out.println("lastPostTime is " + lastPostTime);

        candidates = FetchFreq.getNameDict();
        System.out.println("candidates size=" +candidates.size());

        //publish portion
        nextPostTime = nextPostTime.with(new NextPostAdjuster());
        for (int i = 0; i < PORTION_SIZE && candidates.size() > 0; i++) {
            boolean isPosted;
            System.out.println("---------" + i + "----------");

            nextPost = nextWord();

            if(nextPost != null && nextPost.isPublishable()) {
                System.out.println(String.format(
                        "Trying to publish %s to date %s",
                        nextPost.getName(),
                        nextPostTime));
                //post in public
                isPosted = VK.wallPost(nextPost.toPublish(), nextPostTime);
                //move time forward
                nextPostTime = nextPostTime.with(new NextPostAdjuster());

                System.out.println("is posted :" + isPosted);
            } else {
                i--;
            }

            System.out.println("-------------------");
        }
    }
    private static WordInfo nextWord() {
        WordInfo result = null;
        String word;
        Random random = new Random();

        if (wantedWords != null && wantedWords.size() > 0)
            word = wantedWords.poll();
        else
            word = candidates.get(random.nextInt(candidates.size()));

        if (word != null && !word.isEmpty())
            result = FetchWiki.findWord(word);
        return result;
    }
}
//TODO images https://ru.wiktionary.org/wiki/%D0%B1%D0%BB%D0%BE%D1%85%D0%B0 канарейка
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