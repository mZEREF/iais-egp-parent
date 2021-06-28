package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2021/6/28 14:19
 **/
@Delegator(value = "mohBeStatisticsBoardDelegator")
@Slf4j
public class MohBeStatisticsBoardDelegator {

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    /**
     * StartStep: beStatisticsBoardStart
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, "dashActionValue", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
        String backFlag = ParamUtil.getRequestString(bpc.request, "dashProcessBack");
        if(!AppConsts.YES.equals(backFlag)) {
            ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", null);
            ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", null);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", null);
            ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", null);
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
            ParamUtil.setSessionAttr(bpc.request, "beDashRoleIds", null);
            ParamUtil.setSessionAttr(bpc.request, "dashRoleCheckDto", null);
            ParamUtil.setSessionAttr(bpc.request, "dashAppStatus", null);
            ParamUtil.setSessionAttr(bpc.request, "application_status", null);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "dashProcessBackFlag", backFlag);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspecTaskCreAndAssDto", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAsoCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashPsoCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashPreInspCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashInspCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashPostInspCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAo1CircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAo2CircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAo3CircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashOverAllCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashSvcCheckList", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAppTypeCheckList", null);
    }

    /**
     * StartStep: beStatisticsBoardInit
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardInit start ...."));

    }

    /**
     * StartStep: beStatisticsBoardPre
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardPre start ...."));

    }

    /**
     * StartStep: beStatisticsBoardStep
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardStep start ...."));

    }

    /**
     * StartStep: beStatisticsBoardSearch
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardSearch(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardSearch start ...."));

    }

    /**
     * StartStep: beStatisticsBoardPage
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardPage start ...."));

    }

    /**
     * StartStep: beStatisticsBoardSort
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardSort(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardSort start ...."));

    }

    /**
     * StartStep: beStatisticsBoardQuery
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardQuery start ...."));

    }

    /**
     * StartStep: beStatisticsBoardDetail
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardDetail(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardDetail start ...."));

    }
}
