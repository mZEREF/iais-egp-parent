package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AssessmentSchematicsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SelfAssessmentSchematicsDelegator
 *
 * @author junyu
 * @date 2020/6/8
 */
@Delegator(value = "selfAssessmentSchematicsDelegator")
@Slf4j
public class SelfAssessmentSchematicsDelegator {

    private static final String BASE_SERVICE_ATTR = "baseService";
    private static final String SPECIFIED_SERVICE_ATTR = "specifiedService";
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";

    List<HcsaServiceDto> allbaseService;
    List<HcsaServiceDto> allspecifiedService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    AssessmentSchematicsClient assessmentSchematicsClient;


    private final FilterParameter premiseFilterParameter = new FilterParameter.Builder()
            .clz(PremisesListQueryDto.class)
            .searchAttr("PremisesSearchParam")
            .resultAttr("PremisesSearchResult")
            .sortField("PREMISES_TYPE").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(10).build();
    private FilterParameter appParameter = new FilterParameter.Builder()
            .clz(InboxAppQueryDto.class)
            .searchAttr("appParam")
            .resultAttr("appResult")
            .sortField("CREATED_DT")
            .sortType("DESC").build();
    public void start(BaseProcessClass bpc){
        log.info("****   start ******");

        log.info("****   end ******");
    }


    public void perDate(BaseProcessClass bpc){

        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }

        allbaseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());


        ParamUtil.setRequestAttr(bpc.request, BASE_SERVICE_ATTR, allbaseService);
        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR, allspecifiedService);


        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchResultHelper.doPage(bpc.request, premiseFilterParameter);
        SearchResultHelper.doSort(bpc.request, premiseFilterParameter);
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, premiseFilterParameter, true);
        searchParam.addFilter("licenseeId", licenseeId, true);

        QueryHelp.setMainSql("applicationPersonnelQuery", "queryPremises", searchParam);
        SearchResult<PremisesListQueryDto> searchResult = requestForChangeService.searchPreInfo(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "PremisesSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PremisesSearchResult", searchResult);
        }


        SearchParam appParam = SearchResultHelper.getSearchParam(bpc.request,appParameter,true);
        appParam.addFilter("licenseeId", licenseeId,true);
        appParam.addFilter("appStatus", ApplicationConsts.APPLICATION_STATUS_DRAFT,true);

        QueryHelp.setMainSql("assessmentSchematicsQuery","applicationQuery", appParam);
        SearchResult<InboxAppQueryDto> appResult = assessmentSchematicsClient.searchResultFromApp(appParam).getEntity();
        List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
        for (InboxAppQueryDto inboxAppQueryDto:inboxAppQueryDtoList) {
            if (ApplicationConsts.APPLICATION_STATUS_DRAFT.equals(inboxAppQueryDto.getStatus())){
                ApplicationDraftDto applicationDraftDto = assessmentSchematicsClient.getDraftInfo(inboxAppQueryDto.getId()).getEntity();
                String draftServiceCode = applicationDraftDto.getServiceCode();
                if (!draftServiceCode.isEmpty()){
                    String[] serviceName = draftServiceCode.split("@");
                    StringBuilder draftServiceName = new StringBuilder();
                    for (int i=0;i<serviceName.length;i++){
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(serviceName[i]);
                        if (hcsaServiceDto != null){
                            if (i>0){
                                draftServiceName.append("<br/>")
                                        .append(hcsaServiceDto.getSvcName());
                            }else{
                                draftServiceName.append(hcsaServiceDto.getSvcName());
                            }
                        }
                    }
                    inboxAppQueryDto.setServiceId(draftServiceName.toString());
                }else{
                    inboxAppQueryDto.setServiceId("N/A");
                }
            }
        }
        if(!StringUtil.isEmpty(appResult)){
            ParamUtil.setSessionAttr(bpc.request,"appParam", appParam);
            ParamUtil.setRequestAttr(bpc.request,"appResult", appResult);
        }

    }

    public void doSearch(BaseProcessClass bpc){

    }
    public void newApp1(BaseProcessClass bpc){

    }
    public void newApp2(BaseProcessClass bpc){

    }
    public void newApp3(BaseProcessClass bpc){

    }
    public void renewLic(BaseProcessClass bpc){

    }
    public void renewLicUpdate(BaseProcessClass bpc){

    }
    public void amendLic1_1(BaseProcessClass bpc){

    }
    public void amendLic1_2(BaseProcessClass bpc){

    }
    public void amendLic2(BaseProcessClass bpc){

    }
    public void amendLic3_1(BaseProcessClass bpc){

    }
    public void amendLic3_2(BaseProcessClass bpc){

    }

    public void amendLic4_1(BaseProcessClass bpc){

    }
    public void amendLic4_2(BaseProcessClass bpc){

    }
    public void ceaseLic(BaseProcessClass bpc){

    }
    public void withdrawApp(BaseProcessClass bpc){

    }
    public void resumeDraftApp(BaseProcessClass bpc){

    }
    public void subDateMoh(BaseProcessClass bpc){

    }
    public void updateAdminPers(BaseProcessClass bpc){

    }

}
