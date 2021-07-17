package com.han.auth.configuration.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {

        for(ConfigAttribute configAttribute : collection) {
            //如果你请求的url在数据库中不具备角色，即不存在限制
            if("ROLE_def".equals(configAttribute.getAttribute())) {
                //匿名用户拒绝访问
                if(authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("权限不足，无法访问");
                } else {
                    //登录用户放行
                    return;
                }
            }
            //如果你访问的路径在数据库中存在角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                //查看用户是否拥有该权限,如果有则直接放行
                if(authority.getAuthority().equals(configAttribute.getAttribute()))
                    return;
            }
        }
        //登录却没有相应权限
        throw new AccessDeniedException("权限不足，无法访问");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
