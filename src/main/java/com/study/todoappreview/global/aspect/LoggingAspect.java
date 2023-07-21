package com.study.todoappreview.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    public Object controllerLoggingAround(ProceedingJoinPoint point) throws Throwable {
        long startTime = new Date().getTime();

        Object proceed = point.proceed();

        Object[] args = point.getArgs();

        log.info("requestController : {}, requestURI : {}, request : {}, ms : {}",
                point.getSignature().getDeclaringTypeName(),
                point.getSignature().getName(),
                args,
                new Date().getTime()-startTime);

        return proceed;
    }
}
