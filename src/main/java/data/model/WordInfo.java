package data.model;

/**
 * Created by gleb on 04.04.16.
 */
public class WordInfo {
    private final String name;
    private final String meaning;
    private final String etymology;
    private static final int THRESHOLD = 60;

    public WordInfo(String name, String meaning, String etymology) {
        this.name = name;
        this.meaning = meaning == null ? null : meaning.trim();
        this.etymology = etymology == null ? null : etymology.trim();
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

    public boolean isPublishable() {
        return meaning != null && meaning.length() > THRESHOLD ||
                etymology != null && etymology.length() > THRESHOLD;
    }

    public String toPublish() {
        StringBuilder result = new StringBuilder();
        result.append(name.toUpperCase());
        if (meaning != null) {
            result.append('\n');
            result.append("ЗНАЧЕНИЕ:");
            result.append('\n');
            result.append(meaning);
        }
        if (etymology != null) {
            result.append('\n');
            result.append("ЭТИМОЛОГИЯ:");
            result.append('\n');
            result.append(etymology);
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordInfo wordInfo = (WordInfo) o;

        if (!name.equals(wordInfo.name)) return false;
        if (meaning != null ? !meaning.equals(wordInfo.meaning) : wordInfo.meaning != null) return false;
        return etymology != null ? etymology.equals(wordInfo.etymology) : wordInfo.etymology == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (meaning != null ? meaning.hashCode() : 0);
        result = 31 * result + (etymology != null ? etymology.hashCode() : 0);
        return result;
    }
}
