package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CessationService cessationService;

    private final String LICENCEENDDATE = "52AD8B3B-E652-EA11-BE7F-000C29F371DC";

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The licenceExpiredBatchJob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) throws Exception {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String status = ApplicationConsts.LICENCE_STATUS_ACTIVE;
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(status,dateStr).getEntity();
        List<LicenceDto> licenceDtosForSave = IaisCommonUtils.genNewArrayList();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if(licenceDtos!=null&&!licenceDtos.isEmpty()){
            for(LicenceDto licenceDto :licenceDtos){
                String id = licenceDto.getId();
                ids.clear();
                ids.add(id);
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                String licenseeId = licenceDto.getLicenseeId();
                Map<String, Boolean> stringBooleanMap = cessationService.listResultCeased(ids);
                if(stringBooleanMap.get(id)){
                    licenceDtosForSave.add(licenceDto);
                }
                cessationService.sendEmail(LICENCEENDDATE,date,svcName,id,licenseeId,licenceNo);
            }
        }
        List<LicenceDto> licenceDtos2 = updateLicenceStatus(licenceDtosForSave,date);
        hcsaLicenceClient.updateLicences(licenceDtos2).getEntity();
    }

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos,Date date){
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for(LicenceDto licenceDto :licenceDtos){
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
