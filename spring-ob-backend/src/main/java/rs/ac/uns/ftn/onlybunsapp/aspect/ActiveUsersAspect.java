package rs.ac.uns.ftn.onlybunsapp.aspect;

import io.micrometer.core.instrument.Gauge;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.onlybunsapp.dto.UserTokenState;
import rs.ac.uns.ftn.onlybunsapp.metric.ActiveUserMetric;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class ActiveUsersAspect {
    @Autowired
    ActiveUserMetric activeUserMetric;
    @AfterReturning(value = "execution(* rs.ac.uns.ftn.onlybunsapp.controller.AuthenticationController.createAuthenticationToken(..))",returning ="result")
    public void afterLogIn(JoinPoint joinPoint,Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;

            // Ispitujemo da li je vraćeni rezultat tipa UserTokenState
            if (response.getBody() instanceof UserTokenState) {
                UserTokenState userTokenState = (UserTokenState) response.getBody();

                String jwt = userTokenState.getAccessToken();  // Dohvati JWT token
                activeUserMetric.userLoggedIn(jwt);  // Prilagoditi logiku prema potrebama
            }
        }
    }
}
