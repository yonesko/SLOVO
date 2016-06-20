package data.model;

/**
 * Created by gleb on 03.05.16.
 */
public class AphorismWord implements Word{
    private final String content;

    public AphorismWord(String content) {
        this.content = content;
    }


    @Override
    public boolean isPublishable() {
        return content != null && content.length() > 5;
    }

    @Override
    public String toPublish() {
        return content;
    }

    @Override
    public String toString() {
        int endIndex = 20;
        String result;

        if (content.length() > endIndex)
            result = content.substring(0, endIndex);
        else
            result = content;

        return result;
    }
}
