package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.job.executor.util.DateUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ELISInterfaceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.DsElisDoctorDto;
import com.ecquaria.cloud.moh.iais.dto.DsElisLicenceDto;
import com.ecquaria.cloud.moh.iais.dto.DsElisUserDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.DataSubmissionElisInterfaceService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.AppConsts.COMMON_STATUS_ACTIVE;
import static com.ecquaria.cloud.moh.iais.common.constant.AppConsts.COMMON_STATUS_IACTIVE;
import static com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts.DP_PATIENTINFO_ADDRESS_TYPE_APT_BLK;
import static com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts.DP_PATIENTINFO_ADDRESS_TYPE_WITHOUT_APT_BLK;
import static com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants.LICENSEE_TYPE_CORPPASS;

@Slf4j
@Service
public class DataSubmissionElisInterfaceServiceImpl implements DataSubmissionElisInterfaceService {
    @Value("${iais.datasubmission.elis.path}")
    private String sharedPath;

    private static final String LICENCE_FILE = "eLIS_HALP_Licence_Data_";
    private static final String USER_FILE = "eLIS_HALP_Internet_User_Data_";
    private static final String TOP_DOCTOR_FILE = "eLIS_HALP_TOP_Doctor_Data_";
    private static final String DP_DOCTOR_FILE = "eLIS_HALP_DP_Doctor_Data_";

    private static final String DATE_STR_FORMAT = "yyyyMMdd";

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String DATE_STR = convertToString(new Date(), DATE_STR_FORMAT);
    private static final String NEW_PATH = DATE_STR + "ELIS/";

    @Autowired
    OrganizationClient organizationClient;

    @Autowired
    HcsaLicenceClient licenceClient;

    @Autowired
    AssistedReproductionClient assistedReproductionClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    public static String convertToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    @Override
    public void processLicence() {
        log.info("start processLicence");
        String path = sharedPath + "/" + LICENCE_FILE + DATE_STR + ".txt";
        File licenceFile = MiscUtil.generateFile(sharedPath, LICENCE_FILE + DATE_STR + ".txt");
        boolean flag = true;
        ELISInterfaceDto elisInterfaceDto = new ELISInterfaceDto();
        elisInterfaceDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if (licenceFile.exists()) {
            List<DsElisLicenceDto> dsElisLicenceDtoList = FileUtils.transformCsvToJavaBean(licenceFile, DsElisLicenceDto.class, false, '|');
            if (IaisCommonUtils.isNotEmpty(dsElisLicenceDtoList)) {
                log.info("dsElisLicenceDtoList size is {}", dsElisLicenceDtoList.size());
                for (DsElisLicenceDto dsElisLicenceDto : dsElisLicenceDtoList) {
                    OrganizationDto organizationDto = organizationClient.getByUenNoAndStatus(dsElisLicenceDto.getUen(), COMMON_STATUS_ACTIVE).getEntity();
                    if (Objects.isNull(organizationDto)) {
                        log.info("need create organization and licensee");
                        organizationDto = new OrganizationDto();
                        organizationDto.setUenNo(dsElisLicenceDto.getUen());
                        organizationDto.setOrgType("Comp");
                        organizationDto.setStatus(COMMON_STATUS_ACTIVE);
                        organizationDto.setDoMain(AppConsts.USER_DOMAIN_INTERNET);
                        organizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        //
                        LicenseeDto licenseeDto = generateLicenseeDto(dsElisLicenceDto);
                        organizationDto.setLicenseeDto(licenseeDto);
                        //save
                        try {
                            organizationDto = organizationClient.saveOrgAndLicensee(organizationDto).getEntity();
                            //sync
                            eicFeOrganization(organizationDto);
                        } catch (Exception e) {
                            flag = false;
                            log.warn(e.getMessage(), e);
                            log.warn("save organization and licensee failed,uen is {}", dsElisLicenceDto.getUen());
                        }
                    }
                    if (!Objects.isNull(organizationDto) && StringUtils.hasLength(organizationDto.getId())) {
                        LicenseeDto licenseeDto = organizationDto.getLicenseeDto();
                        if (Objects.isNull(licenseeDto)) {
                            log.info("need create licensee");
                            //create new licensee
                            licenseeDto = generateLicenseeDto(dsElisLicenceDto);
                            organizationDto.setLicenseeDto(licenseeDto);
                            //save
                            try {
                                organizationDto = organizationClient.saveOrgAndLicensee(organizationDto).getEntity();
                                //sync
                                eicFeOrganization(organizationDto);
                            } catch (Exception e) {
                                flag = false;
                                log.warn(e.getMessage(), e);
                                log.warn("save licensee failed,uen is {}", dsElisLicenceDto.getUen());
                            }
                        }
                        List<DsCenterDto> dsCenterDtos = new ArrayList<>();
                        List<String> centerTypes = licenceType2CenterTypes(dsElisLicenceDto.getLicenceType());
                        for (String centerType : centerTypes) {
                            DsCenterDto dsCenterDto = generateDsCenterDto(dsElisLicenceDto, centerType, organizationDto.getId(), licenseeDto.getId());
                            dsCenterDtos.add(dsCenterDto);
                        }
                        try {
                            dsCenterDtos = licenceClient.saveDsCenters(dsCenterDtos).getEntity();
                            elisInterfaceDto.setDsCenterDtos(dsCenterDtos);
                            eicFeELISInterfaceDto(elisInterfaceDto);
                        } catch (Exception e) {
                            flag = false;
                            log.warn(e.getMessage(), e);
                            log.warn("save ds_center failed");
                        }
                        //
                    }
                }
            }
            if (flag) {
                //move file
                try {
                    FileUtils.copyFileToOtherPosition(path,NEW_PATH);
                    FileUtils.deleteTempFile(licenceFile);
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                    log.error("move license file failed");
                }
            }
        }else {
            log.info("create licenceFile failed");
        }
    }

