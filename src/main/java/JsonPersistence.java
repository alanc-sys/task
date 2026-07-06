import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonPersistence {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final Path dataFile;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public JsonPersistence(String dataFile, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this(Path.of(dataFile), objectMapper, modelMapper);
    }

    public JsonPersistence(Path dataFile, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.dataFile = Objects.requireNonNull(dataFile, "dataFile");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper").copy().enable(SerializationFeature.INDENT_OUTPUT);
        this.modelMapper = Objects.requireNonNull(modelMapper, "modelMapper");
    }

    public List<KnowledgeElement> load() {
        return load(dataFile);
    }

    public List<KnowledgeElement> load(Path sourceFile) {
        try {
            if (sourceFile != null && Files.exists(sourceFile)) {
                return readFromPath(sourceFile);
            }
            InputStream fallbackStream = getClass().getClassLoader().getResourceAsStream("data/questions.json");
            if (fallbackStream != null) {
                return readFromStream(fallbackStream);
            }
            return new ArrayList<>();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load knowledge bank", exception);
        }
    }

    public void save(List<KnowledgeElement> knowledgeElements) {
        save(dataFile, knowledgeElements);
    }

    public void save(Path targetFile, List<KnowledgeElement> knowledgeElements) {
        try {
            if (targetFile.getParent() != null) {
                Files.createDirectories(targetFile.getParent());
            }
            List<KnowledgeElementDto> dtos = new ArrayList<>();
            if (knowledgeElements != null) {
                for (KnowledgeElement knowledgeElement : knowledgeElements) {
                    dtos.add(toDto(knowledgeElement));
                }
            }
            objectMapper.writeValue(targetFile.toFile(), dtos);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save knowledge bank", exception);
        }
    }

    private List<KnowledgeElement> readFromPath(Path sourceFile) throws IOException {
        return toModels(objectMapper.readValue(sourceFile.toFile(), new TypeReference<List<KnowledgeElementDto>>() { }));
    }

    private List<KnowledgeElement> readFromStream(InputStream inputStream) throws IOException {
        try (InputStream stream = inputStream) {
            return toModels(objectMapper.readValue(stream, new TypeReference<List<KnowledgeElementDto>>() { }));
        }
    }

    private List<KnowledgeElement> toModels(List<KnowledgeElementDto> dtos) {
        List<KnowledgeElement> models = new ArrayList<>();
        if (dtos == null) {
            return models;
        }
        for (KnowledgeElementDto dto : dtos) {
            KnowledgeElement model = modelMapper.map(dto, KnowledgeElement.class);
            if (dto.getLastAsked() != null && !dto.getLastAsked().isBlank()) {
                model.setLastAsked(LocalDateTime.parse(dto.getLastAsked(), FORMATTER));
            }
            model.setHistory(dto.getHistory());
            models.add(model);
        }
        return models;
    }

    private KnowledgeElementDto toDto(KnowledgeElement model) {
        KnowledgeElementDto dto = modelMapper.map(model, KnowledgeElementDto.class);
        if (model.getLastAsked() != null) {
            dto.setLastAsked(model.getLastAsked().format(FORMATTER));
        }
        dto.setHistory(model.getHistory());
        return dto;
    }
}
