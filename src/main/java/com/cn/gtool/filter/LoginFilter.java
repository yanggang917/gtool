package com.cn.gtool.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Auther: yg
 * @Date: 2019/12/10 10:32
 * @Description: 登录过滤器，可以用于校验当前用户是否登录
 */
@Component
@WebFilter(urlPatterns = "/",filterName = "loginFilter")
public class LoginFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("================init======================");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        logger.info("================进入过滤器 before======================");
        filterChain.doFilter(servletRequest, servletResponse);
//        logger.info("================进入过滤器 after======================");
    }

    @Override
    public void destroy() {
        logger.info("================destroy======================");
    }
}
