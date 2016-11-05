package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.repository.DeliveryAdressRepository;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.PageUtil;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class DeliveryAdressService implements GenericService<DeliveryAdress, DeliveryAdressRepository>, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(DeliveryAdressService.class);

    @Autowired  private DeliveryAdressRepository deliveryAdressRepository;
    @Autowired  private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

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
        PageUtil<DeliveryAdress> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
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
        return getRepository().findByAddressContainsIgnoreCase(adress);
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
        List<DeliveryAdress> addressRelatedToUser = getRepository().findByUsers_Login(login).stream().distinct().collect(Collectors.toList());
        return addressRelatedToUser;
    }

    public List<DeliveryAdress> getByUserEmail(String email) {
        return getRepository().findByUsers_Email(email);
    }

    @Override
    public Response add(DeliveryAdress deliveryAdress) {
        List<User> users = deliveryAdress.getUsers();

        try {
            for (User user : users) {
                if (isUserNotExists(user.getId())) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("User with id: " + user.getId() + " not exist! Add user first!").build();
                }
            }
            getRepository().save(deliveryAdress);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Product added successfully!").build();
    }

    public Response add(String login, DeliveryAdress deliveryAdress) {
        try {
            User user = userRepository.findByLogin(login);
            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("User with id: " + user.getId() + " not exist! Add user first!").build();
            }

            getRepository().save(deliveryAdress);
            user.getDeliveryAdresses().add(deliveryAdress);

            userRepository.save(user);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Product added successfully!").build();
    }

    @Override
    public Response update(int id, DeliveryAdress deliveryAdress) {
        LOG.info("Trying to save delivery adress with id: " + id);

        try {
            DeliveryAdress original = getRepository().findOne(id);
            if (original != null) {
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

    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete DeliveryAdress.");
        String info = "Deleted successfully!";

        LOG.info("Category with id: " + id + " will be deleted.");

        DeliveryAdress deliveryAdress = getRepository().findOne(id);
        if (deliveryAdress != null) {
            try {
                List<User> users = deliveryAdress.getUsers();
                if (users.size() != 0) {
                    for (User user : users) {
                        List<DeliveryAdress> deliveryAdressess = user.getDeliveryAdresses();
                        deliveryAdressess.remove(deliveryAdress);
                    }
                }

                return Response.status(Response.Status.OK).entity("DeliveryAdress with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("DeliveryAdress with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("DeliveryAdress with given id: " + id + " not found!").build();
        }
    }

    private boolean isUserNotExists(Integer id) {
        return id == null || userRepository.findOne(id) == null;
    }
}
