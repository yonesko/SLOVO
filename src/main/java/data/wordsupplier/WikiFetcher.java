package data.wordsupplier;

import data.model.WikiWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by gleb on 16.06.16.
 */
class WikiFetcher {
    public static void main(String[] args) {
        WikiFetcher wikiFetcher = new WikiFetcher();
        System.out.println(wikiFetcher.findWord("подбивать").toPublish());
    }
    /**
     * Makes http query to the wiktionary and parse page.<br>
     * If word exists at wiktionary and at least one parse is successful
     * return word; else null.
     *
     * @param word to find at wiktionary
     * @return word or null if fail or word is null or empty
     */

    public WikiWord findWord(String word) {
        if (word == null || word.isEmpty())
            return null;
        word = word.toLowerCase();
        String content = null;
        WikiWord result = null;
        content = getContent(word);
        if (content != null)
            result = new WikiWord(word,
                    parseMeaning(content),
                    parseEtymology(content),
                    parseSyllables(content, word),
                    parseSynonyms(content));
        return result;
    }

    /**
     * @return formatted page or null if fail
     */
    private String getContent(String word) {
        StringBuilder sbPage = new StringBuilder();
        String result = null;
        String cmds[] = {
                "bash",
                "-c",
                "wget \"https://ru.wiktionary.org/w/index.php?title="+word+"&printable=yes\" -O - 2>/dev/null | w3m -dump -no-graph -T text/html"
        };

        Runtime rt = Runtime.getRuntime();

        try {

        Process proc = rt.exec(cmds);
        proc.waitFor();

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        String s;
        while ((s = stdInput.readLine()) != null)
            sbPage.append(s).append('\n');

        while ((s = stdError.readLine()) != null)
            System.err.println(s);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        //get content between two regexps
        String page = sbPage.toString();
        if (page.length() != 0) {
            String a[] = page.split("(?m)^Русский");
            if (a.length > 1) {
                result = a[1].split("(?m)^Источник")[0];
                result = result.replaceAll("\\[править\\]", "");
                //prevent VK markup expanding
                result = result.replaceAll("\\*", "* ");
                result = result.replaceAll("◆\\s*Не\\s+указан\\s+пример\\s+употребления\\s+\\(см\\s*\\.\\s+рекомендации\\s*\\)\\s*\\.", "");
                result = result.replaceAll("\\s*\\(цитата\\s+из\\s+Национального\\s+корпуса\\s+русского\\s+языка,\\s+см\\.\\s+Список\\s+литературы\\)", "");
                result = result.replaceAll("\\s*\\(цитата\\s+из\\s+Викитеки\\)", "");
            }
        }
        return result;
    }

    /**
     * extracts meaning paragraph of the page.<br>
     * Considered that paragraph is text surrounded with empty lines followed by line starting with para.<br>
     * Para is the only word at this line.
     *
     * @return meaning or null if doesn't exists
     */
    private String parseParagraph(String content, String para) {
        String result = null;
        String a[] = content.split("(?m)^" + para + "\\s*");
        if (a.length > 1) {
            a = a[1].split("(?m)^\n");
            result = a[0];
            result = result.trim();
        }
        return result;
    }

    private String parseSynonyms(String content) {
        String result = parseParagraph(content, "Синонимы");
        if (result != null)
            result = removeEmptyListItems(result).trim();
        return result;
    }

    /**
     * {@link WikiFetcher#parseParagraph Parses} meaning paragraph removing empty items from list.
     */
    private String parseMeaning(String content) {
        String result = parseParagraph(content, "Значение");
        if (result != null)
            result = removeEmptyListItems(result).trim();
        return result;
    }
/*
1. -
 2. -
 */
    private String removeEmptyListItems(String text) {
        return text.replaceAll("(?m)^\\s*\\d{1,2}\\.( \\W)?\\s*$", "");
    }

    /**
     * {@link WikiFetcher#parseParagraph Parses} etymology paragraph avoiding unfinished paragraph
     * which is detected by "??"
     */
    private String parseEtymology(String content) {
        String result = parseParagraph(content, "Этимология");
        if (result != null && result.indexOf("??") > 0)
            result = null;
        return result;
    }

    /**
     * extracts word by syllables.<br>
     * Considered that target line is between "Морфологические и синтаксические свойства" and "Значение" lines
     * as well as between empty lines and syllables word is similar with required word.
     *
     * @return word by syllables or null if doesn't exists
     */
    private String parseSyllables(String content, String word) {
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
