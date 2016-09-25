//package pl.bajtas.squaremoose.api.util;
//
//import org.apache.log4j.Logger;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//
///**
// * Created by Bajtas on 18.09.2016.
// */
//public class TestWrapper<T, K> {
//    private static final Logger LOG = Logger.getLogger(TestWrapper.class);
//    private T entity;
//    public boolean getAll(K repository, String url, RestTemplate restTemplate, Class<T> clazz) {
//        try {
//            Method method = repository.getClass().getMethod("count");
//            long responseCounter = ((Number)method.invoke(repository)).longValue();
//
//            Class<?> namedClass = Class.forName("[L" + entity.getClass().getName() + ";");
//
//            ResponseEntity<Class> responseEntity = restTemplate.getForEntity(url, namedClass);
//
//            return true;
//        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException |  IllegalAccessException | InvocationTargetException | ClassNotFoundException e){
//            LOG.error("Error: ", e);
//            return false;
//        }
//    }
//}
