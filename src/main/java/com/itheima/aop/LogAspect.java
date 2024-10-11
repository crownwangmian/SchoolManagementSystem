package com.itheima.aop;

import com.alibaba.fastjson.JSONObject;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j // log日志
@Component //加入bean
@Aspect // 标注为切面类
public class LogAspect {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.itheima.anno.Log)")  // 只要方法上有这个注解，就是用aop
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 登录人的id

        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        Integer operateUser = (Integer) claims.get("id");
        System.out.println("操作员==============================" + operateUser);

        // 操作时间
        LocalDateTime operateTime = LocalDateTime.now();
        // 类名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        Long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        // 将结果转为jason
        String returnValue = JSONObject.toJSONString(result);

        // 耗时
        Long costTime = System.currentTimeMillis() - startTime;


        OperateLog operateLog = new OperateLog(null, operateUser, operateTime, className, methodName, methodParams, returnValue, costTime);
        operateLogMapper.insert(operateLog);
        log.info("Aop操作日志：{}", operateLog);


        return result;
    }
}
