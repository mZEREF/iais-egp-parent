package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
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
    private InboxService inboxService;

    @Autowired
    RequestForChangeService requestForChangeService;

    @Autowired
    AssessmentGuideService assessmentGuideService;

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

        List<HcsaServiceDto> hcsaServiceDtoList = assessmentGuideService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
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

        SearchParam appParam = SearchResultHelper.getSearchParam(bpc.request,appParameter,true);
        appParam.addFilter("licenseeId", licenseeId,true);
        appParam.addFilter("appStatus", ApplicationConsts.APPLICATION_STATUS_DRAFT,true);

        QueryHelp.setMainSql("assessmentSchematicsQuery","applicationQuery", appParam);
        SearchResult<InboxAppQueryDto> appResult = inboxService.appDoQuery(appParam);

        if(!StringUtil.isEmpty(appResult)){
            ParamUtil.setSessionAttr(bpc.request,"appParam", appParam);
            ParamUtil.setRequestAttr(bpc.request,"appResult", appResult);
        }
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, premiseFilterParameter, true);
        searchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("applicationPersonnelQuery", "queryPremises", searchParam);
        SearchResult<PremisesListQueryDto> searchResult = requestForChangeService.searchPreInfo(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "PremisesSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PremisesSearchResult", searchResult);
        }

    }

    public void page(BaseProcessClass bpc){
        SearchResultHelper.doPage(bpc.request, premiseFilterParameter);
        SearchResultHelper.doPage(bpc.request, appParameter);
    }

    public void sort(BaseProcessClass bpc){
        SearchResultHelper.doSort(bpc.request, premiseFilterParameter);
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
        String[] renewLics = ParamUtil.getStrings(bpc.request,"renewLicenId");
        if(renewLics != null){
            List<String> licIdValue = IaisCommonUtils.genNewArrayList();
            for(String item:renewLics){
                licIdValue.add(ParamUtil.getMaskedString(bpc.request,item));
            }
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licIdValue);
        }
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
