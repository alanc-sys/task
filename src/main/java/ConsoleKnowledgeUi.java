import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConsoleKnowledgeUi implements KnowledgeUi {

    private final BufferedReader input;
    private final PrintStream output;

    public ConsoleKnowledgeUi(BufferedReader input, PrintStream output) {
        this.input = Objects.requireNonNull(input, "input");
        this.output = Objects.requireNonNull(output, "output");
    }

    @Override
    public void showWelcome(List<String> categories, int totalQuestions) {
        output.println("Welcome to the Knowledge Bank Application!");
        output.println("This application will test your knowledge on the following topics: " + String.join(", ", categories));
        output.println("Total questions in the knowledge bank: " + totalQuestions);
        output.println("You will be asked questions from various categories.");
        output.println("If you want to exit, type '/exit' as your answer.");
        output.println();
    }

    @Override
    public String readAnswer(KnowledgeElement knowledgeElement) {
        output.println("Question: " + knowledgeElement.getQuestion());
        output.print("Your answer: ");
        output.flush();
        try {
            return input.readLine();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read user answer", exception);
        }
    }

    @Override
    public void showFeedback(boolean correct, KnowledgeElement knowledgeElement) {
        if (correct) {
            output.println("Correct! Well done.");
        } else {
            output.println("Incorrect. The correct answer is: " + knowledgeElement.getAnswer());
        }
        output.println();
    }

    @Override
    public void showPerformance(Map<String, CategoryStats> performanceByCategory) {
        output.println("Your performance:");
        for (Map.Entry<String, CategoryStats> entry : performanceByCategory.entrySet()) {
            CategoryStats stats = entry.getValue();
            if (stats.getAsked() == 0) {
                continue;
            }
            output.println(entry.getKey() + " - Asked: " + stats.getAsked()
                    + ", Correct: " + stats.getCorrect()
                    + ", Percentage: " + stats.getPercentage() + "%");
        }
    }
}
