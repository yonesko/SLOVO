package data.wordsupplier;

/**
 * Created by gleb on 29 June 2016.
 */

import data.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * randomly chooses words from word suppliers
 */
public class EvenWS implements WordSupplier {
    List<WordSupplier> wordSupplierList;
    Random random;

    public EvenWS() {
        wordSupplierList = new ArrayList<>();
        random = new Random();
    }

    public EvenWS(WordSupplier...wordSuppliers) {
        this();
        if (wordSuppliers != null)
            for (WordSupplier wordSupplier : wordSuppliers)
                wordSupplierList.add(wordSupplier);
    }

    public void add(WordSupplier element) {
        wordSupplierList.add(element);
    }

    @Override
    public Word nextWord() {
        if (wordSupplierList.size() == 0)
            return null;

        int ind = random.nextInt(wordSupplierList.size());

        return wordSupplierList.get(ind).nextWord();
    }

    @Override
    public void resultOK() {

    }
}
