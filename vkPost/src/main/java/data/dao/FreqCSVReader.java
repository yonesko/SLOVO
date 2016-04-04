package data.dao;

import data.model.FreqEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by gleb on 03.04.16.
 */
public class FreqCSVReader {
    private static final Path pFreq = Paths.get("resources/freqrnc2011.csv");
    private static List<FreqEntity> freqDict;

    private static void initDict() {
        freqDict = new LinkedList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(pFreq);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (scanner.nextLine(); scanner.hasNextLine(); scanner.nextLine())
            freqDict.add(new FreqEntity(scanner.next(), scanner.next(), scanner.nextDouble()));
    }

    private FreqCSVReader() {}

    public static List<FreqEntity> getFreqDict() {
        if (freqDict == null)
            initDict();
        return freqDict;
    }

    public static FreqEntity getRandom(String partOfSpeech) {
        List<FreqEntity> l = getFreqDict();
        FreqEntity result;
        Random random = new Random();

        do {
            result = l.get(random.nextInt(l.size()));
        }
        while (!result.getPos().equals(partOfSpeech));
            return result;
    }
    public static FreqEntity getRandom() {
        List<FreqEntity> l = getFreqDict();
        FreqEntity result;
        Random random = new Random();
        return l.get(random.nextInt(l.size()));
    }

    public static FreqEntity getTest() {
        return new FreqEntity("абонемент", "s", 1.8);
    }

}
