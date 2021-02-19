import finder.Finder;
import finder.PhoneFinder;
import finder.UserFinder;
import finder.WordsCounter;

public class Application {
    public static void main(String[] args) {

        //--------------------------------------------------------------------------------------------------
        //        Задание 1 - Поиск в файле и вывод в консоль валидных номеров формата (xxx) xxx-xxxx или
        //        xxx-xxx-xxxx (х обозначает цифру)
        //
        Finder finder = new PhoneFinder("src\\main\\resources\\file.txt");
        finder.find().print();

        //--------------------------------------------------------------------------------------------------
        //        Задание 2 - Поиск в файле списка объектов User и создние новый файл user.json с выводом в
        //        него в формате JSON найденных объектов.
        finder = new UserFinder("src\\main\\resources\\file.txt");
        finder.find().printToFile("src\\main\\resources\\user.json");

        //--------------------------------------------------------------------------------------------------
        //        Задание 3 - Подсчитывание частоты каждого слова в файле words.txt (без учета регистра)
        //        Каждое слово содержит только символы-буквы в нижнем регистре.
        /*File file3 = new File("src\\main\\resources\\words.txt");
        DataExtractorFromFile inputTask3 = new DataExtractorFromFile(file3);
        inputTask3.toConsole(inputTask3.wordQuantitySortedReverse());*/
        finder = new WordsCounter("src\\main\\resources\\words.txt");
        finder.find().print();
        //--------------------------------------------------------------------------------------------------


    }
}
