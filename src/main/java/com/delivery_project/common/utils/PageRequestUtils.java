package com.delivery_project.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestUtils {

    public static PageRequest getPageRequest(int page, int size, String sortProperty, boolean ascending) {
        size = adjustPageSize(size);

        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortProperty));
    }

    private static int adjustPageSize(int size) {
        if (size == 10 || size == 30 || size == 50) {
            return size;
        }
        return 10;
    }
}
