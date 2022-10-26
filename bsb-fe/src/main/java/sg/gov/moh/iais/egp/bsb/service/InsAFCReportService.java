package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionAFCClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDisPlayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_REPO_ID_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.ValidationConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.FINAL_ROUND;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;

/**
 * @author tangtang
 * @date 2022/4/29 17:27
 */
@Service
@Slf4j
public class InsAFCReportService {
    private final InspectionAFCClient inspectionAFCClient;
    private final FileRepoClient fileRepoClient;

    public InsAFCReportService(InspectionAFCClient inspectionAFCClient, FileRepoClient fileRepoClient) {
        this.inspectionAFCClient = inspectionAFCClient;
        this.fileRepoClient = fileRepoClient;
    }

    public AFCCommonDocDto getAFCCommonDocDto(HttpServletRequest request) {
        AFCCommonDocDto commonDocDto = (AFCCommonDocDto) ParamUtil.getSessionAttr(request, DocConstants.KEY_COMMON_DOC_DTO);
        return commonDocDto == null ? new AFCCommonDocDto() : commonDocDto;
    }

    public void setSavedDocMap(ReviewAFCReportDto dto, HttpServletRequest request){
        List<CertificationDocDisPlayDto> certificationDocDtos = dto.getCertificationDocDisPlayDtos();
        if (!CollectionUtils.isEmpty(certificationDocDtos)) {
            Map<String, CertificationDocDisPlayDto> repoIdDocDtoMap = getRepoIdDocDtoMap(certificationDocDtos);
            ParamUtil.setSessionAttr(request,PARAM_REPO_ID_DOC_MAP, (Serializable) repoIdDocDtoMap);
        }
    }

    public String getProfile(String appStatus) {
        String profile;
        switch (appStatus) {
            case APP_STATUS_PEND_AFC_REPORT_UPLOAD:
                profile = ValidationConstants.PROFILE_AFC_ADMIN_FIRST_SUBMIT;
                break;
            case APP_STATUS_PEND_AFC_INPUT:
            case APP_STATUS_PEND_APPLICANT_INPUT:
                profile = ValidationConstants.PROFILE_AFC_REPORT_SUBMIT;
                break;
            default:
                log.warn("The application can't upload AFC Report");
                profile = null;
        }
        return profile;
    }

    public String validateAFC(HttpServletRequest request) {
        ReviewAFCReportDto dto = getDisplayDto(request);
        AFCCommonDocDto commonDocDto = getAFCCommonDocDto(request);
        bindAFCParam(request, dto);
        List<CertificationDocDisPlayDto> sameRoundSavedDocs = findSameRoundSavedDocs(request, dto, RoleConsts.USER_ROLE_BSB_AFC_USER);
        List<DocMeta> docMetas = commonDocDto.convertToDocMetaList(sameRoundSavedDocs);
        dto.setDocMetas(docMetas);
        dto.setProfile(getProfile(dto.getAppStatus()));
        String actionType;
        ValidationResultDto validationResultDto = inspectionAFCClient.validateAFCReportDto(dto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = InspectionConstants.PARAM_PREPARE;
        } else {
            actionType = popupMsgDisplayJudgement(request);
        }
        return actionType;
    }

    public String validateApplicant(HttpServletRequest request) {
        ReviewAFCReportDto dto = getDisplayDto(request);
        AFCCommonDocDto commonDocDto = getAFCCommonDocDto(request);
        bindApplicantParam(request, dto);
        // validate saved docs
        List<CertificationDocDisPlayDto> sameRoundSavedDocs = findSameRoundSavedDocs(request, dto, RoleConsts.USER_ROLE_BSB_FACILITY_USER);
        List<DocMeta> docMetas = commonDocDto.convertToDocMetaList(sameRoundSavedDocs);
        dto.setDocMetas(docMetas);
        dto.setProfile(getProfile(dto.getAppStatus()));
        String actionType;
        ValidationResultDto validationResultDto = inspectionAFCClient.validateAFCReportDto(dto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = InspectionConstants.PARAM_PREPARE;
        } else {
            actionType = popupMsgDisplayJudgement(request);
        }
        return actionType;
    }

