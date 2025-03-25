package com.alishahidi.sbcore.util;

import com.alishahidi.sbcore.entity.BaseEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import jakarta.persistence.criteria.*;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class SpecificationBuilder {

    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            DateTimeFormatter.ISO_DATE,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS X")
    );

    private static final Set<Class<?>> TEMPORAL_TYPES = Set.of(
            Timestamp.class,
            Date.class,
            LocalDateTime.class,
            LocalDate.class,
            Instant.class
    );

    public static <T extends BaseEntity> Specification<T> buildSpecification(
            MultiValueMap<String, String> params,
            List<String> allowedFields) {

        return (root, query, cb) -> {
            Map<String, List<Predicate>> predicateGroups = new HashMap<>();

            // Iterate over the parameters and create predicates
            params.forEach((key, values) -> {
                String[] parts = key.split("_", 2);
                String fieldPath = parts[0];  // This may contain dots for nested properties
                String operation = parts.length > 1 ? parts[1] : "eq";

                // Skip fields not in allowed list
                if (!allowedFields.contains(fieldPath)) return;

                List<Predicate> predicates = values.stream()
                        .filter(value -> value != null && !value.isEmpty())
                        .map(value -> createPredicateForValue(cb, root, fieldPath, operation, value))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!predicates.isEmpty()) {
                    predicateGroups.merge(fieldPath, predicates, (oldList, newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    });
                }
            });

            // Combine predicates with AND between fields and OR within the same field
            return cb.and(
                    predicateGroups.values().stream()
                            .map(predicates -> cb.or(predicates.toArray(new Predicate[0])))
                            .toArray(Predicate[]::new)
            );
        };
    }
    private static Predicate createPredicateForValue(
            CriteriaBuilder cb,
            Root<?> root,
            String fieldPath,
            String operation,
            String value) {

        try {
            Path<?> path = resolvePath(root, fieldPath);
            Object convertedValue = convertToType(path.getJavaType(), value);
            return createPredicate(cb, path, operation, convertedValue);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            // Log and safely handle errors
            System.err.printf("Error creating predicate for field '%s' with value '%s': %s%n",
                    fieldPath, value, e.getMessage());
            return null;
        }
    }

    /**
     * Resolves nested paths (e.g., "province.name") to the actual JPA path
     */
    private static Path<?> resolvePath(Root<?> root, String path) {
        String[] parts = path.split("\\.");
        Path<?> currentPath = root;

        for (String part : parts) {
            currentPath = currentPath.get(part);
        }

        return currentPath;
    }

    private static Predicate createPredicate(
            CriteriaBuilder cb,
            Path<?> path,
            String operation,
            Object value) {

        if (operation.equalsIgnoreCase("eq") && isTemporalType(path.getJavaType())) {
            LocalDate targetDate = convertToLocalDate(value);
            Instant startOfDay = targetDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant endOfDay = targetDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

            Expression<Instant> instantPath = convertPathToInstant(path, path.getJavaType());
            return cb.between(instantPath, startOfDay, endOfDay);
        }

        if (operation.equalsIgnoreCase("le") && isTemporalType(path.getJavaType())) {
            LocalDate targetDate = convertToLocalDate(value);
            Instant endOfDay = targetDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

            Expression<Instant> instantPath = convertPathToInstant(path, path.getJavaType());
            return cb.lessThanOrEqualTo(instantPath, endOfDay);
        }

        switch (operation.toLowerCase()) {
            case "eq":
                return cb.equal(path, value);
            case "ne":
                return cb.notEqual(path, value);
            case "like":
                return createLikePredicate(cb, path, value);
            case "gt":
            case "lt":
            case "ge":
            case "le":
                return createComparisonPredicate(cb, path, operation, value);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }

    private static LocalDate convertToLocalDate(Object value) {
        if (value instanceof LocalDate) return (LocalDate) value;
        if (value instanceof Instant) return ((Instant) value).atZone(ZoneOffset.UTC).toLocalDate();
        if (value instanceof Timestamp) return ((Timestamp) value).toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        if (value instanceof Date) return ((Date) value).toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        throw new IllegalArgumentException("Cannot convert value to LocalDate: " + value);
    }

    private static Predicate createLikePredicate(
            CriteriaBuilder cb,
            Path<?> path,
            Object value) {

        if (!(value instanceof String)) {
            throw new IllegalArgumentException("LIKE operation requires String value");
        }
        return cb.like(cb.lower(path.as(String.class)),
                "%" + ((String) value).toLowerCase() + "%");
    }

    @SuppressWarnings("unchecked")
    private static Predicate createComparisonPredicate(
            CriteriaBuilder cb,
            Path<?> path,
            String operation,
            Object value) {

        Class<?> fieldType = path.getJavaType();

        if (isTemporalType(fieldType)) {
            Instant instantValue = convertToInstant(value);
            Expression<Instant> instantPath = convertPathToInstant(path, fieldType);

            switch (operation.toLowerCase()) {
                case "gt":
                    return cb.greaterThan(instantPath, instantValue);
                case "lt":
                    return cb.lessThan(instantPath, instantValue);
                case "ge":
                    return cb.greaterThanOrEqualTo(instantPath, instantValue);
                case "le":
                    if (value instanceof LocalDate) {
                        Instant endOfDay = ((LocalDate) value).plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);
                        return cb.lessThanOrEqualTo(instantPath, endOfDay);
                    } else {
                        return cb.lessThanOrEqualTo(instantPath, instantValue);
                    }
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + operation);
            }
        }

        if (value instanceof Comparable) {
            Expression<? extends Comparable> comparablePath = path.as((Class<? extends Comparable>) fieldType);
            Comparable comparableValue = (Comparable) value;

            switch (operation.toLowerCase()) {
                case "gt":
                    return cb.greaterThan(comparablePath, comparableValue);
                case "lt":
                    return cb.lessThan(comparablePath, comparableValue);
                case "ge":
                    return cb.greaterThanOrEqualTo(comparablePath, comparableValue);
                case "le":
                    return cb.lessThanOrEqualTo(comparablePath, comparableValue);
                case "eq":
                    return cb.equal(comparablePath, comparableValue);
                case "ne":
                    return cb.notEqual(comparablePath, comparableValue);
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + operation);
            }
        }

        throw new IllegalArgumentException(
                String.format("Cannot compare %s with operation %s", fieldType.getSimpleName(), operation));
    }

    private static boolean isTemporalType(Class<?> type) {
        return TEMPORAL_TYPES.stream().anyMatch(t -> t.isAssignableFrom(type));
    }

    private static Instant convertToInstant(Object value) {
        if (value instanceof Instant) return (Instant) value;
        if (value instanceof Timestamp) return ((Timestamp) value).toInstant();
        if (value instanceof Date) return ((Date) value).toInstant();
        if (value instanceof LocalDateTime) return ((LocalDateTime) value).atZone(ZoneOffset.UTC).toInstant();
        if (value instanceof LocalDate) return ((LocalDate) value).atStartOfDay(ZoneOffset.UTC).toInstant();
        throw new IllegalArgumentException("Unsupported temporal type: " + value.getClass());
    }

    @SuppressWarnings("unchecked")
    private static Expression<Instant> convertPathToInstant(Path<?> path, Class<?> fieldType) {
        if (fieldType == Instant.class) return (Path<Instant>) path;
        if (fieldType == Timestamp.class) return path.as(Instant.class);
        if (fieldType == Date.class) return path.as(Instant.class);
        if (fieldType == LocalDateTime.class) return path.as(Instant.class);
        if (fieldType == LocalDate.class) return path.as(Instant.class);
        throw new IllegalArgumentException("Cannot convert " + fieldType + " to Instant");
    }

    private static Object convertToType(Class<?> targetType, String value) {
        try {
            if (targetType == String.class) return value;
            if (targetType == Long.class) return Long.parseLong(value);
            if (targetType == Integer.class) return Integer.parseInt(value);
            if (targetType == Boolean.class) return Boolean.parseBoolean(value);

            if (isTemporalType(targetType)) {
                return parseTemporal(value, targetType);
            }

            throw new IllegalArgumentException("Unsupported type: " + targetType);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid format for value: " + value, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T parseTemporal(String value, Class<T> targetType) {
        Instant instant = parseInstant(value);

        if (targetType == Instant.class) return (T) instant;
        if (targetType == Timestamp.class) return (T) Timestamp.from(instant);
        if (targetType == Date.class) return (T) Date.from(instant);
        if (targetType == LocalDateTime.class) return (T) LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        if (targetType == LocalDate.class) return (T) LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();

        throw new IllegalArgumentException("Unsupported temporal type: " + targetType);
    }

    private static Instant parseInstant(String value) {
        value = value.replace(" ", "+");  // Normalize time zone format

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                if (formatter == DateTimeFormatter.ISO_DATE || formatter == DateTimeFormatter.ofPattern("yyyy-MM-dd")) {
                    LocalDate date = LocalDate.parse(value, formatter);
                    return date.atStartOfDay(ZoneOffset.UTC).toInstant();
                }
                return Instant.from(formatter.parse(value));
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException("Unsupported date format: " + value);
    }
}
