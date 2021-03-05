package com.ecquaria.cloud.moh.iais.inspector;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * FeignRequestInterceptor
 *
 * @author Jinhua
 * @date 2020/9/22 17:45
 *
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("--------FeignRequestInterceptor apply start ----------------------");
        AuditTrailDto dto = null;
        //when properties {feign.hystrix.enabled} is false , ServletRequestAttributes will be null
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if (request != null) {
                log.info(StringUtil.changeForLog("--------FeignRequestInterceptor request url :"+ request.getRequestURI()+"----------------------"));
                dto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
            }
        }
        if (dto == null) {
            log.info("--------FeignRequestInterceptor get AuditTrailConsts.SESSION_ATTR_PARAM_NAME is null----------------------");
            dto = AuditTrailDto.getThreadDto();
        }

        if (dto != null) {
            log.info(StringUtil.changeForLog("--------FeignRequestInterceptor ssessionId :" + dto.getSessionId() +"----------"));
            requestTemplate.header("currentAuditTrail", JsonUtil.parseToJson(dto));
        }else {
            log.info(StringUtil.changeForLog("--------FeignRequestInterceptor ssessionId is null ----------"));
        }
        log.info(StringUtil.changeForLog("--------FeignRequestInterceptor requestTemplate :" + requestTemplate.toString()+"----------"));
        log.info("--------FeignRequestInterceptor apply end----------------------");
    }
}
