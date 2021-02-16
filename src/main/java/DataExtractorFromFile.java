import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class DataExtractorFromFile {
    //pattern for search all words in texts
    final private static String PHONE_NUMBER_PATTERN =  "\\b" +
                                                        "(\\d{3}-\\d{3}-\\d{4})" +
                                                        "|" +
                                                        "(\\(\\d{3}\\) \\d{3}-\\d{4})" +
                                                        "\\b";

    //pattern for search all words in texts
    final private static String USER_DATA_PATTERN = "\\b" +
                                                    "([A-ZА-Яa-zа-я]+)" + "\\b\\s+" + "(\\d+)" +
                                                    "\\b";

    //pattern for search all words in texts
    final private static String ALL_WORDS_PATTERN = "\\b([A-ZА-Яa-zа-я]+)\\b";


    final File source;                              //source file of the input data

    //constructor defines source file from File object
    public DataExtractorFromFile(File source) {
        if(source.exists()) {
            this.source = source;
        } else {
            System.err.println("Wrong source-file path for \"" + source.getPath() + "\"! Check your input, please");
            this.source = null;
        }
    }

    //constructor defines source file from file path (String object)
    public DataExtractorFromFile(String source) {
        if(isFileExists(source)) {
            this.source = new File(source);
        } else {
            System.err.println("Wrong source-file path for \"" + source + "\"! Check your input, please");
            this.source = null;
        }
    }

    //check is file exists. If not, create it
    private boolean isFileExists(String path){
        return Files.exists(Path.of(path));
    }

    //return source file like File object
    public File getSource(){
        return this.source;
    }

    //return canonical path of the source file like String object
    public String getSourcePath(){
        String path = null;
        try {
            path = this.source.getCanonicalPath();
        } catch (IOException e) {
            System.err.println("Source canonical path extraction error. Check initialization of your" +
                    "dataExtractorFromFile object");
        }
        return path;
    }

    //get phone number from file source
    public List<String> phoneNumbersToList(){
        if( this.source==null ) return null;
        List<String> list = new ArrayList<>();
        String str;
        //regEx for phone numbers
        String regEx = PHONE_NUMBER_PATTERN;
        try( FileReader reader = new FileReader(this.source);
             BufferedReader buffReader = new BufferedReader(reader); ) {
                 while ((str = buffReader.readLine()) != null) {
                     //get ArrayList with find matches groups
                     for(String[] el: findMatches(regEx, str, false)) {
                             list.add(el[0]);
                     }
                 }
        } catch (IOException e){
            System.err.println("Some problem with source file. Check initialization of your" +
                    "dataExtractorFromFile object");
        }
        return list;
    }


    //get users data from file source
    public List<User> usersDataToList(){
        if( this.source==null ) return null;
        List<User> list=new ArrayList<>();
        String str;
        //regEx for users data:-----name---------------------------age-------
        String regEx = USER_DATA_PATTERN;
        try( FileReader reader = new FileReader(this.source);
             BufferedReader buffReader = new BufferedReader(reader); ) {
            while ((str = buffReader.readLine()) != null) {
                //get ArrayList with find matches groups
                for(String[] user: findMatches(regEx, str, false)) {
                    String name = user[1];
                    int age = Integer.valueOf(user[2]);
                    list.add(new User(name, age));
                }
            }
        } catch (IOException e){
            System.err.println("Some problem with source file. Check initialization of your" +
                    "dataExtractorFromFile object");
        }
        return list;
    }

    //get Object-list to JSON-file (user.json)
    public <E> String toJSON(E object){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    //write users data from source file to JSON in destination file path
    public String userDataToJSON(){
        return toJSON( usersDataToList() );
    }

    //write users data from source file to JSON in destination file path
    public String userDataToJSON(String destination){
        //get JSON-string
        String json = toJSON( usersDataToList() );
        //write it to destination file path
        writeToFile(destination, json);
        return json;
    }

    public String userDataToJSON(File destination){
        //get JSON-string
        String json = toJSON( usersDataToList() );
        //write it to destination file path
        writeToFile(destination, json);
        return json;
    }

    //calculate all word (not spaces and punctuation signs) matches quantity existing in this.source file
    //return unsorted LinkedHashMap<word[String], wordQuantity[Integer]>,
    //where word is all matches that was find in the line
    public Map<String, Integer> wordQuantity(){
        return wordQuantity(ALL_WORDS_PATTERN);
    }

    //calculate word or pattern matches quantity existing in this.source file
    //return unsorted LinkedHashMap<word[String], wordQuantity[Integer]>,
    //where word is all matches that was find in the line
    public Map<String, Integer> wordQuantity(String pattern){
        return wordQuantity(pattern, this.source);
    }

    //calculate word or pattern matches quantity existing in String line at path
    //return unsorted LinkedHashMap<word[String], wordQuantity[Integer]>,
    //where word is all matches that was find in the line
    private Map<String, Integer> wordQuantity(String pattern, File path){
        Map<String, Integer> wordMap = new LinkedHashMap<>();
        try( FileReader reader = new FileReader(path);
             BufferedReader buffReader = new BufferedReader(reader); ){
            String line = "";
            String addLine = buffReader.readLine();
            while (addLine != null){
                line += (addLine + " ");
                addLine = buffReader.readLine();
            }
            //provide line to lower case
            line = line.toLowerCase();
            //getting HashSet of unique matches in the line
            Collection<String[]> wordSet = findMatches(pattern, line, true);
            //counting quantity of every particular word, that matched and was return in wordSet
            String word;

            for (String[] words: wordSet) {
                word = words[0];
                Pattern regEx = Pattern.compile("\\b"+word+"\\b");
                Matcher matcher = regEx.matcher(line);
                Integer i = 0;
                while ( matcher.find() ) {
                    ++i;
                }
                //and put new entry to HashMap
                wordMap.put(word, i);
            }
        }catch (Exception e){
            System.err.println("Some problem with your pattern. Check it.\n" + e.getMessage());
        }
        return wordMap;
    }

    //The same that method wordQuantity() - calculate all word (not spaces and punctuation signs) matches quantity
    // existing in this.source file
    //return direct sorted LinkedHashMap<word[String], wordQuantity[Integer]> by quantity ASCENDING,
    //where word is all matches that was find in the line
    private Map<String, Integer> wordQuantitySorted(){
       return wordQuantitySorted(ALL_WORDS_PATTERN);
    }

    //The same that method wordQuantity(String pattern) - calculate word or pattern matches quantity existing in this.source
    //return direct sorted LinkedHashMap<word[String], wordQuantity[Integer]> by quantity ASCENDING,
    //where word is all matches that was find in the line
    private Map<String, Integer> wordQuantitySorted(String pattern){
        return wordQuantitySorted(pattern, this.source);
    }

    //The same that mathod wordQuantity(String pattern, String path) - calculate word or pattern matches quantity existing in String line at path
    //return direct sorted LinkedHashMap<word[String], wordQuantity[Integer]> by quantity ASCENDING,
    //where word is all matches that was find in the line
    private Map<String, Integer> wordQuantitySorted(String pattern, File path){
        Map<String, Integer> wordMap = wordQuantity(pattern, path);
        return wordMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2)->k1, LinkedHashMap::new));
    }

    //The same that mathod wordQuantity() - calculate all word (not spaces and punctuation signs) matches quantity
    // existing in this.source
    //return reverse sorted LinkedHashMap<word[String], wordQuantity[Integer]> by quantity DESCENDING,
    //where word is all matches that was find in the line
    public Map<String, Integer> wordQuantitySortedReverse(){
        return wordQuantitySortedReverse(ALL_WORDS_PATTERN);
    }

    //The same that mathod wordQuantity() - calculate word or pattern matches quantity existing in this.source
    //return reverse sorted LinkedHashMap<word[String], wordQuantity[Integer]> by quantity DESCENDING,
    //where word is all matches that was find in the line
    public Map<String, Integer> wordQuantitySortedReverse(String pattern){
        return wordQuantitySortedReverse(pattern, this.source);
    }

    //The same that mathod wordQuantity() - calculate word or pattern matches quantity existing in String line at path
    //return reverse sorted LinkedHashMap<word[String], wordQuantity[Integer]> by quantity DESCENDING,
    //where word is all matches that was find in the line
    private Map<String, Integer> wordQuantitySortedReverse(String pattern, File path){
        Map<String, Integer> wordMap = wordQuantity(pattern, path);
        Comparator <Integer> comparator = (o1, o2) -> o2.compareTo(o1);
        return wordMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2)->k1, LinkedHashMap::new));
    }

    //formated output to console (new element at new row)
    //If it is not listed object or primitive type - outputs by standart metod toString()
    public void toConsole(Collection collection){
        for (Object el: collection) {
            System.out.println(el);
        }
    }

    public void toConsole(Map map){
        Iterator <Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> node = iterator.next();
            System.out.printf("%-20s %d\n", node.getKey(),node.getValue());
        }
    }

    public <T> void toConsole(T[] array){
        String str = "";
        for (T element : array) {
            str += (element + "\n");
        }
        if (str.length()>0) {
            str = "\n" + str;
        }
        System.out.println("[" + str + "]");
    }

    public <T> void toConsole(T object){
        System.out.println(object);
    }

    //create dirs and file for output
    private <E> boolean writeToFile(String destination, E output) {
        File file = new File(destination);
        return writeToFile(file,output);
    }

    //create dirs and file for output
    private <E> boolean writeToFile(File destination, E output) {
        if (!destination.exists()){
            try {
                destination.getParentFile().mkdirs();
                destination.createNewFile();
            } catch (IOException e) {
                System.err.println("Wrong destination file path! Check it, please.");
                return false;
            }
        }
        try (FileWriter writer = new FileWriter(destination);
             BufferedWriter buffWriter = new BufferedWriter(writer)){
            buffWriter.write(output.toString());
        } catch (IOException e) {
            System.err.println("Wrong destination file path! Check it, please.");
            return false;
        }
        return true;
    }

    //return collection of arrays for regEx matches in string line
    //returnUnique = true; - return HashSet
    //returnUnique = false; - return ArrayList
    private  Collection<String[]> findMatches(String pattern, String line, boolean returnUnique){
        Collection <String[]> collection=null;
        String str;
        if(returnUnique) {
            collection = new HashSet<>();
        } else {
            collection = new ArrayList<>();
        }
        Pattern regEx = Pattern.compile(pattern);
        Matcher matcher = regEx.matcher(line);
        while(matcher.find()) {
            int length = matcher.groupCount()+1;
            String[] matchGroups = new String[length];
            for (int i = 0; i < length; i++) {
                matchGroups[i] = matcher.group(i);
            }
            collection.add(matchGroups);
        }
        return collection;
    }

    //user's data class
    public class User{
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "{\'" + name + "\', " + age + "}";
        }
    }
}
