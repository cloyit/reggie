package com.cloyit.reggie.filter;


import com.alibaba.fastjson.JSON;
//import com.cloyit.reggie.common.BaseContext;
import com.cloyit.reggie.common.BaseContext;
import com.cloyit.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 检查用户是否已经登录.
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //1.获取本次请求的URL
        String requestURL = request.getRequestURI();
        //定义不需要处理的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        //2.判断本次请求是否需要处理
        boolean match = check(urls,requestURL);

        //3.如果不需要处理直接放行
        if(match){
            log.info("本次请求{}不需要处理",requestURL);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1.判断登录状态,如果已经登录,直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("本次请求已经登录id为:{},不需要处理",request.getSession().getAttribute("employee"));
            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }


        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，用户id为:{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        //4-2.判断登录状态,如果已经登录,直接放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("本次请求已经登录id为:{},不需要处理",request.getSession().getAttribute("user"));
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //5,未登录直接返回未登录结果
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;

    }

    /**
     * 检测请求是否放行
     * @param urls
     * @param requestURL
     * @return
     */
    public boolean check(String[] urls, String requestURL){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url,requestURL);
            if(match){
                return true;
            }

        }return false;
    }
}

