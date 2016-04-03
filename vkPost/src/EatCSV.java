import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by gleb on 03.04.16.
 */
public class EatCSV {
    private static Path pFreq = Paths.get("resources/freqrnc2011.csv");

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(pFreq);
        List<FreqEntity> lems = new ArrayList<>();


        for (scanner.nextLine(); scanner.hasNextLine(); scanner.nextLine())
            lems.add(new FreqEntity(scanner.next(), scanner.next(), scanner.nextDouble()));

        System.out.println(lems.get(4734));

    }

    public static FreqEntity getTest() {
        return new FreqEntity("абонемент", "s", 1.8);
    }

}
