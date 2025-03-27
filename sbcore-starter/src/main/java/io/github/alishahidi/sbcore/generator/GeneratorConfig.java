package io.github.alishahidi.sbcore.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GeneratorConfig {
    private final String xmlFolderPath;
}