    private List<String> licenceType2CenterTypes(String licenceType) {
        if ("HP".equals(licenceType) || "MD".equals(licenceType) || "MC".equals(licenceType)) {
            return Arrays.asList(DataSubmissionConsts.DS_TOP, DataSubmissionConsts.DS_DRP);
        } else if ("NH".equals(licenceType)) {
            return Collections.singletonList(DataSubmissionConsts.DS_DRP);
        } else {
            return IaisCommonUtils.genNewArrayList();
        }
    }

    private LicenseeDto generateLicenseeDto(DsElisLicenceDto dsElisLicenceDto) {
        LicenseeDto licenseeDto = new LicenseeDto();
        licenseeDto.setName(dsElisLicenceDto.getLicenseeName());
        licenseeDto.setPostalCode(dsElisLicenceDto.getPostalCode());
        if ("A".equals(dsElisLicenceDto.getAddressType())) {
            licenseeDto.setAddrType(DP_PATIENTINFO_ADDRESS_TYPE_APT_BLK);
        } else if ("B".equals(dsElisLicenceDto.getAddressType())) {
            licenseeDto.setAddrType(DP_PATIENTINFO_ADDRESS_TYPE_WITHOUT_APT_BLK);
        }
        licenseeDto.setBlkNo(dsElisLicenceDto.getBlock());
        licenseeDto.setFloorNo(dsElisLicenceDto.getFloor());
        licenseeDto.setUnitNo(dsElisLicenceDto.getUnit());
        licenseeDto.setStreetName(dsElisLicenceDto.getStreetName());
        licenseeDto.setBuildingName(dsElisLicenceDto.getBuildingName());
        licenseeDto.setLicenseeType(LICENSEE_TYPE_CORPPASS);
        licenseeDto.setVersion(1);
        licenseeDto.setStatus(COMMON_STATUS_ACTIVE);
        //
        LicenseeEntityDto licenseeEntityDto = new LicenseeEntityDto();
        licenseeEntityDto.setEntityType("ds");
        licenseeEntityDto.setOfficeTelNo(null);
        licenseeEntityDto.setOfficeEmailAddr(null);
        licenseeDto.setLicenseeEntityDto(licenseeEntityDto);
        return licenseeDto;
    }

