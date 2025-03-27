package io.github.alishahidi.sbcore.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    public static Pageable createPageable(int page, int size, String[] sort) {
        if (sort == null || sort.length == 0) {
            return PageRequest.of(page, size);
        }

        Sort.Direction direction = Sort.Direction.ASC;
        String property = sort[0];

        if (sort[0].contains(",")) {
            String[] parts = sort[0].split(",");
            property = parts[0];
            direction = parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.ASC;
        }

        Sort sortObj = Sort.by(direction, property);

        if (sort.length > 1) {
            for (int i = 1; i < sort.length; i++) {
                String[] parts = sort[i].contains(",") ?
                        sort[i].split(",") :
                        new String[]{sort[i], "asc"};
                sortObj = sortObj.and(Sort.by(
                        Sort.Direction.fromString(parts[1]),
                        parts[0]
                ));
            }
        }

        return PageRequest.of(page, size, sortObj);
    }
}