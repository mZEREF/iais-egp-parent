package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.client.LicCommClient;
import com.ecquaria.cloud.moh.iais.util.PageDataCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Auther chenlei on 5/3/2022.
 */
@Slf4j
@Service
public class LicCommServiceImpl implements LicCommService {

    @Autowired
    private LicCommClient licCommClient;

    @Autowired
    private ConfigCommService configCommService;

    @Autowired
    private AppCommService appCommService;

    @Override
    public LicenceDto getActiveLicenceById(String licenceId) {
        log.info(StringUtil.changeForLog("Licence Id: " + licenceId));
        if (StringUtil.isEmpty(licenceId)) {
            return null;
        }
        return licCommClient.getActiveLicenceById(licenceId).getEntity();
    }

    @Override
    public List<LicenceDto> getLicenceDtoByHciCode(String hciCode, String licenseeId) {
        log.info(StringUtil.changeForLog("Hci code: " + hciCode + " - Licensee: " + licenseeId));
        if (StringUtil.isEmpty(hciCode) || StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        return licCommClient.getLicenceDtoByHciCode(hciCode, licenseeId).getEntity();
    }

    @Override
    public List<LicenceDto> getLicenceDtoByHciCode(String licenseeId, AppGrpPremisesDto appGrpPremisesDto, String... excludeNos) {
        if (StringUtil.isEmpty(licenseeId) || appGrpPremisesDto == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        String hciCode = appGrpPremisesDto.getHciCode();
        String oldHciCode = appGrpPremisesDto.getOldHciCode();
        if (!StringUtil.isEmpty(oldHciCode) && !oldHciCode.equals(hciCode)) {
            hciCode = oldHciCode;
        }
        List<LicenceDto> licenceDtos = getLicenceDtoByHciCode(hciCode, licenseeId);
        if (licenceDtos == null || licenceDtos.isEmpty()) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        return licenceDtos.stream()
                .filter(dto -> !StringUtil.isIn(dto.getLicenceNo(), excludeNos))
                .map(licenceDto -> {
                    log.info(StringUtil.changeForLog("--- licenceDto licenceNo : " + licenceDto.getLicenceNo()));
                    return licenceDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        log.info(StringUtil.changeForLog("Licence Id: " + licenceId));
        if (StringUtil.isEmpty(licenceId)) {
            return null;
        }
        return licCommClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public AppSubmissionDto viewAppSubmissionDto(String licenceId) {
        log.info(StringUtil.changeForLog("Licence Id: " + licenceId));
        if (StringUtil.isEmpty(licenceId)) {
            return null;
        }
        return licCommClient.viewAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtosByLicenceIds(List<String> licenceIds) {
        log.info(StringUtil.changeForLog("Licence Ids: " + licenceIds));
        if (IaisCommonUtils.isEmpty(licenceIds)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licCommClient.getAppSubmissionDtosByLicenceIds(licenceIds).getEntity();
    }

    @Override
    public List<GiroAccountInfoDto> getGiroAccountsByLicIds(List<String> licIds) {
        log.info(StringUtil.changeForLog("Licence Ids: " + licIds));
        return licCommClient.getGiroAccountsByLicIds(licIds).getEntity();
    }

    @Override
    public List<LicBaseSpecifiedCorrelationDto> getLicBaseSpecifiedCorrelationDtos(String svcType, String originLicenceId) {
        log.info(StringUtil.changeForLog("Params: " + svcType + " - " + originLicenceId));
        if (StringUtil.isEmpty(svcType) || StringUtil.isEmpty(originLicenceId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licCommClient.getLicBaseSpecifiedCorrelationDtos(svcType, originLicenceId).getEntity();
    }

    @Override
    public boolean baseSpecLicenceRelation(LicenceDto licenceDto) {
        return Boolean.parseBoolean(baseSpecLicenceRelation(licenceDto, true));
    }

    @Override
    public String baseSpecLicenceRelation(LicenceDto licenceDto, boolean flag) {
        String svcName = licenceDto.getSvcName();
        HcsaServiceDto activeHcsaServiceDtoByName = configCommService.getActiveHcsaServiceDtoByName(svcName);
        if (activeHcsaServiceDtoByName != null) {
            String svcType = activeHcsaServiceDtoByName.getSvcType();
            log.info(StringUtil.changeForLog("The Svc Type: " + svcType));
            if (ApplicationConsts.SERVICE_TYPE_BASE.equals(svcType)) {
                return flag == true ? String.valueOf(true) : activeHcsaServiceDtoByName.getId();
            } else if (ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)) {
                List<HcsaServiceCorrelationDto> serviceCorrelationDtos = configCommService.getActiveSvcCorrelation();
                if (serviceCorrelationDtos == null || serviceCorrelationDtos.isEmpty()) {
                    log.info(StringUtil.changeForLog("The service correlations is empty!"));
                    return flag == true ? String.valueOf(false) : "";
                }
                String baseService = "";
                Iterator<HcsaServiceCorrelationDto> iterator = serviceCorrelationDtos.iterator();
                while (iterator.hasNext()) {
                    HcsaServiceCorrelationDto next = iterator.next();
                    if (next.getSpecifiedSvcId().equals(activeHcsaServiceDtoByName.getId())) {
                        baseService = next.getBaseSvcId();
                        break;
                    }
                }
                if (StringUtil.isEmpty(baseService)) {
                    log.info(StringUtil.changeForLog("The base service is empty!"));
                    return flag == true ? String.valueOf(false) : "";
                }

                List<LicBaseSpecifiedCorrelationDto> entity = getLicBaseSpecifiedCorrelationDtos(
                        ApplicationConsts.SERVICE_TYPE_SPECIFIED, licenceDto.getId());
                if (entity == null || entity.isEmpty()) {
                    log.info(StringUtil.changeForLog("The related base service is empty!"));
                    return flag == true ? String.valueOf(false) : "";
                }
                Iterator<LicBaseSpecifiedCorrelationDto> iterator1 = entity.iterator();
                while (iterator1.hasNext()) {
                    LicBaseSpecifiedCorrelationDto next = iterator1.next();
                    if (next.getSpecLicId().equals(licenceDto.getId())) {
                        String baseLicId = next.getBaseLicId();
                        LicenceDto licenceDto1 = getActiveLicenceById(baseLicId);
                        if (licenceDto1 == null) {
                            log.info(StringUtil.changeForLog("The base Licence is empty!"));
                            return flag == true ? String.valueOf(false) : "";
                        }
                        String svcname = configCommService.getServiceNameById(baseService);
                        if (Objects.equals(svcname, licenceDto1.getSvcName())) {
                            return flag == true ? String.valueOf(true) : baseService;
                        }
                    }
                }

            }
        }
        log.info("The baseSpecLicenceRelation end!");
        return flag == true ? String.valueOf(false) : "";
    }

    @Override
    public List<AppGrpPremisesDto> getLicencePremisesDtoList(String licenseeId) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = licCommClient.getDistinctPremisesByLicenseeId(licenseeId, "").getEntity();
        if (appGrpPremisesDtos == null || appGrpPremisesDtos.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        return StreamSupport.stream(appGrpPremisesDtos.spliterator(), appGrpPremisesDtos.size() > 4)
                .map(appGrpPremisesDto -> {
                    ApplicationHelper.setWrkTime(appGrpPremisesDto);
                    appGrpPremisesDto.setExistingData(AppConsts.YES);
                    appGrpPremisesDto.setFromDB(true);
                    return appGrpPremisesDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PremisesDto> getPremisesDtoByHciNameAndPremType(String hciName, String premisesType, String licenseeId) {
        log.info(StringUtil.changeForLog("Params: " + hciName + " | " + premisesType + " | " + licenseeId));
        if (StringUtil.isEmpty(licenseeId) || StringUtil.isEmpty(hciName) || StringUtil.isEmpty(premisesType)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licCommClient.getPremisesDtoByHciNameAndPremType(hciName, premisesType, licenseeId).getEntity();
    }

    @Override
    public Boolean getOtherLicseePremises(CheckCoLocationDto checkCoLocationDto) {
        log.info(StringUtil.changeForLog("Params: " + JsonUtil.parseToJson(checkCoLocationDto)));
        if (checkCoLocationDto == null) {
            return false;
        }
        return licCommClient.getOtherLicseePremises(checkCoLocationDto).getEntity();
    }

    @Override
    public List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId) {
        log.info(StringUtil.changeForLog("licenseeId is " + licenseeId));
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licCommClient.getPersonnel(licenseeId).getEntity();
    }

    @Override
    public List<SubLicenseeDto> getIndividualSubLicensees(String orgId) {
        log.info(StringUtil.changeForLog("OrgId is " + orgId));
        if (StringUtil.isEmpty(orgId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licCommClient.getIndividualSubLicensees(orgId).getEntity();
    }

    @Override
    public List<PremisesDto> getPremisesByLicseeIdAndSvcName(String licenseeId, List<String> svcNames) {
        return licCommClient.getPremisesByLicseeIdAndSvcName(licenseeId, svcNames).getEntity();
    }

    @Override
    public List<LicAppCorrelationDto> getInactiveLicAppCorrelations() {
        return licCommClient.getInactiveLicAppCorrelations().getEntity();
    }

    @Override
    public List<AppSubmissionDto> getAlginAppSubmissionDtos(String licenceId, boolean checkSpec) {
        log.info(StringUtil.changeForLog("--------- Licence Id: " + licenceId + " : " + checkSpec + " --------"));
        if (StringUtil.isEmpty(licenceId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licCommClient.getAlginAppSubmissionDtos(licenceId, checkSpec).getEntity();
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtosBySubLicensee(SubLicenseeDto subLicenseeDto) {
        if (subLicenseeDto == null) {
            return null;
        }
        return licCommClient.getAppSubmissionDtosBySubLicensee(subLicenseeDto).getEntity();
    }

    /**
     * Check personnel affected data
     * check -
     * 0: only check changed;
     * 1: check changed and retrieve affected data;
     * 2: check changed, retrieve affected data and reset them.
     *
     * @return
     */
    @Override
    public List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto, int check) throws Exception {
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if (appSvcRelatedInfoDto == null || oldAppSvcRelatedInfoDto == null) {
            return appSubmissionDtoList;
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = PageDataCopyUtil.copyAppSvcCgo(appSvcRelatedInfoDto.getAppSvcCgoDtoList());
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList =
                PageDataCopyUtil.copyMedaler(appSvcRelatedInfoDto.getAppSvcMedAlertPersonList());
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList =
                PageDataCopyUtil.copyAppSvcPo(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList());
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList =
                PageDataCopyUtil.copyAppSvcClinicalDirector(appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList());

        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList =
                PageDataCopyUtil.copyAppSvcCgo(oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList =
                PageDataCopyUtil.copyMedaler(oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList =
                PageDataCopyUtil.copyAppSvcPo(oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList =
                PageDataCopyUtil.copyAppSvcClinicalDirector(oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList());

        List<AppSvcPrincipalOfficersDto> kahList =
                PageDataCopyUtil.copyAppSvcKah(appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList());
        List<AppSvcPrincipalOfficersDto> oldKahList =
                PageDataCopyUtil.copyAppSvcKah(oldAppSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList());

        Set<String> set = IaisCommonUtils.genNewHashSet();
        List<String> list = IaisCommonUtils.genNewArrayList();
        List<String> list1 = RfcHelper.checkKeyPersonnelChanged(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        List<String> list2 = RfcHelper.checkKeyPersonnelChanged(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        List<String> list3 = RfcHelper.checkKeyPersonnelChanged(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);
        List<String> list4 = RfcHelper.checkKeyPersonnelChanged(appSvcClinicalDirectorDtoList, oldAppSvcClinicalDirectorDtoList);
        List<String> list5 = RfcHelper.checkKeyPersonnelChanged(kahList, oldKahList);

        List<String> currEditList = IaisCommonUtils.genNewArrayList();
        if (!list1.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            set.addAll(list1);
        } else if (RfcHelper.isChanged(appSvcCgoDtoList, oldAppSvcCgoDtoList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        }
        if (!list2.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
            set.addAll(list2);
        } else if (RfcHelper.isChanged(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        }
        if (!list3.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
            set.addAll(list3);
        } else if (RfcHelper.isChanged(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        }
        if (!list4.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
            set.addAll(list4);
        } else if (RfcHelper.isChanged(appSvcClinicalDirectorDtoList, oldAppSvcClinicalDirectorDtoList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        }
        if (!list5.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_KAH);
            set.addAll(list5);
        } else if (RfcHelper.isChanged(kahList, oldKahList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_KAH);
        }
        List<String> changeList = appSubmissionDto.getChangeSelectDto().getPersonnelEditList();
        if (changeList == null) {
            changeList = IaisCommonUtils.genNewArrayList();
        }
        changeList.addAll(currEditList);
        appSubmissionDto.getChangeSelectDto().setPersonnelEditList(changeList);
        if (check == 0) {
            return IaisCommonUtils.genNewArrayList();
        }
        list.addAll(set);
        List<LicKeyPersonnelDto> licKeyPersonnelDtos = IaisCommonUtils.genNewArrayList();
        for (String string : list) {
            List<String> personnelDtoByIdNo = getPersonnelIdsByIdNo(string);
            List<LicKeyPersonnelDto> licKeyPersonnelDtoByPerId = getLicKeyPersonnelDtoByPersonId(
                    personnelDtoByIdNo);
            licKeyPersonnelDtos.addAll(licKeyPersonnelDtoByPerId);
        }

        Set<String> licenceId = IaisCommonUtils.genNewHashSet();
        List<String> licenceIdList = IaisCommonUtils.genNewArrayList();

        for (LicKeyPersonnelDto licKeyPersonnelDto : licKeyPersonnelDtos) {
            if (licenseeId.equals(licKeyPersonnelDto.getLicenseeId())) {
                licenceId.add(licKeyPersonnelDto.getLicenceId());
            }
        }
        licenceIdList.addAll(licenceId);
        licenceIdList.remove(appSubmissionDto.getLicenceId());
        for (String string : licenceIdList) {
            AppSubmissionDto appSubmissionDtoByLicenceId = getAppSubmissionDtoByLicenceId(string);
            if (appSubmissionDtoByLicenceId == null || appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList() == null) {
                continue;
            }
            log.info(StringUtil.changeForLog("The affected licence: " + appSubmissionDtoByLicenceId.getLicenceNo()));
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setServiceEdit(true);
            List<String> personnelEditList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0);
            if (!list1.isEmpty()) {
                RfcHelper.reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, personnelEditList);
            }
            if (!list2.isEmpty()) {
                RfcHelper.reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, personnelEditList);
            }
            if (!list3.isEmpty()) {
                RfcHelper.reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO, personnelEditList);
            }
            if (!list4.isEmpty()) {
                RfcHelper.reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, personnelEditList);
            }
            // KAH
            if (!list5.isEmpty()) {
                RfcHelper.reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_KAH, personnelEditList);
            }
            appEditSelectDto.setPersonnelEditList(personnelEditList);
            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
            if (check == 2) {
                appSubmissionDtoByLicenceId.setPartPremise(false);
                appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
                RfcHelper.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                appCommService.transform(appSubmissionDtoByLicenceId, appSubmissionDto.getLicenseeId(),
                        ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, false);
                RfcHelper.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
                appSubmissionDtoByLicenceId.setAutoRfc(true);
                appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
                ApplicationHelper.reSetAdditionalFields(appSubmissionDtoByLicenceId, appEditSelectDto);
            }
            appSubmissionDtoList.add(appSubmissionDtoByLicenceId);
        }
        return appSubmissionDtoList;
    }

    private List<String> getPersonnelIdsByIdNo(String idNo) {
        return licCommClient.getPersonnelDtoByIdNo(idNo).getEntity();
    }

    private List<LicKeyPersonnelDto> getLicKeyPersonnelDtoByPersonId(List<String> personIds) {
        return licCommClient.getLicKeyPersonnelDtoByPersonId(personIds).getEntity();
    }

    @Override
    public List<AppGrpPremisesDto> getLicPremisesInfo(String id) {
        return licCommClient.getLicPremisesById(id).getEntity();
    }

}
