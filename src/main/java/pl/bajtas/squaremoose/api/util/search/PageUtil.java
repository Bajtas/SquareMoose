package pl.bajtas.squaremoose.api.util.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Bajtas on 23.09.2016.
 */
public class PageUtil<T> {
    private static final Logger LOG = Logger.getLogger(PageUtil.class);
    public Page<T> getPage(Integer page, Integer size, String sortBy, String sortDirection, Object repository) {
        LOG.info("Getting page: " + page + " size: " + size + " sorted by: " + sortBy + " with sorting direction: " + sortDirection);
        Method method;

        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<T> results = null;
        try {
            method = repository.getClass().getMethod("findAll", Pageable.class);

            if (!unsorted) {
                direction = SearchUtil.determineSortDirection(sortDirection);
                results = (Page<T>)method.invoke(repository, new PageRequest(page, size, direction, sortBy));
            }
            else
                results = (Page<T>)method.invoke(repository, new PageRequest(page, size));
        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            LOG.error("Error: ", ex);
        }

        return results;
    }
}
