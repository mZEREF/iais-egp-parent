package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author weilu
 * @Date 2020/4/27 8:27
 */
@Delegator("licenceExpiredBatchJob")
@Slf4j
public class LicenceExpiredBatchJob {
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationBeService cessationBeService;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    private final String LICENCEENDDATE = "52AD8B3B-E652-EA11-BE7F-000C29F371DC";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The licenceExpiredBatchJob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) throws Exception {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String status = ApplicationConsts.LICENCE_STATUS_ACTIVE;
        // get expired date == today de licence
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(status, dateStr).getEntity();
        List<LicenceDto> licenceDtosForSave = IaisCommonUtils.genNewArrayList();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (licenceDtos != null && !licenceDtos.isEmpty()) {
            for (LicenceDto licenceDto : licenceDtos) {
                try {
                    String id = licenceDto.getId();
                    ids.clear();
                    ids.add(id);
                    String svcName = licenceDto.getSvcName();
                    String licenceNo = licenceDto.getLicenceNo();
                    String licenseeId = licenceDto.getLicenseeId();
                    //shi fou you qi ta de app zai zuo
                    Map<String, Boolean> stringBooleanMap = cessationBeService.listResultCeased(ids);
                    if (stringBooleanMap.get(id)) {
                        licenceDtosForSave.add(licenceDto);
                    }
                    updateLicenceStatus(licenceDtosForSave, date);
                    cessationBeService.sendEmail(LICENCEENDDATE, date, svcName, id, licenseeId, licenceNo);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
        }
    }

    private void updateLicenceStatus(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            String licId = licenceDto.getId();
            //pan daun shi dou you xin de licence sheng cheng
            LicenceDto newLicDto = hcsaLicenceClient.getLicdtoByOrgId(licId).getEntity();
            if (newLicDto == null) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_LAPSED);
            } else {
                if(ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(newLicDto.getStatus())){
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_EXPIRY);
                }
            }
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
        }
        List<LicenceDto> entity = hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
        if(!IaisCommonUtils.isEmpty(entity)){
            for(LicenceDto licenceDto : entity){
                String status = licenceDto.getStatus();
                log.info(StringUtil.changeForLog("============="+status+"======================"));
            }
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
    }
}
