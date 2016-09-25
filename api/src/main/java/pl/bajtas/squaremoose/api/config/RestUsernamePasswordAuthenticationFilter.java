//package pl.bajtas.squaremoose.api.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Authentication filter for REST services
// */
//@Configuration
//public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request,
//                                             HttpServletResponse response) {
//        boolean retVal = false;
//        String username = request.getParameter("j_username");
//        String password = request.getParameter("j_password");
//        if (username != null && password != null) {
//            Authentication authResult = null;
//            try {
//                authResult = attemptAuthentication(request, response);
//                if (authResult == null) {
//                    retVal = false;
//                }
//            } catch (AuthenticationException failed) {
//                try {
//                    unsuccessfulAuthentication(request, response, failed);
//                } catch (IOException e) {
//                    retVal = false;
//                } catch (ServletException e) {
//                    retVal = false;
//                }
//                retVal = false;
//            }
//            try {
//                successfulAuthentication(request, response, null, authResult);
//            } catch (IOException e) {
//                retVal = false;
//            } catch (ServletException e) {
//                retVal = false;
//            }
//            return false;
//        } else {
//            retVal = true;
//        }
//        return retVal;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder;
//    }
//}
//
//
//
//
