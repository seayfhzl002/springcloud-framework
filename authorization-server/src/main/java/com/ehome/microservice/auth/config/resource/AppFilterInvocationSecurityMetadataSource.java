package com.ehome.microservice.auth.config.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

public class AppFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource  superMetadataSource;

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public AppFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource expressionBasedFilterInvocationSecurityMetadataSource){
         this.superMetadataSource = expressionBasedFilterInvocationSecurityMetadataSource;

         // TODO 从数据库加载权限配置
    }

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    
	// 这里的需要从DB加载
    private final Map<String,String> urlRoleMap = new HashMap<String,String>(){{
        put("/open/**","ROLE_ANONYMOUS");
        put("/health","ROLE_ANONYMOUS");
        put("/restart","ROLE_ADMIN");
        put("/demo","ROLE_USER");
    }};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
            if(antPathMatcher.match(entry.getKey(),url)){
                return SecurityConfig.createList(entry.getValue());
            }
        }

        //  返回代码定义的默认配置
        return superMetadataSource.getAttributes(object);
    }



    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}