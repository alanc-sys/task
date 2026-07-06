import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SequentialStrategy implements QuestionSelectionStrategy {

    private final AtomicInteger cursor = new AtomicInteger();

    @Override
    public KnowledgeElement select(List<KnowledgeElement> knowledgeBank) {
        if (knowledgeBank == null || knowledgeBank.isEmpty()) {
            return null;
        }
        return knowledgeBank.get(Math.floorMod(cursor.getAndIncrement(), knowledgeBank.size()));
    }
}
