import java.util.List;

public class AdaptiveWeightCalculator {

    public int calculateWeight(KnowledgeElement knowledgeElement) {
        return calculateHistoryWeight(knowledgeElement) + calculateTimeWeight(knowledgeElement);
    }

    public int calculateHistoryWeight(KnowledgeElement knowledgeElement) {
        if (knowledgeElement == null) {
            return 0;
        }
        List<Boolean> history = knowledgeElement.getHistory();
        if (history == null || history.isEmpty()) {
            return 0;
        }

        int fromIndex = Math.max(0, history.size() - 2);
        int weight = 0;
        for (Boolean answer : history.subList(fromIndex, history.size())) {
            if (Boolean.TRUE.equals(answer)) {
                weight -= 1;
            } else {
                weight += 2;
            }
        }
        return weight;
    }

    public int calculateTimeWeight(KnowledgeElement knowledgeElement) {
        return knowledgeElement == null || knowledgeElement.getLastAsked() == null ? 1 : 0;
    }
}
