package com.han.auth.configuration.security;

import com.han.auth.entity.Role;
import com.han.auth.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.han.auth.configuration.property.SystemCofing.ANONYMOUS_USER_ROLE;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private UserRoleService userRoleService;

    @Autowired
    public CustomFilterInvocationSecurityMetadataSource(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object obj) throws IllegalArgumentException {
        //获取当前用户请求的url
        String requestUrl = ((FilterInvocation) obj).getRequestUrl();
        //数据库中查询出所有路径
        List<Role> roleList = userRoleService.getAllRole();
        List<String> roles = new ArrayList<>();
        for(Role role : roleList) {
            if(antPathMatcher.match(role.getPath(),requestUrl)) {
                roles.add(role.getName());
            }
        }
        String[] roleStr = new String[roles.size()];
        roles.toArray(roleStr);
        if(roles.size() != 0)
            return SecurityConfig.createList(roleStr);
        else
            return SecurityConfig.createList(ANONYMOUS_USER_ROLE);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
