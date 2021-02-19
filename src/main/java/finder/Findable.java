package finder;

import java.io.File;

public interface Findable<T> {
    void setSource(File source);           //возвращает файл-источник как объект File
    void setSource(String source);

    File getSource();                      //возвращает файл-источник как объект File

    T getMatches();                        //возвращает найденные значения-совпадений
                                            // после последнего выполнения метода find().

    Findable<T> find();                       //выполняет сооветсвующий текущему классу поиск по файлу.

    void print();                          //выводит форматированный результат последнего поиска в консоль

    File printToFile(File destination);     //записывает форматированный результат последнего поиска в заданный .
    File printToFile(String destination);   //файл по пути destination

}
