package data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by gleb on 09.04.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WallPost {
    private long id;
    private long date;
    private String text;

    public WallPost(long id, long date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public WallPost() {
    }

    @Override
    public String toString() {
        return "WallPost{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
