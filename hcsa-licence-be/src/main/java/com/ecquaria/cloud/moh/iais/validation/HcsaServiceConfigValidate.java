package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * HcsaServiceConfigValidate
 *
 * @author suocheng
 * @date 7/11/2022
 */
@Slf4j
public class HcsaServiceConfigValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(Object obj,HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate start ..."));
        ConfigService configService = SpringContextHelper.getContext().getBean(ConfigService.class);
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        HcsaServiceConfigDto hcsaServiceConfigDto = (HcsaServiceConfigDto)obj;
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        String serviceType = hcsaServiceDto.getSvcType();
        if(StringUtil.isEmpty(serviceType)){
            serviceType = HcsaServiceDto.VALIDATE_COMM;
        }
        log.info(StringUtil.changeForLog("The serviceType is -->;"+serviceType));
        ValidationResult validationResult = WebValidationHelper.validateProperty(hcsaServiceDto,serviceType);
        if(validationResult.isHasErrors()){
            result.putAll(validationResult.retrieveAll());
        }
        //validate the svcCode and svcName repetition
        Map<String, Boolean> entity = configService.isExistHcsaService(hcsaServiceDto);
        Boolean svcCode = entity.get("svcCode");
        if(svcCode!=null&&svcCode){
            result.put("svcCode","SC_ERR002");
        }
        Boolean svcName = entity.get("svcName");
        if(svcName!=null&&svcName){
            result.put("svcName","SC_ERR001");
        }


        log.info(StringUtil.changeForLog("The HcsaServiceConfigValidate end ..."));
        return result;
    }
}
