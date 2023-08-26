package ru.nikitavov.soup.web.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.nikitavov.soup.web.security.data.PermissionAuthorize;
import ru.nikitavov.soup.web.security.data.JwtAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermissionAuthorizeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            PermissionAuthorize annotation = handlerMethod.getMethod().getAnnotation(PermissionAuthorize.class);
            if (annotation != null) {
                String functionName = annotation.value();
                boolean hasFunction = hasFunctionPermission(functionName);
                if (!hasFunction) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasFunctionPermission(String permissionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
           return jwtAuthenticationToken.getUserPrincipal().getPermissions().contains(permissionName);
        }

        return true;
    }
}
