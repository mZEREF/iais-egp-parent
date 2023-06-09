package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionAFCClient;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDisPlayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.InsAFCReportService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AFC_CERTIFICATION_UPLOAD_REPORT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.KEY_COMMON_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_REPO_ID_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ACK_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_AFC_DASHBOARD_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_DASHBOARD_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.PARAM_CAN_ACTION_ROLE;

@Slf4j
@Delegator("insAFCReportDelegator")
@RequiredArgsConstructor
public class InspectionAFCReportDelegator {
    private static final String PARAM_AFC_REPORT_APP_ID = "afcCertReportAppId";

    private final InspectionAFCClient inspectionAFCClient;
    private final InsAFCReportService insAFCReportService;
    private final InspectionService inspectionService;
    private final DocSettingService docSettingService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_REVIEW_AFC_REPORT_DTO);
        session.removeAttribute(KEY_COMMON_DOC_DTO);
        session.removeAttribute(PARAM_REPO_ID_DOC_MAP);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_AFC_CERTIFICATION_UPLOAD_REPORT);
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String applicationId = MaskUtil.unMaskValue(PARAM_AFC_REPORT_APP_ID, maskedAppId);
        ParamUtil.setSessionAttr(request,KEY_APP_ID,applicationId);
    }

    @SuppressWarnings("unchecked")
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        //
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos;
        Map<String, CertificationDocDisPlayDto> docDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);
        if(StringUtils.isEmpty(docDtoMap)){
            certificationDocDisPlayDtos = dto.getCertificationDocDisPlayDtos();
            if(certificationDocDisPlayDtos==null){
                certificationDocDisPlayDtos = new ArrayList<>(0);
            }
            for (CertificationDocDisPlayDto certificationDocDisPlayDto : certificationDocDisPlayDtos) {
                certificationDocDisPlayDto.setMaskedRepoId(MaskUtil.maskValue("file", certificationDocDisPlayDto.getRepoId()));
            }
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
            insAFCReportService.setSavedDocMap(dto, request);
        }else {
            certificationDocDisPlayDtos = new ArrayList<>(docDtoMap.values());
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
        }
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_MSG, KEY_AFC_DASHBOARD_MSG);
        ParamUtil.setRequestAttr(request, PARAM_CAN_ACTION_ROLE, RoleConsts.USER_ROLE_BSB_AFC_USER);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.customOptions(docSettingService.getAfcCertificationUploadDocTypeList().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        commonDocDto.reqObjMapping(request,"AFC");
        String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
        String actionType;
        if (actionValue.equals("uploadDoc")) {
            actionType = InspectionConstants.PARAM_PREPARE;
        } else if (actionValue.equals("submitDoc")) {
            actionType = insAFCReportService.validateAFC(request);
        } else {
            actionType = InspectionConstants.PARAM_PREPARE;
        }
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
    }


    public void submitFormData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //do doc sync
        AFCCommonDocDto docDto = insAFCReportService.getAFCCommonDocDto(request);
        AFCSaveDto afcSaveDto = new AFCSaveDto();
        ReviewAFCReportDto displayDto = insAFCReportService.getDisplayDto(request);
        afcSaveDto.setAppId(displayDto.getAppId());
        //set doc
        List<NewFileSyncDto> newFilesToSync = insAFCReportService.saveNewUploadedDoc(docDto);
        //judge is upload new or action on previous uploaded
        boolean uploadNewDoc = false;
        if (!CollectionUtils.isEmpty(docDto.getSavedDocMap())) {
            uploadNewDoc = true;
            afcSaveDto.setNewCertDocDtoList(new ArrayList<>(docDto.getSavedDocMap().values()));
        }
        afcSaveDto.setUploadNewDoc(uploadNewDoc);
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos = new ArrayList<>(0);
        Map<String, CertificationDocDisPlayDto> docDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);
        if(!StringUtils.isEmpty(docDtoMap)){
            certificationDocDisPlayDtos = new ArrayList<>(docDtoMap.values());
        }
        if (!CollectionUtils.isEmpty(certificationDocDisPlayDtos)){
            List<CertificationDocDisPlayDto> needSaveSavedDoc = certificationDocDisPlayDtos.stream().filter(doc -> doc.getRoundOfReview().equals(displayDto.getMaxRound())).filter(doc -> doc.getSubmitterRole().equals(displayDto.getLastUploadRole())).collect(Collectors.toList());
            afcSaveDto.setSavedCertDocDtoList(needSaveSavedDoc);
        }
        inspectionAFCClient.saveAFCAdminInsAFCData(afcSaveDto);
        try {
            // sync docs
            inspectionService.syncNewDocsAndDeleteFiles(newFilesToSync, null);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
        ParamUtil.setRequestAttr(request,KEY_ACK_MSG,"You have successfully submitted the data");
    }


}
