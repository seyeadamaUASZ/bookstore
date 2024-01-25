package com.sid.gl.aspects;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    @Pointcut(value="execution(* com.sid.gl.*.*.*(..))")
    public void myPointCut(){

    }

    @Around("myPointCut()")
    public Object centralizeLogger(ProceedingJoinPoint jpj) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        String methodName = jpj.getSignature().getName();
        String className = jpj.getTarget().getClass().toString();
        Object[] array= jpj.getArgs();
        log.info("method invoked " + className + " : " + methodName + "()" + "arguments : "
                + mapper.writeValueAsString(array));
        Object object = jpj.proceed();
        /*log.info(className + " : " + methodName + "()" + "Response : "
                + mapper.writeValueAsString(object));*/
        return object;
    }
}
