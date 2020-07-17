package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class AdhocChecklistServiceImpl implements AdhocChecklistService {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskApplicationClient taskApplicationClient;

    @Autowired
    private BeEicGatewayClient gatewayClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    private String acquireParameter(String appType, Function<String, String> t){
        return t.apply(appType);
    }

    private static String compareType(String appType){
        if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.AUDIT_INSPECTION);
        } else {
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.INSPECTION);
        }
    }

    private static String compareModule(String appType){
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);
        }else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.RENEWAL);
        }else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.AMENDMENT);
        }else if (ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.REINSTATEMENT);
        }else if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.AUDIT);
        }
        return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);
    }


    @Override
    public List<ChecklistConfigDto> getInspectionChecklist(ApplicationDto application) {
        String appId = application.getId();

        List<AppPremisesCorrelationDto> correlation = taskApplicationClient.getAppPremisesCorrelationsByAppId(appId).getEntity();

        String svcId = application.getServiceId();

        HcsaServiceDto hcsaSvcEntity = hcsaConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
        String svcCode = hcsaSvcEntity.getSvcCode();

        String chklModule = acquireParameter(application.getApplicationType(), AdhocChecklistServiceImpl::compareModule);
        String type = acquireParameter(application.getApplicationType(), AdhocChecklistServiceImpl::compareType);

        log.info(StringUtil.changeForLog("get checklist for pre , module " + chklModule));
        log.info(StringUtil.changeForLog("get checklist for pre , type " + type));

        List<ChecklistConfigDto> inspChecklist = IaisCommonUtils.genNewArrayList();
        boolean oneTime = false;
        for (AppPremisesCorrelationDto corr : correlation){
            String corrId = corr.getId();
            String premId = corr.getAppGrpPremId();

            if (!oneTime){
                String hciCode = "";
                AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getAppGrpPremise(premId).getEntity();
                if (appGrpPremisesEntityDto != null){
                    hciCode  = appGrpPremisesEntityDto.getHciCode();
                }

                ChecklistConfigDto commonConfig = hcsaChklClient.getMaxVersionCommonConfig().getEntity();
                if (commonConfig != null){
                    log.info(StringUtil.changeForLog("inspection checklist for common info: " + commonConfig));
                    inspChecklist.add(commonConfig);
                }

                ChecklistConfigDto svcConfig;
                // issue 65522
                if (StringUtil.isEmpty(hciCode)){
                    svcConfig = hcsaChklClient.getMaxVersionServiceConfigByParams(svcCode, type, chklModule, "", "").getEntity();
                }else {
                    svcConfig = hcsaChklClient.getMaxVersionServiceConfigByParams(svcCode, type, chklModule, "", hciCode).getEntity();
                    if (svcConfig == null){
                        svcConfig = hcsaChklClient.getMaxVersionServiceConfigByParams(svcCode, type, chklModule, "", "").getEntity();
                    }
                }

                log.info(StringUtil.changeForLog("inspection pick up service config service code ====>>>>" + svcCode));
                log.info(StringUtil.changeForLog("inspection pick up service config module ====>>>>" + chklModule));
                log.info(StringUtil.changeForLog("inspection pick up service config type ====>>>>" + type));
                log.info(StringUtil.changeForLog("inspection pick up service config hci code ====>>>>" + hciCode));
                log.info(StringUtil.changeForLog("inspection pick up service config ====>>>>" + svcConfig));

                if (svcConfig != null){
                    inspChecklist.add(svcConfig);
                }

                oneTime = true;
            }

            List<AppSvcPremisesScopeDto> premScope = applicationClient.getAppSvcPremisesScopeListByCorreId(corrId).getEntity();

            if(!IaisCommonUtils.isEmpty(premScope)){
                premScope.stream().filter(scope -> scope.isSubsumedType() == false)
                        .forEach(scope -> {
                            String subTypeId = scope.getScopeName();
                            HcsaServiceSubTypeDto subType = hcsaConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                            String subTypeName = subType.getSubtypeName();
                            ChecklistConfigDto subTypeConfig = hcsaChklClient.getMaxVersionConfigByParams(svcCode, type, chklModule, subTypeName).getEntity();
                            if (subTypeConfig != null){
                                inspChecklist.add(subTypeConfig);
                            }
                        });
            }

        }

        return inspChecklist;
    }

    @Override
    public void saveAdhocChecklist(AdhocCheckListConifgDto adhocConfig) {
        FeignResponseEntity<AdhocCheckListConifgDto> result = applicationClient.saveAdhocChecklist(adhocConfig);
        if (HttpStatus.SC_OK == result.getStatusCode()) {
            AdhocCheckListConifgDto entity = result.getEntity();

            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT,
                    AdhocChecklistServiceImpl.class.getName(),
                    "callEicGatewaySaveItem", currentApp + "-" + currentDomain,
                    AdhocCheckListConifgDto.class.getName(), JsonUtil.parseToJson(entity));

            try {
                FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()){
                    EicRequestTrackingDto preEicRequest = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(preEicRequest.getStatus())){
                        callEicGatewaySaveItem(entity);
                        preEicRequest.setProcessNum(1);
                        Date now = new Date();
                        preEicRequest.setFirstActionAt(now);
                        preEicRequest.setLastActionAt(now);
                        preEicRequest.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                        eicRequestTrackingHelper.getAppEicClient().saveEicTrack(preEicRequest);
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync adhoc item to fe" + e.getMessage()));
            }


        }
    }

    public void callEicGatewaySaveItem(AdhocCheckListConifgDto data) {
        //route to fe
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        gatewayClient.syncAdhocItemData(data, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }
}
