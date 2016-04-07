import data.dao.FetchWiki;
import data.dao.FreqCSVReader;
import data.model.FreqEntity;
import data.model.WordInfo;

public class Main {
    public static void main(String[] args) {
        FreqEntity fe;
        WordInfo wordInfo;

        for (int i = 0; i < 1; i++) {
            fe = FreqCSVReader.getRandom();
            System.out.println(fe);
            wordInfo = FetchWiki.findWord(fe.getWord());
            System.out.println(wordInfo);
            if (wordInfo != null && wordInfo.isPublishable()) {
                try {
                    VK.wallPost(wordInfo.toPublish());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                System.out.println("is not publishable");
        }
    }
}
/*
 WordInfo{name='орк', meaning='1. злобное сказочное существо, выдуманное Толкином ◆ Орки тысячами гибли у
 стен Минас-Тирита.
 ', etymology='Происходит из англ. orc. Английское слово Толкин взял из др.-англ. orc
 «великан, демон».
 '}
 is not publishable
 */