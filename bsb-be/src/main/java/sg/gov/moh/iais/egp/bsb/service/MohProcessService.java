package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.*;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;

/**
 * @author : LiRan
 * @date : 2022/1/26
 */
@Service
@Slf4j
public class MohProcessService {
    private final ProcessClient processClient;

    public MohProcessService(ProcessClient processClient) {
        this.processClient = processClient;
    }

    public DOScreeningDto getDOScreeningDto(HttpServletRequest request, String applicationId){
        DOScreeningDto dto = (DOScreeningDto) ParamUtil.getSessionAttr(request, KEY_DO_SCREENING_DTO);
        if (dto == null){
            dto = processClient.getDOScreeningDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public AOScreeningDto getAOScreeningDto(HttpServletRequest request, String applicationId){
        AOScreeningDto dto = (AOScreeningDto) ParamUtil.getSessionAttr(request, KEY_AO_SCREENING_DTO);
        if (dto == null){
            dto = processClient.getAOScreeningDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public HMScreeningDto getHMScreeningDto(HttpServletRequest request, String applicationId){
        HMScreeningDto dto = (HMScreeningDto) ParamUtil.getSessionAttr(request, KEY_HM_SCREENING_DTO);
        if (dto == null){
            dto = processClient.getHMScreeningDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public DOProcessingDto getDOProcessingDto(HttpServletRequest request, String applicationId){
        DOProcessingDto dto = (DOProcessingDto) ParamUtil.getSessionAttr(request, KEY_DO_PROCESSING_DTO);
        if (dto == null){
            dto = processClient.getDOProcessingDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public AOProcessingDto getAOProcessingDto(HttpServletRequest request, String applicationId){
        AOProcessingDto dto = (AOProcessingDto) ParamUtil.getSessionAttr(request, KEY_AO_PROCESSING_DTO);
        if (dto == null){
            dto = processClient.getAOProcessingDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public void getAndSetDOScreeningDto(HttpServletRequest request, DOScreeningDto doScreeningDto) {
        doScreeningDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        doScreeningDto.setRiskLevel(ParamUtil.getString(request, KEY_RISK_LEVEL));
        doScreeningDto.setRiskLevelComments(ParamUtil.getString(request, KEY_RISK_LEVEL_COMMENTS));
        doScreeningDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        doScreeningDto.setErpReportDate(ParamUtil.getString(request, KEY_ERP_REPORT_DATE));
        doScreeningDto.setRedTeamingReportDate(ParamUtil.getString(request, KEY_RED_TEAMING_REPORT_DATE));
        doScreeningDto.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        doScreeningDto.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        doScreeningDto.setSelectedAfc(ParamUtil.getString(request, KEY_SELECTED_AFC));
        doScreeningDto.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        doScreeningDto.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
    }

    public void getAndSetAOScreeningDto(HttpServletRequest request, AOScreeningDto aoScreeningDto) {
        aoScreeningDto.setAoRemarks(ParamUtil.getString(request, KEY_AO_REMARKS));
        aoScreeningDto.setReviewingDecision(ParamUtil.getString(request, KEY_REVIEWING_DECISION));
        aoScreeningDto.setErpReportDate(ParamUtil.getString(request, KEY_ERP_REPORT_DATE));
        aoScreeningDto.setRedTeamingReportDate(ParamUtil.getString(request, KEY_RED_TEAMING_REPORT_DATE));
        aoScreeningDto.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        aoScreeningDto.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        aoScreeningDto.setSelectedAfc(ParamUtil.getString(request, KEY_SELECTED_AFC));
        aoScreeningDto.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        aoScreeningDto.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        aoScreeningDto.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
    }

    public void getAndSetHMScreeningDto(HttpServletRequest request, HMScreeningDto hmScreeningDto) {
        hmScreeningDto.setHmRemarks(ParamUtil.getString(request, KEY_HM_REMARKS));
        hmScreeningDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
    }

    public void getAndSetDOProcessingDto(HttpServletRequest request, DOProcessingDto doProcessingDto) {
        doProcessingDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        doProcessingDto.setRiskLevel(ParamUtil.getString(request, KEY_RISK_LEVEL));
        doProcessingDto.setRiskLevelComments(ParamUtil.getString(request, KEY_RISK_LEVEL_COMMENTS));
        doProcessingDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        doProcessingDto.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        doProcessingDto.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        doProcessingDto.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        doProcessingDto.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        doProcessingDto.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
    }

    public void getAndSetAOProcessingDto(HttpServletRequest request, AOProcessingDto aoProcessingDto) {
        aoProcessingDto.setAoRemarks(ParamUtil.getString(request, KEY_AO_REMARKS));
        aoProcessingDto.setReviewingDecision(ParamUtil.getString(request, KEY_REVIEWING_DECISION));
        aoProcessingDto.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        aoProcessingDto.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        aoProcessingDto.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        aoProcessingDto.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        aoProcessingDto.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
    }

    /**
     * Convert doc list to map, key is doc repoId, value is docName,
     * for download applicant upload support doc.
     */
    public void setDocDisplayDtoRepoIdNameMapInSession(HttpServletRequest request, List<DocDisplayDto> supportDocDisplayDto){
        Map<String, String> map = new HashMap<>(supportDocDisplayDto.size());
        if (!CollectionUtils.isEmpty(supportDocDisplayDto)){
            for (DocDisplayDto docDisplayDto : supportDocDisplayDto) {
                map.put(docDisplayDto.getFileRepoId(), docDisplayDto.getDocName());
            }
        }
        ParamUtil.setSessionAttr(request, "docDisplayDtoRepoIdNameMap", (Serializable) map);
    }
}
