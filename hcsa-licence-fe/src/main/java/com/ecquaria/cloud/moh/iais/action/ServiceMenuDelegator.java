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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    private static final String SPECIFIED_SERVICE_ATTR = "specifiedService";
    private static final String SPECIFIED_SERVICE_ATTR_CHECKED = "specifiedServiceChecked";
    private static final String ERROR_ATTR = "err";
    private static final String VALIDATION_ATTR = "validationValue";


    List<HcsaServiceDto> baseService;
    List<HcsaServiceDto> specifiedService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the  doStart start 1...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR,null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);

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

        baseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        specifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        List<String> baseServiceChecked = (List<String>)ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR);
        List<String> specifiedServiceChecked = (List<String>)ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR);

        ParamUtil.setRequestAttr(bpc.request, BASE_SERVICE_ATTR, baseService);
        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR, specifiedService);
        ParamUtil.setRequestAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, baseServiceChecked);
        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, specifiedServiceChecked);


    }

    public void validation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do validation start ...."));
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        String[] sepcifiedchk = ParamUtil.getStrings(bpc.request, SPECIFIED_SERVICE_CHECK_BOX_ATTR);
        //no base service
        if(basechks == null){
            String noneerr = "";
            if(sepcifiedchk != null){
                List<String> sepcifiedlist = IaisCommonUtils.genNewArrayList();
                Map<String ,String> baseName = new HashMap<>();
                for (HcsaServiceDto item:baseService
                ) {
                    baseName.put(item.getId(),item.getSvcName());
                }
                for (String item:sepcifiedchk) {
                    sepcifiedlist.add(item);
                }
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
                for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                    if(dto.getSpecifiedSvcId().equals(sepcifiedlist.get(0))){
                        noneerr = baseName.get(dto.getBaseSvcId()) + " should be selected.";
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) sepcifiedlist);
            }else{
                noneerr = "Please select at least one service.";
                ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, sepcifiedchk);
            }
            ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, noneerr);
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.FALSE);
            ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, basechks);
        }else{
            List<String> baselist = IaisCommonUtils.genNewArrayList();
            List<String> sepcifiedlist = IaisCommonUtils.genNewArrayList();
            Map<String ,String> specifiedName = new HashMap<>();
            Map<String ,String> baseName = new HashMap<>();
            for (String item:basechks) {
                baselist.add(item);
            }
            for (HcsaServiceDto item:baseService
            ) {
                baseName.put(item.getId(),item.getSvcName());
            }
            for (HcsaServiceDto item:specifiedService
                 ) {
                specifiedName.put(item.getId(),item.getSvcName());
            }

            if(sepcifiedchk != null){
                List<String> removeBaseList = new ArrayList<>();
                for (String item:sepcifiedchk) {
                    sepcifiedlist.add(item);
                }
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
                Map<String ,String> necessaryBaseServiceList = new HashMap<>();
                for (String item: sepcifiedlist) {
                    for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                        if(dto.getSpecifiedSvcId().equals(item)){
                            necessaryBaseServiceList.put(dto.getBaseSvcId(),dto.getSpecifiedSvcId());
                        }
                    }
                }
                //remove chosed base service
                Iterator<String> iter = necessaryBaseServiceList.keySet().iterator();
                while(iter.hasNext()){
                    String key = iter.next();
                    for (String dto:baselist) {
                        if (dto.equals(key)) {
                            iter.remove();
                        }
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) baselist);
                ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) sepcifiedlist);
                List<String> surplusbaselist = baselist.stream().filter(item -> !removeBaseList.contains(item)).collect(Collectors.toList());
                if(necessaryBaseServiceList.size() > 0){
                    String err = "";
                    if(surplusbaselist.size() == 0){
                        err = baseName.get(getKeyOrNull(necessaryBaseServiceList)) + " should be selected.";
                    }else{
                        err = "The chosen base service ";
                        err = err + baseName.get(surplusbaselist.get(0));
                        err = err + " is not the prerequisite of ";
                        err = err + specifiedName.get(getValueOrNull(necessaryBaseServiceList)) + ".";
                    }
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.FALSE);
                    }else{
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.TRUE);
                }
            }else{
                ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) baselist);
                ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) sepcifiedlist);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, AppConsts.TRUE);
            }
        }
    }

    public void doBeforStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doBeforStart start ...."));

        log.debug(StringUtil.changeForLog("the doBeforStart end ...."));
    }

    private String getValueOrNull(Map<String, String> map) {
        String obj = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }

    private String getKeyOrNull(Map<String, String> map) {
        String obj = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }
}
