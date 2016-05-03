package main;

import data.model.WordInfo;
import org.json.simple.parser.ParseException;
import util.NextPostAdjuster;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * User specifies lastPostTime and the program generates PORTION_SIZE posts with hour {@link NextPostAdjuster delay}.<br>
 * If post at nextPostTime fails nextPostTime is moved forward
 */
public class Main {
    private static final long PORTION_SIZE = 5;

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException, java.text.ParseException {
        WordChooser wc = new WordChooser();
        WordInfo nextPost;
        LocalDateTime lastPostTime, nextPostTime;

        //set most recent post time
        lastPostTime = nextPostTime = LocalDateTime.of(2016, Month.MAY, 5, 16, 0);
        System.out.println("lastPostTime is " + lastPostTime);

        //publish portion
        nextPostTime = nextPostTime.with(new NextPostAdjuster());
        for (int i = 0; i < PORTION_SIZE && (nextPost = wc.nextWordInfo()) != null; i++) {
            boolean isPosted;
            System.out.println("---------" + i + "----------");

            if(nextPost.isPublishable()) {
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