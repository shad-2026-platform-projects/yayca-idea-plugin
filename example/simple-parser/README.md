# Визуализация flow графов из a.yaml

## Назначение
Флоу в a.yaml - это граф. Его описание может быть довольно большим, и понять все взаимосвязи кубиков, глядя только в текст бывает сложно.
Эта утилита парсит файл a.yaml, и генерирует текстовое описание графа в формате [Graphviz](https://graphviz.org/)
С помощью graphviz далее можно получить визуальное изображение графа во многих форматах (.svg, .png, etc)

## Применение
Базовая команда:

```
$ ./flow_graph path/a.yaml '<flow name inside a.yaml>' > flow.dot

```

Пример.

Получить .svg файл для графа релиза бекенда одной строкой:
```
$ ./flow_graph.py ../../../a.yaml 'games-backends-prod-flow' | dot -Tsvg >graph.svg
```

Примечание.

Для более красивой картинки можно включить команду `sfdp`:
```
$ ./flow_graph.py ../../../a.yaml 'games-backends-prod-flow' | sfdp | dot -Tsvg >graph.svg
```