    private DsCenterDto generateDsCenterDto(DsElisLicenceDto dsElisLicenceDto, String centerType, String orgId, String licenseeId) {
        DsCenterDto dsCenterDto = licenceClient.getDsCenterDto(orgId, dsElisLicenceDto.getHciCode(), centerType).getEntity();
        if (Objects.isNull(dsCenterDto)) {
            log.info("create new ds_center");
            dsCenterDto = new DsCenterDto();
            dsCenterDto.setStatus(COMMON_STATUS_ACTIVE);
        } else {
            log.info("update ds center");
        }
        dsCenterDto.setHciCode(dsElisLicenceDto.getHciCode());
        dsCenterDto.setCenterName(dsElisLicenceDto.getHciName());
        dsCenterDto.setCenterType(centerType);
        dsCenterDto.setOrganizationId(orgId);
        dsCenterDto.setLicenseeId(licenseeId);
        dsCenterDto.setPostalCode(dsElisLicenceDto.getPrePostalCode());
        dsCenterDto.setBlkNo(dsElisLicenceDto.getPreBlock());
        dsCenterDto.setFloorNo(dsElisLicenceDto.getPreFloor());
        dsCenterDto.setUnitNo(dsElisLicenceDto.getPreUnit());
        dsCenterDto.setStreetName(dsElisLicenceDto.getPreStreetName());
        dsCenterDto.setBuildingName(dsElisLicenceDto.getPreBuildingName());
        //
        Date fromDate = processDate(dsElisLicenceDto.getLicStartDate(), DATE_FORMAT);
        dsCenterDto.setEffectiveFrom(fromDate);
        //compare LicEndDate with ceseDate
        if (!StringUtils.hasLength(dsElisLicenceDto.getLicCeseDate())) {
            Date endDate = processDate(dsElisLicenceDto.getLicEndDate(), DATE_FORMAT);
            dsCenterDto.setEffectiveTo(endDate);
        } else {
            Date endDate = processDate(dsElisLicenceDto.getLicEndDate(), DATE_FORMAT);
            Date ceseDate = processDate(dsElisLicenceDto.getLicCeseDate(), DATE_FORMAT);
            if (endDate.compareTo(ceseDate) < 0 || endDate.compareTo(ceseDate) == 0) {
                dsCenterDto.setEffectiveTo(endDate);
            } else if (endDate.compareTo(ceseDate) > 0) {
                dsCenterDto.setEffectiveTo(ceseDate);
            }
        }
        if (dsCenterDto.getEffectiveTo().compareTo(new Date()) < 0) {
            dsCenterDto.setStatus(COMMON_STATUS_IACTIVE);
        }
        //
        return dsCenterDto;
    }

