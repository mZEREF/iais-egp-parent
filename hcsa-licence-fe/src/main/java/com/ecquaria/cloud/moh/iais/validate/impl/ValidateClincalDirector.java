package com.ecquaria.cloud.moh.iais.validate.impl;

import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcClinicalDirectorDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/25 14:39
 */
@Component
public class ValidateClincalDirector implements ValidateFlow {
    @Override
    public void doValidateClincalDirector(Map<String, String> map, List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos) {
        if(appSvcClinicalDirectorDtos==null){
            return;
        }
        List<String> stringList=new ArrayList<>(17);
        for(int i=0;i<appSvcClinicalDirectorDtos.size();i++){
            StringBuilder stringBuilder=new StringBuilder();
            String professionBoard = appSvcClinicalDirectorDtos.get(i).getProfessionBoard();
            if(StringUtil.isEmpty(professionBoard)||"-1".equals(professionBoard)){
                map.put("professionBoard"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "professionBoard", "field"));
            }else {

            }
            String profRegNo = appSvcClinicalDirectorDtos.get(i).getProfRegNo();
            if(StringUtil.isEmpty(profRegNo)){
                map.put("profRegNo"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "profRegNo", "field"));
            }else {

            }
            String salutation = appSvcClinicalDirectorDtos.get(i).getSalutation();
            if(StringUtil.isEmpty(salutation)||"-1".equals(salutation)){
                map.put("salutation"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "salutation", "field"));
            }else {

            }
            String name = appSvcClinicalDirectorDtos.get(i).getName();
            if(StringUtil.isEmpty(name)){
                map.put("name"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "name", "field"));
            }else {
                if(name.length()>66){
                    String general_err0041=NewApplicationHelper.repLength("name","66");
                    map.put("name"+i, general_err0041);
                }
            }
            String idType = appSvcClinicalDirectorDtos.get(i).getIdType();
            if(StringUtil.isEmpty(idType)||"-1".equals(idType)){
                map.put("idType"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "idType", "field"));
            }else {

            }
            String idNo = appSvcClinicalDirectorDtos.get(i).getIdNo();
            if(StringUtil.isEmpty(idNo)){
                map.put("idNo"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "idNo", "field"));
            }else {
                if(OrganizationConstants.ID_TYPE_FIN.equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        map.put("idNo"+i,"RFC_ERR0012");
                    }else {
                        stringBuilder.append(idType).append(idNo);
                        if(stringList.contains(stringBuilder.toString())){
                            map.put("idNo"+i,"NEW_ERR0012");
                        }
                    }
                }
                if(OrganizationConstants.ID_TYPE_NRIC.equals(idType)){
                    boolean b = SgNoValidator.validateNric(idNo);
                    if(!b){
                        map.put("idNo"+i,"RFC_ERR0012");
                    }else {
                        stringBuilder.append(idType).append(idNo);
                        if(stringList.contains(stringBuilder.toString())){
                            map.put("idNo"+i,"NEW_ERR0012");
                        }
                    }
                }
            }
            String designation = appSvcClinicalDirectorDtos.get(i).getDesignation();
            if(StringUtil.isEmpty(designation)){
                map.put("designation"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "designation", "field"));
            }else {

            }
            String specialty = appSvcClinicalDirectorDtos.get(i).getSpecialty();
            if(StringUtil.isEmpty(specialty)||"-1".equals(specialty)){
                map.put("specialty"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "specialty", "field"));
            }else {

            }
            Date specialtyGetDate = appSvcClinicalDirectorDtos.get(i).getSpecialtyGetDate();
            if(specialtyGetDate==null){
                map.put("specialtyGetDate"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "specialtyGetDate", "field"));
            }else {

            }
            String typeOfCurrRegi = appSvcClinicalDirectorDtos.get(i).getTypeOfCurrRegi();
            if(StringUtil.isEmpty(typeOfCurrRegi)){
                map.put("typeOfCurrRegi"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "typeOfCurrRegi", "field"));
            }else {
                if(typeOfCurrRegi.length()>50){
                    String general_err0041=NewApplicationHelper.repLength("typeOfCurrRegi","50");
                    map.put("typeOfCurrRegi"+i,general_err0041);
                }
            }
            Date currRegiDate = appSvcClinicalDirectorDtos.get(i).getCurrRegiDate();
            if(currRegiDate==null){
                map.put("currRegiDate"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "currRegiDate", "field"));
            }else {

            }
            Date praCerEndDate = appSvcClinicalDirectorDtos.get(i).getPraCerEndDate();
            if(praCerEndDate==null){
                map.put("praCerEndDate"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "praCerEndDate", "field"));
            }
            String typeOfRegister = appSvcClinicalDirectorDtos.get(i).getTypeOfRegister();
            if(StringUtil.isEmpty(typeOfRegister)){
                map.put("typeOfRegister"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "typeOfRegister", "field"));
            }else {

            }
            String relevantExperience = appSvcClinicalDirectorDtos.get(i).getRelevantExperience();
            if(StringUtil.isEmpty(relevantExperience)){
                map.put("relevantExperience"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "relevantExperience", "field"));
            }else {
                if(relevantExperience.length()>180){
                    String general_err0041=NewApplicationHelper.repLength("relevantExperience","50");
                    map.put("relevantExperience"+i,general_err0041);
                }
            }
            String holdCerByEMS = appSvcClinicalDirectorDtos.get(i).getHoldCerByEMS();
            if(StringUtil.isEmpty(holdCerByEMS)){
                map.put("holdCerByEMS"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "holdCerByEMS", "field"));
            }else {

            }
            Date aclsExpiryDate = appSvcClinicalDirectorDtos.get(i).getAclsExpiryDate();
            if(aclsExpiryDate==null){
                map.put("aclsExpiryDate"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "aclsExpiryDate", "field"));
            }else {

            }
            String mobileNo = appSvcClinicalDirectorDtos.get(i).getMobileNo();
            if(StringUtil.isEmpty(mobileNo)){
                map.put("mobileNo"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "mobileNo", "field"));
            }else {
                if(!mobileNo.matches("^[8|9][0-9]{7}$")){
                    map.put("mobileNo"+i, "GENERAL_ERR0007");
                }
            }
            String emailAddr = appSvcClinicalDirectorDtos.get(i).getEmailAddr();
            if(StringUtil.isEmpty(emailAddr)){
                map.put("emailAddr"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "emailAddr", "field"));
            }else {
                if(!ValidationUtils.isEmail(emailAddr)){
                    map.put("emailAddr"+i, "GENERAL_ERR0014");
                }else if(emailAddr.length()>66){
                    String general_err0041= NewApplicationHelper.repLength("Email Address","66");
                    map.put("emailAddr" + i, general_err0041);
                }
            }
            if(stringList.contains(stringBuilder.toString())&&!StringUtil.isEmpty(stringBuilder.toString())) {


            }else {
                stringList.add(stringBuilder.toString());
            }
        }
    }
}
