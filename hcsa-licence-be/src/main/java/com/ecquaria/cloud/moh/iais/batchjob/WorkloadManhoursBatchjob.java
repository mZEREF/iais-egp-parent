package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.WorkloadManhoursService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkloadManhoursBatchjob
 *
 * @author Guyin
 * @date 12/02/2019
 */
@Delegator("WorkloadManhoursBatchjob")
@Slf4j
public class WorkloadManhoursBatchjob {
    private static final int ONSITE = 2;
    private static final int OFSITE = 2;
    private static final int CONVEYANCE = 2;

    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private WorkloadManhoursService workloadManhoursService;
    public void doBatchJob(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The workloadEffert is  start..." ));
        String appGroupId = "EF7874FA-CF0D-4B27-B79B-06296CDF56E5";
        ApplicationDto applicationDto = applicationViewService.getApplicaitonByAppNo(appGroupId);
        String serviceId = applicationDto.getServiceId();

        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList = workloadManhoursService.calculateWorkload(serviceId);

        Integer workload = 0;
        for (HcsaSvcSpecificStageWorkloadDto item :hcsaSvcSpecificStageWorkloadDtoList
             ) {
            workload += item.getManhourCount();
        }
        log.debug(StringUtil.changeForLog("The workload is  ..." + workload));
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtoList = new ArrayList<>();
        hcsaSvcSpePremisesTypeDtoList = workloadManhoursService.applicationPremisesByIds(hcsaSvcSpecificStageWorkloadDtoList);
        Integer onsite = 0;
        Integer conveyance = 0;
        Integer offsite = 0;
        for (HcsaSvcSpePremisesTypeDto item: hcsaSvcSpePremisesTypeDtoList
             ) {
            switch (item.getPremisesType()){
                case ApplicationConsts.PREMISES_TYPE_CONVEYANCE:
                    conveyance ++;
                    break;
                case ApplicationConsts.PREMISES_TYPE_ON_SITE:
                    onsite ++;
                    break;
                case ApplicationConsts.PREMISES_TYPE_OFF_SITE:
                    offsite ++;
                    break;
                default:
                        break;
            }
        }
        log.debug(StringUtil.changeForLog("The conveyance is  ..." + conveyance));
        log.debug(StringUtil.changeForLog("The onsite is  ..." + onsite));
        log.debug(StringUtil.changeForLog("The offsite is  ..." + offsite));
        Integer workloadEffert = 0;
        workloadEffert = workload * (conveyance + onsite + offsite) + ONSITE * onsite
                + OFSITE * offsite + CONVEYANCE * conveyance;
        log.debug(StringUtil.changeForLog("The workloadEffert is " + workloadEffert));
    }

}
