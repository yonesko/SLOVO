package data.model;

/**
 * Created by gleb on 04.04.16.
 */
public class WikiWord implements Word {
    private final String name;
    private final String meaning;
    private final String etymology;
    private final String syllables;
    private final String synonyms;

    public WikiWord(String name, String meaning, String etymology, String syllables, String synonyms) {
        this.name = name;
        this.meaning = meaning;
        this.etymology = etymology;
        this.syllables = syllables;
        this.synonyms = synonyms;
    }

    public boolean isPublishable() {
        return toPublish().length() >= name.length() * 2;
    }

    public String toPublish() {
        StringBuilder result = new StringBuilder();
        if (syllables != null && !syllables.isEmpty())
            result.append(syllables.toUpperCase());
        else
            result.append(name.toUpperCase());
        if (meaning != null && !meaning.isEmpty()) {
            result.append('\n');
            result.append("ЗНАЧЕНИЕ:");
            result.append('\n');
            result.append(meaning);
        }
        if (synonyms != null && !synonyms.isEmpty()) {
            result.append('\n');
            result.append("СИНОНИМЫ:");
            result.append('\n');
            result.append(synonyms);
        }
        if (etymology != null && !etymology.isEmpty() && etymology.length() >= 20 && etymology.length() <= 100) {
            result.append('\n');
            result.append("ЭТИМОЛОГИЯ:");
            result.append('\n');
            result.append(etymology);
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return name;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        WikiWord wordInfo = (WikiWord) o;
//
//        if (!name.equals(wordInfo.name)) return false;
//        if (meaning != null ? !meaning.equals(wordInfo.meaning) : wordInfo.meaning != null) return false;
//        return etymology != null ? etymology.equals(wordInfo.etymology) : wordInfo.etymology == null;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = name.hashCode();
//        result = 31 * result + (meaning != null ? meaning.hashCode() : 0);
//        result = 31 * result + (etymology != null ? etymology.hashCode() : 0);
//        return result;
//    }
}
