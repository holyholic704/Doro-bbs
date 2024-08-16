package com.doro.cache.aop;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.doro.cache.anno.MyCache;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author jiage
 */
@Aspect
@Component
public class CacheAop {

    @Pointcut("@annotation(com.doro.cache.anno.MyCache)")
    public void pointCut() {
    }

    @AfterReturning(value = "pointCut()", returning = "value")
    public void after(JoinPoint jp, Object value) {
        Object[] args = jp.getArgs();

        if (ArrayUtil.isNotEmpty(args)) {
            MethodSignature signature = (MethodSignature) jp.getSignature();
            Method method = signature.getMethod();

            MyCache myCache = method.getAnnotation(MyCache.class);

            String key = getKey(signature.getParameterNames(), args, myCache.key());
            if (key != null) {
                String area = myCache.area();
                if (StrUtil.isNotEmpty(area)) {

                }
            }
        }
    }

    private String getKey(String[] parameterNames, Object[] args, String key) {
        if (StrUtil.isNotEmpty(key)) {
            String spelKey = isSpelKey(key);

            String keyParam = spelKey == null ? key : spelKey;

            int index = 0;
            for (index = 0; index < parameterNames.length; index++) {
                String parameterName = parameterNames[index];
                if (keyParam.equals(parameterName)) {
                    break;
                }
            }

            if (index == args.length) {
                return null;
            }

            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(key);
            EvaluationContext ctx = new StandardEvaluationContext();
            ctx.setVariable(parameterNames[index], args[index]);
            return expression.getValue(ctx, String.class);
        }
        return null;
    }

    private String isSpelKey(String key) {
        int start = -1;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (start != -1) {
                if (c == '.') {
                    return key.substring(start + 1, i);
                }
                continue;
            }
            if (c == '#') {
                start = i;
            }
        }
        return null;
    }

}
