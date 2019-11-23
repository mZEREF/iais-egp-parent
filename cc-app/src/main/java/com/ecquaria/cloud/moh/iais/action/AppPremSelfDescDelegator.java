package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:11/20/2019 10:12 AM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclRenderDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDescService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Delegator(value = "appPremSelfDescDelegator")
@Slf4j
public class AppPremSelfDescDelegator {

    private final AppPremSelfDescService appPremSelfDesc;
    private final FilterParameter filterParameter;

    private SearchResult<SelfDeclRenderDto> tabResult = null;
    private SearchResult<SelfDeclRenderDto> renderResult = null;

    private Boolean firstEntry = false;

    @Autowired
    public AppPremSelfDescDelegator(AppPremSelfDescService appPremSelfDesc, FilterParameter filterParameter){
        this.appPremSelfDesc = appPremSelfDesc;
        this.filterParameter = filterParameter;
    }

    /**
     * AutoStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Hcsa Application", "Self desc");
        ParamUtil.setSessionAttr(request, "tabResultAttr", null);
        this.firstEntry = false;
    }

    private void prepareTab(HttpServletRequest request){
        String groupId = ParamUtil.getString(request, "GROUP_ID");
        HashSet<String> svcCodeList = new HashSet<>();
        List<HcsaServiceDto> hcsaServiceDtos = appPremSelfDesc.listHcsaService();
        List<ApplicationDto> appList  = appPremSelfDesc.listApplicationByGroupId(groupId);

        List<String> appGrpPremIdList = new ArrayList<>();
        for (ApplicationDto app : appList){
            List<AppPremisesCorrelationDto> correlationDtos = appPremSelfDesc.listAppPremisesCorrelationByAppId(app.getId());
            for (AppPremisesCorrelationDto correlation : correlationDtos){
                appGrpPremIdList.add(correlation.getAppGrpPremId());
            }
        }

        ParamUtil.setSessionAttr(request, "appGrpPremIdList", (Serializable) appGrpPremIdList);

        // add svc code to list
        for(HcsaServiceDto hcsa : hcsaServiceDtos){
            for (ApplicationDto app : appList){
                if (hcsa.getId().equals(app.getServiceId())){
                    String svcCode = hcsa.getSvcCode();
                    svcCodeList.add(svcCode);
                }
            }
        }

        filterParameter.setClz(SelfDeclRenderDto.class);
        filterParameter.setSortField("common");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        searchParam.setSort(filterParameter.getSortField(), SearchParam.DESCENDING);
        searchParam.addFilter("svc_type", "Self-Assessment", true);

        for (String svcCode : svcCodeList){
            searchParam.addFilter("svc_code", svcCode, true);
        }

        QueryHelp.setMainSql("applicationQuery", "listSelfDesc", searchParam);
        this.tabResult = appPremSelfDesc.listSelfDescConfig(searchParam);

        ParamUtil.setSessionAttr(request, "tabResultAttr", tabResult);
        this.firstEntry = true;
    }

    private void prepareSearchResult(HttpServletRequest request, String configId){
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (configId == null){
            searchParam.addFilter("svc_type", "Self-Assessment", true);
            searchParam.addFilter("common", 1, true);
            QueryHelp.setMainSql("applicationQuery", "listSelfDesc", searchParam);
            renderResult = appPremSelfDesc.listSelfDescConfig(searchParam);
        }else {
            searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
            searchParam.addFilter("svc_type", "Self-Assessment", true);
            searchParam.addFilter("configId", configId, true);
            QueryHelp.setMainSql("applicationQuery", "listSelfDesc", searchParam);
            renderResult = appPremSelfDesc.listSelfDescConfig(searchParam);
        }
        ParamUtil.setRequestAttr(request, "renderResultAttr", renderResult);
    }


    /**
     * AutoStep: initData
     *
     * @param bpc
     * @throws
     */
    public void initData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        prepareTab(request);

        String pageIndex = ParamUtil.getString(request, "pageIndex");

        prepareSearchResult(request, pageIndex);

    }

    /**
     * AutoStep: initCommonData
     *
     * @param bpc
     * @throws
     */
    public void initCommonData(BaseProcessClass bpc){

    }

    /**
     * AutoStep: initServiceData
     *
     * @param bpc
     * @throws
     */
    public void initServiceData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }


    /**
     * AutoStep: saveServiceData
     *
     * @param bpc
     * @throws
     */
    public void saveServiceData(BaseProcessClass bpc){

    }

    /**
     * AutoStep: saveCommonData
     *
     * @param bpc
     * @throws
     */
    public void saveCommonData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;


        SearchResult<SelfDeclRenderDto> searchResul = (SearchResult<SelfDeclRenderDto>) ParamUtil.getRequestAttr(request, "renderResultAttr");

       /* List<SelfDeclRenderDto> rows = searchResul.getRows();

        if (rows != null && rows.size() > 0){
            for (SelfDeclRenderDto s : rows){
                String itemAnswer = ParamUtil.getString(request, s.getItemId());



                SelfDeclSubmitDto SelfDeclSubmit = new SelfDeclSubmitDto();

            }
        }*/





    }


    /**
     * AutoStep: validate
     *
     * @param bpc
     * @throws
     */
    public void validate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(request, "crud_action_type");

        ParamUtil.setRequestAttr(bpc.request,"crud_action_type", action);
    }

    /**
     * AutoStep: switchNextStep
     *
     * @param bpc
     * @throws
     */
    public void switchNextStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(request, "crud_action_type");

        ParamUtil.setRequestAttr(bpc.request,"crud_action_type", action);
    }
}
