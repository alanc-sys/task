import java.util.List;

public class FixedStrategy implements QuestionSelectionStrategy {

    private final int questionIndex;

    public FixedStrategy(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    @Override
    public KnowledgeElement select(List<KnowledgeElement> knowledgeBank) {
        if (knowledgeBank == null || knowledgeBank.isEmpty()) {
            return null;
        }
        return knowledgeBank.get(Math.floorMod(questionIndex, knowledgeBank.size()));
    }
}
