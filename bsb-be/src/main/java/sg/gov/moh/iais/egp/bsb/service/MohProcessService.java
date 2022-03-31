package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityActivityInfo;
import sg.gov.moh.iais.egp.bsb.dto.process.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

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

    public MohProcessDto getMohProcessDto(HttpServletRequest request, String applicationId, String routeToRole){
        MohProcessDto dto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        if (dto == null){
            dto = processClient.getMohProcessDtoByAppId(applicationId, routeToRole).getEntity();
        }
        return dto;
    }

    public void getAndSetMohProcessDto(HttpServletRequest request, MohProcessDto mohProcessDto) {
        mohProcessDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        mohProcessDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        mohProcessDto.setInspectionRequired(ParamUtil.getString(request, KEY_INSPECTION_REQUIRED));
        mohProcessDto.setSelectAO(ParamUtil.getString(request, KEY_SELECT_APPROVING_OFFICER));
        List<FacilityActivityInfo> facilityActivityInfoList = mohProcessDto.getFacilityDetailsInfo().getFacilityActivityInfoList();
        for (FacilityActivityInfo facilityActivityInfo : facilityActivityInfoList) {
            String checked = ParamUtil.getString(request, facilityActivityInfo.getId());
            facilityActivityInfo.setStatus(checked);
        }
        List<FacilityBiologicalAgentInfo> facilityBiologicalAgentInfoList = mohProcessDto.getFacilityDetailsInfo().getFacilityBiologicalAgentInfoList();
        for (FacilityBiologicalAgentInfo facilityBiologicalAgentInfo : facilityBiologicalAgentInfoList) {
            String checked = ParamUtil.getString(request, facilityBiologicalAgentInfo.getFacilityBiologicalAgentId());
            facilityBiologicalAgentInfo.setStatus(checked);
        }
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
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
}
