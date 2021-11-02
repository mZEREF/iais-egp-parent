package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description ArDataSubmissionServiceImpl
 * @Auther chenlei on 10/22/2021.
 */
@Service
@Slf4j
public class ArDataSubmissionServiceImpl implements ArDataSubmissionService {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private ArFeClient arFeClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Override
    public Map<String, AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName) {
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewHashMap();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtos = licenceClient.getDistinctPremisesByLicenseeId(licenseeId,
                serviceName).getEntity();
        Map<String, AppGrpPremisesDto> appGrpPremisesDtoMap = IaisCommonUtils.genNewHashMap();
        if (appGrpPremisesDtos == null || appGrpPremisesDtos.isEmpty()) {
            return appGrpPremisesDtoMap;
        }
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
            if (!StringUtil.isEmpty(appGrpPremisesDto.getPremisesSelect())) {
                NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                appGrpPremisesDto.setExistingData(AppConsts.YES);
                appGrpPremisesDtoMap.put(appGrpPremisesDto.getPremisesSelect(), appGrpPremisesDto);
            }
        }
        return appGrpPremisesDtoMap;
    }

    @Override
    public CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return arFeClient.getCycleStageSelectionDtoByConds(idType, idNumber, nationality, orgId, hciCode).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode, String hciCOde) {
        if (StringUtil.isEmpty(patientCode)) {
            log.warn("----- No Patient Code -----");
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDto(patientCode, hciCOde).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDto ..."));
        return arFeClient.saveArSuperDataSubmissionDto(arSuperDataSubmission).getEntity();
    }


    @Override
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDtoToBE(ArSuperDataSubmissionDto arSuperDataSubmission) {
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE start ..."));
        arSuperDataSubmission.setFe(false);
        arSuperDataSubmission = saveBeArSuperDataSubmissionDto(arSuperDataSubmission);

        DataSubmissionDto dataSubmission =  arSuperDataSubmission.getCurrentDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE refNo is -->:"+refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if(eicRequestTrackingDto != null){
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum()+1);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
        }else{
            log.warn(StringUtil.changeForLog(" do not have the eicRequestTrackingDto for this  refNo -->:"+refNo));
        }
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE end ..."));
        return arSuperDataSubmission;
    }

    public ArSuperDataSubmissionDto saveBeArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission) {
        return feEicGatewayClient.saveBeArSuperDataSubmissionDto(arSuperDataSubmission).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto saveDataSubmissionDraft(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        return arFeClient.doUpdateDataSubmissionDraft(arSuperDataSubmissionDto).getEntity();
    }

    @Override
    public String getSubmissionNo(String submisisonType, String cycleStage, DataSubmissionDto lastDataSubmissionDto) {
        String submissionNo = null;
        int serialNo = 0;
        if (!DataSubmissionConsts.DATA_SUBMISSION_CYCLE_STAGE_PATIENT.equals(cycleStage)) {
            if (lastDataSubmissionDto != null
                    && submisisonType.equals(lastDataSubmissionDto.getSubmissionType())
                    && !IaisCommonUtils.getDsFinalStatus().contains(lastDataSubmissionDto.getStatus())
                    && lastDataSubmissionDto.getSubmissionNo() != null) {
                String[] previous = lastDataSubmissionDto.getSubmissionNo().split("-");
                submissionNo = previous[0];
                serialNo = Integer.parseInt(previous[1]);
            }
            serialNo++;
        }
        if (StringUtil.isEmpty(submissionNo)) {
            submissionNo = systemAdminClient.submissionID(submisisonType).getEntity();
        }
        if (serialNo != 0) {
            submissionNo += "-" + Formatter.formatNumber(serialNo, "00");
        }
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public ArSubFreezingStageDto setFreeCryoNumAndDate(ArSubFreezingStageDto arSubFreezingStageDto, String cryopreservedNum, String cryopreservationDate) {
        if(!StringUtil.isEmpty(cryopreservedNum)) {
            try {
                int cryopreservedNo = Integer.parseInt(cryopreservedNum);
                arSubFreezingStageDto.setCryopreservedNum(cryopreservedNo);
            } catch (Exception e) {
                log.info("Freezing invalid cryopreservedNum");
            }
        }
        if(!StringUtil.isEmpty(cryopreservationDate)) {
            try {
                Date date = Formatter.parseDateTime(cryopreservationDate, AppConsts.DEFAULT_DATE_TIME_FORMAT);
                arSubFreezingStageDto.setCryopreservedDate(date);
            } catch (ParseException e) {
                log.info("Freezing invalid cryopreservationDate");
            }
        }
        return arSubFreezingStageDto;
    }

    @Override
    public ArSubFreezingStageDto checkValueIsDirtyData(String freeCryoRadio, ArSubFreezingStageDto arSubFreezingStageDto, List<SelectOption> freeCryoOptions) {
        if(!IaisCommonUtils.isEmpty(freeCryoOptions)) {
            boolean codeValueFlag = false;
            for(SelectOption selectOption : freeCryoOptions) {
                if(selectOption != null) {
                    String codeValue = selectOption.getValue();
                    if(!StringUtil.isEmpty(codeValue) && codeValue.equals(freeCryoRadio)) {
                        codeValueFlag = true;
                        break;
                    }
                }
            }
            if(codeValueFlag) {
                arSubFreezingStageDto.setCryopreservedType(freeCryoRadio);
            } else {
                arSubFreezingStageDto.setCryopreservedType(null);
            }
        }
        return arSubFreezingStageDto;
    }

}
