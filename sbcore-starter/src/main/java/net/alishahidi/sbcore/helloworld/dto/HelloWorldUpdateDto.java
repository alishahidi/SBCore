package net.alishahidi.sbcore.helloworld.dto;

import net.alishahidi.sbcore.core.entity.BaseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HelloWorldUpdateDto extends BaseDto {
    String name;
    String message;
}
