import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class KnowledgeCheckerLauncher {

    private KnowledgeCheckerLauncher() {
    }

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            context.getBean(KnowledgeCheckerApplication.class).run();
        }
    }
}