    public ReviewAFCReportDto getDisplayDto(HttpServletRequest request) {
        ReviewAFCReportDto dto = (ReviewAFCReportDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO);
        if (dto == null) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            dto = inspectionAFCClient.getReviewAFCReportDto(appId).getEntity();
        }
        return dto;
    }

    //get afc action data
    private void bindAFCParam(HttpServletRequest request, ReviewAFCReportDto dto) {
        List<CertificationDocDisPlayDto> certificationDocDtos = dto.getCertificationDocDisPlayDtos();
        bindSavedCertDocParam(request, certificationDocDtos, dto, RoleConsts.USER_ROLE_BSB_AFC_USER);
    }

    //get applicant action data
    private void bindApplicantParam(HttpServletRequest request, ReviewAFCReportDto dto) {
        List<CertificationDocDisPlayDto> certificationDocDtos = dto.getCertificationDocDisPlayDtos();
        //mark previous doc as final
        bindSavedCertDocParam(request, certificationDocDtos, dto, RoleConsts.USER_ROLE_BSB_FACILITY_USER);
    }

    @SuppressWarnings("unchecked")
    private void bindSavedCertDocParam(HttpServletRequest request,List<CertificationDocDisPlayDto> certificationDocDtos, ReviewAFCReportDto dto,String role){
        if (!CollectionUtils.isEmpty(certificationDocDtos)) {
            //key is repoId
            Map<String, CertificationDocDisPlayDto> docDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);

            List<String> maskedRepoIds = certificationDocDtos.stream().filter(docDto -> docDto.getRoundOfReview().equals(dto.getMaxRound())).filter(docDto -> docDto.getSubmitterRole().equals(dto.getLastUploadRole())).map(CertificationDocDisPlayDto::getMaskedRepoId).collect(Collectors.toList());
            List<String> allChecked = new ArrayList<>(maskedRepoIds.size());
            for (String maskedRepoId : maskedRepoIds) {
                String checked = ParamUtil.getString(request, maskedRepoId + role);
                String repoId = MaskUtil.unMaskValue("file", maskedRepoId);
                allChecked.add(checked);
                if (RoleConsts.USER_ROLE_BSB_FACILITY_USER.equals(role)) {
                    docDtoMap.get(repoId).setApplicantMarkFinal(checked);
                }
                if (RoleConsts.USER_ROLE_BSB_AFC_USER.equals(role)) {
                    docDtoMap.get(repoId).setAfcMarkFinal(checked);
                }
            }
            dto.setActionOnOld(allChecked.contains(YES));
            ParamUtil.setSessionAttr(request,PARAM_REPO_ID_DOC_MAP, (Serializable) docDtoMap);
        }
    }

    /**
     * After you exit the page after saving the file, you still need to verify the file the next time you enter the page
     * @param dto contain all saved docs
     * @return same round same user saved docs
     */
    private List<CertificationDocDisPlayDto> findSameRoundSavedDocs(HttpServletRequest request, ReviewAFCReportDto dto, String role){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos = dto.getCertificationDocDisPlayDtos();
        return certificationDocDisPlayDtos.stream()
                .filter(docDto -> docDto.getRoundOfReview().equals(dto.getMaxRound()))
                .filter(docDto -> docDto.getSubmitBy().equals(loginContext.getUserId()))
                .filter(docDto -> docDto.getSubmitterRole().equals(role))
                .collect(Collectors.toList());
    }

    /**
     * Save new uploaded documents into FE file repo.
     *
     * @param dto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(AFCCommonDocDto dto) {
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = dto.getNewDocMap().values().stream().map(CertificationDocDto::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = dto.newFileSaved(repoIds);
        }
        return newFilesToSync;
    }

    public Map<String,CertificationDocDisPlayDto> getRepoIdDocDtoMap(List<CertificationDocDisPlayDto> certificationDocDtos){
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(CertificationDocDisPlayDto::getRepoId, certificationDocDtos);
    }

    public String popupMsgDisplayJudgement(HttpServletRequest request){
        String validateResult;
        ReviewAFCReportDto dto = (ReviewAFCReportDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO);
        Boolean finalRound;
        if (dto.getMaxRound()+1 == 5) {
            finalRound = Boolean.TRUE;
        } else {
            finalRound = Boolean.FALSE;
        }
        ParamUtil.setRequestAttr(request, FINAL_ROUND, finalRound);
        //if haveConfirm equal 'Y', mean popup message have already shown, already can submit form
        String haveConfirm = ParamUtil.getRequestString(request, "haveConfirm");
        if (finalRound == Boolean.TRUE && !"Y".equals(haveConfirm)) {
            validateResult = InspectionConstants.PARAM_PREPARE;
        } else {
            validateResult = InspectionConstants.PARAM_NEXT;
        }
        return validateResult;
    }
}
