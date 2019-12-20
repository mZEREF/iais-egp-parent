package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
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

        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
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
        ParamUtil.setSessionAttr(bpc.request, "baseServiceDto", (Serializable) baseService);
        ParamUtil.setRequestAttr(bpc.request, "specifiedService", specifiedService);

    }

    public void validation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do validation start ...."));
        String[] basechks = ParamUtil.getStrings(bpc.request, "basechk");
        String[] sepcifiedchk = ParamUtil.getStrings(bpc.request, "sepcifiedchk");
        List<HcsaServiceDto> baseService = (List<HcsaServiceDto> )ParamUtil.getSessionAttr(bpc.request,"baseServiceDto");
        if(basechks == null){
            String noneerr = "please select base service.";
            ParamUtil.setRequestAttr(bpc.request, "err", noneerr);
            ParamUtil.setRequestAttr(bpc.request, "validationValue", AppConsts.FALSE);
        }else{
            List<String> baselist = new ArrayList<>();
            List<String> sepcifiedlist = new ArrayList<>();
            for (String item:basechks
            ) {
                baselist.add(item);
            }
            if(sepcifiedchk != null){
                for (String item:sepcifiedchk
                ) {
                    sepcifiedlist.add(item);
                }
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
                List<String> necessaryBaseServiceList = new ArrayList<>();
                for (String item: sepcifiedlist
                ) {
                    for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList
                    ) {
                        if(dto.getSpecifiedSvcId().equals(item)){
                            necessaryBaseServiceList.add(dto.getBaseSvcId());
                        }
                    }
                }
                necessaryBaseServiceList.removeAll(baselist);
                if(necessaryBaseServiceList.size() > 0){
                    String err = "";
                    for (String item:necessaryBaseServiceList
                         ) {
                        for (HcsaServiceDto dto:baseService
                             ) {
                            if(dto.getId().equals(item)){
                                err = err + dto.getSvcName() + ",";
                            }
                        }
                    }
                    err = err.substring(0,err.length() - 1);
                    err = err + " should be selected.";
                    ParamUtil.setRequestAttr(bpc.request, "err", err);
                    ParamUtil.setRequestAttr(bpc.request, "validationValue", AppConsts.FALSE);
                }else{
                    ParamUtil.setSessionAttr(bpc.request, "baseService", (Serializable) baselist);
                    ParamUtil.setSessionAttr(bpc.request, "specifiedService", (Serializable) sepcifiedlist);
                    ParamUtil.setRequestAttr(bpc.request, "validationValue", AppConsts.TRUE);
                }
            }else{
                ParamUtil.setSessionAttr(bpc.request, "baseService", (Serializable) baselist);
                ParamUtil.setSessionAttr(bpc.request, "specifiedService", (Serializable) sepcifiedlist);
                ParamUtil.setRequestAttr(bpc.request, "validationValue", AppConsts.TRUE);
            }
        }
    }


}
