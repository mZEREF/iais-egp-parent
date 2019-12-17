package com.ecquaria.cloud.moh.iais.validation;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/14 15:35
 */
public class ClinicalOfficerValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String ,String> map=new HashMap<>();
        List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getRequestAttr(request, "goveOffice");
        appSvcCgoList(map,appSvcCgoList);
        List<HcsaSvcSubtypeOrSubsumedDto> HcsaSvcSubtypeOrSubsumedDto = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getRequestAttr(request, "hcsaSvcSubtypeOrSubsumedDtos");

        return map;
    }


    private void appSvcCgoList(Map<String,String> map,List<AppSvcCgoDto> appSvcCgoList){
        if(appSvcCgoList!=null){
            for(int i=0;i<appSvcCgoList.size();i++){

                String name = appSvcCgoList.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"cannot be blank");
                }
                String salutation = appSvcCgoList.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    map.put("salutation"+i,"select one ");
                }
                String idType = appSvcCgoList.get(i).getIdType();
                if(StringUtil.isEmpty(idType)){
                    map.put("idTyp"+i,"cannot be blank ");
                }
                String idNo = appSvcCgoList.get(i).getIdNo();
                boolean b = SgNoValidator.validateFin(idNo);
                boolean b1 = SgNoValidator.validateNric(idNo);
                if(!(b||b1)){
                    map.put("idNo"+i,"CHKLMD001_ERR005");
                }else {
                    map.put("idNo"+i,"cannot be blank ");
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if(StringUtil.isEmpty(designation)){
                    map.put("designation"+i,"select one ");
                }
                String professionRegoNo = appSvcCgoList.get(i).getProfessionRegoNo();
                if(StringUtil.isEmpty(professionRegoNo)){
                    map.put("professionRegoNo"+i,"cannot be blank ");
                }
                String professionRegoType = appSvcCgoList.get(i).getProfessionRegoType();
                if(StringUtil.isEmpty(professionRegoType)){
                    map.put("professionRegoType"+i,"select one ");
                }
                String speciality = appSvcCgoList.get(i).getSpeciality();
                if(StringUtil.isEmpty(speciality)){
                    map.put("specialtyOther"+i,"select one ");
                }
                String mobileNo = appSvcCgoList.get(i).getMobileNo();
                if(!StringUtil.isEmpty(mobileNo)){
                    if(!mobileNo.matches("^[8|9][0-9]{7}$")){
                        map.put("mobileNo"+i,"CHKLMD001_ERR004");
                    }
                } else {
                    map.put("mobileNo"+i,"cannot be blank ");
                }
                String emailAddr = appSvcCgoList.get(i).getEmailAddr();
                if(!StringUtil.isEmpty(emailAddr)){
                    if(!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                        map.put("emailAddr"+i,"CHKLMD001_ERR006");
                    }
                }
                else {
                    map.put("emailAddr"+i,"cannot be blank");
                }
            }

        }
    }

    private void HcsaSvcSubtypeOrSubsumedDto(Map<String,String> map,List<HcsaSvcSubtypeOrSubsumedDto> HcsaSvcSubtypeOrSubsumedDto){

    }
}
