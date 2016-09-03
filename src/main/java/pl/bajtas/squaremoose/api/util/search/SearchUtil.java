package pl.bajtas.squaremoose.api.util.search;

import pl.bajtas.squaremoose.api.domain.Product;

import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
public class SearchUtil {
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
}
