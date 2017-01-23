package com.gop.agent.statistics.utils;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by wenzhiwei on 17/1/20.
 */
public class HelloMain {

    public static void main(String[] args) {
        premain("", ByteBuddyAgent.install());
        new SampleController().foo();
    }

    public static void premain(String arguments, Instrumentation instrumentation) {
        System.out.println("==============================agent start!======================================");
        new AgentBuilder.Default().with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .type(ElementMatchers.nameEndsWith("Controller"))
                .transform(new AgentBuilder.Transformer() {
                               @Override
                               public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                                       TypeDescription type,
                                                                       ClassLoader classLoader,
                                                                       JavaModule module) {
                                   System.out.println(type.asBoxed().getSimpleName());
                                   return builder.method(ElementMatchers.any()).intercept(MethodDelegation.to(AccessInterceptor.class));
                               }
                           }
                ).installOn(instrumentation);
        System.out.println("==============================agent end!======================================");
    }

    public static class AccessInterceptor {

        @RuntimeType
        public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
            Logger logger = LoggerFactory.getLogger(method.getClass());
            logger.info("wohhh");
            System.out.println("interceptor start!!!");
            long start = System.currentTimeMillis();
            try {
                return callable.call();
            } finally {
                System.out.println(method + " took " + (System.currentTimeMillis() - start));
            }
        }
    }

    public static class SampleController {
        public void foo() {
            System.out.println("foo");
        }
    }

}
