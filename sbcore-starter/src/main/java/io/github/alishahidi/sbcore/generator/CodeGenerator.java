package io.github.alishahidi.sbcore.generator;

import io.github.alishahidi.sbcore.generator.model.EntityModel;
import io.github.alishahidi.sbcore.generator.model.FieldModel;
import io.github.alishahidi.sbcore.generator.model.RelationModel;
import io.github.alishahidi.sbcore.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);
    private static final String TEMPLATE_DIR = "/generators";
    private static final String SRC_MAIN_JAVA = "src/main/java/";

    Configuration freemarkerConfig;
    String basePackage;
    Validator validator;
    List<EntityModel> entities;
    GeneratorConfig config;

    public CodeGenerator(GeneratorConfig config) throws IOException {
        this.config = config;
        this.freemarkerConfig = createFreemarkerConfig();
        this.basePackage = BasePackage.childBasePackage();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.entities = loadEntities(config.getXmlFolderPath());
    }

    private Configuration createFreemarkerConfig() throws IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_33);
        config.setClassForTemplateLoading(this.getClass(), TEMPLATE_DIR);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return config;
    }

    private List<EntityModel> loadEntities(String xmlFolderPath) throws IOException {
        try {
            List<EntityModel> loadedEntities = XmlParser.readEntitiesFromXml(xmlFolderPath);
            validateEntities(loadedEntities);
            return loadedEntities;
        } catch (Exception e) {
            logger.error("Failed to load entities from XML", e);
            throw new IOException("Entity loading failed", e);
        }
    }

    private void validateEntities(List<EntityModel> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            throw new IllegalArgumentException("No entities found for generation");
        }

        entities.forEach(this::validateEntity);
    }

    private void validateEntity(EntityModel entity) {
        Set<ConstraintViolation<EntityModel>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid entity configuration: " + errorMsg);
        }
    }

    public static void run(GeneratorConfig config) throws CodeGenerationException {
        try {
            CodeGenerator generator = new CodeGenerator(config);
            generator.generateAll();
            logger.info("Code generation completed successfully 234234234234234234234324");
        } catch (Exception e) {
            logger.error("Code generation failed", e);
            throw new CodeGenerationException("Code generation failed", e);
        }
    }

    public void generateAll() throws IOException, TemplateException {
        for (EntityModel entity : entities) {
            logger.info("Generating entity {}", entity.getName());
            if (entity.getGenerate()) {
                generateEntityCode(entity);
            }
        }
    }

    private void generateEntityCode(EntityModel entity) throws IOException, TemplateException {
        Map<String, Object> data = prepareTemplateData(entity);

        generateMainCode(entity, data);
    }

    private Map<String, Object> prepareTemplateData(EntityModel entity) {
        Map<String, Object> data = new HashMap<>();
        data.put("basePackage", basePackage);
        data.put("tableName", StringUtils.toSnakeCase(entity.getName()));
        data.put("entityName", entity.getName());
        data.put("fields", entity.getFields());
        data.put("relationships", entity.getRelations());
        data.put("searchableFields", getSearchableFields(entity));
        return data;
    }

    private List<String> getSearchableFields(EntityModel entity) {
        List<String> searchableFields = new ArrayList<>();

        // Add entity fields
        entity.getFields().stream()
                .filter(FieldModel::isSearchable)
                .map(FieldModel::getName)
                .forEach(searchableFields::add);

        // Add relationship fields
        entity.getRelations().stream()
                .filter(RelationModel::isSearchable)
                .forEach(relation -> processSearchableRelation(relation, searchableFields));

        return searchableFields;
    }

    private void processSearchableRelation(RelationModel relation, List<String> searchableFields) {
        String relatedEntityName = relation.getRelatedEntityName();
        String relationshipName = relation.getRelationshipName();

        entities.stream()
                .filter(e -> e.getName().equals(relatedEntityName))
                .findFirst()
                .ifPresentOrElse(
                        relatedEntity -> relatedEntity.getFields().stream()
                                .filter(FieldModel::isSearchable)
                                .map(FieldModel::getName)
                                .map(field -> relationshipName + "." + field)
                                .forEach(searchableFields::add),
                        () -> {
                            throw new IllegalArgumentException("Cannot find related entity '" + relatedEntityName + "'");
                        }
                );
    }

    private void generateMainCode(EntityModel entity, Map<String, Object> data) throws IOException, TemplateException {
        // Generate entity
        generateFile("EntityTemplate.ftl", entity.getName(), "Entity.java", data, null);

        generateFile("ControllerTemplate.ftl", entity.getName(), "Controller.java", data, null);

        // Generate service
        generateFile("ServiceTemplate.ftl", entity.getName(), "Service.java", data, null);

        // Generate repository
        generateFile("RepositoryTemplate.ftl", entity.getName(), "Repository.java", data, null);

        // Generate DTOs
        generateDtos(entity, data);

        // Generate mapper
        generateFile("MapperTemplate.ftl", entity.getName(), "Mapper.java", data, null);

        // Generate enums if needed
        generateEnums(entity, data);
    }

    private void generateDtos(EntityModel entity, Map<String, Object> data) throws IOException, TemplateException {
        String[] dtoTypes = {"Load", "Update", "Create"};
        for (String type : dtoTypes) {
            data.put("dtoType", type);
            generateFile("DtoTemplate.ftl", entity.getName(), type + "Dto.java", data, "dto");
        }
    }

    private void generateEnums(EntityModel entity, Map<String, Object> data) throws IOException, TemplateException {
        if (!entity.hasFields()) return;

        for (FieldModel field : entity.getFields()) {
            if (!CollectionUtils.isEmpty(field.getEnums())) {
                data.put("enumName", field.getName());
                data.put("enums", field.getEnums());
                generateFile("EnumTemplate.ftl", entity.getName(),
                        StringUtils.toUpperCamelCase(field.getName()) + "Enum.java", data, "constant");
            }
        }
    }

    private void generateFile(String templateName, String entityName, String suffixName,
                              Map<String, Object> data, String subPackage) throws IOException, TemplateException {
        String outputPath = buildOutputPath(entityName, suffixName, subPackage);
        processTemplate(templateName, data, outputPath);
    }

    private String buildOutputPath(String entityName, String suffixName, String subPackage) {
        String packageDir = basePackage.replace(".", "/");
        String safeEntityName = entityName.toLowerCase();

        Path path = Paths.get(SRC_MAIN_JAVA, packageDir, safeEntityName);

        if (subPackage != null) {
            path = path.resolve(subPackage);
        }

        return path.resolve(entityName + suffixName).toString();
    }

    private void processTemplate(String templateName, Map<String, Object> data, String outputPath)
            throws IOException, TemplateException {
        ensureDirectoryExists(outputPath);

        try (FileWriter writer = new FileWriter(outputPath)) {
            Template template = freemarkerConfig.getTemplate(templateName);
            template.process(data, writer);
            logger.debug("Generated file: {}", outputPath);
        }
    }

    private void ensureDirectoryExists(String filePath) throws IOException {
        Path path = Paths.get(filePath).getParent();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}