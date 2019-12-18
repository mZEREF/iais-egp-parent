package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;


/**
 * @author guyin
 * @date 2019/12/12 20:54
 */
@Slf4j
@Delegator("serviceMenuDelegator")
public class ServiceMenuDelegator {
    private static final String BASESERVICE = "SVTP001";
    private static final String SPECIFIEDSERVICE = "SVTP003";
    @Autowired
    private ServiceConfigService serviceConfigService;

    public void serviceMenuSelection(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do Start start 1...."));

        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getAllService();
        List<HcsaServiceDto> baseService = new ArrayList<>();
        List<HcsaServiceDto> specifiedService = new ArrayList<>();
        for (HcsaServiceDto item: hcsaServiceDtoList
             ) {
            if(BASESERVICE.equals(item.getSvcType())){
                baseService.add(item);
            }
            if(SPECIFIEDSERVICE.equals(item.getSvcType())){
                specifiedService.add(item);
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "baseService", baseService);
        ParamUtil.setRequestAttr(bpc.request, "specifiedService", specifiedService);

    }

    public void validation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do validation start ...."));
        String validation = AppConsts.TRUE;

        ParamUtil.setRequestAttr(bpc.request, "validationValue", validation);
    }

}
