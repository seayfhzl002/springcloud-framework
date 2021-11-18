package com.pig4cloud.pigx.common.security.filter;
//
//package com.pig4cloud.pigx.common.security.filter;
//
//import cn.hutool.core.util.StrUtil;
//import com.pig4cloud.pigx.common.core.constant.CommonConstants;
//import com.pig4cloud.pigx.common.core.util.TenantContextHolder;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author
// * @date 2018/9/13
// */
//@Slf4j
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class TenantContextHolderFilter extends GenericFilterBean {
//
//	@Override
//	@SneakyThrows
//	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
//		HttpServletRequest request = (HttpServletRequest) servletRequest;
//		HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//		String tenantId = request.getHeader(CommonConstants.TENANT_ID);
//		log.info("获取header中的租户ID为:{}", tenantId);
//
//		if (StrUtil.isNotBlank(tenantId)) {
//			TenantContextHolder.setTenantId(tenantId);
//		} else {
//			TenantContextHolder.setTenantId(CommonConstants.TENANT_ID_DEV);
//		}
//
//		filterChain.doFilter(request, response);
//		TenantContextHolder.clear();
//	}
//}
