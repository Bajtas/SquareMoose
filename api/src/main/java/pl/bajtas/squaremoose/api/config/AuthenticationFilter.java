package pl.bajtas.squaremoose.api.config;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bajtas.squaremoose.api.service.UserService;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Bajtas on 20.09.2016.
 */

public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter{

    @Context private ResourceInfo resourceInfo;
    @Autowired private UserService userService;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
            .entity("Access blocked for all users !!").build();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if(!method.isAnnotationPresent(PermitAll.class))
        {
            //Access denied for all
            if(method.isAnnotationPresent(DenyAll.class))
            {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty())
            {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            //Get encoded username and password
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            //Decode username and password
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            //Verify user access
            if(method.isAnnotationPresent(RolesAllowed.class))
            {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if( !userService.isUserAllowed(username, password, rolesSet))
                {
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
    }
}
