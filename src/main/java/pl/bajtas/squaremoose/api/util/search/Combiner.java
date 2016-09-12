package pl.bajtas.squaremoose.api.util.search;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bajtas on 11.09.2016.
 */
public class Combiner<T> {
    private static final Logger LOG = Logger.getLogger(Combiner.class);

    private List<List<T>> listsToCombine;

    public Combiner(List<List<T>> listsToCombine) {
        this.listsToCombine = listsToCombine;
    }

    public List<T> combine() {
        LOG.info("Number of lists to combine: " + listsToCombine.size());

        if (listsToCombine.size() == 0)
            return null;

        removeEmptyLists();

        int listsSize = listsToCombine.size();
        List<T> result = new ArrayList<T>();

        result = listsToCombine.get(0);

        for (int i=1; i<listsSize;i++) {
            result = ListUtils.intersection(result, listsToCombine.get(i));
        }

        LOG.info("Result list size: " + result.size());
        return result;
    }

    private void removeEmptyLists() {
        for (int i = 0; i < listsToCombine.size() ; i++)
        {
            List<T> list = listsToCombine.get(i);
            if (list.size() == 0) {
                listsToCombine.remove(list);
                i--;
            }
        }
    }

    public List<List<T>> getListsToCombine() {
        return listsToCombine;
    }

    public void setListsToCombine(List<List<T>> listsToCombine) {
        this.listsToCombine = listsToCombine;
    }
}
