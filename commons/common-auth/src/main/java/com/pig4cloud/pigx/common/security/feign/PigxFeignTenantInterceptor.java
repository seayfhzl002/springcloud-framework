package com.pig4cloud.pigx.common.security.feign;
//
//package com.pig4cloud.pigx.common.security.feign;
//
//import com.pig4cloud.pigx.common.core.constant.CommonConstants;
//import com.pig4cloud.pigx.common.core.util.TenantContextHolder;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author
// * @date 2018/9/14
// */
//@Slf4j
//public class PigxFeignTenantInterceptor implements RequestInterceptor {
//	@Override
//	public void apply(RequestTemplate requestTemplate) {
//		if(requestTemplate.headers().get(CommonConstants.TENANT_ID) != null){
//			log.info("HEADER 中有租户信息不进行feign增强！！！");
//			return;
//		}
//		if (TenantContextHolder.getTenantId() == null) {
//			log.error("TTL 中的 租户ID为空，feign拦截器 >> 增强失败");
//			return;
//		}
//		requestTemplate.header(CommonConstants.TENANT_ID, TenantContextHolder.getTenantId().toString());
//	}
//}
