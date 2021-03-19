package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayNetsAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayStripeAPI;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PersonFieldDto;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.util.CopyUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    public static Map<String,String> doValidateLaboratory(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppSvcLaboratoryDisciplinesDto>  appSvcLaboratoryDisciplinesDtos, String serviceId,List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos){
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
                    /*   map.put("checkError","NEW_ERR0012");*/
                }else {
                    boolean selectOtherScope = selectOtherScope(listDtos);
                    if(selectOtherScope){
                        boolean selectOtherChildrenScope = false;
                        //check children scope is selected
                        List<String> childrenConfigIdList = getOtherScopeChildrenIdList(hcsaSvcSubtypeOrSubsumedDtos);
                        if(!IaisCommonUtils.isEmpty(childrenConfigIdList)){
                            for(AppSvcChckListDto appSvcChckListDto:listDtos){
                                if(childrenConfigIdList.contains(appSvcChckListDto.getChkLstConfId())){
                                    selectOtherChildrenScope = true;
                                    break;
                                }
                            }
                        }
                        if(!selectOtherChildrenScope){
                            map.put("otherScopeError"+premCount,MessageUtil.replaceMessage("GENERAL_ERR0006",NewApplicationConstant.PLEASEINDICATE,"field"));
                        }
                    }
                    for(int i=0;i<listDtos.size();i++){
                        if(NewApplicationConstant.PLEASEINDICATE.equals(listDtos.get(i).getChkName())&&StringUtil.isEmpty(listDtos.get(i).getOtherScopeName()) ){
                            map.put("pleaseIndicateError"+premCount,MessageUtil.replaceMessage("GENERAL_ERR0006",NewApplicationConstant.PLEASEINDICATE,"field"));
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
                    map.put("checkError","NEW_ERR0012");
                }
            premCount++;
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(map);
        return map;
    }

    public static Map<String,String> doValidateGovernanceOfficers(List<AppSvcCgoDto> appSvcCgoList, Map<String,AppSvcPersonAndExtDto> licPersonMap, String svcCode){

        if(appSvcCgoList == null){
            return new HashMap<>(1);
        }

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        for(int i=0;i<appSvcCgoList.size();i++ ){
            StringBuilder stringBuilder1=new StringBuilder();
            String assignSelect = appSvcCgoList.get(i).getAssignSelect();
            if("-1".equals(assignSelect)){
                errMap.put("assignSelect"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Add/Assign a Clinical Governance Officer","field"));
            }else {
                String idTyp = appSvcCgoList.get(i).getIdType();
                String idNo = appSvcCgoList.get(i).getIdNo();
                boolean licPerson = appSvcCgoList.get(i).isLicPerson();
                String idTypeNoKey = "idTypeNo"+i;
                errMap = doPsnCommValidate(errMap,idTyp,idNo,licPerson,licPersonMap,idTypeNoKey,svcCode);
                boolean newErr0006 = StringUtil.isEmpty(errMap.get(idTypeNoKey));
//                String idTypeNoErr = errMap.get(idTypeNoKey);
//                if(!StringUtil.isEmpty(idTypeNoErr)){
//                    continue;
//                }

                if("-1".equals(idTyp)||StringUtil.isEmpty(idTyp)){
                    errMap.put("idTyp"+i, MessageUtil.replaceMessage("GENERAL_ERR0006","ID Type","field"));
                }
                String salutation = appSvcCgoList.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    errMap.put("salutation"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Salutation","field"));
                }
                String speciality = appSvcCgoList.get(i).getSpeciality();
                if(StringUtil.isEmpty(speciality)||"-1".equals(speciality)){
                    errMap.put("speciality"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Specialty","field"));
                }else {
                    if("other".equals(speciality)){
                        String specialityOther = appSvcCgoList.get(i).getSpecialityOther();
                        if(StringUtil.isEmpty(specialityOther)){
                            errMap.put("other"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Others","field"));
                        }
                    }
                }
                String professionType = appSvcCgoList.get(i).getProfessionType();
                if(StringUtil.isEmpty(professionType)){
                    errMap.put("professionType"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Type ","field"));
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if(StringUtil.isEmpty(designation)){
                    errMap.put("designation"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field"));
                }
                String professionRegoNo = appSvcCgoList.get(i).getProfRegNo();
                String qualification = appSvcCgoList.get(i).getSubSpeciality();
                if(StringUtil.isEmpty(professionRegoNo)){
                    //errMap.put("professionRegoNo"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Regn. No.","field"));
                    if(StringUtil.isEmpty(qualification)){
                        errMap.put("qualification"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Sub-specialty or relevant qualification ","field"));
                    }
                }else if(professionRegoNo.length() > 20){
                    String general_err0041=repLength("Professional Regn. No.","20");
                    errMap.put("professionRegoNo" + i, general_err0041);
                }

                if(!StringUtil.isEmpty(qualification) && qualification.length() > 100){
                    String general_err0041=repLength("Sub-specialty or relevant qualification","100");
                    errMap.put("qualification" + i, general_err0041);
                }
                //to do
                if(StringUtil.isEmpty(idNo)){
                    errMap.put("idNo"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","ID No.","field"));
                }else {
                    if(idNo.length() > 9){
                        String general_err0041=repLength("ID No.","9");
                        errMap.put("idNo" + i, general_err0041);
                    }
                    if(OrganizationConstants.ID_TYPE_FIN.equals(idTyp)){
                        boolean b = SgNoValidator.validateFin(idNo);
                        if(!b){
                            errMap.put("idNo"+i,"RFC_ERR0012");
                        }
                        stringBuilder1.append(idTyp).append(idNo);
                        if(newErr0006 && !StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"NEW_ERR0012");
                            }else {
                                stringList.add( stringBuilder1.toString());
                            }
                        }
                    }
                    if(OrganizationConstants.ID_TYPE_NRIC.equals(idTyp)){
                        boolean b1 = SgNoValidator.validateNric(idNo);
                        if(!b1){
                            errMap.put("idNo"+i,"RFC_ERR0012");
                        }
                        stringBuilder1.append(idTyp).append(idNo);

                        if(newErr0006 && !StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"NEW_ERR0012");
                            }else {
                                stringList.add( stringBuilder1.toString());
                            }
                        }
                    }


                }
                String name = appSvcCgoList.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errMap.put("name"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field"));
                }else {
                    if(name.length()>66){
                        String general_err0041 = repLength("Name","66");
                        errMap.put("name" + i, general_err0041);
                    }
                }

                String mobileNo = appSvcCgoList.get(i).getMobileNo();
                if(StringUtil.isEmpty(mobileNo)){
                    errMap.put("mobileNo"+i, MessageUtil.replaceMessage("GENERAL_ERR0006","Mobile No. ","field"));
                }else if (!StringUtil.isEmpty(mobileNo)) {
                    if(mobileNo.length() > 8){
                        String general_err0041=repLength("Mobile No.","8");
                        errMap.put("mobileNo" + i, general_err0041);
                    }
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo"+i, "GENERAL_ERR0007");
                    }
                }
                String emailAddr = appSvcCgoList.get(i).getEmailAddr();

                if(StringUtil.isEmpty(emailAddr)){
                    errMap.put("emailAddr"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Email Address","field"));
                }else if (!StringUtil.isEmpty(emailAddr)) {
                    if(emailAddr.length() > 66){
                        String general_err0041=repLength("Email Address","66");
                        errMap.put("emailAddr" + i, general_err0041);
                    }
                    if (! ValidationUtils.isEmail(emailAddr)) {
                        errMap.put("emailAddr"+i, "GENERAL_ERR0014");
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
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }

    public static  List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", NewApplicationDelegator.FIRESTOPTION);
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption(OrganizationConstants.ID_TYPE_NRIC, "NRIC");
        SelectOption idType2 = new SelectOption(OrganizationConstants.ID_TYPE_FIN, "FIN");
        idTypeSelectList.add(idType2);
        idTypeSelectList.add(idType1);
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
                Object sessionAttr = ParamUtil.getSessionAttr(request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
                if(sessionAttr==null){
                    ParamUtil.setSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
                }
            }
        }
        return appSubmissionDto;
    }
    //todo change
    public static Map<String,  String> doValidatePo(List<AppSvcPrincipalOfficersDto> poDto,Map<String,AppSvcPersonAndExtDto> licPersonMap, String svcCode) {
        Map<String, String> oneErrorMap = IaisCommonUtils.genNewHashMap();
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        int poIndex=0;
        int dpoIndex=0;
        if(IaisCommonUtils.isEmpty(poDto)){
            return oneErrorMap;
        }
        String errSalutation = MessageUtil.replaceMessage("GENERAL_ERR0006","Salutation","field");
        for (int i=0;i< poDto.size();i++) {
            String psnType = poDto.get(i).getPsnType();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){

                StringBuilder stringBuilder =new StringBuilder();

                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
                    oneErrorMap.put("assignSelect"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Assign a Principal Officer","field"));
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
                    oneErrorMap = doPsnCommValidate(oneErrorMap,idType,idNo,licPerson,licPersonMap,poIdTypeNoKey,svcCode);
                    boolean newErr0006 = StringUtil.isEmpty(oneErrorMap.get(poIdTypeNoKey));
//                    String idTypeNoErr = oneErrorMap.get(poIdTypeNoKey);
//                    if(!StringUtil.isEmpty(idTypeNoErr)){
//                        continue;
//                    }
                    if("-1".equals(idType)||StringUtil.isEmpty(idType)){
                        oneErrorMap.put("idType"+poIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","ID Type","field"));
                    }
                    String errName = MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field");
                    if(StringUtil.isEmpty(name)){
                        oneErrorMap.put("name"+poIndex, errName);
                    }else if (name.length()>66){
                        String general_err0041=repLength("Name","66");
                        oneErrorMap.put("name" + poIndex, general_err0041);
                    }
                    if(StringUtil.isEmpty(salutation)){
                        oneErrorMap.put("salutation"+poIndex, errSalutation);
                    }
                    if(StringUtil.isEmpty(designation)){
                        oneErrorMap.put("designation"+poIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field"));
                    }
                    if(!StringUtil.isEmpty(idNo)){
                        if(idNo.length() > 9){
                            String general_err0041=repLength("ID No.","9");
                            oneErrorMap.put("poNRICFIN" + poIndex, general_err0041);
                        }
                        if(OrganizationConstants.ID_TYPE_FIN.equals(idType)){
                            boolean b = SgNoValidator.validateFin(idNo);
                            if(!b){
                                oneErrorMap.put("poNRICFIN"+poIndex,"RFC_ERR0012");
                            }else {
                                stringBuilder.append(idType).append(idNo);
                                String s = stringBuilder.toString();
                                if(newErr0006 && stringList.contains(s)){
                                    oneErrorMap.put("poNRICFIN"+poIndex,"NEW_ERR0012");
                                }
                            }
                        }
                        if(OrganizationConstants.ID_TYPE_NRIC.equals(idType)){
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if(!b1){
                                oneErrorMap.put("poNRICFIN"+poIndex,"RFC_ERR0012");
                            }else {
                                stringBuilder.append(idType).append(idNo);
                                String s = stringBuilder.toString();
                                if(newErr0006 && stringList.contains(s)){
                                    oneErrorMap.put("poNRICFIN"+poIndex,"NEW_ERR0012");
                                }
                            }
                        }
                    }else {
                        oneErrorMap.put("poNRICFIN"+poIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","ID No. ","field"));
                    }
                    if(!StringUtil.isEmpty(mobileNo)){
                        if(mobileNo.length() > 8){
                            String general_err0041=repLength("Mobile No.","8");
                            oneErrorMap.put("mobileNo" + poIndex, general_err0041);
                        }
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo"+poIndex, "GENERAL_ERR0007");
                        }
                    }else {
                        oneErrorMap.put("mobileNo"+poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","Mobile No. ","field"));
                    }
                    if(!StringUtil.isEmpty(emailAddr)) {
                        if (!  ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("emailAddr"+poIndex, "GENERAL_ERR0014");
                        }else if(emailAddr.length()>66){
                            String general_err0041=repLength("Email Address","66");
                            oneErrorMap.put("emailAddr" + poIndex, general_err0041);
                        }
                    }else {
                        oneErrorMap.put("emailAddr"+poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","Email Address ","field"));
                    }
                    if(!StringUtil.isEmpty(officeTelNo)) {
                        if(officeTelNo.length() > 8){
                            String general_err0041=repLength("Office Telephone No.","8");
                            oneErrorMap.put("officeTelNo" + poIndex, general_err0041);
                        }
                        if (!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                            oneErrorMap.put("officeTelNo"+poIndex, "GENERAL_ERR0015");
                        }
                    }else {
                        oneErrorMap.put("officeTelNo"+poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","Office Telephone No.","field"));
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
                    oneErrorMap.put("modeOfMedAlert"+dpoIndex,"GENERAL_ERR0006");
                }*/

                boolean licPerson = poDto.get(i).isLicPerson();
                String dpoIdTypeNoKey = "dpoIdTypeNo"+dpoIndex;
                oneErrorMap = doPsnCommValidate(oneErrorMap,idType,idNo,licPerson,licPersonMap,dpoIdTypeNoKey,svcCode);
                boolean newErr0006 = StringUtil.isEmpty(oneErrorMap.get(dpoIdTypeNoKey));
//                if(!StringUtil.isEmpty(idTypeNoErr)){
//                    continue;
//                }
                String assignSelect = poDto.get(i).getAssignSelect();
                if(StringUtil.isEmpty(assignSelect)||"-1".equals(assignSelect)){
                    oneErrorMap.put("deputyAssignSelect"+dpoIndex,MessageUtil.getMessageDesc("NEW_ERR0018"));
                }else {
                    if(StringUtil.isEmpty(designation)||"-1".equals(designation)){
                        oneErrorMap.put("deputyDesignation"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field"));
                    }
                    if(StringUtil.isEmpty(salutation)||"-1".equals(salutation)){
                        oneErrorMap.put("deputySalutation"+dpoIndex,errSalutation);
                    }

                    if(StringUtil.isEmpty(idType)||"-1".equals(idType)){
                        oneErrorMap.put("deputyIdType"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","ID Type","field"));
                    }
                    if(StringUtil.isEmpty(name)){
                        oneErrorMap.put("deputyName"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field"));
                    }else if(name.length()>66){
                        String general_err0041=repLength("Name","66");
                        oneErrorMap.put("deputyName" + dpoIndex, general_err0041);
                    }
                    if(StringUtil.isEmpty(officeTelNo)){
                        oneErrorMap.put("deputyofficeTelNo"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Office Telephone No.","field"));
                    }else {
                        if(officeTelNo.length() > 8){
                            String general_err0041=repLength("Office Telephone No.","8");
                            oneErrorMap.put("deputyofficeTelNo" + dpoIndex, general_err0041);
                        }
                        if(!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)){
                            oneErrorMap.put("deputyofficeTelNo"+dpoIndex,"GENERAL_ERR0015");
                        }
                    }
                    if(StringUtil.isEmpty(idNo)){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","ID No.","field"));
                    }else{
                        if(idNo.length() > 9){
                            String general_err0041=repLength("ID No.","9");
                            oneErrorMap.put("deputyIdNo" + dpoIndex, general_err0041);
                        }
                        if(OrganizationConstants.ID_TYPE_FIN.equals(idType)){
                            boolean b = SgNoValidator.validateFin(idNo);
                            if(!b){
                                oneErrorMap.put("deputyIdNo"+dpoIndex,"RFC_ERR0012");
                            }else {
                                stringBuilder.append(idType).append(idNo);
                                String s = stringBuilder.toString();
                                if(newErr0006 && stringList.contains(s)){
                                    oneErrorMap.put("deputyIdNo"+dpoIndex,"NEW_ERR0012");
                                }
                            }
                        }
                        if(OrganizationConstants.ID_TYPE_NRIC.equals(idType)){
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if(!b1){
                                oneErrorMap.put("deputyIdNo"+dpoIndex,"RFC_ERR0012");
                            }else {
                                stringBuilder.append(idType).append(idNo);
                                String s = stringBuilder.toString();
                                if(newErr0006 && stringList.contains(s)){
                                    oneErrorMap.put("deputyIdNo"+dpoIndex,"NEW_ERR0012");
                                }
                            }
                        }
                    }


                    if(StringUtil.isEmpty(mobileNo)){
                        oneErrorMap.put("deputyMobileNo"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Mobile No.","field"));
                    }
                    else {
                        if(mobileNo.length() > 8){
                            String general_err0041=repLength("Mobile No.","8");
                            oneErrorMap.put("deputyMobileNo" + dpoIndex, general_err0041);
                        }
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("deputyMobileNo"+dpoIndex, "GENERAL_ERR0007");
                        }
                    }
                    if(StringUtil.isEmpty(emailAddr)){
                        oneErrorMap.put("deputyEmailAddr"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Email Address ","field"));
                    }else {
                        if(emailAddr.length() > 66){
                            String general_err0041=repLength("Email Address","66");
                            oneErrorMap.put("deputyEmailAddr" + dpoIndex, general_err0041);
                        }
                        if (!ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("deputyEmailAddr"+dpoIndex, "GENERAL_ERR0014");
                        }else if(emailAddr.length()>66){

                        }
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
        WebValidationHelper.saveAuditTrailForNoUseResult(oneErrorMap);
        return oneErrorMap;
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption, String checkedVal){
        //sort dropdown
        List<SelectOption> sortSelOptionList = IaisCommonUtils.genNewArrayList();

        List<SelectOption> pleaseSelectSp = IaisCommonUtils.genNewArrayList();
        List<SelectOption> newPremisesSp = IaisCommonUtils.genNewArrayList();
        List<SelectOption> newPsnSp = IaisCommonUtils.genNewArrayList();
        List<SelectOption> otherSp =  IaisCommonUtils.genNewArrayList();
        for(SelectOption sp:selectOptionList){
            String val = sp.getValue();
            if(StringUtil.isEmpty(sp.getValue()) || "-1".equals(val)){
                pleaseSelectSp.add(sp);
            }else if(NewApplicationConstant.NEW_PREMISES.equals(val)){
                newPremisesSp.add(sp);
            }else if(NewApplicationConstant.NEW_PSN.equals(val)){
                newPsnSp.add(sp);
            }else if("other".equals(val)){
                otherSp.add(sp);
            }
        }
        sortSelOptionList.addAll(pleaseSelectSp);
        sortSelOptionList.addAll(newPremisesSp);
        sortSelOptionList.addAll(newPsnSp);

        List<SelectOption> needSortList = IaisCommonUtils.genNewArrayList();
        for(SelectOption sp:selectOptionList){
            String val = sp.getValue();
            boolean pleaseSelectVal = StringUtil.isEmpty(val) || "-1".equals(val);
            boolean newPremisesVal = NewApplicationConstant.NEW_PREMISES.equals(val);
            boolean newPsnVal = NewApplicationConstant.NEW_PSN.equals(val);
            boolean otherVal = "other".equals(val);
            if(pleaseSelectVal || newPremisesVal || newPsnVal || otherVal){
                continue;
            }
            needSortList.add(sp);
        }
        doSortSelOption(needSortList);
        sortSelOptionList.addAll(needSortList);
        sortSelOptionList.addAll(otherSp);

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
        for(SelectOption sp:sortSelOptionList){
            if(!StringUtil.isEmpty(checkedVal)){
                if(checkedVal.equals(sp.getValue())){
//                    sBuffer.append("<option selected=\"selected\" value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
                    sBuffer.append("<option selected=\"selected\" value=\"")
                            .append(sp.getValue())
                            .append("\">")
                            .append(sp.getText())
                            .append("</option>");
                }else{
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }
            }else{
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
            }
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        if(!StringUtil.isEmpty(checkedVal)){
            String text = getTextByValue(sortSelOptionList,checkedVal);
            sBuffer.append("<span selected=\"selected\" class=\"current\">").append(text).append("</span>");
        }else{
            if(!StringUtil.isEmpty(firestOption)){
                sBuffer.append("<span class=\"current\">").append(firestOption).append("</span>");
            }else{
                sBuffer.append("<span class=\"current\">").append(sortSelOptionList.get(0).getText()).append("</span>");
            }
        }
        sBuffer.append("<ul class=\"list\">");

        if(!StringUtil.isEmpty(checkedVal)){
            for(SelectOption kv:sortSelOptionList){
                if(checkedVal.equals(kv.getValue())){
                    sBuffer.append("<li selected=\"selected\" data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }else if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"\" class=\"option selected\">").append(firestOption).append("</li>");
            for(SelectOption kv:sortSelOptionList){
                sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
            }
        }else{
            for(int i = 0;i<sortSelOptionList.size();i++){
                SelectOption kv = sortSelOptionList.get(i);
                if(i == 0){
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }
        sBuffer.append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }

    public static boolean isGetDataFromPage(AppSubmissionDto appSubmissionDto, String currentType, String isClickEdit, boolean isRfi){
        if(appSubmissionDto == null){
            return true;
        }
        boolean isNewApp =  !isRfi&&ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
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
        recursingSvcScope(hcsaSvcSubtypeOrSubsumedDtos, map);
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
        recursingSvcScope(hcsaSvcSubtypeOrSubsumedDtos, map);
        if(appSvcRelatedInfoDto == null){
            return;
        }
        setSvcScopeInfo(appGrpPremisesDtos,appSvcRelatedInfoDto,map);
    }




    public static AppGrpPremisesDto setWrkTime(AppGrpPremisesDto appGrpPremisesDto){
        if(appGrpPremisesDto == null){
            return appGrpPremisesDto;
        }
        List<OperationHoursReloadDto> weeklyDtos = appGrpPremisesDto.getWeeklyDtoList();
        if(!IaisCommonUtils.isEmpty(weeklyDtos)){
            for(OperationHoursReloadDto weeklyDto:weeklyDtos){
                setReloadTime(weeklyDto);
            }
        }

        List<OperationHoursReloadDto> phDtos = appGrpPremisesDto.getPhDtoList();
        if(!IaisCommonUtils.isEmpty(phDtos)){
            for(OperationHoursReloadDto phDto:phDtos){
                setReloadTime(phDto);
            }
        }

        List<AppPremEventPeriodDto> eventDtos = appGrpPremisesDto.getEventDtoList();
        if(!IaisCommonUtils.isEmpty(eventDtos)){
            for(AppPremEventPeriodDto eventDto:eventDtos){
                Date start = eventDto.getStartDate();
                if(start != null){
                    eventDto.setStartDateStr(Formatter.formatDate(start));
                }
                Date end = eventDto.getEndDate();
                if(end != null){
                    eventDto.setEndDateStr(Formatter.formatDate(end));
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


    public static Map<String,String> doValidateMedAlertPsn(List<AppSvcPrincipalOfficersDto> medAlertPsnDtos,Map<String,AppSvcPersonAndExtDto> licPersonMap, String svcCode){
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isEmpty(medAlertPsnDtos)){
            return errMap;
        }
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        for(int i=0;i<medAlertPsnDtos.size();i++ ){
            String assignSelect = medAlertPsnDtos.get(i).getAssignSelect();
            if("-1".equals(assignSelect)||StringUtil.isEmpty(assignSelect)){
                errMap.put("assignSelect"+i, MessageUtil.replaceMessage("GENERAL_ERR0006","Assign a MedAlert Person","field"));
            }else {
                String idTyp = medAlertPsnDtos.get(i).getIdType();
                String idNo = medAlertPsnDtos.get(i).getIdNo();
                boolean licPerson = medAlertPsnDtos.get(i).isLicPerson();
                String idTypeNoKey = "idTypeNo"+i;
                errMap = doPsnCommValidate(errMap,idTyp,idNo,licPerson,licPersonMap,idTypeNoKey,svcCode);
                boolean newErr0006 = StringUtil.isEmpty(errMap.get(idTypeNoKey));
//                String idTypeNoErr = errMap.get(idTypeNoKey);
//                if(!StringUtil.isEmpty(idTypeNoErr)){
//                    continue;
//                }
                StringBuilder stringBuilder1=new StringBuilder();
                if("-1".equals(idTyp)||StringUtil.isEmpty(idTyp)){
                    errMap.put("idTyp"+i, MessageUtil.replaceMessage("GENERAL_ERR0006","ID Type","field"));
                }
                String salutation = medAlertPsnDtos.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    errMap.put("salutation"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Salutation","field"));
                }
                //to do
                if(StringUtil.isEmpty(idNo)){
                    errMap.put("idNo"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","ID No.","field"));
                }else {
                    if(idNo.length()>9){
                        String general_err0041=repLength("ID No.","9");
                        errMap.put("idNo" + i, general_err0041);
                    }
                    if(OrganizationConstants.ID_TYPE_FIN.equals(idTyp)){
                        boolean b = SgNoValidator.validateFin(idNo);
                        if(!b){
                            errMap.put("idNo"+i,"RFC_ERR0012");
                        }
                        stringBuilder1.append(idTyp).append(idNo);
                        if(newErr0006 && !StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"NEW_ERR0012");
                            }
                        }
                    }
                    if(OrganizationConstants.ID_TYPE_NRIC.equals(idTyp)){
                        boolean b1 = SgNoValidator.validateNric(idNo);
                        if(!b1){
                            errMap.put("idNo"+i,"RFC_ERR0012");
                        }
                        stringBuilder1.append(idTyp).append(idNo);
                        if(newErr0006 && !StringUtil.isEmpty(stringBuilder1.toString())){
                            if(stringList.contains(stringBuilder1.toString())){
                                errMap.put("idNo"+i,"NEW_ERR0012");
                            }
                        }
                    }
                }

                String name = medAlertPsnDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errMap.put("name"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field"));
                }else {
                    if(name.length()>66){
                        String general_err0041=repLength("Name","66");
                        errMap.put("name" + i, general_err0041);
                    }
                }

                String mobileNo = medAlertPsnDtos.get(i).getMobileNo();
                if(StringUtil.isEmpty(mobileNo)){
                    errMap.put("mobileNo"+i, MessageUtil.replaceMessage("GENERAL_ERR0006","Mobile No. ","field"));
                }else if (!StringUtil.isEmpty(mobileNo)) {
                    if(mobileNo.length()>8){
                        String general_err0041=repLength("Mobile No.","8");
                        errMap.put("mobileNo" + i, general_err0041);
                    }
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo"+i, "GENERAL_ERR0007");
                    }
                }
                String emailAddr = medAlertPsnDtos.get(i).getEmailAddr();

                if(StringUtil.isEmpty(emailAddr)){
                    errMap.put("emailAddr"+i, MessageUtil.replaceMessage("GENERAL_ERR0006","Email Address","field"));
                }else if (!StringUtil.isEmpty(emailAddr)) {
                    if(mobileNo.length()>66){
                        String general_err0041=repLength("Email Address","66");
                        errMap.put("emailAddr" + i, general_err0041);
                    }
                    if (! ValidationUtils.isEmail(emailAddr)) {
                        errMap.put("emailAddr"+i, "GENERAL_ERR0014");
                    }else if(emailAddr.length()>66) {

                    }
                }

                if(!StringUtil.isEmpty(stringBuilder1.toString())){
                    if(!stringList.contains(stringBuilder1.toString())){
                        stringList.add( stringBuilder1.toString());
                    }
                }


            }

        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }

    public static List<AppSvcPrincipalOfficersDto> transferCgoToPsnDtoList(List<AppSvcCgoDto> appSvcCgoDtos){
        List<AppSvcPrincipalOfficersDto> psnDtos = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(appSvcCgoDtos)){
            return psnDtos;
        }
        for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
            AppSvcPrincipalOfficersDto psnDto = MiscUtil.transferEntityDto(appSvcCgoDto,AppSvcPrincipalOfficersDto.class);
            psnDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            psnDtos.add(psnDto);
        }
        return psnDtos;
    }

    public static Map<String,AppSvcPersonAndExtDto> setPsnIntoSelMap(Map<String,AppSvcPersonAndExtDto> personMap, List<AppSvcPrincipalOfficersDto> psnDtos, String svcCode){
        if(IaisCommonUtils.isEmpty(psnDtos)){
            return personMap;
        }
        for(AppSvcPrincipalOfficersDto psnDto:psnDtos){
            boolean partValidate = psnDoPartValidate(psnDto.getIdType(),psnDto.getIdNo(),psnDto.getName());
            if(!partValidate){
                continue;
            }
            String personMapKey = psnDto.getIdType()+","+psnDto.getIdNo();
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personMapKey);
            List<AppSvcPersonExtDto> appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
            AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto,svcCode,true);
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
                AppSvcPersonAndExtDto newPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonDto.class);
                AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonExtDto.class);
                appSvcPersonExtDto.setServiceCode(svcCode);
                appSvcPersonExtDto.setAssignSelect(psnDto.getAssignSelect());
                appSvcPersonExtDtos.add(appSvcPersonExtDto);
                newPersonAndExtDto.setPersonDto(appSvcPersonDto);
                newPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                newPersonAndExtDto.setLicPerson(psnDto.isLicPerson());
                personMap.put(personMapKey,newPersonAndExtDto);
            }else{
                //dropdown psn
                String licPerson = String.valueOf(person.isLicPerson());
                //dto psn
                String licPsn = String.valueOf(psnDto.isLicPerson());
                if(!licPerson.equals(licPsn)){
                    log.info(StringUtil.changeForLog("personMapKey:"+personMapKey+",dropdown psn:"+licPerson+",,,dto psn:"+licPsn));
                    continue;
                }
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
                            if(!specialityOpts.contains(sp) && !"other".equals(sp.getValue())){
                                specialityOpts.add(sp);
                            }
                        }
                        String specialityVal = psnDto.getSpeciality();
                        if(!StringUtil.isEmpty(specialityVal)){
                            SelectOption sp = getSpecialtyByValue(specialityVal);
                            if(!specialityOpts.contains(sp)){
                                specialityOpts.add(sp);
                            }
                        }
                    }else{
                        log.info(StringUtil.changeForLog("person spcOpts is empty"));
                    }
                    SelectOption sp = new SelectOption("other", "Others");
                    boolean flag=false;
                    for(SelectOption selectOption : specialityOpts){
                        String value = selectOption.getValue();
                        if("other".equals(value)){
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        specialityOpts.add(sp);
                    }
                    person.setSpcOptList(specialityOpts);
                    String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, person.getSpeciality());
                    person.setSpecialityHtml(specialtySelectStr);
                    psnDto.setSpcOptList(specialityOpts);
                    psnDto.setSpecialityHtml(specialtySelectStr);
                    psnDto.setNeedSpcOptList(true);
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())){
                    person.setDesignation(psnDto.getDesignation());
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                    person.setDesignation(psnDto.getDesignation());
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())){

                }
                //person.setLicPerson(true);
                //for person dtos
                //psnDto.setLicPerson(person.isLicPerson());
                psnDto.setAssignSelect(getPersonKey(psnDto.getIdType(),psnDto.getIdNo()));

                AppSvcPersonAndExtDto newPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(person,AppSvcPersonDto.class);
                AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(person,AppSvcPersonExtDto.class);
                appSvcPersonExtDto.setServiceCode(svcCode);
                appSvcPersonExtDto.setAssignSelect(person.getAssignSelect());
                appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
                if(IaisCommonUtils.isEmpty(appSvcPersonExtDtos)){
                    appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                }
                appSvcPersonExtDtos.add(appSvcPersonExtDto);
                newPersonAndExtDto.setPersonDto(appSvcPersonDto);
                newPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                newPersonAndExtDto.setLicPerson(person.isLicPerson());
                personMap.put(personMapKey,newPersonAndExtDto);
            }
        }
        return personMap;
    }

    public static Map<String,AppSvcPersonAndExtDto> initSetPsnIntoSelMap(Map<String,AppSvcPersonAndExtDto> personMap, List<AppSvcPrincipalOfficersDto> psnDtos, String svcCode){
        if(IaisCommonUtils.isEmpty(psnDtos)){
            return personMap;
        }
        for(AppSvcPrincipalOfficersDto psnDto:psnDtos){
            if(StringUtil.isEmpty(psnDto.getIdNo()) || StringUtil.isEmpty(psnDto.getIdType())){
                continue;
            }
            String personMapKey = NewApplicationHelper.getPersonKey(psnDto.getIdType(),psnDto.getIdNo());
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personMapKey);
            List<AppSvcPersonExtDto> appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
            AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto,svcCode,true);
            Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String speciality = psnDto.getSpeciality();
            if(person == null){
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())){
                    psnDto.setNeedSpcOptList(true);
                    List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode,true);
                    boolean canMatch = false;
                    for(SelectOption sp:specialityOpts){
                        if(sp.getValue().equals(speciality)){
                            canMatch = true;
                            break;
                        }
                    }
                    if(!canMatch){
                        log.info(StringUtil.changeForLog("can not match speciality:"+speciality+",when svcCode:"+svcCode));
                        specialityOpts = getAllSpecialtySelList();
                    }
                    psnDto.setSpcOptList(specialityOpts);
                    String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psnDto.getSpeciality());
                    psnDto.setSpecialityHtml(specialtySelectStr);
                }
                psnDto.setAssignSelect(getPersonKey(psnDto.getIdType(),psnDto.getIdNo()));
                AppSvcPersonAndExtDto newPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonDto.class);
                AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonExtDto.class);
                appSvcPersonExtDto.setServiceCode(svcCode);
                appSvcPersonExtDto.setAssignSelect(psnDto.getAssignSelect());
                appSvcPersonExtDtos.add(appSvcPersonExtDto);
                newPersonAndExtDto.setPersonDto(appSvcPersonDto);
                newPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                newPersonAndExtDto.setLicPerson(psnDto.isLicPerson());
                personMap.put(personMapKey,newPersonAndExtDto);
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
                            if(!specialityOpts.contains(sp) && !"other".equals(sp.getValue())){
                                specialityOpts.add(sp);
                            }
                        }
                        String specialityVal = psnDto.getSpeciality();
                        if(!StringUtil.isEmpty(specialityVal)){
                            SelectOption sp = getSpecialtyByValue(specialityVal);
                            if(!specialityOpts.contains(sp)){
                                specialityOpts.add(sp);
                            }
                        }
                    }else{
                        log.info(StringUtil.changeForLog("person spcOpts is empty"));
                    }
                    SelectOption otherSp = new SelectOption("other", "Others");
                    boolean flag=false;
                    for(SelectOption selectOption : specialityOpts){
                        String value = selectOption.getValue();
                        if("other".equals(value)){
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        specialityOpts.add(otherSp);
                    }
                    person.setSpcOptList(specialityOpts);

                    boolean canMatch = false;
                    for(SelectOption sp:specialityOpts){
                        if(sp.getValue().equals(speciality)){
                            canMatch = true;
                            break;
                        }
                    }
                    if(!canMatch){
                        log.info(StringUtil.changeForLog("can not match speciality:"+speciality+",when svcCode:"+svcCode));
                        specialityOpts = getAllSpecialtySelList();
                    }
                    String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, person.getSpeciality());
                    person.setSpecialityHtml(specialtySelectStr);
                    psnDto.setSpcOptList(specialityOpts);
                    psnDto.setSpecialityHtml(specialtySelectStr);
                    psnDto.setNeedSpcOptList(true);
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())){
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())){

                }
                //person.setLicPerson(true);
                //for person dtos
                //psnDto.setLicPerson(person.isLicPerson());
                psnDto.setAssignSelect(getPersonKey(psnDto.getIdType(),psnDto.getIdNo()));
                psnDto.setLicPerson(person.isLicPerson());

                AppSvcPersonAndExtDto newPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(person,AppSvcPersonDto.class);
                AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(person,AppSvcPersonExtDto.class);
                appSvcPersonExtDto.setServiceCode(svcCode);
                appSvcPersonExtDto.setAssignSelect(person.getAssignSelect());
                appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
                if(IaisCommonUtils.isEmpty(appSvcPersonExtDtos)){
                    appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                }
                appSvcPersonExtDtos.add(appSvcPersonExtDto);
                newPersonAndExtDto.setPersonDto(appSvcPersonDto);
                newPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                newPersonAndExtDto.setLicPerson(person.isLicPerson());
                personMap.put(personMapKey,newPersonAndExtDto);
            }
        }
        return personMap;
    }

    @Deprecated
    public static Map<String,AppSvcPrincipalOfficersDto> getLicPsnIntoSelMap(HttpServletRequest request, List<PersonnelListQueryDto> licPsnDtos) {
        Map<String,AppSvcPrincipalOfficersDto> personMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(licPsnDtos)) {
            for (PersonnelListQueryDto psnDto : licPsnDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(psnDto.getSvcName());
                if(hcsaServiceDto == null){
                    log.info(StringUtil.changeForLog("service name:"+psnDto.getSvcName()+" can not get HcsaServiceDto ..."));
                    continue;
                }
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
                    appSvcPrincipalOfficersDto.setNeedDisabled(true);
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
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())) {
                        person.setOfficeTelNo(psnDto.getOfficeTelNo());
                    }
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                        person.setOfficeTelNo(psnDto.getOfficeTelNo());
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())) {

                    }

                    person.setLicPerson(true);
                    personMap.put(personMapKey, person);
                }
            }
        }
        return personMap;
    }

    public static Map<String,AppSvcPersonAndExtDto> getLicPsnIntoSelMap(List<FeUserDto> feUserDtos,List<PersonnelListQueryDto> licPsnDtos,Map<String, AppSvcPersonAndExtDto> personMap) {
        //user account
        if(!IaisCommonUtils.isEmpty(feUserDtos)){
            for(FeUserDto feUserDto:feUserDtos){
                String idType = feUserDto.getIdType();
                String idNo = feUserDto.getIdNumber();
                if(StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)){
                    continue;
                }
                AppSvcPersonAndExtDto appSvcPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = new AppSvcPersonDto();
//                    appSvcPersonDto.setCurPersonelId("");
                appSvcPersonDto.setSalutation(feUserDto.getSalutation());
                appSvcPersonDto.setName(feUserDto.getDisplayName());
                appSvcPersonDto.setIdType(idType);
                appSvcPersonDto.setIdNo(idNo);
                appSvcPersonDto.setDesignation(feUserDto.getDesignation());
                appSvcPersonDto.setMobileNo(feUserDto.getMobileNo());
                appSvcPersonDto.setEmailAddr(feUserDto.getEmail());
                appSvcPersonDto.setOfficeTelNo(feUserDto.getOfficeTelNo());
                appSvcPersonDto.setCurPersonelId(null);
                appSvcPersonAndExtDto.setPersonDto(appSvcPersonDto);
                appSvcPersonAndExtDto.setLicPerson(true);
                appSvcPersonAndExtDto.setLoadingType(ApplicationConsts.PERSON_LOADING_TYPE_BLUR);
                personMap.put(NewApplicationHelper.getPersonKey(idType,idNo),appSvcPersonAndExtDto);
            }
        }

        if (!IaisCommonUtils.isEmpty(licPsnDtos)) {
            Map<String,String> specialtyAttr = getSpecialtyAttr();
            for (PersonnelListQueryDto psnDto : licPsnDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(psnDto.getSvcName());
                if(hcsaServiceDto == null){
                    log.info(StringUtil.changeForLog("service name:"+psnDto.getSvcName()+" can not get HcsaServiceDto ..."));
                    continue;
                }
                String svcCode = hcsaServiceDto.getSvcCode();
                String personMapKey = getPersonKey(psnDto.getIdType(),psnDto.getIdNo());
                AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personMapKey);
                String speciality = psnDto.getSpeciality();
                if (appSvcPersonAndExtDto == null) {
                    //cgo speciality
                    if(!StringUtil.isEmpty(speciality)){
                        psnDto.setNeedSpcOptList(true);
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, true);
                        psnDto.setSpcOptList(specialityOpts);
                        String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psnDto.getSpeciality());
                        psnDto.setSpecialityHtml(specialtySelectStr);
                    }
                    appSvcPersonAndExtDto = new AppSvcPersonAndExtDto();
                    AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonDto.class);
                    List<AppSvcPersonExtDto> appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                    AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonExtDto.class);
                    AppSvcPrincipalOfficersDto person = MiscUtil.transferEntityDto(psnDto,AppSvcPrincipalOfficersDto.class);
                    AppPsnEditDto appPsnEditDto = NewApplicationHelper.setNeedEditField(person);
                    appSvcPersonExtDto.setPsnEditDto(appPsnEditDto);
                    appSvcPersonExtDto.setServiceCode(svcCode);
                    appSvcPersonExtDtos.add(appSvcPersonExtDto);
                    appSvcPersonAndExtDto.setPersonDto(appSvcPersonDto);
                    appSvcPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                    appSvcPersonAndExtDto.setLicPerson(true);
                    personMap.put(personMapKey, appSvcPersonAndExtDto);
                }else{
                    List<AppSvcPersonExtDto> appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
                    if(IaisCommonUtils.isEmpty(appSvcPersonExtDtos)){
                        appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                    }

//                    AppSvcPersonExtDto currSvcPsnExtDto = getPsnExtDtoBySvcCode(appSvcPersonExtDtos,svcCode);
                    AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto,svcCode,true);

