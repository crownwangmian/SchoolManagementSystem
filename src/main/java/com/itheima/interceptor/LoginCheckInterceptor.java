package com.itheima.interceptor;

import ch.qos.logback.core.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component //交给ioc, 定义拦截器
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        // 2. 获取url地址
        String url = req.getRequestURI();
        log.info("请求的url {}", url);
        //判断是否是login请求
        if (url.contains("login")) {
            log.info("登录操作，放行");
            return true;
        }
        // 判断令牌是否存在
        String token = req.getHeader("token");
        if (StringUtil.isNullOrEmpty(token)) {
            log.info("token is empty");
            Result error = Result.error("NOT_LOGIN"); // 生成result对象
            String jsonString = JSONObject.toJSONString(error);//json转为string
            resp.getWriter().write(jsonString);  //响应未登录的结果
            return false;
        }
        //判断令牌是否正确
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            Result error = Result.error("NOT_LOGIN"); // 生成result对象
            String jsonString = JSONObject.toJSONString(error);//json转为string
            resp.getWriter().write(jsonString);  //响应未登录的结果
            return false;
        }
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}


