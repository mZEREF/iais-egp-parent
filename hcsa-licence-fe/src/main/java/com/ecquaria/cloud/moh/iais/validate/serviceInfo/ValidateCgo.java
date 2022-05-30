package com.ecquaria.cloud.moh.iais.validate.serviceInfo;

import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.validate.ValidateServiceInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/25 17:03
 */
@Component
public class ValidateCgo implements ValidateServiceInfo {
    @Override
    public void doValidteCGO(Map<String, String> map, AppSvcPrincipalOfficersDto appSvcCgoDto, Integer index, List<String> stringList, StringBuilder stringBuilder, boolean newErr0006) {
        String idTyp = appSvcCgoDto.getIdType();
        String idNo = appSvcCgoDto.getIdNo();
        if("-1".equals(idTyp)|| StringUtil.isEmpty(idTyp)){
            map.put("idTyp"+index, MessageUtil.replaceMessage("GENERAL_ERR0006","ID Type","field"));
        }
        String salutation = appSvcCgoDto.getSalutation();
        if(StringUtil.isEmpty(salutation)){
            map.put("salutation"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Salutation","field"));
        }
        String speciality = appSvcCgoDto.getSpeciality();
        if(StringUtil.isEmpty(speciality)||"-1".equals(speciality)){
            map.put("speciality"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Specialty","field"));
        }else {
            if("other".equals(speciality)){
                String specialityOther = appSvcCgoDto.getSpecialityOther();
                if(StringUtil.isEmpty(specialityOther)){
                    map.put("other"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Others","field"));
                }
            }
        }
        String professionType = appSvcCgoDto.getProfessionType();
        if(StringUtil.isEmpty(professionType)){
            map.put("professionType"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Type ","field"));
        }
        String designation =appSvcCgoDto.getDesignation();
        if(StringUtil.isEmpty(designation)){
            map.put("designation"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field"));
        }
        String professionRegoNo = appSvcCgoDto.getProfRegNo();
        String qualification = appSvcCgoDto.getSubSpeciality();
        if(StringUtil.isEmpty(professionRegoNo)){
            if(StringUtil.isEmpty(qualification)){
                map.put("qualification"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Sub-specialty or relevant qualification ","field"));
            }
        }else if(professionRegoNo.length() > 20){
            String general_err0041= NewApplicationHelper.repLength("Professional Regn. No.","20");
            map.put("professionRegoNo" + index, general_err0041);
        }

        if(!StringUtil.isEmpty(qualification) && qualification.length() > 100){
            String general_err0041=NewApplicationHelper.repLength("Sub-specialty or relevant qualification","100");
            map.put("qualification" + index, general_err0041);
        }
        //to do
        if(StringUtil.isEmpty(idNo)){
            map.put("idNo"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","ID No.","field"));
        }else {
            if(idNo.length() > 9){
                String general_err0041=NewApplicationHelper.repLength("ID No.","9");
                map.put("idNo" + index, general_err0041);
            }
            if(OrganizationConstants.ID_TYPE_FIN.equals(idTyp)){
                boolean b = SgNoValidator.validateFin(idNo);
                if(!b){
                    map.put("idNo"+index,"RFC_ERR0012");
                }
                stringBuilder.append(idTyp).append(idNo);
                if(newErr0006 && !StringUtil.isEmpty(stringBuilder.toString())){
                    if(stringList.contains(stringBuilder.toString())){
                        map.put("idNo"+index,"NEW_ERR0012");
                    }else {
                        stringList.add( stringBuilder.toString());
                    }
                }
            }
            if(OrganizationConstants.ID_TYPE_NRIC.equals(idTyp)){
                boolean b1 = SgNoValidator.validateNric(idNo);
                if(!b1){
                    map.put("idNo"+index,"RFC_ERR0012");
                }
                stringBuilder.append(idTyp).append(idNo);

                if(newErr0006 && !StringUtil.isEmpty(stringBuilder.toString())){
                    if(stringList.contains(stringBuilder.toString())){
                        map.put("idNo"+index,"NEW_ERR0012");
                    }else {
                        stringList.add( stringBuilder.toString());
                    }
                }
            }


        }
        String name = appSvcCgoDto.getName();
        if(StringUtil.isEmpty(name)){
            map.put("name"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field"));
        }else {
            if(name.length()>110){
                String general_err0041 = NewApplicationHelper.repLength("Name","110");
                map.put("name" + index, general_err0041);
            }
        }

        String mobileNo = appSvcCgoDto.getMobileNo();
        if(StringUtil.isEmpty(mobileNo)){
            map.put("mobileNo"+index, MessageUtil.replaceMessage("GENERAL_ERR0006","Mobile No. ","field"));
        }else if (!StringUtil.isEmpty(mobileNo)) {
            if(mobileNo.length() > 8){
                String general_err0041=NewApplicationHelper.repLength("Mobile No.","8");
                map.put("mobileNo" + index, general_err0041);
            }
            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                map.put("mobileNo"+index, "GENERAL_ERR0007");
            }
        }
        String emailAddr =appSvcCgoDto.getEmailAddr();

        if(StringUtil.isEmpty(emailAddr)){
            map.put("emailAddr"+index,MessageUtil.replaceMessage("GENERAL_ERR0006","Email Address","field"));
        }else if (!StringUtil.isEmpty(emailAddr)) {
            if(emailAddr.length() > 320){
                String general_err0041=NewApplicationHelper.repLength("Email Address","320");
                map.put("emailAddr" + index, general_err0041);
            }
            if (! ValidationUtils.isEmail(emailAddr)) {
                map.put("emailAddr"+index, "GENERAL_ERR0014");
            }
        }
    }
}
