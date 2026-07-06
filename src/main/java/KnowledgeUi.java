import java.util.List;
import java.util.Map;

public interface KnowledgeUi {

    void showWelcome(List<String> categories, int totalQuestions);

    String readAnswer(KnowledgeElement knowledgeElement);

    void showFeedback(boolean correct, KnowledgeElement knowledgeElement);

    void showPerformance(Map<String, CategoryStats> performanceByCategory);
}
