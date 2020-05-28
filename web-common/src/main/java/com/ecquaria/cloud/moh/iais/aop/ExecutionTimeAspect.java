package com.ecquaria.cloud.moh.iais.aop;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: yichen
 * @description: Output function execution time
 * @date:2020/3/28
 **/

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Pointcut("@annotation(com.ecquaria.cloud.moh.iais.annotation.TimerTrack)")
    public static void executionTimeAspect(){
        throw new UnsupportedOperationException();
    }

    @Around("executionTimeAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String clazzName = joinPoint.getTarget().getClass().getName();
        long start = System.currentTimeMillis();
        log.info("feign call execution timer start");

        log.info(StringUtil.changeForLog("feign call execution timer [ " + clazzName + "]"));

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long timeMillis = end - start;
        float seconds = (float) (end - start) / 1000;

        log.info(StringUtil.changeForLog("feign call execution timer end [ " + timeMillis + "]" + "ms"));
        log.info(StringUtil.changeForLog("feign call execution timer end [ " + seconds + "]" + "s"));

        return result;
    }
}
