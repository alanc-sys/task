import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomStrategy implements QuestionSelectionStrategy {

    private final Random random;

    public RandomStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public KnowledgeElement select(List<KnowledgeElement> knowledgeBank) {
        if (knowledgeBank == null || knowledgeBank.isEmpty()) {
            return null;
        }
        return knowledgeBank.get(random.nextInt(knowledgeBank.size()));
    }
}
