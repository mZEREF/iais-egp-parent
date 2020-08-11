package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private CessationBeService cessationBeService;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private LicenceApproveBatchjob licenceApproveBatchjob;
    @Autowired
    private LicenceService licenceService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
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

    private final String EFFECTIVEDATAEQUALDATA = "51AD8B3B-E652-EA11-BE7F-000C29F371DC";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
        List<ApplicationGroupDto> applicationGroupDtos = cessationClient.listAppGrpForCess().getEntity();
        log.info(StringUtil.changeForLog("====================appGrpGtos  size ==================" + applicationGroupDtos.size()));
        List<ApplicationGroupDto> applicationGroupDtosCesead = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(applicationGroupDtos)) {
            for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
                try {
                    String appGrpId = applicationGroupDto.getId();
                    List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                    boolean grpLic = applicationDtos.get(0).isGrpLic();
                    if (grpLic) {
                        Set<String> statusSet = IaisCommonUtils.genNewHashSet();
                        statusSet.clear();
                        for (ApplicationDto applicationDto : applicationDtos) {
                            String status = applicationDto.getStatus();
                            statusSet.add(status);
                        }
                        if (statusSet.size() == 1) {
                            continue;
                        }
                        if (statusSet.size() == 1&&statusSet.contains(ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE)) {
                            String originLicenceId = applicationDtos.get(0).getOriginLicenceId();
                            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                            licenceDtos.add(licenceDto);
                            continue;
                        }
                        for (ApplicationDto applicationDto : applicationDtos) {
                            try {
                                String appId = applicationDto.getId();
                                String status = applicationDto.getStatus();
                                AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                                if (appPremiseMiscDto != null && (ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE.equals(status))) {
                                    Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                    if (effectiveDate.compareTo(date) <= 0) {
                                        String originLicenceId = applicationDto.getOriginLicenceId();
                                        LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                                        if (!licenceDtos.contains(licenceDto)) {
                                            licenceDtos.add(licenceDto);
                                            applicationGroupDtosCesead.add(applicationGroupDto);
                                        }
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                                continue;
                            }
                        }
                        //create grp licence and ceased old licence
                        List<String> grpLicIds = IaisCommonUtils.genNewArrayList();
                        grpLicIds.clear();
                        grpLicIds.add(appGrpId);
                        List<ApplicationLicenceDto> applicationLicenceDtos = applicationClient.getCessGroup(grpLicIds).getEntity();
                        List<String> serviceIds = licenceApproveBatchjob.getAllServiceId(applicationLicenceDtos);
                        List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
                        if (hcsaServiceDtos == null || hcsaServiceDtos.size() == 0) {
                            log.error(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:" + serviceIds));
                            return;
                        }
                        for (ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos) {
                            List<ApplicationListDto> newApplicationListDtoLists = IaisCommonUtils.genNewArrayList();
                            List<ApplicationListDto> oldApplicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                            for (ApplicationListDto applicationListDto : oldApplicationListDtoList) {
                                ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                                if (applicationDto.isNeedNewLicNo()) {
                                    newApplicationListDtoLists.add(applicationListDto);
                                }
                            }
                            log.info(StringUtil.changeForLog("====================newApplicationListDtoLists  size==================" + newApplicationListDtoLists.size()));
                            applicationLicenceDto.setApplicationListDtoList(newApplicationListDtoLists);
                            LicenceApproveBatchjob.GenerateResult groupGenerateResult = licenceApproveBatchjob.generateGroupLicence(applicationLicenceDto, hcsaServiceDtos);
                            licenceApproveBatchjob.createCessLicence(applicationGroupDto, null, groupGenerateResult);
                        }
                    } else {
                        for (ApplicationDto applicationDto : applicationDtos) {
                            String appId = applicationDto.getId();
                            String originLicenceId = applicationDto.getOriginLicenceId();
                            AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                            if (appPremiseMiscDto != null) {
                                Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                if (effectiveDate.compareTo(date) <= 0) {
                                    LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                                    String status = licenceDto.getStatus();
                                    if (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status)) {
                                        applicationGroupDtosCesead.add(applicationGroupDto);
                                        licenceDtos.add(licenceDto);
                                        updateLicenceStatusAndSendMails(licenceDto, date);
                                        String svcName = licenceDto.getSvcName();
                                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                                        String svcType = hcsaServiceDto.getSvcType();
                                        List<LicenceDto> specLicenceDto = null;
                                        if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)) {
                                            List<String> specLicIds = hcsaLicenceClient.getSpecIdsByBaseId(originLicenceId).getEntity();
                                            if (specLicIds != null && !specLicIds.isEmpty()) {
                                                specLicenceDto = hcsaLicenceClient.retrieveLicenceDtos(specLicIds).getEntity();
                                                updateLicencesStatusAndSendMails(specLicenceDto, date);
                                            }
                                        }
                                        if (!IaisCommonUtils.isEmpty(specLicenceDto)) {
                                            licenceDtos.addAll(specLicenceDto);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
            try {
                //update and send email
                log.info(StringUtil.changeForLog("====================licenceDtos  size==================" + licenceDtos.size()));
                if (!IaisCommonUtils.isEmpty(licenceDtos)) {
                    for (LicenceDto licenceDto : licenceDtos) {
                        String id = licenceDto.getId();
                        log.info(StringUtil.changeForLog("====================licence id ==================" + id));
                    }
                }
                updateLicencesStatusAndSendMails(licenceDtos, date);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            //update appGrp
            if (!IaisCommonUtils.isEmpty(applicationGroupDtosCesead)) {
                for (ApplicationGroupDto applicationGroupDto : applicationGroupDtosCesead) {
                    String id = applicationGroupDto.getId();
                    log.info(StringUtil.changeForLog("====================licence id ==================" + id));
                }
            }
            updateAppGroups(applicationGroupDtosCesead);
        }
    }

    private void updateLicencesStatusAndSendMails(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for (LicenceDto licenceDto : licenceDtos) {
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
            String svcName = licenceDto.getSvcName();
            String licenseeId = licenceDto.getLicenseeId();
            String licenceNo = licenceDto.getLicenceNo();
            String id = licenceDto.getId();
            try {
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
                String applicantName=licenseeDto.getName();
                emailMap.put("ApplicantName", applicantName);
                emailMap.put("ApplicationType", "");
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("ApplicationNumber", licenceNo);
                emailMap.put("ApplicationDate", Formatter.formatDateTime(date, AppConsts.DEFAULT_DATE_FORMAT));
                emailMap.put("email", "");
                emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                notificationHelper.sendNotification(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE, emailMap, licenceNo, licenceNo,
                        NotificationHelper.RECEIPT_TYPE_APP, licenseeId);
                //cessationBeService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (!IaisCommonUtils.isEmpty(updateLicenceDtos)) {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            gatewayClient.updateFeLicDto(updateLicenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }
    }

    private void updateLicenceStatusAndSendMails(LicenceDto licenceDto, Date date) {
        if (licenceDto == null) {
            return;
        }
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
        licenceDto.setEndDate(date);
        String svcName = licenceDto.getSvcName();
        String licenseeId = licenceDto.getLicenseeId();
        String licenceNo = licenceDto.getLicenceNo();
        String id = licenceDto.getId();
        List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
        licenceDtos.add(licenceDto);
        hcsaLicenceClient.updateLicences(licenceDtos);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        try {
            gatewayClient.updateFeLicDto(licenceDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
            String applicantName=licenseeDto.getName();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", "");
            emailMap.put("ServiceLicenceName", svcName);
            emailMap.put("ApplicationNumber", licenceNo);
            emailMap.put("ApplicationDate", Formatter.formatDateTime(date, AppConsts.DEFAULT_DATE_FORMAT));
            emailMap.put("email", "");
            emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
            notificationHelper.sendNotification(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE, emailMap, licenceNo, licenceNo,
                    NotificationHelper.RECEIPT_TYPE_APP, licenseeId);
            //cessationBeService.sendEmail(EFFECTIVEDATAEQUALDATA, date, svcName, id, licenseeId, licenceNo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateAppGroups(List<ApplicationGroupDto> applicationGroupDtos) {
        if (IaisCommonUtils.isEmpty(applicationGroupDtos)) {
            return;
        }
        for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
        }
        applicationClient.updateApplications(applicationGroupDtos);
    }
}
