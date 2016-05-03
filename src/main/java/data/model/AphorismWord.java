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
        return content != null & !content.isEmpty();
    }

    @Override
    public String toPublish() {
        return content;
    }
}
