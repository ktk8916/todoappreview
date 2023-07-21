package com.study.todoappreview.global.aspect;

import com.study.todoappreview.global.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAspect {

    private final AuthService authService;

    @Around("@annotation(TokenRequired)")
    public Object checkToken(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();

        log.info("request : {}, uri : {}", signature.getName(), signature.getDeclaringTypeName());

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        String token = requestAttributes.getRequest().getHeader("Authorization");
        
        //token이 비어있으면(blank) 예외 
        Map<String, Object> claims = authService.getClaims(token);

        Object proceed = point.proceed();

        log.info("userInfo : {}", claims);
        return proceed;
    }
}
