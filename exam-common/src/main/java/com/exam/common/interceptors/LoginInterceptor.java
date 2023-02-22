package com.exam.common.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private Logger log= LoggerFactory.getLogger(HandlerInterceptor.class);
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("request:"+request.getRequestURI());
        Object teacher = request.getSession();
        if (teacher==null){
            request.getRequestDispatcher("/common/timeout.html").forward(request,response);
            return false;
        }
        return true;
    }
}
