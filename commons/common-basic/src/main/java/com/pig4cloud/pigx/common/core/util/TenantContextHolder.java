package com.pig4cloud.pigx.common.core.util;
//
//package com.pig4cloud.pigx.common.core.util;
//
//import com.alibaba.ttl.TransmittableThreadLocal;
//import com.pig4cloud.pigx.common.core.constant.CommonConstants;
//import lombok.experimental.UtilityClass;
//
///**
// * @author
// * @date 2018/10/4
// * 租户工具类
// */
//@UtilityClass
//public class TenantContextHolder {
//
//	private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();
//
//
//	/**
//	 * TTL 设置租户ID
//	 *
//	 * @param tenantId
//	 */
//	public void setTenantId(String tenantId) {
//		THREAD_LOCAL_TENANT.set(tenantId);
//	}
//
//	/**
//	 * 获取TTL中的租户ID
//	 *
//	 * @return
//	 */
//	public String getTenantId() {
//		return THREAD_LOCAL_TENANT.get() == null? CommonConstants.TENANT_ID_DEV:THREAD_LOCAL_TENANT.get();
//	}
//
//	public void clear() {
//		THREAD_LOCAL_TENANT.remove();
//	}
//}
