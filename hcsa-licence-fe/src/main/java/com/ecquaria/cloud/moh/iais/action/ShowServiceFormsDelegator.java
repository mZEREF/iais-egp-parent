package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ShowServiceFormsDelegator
 *
 * @author suocheng
 * @date 9/25/2019
 */
@Delegator("showServiceFormsDelegator")
@Slf4j
public class ShowServiceFormsDelegator {
    public  static final String SERVICESTEPDTO = "serviceStepDto";

    @Autowired
    ServiceConfigService serviceConfigService;

    /**
     * StartStep: SubStart
     *
     * @param bpc
     * @throws
     */
    public void subStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do subStart start ...."));

        log.debug(StringUtil.changeForLog("the do subStart end ...."));
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareSwitch start ...."));
        String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,AppServicesConsts.NAVTABS_SERVICEFORMS);
        }
        String actionTab = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        if(StringUtil.isEmpty(actionTab)){
            actionTab = getFirstTab(bpc);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB,getFirstTab(bpc));
        }

        //why call api?  , can get from  AppServicesConsts.HCSASERVICEDTOLIST
        String svcId = serviceConfigService.getSvcIdBySvcCode(actionTab);

        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
        ServiceStepDto serviceStepDto = new ServiceStepDto();
        serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);

        List<HcsaSvcPersonnelDto>  currentSvcAllPsnConfig= serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, svcId);
        Map<String, List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP);
        if(svcAllPsnConfig == null){
            svcAllPsnConfig = IaisCommonUtils.genNewHashMap();
        }
        svcAllPsnConfig.put(svcId, currentSvcAllPsnConfig);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP, (Serializable) svcAllPsnConfig);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID, svcId);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE, actionTab);
        ParamUtil.setSessionAttr(bpc.request, SERVICESTEPDTO, (Serializable) serviceStepDto);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.FORM_TAB,IaisEGPConstant.YES);
        log.debug(StringUtil.changeForLog("the do prepareSwitch end ...."));
    }



    /**
     * StartStep: prepareServiceLoad
     *
     * @param bpc
     * @throws
     */
    public void prepareServiceLoad(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareServiceLoad start ...."));

        log.debug(StringUtil.changeForLog("the do prepareServiceLoad end ...."));
    }
    /**
     * StartStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareJump start ...."));

        log.debug(StringUtil.changeForLog("the do prepareJump end ...."));
    }
    /**
     * StartStep: doServiceformSave
     *
     * @param bpc
     * @throws
     */
    public void doServiceformSave(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doServiceformSave start ...."));

        log.debug(StringUtil.changeForLog("the do doServiceformSave end ...."));
    }

    //=============================================================================
    //private method
    //=============================================================================
    private String getFirstTab(BaseProcessClass bpc){
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto> )ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
        HcsaServiceDto hcsaServiceDto= hcsaServiceDtoList.get(0);
        return hcsaServiceDto.getSvcCode();
    }


}
