package pt.seixal.carlos.util;

import java.util.Map;
import java.util.function.BiConsumer;
import org.springframework.data.domain.Page;
import pt.seixal.carlos.mapper.DozertMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Set;

public class PagedModelUtils {

    private PagedModelUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T, D> Page<D> buildPageModel(
            Map<String, String> params,
            Page<T> pageData,
            Class<D> dtoClass,
            BiConsumer<D, Map<String, String>> addHateoasLinks) {

        Page<D> dtoPage = pageData.map(entity -> {
            D dto = DozertMapper.parseObject(entity, dtoClass);
            if (addHateoasLinks != null) {
                addHateoasLinks.accept(dto, params);
            }
            return dto;
        });

        return dtoPage;
    }

    public static Pageable sanitizePageable(Pageable pageable, Set<String> allowedFields, String defaultSortField) {
        if (pageable == null || pageable.getSort().isUnsorted()) {
            return pageable;
        }

        List<Sort.Order> validOrders = pageable.getSort().stream()
                .filter(order -> allowedFields.contains(order.getProperty()))
                .toList();

        if (validOrders.isEmpty()) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(defaultSortField).ascending()
            );
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(validOrders)
        );
    }
}
