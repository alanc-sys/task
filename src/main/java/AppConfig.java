import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JsonPersistence jsonPersistence(
            @Value("${data.file}") String dataFile,
            ObjectMapper objectMapper,
            ModelMapper modelMapper) {
        return new JsonPersistence(dataFile, objectMapper, modelMapper);
    }

    @Bean
    public FixedStrategy fixedStrategy(@Value("${fixedstrategy.questionIndex}") int questionIndex) {
        return new FixedStrategy(questionIndex);
    }

    @Bean
    public SequentialStrategy sequentialStrategy() {
        return new SequentialStrategy();
    }

    @Bean
    public RandomStrategy randomStrategy() {
        return new RandomStrategy(new Random());
    }

    @Bean
    public AdaptiveWeightCalculator adaptiveWeightCalculator() {
        return new AdaptiveWeightCalculator();
    }

    @Bean
    public AdaptiveStrategy adaptiveStrategy(AdaptiveWeightCalculator weightCalculator) {
        return new AdaptiveStrategy(weightCalculator, new Random());
    }

    @Bean
    public QuestionSelectionStrategy questionSelectionStrategy(
            @Value("${question.selection.strategy}") String strategy,
            FixedStrategy fixedStrategy,
            SequentialStrategy sequentialStrategy,
            RandomStrategy randomStrategy,
            AdaptiveStrategy adaptiveStrategy) {
        return switch (strategy.trim().toLowerCase()) {
            case "fixed" -> fixedStrategy;
            case "random" -> randomStrategy;
            case "adaptive" -> adaptiveStrategy;
            default -> sequentialStrategy;
        };
    }

    @Bean
    public KnowledgeUi knowledgeUi() {
        return new ConsoleKnowledgeUi(
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
                System.out);
    }

    @Bean
    public KnowledgeCheckerApplication knowledgeCheckerApplication(
            JsonPersistence persistence,
            QuestionSelectionStrategy selectionStrategy,
            KnowledgeUi knowledgeUi) {
        return new KnowledgeCheckerApplication(persistence, selectionStrategy, knowledgeUi);
    }
}
