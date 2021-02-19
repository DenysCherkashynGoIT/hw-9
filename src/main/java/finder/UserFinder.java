package finder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFinder extends Finder<ArrayList<User>> {
    final private static String USER_DATA_PATTERN = "\\b([A-ZА-Яa-zа-я]+)\\b\\s+(\\d+)\\b";
    private ArrayList<User> matches;

    public UserFinder(File source) {
        super(source);
        this.matches = new ArrayList<>();
    }

    public UserFinder(String source) {
        super(source);
    }

    @Override
    public ArrayList<User> getMatches() {
        return this.matches;
    }

    @Override
    public UserFinder find() {
        ArrayList<User> list = new ArrayList<>();
        try (FileReader reader = new FileReader(this.getSource());
             BufferedReader buffReader = new BufferedReader(reader)) {
            String str;
            Pattern regEx = Pattern.compile(USER_DATA_PATTERN);
            while ((str = buffReader.readLine()) != null) {
                Matcher matcher = regEx.matcher(str);
                String name;
                Integer age;
                while (matcher.find()) {
                    name = matcher.group(1);
                    age = Integer.valueOf(matcher.group(2));
                    list.add(new User(name, age));
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this.matches);
    }

}
