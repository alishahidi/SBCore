package com.alishahidi.sbcore.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseDto {
    Long id;
    Date createdAt;
    Date updatedAt;
    Date deletedAt;
}
