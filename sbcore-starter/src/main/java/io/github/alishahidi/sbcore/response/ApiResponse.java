package io.github.alishahidi.sbcore.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class ApiResponse<T> implements Serializable {
    String type;
    int status;
    T data;
    Map<String, Object> meta;
    LocalDateTime timestamp;

    public static <T> ApiResponse<List<T>> success(Page<T> page) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("currentPage", page.getNumber());
        meta.put("totalItems", page.getTotalElements());
        meta.put("totalPages", page.getTotalPages());
        meta.put("pageSize", page.getSize());
        meta.put("isLastPage", page.isLast());

        List<T> responseData = page.getContent();

        return ApiResponse.<List<T>>builder()
                .type(ApiResponseType.DATA.getName())
                .status(ApiResponseType.DATA.getStatus().value())
                .data(responseData)
                .meta(meta)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        if (data instanceof Page<?> page) {
            return (ApiResponse<T>) success(page);
        }

        return ApiResponse.<T>builder()
                .type(ApiResponseType.DATA.getName())
                .status(ApiResponseType.DATA.getStatus().value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.ERROR.getName())
                .status(status.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> info(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.INFO.getName())
                .status(status.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> warning(T data) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.WARNING.getName())
                .status(ApiResponseType.WARNING.getStatus().value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
