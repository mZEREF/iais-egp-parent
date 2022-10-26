package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.ProcessDeregistrationClient;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.AOProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.DOProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.HMProcessDto;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.*;

/**
 * @author : LiRan
 * @date : 2022/1/22
 */
@Service
@Slf4j
public class ProcessDeregistrationService {
    private final ProcessDeregistrationClient processDeregistrationClient;

    public ProcessDeregistrationService(ProcessDeregistrationClient processDeregistrationClient) {
        this.processDeregistrationClient = processDeregistrationClient;
    }

    public DOProcessDto getDOProcessDto(HttpServletRequest request, String applicationId){
        DOProcessDto dto = (DOProcessDto) ParamUtil.getSessionAttr(request, KEY_DO_PROCESS_DTO);
        if (dto == null){
            dto = processDeregistrationClient.getDOProcessDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public AOProcessDto getAOProcessDto(HttpServletRequest request, String applicationId){
        AOProcessDto dto = (AOProcessDto) ParamUtil.getSessionAttr(request, KEY_AO_PROCESS_DTO);
        if (dto == null){
            dto = processDeregistrationClient.getAOProcessDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public HMProcessDto getHMProcessDto(HttpServletRequest request, String applicationId){
        HMProcessDto dto = (HMProcessDto) ParamUtil.getSessionAttr(request, KEY_HM_PROCESS_DTO);
        if (dto == null){
            dto = processDeregistrationClient.getHMProcessDataByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public void reqDOProcessDto(HttpServletRequest request, DOProcessDto doProcessDto){
        doProcessDto.setDoRemarks(ParamUtil.getString(request, KEY_DO_REMARKS));
        doProcessDto.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
        doProcessDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        doProcessDto.setReasonForRejection(ParamUtil.getString(request, KEY_REASON_FOR_REJECTION));
    }

    public void reqAOProcessDto(HttpServletRequest request, AOProcessDto aoProcessDto){
        aoProcessDto.setAoRemarks(ParamUtil.getString(request, KEY_AO_REMARKS));
        aoProcessDto.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
        aoProcessDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        aoProcessDto.setReasonForRejection(ParamUtil.getString(request, KEY_REASON_FOR_REJECTION));
    }

    public void reqHMProcessDto(HttpServletRequest request, HMProcessDto hmProcessDto){
        hmProcessDto.setHmRemarks(ParamUtil.getString(request, KEY_HM_REMARKS));
        hmProcessDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
    }
}
