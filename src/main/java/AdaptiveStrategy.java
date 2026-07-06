import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AdaptiveStrategy implements QuestionSelectionStrategy {

    private static final int TOP_CANDIDATES = 5;

    private final AdaptiveWeightCalculator weightCalculator;
    private final Random random;

    public AdaptiveStrategy(Random random) {
        this(new AdaptiveWeightCalculator(), random);
    }

    public AdaptiveStrategy(AdaptiveWeightCalculator weightCalculator, Random random) {
        this.weightCalculator = Objects.requireNonNull(weightCalculator, "weightCalculator");
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public KnowledgeElement select(List<KnowledgeElement> knowledgeBank) {
        if (knowledgeBank == null || knowledgeBank.isEmpty()) {
            return null;
        }

        List<WeightedQuestion> weightedQuestions = new ArrayList<>();
        for (int index = 0; index < knowledgeBank.size(); index++) {
            KnowledgeElement question = knowledgeBank.get(index);
            weightedQuestions.add(new WeightedQuestion(index, question, weightCalculator.calculateWeight(question)));
        }

        weightedQuestions.sort(Comparator
                .comparingInt(WeightedQuestion::weight)
                .reversed()
                .thenComparingInt(WeightedQuestion::index));

        List<WeightedQuestion> candidates = weightedQuestions.subList(0, Math.min(TOP_CANDIDATES, weightedQuestions.size()));
        return candidates.get(random.nextInt(candidates.size())).question();
    }

    private record WeightedQuestion(int index, KnowledgeElement question, int weight) {
    }
}
