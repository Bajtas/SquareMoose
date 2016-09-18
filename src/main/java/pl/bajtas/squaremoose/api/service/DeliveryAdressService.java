package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.*;
import pl.bajtas.squaremoose.api.repository.DeliveryAdressRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class DeliveryAdressService implements GenericService<DeliveryAdress, DeliveryAdressRepository> {

    private static final Logger LOG = Logger.getLogger(DeliveryAdressService.class);

    @Autowired  private DeliveryAdressRepository deliveryAdressRepository;
    @Autowired  private ProductRepository productRepository;
    @Autowired  private UserRepository userRepository;

    @Override
    public DeliveryAdressRepository getRepository() {
        return deliveryAdressRepository;
    }

    @Override
    public Iterable<DeliveryAdress> getAll() {
        return getRepository().findAll();
    }

    @Override
    public Page<DeliveryAdress> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<DeliveryAdress> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findAll(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findAll(new PageRequest(page, size));

        return result;
    }

    @Override
    public DeliveryAdress getById(int id) {
        return getRepository().findOne(id);
    }

    public List<DeliveryAdress> getByNameContainsIgnoreCase(String name) {
        return getRepository().findByNameContainsIgnoreCase(name);
    }

    public List<DeliveryAdress> getBySurnameContainsIgnoreCase(String surname) {
        return getRepository().findBySurnameContainsIgnoreCase(surname);
    }

    public List<DeliveryAdress> getByAdressContainsIgnoreCase(String adress) {
        return getRepository().findByAdressContainsIgnoreCase(adress);
    }

    public List<DeliveryAdress> getByTownContainsIgnoreCase(String town) {
        return getRepository().findByTownContainsIgnoreCase(town);
    }

    public List<DeliveryAdress> getByZipCodeContainsIgnoreCase(String zipcode) {
        return getRepository().findByZipCodeContainsIgnoreCase(zipcode);
    }

    public List<DeliveryAdress> getByContactPhoneContainsIgnoreCase(String phone) {
        return getRepository().findByContactPhoneContainsIgnoreCase(phone);
    }

    public Page<DeliveryAdress> getByAdressWithOrderNotNull(Integer page, Integer size, String sortBy, String sortDirection) {
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<DeliveryAdress> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findByOrdersIsNotNull(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findByOrdersIsNotNull(new PageRequest(page, size));

        return result;
    }

    public Page<DeliveryAdress> getByAdressWithOrderNull(Integer page, Integer size, String sortBy, String sortDirection) {
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<DeliveryAdress> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findByOrdersIsNull(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findByOrdersIsNull(new PageRequest(page, size));

        return result;
    }

    public List<DeliveryAdress> getByOrderId(Integer id) {
        return getRepository().findByOrders_Id(id);
    }

    public List<DeliveryAdress> getByUserId(Integer id) {
        return getRepository().findByUsers_Id(id);
    }

    public List<DeliveryAdress> getByUserLogin(String login) {
        return getRepository().findByUsers_Login(login);
    }

    public List<DeliveryAdress> getByUserEmail(String email) {
        return getRepository().findByUsers_Email(email);
    }

    public Response add(DeliveryAdress deliveryAdress) {
        List<Order> orders = deliveryAdress.getOrders();
        List<User> users = deliveryAdress.getUsers();

        try {
            for (User user : users) {
                if (isUserNotExists(user.getId())) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("User with id: " + user.getId() + " not exist! First add user!").build();
                }
            }
            getRepository().save(deliveryAdress);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Product added successfully!").build();
    }

    public Response update(int id, DeliveryAdress deliveryAdress) {
        LOG.info("Trying to save delivery adress with id: " + id);

        try {
            DeliveryAdress old = getRepository().findOne(id);
            if (old != null) {
                deliveryAdress.setId(id);

                getRepository().save(deliveryAdress);
                return Response.status(Response.Status.OK).entity("DeliveryAdress with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("DeliveryAdress with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    private boolean isUserNotExists(Integer id) {
        if (id != null) {
            return userRepository.findOne(id) == null ? true : false;
        } else {
            return true;
        }
    }
}
