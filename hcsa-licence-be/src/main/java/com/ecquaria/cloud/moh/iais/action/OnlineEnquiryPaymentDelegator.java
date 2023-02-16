package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.PaymentEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.PaymentQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OnlineEnquiryPaymentDelegator
 *
 * @author junyu
 * @date 2022/12/26
 */
@Delegator(value = "onlineEnquiryPaymentDelegator")
@Slf4j
public class OnlineEnquiryPaymentDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter paymentParameter = new FilterParameter.Builder()
            .clz(PaymentQueryResultsDto.class)
            .searchAttr("paymentParam")
            .resultAttr("paymentResult")
            .sortField("APP_ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private RequestForInformationService requestForInformationService;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    private static final String LICENCE_ID = "licenceId";
    private static final String APP_ID = "appId";


    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.MODULE_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        paymentParameter.setPageSize(pageSize);
        paymentParameter.setPageNo(1);
        paymentParameter.setSortField("APP_ID");
        paymentParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"paymentEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "paymentParam",null);
        ParamUtil.setSessionAttr(bpc.request, "payLicStep",null);
        ParamUtil.setSessionAttr(bpc.request, "payAppStep",null);
        ParamUtil.setSessionAttr(bpc.request, "payAppInsStep",null);
        ParamUtil.setSessionAttr(bpc.request, "licAppMain",null);
        ParamUtil.setSessionAttr(bpc.request, "lisLicTab",null);
        ParamUtil.setSessionAttr(bpc.request, "licAppTab",null);
        ParamUtil.setSessionAttr(bpc.request, "appInsStep",null);
        ParamUtil.setSessionAttr(bpc.request, "licInsStep",null);

    }

    public void preSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "paymentParam");

        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        List<SelectOption> paymentModeOption =getPaymentModeOption();
        ParamUtil.setRequestAttr(request,"paymentModeOption", paymentModeOption);
        List<SelectOption> paymentStatusOption =getPaymentStatusOption();
        ParamUtil.setRequestAttr(request,"paymentStatusOption", paymentStatusOption);

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                paymentParameter.setSortType(sortType);
                paymentParameter.setSortField(sortFieldName);
            }
            PaymentEnquiryFilterDto filterDto=setEnquiryFilterDto(request);

            setQueryFilter(filterDto,paymentParameter);
            if(paymentParameter.getFilters().isEmpty()){
                return;
            }

            SearchParam paymentParam = SearchResultHelper.getSearchParam(request, paymentParameter,true);

            if(searchParam!=null){
                paymentParam.setPageNo(searchParam.getPageNo());
                paymentParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(paymentParam,bpc.request);
            QueryHelp.setMainSql("hcsaOnlineEnquiry","paymentOnlineEnquiry",paymentParam);
            SearchResult<PaymentQueryResultsDto> paymentResult = onlineEnquiriesService.searchPaymentQueryResult(paymentParam);
            ParamUtil.setRequestAttr(request,"paymentResult",paymentResult);
            ParamUtil.setSessionAttr(request,"paymentParam",paymentParam);
        }else {
            SearchResult<PaymentQueryResultsDto> paymentResult = onlineEnquiriesService.searchPaymentQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"paymentResult",paymentResult);
            ParamUtil.setSessionAttr(request,"paymentParam",searchParam);
        }

    }

    private PaymentEnquiryFilterDto setEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        PaymentEnquiryFilterDto filterDto=new PaymentEnquiryFilterDto();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String licenceNo=ParamUtil.getString(request,"licenceNo");
        if(StringUtil.isEmpty(licenceNo)){
            String licNo= (String) ParamUtil.getSessionAttr(request,"payLicNo");
            if(StringUtil.isNotEmpty(licNo)){
                licenceNo=licNo;
                ParamUtil.setSessionAttr(request,"payLicNo",null);
            }
        }
        filterDto.setLicenceNo(licenceNo);
        String applicationNo=ParamUtil.getString(request,"applicationNo");
        if(StringUtil.isEmpty(applicationNo)){
            String appNo= (String) ParamUtil.getSessionAttr(request,"payAppNo");
            if(StringUtil.isNotEmpty(appNo)){
                applicationNo=appNo;
                ParamUtil.setSessionAttr(request,"payAppNo",null);
            }
        }
        filterDto.setApplicationNo(applicationNo);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        String applicationType=ParamUtil.getString(request,"applicationType");
        filterDto.setApplicationType(applicationType);
        String paymentMode=ParamUtil.getString(request,"paymentMode");
        filterDto.setPaymentMode(paymentMode);
        String paymentStatus=ParamUtil.getString(request,"paymentStatus");
        filterDto.setPaymentStatus(paymentStatus);
        String[] serviceNames = ParamUtil.getStrings(request, "serviceName");
        filterDto.setServiceNameList(Arrays.asList(serviceNames));
        Date applicationDateFrom= Formatter.parseDate(ParamUtil.getString(request, "applicationDateFrom"));
        filterDto.setApplicationDateFrom(applicationDateFrom);
        Date applicationDateTo= Formatter.parseDate(ParamUtil.getString(request, "applicationDateTo"));
        filterDto.setApplicationDateTo(applicationDateTo);

        if (!StringUtil.isEmpty(applicationDateFrom) && !StringUtil.isEmpty(applicationDateTo) && applicationDateFrom.after(applicationDateTo)){
            String dateErrMsg = MessageUtil.getMessageDesc("NEW_ERR0039");
            dateErrMsg = dateErrMsg.replace("{from}", "Application Date From");
            dateErrMsg = dateErrMsg.replace("{end}", "Application Date To");
            errorMap.put("inspectionDate", dateErrMsg);
        }
        //        volidata allFileds
        String searchNumber = ParamUtil.getString(request,"Search");
        if (ReflectionUtil.isEmpty(filterDto) && "1".equals(searchNumber)){
            errorMap.put("checkAllFileds", MessageUtil.getMessageDesc("Please enter at least one search filter to proceed with search"));
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        ParamUtil.setSessionAttr(request,"paymentEnquiryFilterDto",filterDto);
        return filterDto;
    }


    private void setQueryFilter(PaymentEnquiryFilterDto filterDto, FilterParameter filterParameter){
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getLicenceNo()!=null) {
            filter.put("getLicenceNo", filterDto.getLicenceNo());
        }
        if(filterDto.getApplicationNo()!=null) {
            filter.put("getApplicationNo", filterDto.getApplicationNo());
        }
        if(filterDto.getBusinessName()!=null){
            filter.put("getBusinessName", filterDto.getBusinessName());
        }
        if(filterDto.getApplicationType()!=null){
            filter.put("getApplicationType", filterDto.getApplicationType());
        }
        if(filterDto.getPaymentMode()!=null){
            filter.put("getPaymentMode", filterDto.getPaymentMode());
        }
        if(filterDto.getPaymentStatus()!=null){
            filter.put("getPaymentStatus", filterDto.getPaymentStatus());
        }
        if(IaisCommonUtils.isNotEmpty(filterDto.getServiceNameList())){
            filter.put("getServiceNameList",filterDto.getServiceNameList());
        }
        if(filterDto.getApplicationDateFrom()!=null){
            String birthDateFrom = Formatter.formatDateTime(filterDto.getApplicationDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("getApplicationDateFrom", birthDateFrom);
        }
        if(filterDto.getApplicationDateTo()!=null){
            String birthDateTo = Formatter.formatDateTime(filterDto.getApplicationDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("getApplicationDateTo", birthDateTo);
        }
        filterParameter.setFilters(filter);

    }

    List<SelectOption> getPaymentModeOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO, "GIRO"));
        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_METHOD_NAME_NETS, "eNETS"));
        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT, "stripe (Credit Card/ Debit Card)"));
        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW, "PayNow"));

        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    List<SelectOption> getPaymentStatusOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL, "GIRO Payment Fail"));
        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS, "GIRO Payment Successful"));
        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS, "Payment Sucessful"));
        selectOptions.add(new SelectOption(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO, "Pending GIRO Payment"));

        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    public void nextStep(BaseProcessClass bpc){

    }

}
