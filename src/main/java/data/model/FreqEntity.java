package data.model;

/**
 * Created by gleb on 03.04.16.
 */
public class FreqEntity {
    private final String word;
    private final String pos;
    private final double imp;

    public FreqEntity(String word, String pos, double imp) {
        this.word = word;
        this.pos = pos;
        this.imp = imp;
    }

    public String getWord() {
        return word;
    }

    public String getPos() {
        return pos;
    }

    public double getImp() {
        return imp;
    }

    @Override
    public String toString() {
        return "data.model.FreqEntity{" +
                "word='" + word + '\'' +
                ", pos='" + pos + '\'' +
                ", imp=" + imp +
                '}';
    }
}
