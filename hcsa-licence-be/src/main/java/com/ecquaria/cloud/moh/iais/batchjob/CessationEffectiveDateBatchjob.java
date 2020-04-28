package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2020/2/13 9:42
 * Check for Cessation Effective Date  when cessation date == new date make licence inactive
 */
@Delegator("CessationEffectiveDateBatchjob")
@Slf4j
public class CessationEffectiveDateBatchjob {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationService cessationService;

    private final String EFFECTIVEDATAEQUALDATA = "51AD8B3B-E652-EA11-BE7F-000C29F371DC";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) throws IOException, TemplateException {

        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String type = ApplicationConsts.CESSATION_TYPE_APPLICATION;
        //get misc corrId
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPreCorrDtos(type, dateStr).getEntity();
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        //get applicationIds
        if (appPremisesCorrelationDtos != null && !appPremisesCorrelationDtos.isEmpty()) {
            for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
                String applicationId = appPremisesCorrelationDto.getApplicationId();
                appIds.add(applicationId);
            }
        }
        //get licIds
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationDtosByIds(appIds).getEntity();
        if (applicationDtos != null && !applicationDtos.isEmpty()) {
            for (ApplicationDto applicationDto : applicationDtos) {
                String licenceId = applicationDto.getOriginLicenceId();
                if (!StringUtil.isEmpty(licenceId)) {
                    licIds.add(licenceId);
                }
            }
        }
        //if licId is base licId should ceased specLicId
        List<LicenceDto> specLicenceDto = IaisCommonUtils.genNewArrayList();
        for (String licId : licIds) {
            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
            boolean grpLic = licenceDto.isGrpLic();
            if(grpLic){

            }else {
                String svcName = licenceDto.getSvcName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                String svcType = hcsaServiceDto.getSvcType();
                if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                    List<String> specLicIds = hcsaLicenceClient.getSpecIdsByBaseId(licId).getEntity();
                    if (specLicIds != null && !specLicIds.isEmpty()) {
                        specLicenceDto = hcsaLicenceClient.retrieveLicenceDtos(specLicIds).getEntity();
                        updateLicenceStatus(specLicenceDto, date);
                    }
                }
            }
        }
        //update licence
        List<LicenceDto> licenceDtoApps = hcsaLicenceClient.retrieveLicenceDtos(licIds).getEntity();
        List<LicenceDto> licenceDtos1 = updateLicenceStatus(licenceDtoApps, date);
        licenceDtos1.addAll(specLicenceDto);
        hcsaLicenceClient.updateLicences(licenceDtos1).getEntity();
        for (LicenceDto licenceDto : licenceDtoApps) {
            String svcName = licenceDto.getSvcName();
            String licenseeId = licenceDto.getLicenseeId();
            String licenceNo = licenceDto.getLicenceNo();
            String id = licenceDto.getId();
            cessationService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
        }
    }

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
