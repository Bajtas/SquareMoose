package pl.bajtas.squaremoose.api.util.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;

/**
 * Created by Bajtas on 03.09.2016.
 */
public class SearchUtil {

    private static final Logger LOG = Logger.getLogger(SearchUtil.class);

    public static Sort.Direction determineSortDirection(String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (StringUtils.isNotEmpty(sortDirection)) {
            switch (sortDirection) {
                case "asc":
                    direction = Sort.Direction.ASC;
                    break;
                case "desc":
                    direction = Sort.Direction.DESC;
                    break;
                default:
                    LOG.warn("Sorting direction: " + sortDirection + " not supported! \n Results will be sorted with default direction ASC.");
                    break;
            }
        }

        return direction;
    }
}
