package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.*;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.ApprovalResultDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.EnquiryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.action.AORevocationDelegator.setRevocationDoc;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

/**
 * @author Zhu Tangtang
 */
@Delegator(value = "DORevocationDelegator")
@Slf4j
public class DORevocationDelegator {
    private final RevocationClient revocationClient;
    private final BiosafetyEnquiryClient biosafetyEnquiryClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public DORevocationDelegator(RevocationClient revocationClient, BiosafetyEnquiryClient biosafetyEnquiryClient,FileRepoClient fileRepoClient,BsbFileClient bsbFileClient) {
        this.revocationClient = revocationClient;
        this.biosafetyEnquiryClient = biosafetyEnquiryClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    /**
     * StartStep: startStep
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_REVOCATION, FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_REVOCATION_DETAIL, null);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, null);
    }

    /**
     * AutoStep: prepareData
     * Temporarily disabled
     * enter from Biosafety enquiry
     * approval list
     */
    public void prepareFacilityListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<ApprovalResultDto> searchResult = biosafetyEnquiryClient.getApproval(searchDto);
        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, searchResult.getEntity().getBsbApproval());
        } else {
            log.warn("get revocation application API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, new ArrayList<>());
        }
    }

    /**
     * AutoStep: doSearch
     * Temporarily disabled
     */
    public void doSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, null);
        EnquiryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        String approvalStatus = ParamUtil.getString(request, PARAM_APPROVAL_STATUS);
        String approvalNo = ParamUtil.getString(request, PARAM_APPROVAL_NO);
        searchDto.setApprovalStatus(approvalStatus);
        searchDto.setApprovalNo(approvalNo);
        searchDto.setPage(0);

        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doPaging
     * Temporarily disabled
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doSorting
     * Temporarily disabled
     */
    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * when officer need to update inventory of bat , use this method
     * Inventory update is now required by default
     */
    public void preInventory(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String from = ParamUtil.getRequestString(request, FROM);
        String maskedApprovalId = ParamUtil.getRequestString(request, PARAM_APPROVAL_ID);
        String maskedAppId = ParamUtil.getRequestString(request, KEY_APP_ID);
        String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);

        ParamUtil.setSessionAttr(request, FROM, from);
        ParamUtil.setSessionAttr(request, PARAM_APPROVAL_ID, maskedApprovalId);
        ParamUtil.setSessionAttr(request, KEY_APP_ID, maskedAppId);
        ParamUtil.setSessionAttr(request, KEY_TASK_ID, maskedTaskId);
    }

    /**
     * AutoStep: prepareData
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FLAG, null);
        ParamUtil.setSessionAttr(request, BACK, null);
        ParamUtil.setSessionAttr(request, KEY_CAN_UPLOAD, "Y");

        String from = ParamUtil.getRequestString(request, FROM);
        SubmitRevokeDto revokeDto = getRevokeDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, revokeDto.retrieveValidationResult());
        }
        if (!StringUtils.hasLength(revokeDto.getModule())) {
            if (from.equals(FAC)) {
                String approvalId = ParamUtil.getRequestString(request, PARAM_APPROVAL_ID);
                approvalId = MaskUtil.unMaskValue("id", approvalId);
                revokeDto = revocationClient.getSubmitRevokeDtoByApprovalId(approvalId).getEntity();
                ParamUtil.setSessionAttr(request, FLAG, FAC);
                ParamUtil.setSessionAttr(request, BACK, REVOCATION_FACILITY);
            }
            if (from.equals(APP)) {
                String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
                String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
                String appId = MaskUtil.unMaskValue("id", maskedAppId);
                String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
                if (appId == null || appId.equals(maskedAppId)) {
                    throw new IaisRuntimeException("Invalid masked application ID");
                }
                if (taskId == null || taskId.equals(maskedTaskId)) {
                    throw new IaisRuntimeException("Invalid masked task ID");
                }
                revokeDto = revocationClient.getSubmitRevokeDtoByAppId(appId).getEntity();
                revokeDto.setTaskId(taskId);
                ParamUtil.setSessionAttr(request, FLAG, APP);
                ParamUtil.setSessionAttr(request, BACK, REVOCATION_TASK_LIST);
                setRevocationDoc(request, revokeDto);
            }
        }
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String reason = ParamUtil.getString(request, PARAM_REASON);
        String remarks = ParamUtil.getString(request, PARAM_DO_REMARKS);
        String flag = (String) ParamUtil.getSessionAttr(request, FLAG);
        SubmitRevokeDto revokeDto = getRevokeDto(request);
        //get user name
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        revokeDto.setLoginUser(loginContext.getUserName());
        revokeDto.setApplicationDt(new Date());
        revokeDto.setReason(PARAM_REASON_TYPE_DO);
        revokeDto.setReasonContent(reason);
        revokeDto.setRemarks(remarks);
        revokeDto.setModule("doRevoke");
        if (flag.equals(FAC)) {
            revokeDto.setAppType(PARAM_APPLICATION_TYPE_REVOCATION);
            revokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_AO);
        }
        if (flag.equals(APP)) {
            revokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_AO);
            setRevocationDoc(request,revokeDto);
        }
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqObjMapping(mulReq,request,"Revocation");
        revokeDto.setPrimaryDocDto(primaryDocDto);
        revokeDto.setDocType("Revocation");

        //joint repoId exist
        String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
        revokeDto.setRepoIdNewString(newRepoId);
        //set newDocFiles
        revokeDto.setNewDocInfos(primaryDocDto.getNewDocTypeList());
        //set need Validation value
        revokeDto.setDocMetas(primaryDocDto.doValidation());
        doValidation(revokeDto, request);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    /**
     * AutoStep: doCreate
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SubmitRevokeDto submitRevokeDto = getRevokeDto(request);
        PrimaryDocDto primaryDocDto = submitRevokeDto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if(primaryDocDto != null){
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();

            //newFile change to saved File and save info to db
            newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
            submitRevokeDto.setSavedInfos(primaryDocDto.getExistDocTypeList());
        }else{
            log.info(KEY_NON_OBJECT_ERROR);
        }
        revocationClient.saveRevokeApplication(submitRevokeDto);
        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (newFilesToSync != null && !newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                NewFileSyncDto[] newFileSyncDtoList = newFilesToSync.toArray(new NewFileSyncDto[0]);
                bsbFileClient.syncFiles(newFileSyncDtoList);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to FE", e);
        }
    }

    private EnquiryDto getSearchDto(HttpServletRequest request) {
        EnquiryDto searchDto = (EnquiryDto) ParamUtil.getSessionAttr(request, PARAM_FACILITY_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private EnquiryDto getDefaultSearchDto() {
        EnquiryDto dto = new EnquiryDto();
        dto.clearAllFields();
        dto.defaultPaging();
        return dto;
    }

    private SubmitRevokeDto getRevokeDto(HttpServletRequest request) {
        SubmitRevokeDto searchDto = (SubmitRevokeDto) ParamUtil.getSessionAttr(request, PARAM_REVOKE_DTO);
        return searchDto == null ? getDefaultRevokeDto() : searchDto;
    }

    private SubmitRevokeDto getDefaultRevokeDto() {
        return new SubmitRevokeDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     */
    private void doValidation(SubmitRevokeDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }
}
