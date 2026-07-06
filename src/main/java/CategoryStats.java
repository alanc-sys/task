public class CategoryStats {

    private int asked;
    private int correct;

    public void record(boolean answerCorrect) {
        asked++;
        if (answerCorrect) {
            correct++;
        }
    }

    public int getAsked() {
        return asked;
    }

    public int getCorrect() {
        return correct;
    }

    public int getPercentage() {
        if (asked == 0) {
            return 0;
        }
        return (int) Math.round((double) correct * 100.0 / asked);
    }
}
