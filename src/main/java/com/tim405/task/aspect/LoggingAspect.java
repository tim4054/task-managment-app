package com.tim405.task.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("@annotation(com.tim405.task.aspect.annotations.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Вызван метод: {}",
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(
            pointcut = "@annotation(com.tim405.task.aspect.annotations.LogException)",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Исключение {}: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }

    @AfterReturning(
            pointcut = "@annotation(com.tim405.task.aspect.annotations.LogResult)",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("AfterReturning: Метод {} вернул {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @Around("@annotation(com.tim405.task.aspect.annotations.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        log.info("Метод {} выполнен за {} ms",
                joinPoint.getSignature().getName(),
                executionTime);
        return result;
    }
}