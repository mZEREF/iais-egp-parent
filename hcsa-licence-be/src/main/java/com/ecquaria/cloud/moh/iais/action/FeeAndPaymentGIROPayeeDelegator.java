package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * FeeAndPaymentGIROPayeeDelegator
 *
 * @author junyu
 * @date 2021/3/2
 */
@Delegator(value = "feeAndPaymentGIROPayeeDelegator")
@Slf4j
public class FeeAndPaymentGIROPayeeDelegator {
    @Autowired
    private GiroAccountService giroAccountService;
    @Autowired
    private InsepctionNcCheckListService serviceConfigService;
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter giroAccountParameter = new FilterParameter.Builder()
            .clz(GiroAccountInfoQueryDto.class)
            .searchAttr("giroAcctSearch")
            .resultAttr("giroAcctResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter orgPremParameter = new FilterParameter.Builder()
            .clz(OrganizationPremisesViewQueryDto.class)
            .searchAttr("orgPremParam")
            .resultAttr("orgPremResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();
    @Autowired
    SystemParamConfig systemParamConfig;


    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_GIRO_ACCOUNT_MANAGEMENT,  AuditTrailConsts.MODULE_GIRO_ACCOUNT_MANAGEMENT);
    }
    public void info(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,"cusRefNo",null);
        ParamUtil.setSessionAttr(request,"hciCode",null);
        ParamUtil.setSessionAttr(request,"hciName",null);
        ParamUtil.setSessionAttr(request,"uenNo",null);
        ParamUtil.setSessionAttr(request,"hciSession",null);

        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        orgPremParameter.setPageSize(pageSize);
        giroAccountParameter.setPageSize(pageSize);
    }
    public void prePayeeResult(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String hciCode= ParamUtil.getString(request,"hciCode");
        String cusRefNo =ParamUtil.getString(request,"cusRefNo");
        ParamUtil.setSessionAttr(request,"hciCode",hciCode);
        ParamUtil.setSessionAttr(request,"cusRefNo",cusRefNo);

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(hciCode!=null) {
            filter.put("hciCode", hciCode);
        }
        if(cusRefNo!=null) {
            filter.put("cusRefNo", cusRefNo);
        }
        giroAccountParameter.setFilters(filter);
        SearchParam giroAccountParam = SearchResultHelper.getSearchParam(request, giroAccountParameter,true);
        CrudHelper.doPaging(giroAccountParam,bpc.request);
        QueryHelp.setMainSql("giroPayee","searchByGiroAcctInfo",giroAccountParam);
        SearchResult<GiroAccountInfoQueryDto> giroAccountResult = giroAccountService.searchGiroInfoByParam(giroAccountParam);
        if(giroAccountResult.getRowCount()!=0){
            SearchResult<GiroAccountInfoViewDto> searchGiroDtoResult=new SearchResult<>();
            searchGiroDtoResult.setRowCount(giroAccountResult.getRowCount());
            List<GiroAccountInfoViewDto> giroAccountInfoViewDtos=IaisCommonUtils.genNewArrayList();
            for (GiroAccountInfoQueryDto gai:
            giroAccountResult.getRows()) {
                GiroAccountInfoViewDto giroAccountInfoViewDto=new GiroAccountInfoViewDto();
                List<GiroAccountFormDocDto> giroAccountFormDocDtoList=giroAccountService.findGiroAccountFormDocDtoListByAcctId(gai.getId());
                giroAccountInfoViewDto.setAcctName(gai.getAcctName());
                giroAccountInfoViewDto.setAcctNo(gai.getAcctNo());
                giroAccountInfoViewDto.setBankCode(gai.getBankCode());
                giroAccountInfoViewDto.setGiroAccountFormDocDtoList(giroAccountFormDocDtoList);
                giroAccountInfoViewDto.setBankName(gai.getBankName());
                giroAccountInfoViewDto.setBranchCode(gai.getBranchCode());
                giroAccountInfoViewDto.setHciCode(gai.getHciCode());
                giroAccountInfoViewDto.setHciName(gai.getHciName());
                giroAccountInfoViewDto.setId(gai.getId());
                giroAccountInfoViewDto.setCustomerReferenceNo(gai.getCustomerReferenceNo());
                giroAccountInfoViewDtos.add(giroAccountInfoViewDto);
            }
            searchGiroDtoResult.setRows(giroAccountInfoViewDtos);
            ParamUtil.setRequestAttr(request,"searchGiroAccountParam",giroAccountParam);
            ParamUtil.setRequestAttr(request,"searchGiroDtoResult",searchGiroDtoResult);

        }
    }
    public void deletePayee(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        String [] acctIds=ParamUtil.getStrings(request,"acctIds");
        List<GiroAccountInfoDto> giroAccountInfoDtoList=IaisCommonUtils.genNewArrayList();
        String refNo=System.currentTimeMillis()+"";
        for (String acctId:acctIds
             ) {
            GiroAccountInfoDto giroAccountInfoDto=giroAccountService.findGiroAccountInfoDtoByAcctId(acctId);
            giroAccountInfoDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            giroAccountInfoDto.setEventRefNo(refNo);

            giroAccountInfoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            giroAccountInfoDtoList.add(giroAccountInfoDto);
        }
        giroAccountService.updateGiroAccountInfo(giroAccountInfoDtoList);

        eicSyncGiroAcctToFe(refNo, giroAccountInfoDtoList);

    }
    public void reSearchPayee(BaseProcessClass bpc) {

    }
    public void preOrgResult(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"acctName",null);
        ParamUtil.setSessionAttr(request,"bankCode",null);
        ParamUtil.setSessionAttr(request,"branchCode",null);
        ParamUtil.setSessionAttr(request,"bankName",null);
        ParamUtil.setSessionAttr(request,"bankAccountNo",null);
        ParamUtil.setSessionAttr(request,"cusRefNo",null);
        ParamUtil.setSessionAttr(request,"docDto",null);
        ParamUtil.setSessionAttr(request,"giroAccountInfoDtoList", null);
        String hciCode= ParamUtil.getString(request,"hciCode");
        String hciName =ParamUtil.getString(request,"hciName");
        String uenNo =ParamUtil.getString(request,"uenNo");
        ParamUtil.setSessionAttr(request,"hciCode",hciCode);
        ParamUtil.setSessionAttr(request,"hciName",hciName);
        ParamUtil.setSessionAttr(request,"uenNo",uenNo);

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(hciCode!=null) {
            filter.put("hciCode", hciCode);
        }
        if(hciName!=null) {
            filter.put("hciName", hciName);
        }
        if(uenNo!=null) {
            filter.put("uenNo", uenNo);
        }
        orgPremParameter.setFilters(filter);
        SearchParam orgPremParam = SearchResultHelper.getSearchParam(request, orgPremParameter,true);
        CrudHelper.doPaging(orgPremParam,bpc.request);
        QueryHelp.setMainSql("giroPayee","searchByOrgPremView",orgPremParam);
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
        ParamUtil.setRequestAttr(request,"orgPremParam",orgPremParam);
        ParamUtil.setRequestAttr(request,"orgPremResult",orgPremResult);
    }
    public void reSearchOrg(BaseProcessClass bpc) {

    }
    public void doSelect(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult= (SearchResult<OrganizationPremisesViewQueryDto>) ParamUtil.getSessionAttr(request,"hciSession");
        if(orgPremResult==null){
            String [] orgPerIds=ParamUtil.getStrings(request,"opIds");
            SearchParam orgPremParam = SearchResultHelper.getSearchParam(request, orgPremParameter,true);
            String typeStr = SqlHelper.constructInCondition("opv.OP_ID",orgPerIds.length);
            int indx = 0;
            for (String s : orgPerIds){
                orgPremParam.addFilter("opv.OP_ID"+indx, s);
                indx++;
            }
            orgPremParam.addParam("orgPremIds",typeStr);

            CrudHelper.doPaging(orgPremParam,bpc.request);
            QueryHelp.setMainSql("giroPayee","searchByOrgPremView",orgPremParam);
             orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
            ParamUtil.setSessionAttr(request,"hciSession",orgPremResult);
        }

    }
    public void doBack(BaseProcessClass bpc) {

    }
    public void refill(BaseProcessClass bpc) {

    }
    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        String acctName= ParamUtil.getString(request,"acctName");
        String bankCode =ParamUtil.getString(request,"bankCode");
        String branchCode =ParamUtil.getString(request,"branchCode");
        String bankName= ParamUtil.getString(request,"bankName");
        String bankAccountNo =ParamUtil.getString(request,"bankAccountNo");
        String cusRefNo =ParamUtil.getString(request,"cusRefNo");
        ParamUtil.setSessionAttr(request,"acctName",acctName);
        ParamUtil.setSessionAttr(request,"bankCode",bankCode);
        ParamUtil.setSessionAttr(request,"branchCode",branchCode);
        ParamUtil.setSessionAttr(request,"bankName",bankName);
        ParamUtil.setSessionAttr(request,"bankAccountNo",bankAccountNo);
        ParamUtil.setSessionAttr(request,"cusRefNo",cusRefNo);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(acctName)){
            errorMap.put("acctName", MessageUtil.replaceMessage("GENERAL_ERR0006","acctName","field"));
        }
        if(StringUtil.isEmpty(bankCode)){
            errorMap.put("bankCode", MessageUtil.replaceMessage("GENERAL_ERR0006","bankCode","field"));
        }
        if(StringUtil.isEmpty(branchCode)){
            errorMap.put("branchCode", MessageUtil.replaceMessage("GENERAL_ERR0006","branchCode","field"));
        }
        if(StringUtil.isEmpty(bankName)){
            errorMap.put("bankName", MessageUtil.replaceMessage("GENERAL_ERR0006","bankName","field"));
        }
        if(StringUtil.isEmpty(bankAccountNo)){
            errorMap.put("bankAccountNo", MessageUtil.replaceMessage("GENERAL_ERR0006","bankAccountNo","field"));
        }
        if(StringUtil.isEmpty(cusRefNo)){
            errorMap.put("cusRefNo", MessageUtil.replaceMessage("GENERAL_ERR0006","cusRefNo","field"));
        }
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile file= (CommonsMultipartFile) mulReq.getFile( "UploadFile");
        String commDelFlag = ParamUtil.getString(mulReq, "commDelFlag");
        GiroAccountFormDocDto doc= (GiroAccountFormDocDto) ParamUtil.getSessionAttr(request,"docDto");
        if(doc==null){
            doc=new GiroAccountFormDocDto();
        }
        if(file != null && file.getSize() != 0&&!StringUtil.isEmpty(file.getOriginalFilename())){
            file.getFileItem().setFieldName("selectedFile");
            long size = file.getSize() / 1024;
            doc.setDocName(file.getOriginalFilename());
            doc.setDocSize(Integer.valueOf(String.valueOf(size)));
            String fileRepoGuid = serviceConfigService.saveFiles(file);
            doc.setFileRepoId(fileRepoGuid);
            doc.setPassDocValidate(false);
        }else if("N".equals(commDelFlag)){
            doc.setDocName(null);
            doc.setDocSize(null);
            doc.setFileRepoId(null);
            doc.setPassDocValidate(false);
        }
        doc.setPassDocValidate(true);
        String errDocument=MessageUtil.replaceMessage("GENERAL_ERR0006","Supporting Documents","field");
        String commValidFlag = ParamUtil.getString(mulReq, "commValidFlag");
        List<String> fileTypes = Arrays.asList(systemParamConfig.getUploadFileType().split(","));
        Long fileSize=(systemParamConfig.getUploadFileLimit() * 1024 *1024L);
        if(("N".equals(commValidFlag)||doc.getDocSize()==null)){

            if(file == null || file.getSize() == 0){
                doc.setPassDocValidate(false);
                errorMap.put("UploadFile",errDocument);
            }else{
                Map<String, Boolean> booleanMap = ValidationUtils.validateFile(file,fileTypes,fileSize);
                //name size
                int fileNameLen= Objects.requireNonNull(file.getOriginalFilename()).length();
                if(fileNameLen>100){
                    doc.setPassDocValidate(false);
                    errorMap.put("UploadFile", MessageUtil.getMessageDesc("GENERAL_ERR0022"));
                }
                //file size
                if(!booleanMap.get("fileSize")){
                    doc.setPassDocValidate(false);
                    errorMap.put("UploadFile", MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(systemParamConfig.getUploadFileLimit()),"sizeMax"));
                }
                //type
                if(!booleanMap.get("fileType")){
                    doc.setPassDocValidate(false);
                    errorMap.put("UploadFile",MessageUtil.replaceMessage("GENERAL_ERR0018", systemParamConfig.getUploadFileType(),"fileType"));
                }
            }
        }
        if(("Y".equals(commValidFlag)&&doc.getDocSize()!=null)||(file == null || file.getSize() == 0)&& "Y".equals(commDelFlag)){
            Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
            if (doc.getDocSize() != null) {
                long size = doc.getDocSize();
                String filename = doc.getDocName();
                String fileType = filename.substring(filename.lastIndexOf(46) + 1);
                String s = fileType.toUpperCase();
                if (!fileTypes.contains(s)) {
                    map.put("fileType", Boolean.FALSE);
                } else {
                    map.put("fileType", Boolean.TRUE);
                }

                if (size > fileSize) {
                    map.put("fileSize", Boolean.FALSE);
                } else {
                    map.put("fileSize", Boolean.TRUE);
                }
                //size
                if(!map.get("fileSize")){
                    doc.setPassDocValidate(false);
                    errorMap.put("UploadFile", MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(systemParamConfig.getUploadFileLimit()),"sizeMax"));
                }
                //type
                if(!map.get("fileType")){
                    doc.setPassDocValidate(false);
                    errorMap.put("UploadFile",MessageUtil.replaceMessage("GENERAL_ERR0018", systemParamConfig.getUploadFileType(),"fileType"));
                }
                if(filename.length()>100){
                    doc.setPassDocValidate(false);
                    errorMap.put("UploadFile", MessageUtil.getMessageDesc("GENERAL_ERR0022"));
                }
            }
        }





        ParamUtil.setSessionAttr(request,"docDto",doc);

        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");

        }

        List<GiroAccountFormDocDto> giroAccountFormDocDtoList=IaisCommonUtils.genNewArrayList();
        giroAccountFormDocDtoList.add(doc);

        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult= (SearchResult<OrganizationPremisesViewQueryDto>) ParamUtil.getSessionAttr(request,"hciSession");
        List<GiroAccountInfoDto> giroAccountInfoDtoList=IaisCommonUtils.genNewArrayList();
        for (OrganizationPremisesViewQueryDto opv:orgPremResult.getRows()
        ) {
            GiroAccountInfoDto giroAccountInfoDto=new GiroAccountInfoDto();
            giroAccountInfoDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            giroAccountInfoDto.setAcctName(acctName);
            giroAccountInfoDto.setAcctNo(bankAccountNo);
            giroAccountInfoDto.setBranchCode(branchCode);
            giroAccountInfoDto.setCustomerReferenceNo(cusRefNo);
            giroAccountInfoDto.setBankCode(bankCode);
            giroAccountInfoDto.setBankName(bankName);
            giroAccountInfoDto.setOrganizationId(opv.getOrgId());
            giroAccountInfoDto.setHciName(opv.getHciName());
            giroAccountInfoDto.setHciCode(opv.getHciCode());
            giroAccountInfoDto.setGiroAccountFormDocDtoList(giroAccountFormDocDtoList);
            giroAccountInfoDtoList.add(giroAccountInfoDto);
        }
        ParamUtil.setSessionAttr(request,"giroAccountInfoDtoList", (Serializable) giroAccountInfoDtoList);


    }
    

    public void preView(BaseProcessClass bpc) {

    }
    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        String refNo=System.currentTimeMillis()+"";
        List<GiroAccountInfoDto> giroAccountInfoDtoList= (List<GiroAccountInfoDto>) ParamUtil.getSessionAttr(request,"giroAccountInfoDtoList");
        for (GiroAccountInfoDto giro:giroAccountInfoDtoList
             ) {
            giro.setEventRefNo(refNo);
            giro.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<GiroAccountInfoDto> giroAccountInfoDtoList1= giroAccountService.createGiroAccountInfo(giroAccountInfoDtoList);


        eicSyncGiroAcctToFe(refNo, giroAccountInfoDtoList1);
    }

    private void eicSyncGiroAcctToFe(String refNo, List<GiroAccountInfoDto> giroAccountInfoDtoList1) {
        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.GiroAccountServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallFeGiroLic");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-intranet");
        eicRequestTrackingDto.setDtoClsName(List.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(giroAccountInfoDtoList1));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(refNo);
        giroAccountService.updateGiroAccountInfoTrackingDto(eicRequestTrackingDto);
        giroAccountService.syncFeGiroAcctDto(giroAccountInfoDtoList1);
    }
}
