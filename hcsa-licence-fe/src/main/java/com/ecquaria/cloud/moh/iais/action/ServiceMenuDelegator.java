package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


/**
 * @author guyin
 * @date 2019/12/12 20:54
 */
@Slf4j
@Delegator("serviceMenuDelegator")
public class ServiceMenuDelegator {
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";


    private static final String BASE_SERVICE_CHECK_BOX_ATTR = "basechk";
    private static final String SPECIFIED_SERVICE_CHECK_BOX_ATTR = "sepcifiedchk";
    private static final String BASE_SERVICE_ATTR = "baseService";
    private static final String BASE_SERVICE_ATTR_CHECKED = "baseServiceChecked";
    private static final String SPECIFIED_SERVICE_ATTR_CHECKED = "specifiedServiceChecked";
    private static final String BASE_SERVICE_DTO_ATTR = "baseServiceDto";
    private static final String SPECIFIED_SERVICE_ATTR = "specifiedService";
    private static final String ERROR_ATTR = "err";
    private static final String VALIDATION_ATTR = "validationValue";



    @Autowired
    private ServiceConfigService serviceConfigService;
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the  doStart start 1...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED,null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);

        log.debug(StringUtil.changeForLog("the  doStart end 1...."));
    }

    public void beforeJump(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the  before jump start 1...."));

    }

    public void serviceMenuSelection(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do Start start 1...."));

        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }

        List<HcsaServiceDto> baseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        List<HcsaServiceDto> specifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        List<String> baseServiceChecked = (List<String>)ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED);
        List<String> specifiedServiceChecked = (List<String>)ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED);

        ParamUtil.setRequestAttr(bpc.request, BASE_SERVICE_ATTR, baseService);
        ParamUtil.setRequestAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, baseServiceChecked);
        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, specifiedServiceChecked);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_DTO_ATTR, (Serializable) baseService);
        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR, specifiedService);

    }

    public void validation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do validation start ...."));
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        String[] sepcifiedchk = ParamUtil.getStrings(bpc.request, SPECIFIED_SERVICE_CHECK_BOX_ATTR);
        List<HcsaServiceDto> baseService = (List<HcsaServiceDto> )ParamUtil.getSessionAttr(bpc.request,BASE_SERVICE_DTO_ATTR);
        if(basechks == null){
            String noneerr = "Please select base service.";
            ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, noneerr);
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.FALSE);
        }else{
            List<String> baselist = IaisCommonUtils.genNewArrayList();
            List<String> sepcifiedlist = IaisCommonUtils.genNewArrayList();
            for (String item:basechks) {
                baselist.add(item);
            }
            if(sepcifiedchk != null){
                for (String item:sepcifiedchk) {
                    sepcifiedlist.add(item);
                }
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
                List<String> necessaryBaseServiceList = IaisCommonUtils.genNewArrayList();
                for (String item: sepcifiedlist) {
                    for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                        if(dto.getSpecifiedSvcId().equals(item)){
                            necessaryBaseServiceList.add(dto.getBaseSvcId());
                        }
                    }
                }
                necessaryBaseServiceList.removeAll(baselist);
                if(necessaryBaseServiceList.size() > 0){
                    String err = "";
                    for (String item:necessaryBaseServiceList) {
                        for (HcsaServiceDto dto:baseService) {
                            if(dto.getId().equals(item)){
                                err = err + dto.getSvcName() + ",";
                            }
                        }
                    }
                    err = err.substring(0,err.length() - 1);
                    err = err + " should be selected.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.FALSE);
                }else{
                    ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) baselist);
                    ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) sepcifiedlist);
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.TRUE);
                    ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) baselist);
                    ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedlist);

                }
            }else{
                ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) baselist);
                ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) sepcifiedlist);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.TRUE);
                ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) baselist);
                ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedlist);
            }
        }
    }


}
