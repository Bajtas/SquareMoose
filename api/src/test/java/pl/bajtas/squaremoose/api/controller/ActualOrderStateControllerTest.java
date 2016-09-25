package pl.bajtas.squaremoose.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.bajtas.squaremoose.api.SpringBootWebApplication;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.OrderState;
import pl.bajtas.squaremoose.api.domain.OrderStateHistory;
import pl.bajtas.squaremoose.api.repository.*;
import pl.bajtas.squaremoose.api.util.TestPageImpl;
import pl.bajtas.squaremoose.api.util.TestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Bajtas on 18.09.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8080")
public class ActualOrderStateControllerTest {
    private static final String CONTROLLER_URL = TestUtil.getHost() + "ActualOrderStateService";
    private static final String GET_ALL_STATES_URL = CONTROLLER_URL + "/actualstates";
    private static final String GET_ALL_STATES_PAGE_URL = CONTROLLER_URL + "/actualstates/page/{number}";
    private static final String GET_ALL_STATES_PAGE_WITH_SIZE_URL = GET_ALL_STATES_PAGE_URL + "?size={size}";
    private static final String GET_BY_ID = CONTROLLER_URL + "/actualstate/{id}";
    private static final String GET_BY_NAME = CONTROLLER_URL + "/actualstates/name/{name}";
    private static final String GET_BY_DESCRIPTION = CONTROLLER_URL + "/actualstates/description/{description}";
    private static final String GET_BY_ORDER_ID = CONTROLLER_URL + "/actualstate/order/{id}";
    private static final String ADD_ACTUAL_STATE_URL = CONTROLLER_URL + "/actualstate/add";
    private static final String UPDATE_ACTUAL_STATE_URL = CONTROLLER_URL + "/actualstate/{id}/update";
    private static final String DELETE_ACTUAL_STATE_URL = CONTROLLER_URL + "/actualstate/{id}/delete";

    @Autowired
    private ActualOrderStateRepository actualOrderStateRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImagesRepository productImagesRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;
    @Autowired
    private OrderStateRepository orderStateRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void getAllTest() {
        long responseCounter = actualOrderStateRepository.count();

        ResponseEntity<ActualOrderState[]> responseEntity = restTemplate.getForEntity(GET_ALL_STATES_URL, ActualOrderState[].class);
        ActualOrderState[] objects = responseEntity.getBody();

        MediaType contentType = responseEntity.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = responseEntity.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        assertEquals(responseCounter, objects.length);
    }

