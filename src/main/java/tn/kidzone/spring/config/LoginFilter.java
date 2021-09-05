package tn.kidzone.spring.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tn.kidzone.spring.controller.UserController;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        UserController userController = (UserController) httpServletRequest.getSession().getAttribute("userController");
        if (userController != null && userController.getAuthenticatedUser() != null && userController.getLoggedIn()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.jsf");
        }
    }
}
