package net.alishahidi.sbcore.helloworld;

import net.alishahidi.sbcore.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "hello_worlds"
)
public class HelloWorldEntity extends BaseEntity {
    String name;
    String message;

}
