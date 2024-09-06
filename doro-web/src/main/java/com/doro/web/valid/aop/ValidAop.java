package com.doro.web.valid.aop;

import com.doro.api.model.request.RequestComment;
import com.doro.web.valid.rule.CommentRule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiage
 */
@Aspect
@Component
public class ValidAop {

    @Around(value = "@annotation(validParam)")
    public Object around(ProceedingJoinPoint jp, ValidParam validParam) throws Throwable {
        // 获取方法参数
        Object[] args = jp.getArgs();
        // 获取方法信息
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        // 获取注解信息
//        Class<?> clazz = validParam.ruleClazz();
//        Object obj = clazz.newInstance();
//        Method[] methods = clazz.getMethods();
//        for (Method m : methods) {
//           Parameter[] parameters =  m.getParameters();
//            for (Parameter parameter: parameters) {
//                parameter.getName()
//            }
//            m.invoke(obj, );
//        }

        return jp.proceed();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = CommentRule.class;
        Method[] methods = clazz.getDeclaredMethods();

        Map<Class<?>, Object> map = new HashMap<>();
        RequestComment requestComment = new RequestComment();
        map.put(RequestComment.class, requestComment);

        for (Method m : methods) {
//            System.out.println(m.getName());
            Parameter[] parameters = m.getParameters();
            Object[] params = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                params[i] = map.get(parameters[i].getType());
            }
            m.setAccessible(true);
            System.out.println(m.invoke(null, params));
        }
    }
}
