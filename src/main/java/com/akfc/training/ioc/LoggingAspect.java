package com.akfc.training.ioc;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging(){}

    @Before("executeLogging()")
    public void beforeMethodCall(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length == 0) {
            LOGGER.info("{} called", joinPoint.getSignature().getName());
        }
        else {
            String[] args = Arrays.stream(joinPoint.getArgs())
                    .map(Object::toString)
                    .toArray(String[]::new);
            LOGGER.info("{} called with value {}",
                    joinPoint.getSignature().getName(),
                    String.join(", ", args));
        }
    }

    @Around(value = "executeLogging()")
    public Object aroundMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object returnValue = joinPoint.proceed();
        long duration = System.currentTimeMillis()-startTime;
        LOGGER.info(String.format("Method %s executed in %d ms", joinPoint.getSignature().getName(), duration));
        return returnValue + " How are you ?";
    }

    @AfterReturning(value = "executeLogging()", returning = "returnValue")
    public void logMethodCall(JoinPoint joinPoint, Object returnValue){
        LOGGER.info(String.format("Method %s finished", joinPoint.getSignature().getName()));
    }

}
