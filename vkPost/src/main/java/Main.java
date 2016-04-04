import data.dao.FetchWiki;
import data.dao.FreqCSVReader;
import data.model.FreqEntity;
import data.model.WordInfo;

public class Main {
    public static void main(String[] args) throws Exception {
        FreqEntity fe;
        WordInfo wordInfo;

//        fe = FreqCSVReader.getRandom();
//        System.out.println(fe);
        wordInfo = FetchWiki.findWord("тибетский");
        System.out.println(wordInfo);
    }
}