    private Date processDate(String dateStr, String pattern) {
        Date fromDate = DateUtil.parse(dateStr, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    @Override
    public void processUsers() {
        log.info("start processUsers");
        String path = sharedPath + "/" + USER_FILE + DATE_STR + ".txt";
        File userFile = MiscUtil.generateFile(sharedPath, USER_FILE + DATE_STR + ".txt");
        boolean flag = true;
        if (userFile.exists()) {
            List<DsElisUserDto> dsElisUserDtoList = FileUtils.transformCsvToJavaBean(userFile, DsElisUserDto.class, false, '|');
            if (IaisCommonUtils.isNotEmpty(dsElisUserDtoList)) {
                log.info("dsElisUserDtoList size is {}", dsElisUserDtoList.size());
                for (DsElisUserDto dsElisUserDto : dsElisUserDtoList) {
                    List<OrgUserDto> orgUserDtoList = new ArrayList<>(1);
                    //if have organization,can save or update user info
                    String uen = dsElisUserDto.getUen();
                    String nric = dsElisUserDto.getNric();
                    OrganizationDto organizationDto = organizationClient.getByUenNoAndStatus(uen, COMMON_STATUS_ACTIVE).getEntity();
                    if (!Objects.isNull(organizationDto)) {
                        organizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        // ID type
                        String type = IaisEGPHelper.checkIdentityNoType(nric);
                        String userId = uen + "_" + nric;

                        List<OrgUserDto> oldUserDtoList = organizationClient.getUserDtoByUserId(userId).getEntity();
                        OrgUserDto oldUserDto;
                        if (!CollectionUtils.isEmpty(oldUserDtoList)) {
                            log.info("users get by userId size is {}", oldUserDtoList.size());
                            if (oldUserDtoList.size() == 1) {
                                oldUserDto = oldUserDtoList.get(0);
                            } else {
                                List<OrgUserDto> activeUserList = oldUserDtoList.stream().filter(dto -> COMMON_STATUS_ACTIVE.equals(dto.getStatus())).collect(Collectors.toList());
                                oldUserDto = activeUserList.get(0);
                                log.info("get active user");
                            }
                        } else {
                            oldUserDto = null;
                        }

                        OrgUserDto orgUserDto = generateOrgUserDto(dsElisUserDto, oldUserDto, type, userId, organizationDto.getId());
                        orgUserDtoList.add(orgUserDto);
                        if (!CollectionUtils.isEmpty(orgUserDtoList)) {
                            try {
                                List<FeUserDto> feUserDtos = organizationClient.createDSUserAccounts(orgUserDtoList).getEntity();
                                organizationDto.setFeUserDtoList(feUserDtos);
                                //sync
                                eicFeOrganization(organizationDto);
                                for (FeUserDto feUserDto : feUserDtos) {
                                    syncFeUserWithTrack(feUserDto);
                                }
                            } catch (Exception e) {
                                flag = false;
                                log.warn(e.getMessage(), e);
                                log.warn("save user failed");
                            }
                        } else {
                            log.info("empty orgUserDtoList to save");
                        }
                    } else {
                        flag = false;
                        log.warn("uen {} not find.", uen);
                    }
                }
            }
            if (flag){
                //move file
                try {
                    FileUtils.copyFileToOtherPosition(path,NEW_PATH);
                    FileUtils.deleteTempFile(userFile);
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                    log.error("move user file failed");
                }
            }
        }else {
            log.info("create userFile failed");
        }
    }

    private String[] getFeUserRoleArr(String role) {
        if (StringUtil.isEmpty(role)) {
            return new String[]{};
        } else {
            return role.split("\\^");
        }
    }

    private OrgUserDto generateOrgUserDto(DsElisUserDto dsElisUserDto, OrgUserDto orgUserDto, String type, String userId, String orgId) {
        if (Objects.isNull(orgUserDto)) {
            log.info("create new user");
            orgUserDto = new OrgUserDto();
        } else {
            log.info("update user");
        }
        orgUserDto.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
        orgUserDto.setUserId(userId);
        orgUserDto.setDisplayName(dsElisUserDto.getName());
        orgUserDto.setSalutation(dsElisUserDto.getSalutation());
        orgUserDto.setIdType(type);
        orgUserDto.setIdNumber(dsElisUserDto.getNric());
        orgUserDto.setDesignation(dsElisUserDto.getDesignation());
        orgUserDto.setMobileNo(dsElisUserDto.getMobile());
        orgUserDto.setOfficeTelNo(dsElisUserDto.getOffice());
        orgUserDto.setEmail(dsElisUserDto.getEmail());
        if (dsElisUserDto.getIsActive().equals("Y")) {
            orgUserDto.setStatus(COMMON_STATUS_ACTIVE);
        } else if (dsElisUserDto.getIsActive().equals("N")) {
            orgUserDto.setStatus(COMMON_STATUS_IACTIVE);
        }
        orgUserDto.setOrgId(orgId);
        orgUserDto.setAccountActivateDatetime(new Date());

        orgUserDto.setAvailable(Boolean.TRUE);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(getFeUserRoleArr(dsElisUserDto.getRole())));
        orgUserDto.setUserRoles(strings);
        return orgUserDto;
    }

    @Override
    public void processDoctor() {
        log.info("start processDoctor");
        String topPath = sharedPath + "/" + TOP_DOCTOR_FILE + DATE_STR + ".txt";
        String dpPath = sharedPath + "/" + DP_DOCTOR_FILE + DATE_STR + ".txt";
        File topDoctorFile = MiscUtil.generateFile(sharedPath, TOP_DOCTOR_FILE + DATE_STR + ".txt");
        File dpDoctorFile = MiscUtil.generateFile(sharedPath, DP_DOCTOR_FILE + DATE_STR + ".txt");
        List<DsElisDoctorDto> doctorDtoList = new ArrayList<>();
        ELISInterfaceDto elisInterfaceDto = new ELISInterfaceDto();
        elisInterfaceDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if (topDoctorFile.exists()) {
            List<DsElisDoctorDto> topDoctorDtoList = FileUtils.transformCsvToJavaBean(topDoctorFile, DsElisDoctorDto.class, false, '|');
            if (IaisCommonUtils.isNotEmpty(topDoctorDtoList)) {
                log.info("topDoctorDtoList size is {}", topDoctorDtoList.size());
                doctorDtoList.addAll(topDoctorDtoList);
            }
        }else {
            log.info("create topDoctorFile failed");
        }
        if (dpDoctorFile.exists()) {
            List<DsElisDoctorDto> dpDoctorDtoList = FileUtils.transformCsvToJavaBean(dpDoctorFile, DsElisDoctorDto.class, false, '|');
            if (IaisCommonUtils.isNotEmpty(dpDoctorDtoList)) {
                log.info("dpDoctorDtoList size is {}", dpDoctorDtoList.size());
                doctorDtoList.addAll(dpDoctorDtoList);
            }
        }else {
            log.info("create dpDoctorFile failed");
        }
        boolean flag = true;
        if (!CollectionUtils.isEmpty(doctorDtoList)) {
            log.info("doctorDtoList size is {}", doctorDtoList.size());
            Map<String, DoctorInformationDto> prnDoctorInfoMap = new HashMap<>();
            List<String> deletedDoctorPrns = new ArrayList<>();
            for (DsElisDoctorDto dsElisDoctorDto : doctorDtoList) {
                if ("N".equals(dsElisDoctorDto.getRegisterIndicator())) {
                    log.info("delete doctor, register_no is {}", dsElisDoctorDto.getPrn());
                    deletedDoctorPrns.add(dsElisDoctorDto.getPrn());
                } else {
                    DoctorInformationDto doctorInformationDto = assistedReproductionClient.getDoctorInformationDtoByConds(dsElisDoctorDto.getPrn(), "ELIS").getEntity();
                    DoctorInformationDto doctorDto = generateDoctorDto(dsElisDoctorDto, doctorInformationDto);
                    prnDoctorInfoMap.put(doctorDto.getDoctorReignNo(), doctorDto);
                }
            }
            if (!CollectionUtils.isEmpty(deletedDoctorPrns)) {
                //delete
                try {
                    int num = assistedReproductionClient.deleteDoctorByConds(deletedDoctorPrns, "ELIS").getEntity();
                    elisInterfaceDto.setDeletedDoctorPrns(deletedDoctorPrns);
                    log.info("delete {} doctors", num);
                } catch (Exception e) {
                    flag = false;
                    log.warn(e.getMessage(), e);
                    log.warn("delete doctor failed");
                }
            }
            if (!CollectionUtils.isEmpty(prnDoctorInfoMap)) {
                //save
                try {
                    List<DoctorInformationDto> saveList = new ArrayList<>(prnDoctorInfoMap.values());
                    List<DoctorInformationDto> doctorDtos = assistedReproductionClient.saveDoctorInformationDtos(saveList).getEntity();
                    elisInterfaceDto.setDoctorDtos(doctorDtos);
                    eicFeELISInterfaceDto(elisInterfaceDto);
                } catch (Exception e) {
                    flag = false;
                    log.warn(e.getMessage(), e);
                    log.warn("save doctor failed");
                }
            }
        } else {
            flag = false;
        }
        if (flag){
            //move file
            if (topDoctorFile.exists()) {
                try {
                    FileUtils.copyFileToOtherPosition(topPath, NEW_PATH);
                    FileUtils.deleteTempFile(topDoctorFile);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    log.error("move TOP file failed");
                }
            }
            if (dpDoctorFile.exists()) {
                try {
                    FileUtils.copyFileToOtherPosition(dpPath, NEW_PATH);
                    FileUtils.deleteTempFile(dpDoctorFile);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    log.error("move DP file failed");
                }
            }
        }
    }

    private DoctorInformationDto generateDoctorDto(DsElisDoctorDto dsElisDoctorDto, DoctorInformationDto doctorInformationDto) {
        if (doctorInformationDto == null) {
            log.info("create doctor");
            doctorInformationDto = new DoctorInformationDto();
            doctorInformationDto.setDoctorReignNo(dsElisDoctorDto.getPrn());
            doctorInformationDto.setDoctorSource("ELIS");
        } else {
            log.info("update doctor");
        }
        doctorInformationDto.setName(dsElisDoctorDto.getName());
        return doctorInformationDto;
    }

    public void eicFeOrganization(OrganizationDto organizationDto) {
        log.info(StringUtil.changeForLog("The eicFeOrganization start ..."));
        beEicGatewayClient.callEicWithTrack(organizationDto,
                this::callEicSaveOrganizationDto, this.getClass(), "saveOrganizationDto", EicClientConstant.ORGANIZATION_CLIENT);
        log.info(StringUtil.changeForLog("The eicFeOrganization end ..."));
    }

    private void callEicSaveOrganizationDto(OrganizationDto organizationDto){
        beEicGatewayClient.saveOrganizationDto(organizationDto).getEntity();
    }

    public void eicFeELISInterfaceDto(ELISInterfaceDto elisInterfaceDto) {
        log.info(StringUtil.changeForLog("The eicFeOrganization start ..."));
        beEicGatewayClient.callEicWithTrack(elisInterfaceDto,
                this::callEicSaveElisInterfaceDto, this.getClass(), "saveElisInterfaceDto",EicClientConstant.LICENCE_CLIENT);
        log.info(StringUtil.changeForLog("The eicFeOrganization end ..."));
    }

    private void callEicSaveElisInterfaceDto(ELISInterfaceDto elisInterfaceDto){
        beEicGatewayClient.saveElisInterfaceDto(elisInterfaceDto).getEntity();
    }

    public void syncFeUserWithTrack(FeUserDto userAttr) {
        beEicGatewayClient.callEicWithTrack(userAttr, this::syncFeUser, this.getClass(), "syncFeUser",EicClientConstant.LICENCE_CLIENT);
    }

    public void syncFeUser(FeUserDto userAttr) {
        beEicGatewayClient.syncFeUser(userAttr);
    }
}
