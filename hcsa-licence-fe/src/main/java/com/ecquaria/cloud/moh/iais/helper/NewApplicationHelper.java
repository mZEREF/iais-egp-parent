package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator;
import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NewApplicationHelper
 *
 * @author suocheng
 * @date 2/24/2020
 */

@Slf4j
public class NewApplicationHelper {
    public static Map<String,String> doValidateLaboratory(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppSvcLaboratoryDisciplinesDto>  appSvcLaboratoryDisciplinesDtos, String serviceId){
        Map<String,String> map=IaisCommonUtils.genNewHashMap();
        int premCount = 0 ;
        if(appSvcLaboratoryDisciplinesDtos.isEmpty()){
            return map;
        }
        int svcScopeSize =  appSvcLaboratoryDisciplinesDtos.size();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            if(premCount >=svcScopeSize){
                break;
            }
                AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto = appSvcLaboratoryDisciplinesDtos.get(premCount);
                List<AppSvcChckListDto> listDtos = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                int count=0;
                if(listDtos.isEmpty()){
                    /*   map.put("checkError","UC_CHKLMD001_ERR002");*/
                }else {
                    for(int i=0;i<listDtos.size();i++){
                        if(ClinicalLaboratoryDelegator.PLEASEINDICATE.equals(listDtos.get(i).getChkName())&&StringUtil.isEmpty(listDtos.get(i).getOtherScopeName()) ){
                            map.put("pleaseIndicateError"+premCount,"ERR0009");
                        }

                        String parentName = listDtos.get(i).getParentName();
                        if(parentName==null){
                            count++;
                            continue;
                        }else  if(listDtos.get(i).isChkLstType()){
                            if(serviceId.equals(parentName)){
                                count++;
                                continue;
                            }
                            for(AppSvcChckListDto every :listDtos) {
                                if(every.getChildrenName()!=null){
                                    if(every.getChildrenName().equals(parentName)){
                                        count++;
                                        break;
                                    }
                                }
                            }
                        }
                        else if(!listDtos.get(i).isChkLstType()){
                            for(AppSvcChckListDto every :listDtos) {
                                if (every.getChkLstConfId().equals(parentName)) {
                                    count++;
                                    break;
                                }
                            }
                        }
                    }
                }
                if(count!=listDtos.size()){
                    map.put("checkError","UC_CHKLMD001_ERR002");
                }
            premCount++;
        }
        return map;
    }

    public static Map<String,String> doValidateGovernanceOfficers(List<AppSvcCgoDto> appSvcCgoList, Map<String,AppSvcPrincipalOfficersDto> licPersonMap){

        if(appSvcCgoList == null){
            return IaisCommonUtils.genNewHashMap();
        }

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        for(int i=0;i<appSvcCgoList.size();i++ ){
            StringBuilder stringBuilder1=new StringBuilder();
            String assignSelect = appSvcCgoList.get(i).getAssignSelect();
            if("-1".equals(assignSelect)){
                errMap.put("assignSelect"+i, "UC_CHKLMD001_ERR001");
            }else {
                String idTyp = appSvcCgoList.get(i).getIdType();
                String idNo = appSvcCgoList.get(i).getIdNo();
                boolean licPerson = appSvcCgoList.get(i).isLicPerson();
                String idTypeNoKey = "idTypeNo"+i;
                errMap = doPsnCommValidate(errMap,idTyp,idNo,licPerson,licPersonMap,idTypeNoKey);
                String idTypeNoErr = errMap.get(idTypeNoKey);
                if(!StringUtil.isEmpty(idTypeNoErr)){
                    continue;
                }

                if("-1".equals(idTyp)||StringUtil.isEmpty(idTyp)){
                    errMap.put("idTyp"+i, "UC_CHKLMD001_ERR001");
                }
                String salutation = appSvcCgoList.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    errMap.put("salutation"+i,"UC_CHKLMD001_ERR001");
                }
                String speciality = appSvcCgoList.get(i).getSpeciality();
                if("-1".equals(speciality)){
                    errMap.put("speciality"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if("other".equals(speciality)){
                        String specialityOther = appSvcCgoList.get(i).getSpecialityOther();
                        if(StringUtil.isEmpty(specialityOther)){
                            errMap.put("other"+i,"UC_CHKLMD001_ERR001");
                        }
                    }
                }
                String professionType = appSvcCgoList.get(i).getProfessionType();
                if(StringUtil.isEmpty(professionType)){
                    errMap.put("professionType"+i,"UC_CHKLMD001_ERR001");
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if(StringUtil.isEmpty(designation)){
                    errMap.put("designation"+i,"UC_CHKLMD001_ERR001");
                }
                String professionRegoNo = appSvcCgoList.get(i).getProfRegNo();
                if(StringUtil.isEmpty(professionRegoNo)){
                    errMap.put("professionRegoNo"+i,"UC_CHKLMD001_ERR001");
                    String qualification = appSvcCgoList.get(i).getSubSpeciality();
                    if(StringUtil.isEmpty(qualification)){
                        errMap.put("qualification"+i,"UC_CHKLMD001_ERR001");
                    }
                }
                //to do
                if(StringUtil.isEmpty(idNo)){
                    errMap.put("idNo"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if("FIN".equals(idTyp)){
                        boolean b = SgNoValidator.validateFin(idNo);
                        if(!b){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                        stringBuilder1.append(idTyp).append(idNo);
                        if(!StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"UC_CHKLMD001_ERR002");
                            }else {
                                stringList.add( stringBuilder1.toString());
                            }
                        }
                    }
                    if("NRIC".equals(idTyp)){
                        boolean b1 = SgNoValidator.validateNric(idNo);
                        if(!b1){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                        stringBuilder1.append(idTyp).append(idNo);

                        if(!StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"UC_CHKLMD001_ERR002");
                            }else {
                                stringList.add( stringBuilder1.toString());
                            }
                        }
                    }


                }
                //to do


                String name = appSvcCgoList.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if(name.length()>66){
                        errMap.put("name"+i,"Length is too long");
                    }
                }

                String mobileNo = appSvcCgoList.get(i).getMobileNo();
                if(StringUtil.isEmpty(mobileNo)){
                    errMap.put("mobileNo"+i, "UC_CHKLMD001_ERR001");
                }else if (!StringUtil.isEmpty(mobileNo)) {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo"+i, "CHKLMD001_ERR004");
                    }
                }
                String emailAddr = appSvcCgoList.get(i).getEmailAddr();

                if(StringUtil.isEmpty(emailAddr)){
                    errMap.put("emailAddr"+i, "UC_CHKLMD001_ERR001");
                }else if (!StringUtil.isEmpty(emailAddr)) {
                    if (! ValidationUtils.isEmail(emailAddr)) {
                        errMap.put("emailAddr"+i, "CHKLMD001_ERR006");
                    }else if(emailAddr.length()>66) {
                        errMap.put("emailAddr"+i, "Length is too long");
                    }
                }


                if(!StringUtil.isEmpty(stringBuilder1.toString())){
                    if(!stringList.contains(stringBuilder1.toString())){
                        stringList.add(stringBuilder1.toString());
                    }
                }
            }

        }
        return errMap;
    }

    public static  List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", NewApplicationDelegator.FIRESTOPTION);
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        return idTypeSelectList;
    }

    public static AppSubmissionDto setSubmissionDtoSvcData(HttpServletRequest request, AppSubmissionDto appSubmissionDto) throws CloneNotSupportedException {
        List<HcsaServiceDto> hcsaServiceDtoList = HcsaServiceCacheHelper.receiveAllHcsaService();
        if(appSubmissionDto != null && hcsaServiceDtoList!=null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                    //set hcsaService info
                    for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtoList){
                        String svcId = appSvcRelatedInfoDto.getServiceId();
                        String name = appSvcRelatedInfoDto.getServiceName();
                        if(!StringUtil.isEmpty(svcId)){
                            if(hcsaServiceDto.getId().equals(svcId)){
                                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                            }

                        }else if (!StringUtil.isEmpty(name)){
                            if(hcsaServiceDto.getSvcName().equals(name)){
                                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                            }

                        }
                    }
                    //set svc cgo dropdown info
                    List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                        List<SelectOption> specialtyList = genSpecialtySelectList(appSvcRelatedInfoDto.getServiceCode(),true);
                        List<String> specialtyKeyList = IaisCommonUtils.genNewArrayList();
                        for(SelectOption sp:specialtyList){
                            specialtyKeyList.add(sp.getValue());
                        }
                        List<SelectOption> allSpecialtyList = getAllSpecialtySelList();
                        for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
                            if(specialtyKeyList.contains(appSvcCgoDto.getSpeciality())){
                                continue;
                            }
                            appSvcCgoDto.setNeedSpcOptList(true);
                            appSvcCgoDto.setSpcOptList(allSpecialtyList);
                            Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
                            specialtyAttr.put("name", "specialty");
                            specialtyAttr.put("class", "specialty");
                            specialtyAttr.put("style", "display: none;");
                            String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, allSpecialtyList, null, appSvcCgoDto.getSpeciality());
                            appSvcCgoDto.setSpecialityHtml(specialtySelectStr);
                        }
                    }

                }
            }
        }
        //todo:change place
        Object rfi = ParamUtil.getSessionAttr(request,NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        if(appSubmissionDto != null){
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                    ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                    || rfi != null){
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
            }
        }
        return appSubmissionDto;
    }
    //todo change
    public static Map<String,  String> doValidatePo(List<AppSvcPrincipalOfficersDto> poDto,Map<String,AppSvcPrincipalOfficersDto> licPersonMap) {
        Map<String, String> oneErrorMap = IaisCommonUtils.genNewHashMap();
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        int poIndex=0;
        int dpoIndex=0;
        if(IaisCommonUtils.isEmpty(poDto)){
            return oneErrorMap;
        }
        for (int i=0;i< poDto.size();i++) {
            String psnType = poDto.get(i).getPsnType();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){

                StringBuilder stringBuilder =new StringBuilder();

                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect)) {
                    oneErrorMap.put("assignSelect"+i, "UC_CHKLMD001_ERR001");
                } else {
                    String mobileNo = poDto.get(i).getMobileNo();
                    String officeTelNo = poDto.get(i).getOfficeTelNo();
                    String emailAddr = poDto.get(i).getEmailAddr();
                    String idNo = poDto.get(i).getIdNo();
                    String name = poDto.get(i).getName();
                    String salutation = poDto.get(i).getSalutation();
                    String designation = poDto.get(i).getDesignation();
                    String idType = poDto.get(i).getIdType();
                    boolean licPerson = poDto.get(i).isLicPerson();
                    String poIdTypeNoKey = "poIdTypeNo" + i;
                    oneErrorMap = doPsnCommValidate(oneErrorMap,idType,idNo,licPerson,licPersonMap,poIdTypeNoKey);
                    String idTypeNoErr = oneErrorMap.get(poIdTypeNoKey);
                    if(!StringUtil.isEmpty(idTypeNoErr)){
                        continue;
                    }
                    if("-1".equals(idType)||StringUtil.isEmpty(idType)){
                        oneErrorMap.put("idType"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(name)){
                        oneErrorMap.put("name"+poIndex,"UC_CHKLMD001_ERR001");
                    }else if (name.length()>66){

                    }
                    if(StringUtil.isEmpty(salutation)){
                        oneErrorMap.put("salutation"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(designation)){
                        oneErrorMap.put("designation"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(idNo)){
                        if("FIN".equals(idType)){
                            boolean b = SgNoValidator.validateFin(idNo);
                            if(!b){
                                oneErrorMap.put("poNRICFIN"+poIndex,"CHKLMD001_ERR005");
                            }else {
                                stringBuilder.append(idType).append(idNo);
                                String s = stringBuilder.toString();
                                if(stringList.contains(s)){
                                    oneErrorMap.put("poNRICFIN"+poIndex,"UC_CHKLMD001_ERR002");
                                }
                            }
                        }
                        if("NRIC".equals(idType)){
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if(!b1){
                                oneErrorMap.put("poNRICFIN"+poIndex,"CHKLMD001_ERR005");
                            }else {
                                stringBuilder.append(idType).append(idNo);
                                String s = stringBuilder.toString();
                                if(stringList.contains(s)){
                                    oneErrorMap.put("poNRICFIN"+poIndex,"UC_CHKLMD001_ERR002");
                                }
                            }
                        }
                    }else {
                        oneErrorMap.put("poNRICFIN"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(mobileNo)){

                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo"+poIndex, "CHKLMD001_ERR004");
                        }
                    }else {
                        oneErrorMap.put("mobileNo"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(emailAddr)) {
                        if (!  ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("emailAddr"+poIndex, "CHKLMD001_ERR006");
                        }else if(emailAddr.length()>66){

                        }
                    }else {
                        oneErrorMap.put("emailAddr"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(officeTelNo)) {
                        if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                            oneErrorMap.put("officeTelNo"+poIndex, "GENERAL_ERR0015");
                        }
                    }else {
                        oneErrorMap.put("officeTelNo"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                }
                poIndex++;
                String s = stringBuilder.toString();

                if(stringList.contains(s)) {


                }else {
                    stringList.add(stringBuilder.toString());
                }
            }

            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){
                StringBuilder stringBuilder =new StringBuilder();
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String designation = poDto.get(i).getDesignation();
                String officeTelNo = poDto.get(i).getOfficeTelNo();
                /*if(StringUtil.isEmpty(modeOfMedAlert)||"-1".equals(modeOfMedAlert)){
                    oneErrorMap.put("modeOfMedAlert"+dpoIndex,"UC_CHKLMD001_ERR001");
                }*/

                boolean licPerson = poDto.get(i).isLicPerson();
                String dpoIdTypeNoKey = "dpoIdTypeNo"+dpoIndex;
                oneErrorMap = doPsnCommValidate(oneErrorMap,idType,idNo,licPerson,licPersonMap,dpoIdTypeNoKey);
                String idTypeNoErr = oneErrorMap.get(dpoIdTypeNoKey);
                if(!StringUtil.isEmpty(idTypeNoErr)){
                    continue;
                }
                String assignSelect = poDto.get(i).getAssignSelect();
                if(StringUtil.isEmpty(assignSelect)||"-1".equals(assignSelect)){
                    oneErrorMap.put("deputyAssignSelect"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(designation)||"-1".equals(designation)){
                    oneErrorMap.put("deputyDesignation"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(salutation)||"-1".equals(salutation)){
                    oneErrorMap.put("deputySalutation"+dpoIndex,"UC_CHKLMD001_ERR001");
                }

                if(StringUtil.isEmpty(idType)||"-1".equals(idType)){
                    oneErrorMap.put("deputyIdType"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(name)){
                    oneErrorMap.put("deputyName"+dpoIndex,"UC_CHKLMD001_ERR001");
                }else if(name.length()>66){

                }
                if(StringUtil.isEmpty(officeTelNo)){
                    oneErrorMap.put("deputyofficeTelNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }else {
                    if(!officeTelNo.matches("^[6][0-9]{7}$")){
                        oneErrorMap.put("deputyofficeTelNo"+dpoIndex,"GENERAL_ERR0015");
                    }
                }
                if(StringUtil.isEmpty(idNo)){
                    oneErrorMap.put("deputyIdNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if("FIN".equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,"CHKLMD001_ERR005");
                    }else {
                        stringBuilder.append(idType).append(idNo);
                        String s = stringBuilder.toString();
                        if(stringList.contains(s)){
                            oneErrorMap.put("deputyIdNo"+dpoIndex,"UC_CHKLMD001_ERR002");
                        }
                    }
                }
                if("NRIC".equals(idType)){
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!b1){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,"CHKLMD001_ERR005");
                    }else {
                        stringBuilder.append(idType).append(idNo);
                        String s = stringBuilder.toString();
                        if(stringList.contains(s)){
                            oneErrorMap.put("deputyIdNo"+dpoIndex,"UC_CHKLMD001_ERR002");
                        }
                    }
                }

                if(StringUtil.isEmpty(mobileNo)){
                    oneErrorMap.put("deputyMobileNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                else {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        oneErrorMap.put("deputyMobileNo"+dpoIndex, "CHKLMD001_ERR004");
                    }
                }
                if(StringUtil.isEmpty(emailAddr)){
                    oneErrorMap.put("deputyEmailAddr"+dpoIndex,"UC_CHKLMD001_ERR001");
                }else {
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        oneErrorMap.put("deputyEmailAddr"+dpoIndex, "CHKLMD001_ERR006");
                    }else if(emailAddr.length()>66){

                    }
                }
                dpoIndex++;

                String s = stringBuilder.toString();

                if(stringList.contains(s)&&!StringUtil.isEmpty(s)) {


                }else {
                    stringList.add(stringBuilder.toString());
                }
            }


        }
        return oneErrorMap;
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption, String checkedVal){
        StringBuilder sBuffer = new StringBuilder(100);
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
//            sBuffer.append(entry.getKey()+"=\""+entry.getValue()+"\" ");
            sBuffer.append(entry.getKey())
                    .append("=\"")
                    .append(entry.getValue())
                    .append('\"');
        }
        sBuffer.append(" >");
        if(!StringUtil.isEmpty(firestOption)){
//            sBuffer.append("<option value=\"\">"+ firestOption +"</option>");
            sBuffer.append("<option value=\"\">")
                    .append(firestOption)
                    .append("</option>");
        }
        for(SelectOption sp:selectOptionList){
            if(!StringUtil.isEmpty(checkedVal)){
                if(checkedVal.equals(sp.getValue())){
//                    sBuffer.append("<option selected=\"selected\" value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
                    sBuffer.append("<option selected=\"selected\" value=\"")
                            .append(sp.getValue())
                            .append("\">")
                            .append(sp.getText())
                            .append("</option>");
                }else{
                    sBuffer.append("<option value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
                }
            }else{
                sBuffer.append("<option value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
            }
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select "+className+"\" tabindex=\"0\">");
        if(!StringUtil.isEmpty(checkedVal)){
            String text = getTextByValue(selectOptionList,checkedVal);
            sBuffer.append("<span selected=\"selected\" class=\"current\">"+ text +"</span>");
        }else{
            if(!StringUtil.isEmpty(firestOption)){
                sBuffer.append("<span class=\"current\">"+firestOption+"</span>");
            }else{
                sBuffer.append("<span class=\"current\">"+selectOptionList.get(0).getText()+"</span>");
            }
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");

        if(!StringUtil.isEmpty(checkedVal)){
            for(SelectOption kv:selectOptionList){
                if(checkedVal.equals(kv.getValue())){
                    sBuffer.append("<li selected=\"selected\" data-value=\""+kv.getValue()+"\" class=\"option selected\">"+kv.getText()+"</li>");
                }else{
                    sBuffer.append(" <li data-value=\""+kv.getValue()+"\" class=\"option\">"+kv.getText()+"</li>");
                }
            }
        }else if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"\" class=\"option selected\">"+firestOption+"</li>");
            for(SelectOption kv:selectOptionList){
                sBuffer.append(" <li data-value=\""+kv.getValue()+"\" class=\"option\">"+kv.getText()+"</li>");
            }
        }else{
            for(int i = 0;i<selectOptionList.size();i++){
                SelectOption kv = selectOptionList.get(i);
                if(i == 0){
                    sBuffer.append(" <li data-value=\""+kv.getValue()+"\" class=\"option selected\">"+kv.getText()+"</li>");
                }else{
                    sBuffer.append(" <li data-value=\""+kv.getValue()+"\" class=\"option\">"+kv.getText()+"</li>");
                }
            }
        }

        sBuffer.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }

    public static boolean isGetDataFromPage(AppSubmissionDto appSubmissionDto, String currentType, String isClickEdit, boolean isRfi){
        if(appSubmissionDto == null){
            return true;
        }
        boolean isNewApp = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())
                && !isRfi;
        boolean isOther = false;

        if(appSubmissionDto.isNeedEditController()){
            boolean canEdit = checkCanEdit(appSubmissionDto.getAppEditSelectDto(), currentType);
            isOther = canEdit && AppConsts.YES.equals(isClickEdit);
        }
        return isNewApp || isOther;
    }
    //just for one svc
    public static void setLaboratoryDisciplinesInfo(AppSubmissionDto appSubmissionDto,List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos){
        Map<String, HcsaSvcSubtypeOrSubsumedDto> map = IaisCommonUtils.genNewHashMap();
        turn(hcsaSvcSubtypeOrSubsumedDtos, map);
        if(appSubmissionDto == null){
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) || !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                setSvcScopeInfo(appGrpPremisesDtos,appSvcRelatedInfoDto,map);
            }
        }
    }
    //
    public static void setLaboratoryDisciplinesInfo(List<AppGrpPremisesDto> appGrpPremisesDtos,AppSvcRelatedInfoDto appSvcRelatedInfoDto,List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos){
        Map<String, HcsaSvcSubtypeOrSubsumedDto> map = IaisCommonUtils.genNewHashMap();
        turn(hcsaSvcSubtypeOrSubsumedDtos, map);
        if(appSvcRelatedInfoDto == null){
            return;
        }
        setSvcScopeInfo(appGrpPremisesDtos,appSvcRelatedInfoDto,map);
    }


    //for preview get one svc's DisciplineAllocation
    public static Map<String,List<AppSvcDisciplineAllocationDto>> getDisciplineAllocationDtoList(AppSubmissionDto appSubmissionDto,String svcId){
        log.info(StringUtil.changeForLog("get DisciplineAllocationDtoList start..."));
        if(appSubmissionDto == null || StringUtil.isEmpty(svcId)){
            return null;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
        if(!IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())){
            for(AppSvcRelatedInfoDto item:appSubmissionDto.getAppSvcRelatedInfoDtoList()){
                if(svcId.equals(item.getServiceId())){
                    appSvcRelatedInfoDto = item;
                    break;
                }
            }
        }
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        if(appSvcRelatedInfoDto != null){
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
            //get curr premises's appSvcChckListDto
            List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtoList) && !StringUtil.isEmpty(premisesIndexNo)){
                log.info(StringUtil.changeForLog("appSvcLaboratoryDisciplinesDtoList size:"+appSvcLaboratoryDisciplinesDtoList.size()));
                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                    if(premisesIndexNo.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        break;
                    }
                }
                log.info(StringUtil.changeForLog("appSvcChckListDtoList size:"+appSvcChckListDtoList.size()));
                for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                    AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
                    String chkSvcId = appSvcChckListDto.getChkLstConfId();
                    if(!StringUtil.isEmpty(chkSvcId)){
                        log.info(StringUtil.changeForLog("allocationDto size:"+allocationDto.size()));
                        for(AppSvcDisciplineAllocationDto allocation:allocationDto){
                            if(premisesIndexNo.equals(allocation.getPremiseVal()) && chkSvcId.equals(allocation.getChkLstConfId())){
                                log.info(StringUtil.changeForLog("set chkName ..."));
                                appSvcDisciplineAllocationDto = allocation;
                                //set chkName
                                appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                //set selCgoName
                                String idNo = allocation.getIdNo();
                                if(!IaisCommonUtils.isEmpty(appSvcCgoDtoList) && !StringUtil.isEmpty(idNo)){
                                    for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                        if(idNo.equals(appSvcCgoDto.getIdNo())){
                                            log.info(StringUtil.changeForLog("set cgoSel ..."));
                                            appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if(appSvcDisciplineAllocationDto == null){
                            log.info(StringUtil.changeForLog("new AppSvcDisciplineAllocationDto"));
                            appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            appSvcDisciplineAllocationDto.setPremiseVal(premisesIndexNo);
                            appSvcDisciplineAllocationDto.setChkLstConfId(appSvcChckListDto.getChkLstConfId());
                            appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                        }
                        reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                    }
                }
                reloadDisciplineAllocationMap.put(premisesIndexNo, reloadDisciplineAllocation);
            }
        }
        log.info(StringUtil.changeForLog("get DisciplineAllocationDtoList end..."));
        return reloadDisciplineAllocationMap;
    }

    public static AppGrpPremisesDto setWrkTime(AppGrpPremisesDto appGrpPremisesDto){
        if(appGrpPremisesDto == null){
            return appGrpPremisesDto;
        }
        String premType = appGrpPremisesDto.getPremisesType();
        Time wrkTimeFrom = appGrpPremisesDto.getWrkTimeFrom();
        Time wrkTimeTo = appGrpPremisesDto.getWrkTimeTo();
        if(!StringUtil.isEmpty(wrkTimeFrom)){
            LocalTime localTimeFrom = wrkTimeFrom.toLocalTime();
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
                appGrpPremisesDto.setOnsiteStartHH(String.valueOf(localTimeFrom.getHour()));
                appGrpPremisesDto.setOnsiteStartMM(String.valueOf(localTimeFrom.getMinute()));
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
                appGrpPremisesDto.setConStartHH(String.valueOf(localTimeFrom.getHour()));
                appGrpPremisesDto.setConStartMM(String.valueOf(localTimeFrom.getMinute()));
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
                appGrpPremisesDto.setOffSiteStartHH(String.valueOf(localTimeFrom.getHour()));
                appGrpPremisesDto.setOffSiteStartMM(String.valueOf(localTimeFrom.getMinute()));
            }
        }
        if(!StringUtil.isEmpty(wrkTimeTo)){
            LocalTime localTimeTo = wrkTimeTo.toLocalTime();
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
                appGrpPremisesDto.setOnsiteEndHH(String.valueOf(localTimeTo.getHour()));
                appGrpPremisesDto.setOnsiteEndMM(String.valueOf(localTimeTo.getMinute()));
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
                appGrpPremisesDto.setConEndHH(String.valueOf(localTimeTo.getHour()));
                appGrpPremisesDto.setConEndMM(String.valueOf(localTimeTo.getMinute()));
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
                appGrpPremisesDto.setOffSiteEndHH(String.valueOf(localTimeTo.getHour()));
                appGrpPremisesDto.setOffSiteEndMM(String.valueOf(localTimeTo.getMinute()));
            }

        }
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = appGrpPremisesDto.getAppPremPhOpenPeriodList();
        if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriods)){
            for(AppPremPhOpenPeriodDto appPremPhOpenPeriod:appPremPhOpenPeriods){
                if(appPremPhOpenPeriod.getPhDate() != null){
                    appPremPhOpenPeriod.setPhDateStr(Formatter.formatDate(appPremPhOpenPeriod.getPhDate()));
                }
                Time start = appPremPhOpenPeriod.getStartFrom();
                Time end = appPremPhOpenPeriod.getEndTo();
                if(!StringUtil.isEmpty(start)){
                    LocalTime localTimeStart = start.toLocalTime();
                    if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
                        appPremPhOpenPeriod.setOnsiteStartFromHH(String.valueOf(localTimeStart.getHour()));
                        appPremPhOpenPeriod.setOnsiteStartFromMM(String.valueOf(localTimeStart.getMinute()));
                    }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
                        appPremPhOpenPeriod.setConvStartFromHH(String.valueOf(localTimeStart.getHour()));
                        appPremPhOpenPeriod.setConvStartFromMM(String.valueOf(localTimeStart.getMinute()));
                    }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
                        appPremPhOpenPeriod.setOffSiteStartFromHH(String.valueOf(localTimeStart.getHour()));
                        appPremPhOpenPeriod.setOffSiteStartFromMM(String.valueOf(localTimeStart.getMinute()));
                    }
                }
                if(!StringUtil.isEmpty(end)){
                    LocalTime localTimeEnd = end.toLocalTime();
                    if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
                        appPremPhOpenPeriod.setOnsiteEndToHH(String.valueOf(localTimeEnd.getHour()));
                        appPremPhOpenPeriod.setOnsiteEndToMM(String.valueOf(localTimeEnd.getMinute()));
                    }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
                        appPremPhOpenPeriod.setConvEndToHH(String.valueOf(localTimeEnd.getHour()));
                        appPremPhOpenPeriod.setConvEndToMM(String.valueOf(localTimeEnd.getMinute()));
                    }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
                        appPremPhOpenPeriod.setOffSiteEndToHH(String.valueOf(localTimeEnd.getHour()));
                        appPremPhOpenPeriod.setOffSiteEndToMM(String.valueOf(localTimeEnd.getMinute()));
                    }
                }
            }
        }
        if(appGrpPremisesDto.getCertIssuedDt() != null){
            String certIssuedDtStr = Formatter.formatDate(appGrpPremisesDto.getCertIssuedDt());
            appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
        }
        return appGrpPremisesDto;
    }


    public static void setDisciplineAllocationDtoInfo(AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        if(appSvcRelatedInfoDto == null){
            return;
        }
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtos = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)&&!IaisCommonUtils.isEmpty(appSvcCgoDtos) && !IaisCommonUtils.isEmpty(appSvcDisciplineAllocationDtos)){
            for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:appSvcDisciplineAllocationDtos){
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                String svcScopeConfigId = appSvcDisciplineAllocationDto.getChkLstConfId();
                if(StringUtil.isEmpty(idNo) || StringUtil.isEmpty(svcScopeConfigId)){
                    continue;
                }
                //set svc cgoNo
                for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
                    String cgoIdNo = appSvcCgoDto.getIdNo();
                    if(StringUtil.isEmpty(cgoIdNo)){
                        continue;
                    }
                    if(idNo.equals(cgoIdNo)){
                        appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                    }
                }
            }
        }
    }

    /**
     *
     * @param appSubmissionDto
     * @Descriptio  cgo,po,dpo,map,
     * @return
     */
    public static Map<String,AppSvcPrincipalOfficersDto> getPsnMapFromSubDto(AppSubmissionDto appSubmissionDto){
        Map<String,AppSvcPrincipalOfficersDto> psnMap = IaisCommonUtils.genNewHashMap();
        if(appSubmissionDto == null){
            return psnMap;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                //cgo
                List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                    for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
                        AppSvcPrincipalOfficersDto psnDto = psnMap.get(appSvcCgoDto.getIdNo());
                        if(psnDto != null){
                            continue;
                        }
                        psnDto = transferCgoToPsnDto(appSvcCgoDto);
                        psnMap.put(appSvcCgoDto.getIdNo(),psnDto);
                    }
                }
                //po and dpo
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)){
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
                        AppSvcPrincipalOfficersDto psnDto = psnMap.get(appSvcPrincipalOfficersDto.getIdNo());
                        if(psnDto != null){
                            psnDto.setOfficeTelNo(appSvcPrincipalOfficersDto.getOfficeTelNo());
                        }else{
                            psnDto = appSvcPrincipalOfficersDto;
                        }
                        psnMap.put(appSvcPrincipalOfficersDto.getIdNo(),psnDto);
                    }
                }
                //medAlert
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPsnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                if(!IaisCommonUtils.isEmpty(appSvcMedAlertPsnDtos)){
                    for(AppSvcPrincipalOfficersDto appSvcMedAlertPsnDto:appSvcMedAlertPsnDtos) {
                        AppSvcPrincipalOfficersDto psnDto = psnMap.get(appSvcMedAlertPsnDto.getIdNo());
                        if(psnDto != null){
                            psnDto.setPreferredMode(appSvcMedAlertPsnDto.getPreferredMode());
                        }else{
                            psnDto = appSvcMedAlertPsnDto;
                        }
                        psnMap.put(appSvcMedAlertPsnDto.getIdNo(),psnDto);
                    }
                }
            }
        }
        return psnMap;
    }

    public static AppSvcCgoDto getPsnFromSubDto(AppSubmissionDto appSubmissionDto, String idNo){
        if(StringUtil.isEmpty(idNo)){
            return null;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(appSvcCgoDtos != null){
                    for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
                        if(idNo.equals(appSvcCgoDto.getIdNo())){
                            return appSvcCgoDto;
                        }
                    }
                }
            }
        }
        return null;
    }


    public static Map<String,String> doValidateMedAlertPsn(List<AppSvcPrincipalOfficersDto> medAlertPsnDtos,Map<String,AppSvcPrincipalOfficersDto> licPersonMap){
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isEmpty(medAlertPsnDtos)){
            return errMap;
        }
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        for(int i=0;i<medAlertPsnDtos.size();i++ ){
            String assignSelect = medAlertPsnDtos.get(i).getAssignSelect();
            if("-1".equals(assignSelect)||StringUtil.isEmpty(assignSelect)){
                errMap.put("assignSelect"+i, "UC_CHKLMD001_ERR001");
            }else {
                String idTyp = medAlertPsnDtos.get(i).getIdType();
                String idNo = medAlertPsnDtos.get(i).getIdNo();
                boolean licPerson = medAlertPsnDtos.get(i).isLicPerson();
                String idTypeNoKey = "idTypeNo"+i;
                errMap = doPsnCommValidate(errMap,idTyp,idNo,licPerson,licPersonMap,idTypeNoKey);
                String idTypeNoErr = errMap.get(idTypeNoKey);
                if(!StringUtil.isEmpty(idTypeNoErr)){
                    continue;
                }
                StringBuilder stringBuilder1=new StringBuilder();
                if("-1".equals(idTyp)||StringUtil.isEmpty(idTyp)){
                    errMap.put("idTyp"+i, "UC_CHKLMD001_ERR001");
                }
                String salutation = medAlertPsnDtos.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    errMap.put("salutation"+i,"UC_CHKLMD001_ERR001");
                }
                //to do
                if(StringUtil.isEmpty(idNo)){
                    errMap.put("idNo"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if("FIN".equals(idTyp)){
                        boolean b = SgNoValidator.validateFin(idNo);
                        if(!b){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                        stringBuilder1.append(idTyp).append(idNo);
                        if(!StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"UC_CHKLMD001_ERR002");
                            }
                        }
                    }
                    if("NRIC".equals(idTyp)){
                        boolean b1 = SgNoValidator.validateNric(idNo);
                        if(!b1){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                        stringBuilder1.append(idTyp).append(idNo);
                        if(!StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"UC_CHKLMD001_ERR002");
                            }
                        }
                    }
                }

                String name = medAlertPsnDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if(name.length()>66){

                    }
                }

                String mobileNo = medAlertPsnDtos.get(i).getMobileNo();
                if(StringUtil.isEmpty(mobileNo)){
                    errMap.put("mobileNo"+i, "UC_CHKLMD001_ERR001");
                }else if (!StringUtil.isEmpty(mobileNo)) {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo"+i, "CHKLMD001_ERR004");
                    }
                }
                String emailAddr = medAlertPsnDtos.get(i).getEmailAddr();

                if(StringUtil.isEmpty(emailAddr)){
                    errMap.put("emailAddr"+i, "UC_CHKLMD001_ERR001");
                }else if (!StringUtil.isEmpty(emailAddr)) {
                    if (! ValidationUtils.isEmail(emailAddr)) {
                        errMap.put("emailAddr"+i, "CHKLMD001_ERR006");
                    }else if(emailAddr.length()>66) {

                    }
                }

                if(!StringUtil.isEmpty(stringBuilder1.toString())){
                    if(!stringList.contains(stringBuilder1.toString())){
                        stringList.add( stringBuilder1.toString());
                    }
                }

                String preferredMode = medAlertPsnDtos.get(i).getPreferredMode();
                if(StringUtil.isEmpty(preferredMode)){
                    errMap.put("preferredModeVal"+i,"UC_CHKLMD001_ERR001");
                }

            }

        }

        return errMap;
    }

    public static List<AppSvcPrincipalOfficersDto> transferCgoToPsnDtoList(List<AppSvcCgoDto> appSvcCgoDtos){
        List<AppSvcPrincipalOfficersDto> psnDtos = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(appSvcCgoDtos)){
            return psnDtos;
        }
        for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
            AppSvcPrincipalOfficersDto psnDto = transferCgoToPsnDto(appSvcCgoDto);
            psnDtos.add(psnDto);
        }
        return psnDtos;
    }

    public static void setPsnIntoSelMap(HttpServletRequest request, List<AppSvcPrincipalOfficersDto> psnDtos, String svcCode){
        if(IaisCommonUtils.isEmpty(psnDtos)){
            return;
        }
        Map<String,AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        for(AppSvcPrincipalOfficersDto psnDto:psnDtos){
            if(StringUtil.isEmpty(psnDto.getIdNo()) || StringUtil.isEmpty(psnDto.getIdType())){
                continue;
            }
            String personMapKey = psnDto.getIdType()+","+psnDto.getIdNo();
            AppSvcPrincipalOfficersDto person = personMap.get(personMapKey);
            Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            if(person == null){
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())){
                    psnDto.setNeedSpcOptList(true);
                    List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode,true);
                    psnDto.setSpcOptList(specialityOpts);
                    String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psnDto.getSpeciality());
                    psnDto.setSpecialityHtml(specialtySelectStr);
                }
                psnDto.setAssignSelect(getPersonKey(psnDto.getIdType(),psnDto.getIdNo()));
                personMap.put(personMapKey,psnDto);
            }else{
                //set different page column
                person.setAssignSelect(getPersonKey(psnDto.getIdType(),psnDto.getIdNo()));
                person.setSalutation(psnDto.getSalutation());
                person.setName(psnDto.getName());
                person.setIdType(psnDto.getIdType());
                person.setIdNo(psnDto.getIdNo());
                person.setMobileNo(psnDto.getMobileNo());
                person.setEmailAddr(psnDto.getEmailAddr());
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())){
                    person.setDesignation(psnDto.getDesignation());
                    person.setProfessionType(psnDto.getProfessionType());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpeciality(psnDto.getSpeciality());
                    person.setSpecialityOther(psnDto.getSpecialityOther());
                    person.setSubSpeciality(psnDto.getSubSpeciality());
                    //
                    person.setNeedSpcOptList(true);
                    List<SelectOption> spcOpts = person.getSpcOptList();
                    List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode,false);
                    if(!IaisCommonUtils.isEmpty(spcOpts)){
                        for(SelectOption sp:spcOpts){
                            if(!specialityOpts.contains(sp)){
                                specialityOpts.add(sp);
                            }
                        }
                        person.setSpcOptList(specialityOpts);
                    }else{
                        SelectOption sp = new SelectOption("other", "Others");
                        specialityOpts.add(sp);
                        person.setSpcOptList(specialityOpts);
                    }
                    String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, person.getSpeciality());
                    person.setSpecialityHtml(specialtySelectStr);
                    person.setCgoPsn(true);
                    psnDto.setSpcOptList(specialityOpts);
                    psnDto.setSpecialityHtml(specialtySelectStr);
                    psnDto.setNeedSpcOptList(true);
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())){
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                    person.setPoPsn(true);
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                    person.setDpoPsn(true);
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())){
                    person.setPreferredMode(psnDto.getPreferredMode());
                    person.setMapPsn(true);
                }
                //person.setLicPerson(true);
                //for person dtos
                psnDto.setLicPerson(person.isLicPerson());
                psnDto.setAssignSelect(getPersonKey(psnDto.getIdType(),psnDto.getIdNo()));
                personMap.put(personMapKey,person);
            }
        }
        ParamUtil.setSessionAttr(request,NewApplicationDelegator.PERSONSELECTMAP, (Serializable) personMap);
    }

    public static Map<String,AppSvcPrincipalOfficersDto> getLicPsnIntoSelMap(HttpServletRequest request, List<PersonnelListQueryDto> licPsnDtos) {
        Map<String,AppSvcPrincipalOfficersDto> personMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(licPsnDtos)) {
            for (PersonnelListQueryDto psnDto : licPsnDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(psnDto.getSvcName());
                String svcCode = hcsaServiceDto.getSvcCode();
                String personMapKey = psnDto.getIdType() + "," + psnDto.getIdNo();
                AppSvcPrincipalOfficersDto person = personMap.get(personMapKey);
                Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
                specialtyAttr.put("name", "specialty");
                specialtyAttr.put("class", "specialty");
                specialtyAttr.put("style", "display: none;");
                if (person == null) {
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())) {
                        psnDto.setNeedSpcOptList(true);
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, true);
                        psnDto.setSpcOptList(specialityOpts);
                        String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psnDto.getSpeciality());
                        psnDto.setSpecialityHtml(specialtySelectStr);
                    }
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(psnDto, AppSvcPrincipalOfficersDto.class);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())){
                        appSvcPrincipalOfficersDto.setCgoPsn(true);
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())){
                        appSvcPrincipalOfficersDto.setPoPsn(true);
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                        appSvcPrincipalOfficersDto.setDpoPsn(true);
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())){
                        appSvcPrincipalOfficersDto.setMapPsn(true);
                    }
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    personMap.put(personMapKey, appSvcPrincipalOfficersDto);
                } else {
                    //set different page column
                    person.setSalutation(psnDto.getSalutation());
                    person.setName(psnDto.getName());
                    person.setIdType(psnDto.getIdType());
                    person.setIdNo(psnDto.getIdNo());
                    person.setMobileNo(psnDto.getMobileNo());
                    person.setEmailAddr(psnDto.getEmailAddr());
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())) {
                        person.setDesignation(psnDto.getDesignation());
                        person.setProfessionType(psnDto.getProfessionType());
                        person.setProfRegNo(psnDto.getProfRegNo());
                        person.setSpeciality(psnDto.getSpeciality());
                        person.setSpecialityOther(psnDto.getSpecialityOther());
                        person.setSubSpeciality(psnDto.getSubSpeciality());
                        //
                        person.setNeedSpcOptList(true);
                        List<SelectOption> spcOpts = person.getSpcOptList();
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, false);
                        if (!IaisCommonUtils.isEmpty(spcOpts)) {
                            for (SelectOption sp : spcOpts) {
                                if (!specialityOpts.contains(sp)) {
                                    specialityOpts.add(sp);
                                }
                            }
                            person.setSpcOptList(specialityOpts);
                        } else {
                            SelectOption sp = new SelectOption("other", "Others");
                            specialityOpts.add(sp);
                            person.setSpcOptList(specialityOpts);
                        }
                        String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, person.getSpeciality());
                        person.setSpecialityHtml(specialtySelectStr);
                        person.setCgoPsn(true);
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())) {
                        person.setOfficeTelNo(psnDto.getOfficeTelNo());
                        person.setPoPsn(true);
                    }
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                        person.setOfficeTelNo(psnDto.getOfficeTelNo());
                        person.setDpoPsn(true);
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())) {
                        person.setPreferredMode(psnDto.getPreferredMode());
                        person.setDpoPsn(true);
                    }

                    person.setLicPerson(true);
                    personMap.put(personMapKey, person);
                }
            }
        }
        return personMap;
    }

    public static List<SelectOption> genSpecialtySelectList(String svcCode, boolean needOtherOpt){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please Select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                if(needOtherOpt){
                    SelectOption ssl4 = new SelectOption("other", "Others");
                    specialtySelectList.add(ssl4);
                }
            }else if(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please Select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                if(needOtherOpt){
                    SelectOption ssl4 = new SelectOption("other", "Others");
                    specialtySelectList.add(ssl4);
                }
            }else {
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please Select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl5 = new SelectOption("Haematology", "Haematology");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
                specialtySelectList.add(ssl5);
                if(needOtherOpt){
                    SelectOption ssl6 = new SelectOption("other", "Others");
                    specialtySelectList.add(ssl6);
                }
            }
        }
        return specialtySelectList;
    }

    //todo: change this mode
    public static List<SelectOption> getAllSpecialtySelList(){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        SelectOption ssl1 = new SelectOption("-1", "Please Select");
        SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
        SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
        SelectOption ssl4 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
        SelectOption ssl5 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
        SelectOption ssl6 = new SelectOption("other", "Others");
        specialtySelectList.add(ssl1);
        specialtySelectList.add(ssl2);
        specialtySelectList.add(ssl3);
        specialtySelectList.add(ssl4);
        specialtySelectList.add(ssl5);
        specialtySelectList.add(ssl6);
        return specialtySelectList;
    }

    public static AppSubmissionDto syncPsnData(HttpServletRequest request, AppSubmissionDto appSubmissionDto, List<AppSvcPrincipalOfficersDto> psnDtos){
        if(appSubmissionDto == null || IaisCommonUtils.isEmpty(psnDtos)){
           return appSubmissionDto;
        }
        Map<String,AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                syncCgoDto(appSvcRelatedInfoDto.getAppSvcCgoDtoList(), personMap);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), personMap);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), personMap);
            }
        }
        return appSubmissionDto;
    }


    public static List<SelectOption> genAssignPersonSel(HttpServletRequest request, boolean needFirstOpt){
        List<SelectOption> psnSelectList = IaisCommonUtils.genNewArrayList();
        if(needFirstOpt){
            SelectOption sp0 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
            psnSelectList.add(sp0);
        }
        SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
        psnSelectList.add(sp1);
        Map<String,AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        personMap.forEach((k,v)->{
            SelectOption sp = new SelectOption(k,v.getName()+", "+v.getIdNo()+" ("+v.getIdType()+")");
            psnSelectList.add(sp);
        });
        return psnSelectList;
    }

    /**
     * only rfc
     */
    public static void setPreviewDta(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc){
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                String svcId = (String) ParamUtil.getSessionAttr(bpc.request,"SvcId");
                ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDtos.get(0));
                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                ParamUtil.setRequestAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
                //PO/DPO
                List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
                List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos.get(0).getAppSvcPrincipalOfficersDtoList())){
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcRelatedInfoDtos.get(0).getAppSvcPrincipalOfficersDtoList()){
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }
                    }
                }
                ParamUtil.setRequestAttr(bpc.request, "ReloadPrincipalOfficers", principalOfficersDtos);
                ParamUtil.setRequestAttr(bpc.request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);

            }
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        }

    }

    public static void setTimeList(HttpServletRequest request){
        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 24;i++){
            timeHourList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 60;i++){
            timeMinList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        ParamUtil.setRequestAttr(request, "premiseHours", timeHourList);
        ParamUtil.setRequestAttr(request, "premiseMinute", timeMinList);

    }

    public static void setPremSelect(HttpServletRequest request,Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap){
        List premisesSelect = getPremisesSel();
        List conveyancePremSel = getPremisesSel();
        List offSitePremSel = getPremisesSel();
        if (licAppGrpPremisesDtoMap != null && !licAppGrpPremisesDtoMap.isEmpty()) {
            for (AppGrpPremisesDto item : licAppGrpPremisesDtoMap.values()) {
                SelectOption sp= new SelectOption(item.getPremisesSelect(), item.getAddress());
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())) {
                    premisesSelect.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())){
                    conveyancePremSel.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(item.getPremisesType())){
                    offSitePremSel.add(sp);
                }
            }
        }
        ParamUtil.setSessionAttr(request, "premisesSelect", (Serializable) premisesSelect);
        ParamUtil.setSessionAttr(request, "conveyancePremSel", (Serializable) conveyancePremSel);
        ParamUtil.setSessionAttr(request, "offSitePremSel", (Serializable) offSitePremSel);
    }

    public static void setPremAddressSelect(HttpServletRequest request){
        List<SelectOption> addrTypeOpt = new ArrayList<>();
        SelectOption addrTypeSp = new SelectOption("",NewApplicationDelegator.FIRESTOPTION);
        addrTypeOpt.add(addrTypeSp);
        addrTypeOpt.addAll(MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE));
        ParamUtil.setRequestAttr(request,"addressType",addrTypeOpt);
    }

    /**
     * for preview page
     */
    public static void setDocInfo(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<AppSvcDocDto> appSvcDocDtos, List<HcsaSvcDocConfigDto> primaryDocConfig, List<HcsaSvcDocConfigDto> svcDocConfig){
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:primaryDocConfig){
                    String docConfigId = appGrpPrimaryDocDto.getSvcComDocId();
                    if(!StringUtil.isEmpty(docConfigId) && docConfigId.equals(hcsaSvcDocConfigDto.getId())){
                        appGrpPrimaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                        break;
                    }
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:svcDocConfig){
                    String docConfigId = appSvcDocDto.getSvcDocId();
                    if(!StringUtil.isEmpty(docConfigId) && docConfigId.equals(hcsaSvcDocConfigDto.getId())){
                        appSvcDocDto.setUpFileName(hcsaSvcDocConfigDto.getDocTitle());
                        break;
                    }
                }
            }
        }
    }

    public static void setPremEditStatus(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(oldAppGrpPremisesDtos)){
            return;
        }
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            String premKey = getPremKey(appGrpPremisesDto);
            for(AppGrpPremisesDto oldAppGrppremisesDto:oldAppGrpPremisesDtos){
                String oldPremKey = getPremKey(oldAppGrppremisesDto);
                if(premKey.equals(oldPremKey)){
                    appGrpPremisesDto.setExistingData(AppConsts.NO);
                    break;
                }
            }
        }

    }

    public static String getPremKey(AppGrpPremisesDto appGrpPremisesDto){
        String premKey = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            premKey = IaisCommonUtils.genPremisesKey(appGrpPremisesDto.getPostalCode(),appGrpPremisesDto.getBlkNo(),appGrpPremisesDto.getFloorNo(),appGrpPremisesDto.getUnitNo());
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            premKey = IaisCommonUtils.genPremisesKey(appGrpPremisesDto.getConveyancePostalCode(),appGrpPremisesDto.getConveyanceBlockNo(),appGrpPremisesDto.getConveyanceFloorNo(),appGrpPremisesDto.getConveyanceUnitNo());
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            premKey = IaisCommonUtils.genPremisesKey(appGrpPremisesDto.getOffSitePostalCode(),appGrpPremisesDto.getOffSiteBlockNo(),appGrpPremisesDto.getOffSiteFloorNo(),appGrpPremisesDto.getOffSiteUnitNo());

        }
        return premKey;
    }

    public static List<String> setPremiseHciList(AppGrpPremisesEntityDto premisesDto,List<String> premisesHci){
        String premisesKey = IaisCommonUtils.genPremisesKey(premisesDto.getPostalCode(),premisesDto.getBlkNo(),premisesDto.getFloorNo(),premisesDto.getUnitNo());
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())){
            premisesHci.add(premisesDto.getHciName()+premisesKey);
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())){
            premisesHci.add(premisesDto.getVehicleNo()+premisesKey);
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())){
            premisesHci.add(premisesKey);
        }
        return premisesHci;
    }

    public static boolean checkIsRfi(HttpServletRequest request){
        Object requestInformationConfig = ParamUtil.getSessionAttr(request,NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if(requestInformationConfig != null){
            isRfi = true;
        }
        return isRfi;
    }

    public static AppSvcPrincipalOfficersDto getPsnInfoFromLic(HttpServletRequest request,String personKey) {
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
        Map<String, AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.LICPERSONSELECTMAP);
        if (personMap != null) {
            AppSvcPrincipalOfficersDto person = personMap.get(personKey);
            if (person != null) {
                appSvcPrincipalOfficersDto = person;
            }
        }
        return appSvcPrincipalOfficersDto;
    }

    public static String getPersonKey(String idType, String idNo){
        String personKey = "";
        if(!StringUtil.isEmpty(idNo) && !StringUtil.isEmpty(idType)){
            personKey = idType+ "," + idNo;
        }
        return personKey;
    }



    //=============================================================================
    //private method
    //=============================================================================
    private static List<SelectOption> getPremisesSel(){
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        SelectOption cps1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        SelectOption cps2 = new SelectOption("newPremise", "Add a new premises");
        selectOptionList.add(cps1);
        selectOptionList.add(cps2);
        return selectOptionList;
    }

    private static boolean checkCanEdit(AppEditSelectDto appEditSelectDto, String currentType){
        boolean pageCanEdit = false;
        if(appEditSelectDto != null){
            if(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION.equals(currentType)){
                pageCanEdit = appEditSelectDto.isPremisesEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isServiceEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isDocEdit();
            }else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_DOCUMENT.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isServiceEdit() || appEditSelectDto.isDocEdit();
            }
        }
        return pageCanEdit;
    }

    private NewApplicationHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static void turn(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,Map<String,HcsaSvcSubtypeOrSubsumedDto> allCheckListMap){

        for(HcsaSvcSubtypeOrSubsumedDto dto:hcsaSvcSubtypeOrSubsumedDtos){
            allCheckListMap.put(dto.getId(),dto);
            if(dto.getList() != null && dto.getList().size()>0){
                turn(dto.getList(), allCheckListMap);
            }
        }

    }

    private static void setSvcScopeInfo(List<AppGrpPremisesDto> appGrpPremisesDtos,AppSvcRelatedInfoDto appSvcRelatedInfoDto,Map<String, HcsaSvcSubtypeOrSubsumedDto> map){
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtos){
                if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        String premval = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                        if(!StringUtil.isEmpty(premIndexNo) && premIndexNo.equals(premval)){
                            appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                        }
                    }
                }
                if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList())){
                    for(AppSvcChckListDto appSvcChckListDto:appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList()){
                        HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = map.get(appSvcChckListDto.getChkLstConfId());
                        if(hcsaSvcSubtypeOrSubsumedDto != null){
                            appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                            appSvcChckListDto.setChkLstType(hcsaSvcSubtypeOrSubsumedDto.getType());
                            appSvcChckListDto.setChkCode(hcsaSvcSubtypeOrSubsumedDto.getCode());
                        }
                    }
                }
            }
        }
    }

    private static AppSvcPrincipalOfficersDto transferCgoToPsnDto(AppSvcCgoDto appSvcCgoDto){
        AppSvcPrincipalOfficersDto psnDto = new AppSvcPrincipalOfficersDto();
        if(appSvcCgoDto == null){
            return psnDto;
        }
        psnDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        psnDto.setSalutation(appSvcCgoDto.getSalutation());
        psnDto.setName(appSvcCgoDto.getName());
        psnDto.setIdType(appSvcCgoDto.getIdType());
        psnDto.setIdNo(appSvcCgoDto.getIdNo());
        psnDto.setDesignation(appSvcCgoDto.getDesignation());
        psnDto.setOfficeTelNo(appSvcCgoDto.getOfficeTelNo());
        psnDto.setMobileNo(appSvcCgoDto.getMobileNo());
        psnDto.setOfficeTelNo(appSvcCgoDto.getOfficeTelNo());
        psnDto.setEmailAddr(appSvcCgoDto.getEmailAddr());
        psnDto.setPreferredMode(appSvcCgoDto.getPreferredMode());
        psnDto.setProfessionType(appSvcCgoDto.getProfessionType());
        psnDto.setProfRegNo(appSvcCgoDto.getProfRegNo());
        psnDto.setSpeciality(appSvcCgoDto.getSpeciality());
        psnDto.setSpecialityOther(appSvcCgoDto.getSpecialityOther());
        psnDto.setSubSpeciality(appSvcCgoDto.getSubSpeciality());
        return psnDto;
    }

    private static void syncCgoDto(List<AppSvcCgoDto> appSvcCgoDtos, Map<String,AppSvcPrincipalOfficersDto> personMap){
        if(IaisCommonUtils.isEmpty(appSvcCgoDtos)){
            return;
        }
        for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
            String personKey = appSvcCgoDto.getIdType()+ "," + appSvcCgoDto.getIdNo();
            AppSvcPrincipalOfficersDto selPerson = personMap.get(personKey);
            if(selPerson != null){
                appSvcCgoDto.setAssignSelect(getPersonKey(selPerson.getIdType(),selPerson.getIdNo()));
                appSvcCgoDto.setDesignation(selPerson.getDesignation());
                appSvcCgoDto.setName(selPerson.getName());
                appSvcCgoDto.setIdType(selPerson.getIdType());
                appSvcCgoDto.setIdNo(selPerson.getIdNo());
                appSvcCgoDto.setDesignation(selPerson.getDesignation());
                appSvcCgoDto.setProfessionType(selPerson.getProfessionType());
                appSvcCgoDto.setProfRegNo(selPerson.getProfRegNo());
                appSvcCgoDto.setSpeciality(selPerson.getSpeciality());
                appSvcCgoDto.setSpecialityOther(selPerson.getSpecialityOther());
                appSvcCgoDto.setSubSpeciality(selPerson.getSubSpeciality());
                appSvcCgoDto.setMobileNo(selPerson.getMobileNo());
                appSvcCgoDto.setEmailAddr(selPerson.getEmailAddr());
                //
                appSvcCgoDto.setNeedSpcOptList(true);
                appSvcCgoDto.setSpcOptList(selPerson.getSpcOptList());
                appSvcCgoDto.setSpecialityHtml(selPerson.getSpecialityHtml());
                //set lic person info
                appSvcCgoDto.setLicPerson(selPerson.isLicPerson());
                appSvcCgoDto.setCgoPsn(selPerson.isCgoPsn());
                appSvcCgoDto.setPoPsn(selPerson.isPoPsn());
                appSvcCgoDto.setDpoPsn(selPerson.isDpoPsn());
                appSvcCgoDto.setMapPsn(selPerson.isMapPsn());
            }
        }
    }

    private static void syncPsnDto(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos, Map<String,AppSvcPrincipalOfficersDto> personMap){
        if(IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)){
            return;
        }
        for(AppSvcPrincipalOfficersDto person:appSvcPrincipalOfficersDtos){
            String personKey = person.getIdType()+ "," + person.getIdNo();
            AppSvcPrincipalOfficersDto selPerson = personMap.get(personKey);
            if(selPerson != null){
                person.setAssignSelect(getPersonKey(selPerson.getIdType(),selPerson.getIdNo()));
                person.setDesignation(selPerson.getDesignation());
                person.setName(selPerson.getName());
                person.setIdType(selPerson.getIdType());
                person.setIdNo(selPerson.getIdNo());
                person.setMobileNo(selPerson.getMobileNo());
                person.setEmailAddr(selPerson.getEmailAddr());
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(person.getPsnType()) || ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(person.getPsnType()) ){
                    person.setOfficeTelNo(selPerson.getOfficeTelNo());
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(person.getPsnType())){
                    person.setPreferredMode(selPerson.getPreferredMode());
                }
                //set lic person info
                person.setLicPerson(selPerson.isLicPerson());
                person.setCgoPsn(selPerson.isCgoPsn());
                person.setPoPsn(selPerson.isPoPsn());
                person.setDpoPsn(selPerson.isDpoPsn());
                person.setMapPsn(selPerson.isMapPsn());

            }
        }
    }

    private static String getTextByValue(List<SelectOption> selectOptions,String value){
        String text = "";
        if(!IaisCommonUtils.isEmpty(selectOptions) && !StringUtil.isEmpty(value)){
            for(SelectOption sp:selectOptions){
                if(value.equals(sp.getValue())){
                    text = sp.getText();
                    break;
                }
            }
        }
        return text;
    }

    private static Map<String,String> doPsnCommValidate(Map<String,String> errMap,String idType,String idNo,boolean licPerson,Map<String,AppSvcPrincipalOfficersDto> licPersonMap,String errKey){
        if(!StringUtil.isEmpty(idType) && !StringUtil.isEmpty(idNo) && !licPerson){
            String personKey = NewApplicationHelper.getPersonKey(idType, idNo);
            AppSvcPrincipalOfficersDto person = licPersonMap.get(personKey);
            if(person != null){
                String errMsg = MessageUtil.getMessageDesc("NEW_ERR0006");
//                        errMsg = "The personnel of ID No. <ID No.> has already been registerted, please select from the dropdown list instead";
                errMsg = errMsg.replace("<ID No.>",idNo);
                errMap.put(errKey,errMsg);
            }
        }
        return errMap;
    }


/*
* @parameter file
* @parameter fileTypes
* @parameter fileSize
* */

    public static Map<String,Boolean> validateFile(CommonsMultipartFile file,List<String> fileTypes,Long fileSize){
        Map<String,Boolean> map=new HashMap<>();
        if(file!=null){
            long size = file.getSize();
            String filename = file.getOriginalFilename();
            String fileType=  filename.substring(filename.lastIndexOf(".")+1);
            String s = fileType.toUpperCase();
            if(!fileTypes.contains(s)){
                map.put("fileType",false);
            }else {
                map.put("fileType",true);
            }
            if(size>fileSize){
                map.put("fileSize",false);
            }else {
                map.put("fileSize",true);
            }
        }

        return map;
    }

    /*
     * @parameter file
     * @parameter fileTypes
     * */

    public static Map<String,Boolean> validateFile(CommonsMultipartFile file){
        List<String> list=new ArrayList<>();
        list.add("PDF");
        list.add("JPG");
        list.add("PNG");
        list.add("DOCX");
        list.add("DOC");
        Long size=4*1024*1024L;
        return validateFile(file,list,size);
    }
}
