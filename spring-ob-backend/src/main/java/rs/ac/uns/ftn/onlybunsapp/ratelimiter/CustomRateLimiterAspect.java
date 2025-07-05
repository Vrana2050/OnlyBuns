package rs.ac.uns.ftn.onlybunsapp.ratelimiter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
public class CustomRateLimiterAspect {
    private final ConcurrentHashMap<String, CustomRateLimiterImpl> rateLimiterMap = new ConcurrentHashMap<>();
    @Around("@annotation(customRateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint, CustomRateLimiter customRateLimiter) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CustomRateLimiter customRateLimiterAnnotation = method.getAnnotation(CustomRateLimiter.class);
        Authentication  authentication=SecurityContextHolder.getContext().getAuthentication();
        User user =(User) authentication.getPrincipal();
        Long userId = user.getId();
        String key = method.getName() + userId;
        CustomRateLimiterImpl rateLimiter ;
        synchronized (rateLimiterMap) {
            if(rateLimiterMap.containsKey(key)) {
                rateLimiter=rateLimiterMap.get(key);
            }
            else
            {
                rateLimiter = new CustomRateLimiterImpl(customRateLimiterAnnotation.maxRequests(),customRateLimiterAnnotation.durationInSeconds());
                rateLimiterMap.put(key, rateLimiter);
            }
        }
        if(rateLimiter.IsAllowed())
            return joinPoint.proceed();
        else
            throw new RuntimeException("Rate limit exceeded for user: " + userId);
    }
}
