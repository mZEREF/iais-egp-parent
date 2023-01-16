package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private static final String ORG_PREM_PARAM = "orgPremParam";
    private static final String ORG_PREM_RESULT = "orgPremResult";
    private static final String LICENSEE = "licensee";
    private static final String UEN_NO = "uenNo";
    private static final String GIRO_ACCT_FILE_DTO = "giroAcctFileDto";
    private static final String LICENCE_NO_SER = "licenceNoSer";
    private static final String LICENSEE_NAME = "licenseeName";
    private static final String HCI_SESSION = "hciSession";
    private static final String CRUD_ACTION_VALUE = "crud_action_value";
    private static final String CRUD_ACTION_ADDITIONAL = "crud_action_additional";
    private static final String CRUD_ACTION_TYPE = "crud_action_type";
    private static final String GIRO_PAYEE = "giroPayee";
    private static final String ACCT_NAME = "acctName";
    private static final String BANK_CODE = "bankCode";
    private static final String BRANCH_CODE = "branchCode";
    private static final String BANK_NAME = "bankName";
    private static final String BANK_ACCOUNT_NO = "bankAccountNo";
    private static final String CUS_REF_NO = "cusRefNo";
    private static final String REMARKS = "remarks";
    private static final String GIRO_ACCOUNTINFO_DTO_LIST = "giroAccountInfoDtoList";
    private static final String SEARCH_BY_ORG_PREM_VIEW = "searchByOrgPremView";
    private static final String OPV_ID = "opv.ID";
    private static final String FEE_FIELD = "field";
    private static final String FEE_NUMBER = "number";
    private static final String FEE_FIELD_NO = "fieldNo";
    private static final String FEE_ERROR_USER = "USER_ERR003";
    private static final String FEE_FILE_TYPE = "fileType";
    private static final String FEE_FILE_SIZE = "fileSize";
    private static final String FEE_UPLOAD_FILE = "UploadFile";


    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter giroAccountParameter = new FilterParameter.Builder()
            .clz(GiroAccountInfoQueryDto.class)
            .searchAttr("giroAcctSearch")
            .resultAttr("giroAcctResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter orgPremParameter = new FilterParameter.Builder()
            .clz(OrganizationPremisesViewQueryDto.class)
            .searchAttr(ORG_PREM_PARAM)
            .resultAttr(ORG_PREM_RESULT)
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();
    @Autowired
    private SystemParamConfig systemParamConfig;


    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_GIRO_ACCOUNT_MANAGEMENT,  AuditTrailConsts.MODULE_GIRO_ACCOUNT_MANAGEMENT);
        log.info(StringUtil.changeForLog("start-->"));
    }
    public void info(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,LICENSEE,null);
        ParamUtil.setSessionAttr(request,"uen",null);
        ParamUtil.setSessionAttr(request,"licenceNo",null);
        ParamUtil.setSessionAttr(request,UEN_NO,null);
        ParamUtil.setSessionAttr(request,GIRO_ACCT_FILE_DTO,null);
        ParamUtil.setSessionAttr(request,LICENCE_NO_SER,null);
        ParamUtil.setSessionAttr(request,LICENSEE_NAME,null);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        orgPremParameter.setPageSize(pageSize);
        orgPremParameter.setPageNo(1);
        giroAccountParameter.setPageNo(1);
        giroAccountParameter.setPageSize(pageSize);
    }
    public void prePayeeResult(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,HCI_SESSION,null);
        String uen= ParamUtil.getString(request,"uen");
        String licensee =ParamUtil.getString(request,LICENSEE);
        String licenceNo =ParamUtil.getString(request,"licenceNo");
        ParamUtil.setSessionAttr(request,"uen",uen);
        ParamUtil.setSessionAttr(request,"licenceNo",licenceNo);
        ParamUtil.setSessionAttr(request,LICENSEE,licensee);

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(uen!=null) {
            filter.put("uen", uen);
        }
        if(licensee!=null) {
            filter.put(LICENSEE, licensee);
        }
        if(licenceNo!=null) {
            filter.put("licenceNo", licenceNo);
        }
        giroAccountParameter.setFilters(filter);
        SearchParam giroAccountParam = SearchResultHelper.getSearchParam(request, giroAccountParameter,true);
        CrudHelper.doPaging(giroAccountParam,bpc.request);
        List<SelectOption> bankNameOpts = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BANK_NAME);
        MasterCodePair mcp = new MasterCodePair("BANK_NAME", "BANK_NAME_DESC", bankNameOpts);
        giroAccountParam.setMasterCode(mcp);
        String sortFieldName = ParamUtil.getString(request,CRUD_ACTION_VALUE);
        String sortType = ParamUtil.getString(request,CRUD_ACTION_ADDITIONAL);
        String actionType=ParamUtil.getString(request,CRUD_ACTION_TYPE);
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            giroAccountParameter.setSortType(sortType);
            giroAccountParameter.setSortField(sortFieldName);
        }
        if("back".equals(actionType)){
            giroAccountParam= (SearchParam) ParamUtil.getSessionAttr(request,"searchGiroAccountParam");
        }
        QueryHelp.setMainSql(GIRO_PAYEE,"searchByGiroAcctInfo",giroAccountParam);
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
                giroAccountInfoViewDto.setBankNameDesc(gai.getBankNameDesc());
                giroAccountInfoViewDto.setBranchCode(gai.getBranchCode());
                giroAccountInfoViewDto.setLicenceNo(gai.getLicenceNo());
                giroAccountInfoViewDto.setLicenseeName(gai.getLicenseeName());
                giroAccountInfoViewDto.setSvcName(gai.getSvcName());
                giroAccountInfoViewDto.setUen(gai.getUen());
                giroAccountInfoViewDto.setId(gai.getId());
                giroAccountInfoViewDto.setCustomerReferenceNo(gai.getCustomerReferenceNo());
                giroAccountInfoViewDto.setRemarks(gai.getInternetRemarks());
                giroAccountInfoViewDtos.add(giroAccountInfoViewDto);
            }
            searchGiroDtoResult.setRows(giroAccountInfoViewDtos);
            ParamUtil.setRequestAttr(request,"searchGiroDtoResult",searchGiroDtoResult);

        }
        ParamUtil.setSessionAttr(request,"searchGiroAccountParam",giroAccountParam);

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

        try {
            eicSyncGiroAcctToFe(refNo, giroAccountInfoDtoList);
        }catch (Exception e){
            log.debug("no found fe org :{}",giroAccountInfoDtoList.get(0).getOrganizationId());
        }
    }
    public void reSearchPayee(BaseProcessClass bpc) {

    }
    public void preOrgResult(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,ACCT_NAME,null);
        ParamUtil.setSessionAttr(request,BANK_CODE,null);
        ParamUtil.setSessionAttr(request,BRANCH_CODE,null);
        ParamUtil.setSessionAttr(request,BANK_NAME,null);
        ParamUtil.setSessionAttr(request,BANK_ACCOUNT_NO,null);
        ParamUtil.setSessionAttr(request,CUS_REF_NO,null);
        ParamUtil.setSessionAttr(request,REMARKS,null);
        ParamUtil.setSessionAttr(request,GIRO_ACCOUNTINFO_DTO_LIST, null);
        String licenceNo= ParamUtil.getString(request,LICENCE_NO_SER);
        String licensee =ParamUtil.getString(request,LICENSEE_NAME);
        String uenNo =ParamUtil.getString(request,UEN_NO);
        ParamUtil.setSessionAttr(request,LICENCE_NO_SER,licenceNo);
        ParamUtil.setSessionAttr(request,LICENSEE_NAME,licensee);
        ParamUtil.setSessionAttr(request,UEN_NO,uenNo);

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(licenceNo!=null) {
            filter.put("licenceNo", licenceNo);
        }
        if(licensee!=null) {
            filter.put(LICENSEE, licensee);
        }
        if(uenNo!=null) {
            filter.put(UEN_NO, uenNo);
        }

        orgPremParameter.setFilters(filter);
        SearchParam orgPremParam = SearchResultHelper.getSearchParam(request, orgPremParameter,true);
        String sortFieldName = ParamUtil.getString(request,CRUD_ACTION_VALUE);
        String sortType = ParamUtil.getString(request,CRUD_ACTION_ADDITIONAL);
        String actionType=ParamUtil.getString(request,CRUD_ACTION_TYPE);
        if("back".equals(actionType)){
            orgPremParam= (SearchParam) ParamUtil.getSessionAttr(request,ORG_PREM_PARAM);
            ParamUtil.setSessionAttr(request,HCI_SESSION,null);
        }else if("add".equals(actionType)){
            orgPremParameter.setPageSize(pageSize);
            orgPremParameter.setPageNo(1);
        }else {
            CrudHelper.doPaging(orgPremParam,bpc.request);
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                orgPremParameter.setSortType(sortType);
                orgPremParameter.setSortField(sortFieldName);
                //orgPremParameter.setPageNo(1);
            }
        }
        QueryHelp.setMainSql(GIRO_PAYEE,SEARCH_BY_ORG_PREM_VIEW,orgPremParam);
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
        ParamUtil.setSessionAttr(request,ORG_PREM_PARAM,orgPremParam);
        ParamUtil.setRequestAttr(request,ORG_PREM_RESULT,orgPremResult);
        ParamUtil.setSessionAttr(request,GIRO_ACCT_FILE_DTO,new BlastManagementDto());
    }
    public void reSearchOrg(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType=ParamUtil.getString(request,CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request,CRUD_ACTION_TYPE,actionType);

    }
    public void doSelect(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request,"configFileSize",configFileSize);
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult= (SearchResult<OrganizationPremisesViewQueryDto>) ParamUtil.getSessionAttr(request,HCI_SESSION);
        if(orgPremResult==null){
            String [] orgPerIds=ParamUtil.getStrings(request,"opIds");
            SearchParam orgPremParam = SearchResultHelper.getSearchParam(request, orgPremParameter,true);
            String typeStr = SqlHelper.constructInCondition(OPV_ID,orgPerIds.length);
            int indx = 0;
            for (String s : orgPerIds){
                orgPremParam.addFilter(OPV_ID+indx, s);
                indx++;
            }
            orgPremParam.addParam("licIds",typeStr);
            ParamUtil.setSessionAttr(request,"giroLicIds",orgPerIds);

            CrudHelper.doPaging(orgPremParam,bpc.request);
            QueryHelp.setMainSql(GIRO_PAYEE,SEARCH_BY_ORG_PREM_VIEW,orgPremParam);
            orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
            ParamUtil.setSessionAttr(request,HCI_SESSION,orgPremResult);

        }
        List<SelectOption> selectOptions= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BANK_NAME);

        ParamUtil.setRequestAttr(request,"bankNameSelectOptions", selectOptions);


    }
    public void doBack(BaseProcessClass bpc) {

    }
    public void doSwich(BaseProcessClass bpc){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
    }

    public void backPayee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType=ParamUtil.getString(request,CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request,CRUD_ACTION_TYPE,actionType);
    }
    public void refill(BaseProcessClass bpc) {

    }
    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        String acctName= ParamUtil.getString(request,ACCT_NAME);
        String bankCode =ParamUtil.getString(request,BANK_CODE);
        String branchCode =ParamUtil.getString(request,BRANCH_CODE);
        String bankName= ParamUtil.getString(request,BANK_NAME);
        String bankAccountNo =ParamUtil.getString(request,BANK_ACCOUNT_NO);
        String cusRefNo =ParamUtil.getString(request,CUS_REF_NO);
        String remarks =ParamUtil.getString(request,REMARKS);
        ParamUtil.setSessionAttr(request,ACCT_NAME,acctName);
        ParamUtil.setSessionAttr(request,BANK_CODE,bankCode);
        ParamUtil.setSessionAttr(request,BRANCH_CODE,branchCode);
        ParamUtil.setSessionAttr(request,BANK_NAME,bankName);
        ParamUtil.setSessionAttr(request,BANK_ACCOUNT_NO,bankAccountNo);
        ParamUtil.setSessionAttr(request,CUS_REF_NO,cusRefNo);
        ParamUtil.setSessionAttr(request,REMARKS,remarks);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(acctName)){//60
            errorMap.put(ACCT_NAME, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,ACCT_NAME,FEE_FIELD));
        }else {
            if(acctName.length()>60){
                repMap.put(FEE_NUMBER,"60");
                repMap.put(FEE_FIELD_NO,"Account Name");
                errorMap.put(ACCT_NAME, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_WORD_LIMIT,repMap));
            }
            if(!isAlphanumericSpaceChar(acctName)){
                errorMap.put(ACCT_NAME, MessageUtil.getMessageDesc(FEE_ERROR_USER));
            }
        }
        if(StringUtil.isEmpty(bankCode)){//4
            errorMap.put(BANK_CODE, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,BANK_CODE,FEE_FIELD));
        }else {
            if(bankCode.length()>4){
                repMap.put(FEE_NUMBER,"4");
                repMap.put(FEE_FIELD_NO,"Bank Code");
                errorMap.put(BANK_CODE, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_WORD_LIMIT,repMap));
            }
            if(!isNumeric(bankCode)){
                errorMap.put(BANK_CODE, MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
        }
        if(StringUtil.isEmpty(branchCode)){//3
            errorMap.put(BRANCH_CODE, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,BRANCH_CODE,FEE_FIELD));
        }else {
            if(branchCode.length()>3){
                repMap.put(FEE_NUMBER,"3");
                repMap.put(FEE_FIELD_NO,"Branch Code");
                errorMap.put(BRANCH_CODE, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_WORD_LIMIT,repMap));
            }
            if(!isNumeric(branchCode)){
                errorMap.put(BRANCH_CODE, MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
        }
        if(StringUtil.isEmpty(bankAccountNo)){//10
            errorMap.put(BANK_ACCOUNT_NO, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,BANK_ACCOUNT_NO,FEE_FIELD));
        }else {
            if(bankAccountNo.length()>10){
                repMap.put(FEE_NUMBER,"10");
                repMap.put(FEE_FIELD_NO,"Bank Account No");
                errorMap.put(BANK_ACCOUNT_NO, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_WORD_LIMIT,repMap));
            }
            if(!isAlphanumericSpaceChar(bankAccountNo)){
                errorMap.put(BANK_ACCOUNT_NO, MessageUtil.getMessageDesc(FEE_ERROR_USER));
            }
        }
        if(StringUtil.isEmpty(bankName)){//60
            errorMap.put(BANK_NAME, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,BANK_NAME,FEE_FIELD));
        }
        if(StringUtil.isEmpty(cusRefNo)){//35
            errorMap.put(CUS_REF_NO, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,CUS_REF_NO,FEE_FIELD));
        }else {
            if(cusRefNo.length()>35){
                repMap.put(FEE_NUMBER,"35");
                repMap.put(FEE_FIELD_NO,"DDA Ref No");
                errorMap.put(CUS_REF_NO, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_WORD_LIMIT,repMap));
            }
            if(!isAlphanumericSpaceChar(cusRefNo)){
                errorMap.put(CUS_REF_NO, MessageUtil.getMessageDesc(FEE_ERROR_USER));
            }
        }
        if (!StringUtil.isEmpty(remarks) && remarks.length() > 4000) {//4000
            repMap.put(FEE_NUMBER, "4000");
            repMap.put(FEE_FIELD_NO, "Internal Remarks");
            errorMap.put(REMARKS, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_WORD_LIMIT, repMap));
        }
        List<GiroAccountFormDocDto> docs= IaisCommonUtils.genNewArrayList();

        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,GIRO_ACCT_FILE_DTO);
        if(blastManagementDto != null){
            if(!IaisCommonUtils.isEmpty(blastManagementDto.getAttachmentDtos())) {
                Iterator<AttachmentDto> it = blastManagementDto.getAttachmentDtos().iterator();
                while (it.hasNext()){
                    try {
                        AttachmentDto fileBit=it.next();
                        MultipartFile file = toMultipartFile(fileBit.getDocName(),fileBit.getDocName(), fileBit.getData());
                        GiroAccountFormDocDto doc =new GiroAccountFormDocDto();
                        long size = file.getSize() / 1024;
                        doc.setDocName(file.getOriginalFilename());
                        doc.setDocSize(Integer.valueOf(String.valueOf(size)));
                        doc.setPassDocValidate(true);
                        List<String> fileTypes = Arrays.asList("DOC,DOCX,PDF,JPG,PNG,GIF,TIFF".split(","));
                        Long fileSize=(systemParamConfig.getUploadFileLimit() * 1024 *1024L);
                        if((doc.getDocSize()!=null)){
                            Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
                            if (doc.getDocSize() != null) {
                                String filename = doc.getDocName();
                                String fileType = filename.substring(filename.lastIndexOf(46) + 1);
                                String s = fileType.toUpperCase();
                                if (!fileTypes.contains(s)) {
                                    map.put(FEE_FILE_TYPE, Boolean.FALSE);
                                } else {
                                    map.put(FEE_FILE_TYPE, Boolean.TRUE);
                                }

                                if (size > fileSize) {
                                    map.put(FEE_FILE_SIZE, Boolean.FALSE);
                                } else {
                                    map.put(FEE_FILE_SIZE, Boolean.TRUE);
                                }
                                //size
                                if(!map.get(FEE_FILE_SIZE)){
                                    doc.setPassDocValidate(false);
                                    errorMap.put(FEE_UPLOAD_FILE, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(systemParamConfig.getUploadFileLimit()),"sizeMax"));
                                }
                                //type
                                if(!map.get(FEE_FILE_TYPE)){
                                    doc.setPassDocValidate(false);
                                    errorMap.put(FEE_UPLOAD_FILE,MessageUtil.replaceMessage("GENERAL_ERR0018", "DOC,DOCX,PDF,JPG,PNG,GIF,TIFF",FEE_FILE_TYPE));
                                }
                                if(filename.length()>100){
                                    doc.setPassDocValidate(false);
                                    errorMap.put(FEE_UPLOAD_FILE, MessageUtil.getMessageDesc("GENERAL_ERR0022"));
                                }
                            }
                        }
                        if(doc.isPassDocValidate()){
                            String fileRepoGuid = serviceConfigService.saveFiles(file);
                            doc.setFileRepoId(fileRepoGuid);
                            fileBit.setId(fileRepoGuid);
                            docs.add(doc);
                        }else {
                            it.remove();
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }else {
                String errDocument=MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"GIRO Form",FEE_FIELD);
                errorMap.put(FEE_UPLOAD_FILE, errDocument);

            }
        }
        ParamUtil.setSessionAttr(request,GIRO_ACCT_FILE_DTO,blastManagementDto);

        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
        }

        List<GiroAccountFormDocDto> giroAccountFormDocDtoList=IaisCommonUtils.genNewArrayList();
        giroAccountFormDocDtoList.addAll(docs);

        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult= (SearchResult<OrganizationPremisesViewQueryDto>) ParamUtil.getSessionAttr(request,HCI_SESSION);
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
            giroAccountInfoDto.setLicenceId(opv.getId());
            giroAccountInfoDto.setInternetRemarks(remarks);
            giroAccountInfoDto.setGiroAccountFormDocDtoList(giroAccountFormDocDtoList);
            giroAccountInfoDtoList.add(giroAccountInfoDto);
        }
        ParamUtil.setSessionAttr(request,GIRO_ACCOUNTINFO_DTO_LIST, (Serializable) giroAccountInfoDtoList);


    }

    public static boolean isAlphanumericSpaceChar(CharSequence cs)
    {
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!(Character.isLetterOrDigit(cs.charAt(i))||Character.isSpaceChar(cs.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(CharSequence cs)
    {
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static MultipartFile toMultipartFile(String fieldName, String fileName, byte[] fileByteArray) throws Exception {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        String contentType = new MimetypesFileTypeMap().getContentType(fileName);
        FileItem fileItem = diskFileItemFactory.createItem(fieldName, contentType, false, fileName);
        try (
                InputStream inputStream = new ByteArrayInputStream(fileByteArray);
                OutputStream outputStream = fileItem.getOutputStream()
        ) {
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw e;
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    public void preView(BaseProcessClass bpc) {

    }
    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        String refNo=System.currentTimeMillis()+"";
        List<GiroAccountInfoDto> giroAccountInfoDtoList= (List<GiroAccountInfoDto>) ParamUtil.getSessionAttr(request,GIRO_ACCOUNTINFO_DTO_LIST);
        for (GiroAccountInfoDto giro:giroAccountInfoDtoList
        ) {
            giro.setEventRefNo(refNo);
            giro.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<GiroAccountInfoDto> giroAccountInfoDtoList1= giroAccountService.createGiroAccountInfo(giroAccountInfoDtoList);

        try {
            eicSyncGiroAcctToFe(refNo, giroAccountInfoDtoList1);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.debug("no found fe org :{}",giroAccountInfoDtoList1.get(0).getOrganizationId());
        }
        try {
            for (GiroAccountInfoDto giro:giroAccountInfoDtoList1
            ) {
                giroAccountService.sendEmailForGiroAccountAndSMSAndMessage(giro,giroAccountInfoDtoList1.size());
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.debug("send Email failed");
        }

        ParamUtil.setSessionAttr(request,GIRO_ACCT_FILE_DTO,null);
    }

    private void eicSyncGiroAcctToFe(String refNo, List<GiroAccountInfoDto> giroAccountInfoDtoList1) {
        giroAccountService.syncFeGiroAcctDto(giroAccountInfoDtoList1);
    }

    @PostMapping(value = "/sort-licence-session")
    public @ResponseBody
    Map<String, Object> sortLicSession(HttpServletRequest request){

        String sortFieldName = ParamUtil.getString(request,CRUD_ACTION_VALUE);
        String sortType = ParamUtil.getString(request,CRUD_ACTION_ADDITIONAL);
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            orgPremParameter.setSortType(sortType);
            orgPremParameter.setSortField(sortFieldName);
        }
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(request,"configFileSize",configFileSize);
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult;
        String [] orgPerIds= (String[]) ParamUtil.getSessionAttr(request,"giroLicIds");
        SearchParam orgPremParam = SearchResultHelper.getSearchParam(request, orgPremParameter,true);
        String typeStr = SqlHelper.constructInCondition(OPV_ID,orgPerIds.length);
        int indx = 0;
        for (String s : orgPerIds){
            orgPremParam.addFilter(OPV_ID+indx, s);
            indx++;
        }
        orgPremParam.addParam("licIds",typeStr);

        QueryHelp.setMainSql(GIRO_PAYEE,SEARCH_BY_ORG_PREM_VIEW,orgPremParam);
        orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put(ORG_PREM_RESULT,orgPremResult);
        return map;
    }
}
