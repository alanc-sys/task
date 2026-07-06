import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeElement {

    private Long id;
    private String category;
    private String question;
    private String answer;
    private LocalDateTime lastAsked;
    private List<Boolean> history = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getLastAsked() {
        return lastAsked;
    }

    public void setLastAsked(LocalDateTime lastAsked) {
        this.lastAsked = lastAsked;
    }

    public List<Boolean> getHistory() {
        if (history == null) {
            history = new ArrayList<>();
        }
        return history;
    }

    public void setHistory(List<Boolean> history) {
        this.history = history == null ? new ArrayList<>() : new ArrayList<>(history);
    }

    public void recordAnswer(boolean correct, LocalDateTime askedAt) {
        getHistory().add(correct);
        setLastAsked(askedAt);
    }
}
