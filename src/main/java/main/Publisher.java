package main;

import data.model.Word;
import util.NextPostAdjuster;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;

/**
 * Created by gleb on 20 June 2016.
 * User specifies lastPostTime and the program generates portionSize posts with hour {@link NextPostAdjuster delay}.<br>
 * If post at nextPostTime fails nextPostTime is moved forward
 */
public class Publisher {
    private long portionSize;

    private TemporalAdjuster nextPostAdjuster;

    private WordChooser wordChooser;

    public Publisher() {
        portionSize = Long.parseLong(PropManager.getProp("postAlogo.portionSize"));
        nextPostAdjuster = new NextPostAdjuster();
        wordChooser = new WordChooser();
    }

    public void start() {
        Word nextPost;
        LocalDateTime nextPostTime;

        PropManager.getProps().list(System.out);

        //set most recent post time
        nextPostTime = LocalDateTime.parse(PropManager.getProp("postAlogo.startDateTime"));

        //publish portion
        for (int i = 0; i < portionSize && (nextPost = wordChooser.nextWord()) != null; i++) {
            boolean isPosted = false, isPublishable;

            if(isPublishable = nextPost.isPublishable()) {
                //post in public
                isPosted = VK.wallPost(nextPost.toPublish(), nextPostTime);
                //move time forward
                nextPostTime = nextPostTime.with(nextPostAdjuster);
            } else {
                i--;
            }

            System.out.println(String.format(
                    "--------- %d: %s ---------\n" +
                    "isPublishable=%s\n" +
                    "isPosted=%s\n" +
                    "-------------------", i, nextPost, isPublishable, isPosted));

        }
    }

}
