package com.gop.agent.statistics;

import com.gop.agent.statistics.interceptor.AccessInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.any;

/**
 * Created by wenzhiwei on 17/1/18.
 */
public class AccessAgent {
    public static Logger logger = LoggerFactory.getLogger(AccessAgent.class);
    public static void premain(String arguments,
                               Instrumentation instrumentation) {
        System.out.println("==============================agent start!======================================");
        logger.info("==============================agent start!======================================");
        new AgentBuilder.Default()
                .type(ElementMatchers.nameEndsWith("Controller"))
                .transform(new AgentBuilder.Transformer() {
                               @Override
                               public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) {
                                   System.out.println(type.asBoxed().getSimpleName());
                                   return builder.method(ElementMatchers.any())
                                           .intercept(MethodDelegation.to(AccessInterceptor.class));
                               }
                           }
                ).installOn(instrumentation);
        System.out.println("==============================agent end!======================================");
    }
}
