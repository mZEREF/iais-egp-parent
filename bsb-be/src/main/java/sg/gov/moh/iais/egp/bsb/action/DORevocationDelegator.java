package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.*;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.ApprovalResultDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.EnquiryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto;
import sg.gov.moh.iais.egp.bsb.dto.suspension.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

/**
 * @author Zhu Tangtang
 */
@Delegator(value = "DORevocationDelegator")
@Slf4j
public class DORevocationDelegator {
    private static final String ACTION_TYPE_SUBMIT = "doSubmit";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";

    private final RevocationClient revocationClient;
    private final BiosafetyEnquiryClient biosafetyEnquiryClient;
    private final FileRepoClient fileRepoClient;
    private final ProcessHistoryService processHistoryService;

    public DORevocationDelegator(RevocationClient revocationClient, BiosafetyEnquiryClient biosafetyEnquiryClient, FileRepoClient fileRepoClient, ProcessHistoryService processHistoryService) {
        this.revocationClient = revocationClient;
        this.biosafetyEnquiryClient = biosafetyEnquiryClient;
        this.fileRepoClient = fileRepoClient;
        this.processHistoryService = processHistoryService;
    }

    /**
     * StartStep: startStep
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_REVOCATION, FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, null);
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, null);
        ParamUtil.setSessionAttr(request, FROM, null);
        ParamUtil.setSessionAttr(request, BACK_URL, null);
    }

    /**
     * AutoStep: prepareData
     * approval list
     */
    public void prepareFacilityListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
        selectOption(request, "facilityName", facNames);
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
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, null);
        EnquiryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        addApprovalFilter(request,searchDto);
        searchDto.setPage(0);

        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    private void addApprovalFilter(HttpServletRequest request, EnquiryDto enquiryDto) throws ParseException {
        String facilityName = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_FACILITY_NAME);
        String facilityAddress = ParamUtil.getString(request,"facilityAddress");
        String[] facilityClassifications = ParamUtil.getStrings(request, BioSafetyEnquiryConstants.PARAM_FACILITY_CLASSIFICATION);
        String approvalNo = ParamUtil.getString(request, PARAM_APPROVAL_NO);
        String approvalStatus = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_STATUS);
        String approvalType = ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_TYPE);
        Date approvedDateFrom = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVED_DATE_FROM));
        Date approvedDateTo = Formatter.parseDate(ParamUtil.getString(request, BioSafetyEnquiryConstants.PARAM_APPROVED_DATE_TO));
        if (StringUtil.isNotEmpty(facilityName)) {
            enquiryDto.setFacilityName(facilityName);
        }
        if (StringUtil.isNotEmpty(facilityAddress)) {
            enquiryDto.setFacilityAddress(facilityAddress);
        }
        if (facilityClassifications != null && facilityClassifications.length > 0) {
            enquiryDto.setFacilityClassifications(Arrays.asList(facilityClassifications));
        }
        if (StringUtil.isNotEmpty(approvalNo)) {
            enquiryDto.setApprovalNo(approvalNo);
        }
        if (StringUtil.isNotEmpty(approvalStatus)) {
            enquiryDto.setApprovalStatus(approvalStatus);
        }
        if (StringUtil.isNotEmpty(approvalType)) {
            enquiryDto.setApprovalType(approvalType);
        }
        if (approvedDateFrom != null) {
            enquiryDto.setApprovedDateFrom(approvedDateFrom);
        }
        if (approvedDateTo != null) {
            enquiryDto.setApprovedDateTo(approvedDateTo);
        }
    }

    /**
     * AutoStep: doPaging
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
        if (from.equals(FAC)) {
            ParamUtil.setSessionAttr(request, BACK_URL, APPROVAL_LIST_URL);
        }
        if (from.equals(APP)) {
            ParamUtil.setSessionAttr(request, BACK_URL, TASK_LIST_URL);
        }
    }

    /**
     * AutoStep: prepareData
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_CAN_UPLOAD, "Y");

        String from = ParamUtil.getRequestString(request, FROM);
        SubmitRevokeDto revokeDto = getRevokeDto(request);
        if (!StringUtils.hasLength(revokeDto.getModule())) {
            if (from.equals(FAC)) {
                String approvalId = ParamUtil.getRequestString(request, PARAM_APPROVAL_ID);
                approvalId = MaskUtil.unMaskValue("id", approvalId);
                revokeDto = revocationClient.getSubmitRevokeDtoByApprovalId(approvalId).getEntity();
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
                PrimaryDocDto primaryDocDto = new PrimaryDocDto();
                primaryDocDto.setSavedDocMap(revokeDto.getQueryDocMap());
                revokeDto.setPrimaryDocDto(primaryDocDto);
                //show routingHistory list
                processHistoryService.getAndSetHistoryInSession(revokeDto.getApplicationNo(), request);
            }
        }
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SubmitRevokeDto revokeDto = getRevokeDto(request);
        revokeDto.doReqObjMapping(request);
        //
        PrimaryDocDto primaryDocDto;
        if (revokeDto.getPrimaryDocDto()!=null) {
            primaryDocDto = revokeDto.getPrimaryDocDto();
        }else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request,"Revocation");
        revokeDto.setPrimaryDocDto(primaryDocDto);
        //
        revokeDto.setDocMetas(primaryDocDto.doValidation("doRevoke"));
        validateData(revokeDto, request);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    /**
     * AutoStep: doCreate
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SubmitRevokeDto submitRevokeDto = getRevokeDto(request);
        PrimaryDocDto primaryDocDto = submitRevokeDto.getPrimaryDocDto();
        if (primaryDocDto!=null){
            if (!CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
                deleteUnwantedDoc(primaryDocDto);
                submitRevokeDto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
            }
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //newFile change to saved File and save info to db
            primaryDocDto.newFileSaved(repoIds);
            submitRevokeDto.setSavedInfos(new ArrayList<>(primaryDocDto.getSavedDocMap().values()));
        }else {
            log.info(KEY_NON_OBJECT_ERROR);
        }
        revocationClient.saveRevokeApplication(submitRevokeDto);
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

    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = new ArrayList<>(strings.size());
        for (String string : strings) {
            selectModel.add(new SelectOption(string, string));
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    private void validateData(SubmitRevokeDto dto, HttpServletRequest request){
        //validation
        String actionType;
        ValidationResultDto validationResultDto = revocationClient.validateRevoke(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SUBMIT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }
}
