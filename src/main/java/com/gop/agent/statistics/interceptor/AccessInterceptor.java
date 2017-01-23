package com.gop.agent.statistics.interceptor;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by wenzhiwei on 17/1/18.
 */
public class AccessInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {
        Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);
        System.out.println("start!323");
        logger.info("start!!ewe");
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            System.out.println(method + " took " + (System.currentTimeMillis() - start));
            logger.info(method + " took " + (System.currentTimeMillis() - start));
        }
    }

}
