Home task №9

//--------------------------------------------------------------------------------------------------------------------------
Создан нестатический класс DataExtractorFromFile для работы с содержимым файлов (несколько расширен относительно исходного
домашнего задания). При инициализации экземпляра DataExtractorFromFile конструктор принимает обязательный параметр -
ссылку на файл-источник или объект типа File:
DataExtractorFromFile(File source);
DataExtractorFromFile(String source)

Класс содержит единственное приватное поле source, хранящее считываемый файл.
//--------------------------------------------------------------------------------------------------------------------------

Публичные методы класса DataExtractorFromFile:

        File getSource ()
- возвращает файл-источник как объект File

        String getSourcePath ()
- возвращает относительный путь к файлу-источнику в виде строки String

        List<String> phoneNumbersToList ()
- возвращает список (ArrayList) телефонных номеров из файла источника записанных в виде (xxx) xxx-xxxx или xxx-xxx-xxxx
(х обозначает цифру)

        List<User> usersDataToList ()
- возвращает данные о пользователе (name, age) в виде список (ArrayList) объектов класса User

        String toJSON(E object)
- сериализует object в строку JSON и возвращает е₴

        String userDataToJSON()
- сериализует данные о пользователе (name, age) в виде список (ArrayList) объектов класса User (выполняет метод
usersDataToList ())  в строку JSON и возвращает е₴

        String userDataToJSON (String destination)
        String userDataToJSON (File destination)
- сериализует данные о пользователе (name, age) в виде список (ArrayList) объектов класса User (выполняет метод
usersDataToList ())  в строку JSON и возвращает е₴. Возвращаемая строка JSON также записывается в файл по пути
destination. Если файл отсутствует - создает его.

        Map<String, Integer> wordQuantity (String pattern)
        Map<String, Integer> wordQuantitySorted (String pattern)
        Map<String, Integer> wordQuantitySortedReverse (String pattern)
- подсчитывает количество совпадений с регулярным выражением в файле-источнике, определенном при инициализации
экземпляра класса, без учета регистра и возвращает неосртированную LinkedHashMap<word[String], wordQuantity[Integer]>,
где word - все найденные совпадения в тексте файла, а wordQuantity - количество по каждому из таких совпадений.
Методы с префиксами ---Sorted и ---SortedReverse действуют также, только возвращают сортированную по значениям LinkedHashMap
в прямом и обратном порядке, соответственно.

        Map<String, Integer> wordQuantity ()
        Map<String, Integer> wordQuantitySorted ()
        Map<String, Integer> wordQuantitySortedReverse ()
- подсчитывает количество всех слов  в файле-источнике, определенном при инициализации экземпляра класса, без учета
регистра и возвращает неосртированную LinkedHashMap<word[String], wordQuantity[Integer]>, где word - все слова,
найденные в тексте файла, а wordQuantity - количество по каждому из таких совпадений.
В качестве слова принята непрерывноя последовательность символов (букв или цифр) без учета регистра, не содержащия
пробелы и другие знаки пунктуации.
Методы с префиксами ---Sorted и ---SortedReverse действуют также, только возвращают сортированную по значениям LinkedHashMap
в прямом и обратном порядке, соответственно.

        void toConsole(Collection collection)
        void toConsole(Map map)
        void toConsole(T[] array)
        void toConsole(T object)
- форматированный вывод в консоль (новый элемент на новой строке). Если входной параметр - не перечисляемый объект или
примитивный тип, то применяется тсандартный вывод в строку toString();
