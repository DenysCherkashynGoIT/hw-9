import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainTest {
    public static void main(String[] args) {

        DataExtractorFromFile inputTask12 = new DataExtractorFromFile("src\\main\\resources\\file.txt");
        /*--------------------------------------------------------------------------------------------------*/
        //        Задание 1 - Поиск в файле и вывод в консоль валидных номеров формата (xxx) xxx-xxxx или
        //        xxx-xxx-xxxx (х обозначает цифру)
        //
        inputTask12.toConsole(inputTask12.phoneNumbersToList());

        /*--------------------------------------------------------------------------------------------------*/
        //        Задание 2 - Поиск в файле списка объектов User и создние новый файл user.json с выводом в
        //        него в формате JSON найденных объектов.
        File outputFile2 = new File("src\\main\\resources\\user.json");
        inputTask12.userDataToJSON(outputFile2);

        /*--------------------------------------------------------------------------------------------------*/
        //        Задание 3 - Подсчитывание частоты каждого слова в файле words.txt (без учета регистра)
        //        Каждое слово содержит только символы-буквы в нижнем регистре.
        File file3 = new File("src\\main\\resources\\words.txt");
        DataExtractorFromFile inputTask3 = new DataExtractorFromFile(file3);
        inputTask3.toConsole(inputTask3.wordQuantitySortedReverse());

        /*--------------------------------------------------------------------------------------------------*/














    }
}
