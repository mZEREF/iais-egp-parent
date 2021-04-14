package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohHcsaBeDashboard
 *
 * @author Shicheng
 * @date 2021/4/1 13:31
 **/
@Delegator(value = "mohHcsaBeDashboardDelegator")
@Slf4j
public class MohHcsaBeDashboardDelegator {



    /**
     * StartStep: hcsaBeDashboardStart
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardStart start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardInit
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInit start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardInitPre
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInitPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInitPre start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardStep
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardStep start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardApprove(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardApprove start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardSwitchSort
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardSwitchSort(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardSwitchSort start ...."));
        //todo:The current requirements do not need to be sorted
    }

    /**
     * StartStep: hcsaBeDashboardPage
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: hcsaBeDashboardInGroup
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardInGroup(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardInGroup start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardApplicantReply
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardApplicantReply(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardApplicantReply start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardKpi
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardKpi(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardKpi start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardCommonPool
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardCommonPool(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardCommonPool start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardWaitApprove
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardWaitApprove(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardWaitApprove start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardRenewExpiry
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardRenewExpiry(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardRenewExpiry start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardAssignToMe
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardAssignToMe(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardAssignToMe start ...."));
    }

    /**
     * StartStep: hcsaBeDashboardQuery
     *
     * @param bpc
     * @throws
     */
    public void hcsaBeDashboardQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the hcsaBeDashboardQuery start ...."));
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false, null);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew, String className){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "dashSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(className);
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }
}