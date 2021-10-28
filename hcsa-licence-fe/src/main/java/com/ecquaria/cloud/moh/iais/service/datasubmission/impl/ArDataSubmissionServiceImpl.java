package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission) {
        return arFeClient.saveArSuperDataSubmissionDto(arSuperDataSubmission).getEntity();
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

}
