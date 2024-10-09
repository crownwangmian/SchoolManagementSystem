package com.itheima.filter;

import ch.qos.logback.core.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
// @WebFilter("/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //1. 获取请求 强转为http，这样可以拿到请求行
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        // 2. 获取url地址
        String url = req.getRequestURI();
        log.info("请求的url {}", url);
        //判断是否是login请求
        if (url.contains("login")) {
            log.info("登录操作，放行");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 判断令牌是否存在
        String token = req.getHeader("token");
        if (StringUtil.isNullOrEmpty(token)) {
            log.info("token is empty");
            Result error = Result.error("NOT_LOGIN"); // 生成result对象
            String jsonString = JSONObject.toJSONString(error);//json转为string
            resp.getWriter().write(jsonString);  //响应未登录的结果
            return;
        }
        //判断令牌是否正确
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            Result error = Result.error("NOT_LOGIN"); // 生成result对象
            String jsonString = JSONObject.toJSONString(error);//json转为string
            servletResponse.getWriter().write(jsonString);  //响应未登录的结果
            return;
        }
        log.info("令牌合法，放行");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
