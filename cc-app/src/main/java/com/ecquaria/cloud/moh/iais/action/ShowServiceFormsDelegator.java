package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.HcsaServiceDto;
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
        log.debug("the do subStart start ....");

        log.debug("the do subStart end ....");
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc){
        log.debug("the do prepareSwitch start ....");
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if(StringUtil.isEmpty(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"serviceForms");
        }
        String actionTab = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        if(StringUtil.isEmpty(actionTab)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB,getFirstTab(bpc));
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.FORM_TAB,IaisEGPConstant.YES);
        log.debug("the do prepareSwitch end ....");
    }



    /**
     * StartStep: prepareServiceLoad
     *
     * @param bpc
     * @throws
     */
    public void prepareServiceLoad(BaseProcessClass bpc){
        log.debug("the do prepareServiceLoad start ....");
//        String crud_action_type_tab = (String)ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
//        Map<String,String> ServiceFormUrlMaps = (Map<String,String>)ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.SERVICEFORMURLMAPS);
//        String SericeformUrl = ServiceFormUrlMaps.get(crud_action_type_tab)+"?crud_action_type=serviceForms&crud_action_type_tab="+crud_action_type_tab;
//        ParamUtil.setRequestAttr(bpc.request,"SericeformUrl",SericeformUrl);
//        log.info(StringUtil.changeForLog("The SericeformUrl is -->;"+SericeformUrl));
        log.debug("the do prepareServiceLoad end ....");
    }
    /**
     * StartStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc){
        log.debug("the do prepareJump start ....");

        log.debug("the do prepareJump end ....");
    }
    /**
     * StartStep: doServiceformSave
     *
     * @param bpc
     * @throws
     */
    public void doServiceformSave(BaseProcessClass bpc){
        log.debug("the do doServiceformSave start ....");

        log.debug("the do doServiceformSave end ....");
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
