package finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PhoneFinder extends Finder<ArrayList<String>> {
    final private static String PHONE_NUMBER_PATTERN = "\\b(\\d{3}-\\d{3}-\\d{4})|(\\(\\d{3}\\) \\d{3}-\\d{4})\\b";
    private ArrayList<String> matches;

    public PhoneFinder(File source) {
        super(source);
        this.matches = new ArrayList<>();
    }

    public PhoneFinder(String source) {
        super(source);
    }

    @Override
    public ArrayList<String> getMatches() {
        return this.matches;
    }

    @Override
    public PhoneFinder find() {
        ArrayList<String> list = new ArrayList<>();
        try (FileReader reader = new FileReader(this.getSource());
             BufferedReader buffReader = new BufferedReader(reader)) {
            String str;
            Pattern regEx = Pattern.compile(PHONE_NUMBER_PATTERN);
            while ((str = buffReader.readLine()) != null) {
                Matcher matcher = regEx.matcher(str);
                while (matcher.find()) {
                    list.add(matcher.group());
                }
            }
        } catch (IOException e) {
            System.err.println("Some problem with source file. Check it, please!");
        }
        this.matches = list;
        return this;
    }

    @Override
    public String toString() {
        return String.join("\n", this.matches);
    }
}
