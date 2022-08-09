package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.RegistrationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
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
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
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
        boolean readonly = ApplicationHelper.readonlyPremises(appSubmissionDto);
        if (readonly) {
            return appSubmissionDto.getAppGrpPremisesDtoList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,
                AppServicesConsts.HCSASERVICEDTOLIST);
        boolean isMultiPremService = ApplicationHelper.isMultiPremService(hcsaServiceDtoList);
        String[] premisesIndexNos = ParamUtil.getStrings(request, "premisesIndexNo");
        String[] chooseExistData = ParamUtil.getStrings(request, "chooseExistData");
        String[] opLengths = ParamUtil.getStrings(request, "opLength");
        String[] nonHcsaLengths = ParamUtil.getStrings(request, "nonHcsaLength");
        String[] retrieveflag = ParamUtil.getStrings(request, "retrieveflag");
        int count = premisesIndexNos.length;
        if (!isMultiPremService) {
            count = 1;
        }
        for (int i = 0; i < count; i++) {
            String premType = ParamUtil.getString(request, "premType" + i);
            String premisesSel = "";
            if (ApplicationConsts.PREMISES_TYPE_PERMANENT.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "permanentSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "conveyanceSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "easMtsSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_MOBILE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "mobileSel" + i);
            } else if (ApplicationConsts.PREMISES_TYPE_REMOTE.equals(premType)) {
                premisesSel = ParamUtil.getString(request, "remoteSel" + i);
            }
            String premIndexNo = getVal(premisesIndexNos, i);
            if (StringUtil.isEmpty(premIndexNo)) {
                log.info(StringUtil.changeForLog("New premise index"));
                premIndexNo = UUID.randomUUID().toString();
            }
            String existingData = getVal(chooseExistData, i);
            // data
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            ControllerHelper.get(request, appGrpPremisesDto, String.valueOf(i));
            String certIssuedDtStr = ParamUtil.getString(request, "certIssuedDt" + i);
            appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
            setPremise(appGrpPremisesDto, premIndexNo, ApplicationHelper.getOldAppSubmissionDto(request));
            List<AppPremNonLicRelationDto> appPremNonLicRelationDtos = IaisCommonUtils.genNewArrayList();
            int nonHcsaLength = 0;
            try {
                nonHcsaLength = Integer.parseInt(nonHcsaLengths[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("Non-hcsa service length can not parse to int"));
            }
            for (int k = 0; k < nonHcsaLength; k++) {
                String coBusinessName = ParamUtil.getString(request, i + "CoBusinessName" + k);
                String coSvcName = ParamUtil.getString(request, i + "CoSvcName" + k);
                if (StringUtil.isEmpty(coBusinessName) && StringUtil.isEmpty(coSvcName)) {
                    continue;
                }
                AppPremNonLicRelationDto dto = new AppPremNonLicRelationDto();
                dto.setBusinessName(coBusinessName);
                dto.setProvidedService(coSvcName);
                dto.setSeqNum(k);
                appPremNonLicRelationDtos.add(dto);
            }
            appGrpPremisesDto.setAppPremNonLicRelationDtos(appPremNonLicRelationDtos);

            AppGrpPremisesDto licPremise = ApplicationHelper.getPremisesFromMap(premisesSel, request);
            if (AppConsts.YES.equals(existingData)) {
                setDataFromExisting(appGrpPremisesDto, licPremise);
            } else {
                //appGrpPremisesDto = new AppGrpPremisesDto();
                if (licPremise != null) {
                    appGrpPremisesDto.setRelatedServices(licPremise.getRelatedServices());
                    appGrpPremisesDto.setHciCode(licPremise.getHciCode());
                }
                appGrpPremisesDto.setClickRetrieve(AppConsts.YES.equals(getVal(retrieveflag, i)));
                String floorNo = ParamUtil.getString(request, i + "FloorNo" + 0);
                String unitNo = ParamUtil.getString(request, i + "UnitNo" + 0);
                appGrpPremisesDto.setFloorNo(floorNo);
                appGrpPremisesDto.setUnitNo(unitNo);

                List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
                int opLength = 0;
                try {
                    opLength = Integer.parseInt(opLengths[i]);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("operation length can not parse to int"));
                }
                for (int k = 1; k < opLength; k++) {
                    floorNo = ParamUtil.getString(request, i + "FloorNo" + k);
                    unitNo = ParamUtil.getString(request, i + "UnitNo" + k);
                    if (StringUtil.isEmpty(floorNo) && StringUtil.isEmpty(unitNo)) {
                        continue;
                    }
                    AppPremisesOperationalUnitDto dto = new AppPremisesOperationalUnitDto();
                    dto.setFloorNo(floorNo);
                    dto.setUnitNo(unitNo);
                    dto.setPremType(premType);
                    dto.setPremVal(premIndexNo);
                    dto.setSeqNum(k);
                    appPremisesOperationalUnitDtos.add(dto);
                }
                appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
                appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
            }
            appGrpPremisesDto.setSeqNum(i + 1);
            appGrpPremisesDto.setHasError(null);
            appGrpPremisesDto.setExistingData(existingData);
            appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
            appGrpPremisesDto.setPremisesType(premType);
            appGrpPremisesDto.setPremisesSelect(premisesSel);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        return appGrpPremisesDtoList;
    }

    private static void setPremise(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo, AppSubmissionDto oldAppSubmissionDto) {
        String oldHciCode = null;
        List<LicenceDto> licenceDtos = null;
        if (oldAppSubmissionDto != null && oldAppSubmissionDto.getAppGrpPremisesDtoList() != null) {
            oldHciCode = oldAppSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(AppGrpPremisesDto::getHciCode)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            licenceDtos = oldAppSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(AppGrpPremisesDto::getLicenceDtos)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            appGrpPremisesDto.setLicenceDtos(licenceDtos);
        }
        log.info(StringUtil.changeForLog("--- Old Hci Code: " + oldHciCode));
        log.info(StringUtil.changeForLog("--- Maybe Affected Licence size: " + (licenceDtos == null ? 0 : licenceDtos.size())));
        appGrpPremisesDto.setOldHciCode(oldHciCode);
        appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
    }

    private static void setDataFromExisting(AppGrpPremisesDto dto, AppGrpPremisesDto licPremise) {
        dto.setPremisesSelect(licPremise.getPremisesSelect());
        dto.setHciName(licPremise.getHciName());
        dto.setPostalCode(licPremise.getPostalCode());
        dto.setAddrType(licPremise.getAddrType());
        dto.setBlkNo(licPremise.getBlkNo());
        dto.setFloorNo(licPremise.getFloorNo());
        dto.setUnitNo(licPremise.getUnitNo());
        dto.setAppPremisesOperationalUnitDtos(MiscUtil.transferEntityDtos(licPremise.getAppPremisesOperationalUnitDtos(),
                AppPremisesOperationalUnitDto.class));
        dto.setStreetName(licPremise.getStreetName());
        dto.setBuildingName(licPremise.getBuildingName());
        dto.setHciCode(licPremise.getHciCode());
        dto.setClickRetrieve(true);
        dto.setRelatedServices(licPremise.getRelatedServices());
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
        for (AppDeclarationDocDto viewDoc : appDeclarationDocDtos) {
            String index = String.valueOf(Optional.ofNullable(viewDoc.getSeqNum()).orElse(0));
            PageShowFileDto pageShowFileDto = new PageShowFileDto();
            pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
            pageShowFileDto.setIndex(index);
            pageShowFileDto.setFileName(viewDoc.getDocName());
            pageShowFileDto.setSize(viewDoc.getDocSize());
            pageShowFileDto.setMd5Code(viewDoc.getMd5Code());
            pageShowFileDto.setFileUploadUrl(viewDoc.getFileRepoId());
            pageShowFileDto.setVersion(Optional.ofNullable(viewDoc.getVersion()).orElse(1));
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
                    docDto.setVersion(Optional.ofNullable(pageShowFileDto.getVersion()).orElse(1));
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
                    appSvcVehicleDto.setStatus(oldAppSvcVehicleDto.getStatus());
                    appSvcVehicleDto.setActCode(ApplicationConsts.VEHICLE_ACTION_CODE_EDIT);
                } else {
                    appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_SUBMIT);
                    appSvcVehicleDto.setActCode(ApplicationConsts.VEHICLE_ACTION_CODE_ADD);
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

    public static List<AppSvcPrincipalOfficersDto> genAppSvcClinicalDirectorDto(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto start ..."));
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = genKeyPersonnels(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, "",
                request);
        log.debug(StringUtil.changeForLog("gen app svc clinical director dto end ..."));
        return appSvcCgoDtoList;
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


    public static List<AppSvcPrincipalOfficersDto> genAppSvcPrincipalOfficersDtos(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "", request);
    }

    public static List<AppSvcPrincipalOfficersDto> genAppSvcNomineeDtos(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "dpo", request);
    }

    /*
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
                                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
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
                        setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isNationality()) {
                        setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        setPsnValue(designation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if (appPsnEditDto.isOtherDesignation()) {
                        if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(appSvcPrincipalOfficersDto.getDesignation())) {
                            setPsnValue(otherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                        } else {
                            otherDesignations = removeArrIndex(otherDesignations, i);
                        }
                    }

                    if (appPsnEditDto.isName()) {
                        name = setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        idNo = setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        mobileNo = setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        officeTelNo = setPsnValue(officeTelNo, i, appSvcPrincipalOfficersDto, "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        emailAddress = setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
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
                    //appSvcPrincipalOfficersDto.setSelectDropDown(true);
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
                                        ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
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
                        setPsnValue(deputyIdType, i, appSvcPrincipalOfficersDto, "idType");
                    }
                    if (appPsnEditDto.isSalutation()) {
                        setPsnValue(deputySalutation, i, appSvcPrincipalOfficersDto, "salutation");
                    }
                    if (appPsnEditDto.isNationality()) {
                        setPsnValue(deputyNationlity, i, appSvcPrincipalOfficersDto, "nationality");
                    }
                    if (appPsnEditDto.isDesignation()) {
                        setPsnValue(deputyDesignation, i, appSvcPrincipalOfficersDto, "designation");
                    }
                    if (appPsnEditDto.isOtherDesignation() && MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(
                            appSvcPrincipalOfficersDto.getDesignation())) {
                        setPsnValue(deputyOtherDesignations, i, appSvcPrincipalOfficersDto, "otherDesignation");
                    }
                    //input
                    if (appPsnEditDto.isName()) {
                        deputyName = setPsnValue(deputyName, i, appSvcPrincipalOfficersDto, "name");
                    }
                    if (appPsnEditDto.isIdNo()) {
                        deputyIdNo = setPsnValue(deputyIdNo, i, appSvcPrincipalOfficersDto, "idNo");
                    }
                    if (appPsnEditDto.isMobileNo()) {
                        deputyMobileNo = setPsnValue(deputyMobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                    }
                    if (appPsnEditDto.isOfficeTelNo()) {
                        deputyOfficeTelNo = setPsnValue(deputyOfficeTelNo, i, appSvcPrincipalOfficersDto,
                                "officeTelNo");
                    }
                    if (appPsnEditDto.isEmailAddr()) {
                        deputyEmailAddr = setPsnValue(deputyEmailAddr, i, appSvcPrincipalOfficersDto, "emailAddr");
                    }
                    appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    //appSvcPrincipalOfficersDto.setSelectDropDown(true);
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
    }*/

    private static AppSvcPrincipalOfficersDto getPsnByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo,
            String psnType) {
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(indexNo)) {
            List<AppSvcPrincipalOfficersDto> psnDtos = null;
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                psnDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
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

    public static List<AppSvcPrincipalOfficersDto> genAppSvcGovernanceOfficersDto(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcCgoDto start ...."));
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "", request);
        log.info(StringUtil.changeForLog("genAppSvcCgoDto end ...."));
        return appSvcCgoDtoList;
    }

    public static List<AppSvcPrincipalOfficersDto> genKeyPersonnels(String psnType, String prefix, HttpServletRequest request) {
        List<AppSvcPrincipalOfficersDto> personList = IaisCommonUtils.genNewArrayList();
        String[] licPerson = ParamUtil.getStrings(request, prefix + "licPerson");
        String[] isPartEdit = ParamUtil.getStrings(request, prefix + "isPartEdit");
        String[] indexNos = ParamUtil.getStrings(request, prefix + "indexNo");
        String[] assignSelect = ParamUtil.getStrings(request, prefix + "assignSelVal");
        int size = 0;
        if (assignSelect != null && assignSelect.length > 0) {
            size = assignSelect.length;
        }
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String appType = appSubmissionDto.getAppType();
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto currSvcInfoDto = ApplicationHelper.getAppSvcRelatedInfo(appSubmissionDto, currSvcId,
                appSubmissionDto.getRfiAppNo());

        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        for (int i = 0; i < size; i++) {
            AppSvcPrincipalOfficersDto person = null;
            String indexNo = indexNos[i];
            String assign = assignSelect[i];
            String licPsn = licPerson[i];
            boolean pageData = false;
            boolean nonChanged = false;
            if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                pageData = true;
            } else if (AppConsts.YES.equals(getVal(isPartEdit, i))) {
                pageData = true;
            } else if (!StringUtil.isEmpty(indexNo)) {
                nonChanged = true;
            }
            log.info(StringUtil.changeForLog("Non changed:" + nonChanged));
            log.info(StringUtil.changeForLog("PageData:" + pageData));
            if (nonChanged) {
                person = ApplicationHelper.getKeyPersonnels(psnType, currSvcInfoDto).stream()
                        .filter(dto -> Objects.equals(indexNo, dto.getIndexNo()))
                        .findAny()
                        .orElseGet(AppSvcPrincipalOfficersDto::new);
            } else if (pageData) {
                AppPsnEditDto appPsnEditDto = null;
                if (isExistingPsn(assign, licPsn)) {
                    person = ApplicationHelper.getPsnInfoFromLic(request, assign);
                    appPsnEditDto = ApplicationHelper.setNeedEditField(person);
                }
                boolean needLoadName = isNeedLoadName(appType, licPsn);
                person = genKeyPersonnel(person, appPsnEditDto, prefix, String.valueOf(i), needLoadName, request);
            } else {
                log.info(StringUtil.changeForLog("Invalid data!!!"));
                continue;
            }
            if (StringUtil.isEmpty(indexNo)) {
                person.setIndexNo(UUID.randomUUID().toString());
            } else {
                person.setIndexNo(indexNo);
            }
            person.setLicPerson(AppConsts.YES.equals(licPsn));
            person.setAssignSelect(assign);
            person.setPsnType(psnType);
            personList.add(person);
        }
        log.info(StringUtil.changeForLog(StringUtil.changeForLog(psnType + " size: " + personList.size())));
        return personList;
    }

    private static AppSvcPrincipalOfficersDto genKeyPersonnel(AppSvcPrincipalOfficersDto person, AppPsnEditDto appPsnEditDto,
            String prefix, String suffix, boolean needLoadName, HttpServletRequest request) {
        if (person == null) {
            person = new AppSvcPrincipalOfficersDto();
        }
        setPsnValue(person, appPsnEditDto, "salutation", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "name", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "idType", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "idNo", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "nationality", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "designation", prefix, suffix, request);

        if (appPsnEditDto == null || appPsnEditDto.isOtherDesignation()) {
            if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(person.getDesignation())) {
                String otherDesignation = ParamUtil.getString(request, "otherDesignation" + suffix);
                person.setOtherDesignation(otherDesignation);
            } else {
                person.setOtherDesignation(null);
            }
        }
        setPsnValue(person, appPsnEditDto, "professionBoard", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "professionType", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "profRegNo", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "typeOfCurrRegi", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "currRegiDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "praCerEndDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "typeOfRegister", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "specialityOther", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "specialtyGetDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "otherQualification", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "mobileNo", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "emailAddr", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "holdCerByEMS", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "aclsExpiryDate", prefix, suffix, true, request);
        setPsnValue(person, appPsnEditDto, "relevantExperience", prefix, suffix, request);
        setPsnValue(person, appPsnEditDto, "bclsExpiryDate", prefix, suffix, true, request);

        if (person.getPsnEditDto() == null) {
            if (appPsnEditDto == null) {
                appPsnEditDto = ApplicationHelper.setNeedEditField(person);
            }
            person.setPsnEditDto(appPsnEditDto);
        }

        String profRegNo = person.getProfRegNo();
        if (!StringUtil.isEmpty(profRegNo)) {
            ProfessionalResponseDto professionalResponseDto = getAppCommService().retrievePrsInfo(profRegNo);
            if (professionalResponseDto != null) {
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getSpecialty())) {
                    person.setSpeciality("");
                } else {
                    person.setSpeciality(professionalResponseDto.getSpecialty().get(0));
                }
                if (IaisCommonUtils.isEmpty(professionalResponseDto.getSubspecialty())) {
                    person.setSubSpeciality("");
                } else {
                    person.setSubSpeciality(professionalResponseDto.getSubspecialty().get(0));
                }
                String specialtyGetDateStr = "";
                List<String> entryDateSpecialist = professionalResponseDto.getEntryDateSpecialist();
                if (entryDateSpecialist != null && entryDateSpecialist.size() > 0) {
                    specialtyGetDateStr = entryDateSpecialist.get(0);
                }
                person.setSpecialtyGetDateStr(specialtyGetDateStr);
                if (StringUtil.isEmpty(specialtyGetDateStr)) {
                    person.setSpecialtyGetDate(null);
                } else {
                    Date date = DateUtil.parseDate(specialtyGetDateStr, Formatter.DATE);
                    person.setSpecialtyGetDate(date);
                }

                if (IaisCommonUtils.isEmpty(professionalResponseDto.getQualification())) {
                    person.setQualification("");
                } else {
                    person.setQualification(professionalResponseDto.getQualification().get(0));
                }

                String typeOfCurrRegi = "";
                String currRegiDateStr = "";
                String praCerEndDateStr = "";
                String typeOfRegister = "";
                List<RegistrationDto> registrationDtos = professionalResponseDto.getRegistration();
                if (registrationDtos != null && registrationDtos.size() > 0) {
                    RegistrationDto registrationDto = registrationDtos.get(0);
                    typeOfCurrRegi = registrationDto.getRegistrationType();
                    currRegiDateStr = registrationDto.getRegStartDate();
                    praCerEndDateStr = registrationDto.getPcEndDate();
                    typeOfRegister = registrationDto.getRegisterType();
                }
                person.setTypeOfCurrRegi(typeOfCurrRegi);
                person.setTypeOfRegister(typeOfRegister);
                person.setCurrRegiDateStr(currRegiDateStr);
                if (StringUtil.isEmpty(currRegiDateStr)) {
                    person.setCurrRegiDate(null);
                } else {
                    Date date = DateUtil.parseDate(currRegiDateStr, Formatter.DATE);
                    person.setCurrRegiDate(date);
                }
                person.setPraCerEndDateStr(praCerEndDateStr);
                if (StringUtil.isEmpty(praCerEndDateStr)) {
                    person.setPraCerEndDate(null);
                } else {
                    Date date = DateUtil.parseDate(praCerEndDateStr, Formatter.DATE);
                    person.setPraCerEndDate(date);
                }
                if (needLoadName && !StringUtil.isEmpty(professionalResponseDto.getName())) {
                    person.setName(professionalResponseDto.getName());
                }
            }
        }
        return person;
    }

    private static void setPsnValue(AppSvcPrincipalOfficersDto person, AppPsnEditDto appPsnEditDto, String fieldName,
            String prefix, String suffix, HttpServletRequest request) {
        setPsnValue(person, appPsnEditDto, fieldName, prefix, suffix, false, request);
    }

    private static void setPsnValue(AppSvcPrincipalOfficersDto person, AppPsnEditDto appPsnEditDto, String fieldName,
            String prefix, String suffix, boolean isDate, HttpServletRequest request) {
        if (appPsnEditDto != null) {
            boolean canSet = ReflectionUtil.getPropertyObj(appPsnEditDto, fieldName);
            if (!canSet) {
                return;
            }
        }
        if (isDate) {
            String data = ParamUtil.getString(request, prefix + fieldName + suffix);
            Date value = null;
            if (CommonValidator.isDate(data)) {
                try {
                    value = Formatter.parseDate(data);
                } catch (ParseException e) {
                    log.info(StringUtil.changeForLog(e.getMessage()), e);
                }
            }
            ReflectionUtil.setPropertyObj(fieldName + "Str", value, person);
            ReflectionUtil.setPropertyObj(fieldName, value, person);
        } else {
            String data = ParamUtil.getString(request, prefix + fieldName + suffix);
            ReflectionUtil.setPropertyObj(fieldName, data, person);
        }
    }

    private static String[] setPsnValue(String[] arr, int i, AppSvcPrincipalOfficersDto person, String fieldName) {
        if (arr == null || arr.length <= i) {
            return new String[0];
        }
        ReflectionUtil.setPropertyObj(fieldName, arr[i], person);
        return removeArrIndex(arr, i);
    }

   /* private static AppSvcPrincipalOfficersDto getAppSvcCgoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
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
    }*/

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

    private static AppSvcPersonnelDto getAppSvcPersonnelDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String indexNo) {
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

    public static List<AppSvcPrincipalOfficersDto> genAppSvcKeyAppointmentHolder(HttpServletRequest request) {
        return genKeyPersonnels(ApplicationConsts.PERSONNEL_PSN_KAH, "", request);
    }


    public static List<AppSvcPrincipalOfficersDto> genAppSvcMedAlertPerson(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("genAppSvcMedAlertPerson star ..."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String appType = appSubmissionDto.getAppType();
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        boolean rfcOrRenew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType);
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
        String[] isPartEdit = ParamUtil.getStrings(request, "isPartEdit");
        String[] mapIndexNos = ParamUtil.getStrings(request, "mapIndexNo");
        String[] loadingTypes = ParamUtil.getStrings(request, "loadingType");
        List<AppSvcPrincipalOfficersDto> medAlertPersons = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        int length = 0;
        if (assignSelect != null) {
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
                    } else if (loadingByBlur) {
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
                            appSvcPrincipalOfficersDto = getPsnByIndexNo(appSvcRelatedInfoDto, mapIndexNo,
                                    ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                            medAlertPersons.add(appSvcPrincipalOfficersDto);
                            //change arr
                            mapIndexNos = removeArrIndex(mapIndexNos, i);
                            isPartEdit = removeArrIndex(isPartEdit, i);
                            licPerson = removeArrIndex(licPerson, i);
                            loadingTypes = removeArrIndex(loadingTypes, i);
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
                try {
                    appPsnEditDto = ApplicationHelper.setNeedEditField(appSvcPrincipalOfficersDto);
                } catch (Exception e) {
                    clearAppPsnEditDto(appPsnEditDto);
                    log.error(e.getMessage(), e);
                }
                if (appPsnEditDto.isSalutation()) {
                    setPsnValue(salutation, i, appSvcPrincipalOfficersDto, "salutation");
                }
                if (appPsnEditDto.isIdType()) {
                    setPsnValue(idType, i, appSvcPrincipalOfficersDto, "idType");
                }
                //input
                if (appPsnEditDto.isName()) {
                    name = setPsnValue(name, i, appSvcPrincipalOfficersDto, "name");
                }
                if (appPsnEditDto.isIdNo()) {
                    idNo = setPsnValue(idNo, i, appSvcPrincipalOfficersDto, "idNo");
                }
                if (appPsnEditDto.isNationality()) {
                    setPsnValue(nationality, i, appSvcPrincipalOfficersDto, "nationality");
                }
                if (appPsnEditDto.isMobileNo()) {
                    mobileNo = setPsnValue(mobileNo, i, appSvcPrincipalOfficersDto, "mobileNo");
                }
                if (appPsnEditDto.isEmailAddr()) {
                    emailAddress = setPsnValue(emailAddress, i, appSvcPrincipalOfficersDto, "emailAddr");
                }
                String mapIndexNo = mapIndexNos[i];
                if (!StringUtil.isEmpty(mapIndexNo)) {
                    appSvcPrincipalOfficersDto.setIndexNo(mapIndexNo);
                }
                if (StringUtil.isEmpty(appSvcPrincipalOfficersDto.getIndexNo())) {
                    appSvcPrincipalOfficersDto.setIndexNo(UUID.randomUUID().toString());
                }
                appSvcPrincipalOfficersDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                appSvcPrincipalOfficersDto.setAssignSelect(assignSel);
                appSvcPrincipalOfficersDto.setLicPerson(true);
                //appSvcPrincipalOfficersDto.setSelectDropDown(true);
                appSvcPrincipalOfficersDto.setPsnEditDto(appPsnEditDto);
                medAlertPersons.add(appSvcPrincipalOfficersDto);
                //change arr index
                licPerson = removeArrIndex(licPerson, i);
                existingPsn = removeArrIndex(existingPsn, i);
                loadingTypes = removeArrIndex(loadingTypes, i);
                //dropdown cannot disabled
                assignSelect = removeArrIndex(assignSelect, i);
                salutation = removeArrIndex(salutation, i);
                idType = removeArrIndex(idType, i);
                nationality = removeArrIndex(nationality, i);
                --i;
                --length;
            } else if (getPageData) {
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
                if (emailAddress != null) {
                    if (!StringUtil.isEmpty(emailAddress[i])) {
                        emailAddr = StringUtil.viewHtml(emailAddress[i]);
                    }
                }
                appSvcPrincipalOfficersDto.setEmailAddr(emailAddr);
                if (needEdit && AppConsts.YES.equals(licPsn)) {
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    String personKey = ApplicationHelper.getPersonKey(appSvcPrincipalOfficersDto.getNationality(),
                            appSvcPrincipalOfficersDto.getIdType(), appSvcPrincipalOfficersDto.getIdNo());
                    AppSvcPrincipalOfficersDto licsPerson = ApplicationHelper.getPsnInfoFromLic(request, personKey);
                    if (licsPerson != null) {
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
            List<AppGrpPremisesDto> appGrpPremisesDtos, String appType) {
        List<AppSvcBusinessDto> appSvcBusinessDtos = IaisCommonUtils.genNewArrayList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = ApplicationHelper.getAppSvcRelatedInfo(request, currentSvcId);
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            int i = 0;
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                AppSvcBusinessDto appSvcBusinessDto = null;
                boolean getDataByIndexNo = false;
                boolean getPageData = false;
                String isPartEdit = ParamUtil.getString(request, "isPartEdit" + i);
                String businessIndexNo = ParamUtil.getString(request, "businessIndexNo" + i);
                if (!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    getPageData = true;
                } else if (AppConsts.YES.equals(isPartEdit)) {
                    getPageData = true;
                } else if (!StringUtil.isEmpty(businessIndexNo)) {
                    getDataByIndexNo = true;
                }
                log.debug("get data by index no. is {}", getDataByIndexNo);
                log.debug("get page data is {}", getPageData);
                if (getDataByIndexNo) {
                    appSvcBusinessDto = getAppSvcBusinessDtoByIndexNo(appSvcRelatedInfoDto, businessIndexNo);
                } else if (getPageData) {
                    appSvcBusinessDto = new AppSvcBusinessDto();
                    boolean getOHData=true;
                    String premisesType = appGrpPremisesDto.getPremisesType();
                    if (ApplicationConsts.PREMISES_TYPE_MOBILE.equals(premisesType)||ApplicationConsts.PREMISES_TYPE_REMOTE.equals(premisesType)){
                        getOHData=false;
                    }
                    String serviceCode=ParamUtil.getString(request,"currService"+i);
                    if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)||AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(serviceCode)){
                        getOHData=false;
                    }

                    List<OperationHoursReloadDto> weeklyDtoList = IaisCommonUtils.genNewArrayList();
                    List<OperationHoursReloadDto> phDtoList = IaisCommonUtils.genNewArrayList();
                    List<AppPremEventPeriodDto> eventList = IaisCommonUtils.genNewArrayList();

                    String businessName = ParamUtil.getString(request, "businessName" + i);
                    String contactNo = ParamUtil.getString(request, "contactNo" + i);
                    String emailAddr = ParamUtil.getString(request, "emailAddr" + i);

                    if (getOHData){
                        int weeklyLength=ParamUtil.getInt(request,"weeklyLength"+ i);
                        int phLength=ParamUtil.getInt(request,"phLength"+ i);
                        int eventLength=ParamUtil.getInt(request,"eventLength"+ i);

                        //weekly
                        for (int j = 0; j < weeklyLength; j++) {
                            OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                            String[] weeklyVal = ParamUtil.getStrings(request,"onSiteWeekly"+i+j);
                            String allDay = ParamUtil.getString(request,"onSiteWeeklyAllDay"+i+j);
                            //reload
                            String weeklySelect = StringUtil.arrayToString(weeklyVal);
                            weeklyDto.setSelectVal(weeklySelect);
                            if (weeklyVal != null) {
                                List<String> selectValList = Arrays.asList(weeklyVal);
                                weeklyDto.setSelectValList(selectValList);
                            }
                            if (AppConsts.TRUE.equals(allDay)) {
                                weeklyDto.setSelectAllDay(true);
                                weeklyDto.setStartFromHH(null);
                                weeklyDto.setStartFromMM(null);
                                weeklyDto.setStartFrom(new Time(0,0,0));
                                weeklyDto.setEndToHH(null);
                                weeklyDto.setEndToMM(null);
                                weeklyDto.setEndTo(new Time(0,0,0));
                            } else {
                                String weeklyStartHH = ParamUtil.getString(request,"onSiteWeeklyStartHH"+i+j);
                                String weeklyStartMM = ParamUtil.getString(request,"onSiteWeeklyStartMM"+i+j);
                                int weeklyStartH=weeklyStartHH!=null?Integer.parseInt(weeklyStartHH):0;
                                int weeklyStartM=weeklyStartMM!=null?Integer.parseInt(weeklyStartMM):0;
                                String weeklyEndHH = ParamUtil.getString(request,"onSiteWeeklyEndHH"+i+j);
                                String weeklyEndMM = ParamUtil.getString(request,"onSiteWeeklyEndMM"+i+j);
                                int weeklyEndH=weeklyEndHH!=null?Integer.parseInt(weeklyEndHH):0;
                                int weeklyEndM=weeklyEndMM!=null?Integer.parseInt(weeklyEndMM):0;

                                weeklyDto.setStartFromHH(weeklyStartHH);
                                weeklyDto.setStartFromMM(weeklyStartMM);
                                weeklyDto.setStartFrom(new Time(weeklyStartH,weeklyStartM,0));
                                weeklyDto.setEndToHH(weeklyEndHH);
                                weeklyDto.setEndToMM(weeklyEndMM);
                                weeklyDto.setEndTo(new Time(weeklyEndH,weeklyEndM,0));
                            }
                            weeklyDtoList.add(weeklyDto);
                        }

                        //ph
                        for (int j = 0; j < phLength; j++) {
                            OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                            String[] phVal = ParamUtil.getStrings(request, "onSitePubHoliday"+i+j);
                            String allDay = ParamUtil.getString(request,"onSitePhAllDay"+i+j);
                            //reload
                            String phSelect = StringUtil.arrayToString(phVal);
                            phDto.setSelectVal(phSelect);
                            if (phSelect != null) {
                                List<String> selectValList = Arrays.asList(phVal);
                                phDto.setSelectValList(selectValList);
                            }
                            if (AppConsts.TRUE.equals(allDay)) {
                                phDto.setSelectAllDay(true);
                                phDto.setStartFromHH(null);
                                phDto.setStartFromMM(null);
                                phDto.setStartFrom(new Time(0,0,0));
                                phDto.setEndToHH(null);
                                phDto.setEndToMM(null);
                                phDto.setEndTo(new Time(0,0,0));
                                phDtoList.add(phDto);
                            } else {
                                String phStartHH = ParamUtil.getString(request,"onSitePhStartHH"+i+j);
                                String phStartMM = ParamUtil.getString(request,"onSitePhStartMM"+i+j);
                                int phStartH=phStartHH!=null?Integer.parseInt(phStartHH):0;
                                int phStartM=phStartMM!=null?Integer.parseInt(phStartMM):0;
                                String phEndHH = ParamUtil.getString(request,"onSitePhEndHH"+i+j);
                                String phEndMM = ParamUtil.getString(request,"onSitePhEndMM"+i+j);
                                int phEndH=phEndHH!=null?Integer.parseInt(phEndHH):0;
                                int phEndM=phEndMM!=null?Integer.parseInt(phEndMM):0;
                                
                                phDto.setStartFromHH(phStartHH);
                                phDto.setStartFromMM(phStartMM);
                                phDto.setStartFrom(new Time(phStartH,phStartM,0));
                                phDto.setEndToHH(phEndHH);
                                phDto.setEndToMM(phEndMM);
                                phDto.setEndTo(new Time(phEndH,phEndM,0));
                                if (!StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(
                                        phStartMM) || !StringUtil.isEmpty(phEndHH) || !StringUtil.isEmpty(phEndMM)) {
                                    phDtoList.add(phDto);
                                }
                            }

                        }

                        //event
                        for (int j = 0; j < eventLength; j++) {
                            AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                            String eventName = ParamUtil.getString(request, "onSiteEvent"+i+j);
                            String eventStartStr = ParamUtil.getString(request,"onSiteEventStart"+i+j);
                            Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                            String eventEndStr = ParamUtil.getString(request,"onSiteEventEnd"+i+ j);
                            Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                            appPremEventPeriodDto.setEventName(eventName);
                            appPremEventPeriodDto.setStartDate(eventStart);
                            appPremEventPeriodDto.setStartDateStr(eventStartStr);
                            appPremEventPeriodDto.setEndDate(eventEnd);
                            appPremEventPeriodDto.setEndDateStr(eventEndStr);
                            if (!StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(
                                    eventEndStr)) {
                                eventList.add(appPremEventPeriodDto);
                            }
                        }
                    }

                    appSvcBusinessDto.setCurrService(serviceCode);
                    appSvcBusinessDto.setBusinessName(businessName);
                    appSvcBusinessDto.setContactNo(contactNo);
                    appSvcBusinessDto.setEmailAddr(emailAddr);

                    if (getOHData) {
                        appSvcBusinessDto.setWeeklyDtoList(weeklyDtoList);
                        appSvcBusinessDto.setPhDtoList(phDtoList);
                        appSvcBusinessDto.setEventDtoList(eventList);

                    }else {
                        appSvcBusinessDto.setWeeklyDtoList(null);
                        appSvcBusinessDto.setPhDtoList(null);
                        appSvcBusinessDto.setEventDtoList(null);
                    }
                    if (StringUtil.isEmpty(businessIndexNo)) {
                        appSvcBusinessDto.setBusinessIndexNo(UUID.randomUUID().toString());
                    } else {
                        appSvcBusinessDto.setBusinessIndexNo(businessIndexNo);
                    }
                }
                if (appSvcBusinessDto != null) {
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

    private static AppSvcBusinessDto getAppSvcBusinessDtoByIndexNo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, String businessIndexNo) {
        AppSvcBusinessDto result = null;
        if (appSvcRelatedInfoDto != null && !StringUtil.isEmpty(businessIndexNo)) {
            List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcBusinessDtos)) {
                for (AppSvcBusinessDto appSvcBusinessDto : appSvcBusinessDtos) {
                    if (businessIndexNo.equals(appSvcBusinessDto.getBusinessIndexNo())) {
                        result = appSvcBusinessDto;
                        break;
                    }
                }
            }
        }
        return result;
    }


    private static boolean isExistingPsn(String assign, String licPsn) {
        return !HcsaAppConst.NEW_PSN.equals(assign) && !"-1".equals(assign) && AppConsts.YES.equals(licPsn);
    }

    private static boolean isNeedLoadName(String appType, String licPsn) {
        return !AppConsts.YES.equals(licPsn) && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
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

    public static void setSvcDocuments(List<DocumentShowDto> documentShowDtoList, String appGrpId, String appNo,
            int maxPsnTypeNum, Map<String, File> saveFileMap, HttpServletRequest request) {
        if (documentShowDtoList == null || documentShowDtoList.isEmpty()) {
            return;
        }
        for (DocumentShowDto documentShowDto : documentShowDtoList) {
            String premisesVal = documentShowDto.getPremisesVal();
            List<DocSectionDto> docSectionList = documentShowDto.getDocSectionList();
            for (DocSectionDto docSectionDto : docSectionList) {
                String svcCode = docSectionDto.getSvcCode();
                List<DocSecDetailDto> docSecDetailList = docSectionDto.getDocSecDetailList();
                int secSize = docSecDetailList.size();
                for (int j = 0; j < secSize; j++) {
                    String docKey = ApplicationHelper.getSvcDocKey(j, svcCode, premisesVal);
                    DocSecDetailDto docSecDetailDto = docSecDetailList.get(j);
                    List<AppSvcDocDto> appSvcDocDtoList = genSvcPersonDoc(documentShowDto, docSectionDto, docSecDetailDto, docKey,
                            appGrpId, appNo, saveFileMap, request);
                    if (StringUtil.isNotEmpty(docSecDetailDto.getPsnType()) && !appSvcDocDtoList.isEmpty()) {
                        Optional<Integer> max = appSvcDocDtoList.stream()
                                .map(AppSvcDocDto::getPersonTypeNum)
                                .filter(Objects::nonNull)
                                .max(Comparator.naturalOrder());
                        Integer psnTypeNum = max.isPresent() ? max.get() : ++maxPsnTypeNum;
                        appSvcDocDtoList.forEach(doc -> doc.setPersonTypeNum(psnTypeNum));
                    }
                    docSecDetailDto.setAppSvcDocDtoList(appSvcDocDtoList);
                }
            }
        }
    }

    private static List<AppSvcDocDto> genSvcPersonDoc(DocumentShowDto documentShowDto, DocSectionDto docSectionDto,
            DocSecDetailDto docSecDetailDto, String docKey, String appGrpId, String appNo, Map<String, File> saveFileMap,
            HttpServletRequest request) {
        List<AppSvcDocDto> newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                IaisEGPConstant.SEESION_FILES_MAP_AJAX + docKey);
        if (fileMap != null) {
            fileMap.forEach((k, v) -> {
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index + docKey.length());
                int seqNum = -1;
                try {
                    seqNum = Integer.parseInt(seqNumStr);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                AppSvcDocDto appSvcDocDto = getAppSvcDoc(docSecDetailDto, seqNum);
                if (v != null) {
                    if (appSvcDocDto == null) {
                        appSvcDocDto = new AppSvcDocDto();
                    }
                    String premVal = documentShowDto.getPremisesVal();
                    String svcDocId = docSecDetailDto.getConfigId();
                    String psnIndexNo = docSecDetailDto.getPsnIndexNo();
                    String svcId = docSectionDto.getSvcId();
                    appSvcDocDto.setSvcId(svcId);
                    appSvcDocDto.setSvcDocId(svcDocId);
                    appSvcDocDto.setUpFileName(docSecDetailDto.getDocTitle());
                    appSvcDocDto.setDocName(v.getName());
                    long size = v.length() / 1024;
                    appSvcDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    appSvcDocDto.setMd5Code(FileUtils.getFileMd5(v));
                    appSvcDocDto.setPremisesVal(premVal);
                    appSvcDocDto.setPremisesType(documentShowDto.getPremisesType());
                    appSvcDocDto.setPsnIndexNo(psnIndexNo);
                    appSvcDocDto.setSeqNum(seqNum);
                    //appSvcDocDto.setDupForPrem(documentShowDto.getDupForPrem());
                    appSvcDocDto.setDupForPerson(docSecDetailDto.getDupForPerson());
                    appSvcDocDto.setPersonType(docSecDetailDto.getPsnType());
                    setAppSvcDocDtoFileds(appSvcDocDto, appGrpId, appNo);
                    String key = ApplicationHelper.getFileMapKey(premVal, svcId, svcDocId, psnIndexNo, seqNum);
                    saveFileMap.put(key, v);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k, null);
                if (appSvcDocDto != null) {
                    newAppSvcDocDtoList.add(appSvcDocDto);
                }
            });
        }
        return newAppSvcDocDtoList;
    }

    private static AppSvcDocDto getAppSvcDoc(DocSecDetailDto docSecDetailDto, int seqNum) {
        if (!docSecDetailDto.isExistDoc()) {
            return null;
        }
        return docSecDetailDto.getAppSvcDocDtoList().stream()
                .filter(doc -> seqNum == doc.getSeqNum())
                .findAny()
                .orElse(null);
    }

    private static void setAppSvcDocDtoFileds(AppSvcDocDto appSvcDocDto, String appGrpId, String appNo) {
        String svcDocId = appSvcDocDto.getSvcDocId();
        int seqNum = appSvcDocDto.getSeqNum();
        Integer version = 1;
        AppSvcDocDto maxVersionSDoc = getAppCommService().getMaxVersionSvcSpecDoc(svcDocId, appGrpId, appNo, seqNum);
        if (maxVersionSDoc != null && maxVersionSDoc.getVersion() != null) {
            version = maxVersionSDoc.getVersion();
        }
        appSvcDocDto.setVersion(version);
        if (appSvcDocDto.getSubmitDt() == null) {
            appSvcDocDto.setSubmitDt(new Date());
        }
        if (StringUtil.isEmpty(appSvcDocDto.getSubmitBy())) {
            appSvcDocDto.setSubmitBy(ApplicationHelper.getLoginContext().getUserId());
        }
    }

    public static void setSpecialisedData(List<AppPremSpecialisedDto> appPremSpecialisedDtoList,
            String svcCode, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appPremSpecialisedDtoList)) {
            return;
        }
        for (AppPremSpecialisedDto specialisedDto : appPremSpecialisedDtoList) {
            if (!Objects.equals(specialisedDto.getBaseSvcCode(), svcCode)) {
                continue;
            }
            String premisesVal = specialisedDto.getPremisesVal();
            specialisedDto.setAppPremScopeDtoList(genAppPremScopeDtoList(specialisedDto.getAppPremScopeDtoList(),
                    premisesVal, "", request));
            specialisedDto.setAppPremSubSvcRelDtoList(genAppPremSubSvcRelDtoList(specialisedDto.getAppPremSubSvcRelDtoList(),
                    premisesVal, "", request));
            specialisedDto.initAllAppPremScopeDtoList();
            specialisedDto.initAllAppPremSubSvcRelDtoList();
        }
    }

    private static List<AppPremSubSvcRelDto> genAppPremSubSvcRelDtoList(List<AppPremSubSvcRelDto> appPremScopeDtoList,
            String premisesVal, String parentId, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList)) {
            return null;
        }
        String[] values = ParamUtil.getStrings(request, premisesVal + "_" + parentId + "_service");
        for (AppPremSubSvcRelDto relDto : appPremScopeDtoList) {
            relDto.setChecked(StringUtil.isIn(relDto.getSvcId(), values));
            relDto.setAppPremSubSvcRelDtos(genAppPremSubSvcRelDtoList(relDto.getAppPremSubSvcRelDtos(), premisesVal,
                    relDto.getSvcId(), request));
        }
        return appPremScopeDtoList;
    }

    private static List<AppPremScopeDto> genAppPremScopeDtoList(List<AppPremScopeDto> appPremScopeDtoList,
            String premisesVal, String parentId, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appPremScopeDtoList)) {
            return null;
        }
        String[] values = ParamUtil.getStrings(request, premisesVal + "_" + parentId + "_sub_type");
        for (AppPremScopeDto scopeDto : appPremScopeDtoList) {
            scopeDto.setChecked(StringUtil.isIn(scopeDto.getSubTypeId(), values));
            scopeDto.setAppPremScopeDtos(genAppPremScopeDtoList(scopeDto.getAppPremScopeDtos(), premisesVal,
                    scopeDto.getSubTypeId(), request));
        }
        return appPremScopeDtoList;
    }

}
