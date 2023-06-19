package com.ustlearn.filter;

import com.alibaba.fastjson.JSON;
import com.ustlearn.common.BaseContext;
import com.ustlearn.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//过滤器为javaWeb框架中,不在springboot框架中
//拦截器为springmvc框架.
//urlPattern 设置拦截的路径
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    //下面请求获取的路径和要放行的数组的通配符路径进行匹配,否则数组中的路径和获取到的路径不匹配.
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1 获取本次请求的URI
        String requestURI = request.getRequestURI(); //backend/index.html

        log.info("拦截到请求:{}", requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"

        };

        //2 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3 如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4-1 判断登录状态，如果已登录，则直接放行 ,员工
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录,用户id为:{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            //将id存到threadLocal中.方便后面做自动填充时使用.
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        //4-2 判断登录状态，如果已登录，则直接放行 ,用户
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录,用户id为:{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            //将id存到threadLocal中.方便后面做自动填充时使用.
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }



        log.info("用户未登录");
        //5 如果未登录则返回未登录结果,通过输出流的方式向客户端页面响应数据
        //查看js代码，前端响应格式为NOTLOGIN跳转页面，匹配格式，所以通过输出流方式用fastjson转json
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestUrl
     * @return
     */
    public boolean check(String[] urls, String requestUrl) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
