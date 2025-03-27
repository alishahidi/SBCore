package io.github.alishahidi.sbcore.generator.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.github.alishahidi.sbcore.generator.BasePackage;
import io.github.alishahidi.sbcore.util.StringUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelationModel {
    @JacksonXmlProperty(localName = "relationshipType")
    private RelationType type;

    @JacksonXmlProperty(localName = "relatedEntityName")
    private String relatedEntityName;

    private String relatedEntityPackage;

    @JacksonXmlProperty(localName = "mappedBy")
    private String mappedBy;

    @JacksonXmlProperty(localName = "foreignKey")
    private String foreignKey;

    @JacksonXmlProperty(localName = "required")
    private boolean required;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "document")
    private boolean document = false;

    @JacksonXmlProperty(localName = "searchable")
    @Builder.Default
    private boolean searchable = false;


    public String getRelationshipName() {
        return (name != null && !name.isEmpty()) ? name : StringUtils.toLowerCamelCase(relatedEntityName);
    }


    public static String generatePackageName(String name) {
        if(name == null){
            return null;
        }
        return BasePackage.childBasePackage() + "." + name.toLowerCase();
    }

    public boolean hasMappedBy() {
        return mappedBy != null && !mappedBy.isEmpty();
    }
}
