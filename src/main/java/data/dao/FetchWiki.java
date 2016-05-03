package data.dao;

import data.model.WordInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by gleb on 31.03.16.
 */
public class FetchWiki {
    public static void main(String[] args) throws Exception {
//        PrintWriter writer;
//        for (int i = 0; i < 5; i++) {
//            String cont = getContent(FetchFreq.getRandom().getName());
//            writer = new PrintWriter(i + ".txt");
//            writer.println(cont);
//            writer.flush();
//        }

        System.out.println(findWord("переориентировать").toPublish());

    }
    /**
     * Makes http query to the wiktionary and parse page.<br>
     * If word exists at wiktionary and at least one parse is successful
     * return word; else null.
     * @param word to find at wiktionary
     * @return
     * word or null if fail
     */
    public static WordInfo findWord(String word) {
        word = word.toLowerCase();
        String content = null;
        WordInfo result = null;
        try {
            content = getContent(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content != null)
            result = new WordInfo(word,
                    parseMeaning(content),
                    parseEtymology(content),
                    parseSyllables(content, word));
        return result;
    }

    private static String getContent(String word) throws IOException {
        StringBuilder sbPage = new StringBuilder();
        String result = null;
        String cmds[] = {
                "resources/getPage.sh",
                word
        };
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmds);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        String s;
        while ((s = stdInput.readLine()) != null)
            sbPage.append(s).append('\n');

        while ((s = stdError.readLine()) != null)
            System.err.println(s);

        //get content between two regexps
        String page = sbPage.toString();
        if (page.length() != 0) {
            String a[] = page.split("(?m)^Русский");
            if (a.length > 1) {
                result = a[1].split("(?m)^Источник")[0];
                result = result.replaceAll("\\[править\\]", "");
                //prevent VK markup expanding
                result = result.replaceAll("\\*", "* ");
                result = result.replaceAll("◆\\s*Не\\s+указан\\s+пример\\s+употребления\\s+\\(см\\.\\s+рекомендации\\)\\.", "");
                result = result.replaceAll("\\s*\\(цитата\\s+из\\s+Национального\\s+корпуса\\s+русского\\s+языка,\\s+см\\.\\s+Список\\s+литературы\\)", "");
            }
        }
        return result;
    }
    /**
     * extracts meaning paragraph of the page.<br>
     * Considered that paragraph is text surrounded with empty lines followed by line starting with para.<br>
     * Para is the only word at this line.
     * @return meaning or null if doesn't exists
     */
    private static String parseParagraph(String content, String para) {
        String result = null;
        String a[] = content.split("(?m)^" + para + "\\s*");
        if (a.length > 1) {
            a = a[1].split("(?m)^\n");
            result = a[0];
            result = result.trim();
        }
        return result;
    }

    /**
     * {@link data.dao.FetchWiki#parseParagraph Parses} meaning paragraph removing empty items from list.
     */
    private static String parseMeaning(String content) {
        String result = parseParagraph(content, "Значение");
        if (result != null)
            result = result.replaceAll("(?m)^\\s*\\d{1,2}\\.\\s*$", "").trim();
        return result;
    }
    /**
     * {@link data.dao.FetchWiki#parseParagraph Parses} etymology paragraph avoiding unfinished paragraph
     * which is detected by "??"
     */
    private static String parseEtymology(String content) {
        String result = parseParagraph(content, "Этимология");
        if (result != null && result.indexOf("??") > 0)
            result = null;
        return result;
    }

    /**
     * extracts word by syllables.<br>
     * Considered that target line is between "Морфологические и синтаксические свойства" and "Значение" lines
     * as well as between empty lines and syllables word is similar with required word.
     * @return word by syllables or null if doesn't exists
     */
    private static String parseSyllables(String content, String word) {
        String result = null, soil, candidate;
        Scanner scanner;
        Collator collator = Collator.getInstance(Locale.forLanguageTag("Cyrl"));
        collator.setStrength(Collator.PRIMARY);

        String a[] = content.split("(?m)^Морфологические и синтаксические свойства\\s*");
        if (a.length > 1) {
            a = a[1].split("(?m)^Значение\\s*");
            if (a.length > 1) {
                soil = a[0];
                scanner = new Scanner(soil);
                while (scanner.hasNextLine()) {
                    candidate = scanner.nextLine();
                    if (collator.compare(candidate.replaceAll("·|-", "").trim(), word) == 0) {
                        result = candidate;
                        break;
                    }
                }
            }
        }

        if (result != null)
            result = result.trim();
        return result;
    }
}
