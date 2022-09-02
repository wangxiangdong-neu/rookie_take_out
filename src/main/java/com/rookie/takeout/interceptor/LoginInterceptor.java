package com.rookie.takeout.interceptor;

import com.rookie.takeout.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检查
 * 1、配置好拦截器要拦截哪些请求
 * 2、把这些配置放在容器中
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("preHandle拦截的请求路径是{}", requestURI);

        //登录检查逻辑
        Long id = (Long) request.getSession().getAttribute("employee");

        //判断员工登录状态，如果已登录，则直接放行
        if (id != null) {
            log.info("员工已登录，员工id为：{}", id);

            //将当前登录用户的id存入当前线程的副本
            BaseContext.setCurrentId(id);

            //放行
            return true;
        }

        log.info("员工未登录");
        //拦截住。未登录。跳转到登录页
        request.setAttribute("msg", "请先登录");
        response.sendRedirect("/backend/page/login/login.html");
        //response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }

    /**
     * 目标方法执行完成以后
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle执行{}", modelAndView);
    }

    /**
     * 页面渲染以后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion执行异常{}", ex);
    }
}
