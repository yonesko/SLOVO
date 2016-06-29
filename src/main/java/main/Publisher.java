package main;

import data.model.Word;
import data.wordsupplier.AlgoWS;
import data.wordsupplier.WordSupplier;
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

    private WordSupplier wordSupplier;

    public Publisher() {
        portionSize = Long.parseLong(PropManager.getProp("postAlogo.portionSize"));
        nextPostAdjuster = new NextPostAdjuster();
        wordSupplier = new AlgoWS();
    }

    public void postPortion() {
        Word nextPost;
        LocalDateTime nextPostTime;

        PropManager.getProps().list(System.out);

        //set most recent post time
        nextPostTime = LocalDateTime.parse(PropManager.getProp("postAlogo.startDateTime"));

        //publish portion
        for (int i = 0; i < portionSize && (nextPost = wordSupplier.nextWord()) != null; i++) {
            boolean isPosted = false, isPublishable;

            if(isPublishable = nextPost.isPublishable()) {
                //post in public
                isPosted = VK.wallPost(nextPost.toPublish(), nextPostTime);
                //move time forward
                nextPostTime = nextPostTime.with(nextPostAdjuster);
            } else {
                i--;
            }
//TODO fix logginig now i prints not the same it was on the moment of publishing
            System.out.println(String.format(
                    "--------- %d: %s ---------\n" +
                    "isPublishable=%s\n" +
                    "isPosted=%s\n" +
                    "-------------------", i, nextPost, isPublishable, isPosted));

        }
    }

}
