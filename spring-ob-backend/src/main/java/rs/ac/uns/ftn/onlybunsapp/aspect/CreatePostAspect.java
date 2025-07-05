package rs.ac.uns.ftn.onlybunsapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.onlybunsapp.dto.UserTokenState;
import rs.ac.uns.ftn.onlybunsapp.metric.CreatePostDurationMetric;

import java.time.Instant;

@Aspect
@Component
public class CreatePostAspect {
    @Autowired
    private CreatePostDurationMetric postMetric;
    @Around(value ="execution(* rs.ac.uns.ftn.onlybunsapp.controller.PostController.createPost(..))")
    public Object aroundPostCreation(ProceedingJoinPoint joinPoint)throws Throwable
    {
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        if(result instanceof ResponseEntity)
            if(((ResponseEntity)result).getStatusCode().equals(HttpStatus.OK)) {
                Instant endTime = Instant.now();
                long duration = endTime.toEpochMilli() - startTime.toEpochMilli(); // Izračunavanje trajanja
                postMetric.postCreated(duration);
                System.out.println("Post created in " + duration + " ms");
            }
        return result;
    }
}
