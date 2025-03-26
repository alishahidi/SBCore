package net.alishahidi.sbcore.core.search;

import net.alishahidi.sbcore.core.util.PaginationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public class SearchParamsWrapper {
    public final int page;
    public final int size;
    public final String[] sort;
    public final MultiValueMap<String, String> filters;

    public SearchParamsWrapper(int page, int size, String[] sort,
                               MultiValueMap<String, String> filters) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.filters = filters;
    }

    public Pageable getPageable() {
        return PaginationUtil.createPageable(page, size, sort);
    }
}