//                    person.setDesignation(psnDto.getDesignation());

                    person.setProfessionType(psnDto.getProfessionType());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpeciality(psnDto.getSpeciality());
                    person.setSpecialityOther(psnDto.getSpecialityOther());
                    person.setSubSpeciality(psnDto.getSubSpeciality());
                    //cgo speciality
                    if(!StringUtil.isEmpty(speciality)){
                        person.setNeedSpcOptList(true);
                        List<SelectOption> spcOpts = psnDto.getSpcOptList();
                        if(IaisCommonUtils.isEmpty(spcOpts)){
                            spcOpts = genSpecialtySelectList(svcCode,true);
                        }
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
                    }
                    AppSvcPersonExtDto currSvcPsnExtDto = MiscUtil.transferEntityDto(person,AppSvcPersonExtDto.class);
                    AppPsnEditDto appPsnEditDto = setNeedEditField(person);
                    currSvcPsnExtDto.setPsnEditDto(appPsnEditDto);
                    currSvcPsnExtDto.setServiceCode(svcCode);
                    appSvcPersonExtDtos.add(currSvcPsnExtDto);
                    AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto,AppSvcPersonDto.class);
                    appSvcPersonAndExtDto.setPersonDto(appSvcPersonDto);
                    appSvcPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                    appSvcPersonAndExtDto.setLicPerson(true);
                    personMap.put(personMapKey, appSvcPersonAndExtDto);
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
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl2);
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
                specialtySelectList.add(ssl5);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);

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
        specialtySelectList.add(ssl4);
        specialtySelectList.add(ssl3);
        specialtySelectList.add(ssl5);
        specialtySelectList.add(ssl2);
        specialtySelectList.add(ssl6);
        return specialtySelectList;
    }

    public static SelectOption getSpecialtyByValue(String specialtyVal){
        SelectOption result = new SelectOption(specialtyVal,specialtyVal);
        if(!StringUtil.isEmpty(specialtyVal)){
            List<SelectOption> allSpecialty = getAllSpecialtySelList();
            for(SelectOption sp:allSpecialty){
                if(specialtyVal.equals(sp.getValue())){
                    result = sp;
                    break;
                }
            }
        }
        return result;
    }

    public static AppSubmissionDto syncPsnData(AppSubmissionDto appSubmissionDto, Map<String,AppSvcPersonAndExtDto> personMap ){
        if(appSubmissionDto == null || personMap == null){
           return appSubmissionDto;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                syncCgoDto(appSvcRelatedInfoDto.getAppSvcCgoDtoList(), personMap, svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), personMap,svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), personMap,svcCode);
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

        List<SelectOption> personList = IaisCommonUtils.genNewArrayList();
        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        personMap.forEach((k,v)->{
            AppSvcPersonDto personDto = v.getPersonDto();
            SelectOption sp = new SelectOption(k,personDto.getName()+", "+personDto.getIdNo()+" ("+MasterCodeUtil.getCodeDesc(personDto.getIdType())+")");
            personList.add(sp);
        });
        //sort
        if(personList != null){
            personList.sort((h1,h2)->h1.getText().compareTo(h2.getText()));
            psnSelectList.addAll(personList);
        }
        return psnSelectList;
    }


    public static void setPreviewPo(AppSvcRelatedInfoDto appSvcRelatedInfoDto,HttpServletRequest request){
        List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        if(appSvcRelatedInfoDto != null){
            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList()){
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                    principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                    deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        ParamUtil.setRequestAttr(request, "ReloadPrincipalOfficers", principalOfficersDtos);
        ParamUtil.setRequestAttr(request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);

    }

    public static void setTimeList(HttpServletRequest request){
        List<SelectOption> timeHourList = getTimeHourList();
        List<SelectOption> timeMinList = getTimeMinList();
        ParamUtil.setRequestAttr(request, "premiseHours", timeHourList);
        ParamUtil.setRequestAttr(request, "premiseMinute", timeMinList);

    }

    public static List<SelectOption> getTimeHourList(){
        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 24;i++){
            timeHourList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        return timeHourList;
    }

    public static List<SelectOption> getTimeMinList(){
        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 60;i++){
            timeMinList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        return timeMinList;
    }

    public static void setPremSelect(HttpServletRequest request,Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.APPSUBMISSIONDTO);
        String appType = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION;
        if(appSubmissionDto != null){
            appType = appSubmissionDto.getAppType();
        }
        List<SelectOption> premisesSelect = getPremisesSel(appType);
        List<SelectOption> conveyancePremSel = getPremisesSel(appType);
        List<SelectOption> offSitePremSel = getPremisesSel(appType);
        List<SelectOption> existingOnsitePrem = IaisCommonUtils.genNewArrayList();
        List<SelectOption> existingConvPrem = IaisCommonUtils.genNewArrayList();
        List<SelectOption> existingOffsitePrem = IaisCommonUtils.genNewArrayList();
        if (licAppGrpPremisesDtoMap != null && !licAppGrpPremisesDtoMap.isEmpty()) {
            for (AppGrpPremisesDto item : licAppGrpPremisesDtoMap.values()) {
                SelectOption sp= new SelectOption(item.getPremisesSelect(), item.getAddress());
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())) {
                    existingOnsitePrem.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())){
                    existingConvPrem.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(item.getPremisesType())){
                    existingOffsitePrem.add(sp);
                }
            }
        }
        //sort
        doSortSelOption(existingOnsitePrem);
        doSortSelOption(existingConvPrem);
        doSortSelOption(existingOffsitePrem);
        premisesSelect.addAll(existingOnsitePrem);
        conveyancePremSel.addAll(existingConvPrem);
        offSitePremSel.addAll(existingOffsitePrem);
        ParamUtil.setSessionAttr(request, "premisesSelect", (Serializable) premisesSelect);
        ParamUtil.setSessionAttr(request, "conveyancePremSel", (Serializable) conveyancePremSel);
        ParamUtil.setSessionAttr(request, "offSitePremSel", (Serializable) offSitePremSel);
    }

    public static void doSortSelOption(List<SelectOption> selectOptions){
        Collections.sort(selectOptions,(s1,s2)->(s1.getText().compareTo(s2.getText())));
    }

    public static void setPremAddressSelect(HttpServletRequest request){
        List<SelectOption> addrTypeOpt = new ArrayList<>();
        /*SelectOption addrTypeSp = new SelectOption("",NewApplicationDelegator.FIRESTOPTION);
        addrTypeOpt.add(addrTypeSp);*/
        addrTypeOpt.addAll(MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE));
        doSortSelOption(addrTypeOpt);
        ParamUtil.setRequestAttr(request,"addressType",addrTypeOpt);
    }

    /**
     * for preview page
     */
    public static void setDocInfo(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<AppSvcDocDto> appSvcDocDtos, List<HcsaSvcDocConfigDto> primaryDocConfig, List<HcsaSvcDocConfigDto> svcDocConfig){
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                if(!IaisCommonUtils.isEmpty(primaryDocConfig)){
                    for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:primaryDocConfig){
                        String docConfigId = appGrpPrimaryDocDto.getSvcComDocId();
                        if(!StringUtil.isEmpty(docConfigId) && docConfigId.equals(hcsaSvcDocConfigDto.getId())){
                            appGrpPrimaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                            break;
                        }
                    }
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                if(!IaisCommonUtils.isEmpty(svcDocConfig)){
                    for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:svcDocConfig){
                        String docConfigId = appSvcDocDto.getSvcDocId();
                        if(!StringUtil.isEmpty(docConfigId) && docConfigId.equals(hcsaSvcDocConfigDto.getId())){
                            appSvcDocDto.setUpFileName(hcsaSvcDocConfigDto.getDocTitle());
                            if(AppConsts.NO.equals(hcsaSvcDocConfigDto.getDupForPrem())){
                                appSvcDocDto.setPremisesVal("");
                                appSvcDocDto.setPremisesType("");
                            }
                            break;
                        }
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

    public static String getPremHci(AppGrpPremisesDto appGrpPremisesDto){
        String premHci = "";
        String premKey = getPremKey(appGrpPremisesDto);
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = appGrpPremisesDto.getHciName()+ premKey;
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = appGrpPremisesDto.getConveyanceVehicleNo() + premKey;
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = premKey;

        }
        return premHci;

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
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.LICPERSONSELECTMAP);
        String svcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        if (personMap != null) {
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personKey);
            AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto,svcCode,true);
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

    public static String getPhName(List<SelectOption> phDtos, String dateStr){
        String result = "";
        if(IaisCommonUtils.isEmpty(phDtos) || StringUtil.isEmpty(dateStr)){
            return result;
        }
        for(SelectOption publicHolidayDto : phDtos){
            if(dateStr.equals(publicHolidayDto.getValue())){
                result = publicHolidayDto.getText();
                break;
            }
        }
        return result;
    }

    public static List<AppSvcRelatedInfoDto> addOtherSvcInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,List<HcsaServiceDto> hcsaServiceDtos,boolean needSort){
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<HcsaServiceDto> otherSvcDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                //
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                    String svcCode = hcsaServiceDto.getSvcCode();
                    int i = 0;
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        if(svcCode.equals(appSvcRelatedInfoDto.getServiceCode())){
                            break;
                        }
                        String baseSvcId = appSvcRelatedInfoDto.getBaseServiceId();
                        //specified svc
                        if(!StringUtil.isEmpty(baseSvcId)){
                            HcsaServiceDto baseSvcDto = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                            if(baseSvcDto == null){
                                log.info(StringUtil.changeForLog("current svc id is dirty data ..."));
                                continue;
                            }
                            if(svcCode.equals(baseSvcDto.getSvcCode())){
                                break;
                            }
                        }
                        if(i == appSvcRelatedInfoDtos.size()-1){
                            otherSvcDtoList.add(hcsaServiceDto);
                        }
                        i++;
                    }
                }
            }else{
                otherSvcDtoList.addAll(hcsaServiceDtos);
            }
            //create other appSvcDto
            if(!IaisCommonUtils.isEmpty(otherSvcDtoList)){
                for(HcsaServiceDto hcsaServiceDto:otherSvcDtoList){
                    AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                    appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                    appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                    appSvcRelatedInfoDto.setServiceType(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE);
                    appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                }
            }
            if(needSort){
                appSvcRelatedInfoDtos = sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            }
        }
        return appSvcRelatedInfoDtos;
    }

    public static List<AppSvcRelatedInfoDto> sortAppSvcRelatDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        List<AppSvcRelatedInfoDto> newAppSvcDto = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            List<AppSvcRelatedInfoDto> baseDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcRelatedInfoDto> specDtos = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                if(hcsaServiceDto == null){
                    log.info(StringUtil.changeForLog("svc code:"+svcCode+" can not found HcsaServiceDto"));
                    continue;
                }
                String serviceType = hcsaServiceDto.getSvcType();
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(serviceType)){
                    baseDtos.add(appSvcRelatedInfoDto);
                }else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(serviceType)){
                    specDtos.add(appSvcRelatedInfoDto);
                }
            }

            if(!IaisCommonUtils.isEmpty(baseDtos)){
                baseDtos.sort((h1,h2)->h1.getServiceName().compareTo(h2.getServiceName()));
                newAppSvcDto.addAll(baseDtos);
            }
            if(!IaisCommonUtils.isEmpty(specDtos)){
                specDtos.sort((h1,h2)->h1.getServiceName().compareTo(h2.getServiceName()));
                newAppSvcDto.addAll(specDtos);
            }
            appSvcRelatedInfoDtos = newAppSvcDto;
        }
        return newAppSvcDto;
    }

    public static String getPremisesHci(AppAlignLicQueryDto item){
        String premisesHci = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())) {
            premisesHci = item.getHciName() + IaisCommonUtils.genPremisesKey(item.getPostalCode(), item.getBlkNo(), item.getFloorNo(), item.getUnitNo());
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())) {
            premisesHci = item.getVehicleNo() + IaisCommonUtils.genPremisesKey(item.getPostalCode(), item.getBlkNo(), item.getFloorNo(), item.getUnitNo());
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(item.getPremisesType())) {
            premisesHci = IaisCommonUtils.genPremisesKey(item.getPostalCode(), item.getBlkNo(), item.getFloorNo(), item.getUnitNo());
        }
        return premisesHci;
    }

    public static boolean isAllFieldNull(AppSvcPrincipalOfficersDto person) throws Exception {
        boolean result = true;
        if(person != null){
            PersonFieldDto personFieldDto = MiscUtil.transferEntityDto(person,PersonFieldDto.class);
            if("-1".equals(personFieldDto.getSpeciality())){
                personFieldDto.setSpeciality(null);
            }
            Class psnClsa = personFieldDto.getClass();
            Field[] fs = psnClsa.getDeclaredFields();
            for(Field f:fs){
                ReflectionUtils.makeAccessible(f);
                Object value = IaisCommonUtils.getFieldValue(personFieldDto, f);
                if(!StringUtil.isEmpty(value)){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static AppPsnEditDto setNeedEditField(AppSvcPrincipalOfficersDto person){
        AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
        if(person != null){
            PersonFieldDto personFieldDto = MiscUtil.transferEntityDto(person,PersonFieldDto.class);
            if("-1".equals(personFieldDto.getSpeciality())){
                personFieldDto.setSpeciality(null);
            }
            Class psnClsa = personFieldDto.getClass();
            Field[] fs = psnClsa.getDeclaredFields();
            for(Field f:fs){
                if( Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                ReflectionUtils.makeAccessible(f);
                Object value = IaisCommonUtils.getFieldValue(personFieldDto, f);
                if(StringUtil.isEmpty(value)){
                    String fieldName = f.getName();
                    Field field = null;
                    try {
                        field = appPsnEditDto.getClass().getDeclaredField(fieldName);
                        ReflectionUtils.makeAccessible(field);
                        field.setBoolean(appPsnEditDto,true);
                    } catch (NoSuchFieldException e) {
                        log.debug(StringUtil.changeForLog("not found this field:"+fieldName));
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            //confirm with mingde , person_ext field can edit anytime
            appPsnEditDto.setDesignation(true);
            appPsnEditDto.setSubSpeciality(true);
            appPsnEditDto.setSpecialityOther(true);
            appPsnEditDto.setSpeciality(true);
            appPsnEditDto.setProfRegNo(true);
            appPsnEditDto.setProfessionType(true);

            if(ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(person.getLoadingType())){
                appPsnEditDto.setIdType(true);
                appPsnEditDto.setIdNo(true);
            }
        }
        return appPsnEditDto;
    }

    public static String[] setPsnValue(String[] arr, int i, AppSvcPrincipalOfficersDto person,String fieldName){
        if(arr == null){
            return new String[0];
        }
        String value = arr[i];
        Field field = null;
        try {
            field = person.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            log.error(StringUtil.changeForLog("not found this field:"+fieldName));
        }
        if(field != null){
            ReflectionUtils.makeAccessible(field);
            try {
                field.set(person,value);
            } catch (IllegalAccessException e) {
                log.error(StringUtil.changeForLog(e.getMessage()),e);
            }
        }
        return removeArrIndex(arr,i);
    }

    public static AppSvcPrincipalOfficersDto genAppSvcPrincipalOfficersDto(AppSvcPersonAndExtDto appSvcPersonAndExtDto, String svcCode,  boolean removeCurrExt){
        if(appSvcPersonAndExtDto == null){
            return null;
        }
        AppSvcPrincipalOfficersDto person = new AppSvcPrincipalOfficersDto();
        AppSvcPersonDto appSvcPersonDto = appSvcPersonAndExtDto.getPersonDto();
        if(appSvcPersonDto != null){
            person = MiscUtil.transferEntityDto(appSvcPersonDto,AppSvcPrincipalOfficersDto.class);
        }
        List<AppSvcPersonExtDto> appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
        AppSvcPersonExtDto appSvcPersonExtDto = getPsnExtDtoBySvcCode(appSvcPersonExtDtos,svcCode);
        if(appSvcPersonExtDto == null){
            appSvcPersonExtDto = new AppSvcPersonExtDto();

        }
        if(removeCurrExt && !IaisCommonUtils.isEmpty(appSvcPersonExtDtos)){
            appSvcPersonExtDtos.remove(appSvcPersonExtDto);
        }
        Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
        person = MiscUtil.transferEntityDto(appSvcPersonExtDto,AppSvcPrincipalOfficersDto.class,fieldMap,person);
        //transfer
        person.setLicPerson(appSvcPersonAndExtDto.isLicPerson());
        AppPsnEditDto appPsnEditDto = NewApplicationHelper.setNeedEditField(person);
        person.setPsnEditDto(appPsnEditDto);
        return person;
    }

    public static Map<String,String> getSpecialtyAttr(){
        Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
        specialtyAttr.put("name", "specialty");
        specialtyAttr.put("class", "specialty");
        specialtyAttr.put("style", "display: none;");
        return specialtyAttr;
    }

    public static void setPhName(List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos){
        if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)) {
            for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodDtos) {
                String dayName = appPremPhOpenPeriodDto.getDayName();
                String phDate = appPremPhOpenPeriodDto.getPhDate();
                if (StringUtil.isEmpty(dayName) && !StringUtil.isEmpty(phDate)) {
                    appPremPhOpenPeriodDto.setDayName(MasterCodeUtil.getCodeDesc(phDate));
                }
            }
        }
    }

    public static boolean isCharity(HttpServletRequest request){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        boolean isCharity = false;
        if(loginContext != null && AcraConsts.ENTITY_TYPE_CHARITIES.equals(loginContext.getLicenseeEntityType())){
            isCharity = true;
        }
        return isCharity;
    }

    public static boolean newAndNotRfi(HttpServletRequest request,String appType){
        return !checkIsRfi(request) && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
    }

    public static AppSubmissionDto getOldSubmissionDto(HttpServletRequest request){
        return ParamUtil.getSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO) == null?new AppSubmissionDto(): (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
    }

    public static List<String> genPremisesHciList(AppGrpPremisesDto appGrpPremisesDto){
        List<String> premisesHciList = IaisCommonUtils.genNewArrayList();
        if(appGrpPremisesDto != null){
            String premisesHciPre = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                premisesHciPre = appGrpPremisesDto.getHciName() + appGrpPremisesDto.getPostalCode() + appGrpPremisesDto.getBlkNo();
                premisesHciList.add(premisesHciPre + appGrpPremisesDto.getFloorNo() + appGrpPremisesDto.getUnitNo());
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                premisesHciPre = appGrpPremisesDto.getConveyanceHciName() + appGrpPremisesDto.getConveyanceVehicleNo() + appGrpPremisesDto.getConveyancePostalCode() + appGrpPremisesDto.getConveyanceBlockNo();
                premisesHciList.add(premisesHciPre + appGrpPremisesDto.getConveyanceFloorNo() + appGrpPremisesDto.getConveyanceUnitNo());
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
                premisesHciPre = appGrpPremisesDto.getOffSiteHciName() + appGrpPremisesDto.getOffSitePostalCode() + appGrpPremisesDto.getOffSiteBlockNo();
                premisesHciList.add(premisesHciPre + appGrpPremisesDto.getOffSiteFloorNo() + appGrpPremisesDto.getOffSiteUnitNo());
            }
            List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
            if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
                for(AppPremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                    premisesHciList.add(premisesHciPre + operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo());
                }
            }
        }
        return premisesHciList;
    }

    public static List<String> genPremisesHciList(PremisesDto premisesDto){
        List<String> premisesHciList = IaisCommonUtils.genNewArrayList();
        if(premisesDto != null){
            String premisesHciPre = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName() +premisesDto.getVehicleNo() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName() +premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }
            premisesHciList.add(premisesHciPre + premisesDto.getFloorNo() + premisesDto.getUnitNo());
            List<PremisesOperationalUnitDto> operationalUnitDtos = premisesDto.getPremisesOperationalUnitDtos();
            if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
                for(PremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                    premisesHciList.add(premisesHciPre + operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo());
                }
            }
        }
        return premisesHciList;
    }

    public static void setAudiErrMap(boolean isRfi, String appType, Map<String,String> errMap, String appNo,String licenceNo){
        if(isRfi){
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setApplicationNo(appNo);
            WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto,errMap);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            LicenceDto licenceDto = new LicenceDto();
            licenceDto.setLicenceNo(licenceNo);
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errMap);
        }else{
            WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        }

    }

    public static String genBankUrl(HttpServletRequest request,String payMethod,Map<String, String> fieldMap,PmtReturnUrlDto pmtReturnUrlDto) throws Exception {
        String url = "";
        switch (payMethod){
            case ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT:
                url= GatewayStripeAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getCreditRetUrl());break;
            case ApplicationConsts.PAYMENT_METHOD_NAME_NETS:
                url= GatewayNetsAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getNetsRetUrl());break;
            case ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW:
                url= GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getPayNowRetUrl());break;
            default:
                url= GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getOtherRetUrl());
        }
        return url;
    }

    public static AppGrpPrimaryDocDto getAppGrpprimaryDocDto(String docId,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!StringUtil.isEmpty(docId)){
            for(AppGrpPrimaryDocDto docDto:appGrpPrimaryDocDtos){
                if(docDto.getSvcComDocId().equals(docId)){
                    appGrpPrimaryDocDto = docDto;
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }

    public static AppGrpPrimaryDocDto genEmptyPrimaryDocDto(String docConfigId){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
        appGrpPrimaryDocDto.setSvcComDocId(docConfigId);
        return appGrpPrimaryDocDto;
    }

    public static String getLicenseeId(List<AppSubmissionDto> appSubmissionDtos){
        String licenseeId = "";
        if(!IaisCommonUtils.isEmpty(appSubmissionDtos)){
            for(AppSubmissionDto appSubmissionDto:appSubmissionDtos){
                licenseeId = appSubmissionDto.getLicenseeId();
                if(!StringUtil.isEmpty(licenseeId)){
                    break;
                }
            }
        }
        return licenseeId;
    }


    public static void removePremiseEmptyAlignInfo(AppSubmissionDto appSubmissionDto){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        //remove empty align primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = NewApplicationHelper.removeEmptyAlignPrimaryDoc(appGrpPremisesDtoList,appSubmissionDto.getAppGrpPrimaryDocDtos());
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                //remove empty align laboratoryDisciplinesDto
                List<AppSvcLaboratoryDisciplinesDto> laboratoryDisciplinesDtos = NewApplicationHelper.removeEmptyAlignSvcScope(appGrpPremisesDtoList,appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(laboratoryDisciplinesDtos);
                //remove empty align disciplineAllocation
                List<AppSvcDisciplineAllocationDto> disciplineAllocationDtos = NewApplicationHelper.removeEmptyAlignAllocation(appGrpPremisesDtoList,appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(disciplineAllocationDtos);
                //remove empty align svc spec doc
                List<AppSvcDocDto> appSvcDocDtos = NewApplicationHelper.removeEmptyAlignSvcDoc(appGrpPremisesDtoList,appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
    }

    public static void svcDocMandatoryValidate(List<HcsaSvcDocConfigDto> svcDocConfigDtos, List<HcsaSvcDocConfigDto> premSvcDocConfigDtos, List<AppSvcDocDto> appSvcDocDtos, List<AppGrpPremisesDto> appGrpPremisesDtos,Map<String, String> errorMap) {

        String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
        if(!IaisCommonUtils.isEmpty(svcDocConfigDtos)){
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:svcDocConfigDtos){
                String errKey = hcsaSvcDocConfigDto.getId()+"selectedFile";
                Boolean isMandatory = hcsaSvcDocConfigDto.getIsMandatory();
                boolean mandatoryFlag;
                if(isMandatory){
                    mandatoryFlag = false;
                }else{
                    mandatoryFlag = true;
                }
                if (isMandatory && IaisCommonUtils.isEmpty(appSvcDocDtos)) {

                } else if (isMandatory && !IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                    for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                        String svcDocId = appSvcDocDto.getSvcDocId();
                        if (hcsaSvcDocConfigDto.getId().equals(svcDocId)) {
                            mandatoryFlag = true;
                            break;
                        }
                    }
                }
                if (!mandatoryFlag) {
                    errorMap.put(errKey, err006);
                }
            }
        }

        if(!IaisCommonUtils.isEmpty(premSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                String premType = appGrpPremisesDto.getPremisesType();
                for (HcsaSvcDocConfigDto premHcasDocConfig : premSvcDocConfigDtos) {
                    String errKey = "prem" + premHcasDocConfig.getId()+ premIndexNo;
                    Boolean isMandatory = premHcasDocConfig.getIsMandatory();
                    boolean mandatoryFlag;
                    if(isMandatory){
                        mandatoryFlag = false;
                    }else{
                        mandatoryFlag = true;
                    }
                    if (isMandatory && IaisCommonUtils.isEmpty(appSvcDocDtos)) {

                    } else if (isMandatory && !IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                            String docPremVal = appSvcDocDto.getPremisesVal();
                            String docPremType = appSvcDocDto.getPremisesType();
                            if (!StringUtil.isEmpty(docPremVal) && !StringUtil.isEmpty(docPremType)) {
                                if (docPremVal.equals(premIndexNo) && docPremType.equals(premType)) {
                                    mandatoryFlag = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!mandatoryFlag) {
                        errorMap.put(errKey, err006);
                    }
                }
            }
        }
    }

    public static String repLength(String ... ars ) {
        int length = ars.length;
        String general_err0041 = MessageUtil.getMessageDesc("GENERAL_ERR0041");
        if(length==0){
            repLength(general_err0041);
        }else if(length==1){
            String field = ars[0].replace("{field}", "field");
            field=field.replace("{maxlength}","100");
            return field;
        }else if(length==2){
            general_err0041=general_err0041.replace("{field}",ars[0]);
            general_err0041=general_err0041.replace("{maxlength}",ars[1]); //NOSONAR

            return general_err0041;
        }else if(length==3){
            String messageDesc = MessageUtil.getMessageDesc(ars[0]);
            messageDesc=messageDesc.replace("{field}",ars[0]);//NOSONAR
            messageDesc=messageDesc.replace("{maxlength}",ars[1]);//NOSONAR
            return messageDesc;
        }else if(length==4){
            general_err0041=general_err0041.replace(ars[0],ars[1]);//NOSONAR
            general_err0041=general_err0041.replace(ars[2],ars[3]);//NOSONAR
            return general_err0041;
        }else if(length==5){
            String messageDesc = MessageUtil.getMessageDesc(ars[0]);
            if(messageDesc!=null){
                messageDesc=messageDesc.replace(ars[1],ars[2]);//NOSONAR
                messageDesc=messageDesc.replace(ars[3],ars[4]);//NOSONAR
            }
            return messageDesc;
        }else {
            return general_err0041;
        }

        return general_err0041;
    }

    //handler please indicate lab,not display "others" lab
    /**
     *  show others
     * */
    public static List<AppSvcChckListDto> handlerPleaseIndicateLab( List<AppSvcChckListDto> appSvcChckListDtos ,Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap) throws CloneNotSupportedException {
        List<AppSvcChckListDto> newAppSvcChckListDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcChckListDtos) && svcScopeAlignMap != null){
            AppSvcChckListDto targetDto = getScopeDtoByRecursiveTarNameUpward(appSvcChckListDtos,svcScopeAlignMap,NewApplicationConstant.PLEASEINDICATE,NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS);
            if(targetDto != null){
                for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtos){
                    AppSvcChckListDto newAppSvcChckListDto = (AppSvcChckListDto) CopyUtil.copyMutableObject(appSvcChckListDto);
                    String chkName = newAppSvcChckListDto.getChkName();
                    if(NewApplicationConstant.PLEASEINDICATE.equals(chkName)){
                        continue;
                    }
                    if(NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS.equals(chkName)){
                        chkName = chkName + " ("+ targetDto.getOtherScopeName() +")";
                        newAppSvcChckListDto.setChkName(chkName);
                    }
                    newAppSvcChckListDtos.add(newAppSvcChckListDto);
                }
            }else{
                newAppSvcChckListDtos = appSvcChckListDtos;
            }
        }
        return newAppSvcChckListDtos;
    }

    public static AppSvcChckListDto getScopeDtoByRecursiveTarNameUpward(List<AppSvcChckListDto> appSvcChckListDtos,Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap,String recursiveStartName,String recursiveEndName){
        AppSvcChckListDto targetDto = null;
        if(svcScopeAlignMap != null && !IaisCommonUtils.isEmpty(appSvcChckListDtos) && !StringUtil.isEmpty(recursiveStartName) && !StringUtil.isEmpty(recursiveEndName)){
            for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtos){
                String chkName = appSvcChckListDto.getChkName();
                if(recursiveStartName.equals(chkName)){
                    HcsaSvcSubtypeOrSubsumedDto targetConfigDto = svcScopeAlignMap.get(appSvcChckListDto.getChkLstConfId());
                    if(targetConfigDto != null){
                        String parentId = targetConfigDto.getParentId();
                        if(!StringUtil.isEmpty(parentId)){
                            HcsaSvcSubtypeOrSubsumedDto parentDto = getScopeConfigByRecursiveTarNameUpward(svcScopeAlignMap,recursiveEndName,parentId);
                            if(parentDto != null && recursiveEndName.equals(parentDto.getName())){
                                targetDto = getSvcChckListDtoByConfigId(targetConfigDto.getId(),appSvcChckListDtos);
                            }
                        }
                    }
                    break;
                }
            }
        }
        return targetDto;
    }

    public static AppSvcChckListDto getSvcChckListDtoByConfigName(String configName,List<AppSvcChckListDto> appSvcChckListDtos){
        AppSvcChckListDto  result = null;
        if(!StringUtil.isEmpty(configName) && !IaisCommonUtils.isEmpty(appSvcChckListDtos)){
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                if (configName.equals(appSvcChckListDto.getChkName())) {
                    result = appSvcChckListDto;
                    break;
                }
            }
        }
        return result;
    }
    //key is config id
    public static void recursingSvcScope(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getId(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                recursingSvcScope(dto.getList(), allCheckListMap);
            }
        }

    }
    //key is config name
    public static void recursingSvcScopeKeyIsName(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getName(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                recursingSvcScopeKeyIsName(dto.getList(), allCheckListMap);
            }
        }

    }
    public static void setPremAddress(AppSubmissionDto appSubmissionDto){
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    setPremAddressForSvcScope(appSubmissionDto.getAppGrpPremisesDtoList(),appSvcRelatedInfoDto);
                }
            }
        }
    }

    public static String generateMultipleDropDown(Map<String, String> pageAttr, List<SelectOption> selectOptionList, String firestOption, List<String> checkValList){
        StringBuilder result = new StringBuilder();
        if(!IaisCommonUtils.isEmpty(selectOptionList) && !IaisCommonUtils.isEmpty(pageAttr)){
            String id = pageAttr.get("id");
            if(StringUtil.isEmpty(id)){
                id = "";
            }
            result.append("<div class=\"row\"><div class=\"col-md-12 multi-select\">")
                    .append("<div style=\"height: 200px; border: 1px solid darkgrey;overflow: scroll\" id=\"")
                    .append(id)
                    .append("Clear\">");
            int i = 0;
            for(SelectOption sp:selectOptionList){
                String alignId = pageAttr.get("name") + i;
                result.append("<label class=\"checkbox-custom check-primary\" style=\"margin-left: 2px\">")
                        .append("<input value=\"")
                        .append(sp.getValue())
                        .append("\"");
                for(Map.Entry<String, String> entry : pageAttr.entrySet()){
                    result.append(entry.getKey())
                            .append("=\"");
                            if("id".equals(entry.getKey())){
                                result.append(alignId)
                                        .append('\"');
                            }else{
                                result.append(entry.getValue())
                                        .append('\"');
                            }
                }
                result.append("type=\"checkbox\">")
                        .append(" <label for=\"")
                        .append(alignId)
                        .append("\">")
                        .append("<span>")
                        .append(sp.getText())
                        .append("</span>")
                        .append("</label>")
                        .append("</label><br>");
                i++;
            }
            result.append("</div></div></div>");
        }

        return result.toString();
    }

    public static List<SelectOption> genWorkingDaySp(){
        List<SelectOption> workingDaySp = IaisCommonUtils.genNewArrayList();
        SelectOption sp1 = new SelectOption("Mon","Monday");
        SelectOption sp2 = new SelectOption("Tue","Tuesday");
        SelectOption sp3 = new SelectOption("Wed","Wednesday");
        SelectOption sp4 = new SelectOption("Thu","Thursday");
        SelectOption sp5 = new SelectOption("Fri","Friday");
        SelectOption sp6 = new SelectOption("Sat","Saturday");
        SelectOption sp7 = new SelectOption("Sun","Sunday");
        workingDaySp.add(sp1);
        workingDaySp.add(sp2);
        workingDaySp.add(sp3);
        workingDaySp.add(sp4);
        workingDaySp.add(sp5);
        workingDaySp.add(sp6);
        workingDaySp.add(sp7);
        return workingDaySp;
    }

    public static boolean psnDoPartValidate(String idType,String idNo,String name){
        boolean result = true;
        if(StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo) || StringUtil.isEmpty(name)){
            result = false;
        }else{
            if(idNo.length() > 9){
                result = false;
            }
            if(OrganizationConstants.ID_TYPE_FIN.equals(idType)){
                boolean b = SgNoValidator.validateFin(idNo);
                if(!b){
                    result = false;
                }
            }
            if(OrganizationConstants.ID_TYPE_NRIC.equals(idType)){
                boolean b1 = SgNoValidator.validateNric(idNo);
                if(!b1){
                    result = false;
                }
            }

        }
        return result;
    }

    //=============================================================================
    //private method
    //=============================================================================

    private static HcsaSvcSubtypeOrSubsumedDto getScopeConfigByRecursiveTarNameUpward(Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap,String targetChkName,String startId){
        HcsaSvcSubtypeOrSubsumedDto targetDto = null;
        if(svcScopeAlignMap != null && !StringUtil.isEmpty(startId) && !StringUtil.isEmpty(targetChkName)){
            HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = svcScopeAlignMap.get(startId);
            if(hcsaSvcSubtypeOrSubsumedDto != null){
                if(targetChkName.equals(hcsaSvcSubtypeOrSubsumedDto.getName())){
                    targetDto = hcsaSvcSubtypeOrSubsumedDto;
                }else if(!StringUtil.isEmpty(hcsaSvcSubtypeOrSubsumedDto.getParentId())){
                    targetDto = getScopeConfigByRecursiveTarNameUpward(svcScopeAlignMap,targetChkName,hcsaSvcSubtypeOrSubsumedDto.getParentId());
                }
            }
        }
        return targetDto;
    }

    private static List<AppGrpPrimaryDocDto> removeEmptyAlignPrimaryDoc( List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos){
        List<AppGrpPrimaryDocDto> newGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                String docPremName = appGrpPrimaryDocDto.getPremisessName();
                String docPremType = appGrpPrimaryDocDto.getPremisessType();
                //add prem doc
                if (!StringUtil.isEmpty(docPremName) && !StringUtil.isEmpty(docPremType)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        String premType = appGrpPremisesDto.getPremisesType();
                        if (docPremName.equals(premIndexNo) && docPremType.equals(premType)) {
                            newGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        }
                    }
                } else if (StringUtil.isEmpty(docPremName) && StringUtil.isEmpty(docPremType)) {
                    //add comm doc
                    newGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                }
            }
            appGrpPrimaryDocDtos = newGrpPrimaryDocDtos;
        }
        return appGrpPrimaryDocDtos;
    }

    private static List<AppSvcLaboratoryDisciplinesDto> removeEmptyAlignSvcScope( List<AppGrpPremisesDto> appGrpPremisesDtoList,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        List<AppSvcLaboratoryDisciplinesDto> laboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if (!IaisCommonUtils.isEmpty(laboratoryDisciplinesDtos)) {
            List<AppSvcLaboratoryDisciplinesDto> newLaboratoryDisciplinesDtos = IaisCommonUtils.genNewArrayList();
            for (AppSvcLaboratoryDisciplinesDto laboratoryDisciplinesDto : laboratoryDisciplinesDtos) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    if (laboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                        newLaboratoryDisciplinesDtos.add(laboratoryDisciplinesDto);
                        break;
                    }
                }
            }
            laboratoryDisciplinesDtos = newLaboratoryDisciplinesDtos;
        }
        return laboratoryDisciplinesDtos;
    }

    private static List<AppSvcDisciplineAllocationDto> removeEmptyAlignAllocation(List<AppGrpPremisesDto> appGrpPremisesDtoList,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        List<AppSvcDisciplineAllocationDto> disciplineAllocationDtos = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if (!IaisCommonUtils.isEmpty(disciplineAllocationDtos)) {
            List<AppSvcDisciplineAllocationDto> newDisciplineAllocations = IaisCommonUtils.genNewArrayList();
            for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : disciplineAllocationDtos) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    if (appSvcDisciplineAllocationDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                        newDisciplineAllocations.add(appSvcDisciplineAllocationDto);
                        break;
                    }
                }
            }
            disciplineAllocationDtos = newDisciplineAllocations;
        }
        return disciplineAllocationDtos;
    }

    private static List<AppSvcDocDto> removeEmptyAlignSvcDoc(List<AppGrpPremisesDto> appGrpPremisesDtoList,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            List<AppSvcDocDto> newAppSvcDocDtos = IaisCommonUtils.genNewArrayList();
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                String docPremType = appSvcDocDto.getPremisesType();
                String docPremVal = appSvcDocDto.getPremisesVal();
                if(StringUtil.isEmpty(docPremType) && StringUtil.isEmpty(docPremVal)){
                    newAppSvcDocDtos.add(appSvcDocDto);
                }else if(!StringUtil.isEmpty(docPremType) && !StringUtil.isEmpty(docPremVal)){
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        String premType = appGrpPremisesDto.getPremisesType();
                        if (docPremVal.equals(premIndexNo) && docPremType.equals(premType)) {
                            newAppSvcDocDtos.add(appSvcDocDto);
                        }
                    }
                }
            }
            appSvcDocDtos = newAppSvcDocDtos;
        }
        return appSvcDocDtos;
    }

    private static List<SelectOption> getPremisesSel(String appType){
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        SelectOption cps1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        selectOptionList.add(cps1);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            SelectOption cps2 = new SelectOption("newPremise", "Moving to a new address");
            selectOptionList.add(cps2);
        }else{
            SelectOption cps2 = new SelectOption("newPremise", "Add a new premises");
            selectOptionList.add(cps2);
        }
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


    private static void setPremAddressForSvcScope(List<AppGrpPremisesDto> appGrpPremisesDtos,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtos) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                    String premval = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                    if (!StringUtil.isEmpty(premIndexNo) && premIndexNo.equals(premval)) {
                        appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                    }
                }
            }
            appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtos);
        }
    }

    private static void setSvcScopeInfo(List<AppGrpPremisesDto> appGrpPremisesDtos,AppSvcRelatedInfoDto appSvcRelatedInfoDto,Map<String, HcsaSvcSubtypeOrSubsumedDto> map){
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)){
            setPremAddressForSvcScope(appGrpPremisesDtos,appSvcRelatedInfoDto);
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtos){
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

        psnDto.setProfessionType(appSvcCgoDto.getProfessionType());
        psnDto.setProfRegNo(appSvcCgoDto.getProfRegNo());
        psnDto.setSpeciality(appSvcCgoDto.getSpeciality());
        psnDto.setSpecialityOther(appSvcCgoDto.getSpecialityOther());
        psnDto.setSubSpeciality(appSvcCgoDto.getSubSpeciality());
        psnDto.setLicPerson(appSvcCgoDto.isLicPerson());
        return psnDto;
    }

    private static void syncCgoDto(List<AppSvcCgoDto> appSvcCgoDtos, Map<String,AppSvcPersonAndExtDto> personMap, String svcCode){
        if(IaisCommonUtils.isEmpty(appSvcCgoDtos) || personMap == null || StringUtil.isEmpty(svcCode)){
            return;
        }
        for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
            boolean isLicPsn = appSvcCgoDto.isLicPerson();
            String personKey = appSvcCgoDto.getIdType()+ "," + appSvcCgoDto.getIdNo();
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personKey);
            AppSvcPrincipalOfficersDto selPerson = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto,svcCode,false);
            if(selPerson != null){
                appSvcCgoDto.setAssignSelect(getPersonKey(selPerson.getIdType(),selPerson.getIdNo()));
                appSvcCgoDto.setSalutation(selPerson.getSalutation());
                appSvcCgoDto.setName(selPerson.getName());
                appSvcCgoDto.setIdType(selPerson.getIdType());
                appSvcCgoDto.setIdNo(selPerson.getIdNo());
                appSvcCgoDto.setDesignation(selPerson.getDesignation());
                String professionType = selPerson.getProfessionType();
                if(!StringUtil.isEmpty(professionType)){
                    appSvcCgoDto.setProfessionType(professionType);
                }
                String profRegNo = selPerson.getProfRegNo();
                if(!StringUtil.isEmpty(profRegNo)){
                    appSvcCgoDto.setProfRegNo(profRegNo);
                }
                String speciality = selPerson.getSpeciality();
                if(!StringUtil.isEmpty(speciality)){
                    appSvcCgoDto.setSpeciality(speciality);
                }
                String specialityOther = selPerson.getSpecialityOther();
                if(!StringUtil.isEmpty(specialityOther)){
                    appSvcCgoDto.setSpecialityOther(specialityOther);
                }
                String subSpeciality = selPerson.getSubSpeciality();
                if(!StringUtil.isEmpty(subSpeciality)){
                    appSvcCgoDto.setSubSpeciality(subSpeciality);
                }
                appSvcCgoDto.setMobileNo(selPerson.getMobileNo());
                appSvcCgoDto.setEmailAddr(selPerson.getEmailAddr());
                //sync other field
                String officeTelNo = selPerson.getOfficeTelNo();
                appSvcCgoDto.setOfficeTelNo(officeTelNo);

                //
                appSvcCgoDto.setNeedSpcOptList(selPerson.isNeedSpcOptList());
                List<SelectOption> spcOptList = selPerson.getSpcOptList();
                if(!IaisCommonUtils.isEmpty(spcOptList)){
                    appSvcCgoDto.setSpcOptList(selPerson.getSpcOptList());
                }
                String specHtml = selPerson.getSpecialityHtml();
                if(!StringUtil.isEmpty(specHtml)){
                    appSvcCgoDto.setSpecialityHtml(specHtml);
                }
                //set lic person info
                appSvcCgoDto.setLicPerson(isLicPsn);
            }
        }
    }

    private static void syncPsnDto(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos, Map<String,AppSvcPersonAndExtDto> personMap, String svcCode){
        if(IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos) || personMap == null || StringUtil.isEmpty(svcCode)){
            return;
        }
        for(AppSvcPrincipalOfficersDto person:appSvcPrincipalOfficersDtos){
            String personKey = person.getIdType()+ "," + person.getIdNo();
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personKey);
            AppSvcPrincipalOfficersDto selPerson = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto,svcCode,false);
            if(selPerson != null){
                person.setAssignSelect(getPersonKey(selPerson.getIdType(),selPerson.getIdNo()));
                person.setSalutation(selPerson.getSalutation());
                person.setName(selPerson.getName());
                person.setIdType(selPerson.getIdType());
                person.setIdNo(selPerson.getIdNo());
                person.setMobileNo(selPerson.getMobileNo());
                person.setEmailAddr(selPerson.getEmailAddr());
                String officeTelNo = selPerson.getOfficeTelNo();
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(person.getPsnType()) || ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(person.getPsnType()) ){
                    person.setOfficeTelNo(officeTelNo);
                }else if(!StringUtil.isEmpty(officeTelNo)){
                    //cgo column -> cgoC1 , po not have cgoC1 po to sync data shouldnt set null
                    person.setOfficeTelNo(officeTelNo);
                }
               /* String preferredMode = selPerson.getPreferredMode();
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(person.getPsnType())){
                    person.setPreferredMode(preferredMode);
                }else if(!StringUtil.isEmpty(preferredMode)){
                    person.setPreferredMode(preferredMode);
                }*/

                //sync other field
                String designation = selPerson.getDesignation();
                String professionType = selPerson.getProfessionType();
                String profRegNo = selPerson.getProfRegNo();
                String speciality = selPerson.getSpeciality();
                String specialityOther = selPerson.getSpecialityOther();
                String subSpeciality = selPerson.getSubSpeciality();
                if(!StringUtil.isEmpty(designation)){
                    person.setDesignation(designation);
                }
                if(!StringUtil.isEmpty(professionType)){
                    person.setProfessionType(professionType);
                }
                if(!StringUtil.isEmpty(profRegNo)){
                    person.setProfRegNo(profRegNo);
                }
                if(!StringUtil.isEmpty(speciality)){
                    person.setSpeciality(speciality);
                }
                if(!StringUtil.isEmpty(specialityOther)){
                    person.setSpecialityOther(specialityOther);
                }
                if(!StringUtil.isEmpty(subSpeciality)){
                    person.setSubSpeciality(subSpeciality);
                }
                //set lic person info
                person.setLicPerson(selPerson.isLicPerson());

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

    private static Map<String,String> doPsnCommValidate(Map<String,String> errMap,String idType,String idNo,boolean licPerson,Map<String,AppSvcPersonAndExtDto> licPersonMap,String errKey,String svcCode){
        if(!StringUtil.isEmpty(idType) && !StringUtil.isEmpty(idNo) && !licPerson){
            String personKey = NewApplicationHelper.getPersonKey(idType, idNo);
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = licPersonMap.get(personKey);
            if(appSvcPersonAndExtDto != null){
                String errMsg = MessageUtil.getMessageDesc("NEW_ERR0006");
                errMsg = errMsg.replace("{ID No.}",idNo);
                errMap.put(errKey,errMsg);
            }
        }
        return errMap;
    }

    private static AppSvcChckListDto getSvcChckListDtoByConfigId(String configId,List<AppSvcChckListDto> appSvcChckListDtos){
        AppSvcChckListDto  result = null;
        if(!StringUtil.isEmpty(configId) && !IaisCommonUtils.isEmpty(appSvcChckListDtos)){
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                if (configId.equals(appSvcChckListDto.getChkLstConfId())) {
                    result = appSvcChckListDto;
                    break;
                }
            }
        }
        return result;
    }



    private static String[] removeArrIndex(String[] arrs, int index) {
        if (arrs == null) {
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

    private static AppSvcPersonExtDto getPsnExtDtoBySvcCode(List<AppSvcPersonExtDto> appSvcPersonExtDtos, String svcCode){
        AppSvcPersonExtDto appSvcPersonExtDto = null;
        if(!IaisCommonUtils.isEmpty(appSvcPersonExtDtos) && !StringUtil.isEmpty(svcCode)){
            for(AppSvcPersonExtDto extPsn:appSvcPersonExtDtos){
                String serviceCode = extPsn.getServiceCode();
                String serviceName = extPsn.getServiceName();
                if(!StringUtil.isEmpty(serviceCode)){
                    if(svcCode.equals(serviceCode)){
                        appSvcPersonExtDto = extPsn;
                        break;
                    }
                }else if(!StringUtil.isEmpty(serviceName)){
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                    boolean flag = hcsaServiceDto != null && serviceName.equals(hcsaServiceDto.getSvcName());
                    if(flag){
                        appSvcPersonExtDto = extPsn;
                        break;
                    }
                }
            }
        }
        return appSvcPersonExtDto;
    }

    private static void setReloadTime(OperationHoursReloadDto operationHoursReloadDto){
        List<String> selectValList = operationHoursReloadDto.getSelectValList();
        if(!IaisCommonUtils.isEmpty(selectValList)){
            String [] selectArr = (String[]) selectValList.toArray(new String[selectValList.size()]);
            String phSelect = ParamUtil.StringsToString(selectArr);
            operationHoursReloadDto.setSelectVal(phSelect);
        }
        Time startTime = operationHoursReloadDto.getStartFrom();
        Time endTime = operationHoursReloadDto.getEndTo();
        if(startTime != null){
            LocalTime localTimeStart = startTime.toLocalTime();
            operationHoursReloadDto.setStartFromHH(String.valueOf(localTimeStart.getHour()));
            operationHoursReloadDto.setStartFromMM(String.valueOf(localTimeStart.getMinute()));
        }
        if(endTime != null){
            LocalTime localTimeStart = endTime.toLocalTime();
            operationHoursReloadDto.setEndToHH(String.valueOf(localTimeStart.getHour()));
            operationHoursReloadDto.setEndToMM(String.valueOf(localTimeStart.getMinute()));
        }
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
            String fileType=  filename.substring(filename.lastIndexOf('.')+1);
            String s = fileType.toUpperCase();
            if(!fileTypes.contains(s)){
                map.put("fileType",Boolean.FALSE);
            }else {
                map.put("fileType",Boolean.TRUE);
            }
            if(size>fileSize){
                map.put("fileSize",Boolean.FALSE);
            }else {
                map.put("fileSize",Boolean.TRUE);
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

    public static void validatePH(Map<String,String> errorMap,AppSubmissionDto appSubmissionDto){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(appGrpPremisesDtoList!=null){
            for(int i=0;i<appGrpPremisesDtoList.size();i++){
                String premisesType = appGrpPremisesDtoList.get(i).getPremisesType();
                String s="";
                if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                    s="conveyance";
                }else if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                    s="onSite";
                }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
                    s="offSite";
                }
                List<OperationHoursReloadDto> weeklyDtoList = appGrpPremisesDtoList.get(i).getWeeklyDtoList();
                List<OperationHoursReloadDto> phDtoList = appGrpPremisesDtoList.get(i).getPhDtoList();
                List<AppPremEventPeriodDto> eventDtoList = appGrpPremisesDtoList.get(i).getEventDtoList();
                validate(phDtoList,errorMap,i,s+"PubHoliday");
                validate(weeklyDtoList,errorMap,i,s+"Weekly");
                validateEvent(eventDtoList,errorMap,i,s+"Event");
            }
        }
    }
    public static void validate(List<OperationHoursReloadDto> list,Map<String,String> errorMap,int index,String errorId){
        if(list==null) {
            return;
        }
        for(int i=0;i< list.size();i++){
            for(int j=i+1;j< list.size() &&i!=j ;j++){
                List<String> selectValList = list.get(i).getSelectValList();
                List<String> selectValList1 = list.get(j).getSelectValList();
                if(selectValList==null || selectValList1==null){
                    continue;
                }
                boolean disjoint = Collections.disjoint(selectValList, selectValList1);
                if(disjoint){
                    continue;
                }
                boolean selectAllDay = list.get(i).isSelectAllDay();
                boolean selectAllDay1 = list.get(j).isSelectAllDay();
                if(selectAllDay ||selectAllDay1){
                    errorMap.put(errorId+index+j,MessageUtil.getMessageDesc("NEW_ERR0021"));
                    continue;
                }
                int time = getTime(list.get(i).getEndToHH(), list.get(i).getEndToMM());
                int   time1 = getTime(list.get(j).getStartFromHH(), list.get(j).getStartFromMM());
                if(time>=time1){
                    errorMap.put(errorId+index+j,MessageUtil.getMessageDesc("NEW_ERR0021"));
                }
            }
        }
    }

    public static int getTime(String hh,String mm){
        try {
            int i = Integer.parseInt(hh);
            int i1 = Integer.parseInt(mm);
            return i*60+i1;
        }catch (NumberFormatException e){
            return 0;
        }
    }
    public static void validateEvent(List<AppPremEventPeriodDto> appPremEventPeriodDtoList,Map<String,String> map,int index,String errorId){
        if(appPremEventPeriodDtoList==null){
            return;
        }
        for(int i=0;i<appPremEventPeriodDtoList.size();i++){
            for(int j=i+1;j<appPremEventPeriodDtoList.size()&&i!=j;j++){
                String eventName = appPremEventPeriodDtoList.get(i).getEventName();
                String eventName1 = appPremEventPeriodDtoList.get(j).getEventName();
                if(!StringUtil.isEmpty(eventName) && !StringUtil.isEmpty(eventName1)){
                    if(!eventName.equals(eventName1)){
                        continue;
                    }
                    Date endDate = appPremEventPeriodDtoList.get(i).getEndDate();
                    Date startDate = appPremEventPeriodDtoList.get(j).getStartDate();
                    if(endDate != null && startDate != null){
                        if(endDate.after(startDate)||endDate.compareTo(startDate)==0){
                            map.put(errorId+index+j,MessageUtil.getMessageDesc("NEW_ERR0021"));
                        }
                    }
                }
            }
        }
    }

    private static List<String> getOtherScopeChildrenIdList(List<HcsaSvcSubtypeOrSubsumedDto> scopeConfigDtoList){
        List<String> otherScopeChildrenList = IaisCommonUtils.genNewArrayList();
        HcsaSvcSubtypeOrSubsumedDto otherScopeConfigDto = null;
        if(!IaisCommonUtils.isEmpty(scopeConfigDtoList)){
            for(HcsaSvcSubtypeOrSubsumedDto scopeConfigDto:scopeConfigDtoList){
                if(NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS.equals(scopeConfigDto.getName())){
                    otherScopeConfigDto = scopeConfigDto;
                    break;
                }
            }
            if(otherScopeConfigDto != null){
                List<HcsaSvcSubtypeOrSubsumedDto> otherScopeChildrenDtoList = otherScopeConfigDto.getList();
                if(!IaisCommonUtils.isEmpty(otherScopeChildrenDtoList)){
                    for(HcsaSvcSubtypeOrSubsumedDto otherScopeChildrenDto:otherScopeChildrenDtoList){
                        otherScopeChildrenList.add(otherScopeChildrenDto.getId());
                    }
                }

            }
        }
        return otherScopeChildrenList;
    }

    private static  boolean selectOtherScope(List<AppSvcChckListDto> appSvcChckListDtos){
        boolean flag = false;
        if(!IaisCommonUtils.isEmpty(appSvcChckListDtos)){
            for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtos){
                if(NewApplicationConstant.SERVICE_SCOPE_LAB_OTHERS.equals(appSvcChckListDto.getChkName())){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
