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
    private static final long PORTION_SIZE = 6;
    private static List<FreqEntity> candidates;
    private static final Queue<String> wantedWords = new LinkedList<>(Arrays.asList(new String[]{
            "канитель",
            "осовелый"
    }));

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        List<WallPost> published = new ArrayList<>();
        WordInfo nextPost;
        LocalDateTime lastPostTime, nextPostTime;

        candidates = FetchFreq.getFreqDict();
        published = VK.getPosts("owner");
        //set most recent post time
        lastPostTime = nextPostTime = LocalDateTime.of(2016, Month.MAY, 1, 10, 0);
        System.out.println("lastPostTime is " + lastPostTime);
        //add manually added words to the candidate list
        for (String wantedWord : wantedWords)
            candidates.add(FetchFreq.get(wantedWord));
        //remove already published from candidates
        for (WallPost post : published) {
            FreqEntity word = FetchFreq.get(post.getWord());
            if (candidates.contains(word))
                candidates.remove(word);
        }

        System.out.println("candidates size=" +candidates.size());

        nextPostTime = nextPostTime.with(new NextPostAdjuster());
        //publish portion
        for (int i = 0; i < PORTION_SIZE && candidates.size() > 0; i++) {
            boolean isAppropriate = false, isPosted;
            System.out.println("---------" + i + "----------");

            nextPost = nextWord();
            isAppropriate = nextPost != null && nextPost.isPublishable();

            if(isAppropriate) {
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
        Random random = new Random();
        FreqEntity fe;

        if (wantedWords != null && wantedWords.size() > 0)
            fe = FetchFreq.getDirect(wantedWords.poll());
        else
            fe = candidates.get(random.nextInt(candidates.size()));

        if (fe != null)
            result = FetchWiki.findWord(fe.getWord());
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