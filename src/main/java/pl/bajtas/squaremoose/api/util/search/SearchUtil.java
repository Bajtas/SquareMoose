package pl.bajtas.squaremoose.api.util.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.service.ProductService;

import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
public class SearchUtil {

    private static final Logger LOG = Logger.getLogger(SearchUtil.class);

    public static List<Product> combine(List<List<Product>> lists) {
        if (lists.size() == 0)
            return null;

        List<Product> mainlist = lists.get(0);
        int listsSize = lists.size();

        for (int i=1; i<listsSize;i++) {
            mainlist.retainAll(lists.get(i));
        }

        return mainlist;
    }

    public static List<UserRole> combine2(List<List<UserRole>> lists) {
        if (lists.size() == 0)
            return null;

        List<UserRole> mainlist = lists.get(0);
        int listsSize = lists.size();

        for (int i=1; i<listsSize;i++) {
            mainlist.retainAll(lists.get(i));
        }

        return mainlist;
    }

    public static List<ProductImage> combine3(List<List<ProductImage>> lists) {
        if (lists.size() == 0)
            return null;

        List<ProductImage> mainlist = lists.get(0);
        int listsSize = lists.size();

        for (int i=1; i<listsSize;i++) {
            mainlist.retainAll(lists.get(i));
        }

        return mainlist;
    }

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

    public static List<User> combine4(List<List<User>> lists) {
        if (lists.size() == 0)
            return null;

        List<User> mainlist = lists.get(0);
        int listsSize = lists.size();

        for (int i=1; i<listsSize;i++) {
            mainlist.retainAll(lists.get(i));
        }

        return mainlist;
    }
}
