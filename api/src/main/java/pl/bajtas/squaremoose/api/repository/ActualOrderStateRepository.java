package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface ActualOrderStateRepository  extends CrudRepository<ActualOrderState, Integer> {
    /* Find by Product properties */
    // First method, by id is not needed
    ActualOrderState findByName(String name);
    List<ActualOrderState> findByNameContainsIgnoreCase(String name);
    List<ActualOrderState> findByDescriptionContainsIgnoreCase(String description);
    Page<ActualOrderState> findAll(Pageable pageable);

    /* Find by Order properties */
    ActualOrderState findByOrder_Id(int id);
}
