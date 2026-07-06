# Knowledge Checker

Knowledge Checker is a command-line quiz application built with Java 17 and Spring.

## Requirements

- Java 17
- Maven 3.9+

## Run

From the project root:

```bash
mvn exec:java
```

## Tests

```bash
mvn test
```

## Configuration

The main runtime settings are in `src/main/resources/application.properties`:

- `data.file`: JSON file used to load and save the knowledge bank
- `fixedstrategy.questionIndex`: index used by the fixed strategy
- `question.selection.strategy`: `fixed`, `sequential`, `random`, or `adaptive`
