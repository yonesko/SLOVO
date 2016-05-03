package data.model;

/**
 * Created by gleb on 03.04.16.
 */
public class FreqEntity {
    private final String name;
    private final String pos;
    private final double imp;

    public FreqEntity(String word, String pos, double imp) {
        this.name = word;
        this.pos = pos;
        this.imp = imp;
    }

    public String getName() {
        return name;
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
                "name='" + name + '\'' +
                ", pos='" + pos + '\'' +
                ", imp=" + imp +
                '}';
    }
}
