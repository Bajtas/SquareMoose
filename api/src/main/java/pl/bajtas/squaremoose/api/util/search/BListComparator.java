package pl.bajtas.squaremoose.api.util.search;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Bajtas on 05.10.2016.
 */
public class BListComparator implements Comparator<Object> {
    private String sortBy;
    private String sortOrder;

    public BListComparator(String sortBy, String sortOrder) {
        this.sortBy = sortBy.toLowerCase();
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Object o1, Object o2) {

        try {
            Field field1 = o1.getClass().getDeclaredField(sortBy);
            Field field2 = o2.getClass().getDeclaredField(sortBy);

            field1.setAccessible(true);
            field2.setAccessible(true);

            if (o1.getClass().getDeclaredField(sortBy).getType() == Long.class) {
                Long d1 = (Long) field1.get(o1);
                Long d2 = (Long) field2.get(o2);
                return (sortOrder.toLowerCase().equals("asc")) ? d1.compareTo(d2) : d2.compareTo(d1);

            } else if (o1.getClass().getDeclaredField(sortBy).getType() == Date.class) {
                Date d1 = (Date) field1.get(o1);
                Date d2 = (Date) field2.get(o2);
                return (sortOrder.toLowerCase().equals("asc")) ? d1.compareTo(d2) : d2.compareTo(d1);

            } else if (o1.getClass().getDeclaredField(sortBy).getType() == double.class) {
                Double d1 = (Double) field1.get(o1);
                Double d2 = (Double) field2.get(o2);
                return (sortOrder.toLowerCase().equals("asc")) ? d1.compareTo(d2) : d2.compareTo(d1);
            } else {
                String d1 = (String) field1.get(o1);
                String d2 = (String) field2.get(o2);
                return (sortOrder.toLowerCase().equals("asc")) ? d1.compareTo(d2) : d2.compareTo(d1);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Missing variable sortBy");
        } catch (ClassCastException e) {
            throw new RuntimeException("sortBy is not found in class list");
        }
    }
}
