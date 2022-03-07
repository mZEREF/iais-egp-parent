package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.RequestForChangeMenuDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.rfcutil.PageDataCopyUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceInfoChangeEffectPerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2021/4/27 15:48
 */
@Slf4j
@Component
public class ServiceInfoChangeEffectPersonAbstract implements ServiceInfoChangeEffectPerson {

    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;

    /**
     * Generate the svc related info with the auto fields changed
     *
     * @param appSubmissionDto
     * @param oldAppSubmissionDto
     * @param changeList:         The changed personnel types.
     *                            Refer to the method - EqRequestForChangeSubmitResultChange.eqServiceChange and personContact
     * @param stepList:           The steps which is changed, it will contains not-auto fields and maybe it contains auto fields.
     *                            Refer to the method - EqRequestForChangeSubmitResultChange.compareNotChangePersonnel
     *                            && EqRequestForChangeSubmitResultChange.rfcChangeModuleEvaluationDto
     * @return The svc related info with the auto fields changed
     */
    public List<AppSvcRelatedInfoDto> generateDtosForAutoFields(AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto, List<String> changeList, List<String> stepList) {
        AppSvcRelatedInfoDto oldSvcInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto currSvcInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if (currSvcInfoDto == null || oldSvcInfoDto == null) {
            return null;
        }
        if (stepList == null || stepList.isEmpty()) {
            return appSubmissionDto.getAppSvcRelatedInfoDtoList();
        }
        if (changeList == null) {
            changeList = IaisCommonUtils.genNewArrayList();
        }
        AppSvcRelatedInfoDto newDto = (AppSvcRelatedInfoDto) CopyUtil.copyMutableObject(currSvcInfoDto);
        for (String step : stepList) {
            if (HcsaConsts.STEP_BUSINESS_NAME.equals(step)) {
                newDto.setAppSvcBusinessDtoList(
                        (List<AppSvcBusinessDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcBusinessDtoList()));
            } else if (HcsaConsts.STEP_VEHICLES.equals(step)) {
                newDto.setAppSvcVehicleDtoList(
                        (List<AppSvcVehicleDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcVehicleDtoList()));
            } else if (HcsaConsts.STEP_SECTION_LEADER.equals(step)) {
                newDto.setAppSvcSectionLeaderList(
                        (List<AppSvcPersonnelDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcSectionLeaderList()));
            } else if (HcsaConsts.STEP_DOCUMENTS.equals(step)) {
                /*List<AppSvcDocDto> oldASvcDocDtoLit = oldSvcInfoDto.getAppSvcDocDtoLit();
                List<AppSvcDocDto> appSvcDocDtoLit = newDto.getAppSvcDocDtoLit();*/
            } else if (HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(step)) {
                newDto.setAppSvcLaboratoryDisciplinesDtoList(
                        (List<AppSvcLaboratoryDisciplinesDto>) CopyUtil.copyMutableObjectList(
                                oldSvcInfoDto.getAppSvcLaboratoryDisciplinesDtoList()));
            } else if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(step)) {
                newDto.setAppSvcDisciplineAllocationDtoList(
                        (List<AppSvcDisciplineAllocationDto>) CopyUtil.copyMutableObjectList(
                                oldSvcInfoDto.getAppSvcDisciplineAllocationDtoList()));
            } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_PO));
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
                if (changeList.contains(HcsaConsts.STEP_DISCIPLINE_ALLOCATION) && !stepList.contains(HcsaConsts.STEP_DISCIPLINE_ALLOCATION)) {
                    newDto.setAppSvcDisciplineAllocationDtoList(reSetAllocation(newDto, oldSvcInfoDto));
                }
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO));
            } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR));
            } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_KAH));
            } else if (HcsaConsts.MEDALERT_PERSON.equals(step)) {
                reSetPersonnels(oldSvcInfoDto, newDto, step, changeList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP));
            }
        }
        List<AppSvcRelatedInfoDto> result = IaisCommonUtils.genNewArrayList(1);
        result.add(newDto);
        return result;
    }

    private List<AppSvcDisciplineAllocationDto> reSetAllocation(AppSvcRelatedInfoDto newDto, AppSvcRelatedInfoDto oldSvcInfoDto) {
        List<AppSvcDisciplineAllocationDto> newAllocations = newDto.getAppSvcDisciplineAllocationDtoList();
        if (newAllocations == null || newAllocations.isEmpty() || oldSvcInfoDto.getAppSvcCgoDtoList() == null) {
            return newAllocations;
        }
        boolean isOld = true;
        for (AppSvcDisciplineAllocationDto dto : newAllocations) {
            isOld = oldSvcInfoDto.getAppSvcCgoDtoList().stream().anyMatch(cgo -> Objects.equals(cgo.getIdNo(), dto.getIdNo()));
            if (!isOld) {
                break;
            }
        }
        if (!isOld) {
            return (List<AppSvcDisciplineAllocationDto>) CopyUtil.copyMutableObjectList(oldSvcInfoDto.getAppSvcDisciplineAllocationDtoList());
        }
        return newAllocations;
    }

    private void reSetPersonnels(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String step,
            boolean isChanged) {
        List<AppSvcPrincipalOfficersDto> psnList = null;
        List<AppSvcPrincipalOfficersDto> newList = null;
        if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcPrincipalOfficersDtoList());
            newList = targetReletedInfo.getAppSvcPrincipalOfficersDtoList();
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcCgoDtoList());
            newList = targetReletedInfo.getAppSvcCgoDtoList();
        } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcClinicalDirectorDtoList());
            newList = targetReletedInfo.getAppSvcClinicalDirectorDtoList();
        } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcKeyAppointmentHolderDtoList());
            newList = targetReletedInfo.getAppSvcKeyAppointmentHolderDtoList();
        } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(step)) {
            psnList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    sourceReletedInfo.getAppSvcMedAlertPersonList());
            newList = targetReletedInfo.getAppSvcMedAlertPersonList();
        }
        if (isChanged && psnList != null && newList != null) {
            for (int i = 0, len = psnList.size(); i < len; i++) {
                AppSvcPrincipalOfficersDto psnDto = psnList.get(i);
                for (AppSvcPrincipalOfficersDto newPsn : newList) {
                    if (Objects.equals(psnDto.getIdNo(), newPsn.getIdNo())) {
                        psnList.set(i, newPsn);
                        break;
                    }
                }
            }
        }
        if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(step)) {
            targetReletedInfo.setAppSvcPrincipalOfficersDtoList(psnList);
        } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(step)) {
            targetReletedInfo.setAppSvcCgoDtoList(psnList);
        } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(step)) {
            targetReletedInfo.setAppSvcClinicalDirectorDtoList(psnList);
        } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(step)) {
            targetReletedInfo.setAppSvcKeyAppointmentHolderDtoList(psnList);
        } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(step)) {
            targetReletedInfo.setAppSvcMedAlertPersonList(psnList);
        }
    }



    @Override
    public List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto) throws Exception {
        return personContact(licenseeId, appSubmissionDto, oldAppSubmissionDto, 2);
    }

    /**
     * Check personnel affected data
     *
     * @param licenseeId
     * @param appSubmissionDto
     * @param oldAppSubmissionDto
     * @param check 0: only check changed; 1: check changed and retrieve affected data; 2: check changed, retrieve affected data and
     *             reset them.
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
        List<String> list1 = changeCgo(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        List<String> list2 = changeMeadrter(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList);
        List<String> list3 = changePo(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList);
        List<String> list4 = changeClinicalDirector(appSvcClinicalDirectorDtoList, oldAppSvcClinicalDirectorDtoList);
        List<String> list5 = changeKeyAppointmentHolder(kahList, oldKahList);

        List<String> currEditList = IaisCommonUtils.genNewArrayList();
        if (!list1.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            set.addAll(list1);
        } else if (isChanged(appSvcCgoDtoList, oldAppSvcCgoDtoList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        }
        if (!list2.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
            set.addAll(list2);
        } else if (isChanged(appSvcMedAlertPersonList, oldAppSvcMedAlertPersonList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        }
        if (!list3.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
            set.addAll(list3);
        } else if (isChanged(appSvcPrincipalOfficersDtoList, oldAppSvcPrincipalOfficersDtoList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        }
        if (!list4.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
            set.addAll(list4);
        } else if (isChanged(appSvcClinicalDirectorDtoList, oldAppSvcClinicalDirectorDtoList)) {
            currEditList.add(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        }
        if (!list5.isEmpty()) {
            currEditList.add(ApplicationConsts.PERSONNEL_PSN_KAH);
            set.addAll(list5);
        } else if (isChanged(kahList, oldKahList)) {
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
            List<String> personnelDtoByIdNo = requestForChangeService.getPersonnelIdsByIdNo(string);
            List<LicKeyPersonnelDto> licKeyPersonnelDtoByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(
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
            AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string);
            if (appSubmissionDtoByLicenceId == null || appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList() == null) {
                continue;
            }
            log.info(StringUtil.changeForLog("The affected licence: " + appSubmissionDtoByLicenceId.getLicenceNo()));
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setServiceEdit(true);
            List<String> personnelEditList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0);
            if (!list1.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, personnelEditList);
            }
            if (!list2.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, personnelEditList);
            }
            if (!list3.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO, personnelEditList);
            }
            if (!list4.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, personnelEditList);
            }
            // KAH
            if (!list5.isEmpty()) {
                reSetPersonnels(appSvcRelatedInfoDto, appSvcRelatedInfoDto2,
                        ApplicationConsts.PERSONNEL_PSN_KAH, personnelEditList);
            }
            appEditSelectDto.setPersonnelEditList(personnelEditList);
            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
            if (check == 2) {
                appSubmissionDtoByLicenceId.setPartPremise(false);
                appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
                RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionService.transform(appSubmissionDtoByLicenceId, appSubmissionDto.getLicenseeId());
                requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
                appSubmissionDtoByLicenceId.setAutoRfc(true);
                appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
                appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                NewApplicationHelper.reSetAdditionalFields(appSubmissionDtoByLicenceId, appEditSelectDto);
            }
            appSubmissionDtoList.add(appSubmissionDtoByLicenceId);
        }
        return appSubmissionDtoList;
    }

    private void reSetPersonnels(AppSvcRelatedInfoDto sourceReletedInfo, AppSvcRelatedInfoDto targetReletedInfo, String psnType,
            List<String> personnelEditList) {
        if (sourceReletedInfo == null || targetReletedInfo == null) {
            return;
        }
        log.info(StringUtil.changeForLog("Re-set personnel affected by " + psnType));
        List<AppSvcPrincipalOfficersDto> sourceList = null;
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcCgoDtoList();
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcMedAlertPersonList();
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcPrincipalOfficersDtoList();
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcClinicalDirectorDtoList();
        } else if (ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)) {
            sourceList = sourceReletedInfo.getAppSvcKeyAppointmentHolderDtoList();
        }
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcCgoDtoList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcMedAlertPersonList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcPrincipalOfficersDtoList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_TYPE_PO, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcClinicalDirectorDtoList(), psnType,
                ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, personnelEditList);
        reSetPersonnels(sourceList, targetReletedInfo.getAppSvcKeyAppointmentHolderDtoList(), psnType,
                ApplicationConsts.PERSONNEL_PSN_KAH, personnelEditList);
    }

    private void reSetPersonnels(List<AppSvcPrincipalOfficersDto> sourceList, List<AppSvcPrincipalOfficersDto> targetList,
            String sourcePsnType, String psnType, List<String> personnelEditList) {
        if (sourceList == null || targetList == null || StringUtil.isEmpty(sourcePsnType)) {
            return;
        }
        boolean isPersonnelEdit = false;
        for (AppSvcPrincipalOfficersDto target : targetList) {
            for (AppSvcPrincipalOfficersDto source : sourceList) {
                if (Objects.equals(target.getIdNo(), source.getIdNo())) {
                    if (sourcePsnType.equals(psnType)) {
                        AppSvcPrincipalOfficersDto newDto = (AppSvcPrincipalOfficersDto) CopyUtil.copyMutableObject(source);
                        newDto.setIndexNo(target.getIndexNo());
                        newDto.setCurPersonelId(target.getCurPersonelId());
                        targetList.set(targetList.indexOf(target), newDto);
                    } else {
                        NewApplicationHelper.syncPsnDto(source, target);
                    }
                    isPersonnelEdit = true;
                    break;
                }
            }
        }
        if (isPersonnelEdit) {
            personnelEditList.add(psnType);
        }
    }

    private List<String> changeKeyAppointmentHolder(List<AppSvcPrincipalOfficersDto> kahList,
            List<AppSvcPrincipalOfficersDto> oldKahList) {
        List<String> ids = new ArrayList<>(10);
        if (kahList != null && oldKahList != null) {
            if (kahList.equals(oldKahList)) {
                return ids;
            }
            kahList.forEach(kah -> {
                AppSvcPrincipalOfficersDto matched = oldKahList.stream()
                        .filter(dto -> Objects.equals(NewApplicationHelper.getPersonKey(dto), NewApplicationHelper.getPersonKey(kah)))
                        .findAny()
                        .orElse(null);
                if (matched != null && !(Objects.equals(matched.getIdType(), kah.getIdType())
                        && Objects.equals(matched.getName(), kah.getName())
                        && Objects.equals(matched.getSalutation(), kah.getSalutation()))) {
                    ids.add(NewApplicationHelper.getPersonKey(kah));
                }
            });
        }
        return ids;
    }

    protected List<String> changePo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList) {
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (appSvcPrincipalOfficersDtoList != null && oldAppSvcPrincipalOfficersDtoList != null) {
            if (appSvcPrincipalOfficersDtoList.equals(oldAppSvcPrincipalOfficersDtoList)) {
                return ids;
            }
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList) {
                String personKey = NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto);
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcPrincipalOfficersDtoList) {
                    if (Objects.equals(personKey, NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto1))) {
                        boolean b = appSvcPrincipalOfficersDto.getName().equals(appSvcPrincipalOfficersDto1.getName())
                                && appSvcPrincipalOfficersDto.getSalutation().equals(appSvcPrincipalOfficersDto1.getSalutation())
                                && appSvcPrincipalOfficersDto.getDesignation().equals(appSvcPrincipalOfficersDto1.getDesignation())
                                && appSvcPrincipalOfficersDto.getOfficeTelNo().equals(appSvcPrincipalOfficersDto1.getOfficeTelNo())
                                && appSvcPrincipalOfficersDto.getMobileNo().equals(appSvcPrincipalOfficersDto1.getMobileNo())
                                && appSvcPrincipalOfficersDto.getEmailAddr().equals(appSvcPrincipalOfficersDto1.getEmailAddr());
                        if (!b) {
                            ids.add(personKey);
                        }
                    }
                }
            }
        }
        return ids;
    }

    protected List<String> changeMeadrter(List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList1) {
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (appSvcMedAlertPersonList != null && oldAppSvcMedAlertPersonList1 != null) {
            if (appSvcMedAlertPersonList.equals(oldAppSvcMedAlertPersonList1)) {
                return ids;
            }
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
                String personKey = NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto);
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcMedAlertPersonList1) {
                    if (Objects.equals(personKey, NewApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto1))) {
                        boolean b = appSvcPrincipalOfficersDto.getSalutation().equals(appSvcPrincipalOfficersDto1.getSalutation())
                                && appSvcPrincipalOfficersDto.getName().equals(appSvcPrincipalOfficersDto1.getName())
                                && appSvcPrincipalOfficersDto.getMobileNo().equals(appSvcPrincipalOfficersDto1.getMobileNo())
                                && appSvcPrincipalOfficersDto.getEmailAddr().equals(appSvcPrincipalOfficersDto1.getEmailAddr());
                        if (!b) {
                            ids.add(personKey);
                        }
                    }
                }
            }

        }
        return ids;
    }

    protected List<String> changeCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList,
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList) {
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
            if (appSvcCgoDtoList.equals(oldAppSvcCgoDtoList)) {
                return ids;
            }
            for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
                String personKey = NewApplicationHelper.getPersonKey(appSvcCgoDto);
                for (AppSvcPrincipalOfficersDto appSvcCgoDto1 : oldAppSvcCgoDtoList) {
                    if (Objects.equals(personKey, NewApplicationHelper.getPersonKey(appSvcCgoDto1))) {
                        boolean b = appSvcCgoDto.getName().equals(appSvcCgoDto1.getName())
                                && appSvcCgoDto.getDesignation().equals(appSvcCgoDto1.getDesignation())
                                && appSvcCgoDto.getEmailAddr().equals(appSvcCgoDto1.getEmailAddr())
                                && appSvcCgoDto.getMobileNo().equals(appSvcCgoDto1.getMobileNo());
                        if (!b) {
                            ids.add(personKey);
                        }
                    }
                }
            }
        }
        return ids;
    }

    protected List<String> changeClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos,
            List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtos) {
        List<String> ids = new ArrayList<>(10);
        if (appSvcClinicalDirectorDtos != null && oldAppSvcClinicalDirectorDtos != null) {
            if (appSvcClinicalDirectorDtos.equals(oldAppSvcClinicalDirectorDtos)) {
                return ids;
            }
            for (AppSvcPrincipalOfficersDto v : appSvcClinicalDirectorDtos) {
                String personKey = NewApplicationHelper.getPersonKey(v);
                for (AppSvcPrincipalOfficersDto v1 : oldAppSvcClinicalDirectorDtos) {
                    if (Objects.equals(personKey, NewApplicationHelper.getPersonKey(v1))) {
                        boolean b = Objects.equals(v.getSalutation(), v1.getSalutation())
                                && Objects.equals(v.getName(), v1.getName())
                                && Objects.equals(v.getIdType(), v1.getIdType())
                                && Objects.equals(v.getDesignation(), v1.getDesignation())
                                /*&& Objects.equals(v.getProfRegNo(), v1.getProfRegNo())
                                && Objects.equals(v.getProfessionBoard(), v1.getProfessionBoard())
                                && Objects.equals(v.getSpeciality(), v1.getSpeciality())
                                && Objects.equals(v.getSpecialtyGetDate(), v1.getSpecialtyGetDate())
                                && Objects.equals(v.getTypeOfCurrRegi(), v1.getTypeOfCurrRegi())
                                && Objects.equals(v.getCurrRegiDate(), v1.getCurrRegiDate())
                                && Objects.equals(v.getPraCerEndDate(), v1.getPraCerEndDate())
                                && Objects.equals(v.getTypeOfRegister(), v1.getTypeOfRegister())
                                && Objects.equals(v.getRelevantExperience(), v1.getRelevantExperience())
                                && Objects.equals(v.getHoldCerByEMS(), v1.getHoldCerByEMS())
                                && Objects.equals(v.getAclsExpiryDate(), v1.getAclsExpiryDate())*/
                                && Objects.equals(v.getMobileNo(), v1.getMobileNo())
                                && Objects.equals(v.getEmailAddr(), v1.getEmailAddr());
                        if (!b) {
                            ids.add(personKey);
                        }
                    }
                }
            }
        }
        return ids;
    }

    private boolean isChanged(List<AppSvcPrincipalOfficersDto> psnList, List<AppSvcPrincipalOfficersDto> oldPsnList) {
        if (psnList == null || psnList.isEmpty() || oldPsnList == null || oldPsnList.isEmpty()) {
            return false;
        }
        for (AppSvcPrincipalOfficersDto psnDto : psnList) {
            for (AppSvcPrincipalOfficersDto oldPsnDto : oldPsnList) {
                if (psnDto.getIdNo().equals(oldPsnDto.getIdNo())) {
                    return !psnDto.equals(oldPsnDto);
                }
            }
        }
        return false;
    }

}
