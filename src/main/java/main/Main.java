package main;

public class  Main {
    static Publisher publisher;

    public static void main(String[] args) {
        String publProps;

        if (args.length <= 0 || (publProps = args[0]) == null || publProps.isEmpty()) {
            System.err.println("Specify publishing property file");
            System.exit(0);
        } else {
            PropManager.init(publProps);
            PropManager.getProps().list(System.out);

            publisher = new Publisher();
            publisher.postPortion();
        }
    }
}
//TODO use de.tudarmstadt.ukp.jwktl
//TODO images https://ru.wiktionary.org/wiki/%D0%B1%D0%BB%D0%BE%D1%85%D0%B0 канарейка
/*
TODO
Фразеологизмы и устойчивые сочетания

  *  как бык поссал
  *  мало — на раз поссать
  *  поссать в бане
  *  поссать от души / от души поссать
  *



Пословицы и поговорки
 */