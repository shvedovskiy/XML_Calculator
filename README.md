#XML_Calculator

Команда для запуска программы:
```bash
    java -jar "XML_Calculator-1.0.jar" <input>.xml
```
где `<input>.xml` -- файл с исходным выражением.

Для работы программы необходимо наличие рядом с `.jar` файлом директории `lib/` (библиотека JDOM).
Файл XML-схемы для валидации (`Calculator.xsd`) должен находиться в той же директории, что и `.jar` файл.

Файл с результатом создается в той же директории, что и `.jar` файл под именем `Result.xml`.