    @Test
    public void getAllPagesDefaultSettingsTest() {
        // first page
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
        }, 0);

        Page<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i = 0; i < totalPages; i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
            }, i, null);

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> states = object.getContent();
            for (ActualOrderState state : states)
                assertNotNull(state.getId());

            assertEquals(object.getSize(), 20);
            assertNull(object.getSort());
        }
        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getAllPagesSizeIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
        }, 0, randomSize);

        Page<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i = 0; i < totalPages; i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
            }, i, randomSize);

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> states = object.getContent();
            for (ActualOrderState state : states)
                assertNotNull(state.getId());

            assertEquals(randomSize, object.getSize());
            assertNull(object.getSort());
        }

        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getAllPagesSizeAndSortWithoutDirectionIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
        }, 0, randomSize, "name");

        TestPageImpl<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i = 0; i < totalPages; i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
            }, i, randomSize, "name");

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> products = object.getContent();
            for (ActualOrderState product : products)
                assertNotNull(product.getId());

            assertNotNull(object.getSort());
        }

        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getAllPagesSizeAndSortWithDirectionIsSpecifiedTest() {
        // first page
        Random rand = new Random();
        int randomSize = rand.nextInt(500);
        ResponseEntity<TestPageImpl<ActualOrderState>> pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}&dir={dir}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
        }, 0, randomSize, "name", "desc");

        Page<ActualOrderState> object = pageActualOrderState.getBody();
        MediaType contentType = pageActualOrderState.getHeaders().getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        HttpStatus statusCode = pageActualOrderState.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);
        assertEquals(contentType, MediaType.APPLICATION_JSON);

        int totalPages = object.getTotalPages();

        for (int i = 0; i < totalPages; i++) {
            pageActualOrderState = restTemplate.exchange(GET_ALL_STATES_PAGE_WITH_SIZE_URL + "&sortBy={sortBy}&dir={dir}", HttpMethod.GET, null, new ParameterizedTypeReference<TestPageImpl<ActualOrderState>>() {
            }, i, randomSize, "name", "asc");

            object = pageActualOrderState.getBody();
            contentType = pageActualOrderState.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            statusCode = pageActualOrderState.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            assertEquals(object.getNumber(), i);
            List<ActualOrderState> states = object.getContent();
            for (ActualOrderState state : states)
                assertNotNull(state.getId());

            assertNotNull(object.getSort());

            String name1 = object.getContent().get(i).getName();
            String name2 = "";
            if ((i + 1) == totalPages)
                name2 = object.getContent().get(i + 1).getName();

            if (name1.compareToIgnoreCase(name2) == 0) {
                assertEquals(name1, name2);
            } else {
                boolean compareResult = name1.compareToIgnoreCase(name2) > 0 ? true : false;
                assertFalse(compareResult);
            }
        }

        assertEquals(object.getTotalElements(), actualOrderStateRepository.count());
    }

    @Test
    public void getByIdTest() {
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState state = actualOrderStateRepository.findOne(randomId);
            if (state != null) {
                ResponseEntity<ActualOrderState> responseEntity = restTemplate.getForEntity(GET_BY_ID, ActualOrderState.class, randomId);
                ActualOrderState actualOrderStateFromRest = responseEntity.getBody();

                MediaType contentType = responseEntity.getHeaders().getContentType();
                assertEquals(contentType, MediaType.APPLICATION_JSON);

                HttpStatus statusCode = responseEntity.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                assertEquals(state, actualOrderStateFromRest);
            }
        }
    }

    @Test
    public void getByNameContainsIgnoreCaseTest() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState state = actualOrderStateRepository.findOne(randomId);
            if (state != null) {
                names.add(state.getName());
            }
        }

        for (String name : names) {
            ResponseEntity<ActualOrderState[]> responseEntity = restTemplate.getForEntity(GET_BY_NAME, ActualOrderState[].class, name);
            ActualOrderState[] actualOrderStatesFromRest = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (ActualOrderState state : actualOrderStatesFromRest) {
                boolean result = name.toLowerCase().contains(state.getName().toLowerCase());
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByDescriptionContainsIgnoreCaseTest() {
        List<String> descriptions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState state = actualOrderStateRepository.findOne(randomId);
            if (state != null) {
                descriptions.add(state.getDescription());
            }
        }

        for (String description : descriptions) {
            ResponseEntity<ActualOrderState[]> responseEntity = restTemplate.getForEntity(GET_BY_DESCRIPTION, ActualOrderState[].class, description);
            ActualOrderState[] states = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);

            for (ActualOrderState state : states) {
                boolean result = description.toLowerCase().contains(state.getDescription().toLowerCase());
                assertTrue(result);
            }
        }
    }

    @Test
    public void getByOrderIdTest() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            Order order = orderRepository.findOne(randomId);
            if (order != null) {
                if (order.getActualOrderState() != null) {
                    ids.add(randomId);
                }
            }
        }

        for (int id : ids) {
            ResponseEntity<ActualOrderState> responseEntity = restTemplate.getForEntity(GET_BY_ORDER_ID, ActualOrderState.class, id);
            ActualOrderState state = responseEntity.getBody();

            MediaType contentType = responseEntity.getHeaders().getContentType();
            assertEquals(contentType, MediaType.APPLICATION_JSON);

            HttpStatus statusCode = responseEntity.getStatusCode();
            assertEquals(statusCode, HttpStatus.OK);


            assertEquals(orderRepository.findOne(id).getActualOrderState(), state);
        }
    }

    @Test
    public void addOneWithoutHistoryAndOrderStateTest() {
        ActualOrderState state = new ActualOrderState("test", "test desc");

        long allActualStatesCounterBefore = actualOrderStateRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(state, headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_ACTUAL_STATE_URL, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = out.getStatusCode();
        assertEquals(HttpStatus.OK, statusCode);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "ActualOrderState added successfully!");

        long allActualStatesCounterAfter = actualOrderStateRepository.count();

        assertEquals(allActualStatesCounterBefore + 1, allActualStatesCounterAfter);
    }

    @Test
    public void addOneWithHistoryAndWithoutOrderStateTest() {
        ActualOrderState state = new ActualOrderState("test", "test desc");

        List<OrderStateHistory> histories = new ArrayList<>();
        OrderStateHistory history = new OrderStateHistory();
        history.setName("history name");
        history.setDescription("history description");
        history.setLmod(new Date());
        histories.add(history);

        state.setOrderStateHistories(histories);
        long allActualStatesCounterBefore = actualOrderStateRepository.count();
        long allstatesHistoriesCounterBefore = orderStateHistoryRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(state, headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_ACTUAL_STATE_URL, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = out.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "ActualOrderState added successfully!");

        long allActualStatesCounterAfter = actualOrderStateRepository.count();
        long allStatesHistoriesCounterAfter = orderStateHistoryRepository.count();

        assertEquals(allActualStatesCounterBefore + 1, allActualStatesCounterAfter);
        assertEquals(allstatesHistoriesCounterBefore + 1, allStatesHistoriesCounterAfter);
    }

    @Test
    public void addOneWithHistoryAndWithOrderStateTest() {
        OrderState state = new OrderState("some state", "some description for state");

        ActualOrderState actualState = new ActualOrderState("test", "test desc");

        actualState.setOrderState(state);

        List<OrderStateHistory> histories = new ArrayList<>();
        OrderStateHistory history = new OrderStateHistory();
        history.setName("history name");
        history.setDescription("history description");
        history.setLmod(new Date());
        histories.add(history);

        actualState.setOrderStateHistories(histories);
        long allActualStatesCounterBefore = actualOrderStateRepository.count();
        long allstatesHistoriesCounterBefore = orderStateHistoryRepository.count();
        long allorderStateCounterBefore = orderStateRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(actualState, headers);

        ResponseEntity<String> out = restTemplate.exchange(ADD_ACTUAL_STATE_URL, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = out.getStatusCode();
        assertEquals(statusCode, HttpStatus.OK);

        MediaType contentType = out.getHeaders().getContentType();
        assertEquals(contentType, MediaType.TEXT_PLAIN);

        assertEquals(out.getBody(), "ActualOrderState added successfully!");

        long allActualStatesCounterAfter = actualOrderStateRepository.count();
        long allStatesHistoriesCounterAfter = orderStateHistoryRepository.count();
        long allorderStateCounterAfter = orderStateRepository.count();

        assertEquals(allActualStatesCounterBefore + 1, allActualStatesCounterAfter);
        assertEquals(allstatesHistoriesCounterBefore + 1, allStatesHistoriesCounterAfter);
        assertEquals(allorderStateCounterBefore + 1, allorderStateCounterAfter);
    }

    @Test
    public void updateOneWithHistoryAndOrderStateTest() {
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState actualOrderState = actualOrderStateRepository.findOne(randomId);
            if (actualOrderState != null) {
                if (actualOrderState.getOrderState() == null || actualOrderState.getOrderStateHistories().size() == 0) {
                    boolean orderStateAdded = actualOrderState.getOrderState() == null ? true : false;
                    boolean historyAdded = actualOrderState.getOrderStateHistories().size() == 0 ? true : false;

                    ActualOrderState updatedActualOrderState = new ActualOrderState("updatedActualState", "updatedDescription");

                    if (orderStateAdded) {
                        OrderState state = new OrderState("updateOrderState", "updatedOrderStateDescription");
                        updatedActualOrderState.setOrderState(state);
                    }

                    if (historyAdded) {
                        OrderStateHistory history = new OrderStateHistory("updated history name", "updated history desc");
                        List<OrderStateHistory> histories = new ArrayList<>();
                        histories.add(history);

                        updatedActualOrderState.setOrderStateHistories(histories);
                    }

                    long allActualStatesCounterBefore = actualOrderStateRepository.count();
                    long allstatesHistoriesCounterBefore = orderStateHistoryRepository.count();
                    long allorderStateCounterBefore = orderStateRepository.count();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity entity = new HttpEntity(updatedActualOrderState, headers);

                    ResponseEntity<String> out = restTemplate.exchange(UPDATE_ACTUAL_STATE_URL, HttpMethod.PUT, entity, String.class, randomId);

                    HttpStatus statusCode = out.getStatusCode();
                    assertEquals(statusCode, HttpStatus.OK);

                    MediaType contentType = out.getHeaders().getContentType();
                    assertEquals(contentType, MediaType.TEXT_PLAIN);

                    ActualOrderState actualState = actualOrderStateRepository.findOne(randomId);
                    assertEquals(actualState.getName(), "updatedActualState");
                    assertEquals(actualState.getDescription(), "updatedDescription");

                    assertEquals(out.getBody(), "ActualOrderState with id: " + randomId + " updated successfully!");

                    long allActualStatesCounterAfter = actualOrderStateRepository.count();
                    long allStatesHistoriesCounterAfter = orderStateHistoryRepository.count();
                    long allorderStateCounterAfter = orderStateRepository.count();

                    assertEquals(allActualStatesCounterBefore, allActualStatesCounterAfter);
                    if (orderStateAdded)
                        assertEquals(allorderStateCounterBefore + 1, allorderStateCounterAfter);
                    if (historyAdded)
                        assertEquals(allstatesHistoriesCounterBefore + 1, allStatesHistoriesCounterAfter);
                }
            }
        }
    }

    @Test
    public void deleteOneTest() {
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int randomId = random.nextInt(150);

            ActualOrderState actualOrderState = actualOrderStateRepository.findOne(randomId);
            if (actualOrderState != null) {
                long allActualStatesCounterBefore = actualOrderStateRepository.count();
                long allstatesHistoriesCounterBefore = orderStateHistoryRepository.count();
                long allorderStateCounterBefore = orderStateRepository.count();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                boolean hasOrderState = false;
                boolean hasOrderStateHistories = false;
                int orderHistoriesSize = 0;
                if (actualOrderState.getOrderState() != null)
                    hasOrderState = true;
                if (actualOrderState.getOrderStateHistories().size() != 0) {
                    hasOrderStateHistories = true;
                    orderHistoriesSize = actualOrderState.getOrderStateHistories().size();
                }

                ResponseEntity<String> out = restTemplate.exchange(DELETE_ACTUAL_STATE_URL, HttpMethod.DELETE, null, String.class, randomId);

                HttpStatus statusCode = out.getStatusCode();
                assertEquals(statusCode, HttpStatus.OK);

                MediaType contentType = out.getHeaders().getContentType();
                assertEquals(contentType, MediaType.TEXT_PLAIN);

                ActualOrderState actualState = actualOrderStateRepository.findOne(randomId);
                assertNull(actualState);

                assertEquals(out.getBody(), "ActualOrderState with id: " + randomId + " deleted successfully!");

                long allActualStatesCounterAfter = actualOrderStateRepository.count();
                long allStatesHistoriesCounterAfter = orderStateHistoryRepository.count();
                long allorderStateCounterAfter = orderStateRepository.count();

                assertEquals(allActualStatesCounterBefore - 1, allActualStatesCounterAfter);
                if (hasOrderState)
                    assertEquals(allorderStateCounterBefore, allorderStateCounterAfter);
                if (hasOrderStateHistories)
                    assertEquals(allstatesHistoriesCounterBefore - orderHistoriesSize, allStatesHistoriesCounterAfter);
            }
        }
    }
}
