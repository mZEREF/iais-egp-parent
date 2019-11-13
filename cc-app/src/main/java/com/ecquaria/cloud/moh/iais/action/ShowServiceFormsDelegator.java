package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * ShowServiceFormsDelegator
 *
 * @author suocheng
 * @date 9/25/2019
 */
@Delegator("showServiceFormsDelegator")
@Slf4j
public class ShowServiceFormsDelegator {
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
        String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if(StringUtil.isEmpty(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,AppServicesConsts.NAVTABS_SERVICEFORMS);
        }
        String actionTab = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        if(StringUtil.isEmpty(actionTab)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB,getFirstTab(bpc));
        }
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
