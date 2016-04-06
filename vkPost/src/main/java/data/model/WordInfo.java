package data.model;

/**
 * Created by gleb on 04.04.16.
 */
public class WordInfo {
    private final String name;
    private final String meaning;
    private final String etymology;

    public WordInfo(String name, String meaning, String etymology) {
        this.name = name;
        this.meaning = meaning;
        this.etymology = etymology;
    }

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getEtymology() {
        return etymology;
    }

    @Override
    public String toString() {
        return "WordInfo{" +
                "name='" + name + '\'' +
                ", meaning='" + meaning + '\'' +
                ", etymology='" + etymology + '\'' +
                '}';
    }

    public String toPublish() {
        StringBuilder result = new StringBuilder();
        result.append(name);
        result.append('\n');
        result.append(meaning);
        result.append('\n');
//        result.append(etymology);
        return result.toString();
    }
}
