package io.github.alishahidi.sbcore.generator.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityModel {

    @NotBlank(message = "Entity name is required")
    @JacksonXmlProperty(localName = "name")
    String name;

    @NotNull(message = "Generate flag is required")
    @JacksonXmlProperty(localName = "generate")
    Boolean generate;

    @Valid
    @JacksonXmlElementWrapper(localName = "fields")
    @JacksonXmlProperty(localName = "field")
    @Builder.Default
    List<FieldModel> fields = new ArrayList<>();

    @Valid
    @JacksonXmlElementWrapper(localName = "relationships")
    @JacksonXmlProperty(localName = "relationship")
    @Builder.Default
    List<RelationModel> relations = new ArrayList<>();

    public boolean hasFields() {
        return fields != null && !fields.isEmpty();
    }

    public boolean hasRelations() {
        return relations != null && !relations.isEmpty();
    }
}