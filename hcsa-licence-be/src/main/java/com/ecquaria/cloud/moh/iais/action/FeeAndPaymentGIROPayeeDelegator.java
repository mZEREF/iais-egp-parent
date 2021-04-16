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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
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
import java.util.Date;
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
    private SystemParamConfig systemParamConfig;


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
        ParamUtil.setSessionAttr(request,"giroAcctFileDto",null);

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
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        String actionType=ParamUtil.getString(request,"crud_action_type");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            giroAccountParameter.setSortType(sortType);
            giroAccountParameter.setSortField(sortFieldName);
            //giroAccountParameter.setPageNo(1);
        }
        if("back".equals(actionType)){
            giroAccountParam= (SearchParam) ParamUtil.getSessionAttr(request,"searchGiroAccountParam");
        }
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
            ParamUtil.setSessionAttr(request,"searchGiroAccountParam",giroAccountParam);
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
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        String actionType=ParamUtil.getString(request,"crud_action_type");
        if("back".equals(actionType)){
            orgPremParam= (SearchParam) ParamUtil.getSessionAttr(request,"orgPremParam");
            ParamUtil.setSessionAttr(request,"hciSession",null);
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
        QueryHelp.setMainSql("giroPayee","searchByOrgPremView",orgPremParam);
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
        ParamUtil.setSessionAttr(request,"orgPremParam",orgPremParam);
        ParamUtil.setRequestAttr(request,"orgPremResult",orgPremResult);
        ParamUtil.setSessionAttr(request,"giroAcctFileDto",new BlastManagementDto());
    }
    public void reSearchOrg(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType=ParamUtil.getString(request,"crud_action_type");
        ParamUtil.setRequestAttr(request,"crud_action_type",actionType);

    }
    public void doSelect(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request,"configFileSize",configFileSize);
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
    public void doSwich(BaseProcessClass bpc){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
    }

    public void backPayee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType=ParamUtil.getString(request,"crud_action_type");
        ParamUtil.setRequestAttr(request,"crud_action_type",actionType);
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

        //MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        //CommonsMultipartFile file= (CommonsMultipartFile) mulReq.getFile( "UploadFile");
        //String commDelFlag = ParamUtil.getString(mulReq, "commDelFlag");
        List<GiroAccountFormDocDto> docs= IaisCommonUtils.genNewArrayList();

        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"giroAcctFileDto");
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
                                    errorMap.put("UploadFile",MessageUtil.replaceMessage("GENERAL_ERR0018", "DOC,DOCX,PDF,JPG,PNG,GIF,TIFF","fileType"));
                                }
                                if(filename.length()>100){
                                    doc.setPassDocValidate(false);
                                    errorMap.put("UploadFile", MessageUtil.getMessageDesc("GENERAL_ERR0022"));
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
                String errDocument=MessageUtil.replaceMessage("GENERAL_ERR0006","GIRO Form","field");
                errorMap.put("UploadFile", errDocument);

            }
        }
        ParamUtil.setSessionAttr(request,"giroAcctFileDto",blastManagementDto);

        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
        }

        List<GiroAccountFormDocDto> giroAccountFormDocDtoList=IaisCommonUtils.genNewArrayList();
        giroAccountFormDocDtoList.addAll(docs);

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
        List<GiroAccountInfoDto> giroAccountInfoDtoList= (List<GiroAccountInfoDto>) ParamUtil.getSessionAttr(request,"giroAccountInfoDtoList");
        for (GiroAccountInfoDto giro:giroAccountInfoDtoList
             ) {
            giro.setEventRefNo(refNo);
            giro.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<GiroAccountInfoDto> giroAccountInfoDtoList1= giroAccountService.createGiroAccountInfo(giroAccountInfoDtoList);


        eicSyncGiroAcctToFe(refNo, giroAccountInfoDtoList1);
        try {
            for (GiroAccountInfoDto giro:giroAccountInfoDtoList1
            ) {
                giroAccountService.sendEmailForGiroAccountAndSMSAndMessage(giro,giroAccountInfoDtoList1.size());
            }
        }catch (Exception e){
            log.debug("send Email failed");
        }

        ParamUtil.setSessionAttr(request,"giroAcctFileDto",null);
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
