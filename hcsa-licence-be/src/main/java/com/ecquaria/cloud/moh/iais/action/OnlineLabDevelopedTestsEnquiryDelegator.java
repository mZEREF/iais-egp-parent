package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * OnlineLabDevelopedTestsEnquiryDelegator
 *
 * @author junyu
 * @date 2022/1/24
 */
@Delegator(value = "labDevelopedTestsEnquiryDelegator")
@Slf4j
public class OnlineLabDevelopedTestsEnquiryDelegator {

    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter ldtParameter = new FilterParameter.Builder()
            .clz(DsLaboratoryDevelopTestEnquiryResultsDto.class)
            .searchAttr("ldtParam")
            .resultAttr("ldtResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_LDT);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        ldtParameter.setPageSize(pageSize);
        ldtParameter.setPageNo(1);
        ParamUtil.setSessionAttr(bpc.request,"dsLaboratoryDevelopTestEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "ldtParam",null);

    }

    public void perSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        DsLaboratoryDevelopTestEnquiryFilterDto dsFilterDto=new DsLaboratoryDevelopTestEnquiryFilterDto();
        String laboratoryName=ParamUtil.getString(request,"laboratoryName");
        dsFilterDto.setLaboratoryName(laboratoryName);

        Date ldtDateFrom= Formatter.parseDate(ParamUtil.getString(request, "ldtDateFrom"));
        dsFilterDto.setLdtDateFrom(ldtDateFrom);
        Date ldtDateTo= Formatter.parseDate(ParamUtil.getString(request, "ldtDateTo"));
        dsFilterDto.setLdtDateTo(ldtDateTo);
        String ldtTestName=ParamUtil.getString(request,"ldtTestName");
        dsFilterDto.setLdtTestName(ldtTestName);
        String testStatus=ParamUtil.getString(request,"testStatus");
        dsFilterDto.setTestStatus(testStatus);
        String responsePerson=ParamUtil.getString(request,"responsePerson");
        dsFilterDto.setResponsePerson(responsePerson);

        ParamUtil.setSessionAttr(request,"dsLaboratoryDevelopTestEnquiryFilterDto",dsFilterDto);

        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            ldtParameter.setSortType(sortType);
            ldtParameter.setSortField(sortFieldName);
        }
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(dsFilterDto.getLaboratoryName()!=null){
            filter.put("laboratoryName", dsFilterDto.getLaboratoryName());
        }
        if(dsFilterDto.getLdtDateFrom()!=null){
            String ldtDateFromStr = Formatter.formatDateTime(dsFilterDto.getLdtDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("ldtDateFrom", ldtDateFromStr);
        }
        if(dsFilterDto.getLdtDateTo()!=null){
            String ldtDateToStr = Formatter.formatDateTime(dsFilterDto.getLdtDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("ldtDateTo", ldtDateToStr);
        }
        if(dsFilterDto.getLdtTestName()!=null){
            filter.put("ldtTestName", dsFilterDto.getLdtTestName());
        }
        if(dsFilterDto.getTestStatus()!=null){
            if("0".equals(dsFilterDto.getTestStatus())){
                filter.put("testStatus", 0);
            }
            if("1".equals(dsFilterDto.getTestStatus())){
                filter.put("testStatus", 1);
            }
        }
        if(dsFilterDto.getResponsePerson()!=null){
            filter.put("responsePerson", dsFilterDto.getResponsePerson());
        }

        ldtParameter.setFilters(filter);
        SearchParam ldtParam = SearchResultHelper.getSearchParam(request, ldtParameter,true);
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "ldtParam");
        if(searchParam!=null){
            ldtParam.setPageNo(searchParam.getPageNo());
            ldtParam.setPageSize(searchParam.getPageSize());
        }
        CrudHelper.doPaging(ldtParam,bpc.request);

        QueryHelp.setMainSql("onlineEnquiry","searchLaboratoryDevelopTest",ldtParam);

        SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto> ldtResult = assistedReproductionService.searchDsLdtByParam(ldtParam);
        ParamUtil.setRequestAttr(request,"ldtResult",ldtResult);
        ParamUtil.setSessionAttr(request,"ldtParam",ldtParam);
    }

    public void ldtEnquiryStep(BaseProcessClass bpc){

    }




}
