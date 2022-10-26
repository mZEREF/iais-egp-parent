package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.FileSyncClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionAFCClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCSaveDto;
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
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_CERTIFICATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsAFCReportService {
    private final InspectionAFCClient inspectionAFCClient;
    private final FileRepoClient fileRepoClient;
    private final FileSyncClient fileSyncClient;

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
            case APP_STATUS_PEND_DO_REPORT_REVIEW:
            case APP_STATUS_PEND_AO_REPORT_REVIEW:
                profile = ValidationConstants.PROFILE_AFC_REPORT_SUBMIT;
                break;
            default:
                log.warn("The application can't upload AFC Report");
                profile = null;
        }
        return profile;
    }


    public ReviewAFCReportDto getDisplayDto(HttpServletRequest request) {
        ReviewAFCReportDto dto = (ReviewAFCReportDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO);
        if (dto == null) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            dto = inspectionAFCClient.getReviewAFCReportDto(appId).getEntity();
        }
        return dto;
    }

    @SuppressWarnings("unchecked")
    public void validateAndSaveReport(HttpServletRequest request, String role, boolean needSave) {
        ReviewAFCReportDto dto = getDisplayDto(request);
        AFCCommonDocDto commonDocDto = getAFCCommonDocDto(request);
        bindOfficerParam(request, dto, role);
        // validate saved docs
        List<CertificationDocDisPlayDto> sameRoundSavedDocs = findSameRoundSavedDocs(request, dto);
        List<DocMeta> docMetas = commonDocDto.convertToDocMetaList(sameRoundSavedDocs);
        dto.setDocMetas(docMetas);
        dto.setProfile(getProfile(dto.getAppStatus()));

        Map<String, CertificationDocDisPlayDto> docDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);
        if(!CollectionUtils.isEmpty(docDtoMap)){
            List<CertificationDocDisPlayDto> certificationDocDisPlayDtos = new ArrayList<>(docDtoMap.values());
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
            ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
        }
        ValidationResultDto validationResultDto;
        if (RoleConsts.USER_ROLE_BSB_AO.equals(role)) {
            validationResultDto = inspectionAFCClient.validateAOCertificationReport(dto);
        } else {
            validationResultDto = inspectionAFCClient.validateDOCertificationReport(dto);
        }
        if (validationResultDto.isPass()) {
            if (needSave) {
                AFCSaveDto afcSaveDto = new AFCSaveDto();
                ReviewAFCReportDto displayDto = getDisplayDto(request);
                afcSaveDto.setAppId(displayDto.getAppId());
                List<NewFileSyncDto> newFileSyncDtos = saveNewUploadedDoc(commonDocDto);
                boolean uploadNewDoc = false;
                if (!CollectionUtils.isEmpty(commonDocDto.getSavedDocMap())) {
                    uploadNewDoc = true;
                    afcSaveDto.setNewCertDocDtoList(new ArrayList<>(commonDocDto.getSavedDocMap().values()));
                    docDtoMap.putAll(commonDocDto.getSavedDocMap());
                }else if (!CollectionUtils.isEmpty(displayDto.getCertificationDocDisPlayDtos())) {
                    List<CertificationDocDisPlayDto> needSaveSavedDoc = displayDto.getCertificationDocDisPlayDtos().stream().filter(docDto -> docDto.getRoundOfReview().equals(dto.getMaxRound())).filter(docDto -> docDto.getSubmitterRole().equals(dto.getLastUploadRole())).collect(Collectors.toList());
                    afcSaveDto.setSavedCertDocDtoList(needSaveSavedDoc);
                }
                afcSaveDto.setUploadNewDoc(uploadNewDoc);
                ResponseDto<String> responseDto = inspectionAFCClient.saveInsAFCData(afcSaveDto);
                ParamUtil.setSessionAttr(request, "ValidSave", "Y");
                if (responseDto.ok()) {
                    log.info("You have successfully submitted the data");
                } else {
                    log.info("Failed to submit the data");
                }
                try {
                    // sync docs
                    fileSyncClient.syncSaveFiles(newFileSyncDtos.toArray(new NewFileSyncDto[0]));
                } catch (Exception e) {
                    log.error("Fail to sync files to FE", e);
                }
            }
        }else {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
        }
        ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_CERTIFICATION);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_DOC_MAP, (Serializable) docDtoMap);
        // put saved doc to ReviewAFCReportDto, otherwise bindSavedCertDocParam method will error
        dto.getCertificationDocDisPlayDtos().addAll(commonDocDto.getSavedDocMap().values());
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
    }

    //get Do/Ao action data
    public void bindOfficerParam(HttpServletRequest request, ReviewAFCReportDto dto, String role) {
        List<CertificationDocDisPlayDto> certificationDocDtos = dto.getCertificationDocDisPlayDtos();
        //mark previous doc as final
        bindSavedCertDocParam(request,certificationDocDtos,dto,role);
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
                if (RoleConsts.USER_ROLE_BSB_DO.equals(role) || RoleConsts.USER_ROLE_BSB_AO.equals(role)) {
                    docDtoMap.get(repoId).setMohMarkFinal(checked);
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
    private List<CertificationDocDisPlayDto> findSameRoundSavedDocs(HttpServletRequest request, ReviewAFCReportDto dto){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos = dto.getCertificationDocDisPlayDtos();
        return certificationDocDisPlayDtos.stream()
                .filter(docDto -> docDto.getRoundOfReview().equals(dto.getMaxRound()))
                .filter(docDto -> docDto.getSubmitBy().equals(loginContext.getUserId()))
                .filter(docDto -> docDto.getSubmitterRole().equals("MOH"))
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

    public String popupMsgDisplayJudgement(HttpServletRequest request, String validateResult){
        ReviewAFCReportDto dto = (ReviewAFCReportDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO);
        Boolean finalRound;
        if (dto.getMaxRound()+1 == 5) {
            finalRound = Boolean.TRUE;
        } else {
            finalRound = Boolean.FALSE;
        }
        ParamUtil.setRequestAttr(request, FINAL_ROUND, finalRound);
        //if haveConfirm equal 'Y', mean popup message have already shown, already can submit form
        String haveConfirm = ParamUtil.getString(request, "haveConfirm");
        if (finalRound == Boolean.TRUE && !"Y".equals(haveConfirm)) {
            validateResult = "back";
        }
        return validateResult;
    }
}
