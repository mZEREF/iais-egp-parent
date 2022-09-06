package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sop.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/25 14:39
 */
@Component
@Slf4j
public class ValidateClincalDirector {

    public void doValidateClincalDirector(Map<String, String> map, List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String serviceCode) {
        if (appSvcClinicalDirectorDtos == null) {
            return;
        }
        List<String> stringList = new ArrayList<>();
        List<String> assignList = new ArrayList<>();
        for (int i = 0; i < appSvcClinicalDirectorDtos.size(); i++) {
            AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto = appSvcClinicalDirectorDtos.get(i);
            String assignSelect = appSvcClinicalDirectorDto.getAssignSelect();
            if ("-1".equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
                map.put("assignSelect" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Assign a " + HcsaConsts.CLINICAL_DIRECTOR + " Person",
                                "field"));
            } else {
                String nationality = appSvcClinicalDirectorDto.getNationality();
                String idType = appSvcClinicalDirectorDto.getIdType();
                String idNo = appSvcClinicalDirectorDto.getIdNo();
                // check person key
                String keyIdType = "idType" + i;
                String keyIdNo = "idNo" + i;
                String keyNationality = "nationality" + i;
                boolean isValid = AppValidatorHelper.validateId(nationality, idType, idNo, keyNationality, keyIdType, keyIdNo, map);
                // check duplicated
                if (isValid) {
                    String personKey = ApplicationHelper.getPersonKey(nationality, idType, idNo);
                    boolean licPerson = appSvcClinicalDirectorDto.isLicPerson();
                    String idTypeNoKey = "idTypeNo" + i;
                    isValid = AppValidatorHelper.doPsnCommValidate(map, personKey, idNo, licPerson, licPersonMap, idTypeNoKey);
                    if (isValid) {
                        if (stringList.contains(personKey)) {
                            map.put(keyIdNo, "NEW_ERR0012");
                            isValid = false;
                        } else {
                            stringList.add(personKey);
                        }
                    }
                }
                if (isValid) {
                    if (assignList.contains(assignSelect)) {
                        map.put("assignSelect" + i, "NEW_ERR0012");
                    } else if (!HcsaAppConst.NEW_PSN.equals(assignSelect)) {
                        assignList.add(assignSelect);
                    }
                }

                String salutation = appSvcClinicalDirectorDto.getSalutation();
                if (StringUtil.isEmpty(salutation) || "-1".equals(salutation)) {
                    map.put("salutation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
                } else {

                }
                String name = appSvcClinicalDirectorDto.getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                } else {
                    if (name.length() > 110) {
                        String general_err0041 = AppValidatorHelper.repLength("Name", "110");
                        map.put("name" + i, general_err0041);
                    }
                }
                String designation = appSvcClinicalDirectorDto.getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    map.put("designation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field"));
                } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                    String otherDesignation = appSvcClinicalDirectorDto.getOtherDesignation();
                    if (StringUtil.isEmpty(otherDesignation)) {
                        map.put("otherDesignation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field"));
                    } else if (otherDesignation.length() > 100) {
                        String general_err0041 = AppValidatorHelper.repLength("Others Designation", "100");
                        map.put("otherDesignation" + i, general_err0041);
                    }
                }
                String typeOfCurrRegi = appSvcClinicalDirectorDto.getTypeOfCurrRegi();
                if (StringUtil.isEmpty(typeOfCurrRegi)) {
                    map.put("typeOfCurrRegi" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Registration Date", "field"));
                } else {
                    if (typeOfCurrRegi.length() > 50) {
                        String general_err0041 = AppValidatorHelper.repLength("Type of Registration Date", "50");
                        map.put("typeOfCurrRegi" + i, general_err0041);
                    }
                }
                String currRegiDate = appSvcClinicalDirectorDto.getCurrRegiDateStr();
                if (StringUtil.isEmpty(currRegiDate)) {
                    map.put("currRegiDate" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Current Registration Date", "field"));
                }
                String praCerEndDate = appSvcClinicalDirectorDto.getPraCerEndDateStr();
                if (StringUtil.isEmpty(praCerEndDate)) {
                    map.put("praCerEndDate" + i,
                            MessageUtil.replaceMessage("GENERAL_ERR0006", "Practicing Certificate End Date", "field"));
                }
                String typeOfRegister = appSvcClinicalDirectorDto.getTypeOfRegister();
                if (StringUtil.isEmpty(typeOfRegister)) {
                    map.put("typeOfRegister" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Register", "field"));
                } else if (typeOfRegister.length() > 50) {
                    map.put("typeOfRegister" + i, AppValidatorHelper.repLength("Type of Register", "50"));
                }
                // Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop

                String mobileNo = appSvcClinicalDirectorDto.getMobileNo();
                if (StringUtil.isEmpty(mobileNo)) {
                    map.put("mobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No.", "field"));
                } else {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        map.put("mobileNo" + i, "GENERAL_ERR0007");
                    }
                }
                String emailAddr = appSvcClinicalDirectorDto.getEmailAddr();
                if (StringUtil.isEmpty(emailAddr)) {
                    map.put("emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field"));
                } else {
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        map.put("emailAddr" + i, "GENERAL_ERR0014");
                    } else if (emailAddr.length() > 320) {
                        String general_err0041 = AppValidatorHelper.repLength("Email Address", "320");
                        map.put("emailAddr" + i, general_err0041);
                    }
                }
                switchService(serviceCode, appSvcClinicalDirectorDto, map, i);
            }
        }
        log.info(StringUtil.changeForLog("=====>ValidateClincalDirector-->" + JsonUtil.parseToJson(map)));
    }

    //Medical Transport Service
    protected void doValidateForMTS(AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto, Map<String, String> map, int index) {
        String noRegWithProfBoard = appSvcClinicalDirectorDto.getNoRegWithProfBoard();
        if (!AppConsts.YES.equals(noRegWithProfBoard)) {
            String professionBoard = appSvcClinicalDirectorDto.getProfessionBoard();
            if (StringUtil.isEmpty(professionBoard) || "-1".equals(professionBoard)) {
                map.put("professionBoard" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "professionBoard", "field"));
            }
            String profRegNo = appSvcClinicalDirectorDto.getProfRegNo();
            if (StringUtil.isEmpty(profRegNo)) {
                map.put("profRegNo" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "profRegNo", "field"));
            }
        }
        String relevantExperience = appSvcClinicalDirectorDto.getRelevantExperience();
        if (StringUtil.isEmpty(relevantExperience)) {
            map.put("relevantExperience" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Relevant Experience", "field"));
        } else if (relevantExperience.length() > 180) {
            map.put("relevantExperience" + index, AppValidatorHelper.repLength("Relevant Experience", "180"));
        }
//        Date now = new Date();
        LocalDate now = LocalDate.now();
        String err032 = MessageUtil.getMessageDesc("NEW_ERR0032");
        Date aclsExpiryDate = appSvcClinicalDirectorDto.getAclsExpiryDate();
        LocalDate aclsDate = transferLocalDate(aclsExpiryDate);
        if (!StringUtil.isEmpty(aclsExpiryDate) && aclsDate != null && aclsDate.isBefore(now)) {
            map.put("expiryDateAcls" + index, err032);
        }
        Date bclsExpiryDate = appSvcClinicalDirectorDto.getBclsExpiryDate();
        LocalDate bclsaDate = transferLocalDate(bclsExpiryDate);
        if (StringUtil.isEmpty(bclsExpiryDate)) {
            map.put("expiryDateBcls" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Expiry Date (BCLS and AED)", "field"));
        } else if (bclsaDate != null && bclsaDate.isBefore(now)) {
            map.put("expiryDateBcls" + index, err032);
        }
    }

    //Emergency Ambulance Service
    protected void doValidateForEAS(AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto, Map<String, String> map, int index) {
        String professionBoard = appSvcClinicalDirectorDto.getProfessionBoard();
        if (StringUtil.isEmpty(professionBoard) || "-1".equals(professionBoard)) {
            map.put("professionBoard" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "professionBoard", "field"));
        }
        String profRegNo = appSvcClinicalDirectorDto.getProfRegNo();
        if (StringUtil.isEmpty(profRegNo)) {
            map.put("profRegNo" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "profRegNo", "field"));
        }
        String relevantExperience = appSvcClinicalDirectorDto.getRelevantExperience();
        String speciality = appSvcClinicalDirectorDto.getSpeciality();
        if (!StringUtil.isEmpty(profRegNo) && StringUtil.isEmpty(speciality)) {
            if (StringUtil.isEmpty(relevantExperience)) {
                map.put("relevantExperience" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Relevant Experience", "field"));
            }
        }
        if (relevantExperience != null && relevantExperience.length() > 180) {
            map.put("relevantExperience" + index, AppValidatorHelper.repLength("Relevant Experience", "180"));
        }
        Date aclsExpiryDate = appSvcClinicalDirectorDto.getAclsExpiryDate();
        LocalDate now = LocalDate.now();
        LocalDate aclsDate = transferLocalDate(aclsExpiryDate);
        if (aclsExpiryDate == null) {
            map.put("expiryDateAcls" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "aclsExpiryDate", "field"));
        } else if (aclsDate != null && aclsDate.isBefore(now)) {
            map.put("expiryDateAcls" + index, MessageUtil.getMessageDesc("NEW_ERR0032"));
        }
    }

    protected void switchService(String code, AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto, Map<String, String> map,
            int index) {
        if (code == null) {
            return;
        }
        if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(code)) {
            doValidateForMTS(appSvcClinicalDirectorDto, map, index);
        } else if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(code)) {
            doValidateForEAS(appSvcClinicalDirectorDto, map, index);
        }
    }

    private LocalDate transferLocalDate(Date date) {
        LocalDate localDate = null;
        if (date != null) {
            localDate = LocalDate.parse(DateUtil.formatDate(date, Formatter.DATE), DateTimeFormatter.ofPattern(Formatter.DATE));
        }
        return localDate;
    }

}
