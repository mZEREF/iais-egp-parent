package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityActivityInfo;
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

    public MohProcessDto getMohProcessDto(HttpServletRequest request, String applicationId, String moduleName){
        MohProcessDto dto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        if (dto == null){
            dto = processClient.getMohProcessDtoByAppId(applicationId, moduleName).getEntity();
        }
        return dto;
    }

    public void getAndSetMohProcessDto(HttpServletRequest request, MohProcessDto mohProcessDto) {
        mohProcessDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        mohProcessDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        mohProcessDto.setInspectionRequired(ParamUtil.getString(request, KEY_INSPECTION_REQUIRED));
        mohProcessDto.setCertificationRequired(ParamUtil.getString(request, KEY_CERTIFICATION_REQUIRED));
        mohProcessDto.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
        List<FacilityActivityInfo> facilityActivityInfoList = mohProcessDto.getFacilityDetailsInfo().getFacilityActivityInfoList();
        for (FacilityActivityInfo facilityActivityInfo : facilityActivityInfoList) {
            String checked = ParamUtil.getString(request, facilityActivityInfo.getId());
            facilityActivityInfo.setStatus(checked);
        }
        List<FacilityBiologicalAgentInfo> facilityBiologicalAgentInfoList = mohProcessDto.getFacilityDetailsInfo().getFacilityBiologicalAgentInfoList();
        for (FacilityBiologicalAgentInfo facilityBiologicalAgentInfo : facilityBiologicalAgentInfoList) {
            String checked = ParamUtil.getString(request, facilityBiologicalAgentInfo.getId());
            facilityBiologicalAgentInfo.setStatus(checked);
        }
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
    }
}
