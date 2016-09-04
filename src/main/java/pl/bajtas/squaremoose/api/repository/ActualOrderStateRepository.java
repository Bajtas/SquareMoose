package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Category;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface ActualOrderStateRepository  extends CrudRepository<ActualOrderState, Integer> {
}
