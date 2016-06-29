package data.wordsupplier;

import data.model.Word;
import main.PropManager;

/**N
 * Implements logic of word choosing for publisher
 * Created by gleb on 29 June 2016.
 */
public class AlgoWS implements WordSupplier {
    private final FunnyAphorismWS funnyAphorismWS;
    private final WiktionaryWS wiktionaryWS;
    private int type;

    public AlgoWS() {
        try {
            type = Integer.parseInt(PropManager.getProp("postAlgo.wordChooser.algo.type"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            type = 1;
        }
        funnyAphorismWS = FunnyAphorismWS.getInstance();
        wiktionaryWS = new WiktionaryWS();
    }

    @Override
    public Word nextWord() {
        WordSupplier wordSupplier;

        switch (type) {
            default:
            case 1:
                wordSupplier =  new EvenWS(funnyAphorismWS, wiktionaryWS);
                break;
            case 2:
                wordSupplier = wiktionaryWS;
                break;
            case 3:
                wordSupplier = funnyAphorismWS;
                break;
        }

        return wordSupplier.nextWord();
    }

    @Override
    public void resultOK() {

    }
}
