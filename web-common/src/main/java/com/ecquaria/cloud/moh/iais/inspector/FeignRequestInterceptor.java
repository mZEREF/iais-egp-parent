package com.ecquaria.cloud.moh.iais.inspector;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * FeignRequestInterceptor
 *
 * @author Jinhua
 * @date 2020/9/22 17:45
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        if (request == null) {
            return;
        }
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if (dto != null) {
            requestTemplate.header("currentAuditTrail", JsonUtil.parseToJson(dto));
        }
    }
}
