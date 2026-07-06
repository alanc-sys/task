import java.util.List;

public interface QuestionSelectionStrategy {
    KnowledgeElement select(List<KnowledgeElement> knowledgeBank);
}
