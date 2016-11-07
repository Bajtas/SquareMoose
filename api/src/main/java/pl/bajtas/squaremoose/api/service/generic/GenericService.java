package pl.bajtas.squaremoose.api.service.generic;

import org.springframework.data.domain.Page;

import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 18.09.2016.
 */
public interface GenericService<T, K> {
    K getRepository();

    Iterable<T> getAll();

    Page<T> getAll(Integer page, Integer size, String sortBy, String sortDirection);

    T getById(int id);

    Response add(T object);

    Response update(int id, T object);

    Response delete(int id);
}
