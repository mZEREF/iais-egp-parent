package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/4/28
 **/

@Slf4j
public final class BeSelfChecklistHelper {
    private BeSelfChecklistHelper(){}

    public static List<SelfAssessment> receiveSelfAssessmentDataByCorrId(String corrId){
        List<SelfAssessment> rfiData = IaisCommonUtils.genNewArrayList();
        Map<String, SelfAssessmentConfig> selfAssessmentConfigMap = IaisCommonUtils.genNewHashMap();

        ApplicationClient applicationClient = SpringContextHelper.getContext().getBean(ApplicationClient.class);
        HcsaChklClient hcsaChklClient = SpringContextHelper.getContext().getBean(HcsaChklClient.class);
        InspectionTaskClient inspectionTaskClient = SpringContextHelper.getContext().getBean(InspectionTaskClient.class);
        if (applicationClient == null || hcsaChklClient == null || inspectionTaskClient == null){
            log.info("receiveSelfAssessmentRfiByCorrId ==>> ApplicationClient or AppConfigClient is null");
            return rfiData;
        }

        FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> result = applicationClient.getAppPremisesSelfDeclByCorrelationId(corrId);
        if(HttpStatus.SC_OK == result.getStatusCode()){
            List<AppPremisesSelfDeclChklDto> entity = result.getEntity();
            SelfAssessment selfAssessment = new SelfAssessment();
            List<SelfAssessmentConfig> selfAssessmentConfigList = IaisCommonUtils.genNewArrayList();

            FeignResponseEntity<AppGrpPremisesDto> fetchPremisesResult = inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(corrId);
            if (HttpStatus.SC_OK == fetchPremisesResult.getStatusCode()){
                AppGrpPremisesDto appGrpPremises = fetchPremisesResult.getEntity();
                String address = MiscUtil.getAddressForApp(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode()
                        ,appGrpPremises.getAppPremisesOperationalUnitDtos());
                selfAssessment.setPremises(address);
            }

            boolean addedCommon = false;
            for (AppPremisesSelfDeclChklDto ent : entity){

                if (!addedCommon){
                    selfAssessmentConfigList.add(null);
                    addedCommon = true;
                }

                String answerJson = ent.getAnswer();
                List<PremCheckItem> answerData = JsonUtil.parseToList(answerJson, PremCheckItem.class);

                //set unique key
                answerData.forEach(i -> i.setAnswerKey(UUID.randomUUID().toString()));

                String checklistConfigId = ent.getChkLstConfId();
                FeignResponseEntity<ChecklistConfigDto> fetchConfigResult = hcsaChklClient.getChecklistConfigById(checklistConfigId);
                if (HttpStatus.SC_OK == fetchConfigResult.getStatusCode()) {
                    ChecklistConfigDto checklistConfigDto = fetchConfigResult.getEntity();

                    boolean isCommonChecklistConfig = checklistConfigDto.isCommon();
                    if (isCommonChecklistConfig) {
                        SelfAssessmentConfig selfAssessmentConfig = new SelfAssessmentConfig();
                        selfAssessmentConfig.setCommon(true);
                        selfAssessmentConfig.setQuestion(answerData);
                        selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                        selfAssessmentConfig.setVersion(ent.getVersion());

                        selfAssessmentConfigMap.put("common", selfAssessmentConfig);
                        //the first config always common checklist config
                        selfAssessmentConfigList.set(0, selfAssessmentConfig);
                    }else {
                        String svcCode = checklistConfigDto.getSvcCode();
                        HcsaServiceDto serviceInfo = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                        if (serviceInfo != null) {
                            String svcName = serviceInfo.getSvcName();
                            selfAssessment.setSvcName(svcName);
                            selfAssessment.setSvcId(serviceInfo.getId());
                            SelfAssessmentConfig selfAssessmentConfig = selfAssessmentConfigMap.get(svcCode);
                            if ( selfAssessmentConfig != null) {
                                selfAssessmentConfig.getQuestion().addAll(answerData);
                            } else {
                                selfAssessmentConfig = new SelfAssessmentConfig();
                                selfAssessmentConfig.setCommon(false);
                                selfAssessmentConfig.setSvcName(svcName);

                                if (!StringUtils.isEmpty(checklistConfigDto.getSvcSubType())) {
                                    selfAssessmentConfig.setHasSubtype(true);
                                }

                                selfAssessmentConfig.setVersion(ent.getVersion());
                                selfAssessmentConfig.setQuestion(answerData);
                                selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                                selfAssessmentConfigList.add(selfAssessmentConfig);
                                selfAssessmentConfigMap.put(svcCode, selfAssessmentConfig);
                            }
                        }
                    }
                }
            }

            selfAssessment.setSelfAssessmentConfig(selfAssessmentConfigList);
            selfAssessment.setCanEdit(true);
            selfAssessment.setCorrId(corrId);
            rfiData.add(selfAssessment);
        }

        return rfiData;
    }
}
