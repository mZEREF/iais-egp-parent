package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
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
    @Autowired
    private CessationClient cessationClient;

    private final String EFFECTIVEDATAEQUALDATA = "51AD8B3B-E652-EA11-BE7F-000C29F371DC";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) throws Exception {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        List<ApplicationGroupDto> applicationGroupDtos = cessationClient.listAppGrpForCess().getEntity();
        if (applicationGroupDtos != null && !applicationGroupDtos.isEmpty()) {
            for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
                String appGrpId = applicationGroupDto.getId();
                List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                boolean grpLic = applicationDtos.get(0).isGrpLic();
                if (grpLic) {
                    List<ApplicationDto> activeAppDtos = IaisCommonUtils.genNewArrayList();
                    for (ApplicationDto applicationDto : applicationDtos) {
                        String appId = applicationDto.getId();
                        AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                        if (appPremiseMiscDto != null) {
                            Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                            if (effectiveDate.before(date) || effectiveDate.equals(date)) {
                                if (applicationDto.isNeedNewLicNo()) {
                                    activeAppDtos.add(applicationDto);
                                }
                            }
                        }
                    }
                    //create grp licence and cease old grpLicence
                } else {
                    //
                    for (ApplicationDto applicationDto : applicationDtos) {
                        String appId = applicationDto.getId();
                        String originLicenceId = applicationDto.getOriginLicenceId();
                        AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                        if (appPremiseMiscDto != null) {
                            Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                            if (effectiveDate.before(date) || effectiveDate.equals(date)) {
                                LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(originLicenceId).getEntity();
                                updateLicenceStatusAndSendMails(licenceDto,date);
                                String svcName = licenceDto.getSvcName();
                                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                                String svcType = hcsaServiceDto.getSvcType();
                                List<LicenceDto> specLicenceDto = IaisCommonUtils.genNewArrayList();
                                if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                                    List<String> specLicIds = hcsaLicenceClient.getSpecIdsByBaseId(originLicenceId).getEntity();
                                    if (specLicIds != null && !specLicIds.isEmpty()) {
                                        specLicenceDto = hcsaLicenceClient.retrieveLicenceDtos(specLicIds).getEntity();
                                        updateLicencesStatusAndSendMails(specLicenceDto, date);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //get misc corrId
//        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPreCorrDtos(type, dateStr).getEntity();
//        List<String> appIds = IaisCommonUtils.genNewArrayList();
//        List<String> licIds = IaisCommonUtils.genNewArrayList();
//        List<String> hciCodes = IaisCommonUtils.genNewArrayList();
        //get applicationIds
//        if (appPremisesCorrelationDtos != null && !appPremisesCorrelationDtos.isEmpty()) {
//            for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
//                String applicationId = appPremisesCorrelationDto.getApplicationId();
//                String appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
//            }
//        }
        //get licIds
//        List<ApplicationDto> applicationDtos = applicationClient.getApplicationDtosByIds(appIds).getEntity();
//        if (applicationDtos != null && !applicationDtos.isEmpty()) {
//            for (ApplicationDto applicationDto : applicationDtos) {
//                String licenceId = applicationDto.getOriginLicenceId();
//                if (!StringUtil.isEmpty(licenceId)) {
//                    licIds.add(licenceId);
//                }
//            }
//        }
//        //if licId is base licId should ceased specLicId
//        List<LicenceDto> specLicenceDto = IaisCommonUtils.genNewArrayList();
//        for (String licId : licIds) {
//            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
//            boolean grpLic = licenceDto.isGrpLic();
//            if (grpLic) {
//
//            } else {
//                String svcName = licenceDto.getSvcName();
//                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
//                String svcType = hcsaServiceDto.getSvcType();
//                if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
//                    List<String> specLicIds = hcsaLicenceClient.getSpecIdsByBaseId(licId).getEntity();
//                    if (specLicIds != null && !specLicIds.isEmpty()) {
//                        specLicenceDto = hcsaLicenceClient.retrieveLicenceDtos(specLicIds).getEntity();
//                        updateLicencesStatusAndSendMails(specLicenceDto, date);
//                    }
//                }
//            }
//        }
//        //update licence
//        List<LicenceDto> licenceDtoApps = hcsaLicenceClient.retrieveLicenceDtos(licIds).getEntity();
//        updateLicencesStatusAndSendMails(licenceDtoApps, date);
//        licenceDtos1.addAll(specLicenceDto);
//        hcsaLicenceClient.updateLicences(licenceDtos1).getEntity();
//        for (LicenceDto licenceDto : licenceDtoApps) {
//            String svcName = licenceDto.getSvcName();
//            String licenseeId = licenceDto.getLicenseeId();
//            String licenceNo = licenceDto.getLicenceNo();
//            String id = licenceDto.getId();
//            cessationService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
//        }
    }

    private void updateLicencesStatusAndSendMails(List<LicenceDto> licenceDtos, Date date) throws Exception {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
            String svcName = licenceDto.getSvcName();
            String licenseeId = licenceDto.getLicenseeId();
            String licenceNo = licenceDto.getLicenceNo();
            String id = licenceDto.getId();
            cessationService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
        }
        hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
    }

    private void updateLicenceStatusAndSendMails(LicenceDto licenceDto, Date date) throws Exception {
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
        licenceDto.setEndDate(date);
        String svcName = licenceDto.getSvcName();
        String licenseeId = licenceDto.getLicenseeId();
        String licenceNo = licenceDto.getLicenceNo();
        String id = licenceDto.getId();
        List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
        licenceDtos.add(licenceDto);
        hcsaLicenceClient.updateLicences(licenceDtos);
        cessationService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
    }
}
