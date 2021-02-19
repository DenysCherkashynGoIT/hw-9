package finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordsCounter extends Finder<LinkedHashMap<String, Integer>> {
    final private static String ALL_WORDS_PATTERN = "\\b([A-ZА-Яa-zа-я]+)\\b";
    private LinkedHashMap<String, Integer> matches;

    public WordsCounter(File source) {
        super(source);
        this.matches = new LinkedHashMap<>();
    }

    public WordsCounter(String source) {
        super(source);
    }

    @Override
    public LinkedHashMap<String, Integer> getMatches() {
        return this.matches;
    }

    @Override
    public WordsCounter find() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        try (FileReader reader = new FileReader(this.getSource());
             BufferedReader buffReader = new BufferedReader(reader)) {
            String str, key;
            Integer value;
            Pattern regEx = Pattern.compile(ALL_WORDS_PATTERN);
            while ((str = buffReader.readLine()) != null) {
                Matcher matcher = regEx.matcher(str);
                while (matcher.find()) {
                    key = matcher.group();
                    value = map.get(key);
                    value = (value != null) ? ++value : 1;
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("Some problem with source file. Check it, please!");
        }
        Comparator<Integer> comparator = (v1, v2) -> v2.compareTo(v1);
        this.matches = map.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k1, LinkedHashMap::new));
        return this;
    }

    @Override
    public String toString() {
        Iterator<Map.Entry<String, Integer>> iterator = this.matches.entrySet().iterator();
        StringBuilder str = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            str.append(String.format("%-20s %d\n", entry.getKey(), entry.getValue()));
        }
        return str.toString();
    }
}
