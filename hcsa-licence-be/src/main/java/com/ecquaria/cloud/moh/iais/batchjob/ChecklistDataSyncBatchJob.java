package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareData;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataRequest;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataResponse;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SyncDataBody;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/4/13
 **/

@Delegator("checklistDataSyncBatchJob")
@Slf4j
public class ChecklistDataSyncBatchJob {

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private HcsaChklClient chklClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    /**
     * StartStep: run
     * @param bpc
     * @throws IllegalAccessException
     */
    public void run(BaseProcessClass bpc) throws IllegalAccessException {

        compareSyncRegulation();

    }

    /**
     * StartStep: stop
     * @param bpc
     * @throws IllegalAccessException
     */
    public void stop(BaseProcessClass bpc) throws IllegalAccessException {

    }

    private void compareSyncRegulation(){
        List<HcsaChklSvcRegulationDto> regulationList =  chklClient.getAllRegulation().getEntity();

        BeSyncCompareDataRequest regulationCompare = new BeSyncCompareDataRequest();
        List<BeSyncCompareData> beSyncCompareData = IaisCommonUtils.genNewArrayList();

        regulationList.forEach(reg -> {
            BeSyncCompareData syncData = new BeSyncCompareData();
            syncData.setGuid(reg.getId());
            syncData.setStatus(reg.getStatus());
            beSyncCompareData.add(syncData);
        });

        regulationCompare.setCompareTable(HcsaChecklistConstants.BATCH_JOB_REGULATION_COMPARE_TABLE);
        regulationCompare.setBeSyncCompareData(beSyncCompareData);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        String jsonTest = JsonUtil.parseToJson(regulationCompare);

        List<BeSyncCompareDataResponse> regulationCompareResult = beEicGatewayClient.compareFeData(regulationCompare, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();

        List<String> needSyncData = regulationCompareResult.stream().filter(i -> !i.isBreakOperation())
                .map(BeSyncCompareDataResponse::getGuid)
                .collect(Collectors.toList());

        regulationList = regulationList.stream().filter(i -> needSyncData.contains(i.getId())).collect(Collectors.toList());

        SyncDataBody syncDataBody = new SyncDataBody();
        syncDataBody.setAffectTable(HcsaChecklistConstants.BATCH_JOB_REGULATION_COMPARE_TABLE);
        syncDataBody.setEntity(regulationList);
        beEicGatewayClient.saveSyncData(syncDataBody, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());

    }
}
