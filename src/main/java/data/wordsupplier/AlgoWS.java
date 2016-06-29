package data.wordsupplier;

import data.model.Word;
import main.PropManager;

/**N
 * Implements logic of word choosing for publisher
 * Created by gleb on 29 June 2016.
 */
public class AlgoWS implements WordSupplier {
    private int type;

    public AlgoWS() {
        try {
            type = Integer.parseInt(PropManager.getProp("postAlgo.wordChooser.algo.type"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            type = 1;
        }
    }

    @Override
    public Word nextWord() {
        WordSupplier wordSupplier;

        switch (type) {
            default:
            case 1:
                wordSupplier = new EvenWS(FunnyAphorismWS.getInstance(), new WiktionaryWS());
                break;
            case 2:
                wordSupplier = new WiktionaryWS();
                break;
            case 3:
                wordSupplier = FunnyAphorismWS.getInstance();
                break;
        }

        return wordSupplier.nextWord();
    }

    @Override
    public void resultOK() {

    }
}
