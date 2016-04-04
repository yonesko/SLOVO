package data.dao;

import data.model.FreqEntity;
import data.model.WordInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by gleb on 31.03.16.
 */
public class FetchWiki {
    public static void main(String[] args) throws Exception {
        String content, word;
        FreqEntity freqEntity;
        PrintWriter writer;

//        for (int i = 0; i < 3; i++) {
//            writer = new PrintWriter("page" + i + ".txt");
//            freqEntity = FreqCSVReader.getRandom("s");
//            word = freqEntity.getWord();
//            System.out.println(freqEntity);
//
//            content = getContent(word);
//            if (content.length() > 0) {
//                writer.print(content);
//                writer.flush();
//                writer.close();
//            }
//            else i--;
//        }
        content = getContent("вертеп");
        System.out.println(parseMeaning(content));
        System.out.println(parseEtymology(content));
    }

    public static WordInfo findWord(String word) {
        String content = null;
        WordInfo result = null;
        try {
            content = getContent(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content != null)
            result = new WordInfo(word, parseMeaning(content), parseEtymology(content));
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

        String s = null;
        while ((s = stdInput.readLine()) != null)
            sbPage.append(s).append('\n');

        while ((s = stdError.readLine()) != null)
            System.err.println(s);

        //get content between two regexps
        String page = sbPage.toString();
        String a[] = page.split("(?m)^Русский");
        if (a.length > 1)
            result = a[1].split("(?m)^Источник")[0];
        result = result.replaceAll("\\[править\\]", "");
        result = result.replaceAll("◆\\s+Не\\s+указан\\s+пример\\s+употребления\\s+\\(см\\.\\s+рекомендации\\)\\.", "");
        return result;
    }

    private static String parseMeaning(String content) {
        String result = null;
        String a[] = content.split("(?m)^Значение\\s*");
        if (a.length > 1)
            a = a[1].split("(?m)^\n");
        result = a[0];
        return result;
    }
    private static String parseEtymology(String content) {
        String result = null;
        String a[] = content.split("(?m)^Этимология\\s*");
        if (a.length > 1)
            a = a[1].split("(?m)^\n");
        result = a[0];
        if (result != null && result.indexOf("??") > 0)
            result = null;
        return result;
    }
    private static String parseMorpho(String content) {
        String result = "";
        String a[] = content.split("(?m)^Морфологические и синтаксические свойства");
        if (a.length > 1)
            a = a[1].split("(?m)^\n");
        if (a.length > 1)
            result = a[1];
        return result;
    }
}
