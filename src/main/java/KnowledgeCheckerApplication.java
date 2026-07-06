import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class KnowledgeCheckerApplication {

    private final JsonPersistence persistence;
    private final QuestionSelectionStrategy selectionStrategy;
    private final KnowledgeUi knowledgeUi;
    private final Clock clock;

    public KnowledgeCheckerApplication(JsonPersistence persistence,
                                       QuestionSelectionStrategy selectionStrategy,
                                       KnowledgeUi knowledgeUi) {
        this(persistence, selectionStrategy, knowledgeUi, Clock.systemDefaultZone());
    }

    public KnowledgeCheckerApplication(JsonPersistence persistence,
                                       QuestionSelectionStrategy selectionStrategy,
                                       KnowledgeUi knowledgeUi,
                                       Clock clock) {
        this.persistence = Objects.requireNonNull(persistence, "persistence");
        this.selectionStrategy = Objects.requireNonNull(selectionStrategy, "selectionStrategy");
        this.knowledgeUi = Objects.requireNonNull(knowledgeUi, "knowledgeUi");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public void run() {
        List<KnowledgeElement> knowledgeBank = new ArrayList<>(persistence.load());
        Set<String> categories = new LinkedHashSet<>();
        for (KnowledgeElement element : knowledgeBank) {
            if (element.getCategory() != null && !element.getCategory().isBlank()) {
                categories.add(element.getCategory());
            }
        }

        knowledgeUi.showWelcome(new ArrayList<>(categories), knowledgeBank.size());

        Map<String, CategoryStats> performanceByCategory = new LinkedHashMap<>();
        while (!knowledgeBank.isEmpty()) {
            KnowledgeElement question = selectionStrategy.select(knowledgeBank);
            if (question == null) {
                break;
            }

            String answer = knowledgeUi.readAnswer(question);
            if (answer == null || "/exit".equalsIgnoreCase(answer.trim())) {
                break;
            }

            boolean correct = isCorrectAnswer(question.getAnswer(), answer);
            question.recordAnswer(correct, LocalDateTime.now(clock).withNano(0));
            performanceByCategory.computeIfAbsent(question.getCategory(), ignored -> new CategoryStats())
                    .record(correct);
            knowledgeUi.showFeedback(correct, question);
        }

        knowledgeUi.showPerformance(performanceByCategory);
        persistence.save(knowledgeBank);
    }

    private boolean isCorrectAnswer(String expected, String actual) {
        if (expected == null) {
            return actual == null || actual.isBlank();
        }
        return expected.trim().equalsIgnoreCase(actual == null ? null : actual.trim());
    }
}
