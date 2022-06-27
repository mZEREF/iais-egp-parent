package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppDeclarationDocShowPageDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import lombok.extern.slf4j.Slf4j;
import sop.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSERVICEID;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSVCCODE;

/**
 * @Auther chenlei on 5/4/2022.
 */
@Slf4j
public final class AppDataHelper {

    private static LicCommService licCommService;
    private static ConfigCommService configCommService;
    private static AppCommService appCommService;

    private static ConfigCommService getConfigCommService() {
        if (configCommService == null) {
            configCommService = SpringHelper.getBean(ConfigCommService.class);
        }
        return configCommService;
    }

    private static AppCommService getAppCommService() {
        if (appCommService == null) {
            appCommService = SpringHelper.getBean(AppCommService.class);
        }
        return appCommService;
    }

    private static LicCommService getLicCommService() {
        if (licCommService == null) {
            licCommService = SpringHelper.getBean(LicCommService.class);
        }
        return licCommService;
    }

    public static SubLicenseeDto getSubLicenseeDtoDetailFromPage(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNumber = ParamUtil.getString(request, "idNumber");
        String licenseeName = ParamUtil.getString(request, "licenseeName");
        String postalCode = ParamUtil.getString(request, "postalCode");
        String addrType = ParamUtil.getString(request, "addrType");
        String blkNo = ParamUtil.getString(request, "blkNo");
        String floorNo = ParamUtil.getString(request, "floorNo");
        String unitNo = ParamUtil.getString(request, "unitNo");
        String streetName = ParamUtil.getString(request, "streetName");
        String buildingName = ParamUtil.getString(request, "buildingName");
        String telephoneNo = ParamUtil.getString(request, "telephoneNo");
        String emailAddr = ParamUtil.getString(request, "emailAddr");
        String nationality = ParamUtil.getString(request, "nationality");

        SubLicenseeDto dto = new SubLicenseeDto();
        dto.setIdType(idType);
        dto.setIdNumber(StringUtil.toUpperCase(idNumber));
        dto.setLicenseeName(licenseeName);
        dto.setPostalCode(postalCode);
        dto.setAddrType(addrType);
        dto.setBlkNo(blkNo);
        dto.setFloorNo(floorNo);
        dto.setUnitNo(unitNo);
        dto.setStreetName(streetName);
        dto.setBuildingName(buildingName);
        dto.setTelephoneNo(telephoneNo);
        dto.setEmailAddr(emailAddr);
        dto.setNationality(nationality);
        return dto;
    }

    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: request
     * @return: AppGrpPremisesDto
     */
    public static List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        boolean onlySpecifiedSvc = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(
                        appSvcRelatedInfoDto.getAlignLicenceNo())) {
                    onlySpecifiedSvc = true;
                    break;
                }
            }
        }
        if (onlySpecifiedSvc) {
            return appSubmissionDto.getAppGrpPremisesDtoList();
        }

        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        boolean isMultiPremService = ApplicationHelper.isMultiPremService(hcsaServiceDtoList);
        int count = 0;
        String[] premisesType = ParamUtil.getStrings(request, "premType");
        String[] hciName = ParamUtil.getStrings(request, "onSiteHciName");
        if (premisesType != null) {
            count = premisesType.length;
        }
        if (!isMultiPremService) {
            count = 1;
        }
        String[] premisesIndexNo = ParamUtil.getStrings(request, "premisesIndexNo");
        String[] rfiCanEdit = ParamUtil.getStrings(request, "rfiCanEdit");
        //onsite
        String[] premisesSelect = ParamUtil.getStrings(request, "onSiteSelect");
        String[] postalCode = ParamUtil.getStrings(request, "onSitePostalCode");
        String[] blkNo = ParamUtil.getStrings(request, "onSiteBlkNo");
        String[] streetName = ParamUtil.getStrings(request, "onSiteStreetName");
        String[] floorNo = ParamUtil.getStrings(request, "onSiteFloorNo");
        String[] unitNo = ParamUtil.getStrings(request, "onSiteUnitNo");
        String[] buildingName = ParamUtil.getStrings(request, "onSiteBuildingName");
        String[] siteAddressType = ParamUtil.getStrings(request, "onSiteAddressType");
        String[] siteEmail = ParamUtil.getStrings(request, "onSiteEmail");
        String[] offTelNo = ParamUtil.getStrings(request, "onSiteOffTelNo");
        String[] scdfRefNo = ParamUtil.getStrings(request, "onSiteScdfRefNo");
        String[] isOtherLic = ParamUtil.getStrings(request, "onSiteIsOtherLic");
        //conveyance
        String[] conveyanceHciName = ParamUtil.getStrings(request, "conveyanceHciName");
        String[] conPremisesSelect = ParamUtil.getStrings(request, "conveyanceSelect");
        String[] conVehicleNo = ParamUtil.getStrings(request, "conveyanceVehicleNo");
        String[] conPostalCode = ParamUtil.getStrings(request, "conveyancePostalCode");
        String[] conBlkNo = ParamUtil.getStrings(request, "conveyanceBlkNo");
        String[] conStreetName = ParamUtil.getStrings(request, "conveyanceStreetName");
        String[] conFloorNo = ParamUtil.getStrings(request, "conveyanceFloorNo");
        String[] conUnitNo = ParamUtil.getStrings(request, "conveyanceUnitNo");
        String[] conBuildingName = ParamUtil.getStrings(request, "conveyanceBuildingName");
        String[] conEmail = ParamUtil.getStrings(request, "conveyanceEmail");
        String[] conSiteAddressType = ParamUtil.getStrings(request, "conveyanceAddrType");
        //offSite
        String[] offSiteHciName = ParamUtil.getStrings(request, "offSiteHciName");
        String[] offSitePremisesSelect = ParamUtil.getStrings(request, "offSiteSelect");
        String[] offSitePostalCode = ParamUtil.getStrings(request, "offSitePostalCode");
        String[] offSiteBlkNo = ParamUtil.getStrings(request, "offSiteBlkNo");
        String[] offSiteStreetName = ParamUtil.getStrings(request, "offSiteStreetName");
        String[] offSiteFloorNo = ParamUtil.getStrings(request, "offSiteFloorNo");
        String[] offSiteUnitNo = ParamUtil.getStrings(request, "offSiteUnitNo");
        String[] offSiteBuildingName = ParamUtil.getStrings(request, "offSiteBuildingName");
        String[] offSiteEmail = ParamUtil.getStrings(request, "offSiteEmail");
        String[] offSiteSiteAddressType = ParamUtil.getStrings(request, "offSiteAddrType");

        String[] easMtsPremisesSelect = ParamUtil.getStrings(request, "easMtsSelect");
        //every prem's ph length
        String[] phLengths = ParamUtil.getStrings(request, "phLength");
        String[] premValue = ParamUtil.getStrings(request, "premValue");
        String[] isParyEdit = ParamUtil.getStrings(request, "isPartEdit");
        String[] chooseExistData = ParamUtil.getStrings(request, "chooseExistData");
        String[] opLengths = ParamUtil.getStrings(request, "opLength");
        String[] retrieveflag = ParamUtil.getStrings(request, "retrieveflag");
        String[] weeklyLengths = ParamUtil.getStrings(request, "weeklyLength");
        String[] eventLengths = ParamUtil.getStrings(request, "eventLength");
        for (int i = 0; i < count; i++) {
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            String premisesSel = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])) {
                premisesSel = premisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])) {
                premisesSel = conPremisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                premisesSel = offSitePremisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType[i])) {
                premisesSel = easMtsPremisesSelect[i];
            }
            String premIndexNo = "";
            try {
                premIndexNo = premisesIndexNo[i];
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            if (StringUtil.isEmpty(premIndexNo)) {
                log.info(StringUtil.changeForLog("New premise index"));
                premIndexNo = UUID.randomUUID().toString();
            }
            String appType = appSubmissionDto.getAppType();
            boolean newApp = !isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
            if (newApp) {
                if (!StringUtil.isEmpty(premisesSel) && !premisesSel.equals("-1")
                        && !premisesSel.equals(ApplicationConsts.NEW_PREMISES) && AppConsts.YES.equals(chooseExistData[i])) {
                    AppGrpPremisesDto licPremise = ApplicationHelper.getPremisesFromMap(premisesSel, request);
                    if (licPremise != null) {
                        appGrpPremisesDto = (AppGrpPremisesDto) CopyUtil.copyMutableObject(licPremise);
                    } else {
                        log.info(StringUtil.changeForLog("can not found this existing premises data ...."));
                    }
                    if (appGrpPremisesDto != null) {
                        //get value for jsp page
                        appGrpPremisesDto.setExistingData(chooseExistData[i]);
                        ApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                    }
                    continue;
                }
            } else if (AppConsts.YES.equals(chooseExistData[i])) {
                appGrpPremisesDto = ApplicationHelper.getPremisesFromMap(premisesSel, request);
                if (appGrpPremisesDto != null) {
                    if (AppConsts.TRUE.equals(rfiCanEdit[i])) {
                        appGrpPremisesDto.setRfiCanEdit(true);
                    } else {
                        appGrpPremisesDto.setRfiCanEdit(false);
                    }
                    if (!AppConsts.YES.equals(isParyEdit[i])) {
                        appGrpPremisesDto.setExistingData(chooseExistData[i]);
                        ApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                        continue;
                    } else {
                        log.info(StringUtil.changeForLog("Get data from page for " + StringUtil.clarify(premisesSel)));
                    }
                } else {
                    log.warn(StringUtil.changeForLog("##### warn Data: " + premIndexNo));
                    appGrpPremisesDto = new AppGrpPremisesDto();
                }
            }
            appGrpPremisesDto.setExistingData(chooseExistData[i]);
            ApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
            // set premise type
            appGrpPremisesDto.setPremisesType(premisesType[i]);
            //List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = IaisCommonUtils.genNewArrayList();
            List<OperationHoursReloadDto> weeklyDtoList = IaisCommonUtils.genNewArrayList();
            List<OperationHoursReloadDto> phDtoList = IaisCommonUtils.genNewArrayList();
            List<AppPremEventPeriodDto> eventList = IaisCommonUtils.genNewArrayList();
            /*int length = 0;
            try {
                length = Integer.parseInt(phLength[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("length can not parse to int"));
            }*/
            int opLength = 0;
            try {
                opLength = Integer.parseInt(opLengths[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("operation length can not parse to int"));
            }
            int weeklyLength = 0;
            try {
                weeklyLength = Integer.parseInt(weeklyLengths[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("weekly length can not parse to int"));
            }
            int phLength = 0;
            try {
                phLength = Integer.parseInt(phLengths[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("ph length can not parse to int"));
            }
            int eventLength = 0;
            try {
                eventLength = Integer.parseInt(eventLengths[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("event length can not parse to int"));
            }
            if (AppConsts.TRUE.equals(rfiCanEdit[i])) {
                appGrpPremisesDto.setRfiCanEdit(true);
            } else {
                appGrpPremisesDto.setRfiCanEdit(false);
            }
            if (AppConsts.YES.equals(retrieveflag[i])) {
                appGrpPremisesDto.setClickRetrieve(true);
            } else {
                appGrpPremisesDto.setClickRetrieve(false);
            }
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])) {
                String premVal = premValue[i];
                String fireSafetyCertIssuedDateStr = ParamUtil.getString(request, premVal + "onSiteFireSafetyCertIssuedDate");
                appGrpPremisesDto.setPremisesSelect(premisesSelect[i]);
                appGrpPremisesDto.setHciName(hciName[i]);
                appGrpPremisesDto.setPostalCode(postalCode[i]);
                appGrpPremisesDto.setBlkNo(blkNo[i]);
                appGrpPremisesDto.setStreetName(streetName[i]);
                appGrpPremisesDto.setFloorNo(floorNo[i]);
                appGrpPremisesDto.setUnitNo(unitNo[i]);
                appGrpPremisesDto.setBuildingName(buildingName[i]);
                appGrpPremisesDto.setEasMtsPubEmail(siteEmail[i]);
                appGrpPremisesDto.setScdfRefNo(scdfRefNo[i]);
                appGrpPremisesDto.setAddrType(siteAddressType[i]);
                appGrpPremisesDto.setOffTelNo(offTelNo[i]);
                Date fireSafetyCertIssuedDateDate = DateUtil.parseDate(fireSafetyCertIssuedDateStr, Formatter.DATE);
                appGrpPremisesDto.setCertIssuedDt(fireSafetyCertIssuedDateDate);
                String certIssuedDtStr = Formatter.formatDate(fireSafetyCertIssuedDateDate);
                appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
                if (AppConsts.YES.equals(isOtherLic[i])) {
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.YES);
                } else if (AppConsts.NO.equals(isOtherLic[i])) {
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.NO);
                }

                //weekly
                for (int j = 0; j < weeklyLength; j++) {
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request, genPageName(premVal, "onSiteWeekly", j));
                    String allDay = ParamUtil.getString(request, genPageName(premVal, "onSiteWeeklyAllDay", j));
                    //reload
                    String weeklySelect = ParamUtil.StringsToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if (weeklyVal != null) {
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if (AppConsts.TRUE.equals(allDay)) {
                        weeklyDto.setSelectAllDay(true);
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
                    } else {
                        String weeklyStartHH = ParamUtil.getString(request, genPageName(premVal, "onSiteWeeklyStartHH", j));
                        String weeklyStartMM = ParamUtil.getString(request, genPageName(premVal, "onSiteWeeklyStartMM", j));
                        String weeklyEndHH = ParamUtil.getString(request, genPageName(premVal, "onSiteWeeklyEndHH", j));
                        String weeklyEndMM = ParamUtil.getString(request, genPageName(premVal, "onSiteWeeklyEndMM", j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for (int j = 0; j < phLength; j++) {
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request, genPageName(premVal, "onSitePubHoliday", j));
                    String allDay = ParamUtil.getString(request, genPageName(premVal, "onSitePhAllDay", j));
                    //reload
                    String phSelect = ParamUtil.StringsToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if (phSelect != null) {
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if (AppConsts.TRUE.equals(allDay)) {
                        phDto.setSelectAllDay(true);
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
                        phDtoList.add(phDto);
                    } else {
                        String phStartHH = ParamUtil.getString(request, genPageName(premVal, "onSitePhStartHH", j));
                        String phStartMM = ParamUtil.getString(request, genPageName(premVal, "onSitePhStartMM", j));
                        String phEndHH = ParamUtil.getString(request, genPageName(premVal, "onSitePhEndHH", j));
                        String phEndMM = ParamUtil.getString(request, genPageName(premVal, "onSitePhEndMM", j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if (phLength > 1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(
                                phStartMM) || !StringUtil.isEmpty(phEndHH) || !StringUtil.isEmpty(phEndMM)) {
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for (int j = 0; j < eventLength; j++) {
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request, genPageName(premVal, "onSiteEvent", j));
                    String eventStartStr = ParamUtil.getString(request, genPageName(premVal, "onSiteEventStart", j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request, genPageName(premVal, "onSiteEventEnd", j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if (eventLength > 1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(
                            eventEndStr)) {
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                addFloorNoAndUnitNo(premValue[i], "onSiteFloorNo", "onSiteUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])) {

                appGrpPremisesDto.setConveyanceHciName(conveyanceHciName[i]);
                appGrpPremisesDto.setPremisesSelect(conPremisesSelect[i]);
                appGrpPremisesDto.setConveyanceVehicleNo(conVehicleNo[i]);
                appGrpPremisesDto.setConveyancePostalCode(conPostalCode[i]);
                appGrpPremisesDto.setConveyanceBlockNo(conBlkNo[i]);
                appGrpPremisesDto.setConveyanceStreetName(conStreetName[i]);
                appGrpPremisesDto.setConveyanceFloorNo(conFloorNo[i]);
                appGrpPremisesDto.setConveyanceUnitNo(conUnitNo[i]);
                appGrpPremisesDto.setConveyanceBuildingName(conBuildingName[i]);
                appGrpPremisesDto.setConveyanceEmail(conEmail[i]);
                appGrpPremisesDto.setEasMtsPubEmail(conEmail[i]);
                appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType[i]);

                //weekly
                String premVal = premValue[i];
                for (int j = 0; j < weeklyLength; j++) {
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request, genPageName(premVal, "conveyanceWeekly", j));
                    String allDay = ParamUtil.getString(request, genPageName(premVal, "conveyanceWeeklyAllDay", j));
                    //reload
                    String weeklySelect = ParamUtil.StringsToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if (weeklyVal != null) {
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if (AppConsts.TRUE.equals(allDay)) {
                        weeklyDto.setSelectAllDay(true);
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
                    } else {
                        String weeklyStartHH = ParamUtil.getString(request, genPageName(premVal, "conveyanceWeeklyStartHH", j));
                        String weeklyStartMM = ParamUtil.getString(request, genPageName(premVal, "conveyanceWeeklyStartMM", j));
                        String weeklyEndHH = ParamUtil.getString(request, genPageName(premVal, "conveyanceWeeklyEndHH", j));
                        String weeklyEndMM = ParamUtil.getString(request, genPageName(premVal, "conveyanceWeeklyEndMM", j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for (int j = 0; j < phLength; j++) {
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request, genPageName(premVal, "conveyancePubHoliday", j));
                    String allDay = ParamUtil.getString(request, genPageName(premVal, "conveyancePhAllDay", j));
                    //reload
                    String phSelect = ParamUtil.StringsToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if (phSelect != null) {
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if (AppConsts.TRUE.equals(allDay)) {
                        phDto.setSelectAllDay(true);
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
                        phDtoList.add(phDto);
                    } else {
                        String phStartHH = ParamUtil.getString(request, genPageName(premVal, "conveyancePhStartHH", j));
                        String phStartMM = ParamUtil.getString(request, genPageName(premVal, "conveyancePhStartMM", j));
                        String phEndHH = ParamUtil.getString(request, genPageName(premVal, "conveyancePhEndHH", j));
                        String phEndMM = ParamUtil.getString(request, genPageName(premVal, "conveyancePhEndMM", j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if (phLength > 1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(
                                phStartMM) || !StringUtil.isEmpty(phEndHH) || !StringUtil.isEmpty(phEndMM)) {
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for (int j = 0; j < eventLength; j++) {
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request, genPageName(premVal, "conveyanceEvent", j));
                    String eventStartStr = ParamUtil.getString(request, genPageName(premVal, "conveyanceEventStart", j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request, genPageName(premVal, "conveyanceEventEnd", j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if (eventLength > 1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(
                            eventEndStr)) {
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                addFloorNoAndUnitNo(premValue[i], "conveyanceFloorNo", "conveyanceUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                appGrpPremisesDto.setOffSiteHciName(offSiteHciName[i]);
                appGrpPremisesDto.setPremisesSelect(offSitePremisesSelect[i]);
                appGrpPremisesDto.setOffSitePostalCode(offSitePostalCode[i]);
                appGrpPremisesDto.setOffSiteBlockNo(offSiteBlkNo[i]);
                appGrpPremisesDto.setOffSiteStreetName(offSiteStreetName[i]);
                appGrpPremisesDto.setOffSiteFloorNo(offSiteFloorNo[i]);
                appGrpPremisesDto.setOffSiteUnitNo(offSiteUnitNo[i]);
                appGrpPremisesDto.setOffSiteBuildingName(offSiteBuildingName[i]);
                appGrpPremisesDto.setOffSiteEmail(offSiteEmail[i]);
                appGrpPremisesDto.setEasMtsPubEmail(offSiteEmail[i]);
                appGrpPremisesDto.setOffSiteAddressType(offSiteSiteAddressType[i]);
                //weekly
                String premVal = premValue[i];
                for (int j = 0; j < weeklyLength; j++) {
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request, genPageName(premVal, "offSiteWeekly", j));
                    String allDay = ParamUtil.getString(request, genPageName(premVal, "offSiteWeeklyAllDay", j));
                    //reload
                    String weeklySelect = ParamUtil.StringsToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if (weeklyVal != null) {
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if (AppConsts.TRUE.equals(allDay)) {
                        weeklyDto.setSelectAllDay(true);
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
                    } else {
                        String weeklyStartHH = ParamUtil.getString(request, genPageName(premVal, "offSiteWeeklyStartHH", j));
                        String weeklyStartMM = ParamUtil.getString(request, genPageName(premVal, "offSiteWeeklyStartMM", j));
                        String weeklyEndHH = ParamUtil.getString(request, genPageName(premVal, "offSiteWeeklyEndHH", j));
                        String weeklyEndMM = ParamUtil.getString(request, genPageName(premVal, "offSiteWeeklyEndMM", j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for (int j = 0; j < phLength; j++) {
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request, genPageName(premVal, "offSitePubHoliday", j));
                    String allDay = ParamUtil.getString(request, genPageName(premVal, "offSitePhAllDay", j));
                    //reload
                    String phSelect = ParamUtil.StringsToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if (phSelect != null) {
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if (AppConsts.TRUE.equals(allDay)) {
                        phDto.setSelectAllDay(true);
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
                        phDtoList.add(phDto);
                    } else {
                        String phStartHH = ParamUtil.getString(request, genPageName(premVal, "offSitePhStartHH", j));
                        String phStartMM = ParamUtil.getString(request, genPageName(premVal, "offSitePhStartMM", j));
                        String phEndHH = ParamUtil.getString(request, genPageName(premVal, "offSitePhEndHH", j));
                        String phEndMM = ParamUtil.getString(request, genPageName(premVal, "offSitePhEndMM", j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if (phLength > 1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(
                                phStartMM) || !StringUtil.isEmpty(phEndHH) || !StringUtil.isEmpty(phEndMM)) {
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for (int j = 0; j < eventLength; j++) {
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request, genPageName(premVal, "offSiteEvent", j));
                    String eventStartStr = ParamUtil.getString(request, genPageName(premVal, "offSiteEventStart", j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request, genPageName(premVal, "offSiteEventEnd", j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if (eventLength > 1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(
                            eventEndStr)) {
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                addFloorNoAndUnitNo(premValue[i], "offSiteFloorNo", "offSiteUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType[i])) {
                String easMtsHciName = ParamUtil.getString(request, "easMtsHciName");
                String easMtsPostalCode = ParamUtil.getString(request, "easMtsPostalCode");
                String easMtsBlkNo = ParamUtil.getString(request, "easMtsBlkNo");
                String easMtsStreetName = ParamUtil.getString(request, "easMtsStreetName");
                String easMtsFloorNo = ParamUtil.getString(request, "easMtsFloorNo");
                String easMtsUnitNo = ParamUtil.getString(request, "easMtsUnitNo");
                String easMtsBuildingName = ParamUtil.getString(request, "easMtsBuildingName");
                String easMtsAddressType = ParamUtil.getString(request, "easMtsAddrType");
                String easMtsUseOnly = ParamUtil.getString(request, "easMtsUseOnlyVal");
                String easMtsPubEmail = ParamUtil.getString(request, "easMtsPubEmail");
                String easMtsPubHotline = ParamUtil.getString(request, "easMtsPubHotline");
                appGrpPremisesDto.setEasMtsHciName(easMtsHciName);
                appGrpPremisesDto.setPremisesSelect(easMtsPremisesSelect[i]);
                appGrpPremisesDto.setEasMtsPostalCode(easMtsPostalCode);
                appGrpPremisesDto.setEasMtsAddressType(easMtsAddressType);
                appGrpPremisesDto.setEasMtsBlockNo(easMtsBlkNo);
                appGrpPremisesDto.setEasMtsFloorNo(easMtsFloorNo);
                appGrpPremisesDto.setEasMtsUnitNo(easMtsUnitNo);
                appGrpPremisesDto.setEasMtsStreetName(easMtsStreetName);
                appGrpPremisesDto.setEasMtsBuildingName(easMtsBuildingName);
                appGrpPremisesDto.setEasMtsUseOnly(easMtsUseOnly);
                appGrpPremisesDto.setEasMtsPubEmail(easMtsPubEmail);
                appGrpPremisesDto.setEasMtsPubHotline(easMtsPubHotline);
                addFloorNoAndUnitNo(premValue[i], "easMtsFloorNo", "easMtsUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            }
            //appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
            appGrpPremisesDto.setWeeklyDtoList(weeklyDtoList);
            appGrpPremisesDto.setPhDtoList(phDtoList);
            appGrpPremisesDto.setEventDtoList(eventList);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        /*if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            //set premises edit status
            ApplicationHelper.setPremEditStatus(appGrpPremisesDtoList, getAppGrpPremisesDtos(appSubmissionDto.getOldAppSubmissionDto()));
        }*/
        return appGrpPremisesDtoList;
    }

    private static String genPageName(Object prefix, String name, Object suffix) {
        return prefix + name + suffix;
    }

    private static void addFloorNoAndUnitNo(String prefix, String floorNoName, String unitNoNmae, int opLength,
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The length of additional floor and unit: " + opLength));
        if (opLength <= 0) {
            return;
        }
        for (int j = 0; j < opLength; j++) {
            String opFloorNo = ParamUtil.getString(request, prefix + floorNoName + j);
            String opUnitNo = ParamUtil.getString(request, prefix + unitNoNmae + j);
            if (StringUtil.isEmpty(opFloorNo) && StringUtil.isEmpty(opUnitNo)) {
                continue;
            }
            AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
            operationalUnitDto.setFloorNo(opFloorNo);
            operationalUnitDto.setUnitNo(opUnitNo);
            operationalUnitDto.setSeqNum(j);
            appPremisesOperationalUnitDtos.add(operationalUnitDto);
        }
    }

    public static String getFileAppendId(String appType) {
        StringBuilder s = new StringBuilder("selected");
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            s.append("New");
        } else if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)) {
            s.append("Cess");
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            s.append("RFC");
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            s.append("RENEW");
        }
        s.append("File");
        return s.toString();
    }

    public static void initDeclarationFiles(List<AppDeclarationDocDto> appDeclarationDocDtos, String appType,
            HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appDeclarationDocDtos)) {
            return;
        }
        String fileAppendId = getFileAppendId(appType);
        AppDeclarationDocShowPageDto dto = (AppDeclarationDocShowPageDto) request.getSession().getAttribute(
                fileAppendId + "DocShowPageDto");
        if (Objects.nonNull(dto)) {
            return;
        }
        List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
        HashMap<String, File> map = IaisCommonUtils.genNewHashMap();
        Map<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
        for (int i = 0, len = appDeclarationDocDtos.size(); i < len; i++) {
            AppDeclarationDocDto viewDoc = appDeclarationDocDtos.get(i);
            String index = String.valueOf(Optional.ofNullable(viewDoc.getSeqNum()).orElseGet(() -> 0));
            PageShowFileDto pageShowFileDto = new PageShowFileDto();
            pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
            pageShowFileDto.setIndex(index);
            pageShowFileDto.setFileName(viewDoc.getDocName());
            pageShowFileDto.setSize(viewDoc.getDocSize());
            pageShowFileDto.setMd5Code(viewDoc.getMd5Code());
            pageShowFileDto.setFileUploadUrl(viewDoc.getFileRepoId());
            pageShowFileDto.setVersion(Optional.ofNullable(viewDoc.getVersion()).orElseGet(() -> 1));
            pageShowFileDtos.add(pageShowFileDto);
            map.put(fileAppendId + index, null);
            pageShowFileHashMap.put(fileAppendId + index, pageShowFileDto);
        }
        // put page entity to sesstion
        dto = new AppDeclarationDocShowPageDto();
        dto.setFileMaxIndex(appDeclarationDocDtos.size());
        dto.setPageShowFileDtos(pageShowFileDtos);
        dto.setPageShowFileHashMap(pageShowFileHashMap);
        request.getSession().setAttribute(fileAppendId + "DocShowPageDto", dto);
        request.getSession().setAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId, map);
    }

    public static AppDeclarationMessageDto getAppDeclarationMessageDto(HttpServletRequest request, String type) {
        AppDeclarationMessageDto appDeclarationMessageDto = new AppDeclarationMessageDto();
        appDeclarationMessageDto.setAppType(type);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)) {
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            String preliminaryQuestionItem1 = request.getParameter("preliminaryQuestionItem1");
            String preliminaryQuestiontem2 = request.getParameter("preliminaryQuestiontem2");
            String effectiveDt = request.getParameter("effectiveDt");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            appDeclarationMessageDto.setPreliminaryQuestionItem1(preliminaryQuestionItem1);
            appDeclarationMessageDto.setPreliminaryQuestiontem2(preliminaryQuestiontem2);
            if (effectiveDt != null) {
                try {
                    Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDt);
                    appDeclarationMessageDto.setEffectiveDt(parse);
                } catch (ParseException e) {

                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)) {
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            String bankruptcyItem1 = request.getParameter("bankruptcyItem1");
            appDeclarationMessageDto.setBankruptcyItem1(bankruptcyItem1);
            String bankruptcyItem2 = request.getParameter("bankruptcyItem2");
            appDeclarationMessageDto.setBankruptcyItem2(bankruptcyItem2);
            String bankruptcyItem3 = request.getParameter("bankruptcyItem3");
            appDeclarationMessageDto.setBankruptcyItem3(bankruptcyItem3);
            String bankruptcyItem4 = request.getParameter("bankruptcyItem4");
            appDeclarationMessageDto.setBankruptcyItem4(bankruptcyItem4);
            String bankruptcyRemark = request.getParameter("bankruptcyRemark");
            appDeclarationMessageDto.setBankruptcyRemark(bankruptcyRemark);
            String competenciesItem1 = request.getParameter("competenciesItem1");
            appDeclarationMessageDto.setCompetenciesItem1(competenciesItem1);
            String competenciesItem2 = request.getParameter("competenciesItem2");
            appDeclarationMessageDto.setCompetenciesItem2(competenciesItem2);
            String competenciesItem3 = request.getParameter("competenciesItem3");
            appDeclarationMessageDto.setCompetenciesItem3(competenciesItem3);
            String competenciesRemark = request.getParameter("competenciesRemark");
            appDeclarationMessageDto.setCompetenciesRemark(competenciesRemark);
            String criminalRecordsItem1 = request.getParameter("criminalRecordsItem1");
            appDeclarationMessageDto.setCriminalRecordsItem1(criminalRecordsItem1);
            String criminalRecordsItem2 = request.getParameter("criminalRecordsItem2");
            appDeclarationMessageDto.setCriminalRecordsItem2(criminalRecordsItem2);
            String criminalRecordsItem3 = request.getParameter("criminalRecordsItem3");
            appDeclarationMessageDto.setCriminalRecordsItem3(criminalRecordsItem3);
            String criminalRecordsItem4 = request.getParameter("criminalRecordsItem4");
            appDeclarationMessageDto.setCriminalRecordsItem4(criminalRecordsItem4);
            String criminalRecordsRemark = request.getParameter("criminalRecordsRemark");
            appDeclarationMessageDto.setCriminalRecordsRemark(criminalRecordsRemark);
            String generalAccuracyItem1 = request.getParameter("generalAccuracyItem1");
            appDeclarationMessageDto.setGeneralAccuracyItem1(generalAccuracyItem1);

        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)) {
            // Preliminary Question
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            // Declaration on Bankruptcy
            appDeclarationMessageDto.setBankruptcyItem1(ParamUtil.getString(request, "bankruptcyItem1"));
            appDeclarationMessageDto.setBankruptcyItem2(ParamUtil.getString(request, "bankruptcyItem2"));
            appDeclarationMessageDto.setBankruptcyItem3(ParamUtil.getString(request, "bankruptcyItem3"));
            appDeclarationMessageDto.setBankruptcyItem4(ParamUtil.getString(request, "bankruptcyItem4"));
            appDeclarationMessageDto.setBankruptcyRemark(ParamUtil.getString(request, "bankruptcyRemark"));
            // Declaration on Competencies
            appDeclarationMessageDto.setCompetenciesItem1(ParamUtil.getString(request, "competenciesItem1"));
            appDeclarationMessageDto.setCompetenciesItem2(ParamUtil.getString(request, "competenciesItem2"));
            appDeclarationMessageDto.setCompetenciesItem3(ParamUtil.getString(request, "competenciesItem3"));
            appDeclarationMessageDto.setCompetenciesRemark(ParamUtil.getString(request, "competenciesRemark"));
            // Declaration on Criminal Records and Past Suspension/ Revocation under PHMCA/HCSA
            appDeclarationMessageDto.setCriminalRecordsItem1(ParamUtil.getString(request, "criminalRecordsItem1"));
            appDeclarationMessageDto.setCriminalRecordsItem2(ParamUtil.getString(request, "criminalRecordsItem2"));
            appDeclarationMessageDto.setCriminalRecordsItem3(ParamUtil.getString(request, "criminalRecordsItem3"));
            appDeclarationMessageDto.setCriminalRecordsItem4(ParamUtil.getString(request, "criminalRecordsItem4"));
            appDeclarationMessageDto.setCriminalRecordsRemark(ParamUtil.getString(request, "criminalRecordsRemark"));
            // General Accuracy Declaration
            appDeclarationMessageDto.setGeneralAccuracyItem1(ParamUtil.getString(request, "generalAccuracyItem1"));
        }
        appDeclarationMessageDto.setAppType(type);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return appDeclarationMessageDto;
    }

    public static List<AppDeclarationDocDto> getDeclarationFiles(String appType, HttpServletRequest request) {
        return getDeclarationFiles(appType, request, false);
    }

    public static List<AppDeclarationDocDto> getDeclarationFiles(String appType, HttpServletRequest request, boolean forPrint) {
        String fileAppendId = getFileAppendId(appType);
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        if (IaisCommonUtils.isEmpty(fileMap)) {
            request.getSession().setAttribute(fileAppendId + "DocShowPageDto", null);
            return null;
        }
        AppDeclarationDocShowPageDto dto = (AppDeclarationDocShowPageDto) request.getSession().getAttribute(
                fileAppendId + "DocShowPageDto");
        if (Objects.isNull(dto)) {
            dto = new AppDeclarationDocShowPageDto();
            dto.setPageShowFileHashMap(IaisCommonUtils.genNewHashMap());
        }
        Map<String, PageShowFileDto> pageShowFileHashMap = dto.getPageShowFileHashMap();
        List<PageShowFileDto> pageDtos = IaisCommonUtils.genNewArrayList();
        List<File> files = IaisCommonUtils.genNewArrayList();
        List<AppDeclarationDocDto> docDtos = IaisCommonUtils.genNewArrayList();
        List<AppDeclarationDocDto> oldDocDtos = IaisCommonUtils.genNewArrayList();
        fileMap.forEach((s, file) -> {
            // the current uploaed files
            String index = s.substring(fileAppendId.length());
            if (file != null) {
                long length = file.length();
                if (length > 0) {
                    Long size = length / 1024;
                    files.add(file);
                    AppDeclarationDocDto docDto = new AppDeclarationDocDto();
                    docDto.setDocName(file.getName());
                    String fileMd5 = FileUtils.getFileMd5(file);
                    docDto.setMd5Code(FileUtils.getFileMd5(file));
                    docDto.setDocSize(Integer.valueOf(size.toString()));
                    docDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    docDto.setSeqNum(Integer.valueOf(index));
                    Optional<Integer> versions = pageShowFileHashMap.entrySet()
                            .stream()
                            .filter(i -> s.equals(i.getKey()))
                            .map(i -> i.getValue().getVersion())
                            .findAny();
                    docDto.setVersion(versions.orElse(0) + 1);
                    docDtos.add(docDto);
                    PageShowFileDto pageShowFileDto = new PageShowFileDto();
                    pageShowFileDto.setIndex(index);
                    pageShowFileDto.setFileName(file.getName());
                    pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
                    pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageDtos.add(pageShowFileDto);
                }
            } else {
                // the previous / old files
                PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(s);
                if (Objects.nonNull(pageShowFileDto)) {
                    AppDeclarationDocDto docDto = new AppDeclarationDocDto();
                    docDto.setDocName(pageShowFileDto.getFileName());
                    docDto.setMd5Code(pageShowFileDto.getMd5Code());
                    docDto.setDocSize(pageShowFileDto.getSize());
                    docDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                    docDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    docDto.setSeqNum(Integer.valueOf(index));
                    docDto.setVersion(Optional.ofNullable(pageShowFileDto.getVersion()).orElseGet(() -> 1));
                    oldDocDtos.add(docDto);
                    pageDtos.add(pageShowFileDto);
                }
            }
        });
        if (!forPrint) {
            dto.setPageShowFileDtos(pageDtos);
            request.getSession().setAttribute(fileAppendId + "DocShowPageDto", dto);
            // dto.setFileMaxIndex(pageDtos.size());
            List<String> list = getConfigCommService().saveFileRepo(files);
            if (list != null) {
                ListIterator<String> iterator = list.listIterator();
                for (int j = 0; j < docDtos.size(); j++) {
                    String fileRepoId = docDtos.get(j).getFileRepoId();
                    if (fileRepoId == null) {
                        if (iterator.hasNext()) {
                            String next = iterator.next();
                            pageDtos.get(j).setFileUploadUrl(next);
                            docDtos.get(j).setFileRepoId(next);
                            iterator.remove();
                        }
                    }
                }
            }
        }
        docDtos.addAll(oldDocDtos);
        return docDtos;
    }

    public static List<AppSvcVehicleDto> genAppSvcVehicleDto(HttpServletRequest request, String appType) {
        List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        int vehicleLength = ParamUtil.getInt(request, "vehiclesLength");
        for (int i = 0; i < vehicleLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
            String vehicleIndexNo = ParamUtil.getString(request, "vehicleIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(vehicleIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}", getDataByIndexNo);
            log.debug("get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcVehicleDto appSvcVehicleDto = getAppSvcVehicleDtoByIndexNo(appSvcRelatedInfoDto, vehicleIndexNo);
                if (appSvcVehicleDto != null) {
                    appSvcVehicleDtos.add(appSvcVehicleDto);
                }
            } else if (getPageData) {
                String vehicleName = ParamUtil.getString(request, "vehicleName" + i);
                String chassisNum = ParamUtil.getString(request, "chassisNum" + i);
                String engineNum = ParamUtil.getString(request, "engineNum" + i);
                AppSvcVehicleDto appSvcVehicleDto = new AppSvcVehicleDto();
                appSvcVehicleDto.setVehicleNum(vehicleName);
                appSvcVehicleDto.setChassisNum(chassisNum);
                appSvcVehicleDto.setEngineNum(engineNum);
                appSvcVehicleDto.setDummyVehNum(StringUtil.isEmpty(vehicleName));
                String dummyVehNum = "";
                AppSvcVehicleDto oldAppSvcVehicleDto = getAppSvcVehicleDtoByIndexNo(appSvcRelatedInfoDto, vehicleIndexNo);
                if (oldAppSvcVehicleDto != null) {
                    dummyVehNum = oldAppSvcVehicleDto.getVehicleName();
                }
                if (StringUtil.isEmpty(dummyVehNum)) {
                    dummyVehNum = IaisEGPHelper.generateDummyVehicleNum(i);
                }
                appSvcVehicleDto.setVehicleName(dummyVehNum);
                if (appSvcVehicleDto.isDummyVehNum()) {
                    appSvcVehicleDto.setVehicleNum("Vehicle_No_" + (i + 1));
                }
                if (StringUtil.isEmpty(vehicleIndexNo)) {
                    appSvcVehicleDto.setVehicleIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcVehicleDto.setVehicleIndexNo(vehicleIndexNo);
                }
                appSvcVehicleDtos.add(appSvcVehicleDto);
            }
        }
        return appSvcVehicleDtos;
    }

    private static AppSvcVehicleDto getAppSvcVehicleDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        AppSvcVehicleDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                for (AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                    if (indexNo.equals(appSvcVehicleDto.getVehicleIndexNo())) {
                        result = appSvcVehicleDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static AppSvcPrincipalOfficersDto getClinicalDirectorByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        AppSvcPrincipalOfficersDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto : appSvcClinicalDirectorDtos) {
                    if (indexNo.equals(appSvcClinicalDirectorDto.getIndexNo())) {
                        result = appSvcClinicalDirectorDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static AppSvcChargesDto getChargesByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo, boolean isGeneral) {
        AppSvcChargesDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            AppSvcChargesPageDto appSvcChargesPageDto = appSvcRelatedInfoDto.getAppSvcChargesPageDto();
            if (appSvcChargesPageDto != null) {
                if (isGeneral) {
                    List<AppSvcChargesDto> generalChargesDtos = appSvcChargesPageDto.getGeneralChargesDtos();
                    result = getChargesByIndexNo(generalChargesDtos, indexNo);
                } else {
                    List<AppSvcChargesDto> otherChargesDtos = appSvcChargesPageDto.getOtherChargesDtos();
                    result = getChargesByIndexNo(otherChargesDtos, indexNo);
                }
            }
        }
        return result;
    }

    private static AppSvcChargesDto getChargesByIndexNo(List<AppSvcChargesDto> chargesDtos, String indexNo) {
        AppSvcChargesDto result = null;
        if (!IaisCommonUtils.isEmpty(chargesDtos) && !StringUtil.isEmpty(indexNo)) {
            for (AppSvcChargesDto appSvcChargesDto : chargesDtos) {
                if (indexNo.equals(appSvcChargesDto.getChargesIndexNo())) {
                    result = appSvcChargesDto;
                    break;
                }
            }
        }
        return result;
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcClinicalDirectorDto(HttpServletRequest request, String appType) {
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto start ..."));
        String currSvcCode = (String) ParamUtil.getSessionAttr(request, CURRENTSVCCODE);
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = IaisCommonUtils.genNewArrayList();
        int cdLength = ParamUtil.getInt(request, "cdLength");
        for (int i = 0; i < cdLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
            String cdIndexNo = ParamUtil.getString(request, "cdIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(cdIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}", getDataByIndexNo);
            log.debug("get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto = getClinicalDirectorByIndexNo(appSvcRelatedInfoDto, cdIndexNo);
                if (appSvcClinicalDirectorDto != null) {
                    appSvcClinicalDirectorDtos.add(appSvcClinicalDirectorDto);
                }
            } else if (getPageData) {
                String professionBoard = ParamUtil.getString(request, "professionBoard" + i);
                String profRegNo = ParamUtil.getString(request, "profRegNo" + i);
                String name = ParamUtil.getString(request, "name" + i);
                String salutation = ParamUtil.getString(request, "salutation" + i);
                String idType = ParamUtil.getString(request, "idType" + i);
                String idNo = ParamUtil.getString(request, "idNo" + i);
                String nationality = ParamUtil.getString(request, "nationality" + i);
                String designation = ParamUtil.getString(request, "designation" + i);
                String otherDesignation = ParamUtil.getString(request, "otherDesignation" + i);
//                String specialty = ParamUtil.getString(request,"speciality"+i);
//                String specialityOther = ParamUtil.getString(request,"specialityOther"+i);
                String specialtyGetDateStr = ParamUtil.getString(request, "specialtyGetDate" + i);
                String typeOfCurrRegi = ParamUtil.getString(request, "typeOfCurrRegi" + i);
                String currRegiDateStr = ParamUtil.getString(request, "currRegiDate" + i);
                String praCerEndDateStr = ParamUtil.getString(request, "praCerEndDate" + i);
                String typeOfRegister = ParamUtil.getString(request, "typeOfRegister" + i);
                String relevantExperience = ParamUtil.getString(request, "relevantExperience" + i);
                String holdCerByEMS = ParamUtil.getString(request, "holdCerByEMS" + i);
                String aclsExpiryDateStr = ParamUtil.getString(request, "aclsExpiryDate" + i);
                String bclsExpiryDateStr = ParamUtil.getString(request, "bclsExpiryDate" + i);
                String mobileNo = ParamUtil.getString(request, "mobileNo" + i);
                String emailAddr = ParamUtil.getString(request, "emailAddr" + i);
                String noRegWithProfBoard = ParamUtil.getString(request, "noRegWithProfBoardVal" + i);
                String transportYear = ParamUtil.getString(request, "transportYear" + i);

                String assignSel = ParamUtil.getString(request, "assignSel" + i);
                AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcClinicalDirectorDto.setPsnType(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
                log.info(StringUtil.changeForLog("Clinical Governance Officer assgined select: " + assignSel));
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType) || !ApplicationHelper.isEmpty(assignSel)) {
                    appSvcClinicalDirectorDto.setAssignSelect(assignSel);
                } else {
                    appSvcClinicalDirectorDto.setAssignSelect(ApplicationHelper.getAssignSelect(nationality, idType, idNo,
                            "-1"));
                }
                AppPsnEditDto appPsnEditDto = appSvcClinicalDirectorDto.getPsnEditDto();
                if (appPsnEditDto == null) {
                    appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcClinicalDirectorDto);
                    appSvcClinicalDirectorDto.setPsnEditDto(appPsnEditDto);
                }
                boolean partEdit = AppConsts.YES.equals(isPartEdit) && !StringUtil.isEmpty(cdIndexNo);
                boolean isNewOfficer = IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(
                        assignSel) || !appSvcClinicalDirectorDto.isLicPerson();
                if (canSetValue(appPsnEditDto.isProfessionBoard(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setProfessionBoard(professionBoard);
                }
                if (canSetValue(appPsnEditDto.isProfRegNo(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setProfRegNo(profRegNo);
                }
                if (canSetValue(appPsnEditDto.isName(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setName(name);
                }
                if (canSetValue(appPsnEditDto.isSalutation(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setSalutation(salutation);
                }
                if (canSetValue(appPsnEditDto.isIdType(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setIdType(idType);
                }
                if (canSetValue(appPsnEditDto.isIdNo(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setIdNo(StringUtil.toUpperCase(idNo));
                }
                if (canSetValue(appPsnEditDto.isNationality(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setNationality(nationality);
                }
                if (canSetValue(appPsnEditDto.isDesignation(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setDesignation(designation);
                }

                if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcClinicalDirectorDto.getDesignation())) {
                    if (canSetValue(appPsnEditDto.isOtherDesignation(), isNewOfficer, partEdit)) {
                        appSvcClinicalDirectorDto.setOtherDesignation(otherDesignation);
                    }
                } else {
                    appSvcClinicalDirectorDto.setOtherDesignation(null);
                }
                if (canSetValue(appPsnEditDto.isSpeciality(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setSpeciality(null);
                }
                if (canSetValue(appPsnEditDto.isTypeOfRegister(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setTypeOfRegister(typeOfRegister);
                }
                if (canSetValue(appPsnEditDto.isHoldCerByEMS(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setHoldCerByEMS(holdCerByEMS);
                }
                if (canSetValue(appPsnEditDto.isMobileNo(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setMobileNo(mobileNo);
                }
                if (canSetValue(appPsnEditDto.isEmailAddr(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setEmailAddr(emailAddr);
                }
                if (canSetValue(appPsnEditDto.isTypeOfCurrRegi(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setTypeOfCurrRegi(typeOfCurrRegi);
                }
                if (canSetValue(appPsnEditDto.isRelevantExperience(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setRelevantExperience(relevantExperience);
                }
                if (StringUtil.isEmpty(cdIndexNo)) {
                    appSvcClinicalDirectorDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcClinicalDirectorDto.setIndexNo(cdIndexNo);
                }

                //date pick
                if (canSetValue(appPsnEditDto.isSpecialtyGetDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setSpecialtyGetDateStr(specialtyGetDateStr);
                    Date specialtyGetDate = DateUtil.parseDate(specialtyGetDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setSpecialtyGetDate(specialtyGetDate);
                }
                if (canSetValue(appPsnEditDto.isPraCerEndDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setPraCerEndDateStr(praCerEndDateStr);
                    Date praCerEndDate = DateUtil.parseDate(praCerEndDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setPraCerEndDate(praCerEndDate);
                }
                if (canSetValue(appPsnEditDto.isCurrRegiDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setCurrRegiDateStr(currRegiDateStr);
                    Date currRegiDate = DateUtil.parseDate(currRegiDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setCurrRegiDate(currRegiDate);
                }
                if (canSetValue(appPsnEditDto.isAclsExpiryDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setAclsExpiryDateStr(aclsExpiryDateStr);
                    Date aclsExpiryDate = DateUtil.parseDate(aclsExpiryDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setAclsExpiryDate(aclsExpiryDate);
                }
                if (canSetValue(appPsnEditDto.isBclsExpiryDate(), isNewOfficer, partEdit)) {
                    appSvcClinicalDirectorDto.setBclsExpiryDateStr(bclsExpiryDateStr);
                    Date bclsExpiryDate = DateUtil.parseDate(bclsExpiryDateStr, Formatter.DATE);
                    appSvcClinicalDirectorDto.setBclsExpiryDate(bclsExpiryDate);
                }

                if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(currSvcCode)) {
                    if (canSetValue(appPsnEditDto.isNoRegWithProfBoard(), isNewOfficer, partEdit)) {
                        if (AppConsts.YES.equals(noRegWithProfBoard)) {
                            appSvcClinicalDirectorDto.setNoRegWithProfBoard(noRegWithProfBoard);
                        } else {
                            appSvcClinicalDirectorDto.setNoRegWithProfBoard(null);
                        }
                    }
                    if (canSetValue(appPsnEditDto.isTransportYear(), isNewOfficer, partEdit)) {
                        appSvcClinicalDirectorDto.setTransportYear(transportYear);
                    }
                }
                appSvcClinicalDirectorDtos.add(appSvcClinicalDirectorDto);
            }
        }
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto end ..."));
        return appSvcClinicalDirectorDtos;
    }

    private static boolean canSetValue(boolean canEdit, boolean isNewOfficer, boolean isPartEdit) {
        return isPartEdit || canEdit || isNewOfficer;
    }

    public static AppSvcChargesPageDto genAppSvcChargesDto(HttpServletRequest request, String appType) {
        AppSvcChargesPageDto appSvcChargesPageDto = new AppSvcChargesPageDto();
        List<AppSvcChargesDto> generalChargesDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcChargesDto> otherChargesDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        int generalChargeLength = ParamUtil.getInt(request, "generalChargeLength");
        for (int i = 0; i < generalChargeLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
            String chargesIndexNo = ParamUtil.getString(request, "chargesIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(chargesIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("get data by index no. is {}", getDataByIndexNo);
            log.debug("get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcChargesDto appSvcChargesDto = getChargesByIndexNo(appSvcRelatedInfoDto, chargesIndexNo, true);
                if (appSvcChargesDto != null) {
                    generalChargesDtos.add(appSvcChargesDto);
                }
            } else if (getPageData) {
                String chargesType = ParamUtil.getString(request, "chargesType" + i);
                String minAmount = ParamUtil.getString(request, "minAmount" + i);
                String maxAmount = ParamUtil.getString(request, "maxAmount" + i);
                String remarks = ParamUtil.getString(request, "remarks" + i);

                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesType(chargesType);
                appSvcChargesDto.setMinAmount(minAmount);
                appSvcChargesDto.setMaxAmount(maxAmount);
                appSvcChargesDto.setRemarks(remarks);
                if (StringUtil.isEmpty(chargesIndexNo)) {
                    appSvcChargesDto.setChargesIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcChargesDto.setChargesIndexNo(chargesIndexNo);
                }
                generalChargesDtos.add(appSvcChargesDto);
            }
        }
        int otherChargeLength = ParamUtil.getInt(request, "otherChargeLength");
        for (int i = 0; i < otherChargeLength; i++) {
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request, "otherChargesIsPartEdit" + i);
            String chargesIndexNo = ParamUtil.getString(request, "otherChargesIndexNo" + i);
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                getPageData = true;
            } else if (AppConsts.YES.equals(isPartEdit)) {
                getPageData = true;
            } else if (!StringUtil.isEmpty(chargesIndexNo)) {
                getDataByIndexNo = true;
            }
            log.debug("other charges get data by index no. is {}", getDataByIndexNo);
            log.debug("other charges get page data is {}", getPageData);
            if (getDataByIndexNo) {
                AppSvcChargesDto appSvcChargesDto = getChargesByIndexNo(appSvcRelatedInfoDto, chargesIndexNo, false);
                if (appSvcChargesDto != null) {
                    otherChargesDtos.add(appSvcChargesDto);
                }
            } else if (getPageData) {
                String otherChargesCategory = ParamUtil.getString(request, "otherChargesCategory" + i);
                String otherChargesType = ParamUtil.getString(request, "otherChargesType" + i);
                String otherAmountMin = ParamUtil.getString(request, "otherAmountMin" + i);
                String otherAmountMax = ParamUtil.getString(request, "otherAmountMax" + i);
                String otherRemarks = ParamUtil.getString(request, "otherRemarks" + i);
                AppSvcChargesDto appSvcChargesDto = new AppSvcChargesDto();
                appSvcChargesDto.setChargesCategory(otherChargesCategory);
                appSvcChargesDto.setChargesType(otherChargesType);
                appSvcChargesDto.setMinAmount(otherAmountMin);
                appSvcChargesDto.setMaxAmount(otherAmountMax);
                appSvcChargesDto.setRemarks(otherRemarks);
                if (StringUtil.isEmpty(chargesIndexNo)) {
                    appSvcChargesDto.setChargesIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcChargesDto.setChargesIndexNo(chargesIndexNo);
                }
                otherChargesDtos.add(appSvcChargesDto);
            }
        }
        appSvcChargesPageDto.setGeneralChargesDtos(generalChargesDtos);
        appSvcChargesPageDto.setOtherChargesDtos(otherChargesDtos);
        return appSvcChargesPageDto;
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcPrincipalOfficersDto(HttpServletRequest request,
            Boolean isGetDataFromPagePo, Boolean isGetDataFromPageDpo) {
        log.info(StringUtil.changeForLog("genAppSvcPrincipalOfficersDto start ...."));
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        String appType = appSubmissionDto.getAppType();
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        if (isGetDataFromPagePo) {
            log.info(StringUtil.changeForLog("get po data..."));
            String[] poExistingPsn = ParamUtil.getStrings(request, "poExistingPsn");
            String[] poLicPerson = ParamUtil.getStrings(request, "poLicPerson");
            String[] assignSelect = ParamUtil.getStrings(request, "poSelect");
            String[] salutation = ParamUtil.getStrings(request, "salutation");
            String[] name = ParamUtil.getStrings(request, "name");
            String[] idType = ParamUtil.getStrings(request, "idType");
            String[] idNo = ParamUtil.getStrings(request, "idNo");
            String[] nationality = ParamUtil.getStrings(request, "nationality");
            String[] designation = ParamUtil.getStrings(request, "designation");
            String[] otherDesignations = ParamUtil.getStrings(request, "otherDesignation");
            String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
            String[] officeTelNo = ParamUtil.getStrings(request, "officeTelNo");
            String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
            String[] poIsPartEdit = ParamUtil.getStrings(request, "poIsPartEdit");
            String[] poIndexNos = ParamUtil.getStrings(request, "poIndexNo");
            String[] loadingTypes = ParamUtil.getStrings(request, "loadingType");
            int length = 0;
            if (assignSelect != null) {
                length = assignSelect.length;
            }
            if (needEdit) {
                if (poIndexNos != null) {
                    length = poIndexNos.length;
                } else {
                    length = 0;
                }
            }
            for (int i = 0; i < length; i++) {
                boolean chooseExisting = false;
                boolean getPageData = false;
                String assign = assignSelect[i];
                String licPsn = poLicPerson[i];
                String loadingType = loadingTypes[i];
                boolean loadingByBlur = HcsaAppConst.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn)
                        && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
                //for rfi,rfc,renew use
//                String existingPsn = poExistingPsn[i];
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    if (assign != null) {
                        if (isExistingPsn(assign, licPsn)) {
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }
                } else if (needEdit) {
                    if (assign != null) {
                        String poIndexNo = poIndexNos[i];
                        if (!StringUtil.isEmpty(poIndexNo)) {
                            //not click edit
                            if (AppConsts.NO.equals(poIsPartEdit[i])) {
                                appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, poIndexNo,
                                        HcsaAppConst.PAGE_NAME_PO);
                                appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                                //change arr
                                poIndexNos = removeArrIndex(poIndexNos, i);
                                poIsPartEdit = removeArrIndex(poIsPartEdit, i);
                                poLicPerson = removeArrIndex(poLicPerson, i);
                                loadingTypes = removeArrIndex(loadingTypes, i);
                                //dropdown cannot disabled
                                assignSelect = removeArrIndex(assignSelect, i);
                                salutation = removeArrIndex(salutation, i);
                                idType = removeArrIndex(idType, i);
                                nationality = removeArrIndex(nationality, i);
                                designation = removeArrIndex(designation, i);
//                                existingPsn = removeArrIndex(existingPsn, i);
                                //change arr index
                                --i;
                                --length;
                                continue;
                            }
                        }
                        //isPartEdit->1.click edit button 2.add more psn
                        if (isExistingPsn(assign, licPsn)) {
                            //add cgo and choose existing
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }

                } else {
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:" + chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:" + getPageData));
                String assignSel = assignSelect[i];
                if (chooseExisting) {
                    if (loadingByBlur) {
                        assignSel = ApplicationHelper.getPersonKey(nationality[i], idType[i], idNo[i]);
                    }
                    appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                    appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                    AppPsnEditDto appPsnEditDto;
                    try {
                        appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                    } catch (Exception e) {
                        appPsnEditDto = new AppPsnEditDto();
                        log.error(e.getMessage(), e);
                    }
                    if (appPsnEditDto.isIdType()) {
                        ApplicationHelper.setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        ApplicationHelper.setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isNationality()) {
                        ApplicationHelper.setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        ApplicationHelper.setPsnValue(designation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if (appPsnEditDto.isOtherDesignation()) {
                        if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())) {
                            ApplicationHelper.setPsnValue(otherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                        } else {
                            otherDesignations = removeArrIndex(otherDesignations, i);
                        }
                    }

                    if (appPsnEditDto.isName()) {
                        name = ApplicationHelper.setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        idNo = ApplicationHelper.setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        mobileNo = ApplicationHelper.setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        officeTelNo = ApplicationHelper.setPsnValue(officeTelNo, i, appSvcPrincipalOfficersDto, "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        emailAddress = ApplicationHelper.setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                    }
                    String poIndexNo = poIndexNos[i];
                    if (!StringUtil.isEmpty(poIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(poIndexNo);
                    }
                    if (StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    poExistingPsn = removeArrIndex(poExistingPsn, i);
                    poLicPerson = removeArrIndex(poLicPerson, i);
                    poIndexNos = removeArrIndex(poIndexNos, i);
                    loadingTypes = removeArrIndex(loadingTypes, i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    salutation = removeArrIndex(salutation, i);
                    idType = removeArrIndex(idType, i);
                    nationality = removeArrIndex(nationality, i);
                    designation = removeArrIndex(designation, i);
                    --i;
                    --length;
                } else if (getPageData) {
                    String poIndexNo = poIndexNos[i];
                    if (StringUtil.isEmpty(poIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(poIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(getVal(salutation, i));
                    appSvcPrincipalOfficersDto.setName(getVal(name, i));
                    appSvcPrincipalOfficersDto.setIdType(getVal(idType, i));
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(getVal(idNo, i)));
                    appSvcPrincipalOfficersDto.setNationality(getVal(nationality, i));
                    appSvcPrincipalOfficersDto.setDesignation(getVal(designation, i));
                    appSvcPrincipalOfficersDto.setOtherDesignation(getVal(otherDesignations, i));
                    appSvcPrincipalOfficersDto.setMobileNo(getVal(mobileNo, i));
                    appSvcPrincipalOfficersDto.setOfficeTelNo(getVal(officeTelNo, i));
                    String emailAddr = "";
                    if (emailAddress != null) {
                        String val = getVal(emailAddress, i);
                        if (!StringUtil.isEmpty(val)) {
                            emailAddr = StringUtil.viewHtml(val);
                        }
                    }
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
                        String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                                appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                        AppSvcPrincipalOfficersDto licPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                        if (licPerson != null) {
                            appSvcPrincipalOfficersDto.setCurPersonelId(licPerson.getCurPersonelId());
                        }
                    }
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        //depo
        String deputySelect = ParamUtil.getString(request, "deputyPrincipalOfficer");
        if (AppConsts.YES.equals(deputySelect) && isGetDataFromPageDpo) {
            log.info(StringUtil.changeForLog("get dpo data..."));
            String[] dpoExistingPsn = ParamUtil.getStrings(request, "dpoExistingPsn");
            String[] dpoLicPerson = ParamUtil.getStrings(request, "dpoLicPerson");
            String[] assignSelect = ParamUtil.getStrings(request, "deputyPoSelect");
            String[] deputySalutation = ParamUtil.getStrings(request, "deputySalutation");
            String[] deputyDesignation = ParamUtil.getStrings(request, "deputyDesignation");
            String[] deputyOtherDesignations = ParamUtil.getStrings(request, "deputyOtherDesignation");
            String[] deputyName = ParamUtil.getStrings(request, "deputyName");
            String[] deputyIdType = ParamUtil.getStrings(request, "deputyIdType");
            String[] deputyIdNo = ParamUtil.getStrings(request, "deputyIdNo");
            String[] deputyNationlity = ParamUtil.getStrings(request, "deputyNationality");
            String[] deputyMobileNo = ParamUtil.getStrings(request, "deputyMobileNo");
            String[] deputyOfficeTelNo = ParamUtil.getStrings(request, "deputyOfficeTelNo");
            String[] deputyEmailAddr = ParamUtil.getStrings(request, "deputyEmailAddr");
            String[] dpoIsPartEdit = ParamUtil.getStrings(request, "dpoIsPartEdit");
            String[] dpoIndexNos = ParamUtil.getStrings(request, "dpoIndexNo");
            String[] dpoLoadingTypes = ParamUtil.getStrings(request, "dpoLoadingType");
            int length = 0;
            if (assignSelect != null) {
                length = assignSelect.length;
            }
            if (needEdit) {
                if (dpoIndexNos != null) {
                    length = dpoIndexNos.length;
                } else {
                    length = 0;
                }
            }
            for (int i = 0; i < length; i++) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                String assign = assignSelect[i];
                String licPsn = dpoLicPerson[i];
                boolean chooseExisting = false;
                boolean getPageData = false;
                String loadingType = dpoLoadingTypes[i];
                boolean loadingByBlur = HcsaAppConst.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn)
                        && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
                //for rfi,rfc,renew use
                String existingPsn = dpoExistingPsn[i];
                //new and not rfi
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    if (assign != null) {
                        if (isExistingPsn(assign, licPsn)) {
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }
                } else if (needEdit) {
                    if (assign != null) {
                        String dpoIndexNo = dpoIndexNos[i];
                        if (!StringUtil.isEmpty(dpoIndexNo)) {
                            //not click edit
                            if (AppConsts.NO.equals(dpoIsPartEdit[i])) {
                                appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, dpoIndexNo,
                                        HcsaAppConst.PAGE_NAME_PO);
                                appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                                //change arr
                                dpoIndexNos = removeArrIndex(dpoIndexNos, i);
                                dpoIsPartEdit = removeArrIndex(dpoIsPartEdit, i);
                                dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                                dpoLoadingTypes = removeArrIndex(dpoLoadingTypes, i);
                                //dropdown cannot disabled
                                assignSelect = removeArrIndex(assignSelect, i);
                                deputySalutation = removeArrIndex(deputySalutation, i);
                                deputyIdType = removeArrIndex(deputyIdType, i);
                                deputyNationlity = removeArrIndex(deputyNationlity, i);
                                deputyDesignation = removeArrIndex(deputyDesignation, i);
//                                existingPsn = removeArrIndex(existingPsn, i);
                                //change arr index
                                --i;
                                --length;
                                continue;
                            }
                        }
                        //isPartEdit->1.click edit button 2.add more psn
                        if (isExistingPsn(assign, licPsn)) {
                            //add cgo and choose existing
                            chooseExisting = true;
                        } else if (loadingByBlur) {
                            chooseExisting = true;
                        } else {
                            getPageData = true;
                        }
                    }
                } else {
                    log.info(StringUtil.changeForLog("The current type is not supported"));
                }
                log.info(StringUtil.changeForLog("chooseExisting:" + chooseExisting));
                log.info(StringUtil.changeForLog("getPageData:" + getPageData));
                String assignSel = assignSelect[i];
                if (chooseExisting) {
                    if (loadingByBlur) {
                        assignSel = ApplicationHelper.getPersonKey(deputyNationlity[i], deputyIdType[i], deputyIdNo[i]);
                    }
                    appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                    appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                    String dpoIndexNo = dpoIndexNos[i];
                    if (StringUtil.isEmpty(dpoIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(dpoIndexNo);
                    }
                    AppPsnEditDto appPsnEditDto;
                    try {
                        appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                    } catch (Exception e) {
                        appPsnEditDto = new AppPsnEditDto();
                        log.error(e.getMessage(), e);
                    }
                    if (appPsnEditDto.isIdType()) {
                        ApplicationHelper.setPsnValue(deputyIdType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        ApplicationHelper.setPsnValue(deputySalutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isNationality()) {
                        ApplicationHelper.setPsnValue(deputyNationlity, i, appSvcPrincipalOfficersDto, "nationality");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        ApplicationHelper.setPsnValue(deputyDesignation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if (appPsnEditDto.isOtherDesignation() && MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(
                            appSvcPrincipalOfficersDto.getDesignation())) {
                        ApplicationHelper.setPsnValue(deputyOtherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                    }
                    //input
                    if (appPsnEditDto.isName()) {
                        deputyName = ApplicationHelper.setPsnValue(deputyName, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        deputyIdNo = ApplicationHelper.setPsnValue(deputyIdNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        deputyMobileNo = ApplicationHelper.setPsnValue(deputyMobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        deputyOfficeTelNo = ApplicationHelper.setPsnValue(deputyOfficeTelNo, i, appSvcPrincipalOfficersDto,
                                "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        deputyEmailAddr = ApplicationHelper.setPsnValue(deputyEmailAddr, i, appSvcPrincipalOfficersDto, "emailAddr");
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setSelectDropDown(true);
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                    //change arr index
                    dpoLicPerson = removeArrIndex(dpoLicPerson, i);
                    dpoExistingPsn = removeArrIndex(dpoExistingPsn, i);
                    dpoLoadingTypes = removeArrIndex(dpoLoadingTypes, i);
                    //dropdown cannot disabled
                    assignSelect = removeArrIndex(assignSelect, i);
                    deputySalutation = removeArrIndex(deputySalutation, i);
                    deputyIdType = removeArrIndex(deputyIdType, i);
                    deputyNationlity = removeArrIndex(deputyNationlity, i);
                    deputyDesignation = removeArrIndex(deputyDesignation, i);
                    --i;
                    --length;
                } else if (getPageData) {
                    String dpoIndexNo = getVal(dpoIndexNos, i);
                    if (StringUtil.isEmpty(dpoIndexNo)) {
                        appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcPrincipalOfficersDto.setIndexNo(dpoIndexNo);
                    }
                    appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setSalutation(getVal(deputySalutation, i));
                    appSvcPrincipalOfficersDto.setName(getVal(deputyName, i));
                    appSvcPrincipalOfficersDto.setIdType(getVal(deputyIdType, i));
                    appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(getVal(deputyIdNo, i)));
                    appSvcPrincipalOfficersDto.setNationality(getVal(deputyNationlity, i));
                    appSvcPrincipalOfficersDto.setDesignation(getVal(deputyDesignation, i));
                    appSvcPrincipalOfficersDto.setOtherDesignation(getVal(deputyOtherDesignations, i));
                    appSvcPrincipalOfficersDto.setMobileNo(getVal(deputyMobileNo, i));
                    appSvcPrincipalOfficersDto.setOfficeTelNo(getVal(deputyOfficeTelNo, i));
                    String emailAddr = "";
                    if (deputyEmailAddr != null) {
                        String val = getVal(deputyEmailAddr, i);
                        if (!StringUtil.isEmpty(val)) {
                            emailAddr = StringUtil.viewHtml(val);
                        }
                    }
                    appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                    if (needEdit && AppConsts.YES.equals(licPsn)) {
                        appSvcPrincipalOfficersDto.setLicPerson(true);
                        String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                                appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                        AppSvcPrincipalOfficersDto licPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                        if (licPerson != null) {
                            appSvcPrincipalOfficersDto.setCurPersonelId(licPerson.getCurPersonelId());
                        }
                    }
                    appSvcPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("genAppSvcPrincipalOfficersDto end ...."));
        return appSvcPrincipalOfficersDtos;
    }

    private static AppSvcPrincipalOfficersDto getPsnByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo,
            String pageName) {
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> psnDtos = null;
            if (HcsaAppConst.PAGE_NAME_PO.equals(pageName)) {
                psnDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            } else if (HcsaAppConst.PAGE_NAME_MAP.equals(pageName)) {
                psnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
            }
            if (!IaisCommonUtils.isEmpty(psnDtos)) {
                for (AppSvcPrincipalOfficersDto psnDto : psnDtos) {
                    if (indexNo.equals(psnDto.getIndexNo())) {
                        return psnDto;
                    }
                }
            }
        }
        return new AppSvcPrincipalOfficersDto();
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcCgoDto(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcCgoDto start ...."));
        ParamUtil.setSessionAttr(request, HcsaAppConst.ERRORMAP_GOVERNANCEOFFICERS, null);
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto appSvcCgoDto;
        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size = 0;
        if (assignSelect != null && assignSelect.length > 0) {
            size = assignSelect.length;
        }
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String appType = "";
        if (appSubmissionDto != null) {
            appType = appSubmissionDto.getAppType();
        }
        String currentSvcId = ApplicationHelper.getCurrentServiceId(request);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        //indexNo
        String[] indexNos = ParamUtil.getStrings(request, "indexNo");
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        boolean needEdit = rfcOrRenew || isRfi;
        if (needEdit) {
            if (indexNos == null) {
                size = 0;
            } else {
                size = indexNos.length;
            }
        }
        String[] existingPsn = ParamUtil.getStrings(request, "existingPsn");
        String[] licPerson = ParamUtil.getStrings(request, "licPerson");
        String[] isPartEdit = ParamUtil.getStrings(request, "isPartEdit");
        //form display data
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] nationality = ParamUtil.getStrings(request, "nationality");
        String[] designation = ParamUtil.getStrings(request, "designation");
        String[] otherDesignations = ParamUtil.getStrings(request, "otherDesignation");
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, "professionRegoNo");
        //String[] specialty = ParamUtil.getStrings(request, "specialty");
        //String[] specialtyOther = ParamUtil.getStrings(request, "specialtyOther");
        //String[] qualification = ParamUtil.getStrings(request, "qualification");
        String[] otherQualification = ParamUtil.getStrings(request, "otherQualification");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        //new and not rfi
        for (int i = 0; i < size; i++) {
            AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
            boolean chooseExisting = false;
            boolean getPageData = false;
            appSvcCgoDto = new AppSvcPrincipalOfficersDto();
            String indexNo = indexNos[i];
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                if (assign != null) {
                    if (isExistingPsn(assign, licPsn)) {
                        chooseExisting = true;
                    } else {
                        getPageData = true;
                    }
                }
            } else if (needEdit) {
                if (assign != null) {
                    if (!StringUtil.isEmpty(indexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcCgoDto = getAppSvcCgoByIndexNo(appSvcRelatedInfoDto, indexNo);
                            appSvcCgoDtoList.add(appSvcCgoDto);
                            //change arr
                            indexNos = removeArrIndex(indexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            //dropdown cannot disabled
                            assignSelect = removeArrIndex(assignSelect, i);
                            salutation = removeArrIndex(salutation, i);
                            idType = removeArrIndex(idType, i);
                            nationality = removeArrIndex(nationality, i);
                            designation = removeArrIndex(designation, i);
                            professionType = removeArrIndex(professionType, i);
                            //specialty = removeArrIndex(specialty, i);
                            existingPsn = removeArrIndex(existingPsn, i);
                            //specialtyOther = removeArrIndex(specialtyOther,i);
                            //change arr index
                            --i;
                            --size;
                            continue;
                        }
                    }
                    //isPartEdit->1.click edit button 2.add more psn
                    if (isExistingPsn(assign, licPsn)) {
                        //add cgo and choose existing
                        chooseExisting = true;
                    } else {
                        getPageData = true;
                    }

                }
            } else {
                log.info(StringUtil.changeForLog("The current type is not supported"));
            }
            log.info(StringUtil.changeForLog("chooseExisting:" + chooseExisting));
            log.info(StringUtil.changeForLog("getPageData:" + getPageData));
            if (chooseExisting) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSelect[i]);
                try {
                    appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                } catch (Exception e) {
                    clearAppPsnEditDto(appPsnEditDto);
                    log.error(e.getMessage(), e);
                }
                if (appPsnEditDto.isSalutation()) {
                    ApplicationHelper.setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                }
                if (appPsnEditDto.isIdType()) {
                    ApplicationHelper.setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                }
                if (appPsnEditDto.isNationality()) {
                    ApplicationHelper.setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                }
                if (appPsnEditDto.isDesignation()) {
                    ApplicationHelper.setPsnValue(designation, i, appSvcPrincipalOfficersDto, "designation");
                }
                if (appPsnEditDto.isOtherDesignation()) {
                    if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())) {
                        ApplicationHelper.setPsnValue(otherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                    } else {
                        otherDesignations = removeArrIndex(otherDesignations, i);
                    }
                }
                if (appPsnEditDto.isProfessionType()) {
                    ApplicationHelper.setPsnValue(professionType, i, appSvcPrincipalOfficersDto, "professionType");
                }

                //input
                if (appPsnEditDto.isName()) {
                    name = ApplicationHelper.setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                }
                if (appPsnEditDto.isIdNo()) {
                    idNo = ApplicationHelper.setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                }
                if (appPsnEditDto.isMobileNo()) {
                    mobileNo = ApplicationHelper.setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                }
                if (appPsnEditDto.isProfRegNo()) {
                    professionRegoNo = ApplicationHelper.setPsnValue(professionRegoNo, i, appSvcPrincipalOfficersDto, "profRegNo");
                }
                if (appPsnEditDto.isOtherQualification()) {
                    otherQualification = ApplicationHelper.setPsnValue(otherQualification, i, appSvcPrincipalOfficersDto,
                            "otherQualification");
                }
                if (appPsnEditDto.isEmailAddr()) {
                    emailAddress = ApplicationHelper.setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                }
                appSvcCgoDto = (AppSvcPrincipalOfficersDto) CopyUtil.copyMutableObject(appSvcPrincipalOfficersDto);
                appSvcCgoDto.setAssignSelect(assignSelect[i]);
                appSvcCgoDto.setLicPerson(true);
                appSvcCgoDto.setSelectDropDown(true);
                if (!StringUtil.isEmpty(indexNo)) {
                    appSvcCgoDto.setIndexNo(indexNo);
                } else if (StringUtil.isEmpty(appSvcCgoDto.getIndexNo())) {
                    appSvcCgoDto.setIndexNo(UUID.randomUUID().toString());
                }
                //
                boolean needSpcOptList = appSvcCgoDto.isNeedSpcOptList();
                if (needSpcOptList) {
                    Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
                    specialtyAttr.put("name", "specialty");
                    specialtyAttr.put("class", "specialty");
                    specialtyAttr.put("style", "display: none;");
                    List<SelectOption> spcOpts = appSvcCgoDto.getSpcOptList();
                    String specialtySelectStr = ApplicationHelper.generateDropDownHtml(specialtyAttr, spcOpts, null,
                            appSvcCgoDto.getSpeciality());
                    appSvcCgoDto.setSpecialityHtml(specialtySelectStr);
                }
                appSvcCgoDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                appSvcCgoDtoList.add(appSvcCgoDto);
                //change arr index
                indexNos = removeArrIndex(indexNos, i);
                isPartEdit = removeArrIndex(isPartEdit, i);
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn, i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                nationality = removeArrIndex(nationality, i);
                designation = removeArrIndex(designation, i);
                professionType = removeArrIndex(professionType, i);
                //specialty = removeArrIndex(specialty, i);
                --i;
                --size;
            } else if (getPageData) {
                if (StringUtil.isEmpty(indexNo)) {
                    appSvcCgoDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcCgoDto.setIndexNo(indexNos[i]);
                }
                appSvcCgoDto.setAssignSelect(assignSelect[i]);
                appSvcCgoDto.setSalutation(salutation[i]);
                appSvcCgoDto.setName(name[i]);
                appSvcCgoDto.setIdType(idType[i]);
                appSvcCgoDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                appSvcCgoDto.setNationality(nationality[i]);
                appSvcCgoDto.setDesignation(designation[i]);
                if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation[i])) {
                    appSvcCgoDto.setOtherDesignation(otherDesignations[i]);
                }
                appSvcCgoDto.setProfessionType(professionType[i]);
                appSvcCgoDto.setProfRegNo(professionRegoNo[i]);
                appSvcCgoDto.setOtherQualification(otherQualification[i]);
                appSvcCgoDto.setMobileNo(mobileNo[i]);
                String emailAddr = "";
                if (emailAddress != null) {
                    if (!StringUtil.isEmpty(emailAddress[i])) {
                        emailAddr = StringUtil.viewHtml(emailAddress[i]);
                    }
                }
                appSvcCgoDto.setEmailAddr(emailAddr);
                if (needEdit && AppConsts.YES.equals(licPerson[i])) {
                    appSvcCgoDto.setLicPerson(true);
                    String personKey = ApplicationHelper.getPersonKey(appSvcCgoDto.getNationality(), appSvcCgoDto.getIdType(),
                            appSvcCgoDto.getIdNo());
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                    if (appSvcPrincipalOfficersDto != null) {
                        appSvcCgoDto.setCurPersonelId(appSvcPrincipalOfficersDto.getCurPersonelId());
                    }
                }
                appSvcCgoDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);

                appSvcCgoDtoList.add(appSvcCgoDto);
            }
        }
        ParamUtil.setSessionAttr(request, HcsaAppConst.GOVERNANCEOFFICERSDTOLIST, (Serializable) appSvcCgoDtoList);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
    }

    private static AppSvcPrincipalOfficersDto getAppSvcCgoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcCgoDto1 : appSvcCgoDtos) {
                    if (indexNo.equals(appSvcCgoDto1.getIndexNo())) {
                        return appSvcCgoDto1;
                    }
                }
            }
        }
        return new AppSvcPrincipalOfficersDto();
    }

    public static List<AppSvcPersonnelDto> genAppSvcPersonnelDtoList(HttpServletRequest request, List<String> personnelTypeList,
            String svcCode) {
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
        String[] personnelSels = ParamUtil.getStrings(request, "personnelSel");
        String[] designations = ParamUtil.getStrings(request, "designation");
        String[] otherDesignationss = ParamUtil.getStrings(request, "otherDesignation");
        String[] names = ParamUtil.getStrings(request, "name");
        String[] qualifications = ParamUtil.getStrings(request, "qualification");
        String[] wrkExpYears = ParamUtil.getStrings(request, "wrkExpYear");
        String[] professionalRegnNos = ParamUtil.getStrings(request, "regnNo");
        String[] indexNos = ParamUtil.getStrings(request, "indexNo");
        String[] isPartEdit = ParamUtil.getStrings(request, "isPartEdit");
        String appType = ApplicationHelper.getAppSubmissionDto(request).getAppType();
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        if (personnelSels != null && personnelSels.length > 0) {
            int length = personnelSels.length;
            for (int i = 0; i < length; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                String personnelSel = personnelSels[i];
                appSvcPersonnelDto.setPersonnelType(personnelSel);
                if (needEdit && personnelSel != null) {
                    String indexNo = indexNos[i];
                    if (!StringUtil.isEmpty(indexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcPersonnelDto = getAppSvcPersonnelDtoByIndexNo(appSvcRelatedInfoDto, indexNo);
                            appSvcPersonnelDto.setSeqNum(i);
                            appSvcPersonnelDtos.add(appSvcPersonnelDto);
                            //change arr
                            indexNos = removeArrIndex(indexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            //dropdown cannot disabled
                            personnelSels = removeArrIndex(personnelSels, i);
                            designations = removeArrIndex(designations, i);
                            //change arr index
                            --i;
                            --length;
                            continue;
                        }
                    }
                }
                if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode) ||
                        AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)) {
                    if (StringUtil.isEmpty(personnelSel) || !personnelTypeList.contains(personnelSel)) {
                        appSvcPersonnelDto.setSeqNum(i);
                        appSvcPersonnelDtos.add(appSvcPersonnelDto);
                        continue;
                    }
                }

                String designation = "";
                String name = "";
                String qualification = "";
                String wrkExpYear = "";
                String professionalRegnNo = "";
                if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
                    if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                        name = names[i];
                        qualification = qualifications[i];
                        wrkExpYear = wrkExpYears[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                        name = names[i];
                    }
                } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)) {
                    if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                        designation = designations[i];
                        name = names[i];
                        qualification = qualifications[i];
                        wrkExpYear = wrkExpYears[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                        name = names[i];
                        qualification = qualifications[i];
                        wrkExpYear = wrkExpYears[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                        name = names[i];
                    } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                        name = names[i];
                        professionalRegnNo = professionalRegnNos[i];
                    }
                } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                    designation = designations[i];
                    name = names[i];
                    professionalRegnNo = professionalRegnNos[i];
                    wrkExpYear = wrkExpYears[i];
                } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                    name = names[i];
                    qualification = qualifications[i];
                    wrkExpYear = wrkExpYears[i];
                } else {
                    name = names[i];
                    qualification = qualifications[i];
                    wrkExpYear = wrkExpYears[i];
                }

                appSvcPersonnelDto.setDesignation(designation);
                if (HcsaAppConst.DESIGNATION_OTHERS.equals(designation)) {
                    appSvcPersonnelDto.setOtherDesignation(otherDesignationss[i]);
                }
                appSvcPersonnelDto.setName(name);
                appSvcPersonnelDto.setQualification(qualification);
                appSvcPersonnelDto.setWrkExpYear(wrkExpYear);
                appSvcPersonnelDto.setProfRegNo(professionalRegnNo);
                String indexNo = indexNos[i];
                if (!StringUtil.isEmpty(indexNo)) {
                    appSvcPersonnelDto.setIndexNo(indexNo);
                } else {
                    appSvcPersonnelDto.setIndexNo(UUID.randomUUID().toString());
                }
                appSvcPersonnelDto.setSeqNum(i);
                appSvcPersonnelDtos.add(appSvcPersonnelDto);
            }
        }
        return appSvcPersonnelDtos;
    }

    private static AppSvcPersonnelDto getAppSvcPersonnelDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo){
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcPersonnelDtoList)) {
                for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList) {
                    if (indexNo.equals(appSvcPersonnelDto.getIndexNo())) {
                        return appSvcPersonnelDto;
                    }
                }
            }
        }
        return new AppSvcPersonnelDto();
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcKeyAppointmentHolder(HttpServletRequest request, String appType) {
        List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = IaisCommonUtils.genNewArrayList();
        int keyAppointmentHolderLength = ParamUtil.getInt(request,"keyAppointmentHolderLength");
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        for(int i = 0; i < keyAppointmentHolderLength; i++){
            boolean getDataByIndexNo = false;
            boolean getPageData = false;
            String isPartEdit = ParamUtil.getString(request,"isPartEdit"+i);
            String indexNo = ParamUtil.getString(request,"indexNo"+i);
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                getPageData = true;
            }else if(AppConsts.YES.equals(isPartEdit)){
                getPageData = true;
            }else if(!StringUtil.isEmpty(indexNo)){
                getDataByIndexNo = true;
            }
            AppSvcPrincipalOfficersDto appSvcKeyAppointmentHolderDto = null;
            if(getDataByIndexNo){
                appSvcKeyAppointmentHolderDto = getKeyAppointmentHolderByIndexNo(appSvcRelatedInfoDto, indexNo);
            }else if(getPageData){
                String assignSel = ParamUtil.getString(request,"assignSel"+i);
                String name = ParamUtil.getString(request,"name"+i);
                String salutation = ParamUtil.getString(request,"salutation"+i);
                String idType = ParamUtil.getString(request,"idType"+i);
                String idNo = ParamUtil.getString(request,"idNo"+i);
                String nationality = ParamUtil.getString(request, "nationality" + i);
                appSvcKeyAppointmentHolderDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcKeyAppointmentHolderDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_KAH);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType) || !ApplicationHelper.isEmpty(assignSel)) {
                    appSvcKeyAppointmentHolderDto.setAssignSelect(assignSel);
                } else {
                    appSvcKeyAppointmentHolderDto.setAssignSelect(ApplicationHelper.getAssignSelect(nationality, idType, idNo,
                            "-1"));
                }
                AppPsnEditDto appPsnEditDto = appSvcKeyAppointmentHolderDto.getPsnEditDto();
                if (appPsnEditDto == null) {
                    appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcKeyAppointmentHolderDto);
                    appSvcKeyAppointmentHolderDto.setPsnEditDto(appPsnEditDto);
                }
                boolean partEdit = AppConsts.YES.equals(isPartEdit) && !StringUtil.isEmpty(indexNo);
                boolean isNewOfficer = IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assignSel) || !appSvcKeyAppointmentHolderDto.isLicPerson();
                if (isNewOfficer && (appPsnEditDto.isSalutation() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setSalutation(salutation);
                }
                if (isNewOfficer && (appPsnEditDto.isName() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setName(name);
                }
                if (isNewOfficer && (appPsnEditDto.isIdType() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setIdType(idType);
                }
                if (isNewOfficer && (appPsnEditDto.isIdNo() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setIdNo(StringUtil.toUpperCase(idNo));
                }
                if (isNewOfficer && (appPsnEditDto.isNationality() || partEdit)) {
                    appSvcKeyAppointmentHolderDto.setNationality(nationality);
                }
                if (StringUtil.isEmpty(indexNo)) {
                    appSvcKeyAppointmentHolderDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcKeyAppointmentHolderDto.setIndexNo(indexNo);
                }
            }
            if (appSvcKeyAppointmentHolderDto == null) {
                appSvcKeyAppointmentHolderDto = new AppSvcPrincipalOfficersDto();
                appSvcKeyAppointmentHolderDto.setIndexNo(UUID.randomUUID().toString());
            }
            appSvcKeyAppointmentHolderDtoList.add(appSvcKeyAppointmentHolderDto);
        }
        return appSvcKeyAppointmentHolderDtoList;
    }

    private static AppSvcPrincipalOfficersDto getKeyAppointmentHolderByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            String indexNo) {
        AppSvcPrincipalOfficersDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderDtoList)) {
                for (AppSvcPrincipalOfficersDto appSvcKeyAppointmentHolder : appSvcKeyAppointmentHolderDtoList) {
                    if (indexNo.equals(appSvcKeyAppointmentHolder.getIndexNo())) {
                        result = appSvcKeyAppointmentHolder;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcMedAlertPerson(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson star ..."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String appType = appSubmissionDto.getAppType();
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
        boolean needEdit = rfcOrRenew || isRfi;
        String[] existingPsn = ParamUtil.getStrings(request, "existingPsn");
        String[] licPerson = ParamUtil.getStrings(request, "licPerson");
        String[] assignSelect = ParamUtil.getStrings(request, "assignSel");
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] nationality = ParamUtil.getStrings(request, "nationality");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        String[] isPartEdit = ParamUtil.getStrings(request,"isPartEdit");
        String[] mapIndexNos = ParamUtil.getStrings(request,"mapIndexNo");
        String[] loadingTypes = ParamUtil.getStrings(request,"loadingType");
        List<AppSvcPrincipalOfficersDto> medAlertPersons = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        int length = 0;
        if(assignSelect != null){
            length = assignSelect.length;
        }
        //new and not rfi
        for (int i = 0; i < length; i++) {
            AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
            AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            boolean chooseExisting = false;
            boolean getPageData = false;
            String loadingType = loadingTypes[i];
            boolean loadingByBlur = HcsaAppConst.NEW_PSN.equals(assign) && AppConsts.YES.equals(licPsn)
                            && ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(loadingType);
            //String existPsn = existingPsn[i];
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                if (assign != null) {
                    if (isExistingPsn(assign, licPsn)) {
                        chooseExisting = true;
                    }else if(loadingByBlur){
                        chooseExisting = true;
                    } else {
                        getPageData = true;
                    }
                }
            } else if (needEdit) {
                if (assign != null) {
                    String mapIndexNo = mapIndexNos[i];
                    if (!StringUtil.isEmpty(mapIndexNo)) {
                        //not click edit
                        if (AppConsts.NO.equals(isPartEdit[i])) {
                            appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, mapIndexNo, HcsaAppConst.PAGE_NAME_MAP);
                            medAlertPersons.add(appSvcPrincipalOfficersDto);
                            //change arr
                            mapIndexNos = removeArrIndex(mapIndexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            loadingTypes = removeArrIndex(loadingTypes,i);
                            //dropdown cannot disabled
                            assignSelect = removeArrIndex(assignSelect, i);
                            salutation = removeArrIndex(salutation, i);
                            nationality = removeArrIndex(nationality, i);
                            idType = removeArrIndex(idType, i);
//                            designation = removeArrIndex(designation, i);
//                            existingPsn = removeArrIndex(existingPsn, i);
                            //change arr index
                            --i;
                            --length;
                            continue;
                        }
                    }
                    //isPartEdit->1.click edit button 2.add more psn
                    if(isExistingPsn(assign,licPsn)){
                        //add cgo and choose existing
                        chooseExisting = true;
                    }else if(loadingByBlur){
                        chooseExisting = true;
                    }else{
                        getPageData = true;
                    }
                }
            } else {
                log.info(StringUtil.changeForLog("The current type is not supported"));
            }
            log.info(StringUtil.changeForLog("chooseExisting:"+chooseExisting));
            log.info(StringUtil.changeForLog("getPageData:"+getPageData));
            String assignSel = assignSelect[i];
            if(chooseExisting){
                if (loadingByBlur) {
                    assignSel = ApplicationHelper.getPersonKey(nationality[i], idType[i], idNo[i]);
                }
                appSvcPrincipalOfficersDto = ApplicationHelper.getPsnInfoFromLic(request, assignSel);
                appSvcPrincipalOfficersDto.setLoadingType(loadingType);
                try {
                    appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                } catch (Exception e) {
                    clearAppPsnEditDto(appPsnEditDto);
                    log.error(e.getMessage(), e);
                }
                if (appPsnEditDto.isSalutation()) {
                    ApplicationHelper.setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                }
                if (appPsnEditDto.isIdType()) {
                    ApplicationHelper.setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                }
                //input
                if (appPsnEditDto.isName()) {
                    name = ApplicationHelper.setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                }
                if (appPsnEditDto.isIdNo()) {
                    idNo = ApplicationHelper.setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                }
                if (appPsnEditDto.isNationality()) {
                    ApplicationHelper.setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                }
                if (appPsnEditDto.isMobileNo()) {
                    mobileNo = ApplicationHelper.setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                }
                if (appPsnEditDto.isEmailAddr()) {
                    emailAddress = ApplicationHelper.setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                }
                String mapIndexNo = mapIndexNos[i];
                if(!StringUtil.isEmpty(mapIndexNo)){
                    appSvcPrincipalOfficersDto.setIndexNo(mapIndexNo);
                }
                if(StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())){
                    appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                appSvcPrincipalOfficersDto.setLicPerson(true);
                appSvcPrincipalOfficersDto.setSelectDropDown(true);
                appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                medAlertPersons.add(appSvcPrincipalOfficersDto);
                //change arr index
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn,i);
                loadingTypes = removeArrIndex(loadingTypes,i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                nationality = removeArrIndex(nationality, i);
                --i;
                --length;
            }else if(getPageData){
                String mapIndexNo = mapIndexNos[i];
                if (StringUtil.isEmpty(mapIndexNo)) {
                    appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                } else {
                    appSvcPrincipalOfficersDto.setIndexNo(mapIndexNo);
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                appSvcPrincipalOfficersDto.setSalutation(salutation[i]);
                appSvcPrincipalOfficersDto.setName(name[i]);
                appSvcPrincipalOfficersDto.setIdType(idType[i]);
                appSvcPrincipalOfficersDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
                appSvcPrincipalOfficersDto.setNationality(nationality[i]);
                appSvcPrincipalOfficersDto.setMobileNo(mobileNo[i]);
                String emailAddr = "";
                if(emailAddress != null){
                    if(!StringUtil.isEmpty(emailAddress[i])){
                        emailAddr = StringUtil.viewHtml(emailAddress[i]);
                    }
                }
                appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                if (needEdit && AppConsts.YES.equals(licPsn)) {
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                            appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                    AppSvcPrincipalOfficersDto licsPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                    if(licsPerson != null){
                        appSvcPrincipalOfficersDto.setCurPersonelId(licsPerson.getCurPersonelId());
                    }
                }
                medAlertPersons.add(appSvcPrincipalOfficersDto);
            }
        }
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson end ..."));
        return medAlertPersons;
    }

    public static List<AppSvcBusinessDto> genAppSvcBusinessDtoList(HttpServletRequest request,
            List<AppGrpPremisesDto> appGrpPremisesDtos,
            String appType){
        List<AppSvcBusinessDto> appSvcBusinessDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            int i = 0;
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                AppSvcBusinessDto appSvcBusinessDto = null;
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request,"isPartEdit" + i);
                String businessIndexNo = ParamUtil.getString(request,"businessIndexNo" + i);
                if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                    getPageData = true;
                }else if(AppConsts.YES.equals(isPartEdit)){
                    getPageData = true;
                }else if(!StringUtil.isEmpty(businessIndexNo)){
                    getDataByIndexNo = true;
                }
                log.debug("get data by index no. is {}",getDataByIndexNo);
                log.debug("get page data is {}",getPageData);
                if(getDataByIndexNo){
                    appSvcBusinessDto = getAppSvcBusinessDtoByIndexNo(appSvcRelatedInfoDto, businessIndexNo);
                }else if(getPageData){
                    String businessName = ParamUtil.getString(request,"businessName" + i);
                    appSvcBusinessDto = new AppSvcBusinessDto();
                    appSvcBusinessDto.setBusinessName(businessName);
                    if(StringUtil.isEmpty(businessIndexNo)){
                        appSvcBusinessDto.setBusinessIndexNo(UUID.randomUUID().toString());
                    }else{
                        appSvcBusinessDto.setBusinessIndexNo(businessIndexNo);
                    }
                }
                if(appSvcBusinessDto != null){
                    appSvcBusinessDto.setPremIndexNo(appGrpPremisesDto.getPremisesIndexNo());
                    appSvcBusinessDto.setPremType(appGrpPremisesDto.getPremisesType());
                    appSvcBusinessDto.setPremAddress(appGrpPremisesDto.getAddress());
                    appSvcBusinessDtos.add(appSvcBusinessDto);
                }
                i++;
            }
        }

        return appSvcBusinessDtos;
    }

    private static AppSvcBusinessDto getAppSvcBusinessDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String businessIndexNo){
        AppSvcBusinessDto result = null;
        if(appSvcRelatedInfoDto != null && !StringUtil.isEmpty(businessIndexNo)){
            List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcBusinessDtos)){
                for(AppSvcBusinessDto appSvcBusinessDto:appSvcBusinessDtos){
                    if(businessIndexNo.equals(appSvcBusinessDto.getBusinessIndexNo())){
                        result = appSvcBusinessDto;
                        break;
                    }
                }
            }
        }
        return result;
    }


    private static boolean isExistingPsn(String assign, String licPsn) {
        return !HcsaAppConst.NEW_PSN.equals(assign) && !assign.equals("-1") && AppConsts.YES.equals(licPsn);
    }


    private static void clearAppPsnEditDto(AppPsnEditDto appPsnEditDto) {
        appPsnEditDto.setName(false);
        appPsnEditDto.setSalutation(false);
        appPsnEditDto.setIdType(false);
        appPsnEditDto.setIdNo(false);
        appPsnEditDto.setDesignation(false);
        appPsnEditDto.setMobileNo(false);
        appPsnEditDto.setOfficeTelNo(false);
        appPsnEditDto.setEmailAddr(false);
        appPsnEditDto.setProfessionType(false);
        appPsnEditDto.setProfRegNo(false);
        appPsnEditDto.setSpeciality(false);
        appPsnEditDto.setSpecialityOther(false);
        appPsnEditDto.setSubSpeciality(false);
        appPsnEditDto.setDesignation(false);
    }

    private static String[] removeArrIndex(String[] arrs, int index) {
        if (arrs == null || arrs.length == 0) {
            return new String[]{""};
        }
        String[] newArrs = new String[arrs.length - 1];
        int j = 0;
        for (int i = 0; i < arrs.length; i++) {
            if (i != index) {
                newArrs[j] = arrs[i];
                j++;
            }
        }
        return newArrs;
    }

    private static String getVal(String[] arrs, int index) {
        if (arrs == null || arrs.length == 0 || arrs.length < index) {
            return null;
        }
        return arrs[index];
    }
}
