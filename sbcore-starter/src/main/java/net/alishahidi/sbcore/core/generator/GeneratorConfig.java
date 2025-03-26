package net.alishahidi.sbcore.core.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GeneratorConfig {
    private final String xmlFolderPath;
}