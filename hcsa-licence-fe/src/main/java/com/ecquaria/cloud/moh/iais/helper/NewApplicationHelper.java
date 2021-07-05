package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator;
import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayNetsAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayPayNowAPI;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PersonFieldDto;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.util.CopyUtil;
import sop.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
                    String err006=MessageUtil.replaceMessage("GENERAL_ERR0006",NewApplicationConstant.PLEASEINDICATE,"field");
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
                            map.put("otherScopeError"+premCount,err006);
                        }
                    }
                    for(int i=0;i<listDtos.size();i++){
                        if(NewApplicationConstant.PLEASEINDICATE.equals(listDtos.get(i).getChkName())&&StringUtil.isEmpty(listDtos.get(i).getOtherScopeName()) ){
                            map.put("pleaseIndicateError"+premCount,err006);
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

    public static Map<String,String> doValidateGovernanceOfficers(List<AppSvcPrincipalOfficersDto> appSvcCgoList, Map<String,AppSvcPersonAndExtDto> licPersonMap, String svcCode){

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

                String professionType = appSvcCgoList.get(i).getProfessionType();
                if(StringUtil.isEmpty(professionType)){
                    errMap.put("professionType"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Type ","field"));
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if(StringUtil.isEmpty(designation)){
                    errMap.put("designation"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field"));
                }else if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)){
                    String otherDesignation = appSvcCgoList.get(i).getOtherDesignation();
                    if(StringUtil.isEmpty(otherDesignation)){
                        errMap.put("otherDesignation"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Others Designation","field"));
                    }else if(otherDesignation.length() > 100){
                        String general_err0041 = repLength("Others Designation","100");
                        errMap.put("otherDesignation" + i, general_err0041);
                    }

                }
                String professionRegoNo = appSvcCgoList.get(i).getProfRegNo();
                if(!StringUtil.isEmpty(professionRegoNo) && professionRegoNo.length() > 20){
                    String general_err0041=repLength("Professional Regn. No.","20");
                    errMap.put("professionRegoNo" + i, general_err0041);
                }
                String specialty = appSvcCgoList.get(i).getSpeciality();
                if(StringUtil.isEmpty(professionRegoNo) || StringUtil.isEmpty(specialty)){
                    String otherQualification = appSvcCgoList.get(i).getOtherQualification();
                    if(StringUtil.isEmpty(otherQualification)){
                        errMap.put("otherQualification"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Other Qualification","field"));
                    }
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
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                        List<SelectOption> specialtyList = genSpecialtySelectList(appSvcRelatedInfoDto.getServiceCode(),true);
                        List<String> specialtyKeyList = IaisCommonUtils.genNewArrayList();
                        for(SelectOption sp:specialtyList){
                            specialtyKeyList.add(sp.getValue());
                        }
                        List<SelectOption> allSpecialtyList = getAllSpecialtySelList();
                        for(AppSvcPrincipalOfficersDto appSvcCgoDto:appSvcCgoDtos){
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
                    }else if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)){
                        String otherDesignation = poDto.get(i).getOtherDesignation();
                        if(StringUtil.isEmpty(otherDesignation)){
                            oneErrorMap.put("otherDesignation"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Others Designation","field"));
                        }else if(otherDesignation.length() > 100){
                            String general_err0041 = repLength("Others Designation","100");
                            oneErrorMap.put("otherDesignation" + i, general_err0041);
                        }
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
                    }else if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)){
                        String otherDesignation = poDto.get(i).getOtherDesignation();
                        if(StringUtil.isEmpty(otherDesignation)){
                            oneErrorMap.put("deputyOtherDesignation"+dpoIndex,MessageUtil.replaceMessage("GENERAL_ERR0006","Others Designation","field"));
                        }else if(otherDesignation.length() > 100){
                            String general_err0041 = repLength("Others Designation","100");
                            oneErrorMap.put("deputyOtherDesignation" + dpoIndex, general_err0041);
                        }
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
            }else if("other".equals(val) || MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(val) || NewApplicationConstant.DESIGNATION_OTHERS.equals(val)){
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
            boolean otherVal = "other".equals(val) || MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(val) || NewApplicationConstant.DESIGNATION_OTHERS.equals(val);
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
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtos = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)&&!IaisCommonUtils.isEmpty(appSvcCgoDtos) && !IaisCommonUtils.isEmpty(appSvcDisciplineAllocationDtos)){
            for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:appSvcDisciplineAllocationDtos){
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                String svcScopeConfigId = appSvcDisciplineAllocationDto.getChkLstConfId();
                if(StringUtil.isEmpty(idNo) || StringUtil.isEmpty(svcScopeConfigId)){
                    continue;
                }
                //set svc cgoNo
                for(AppSvcPrincipalOfficersDto appSvcCgoDto:appSvcCgoDtos){
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
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                    for(AppSvcPrincipalOfficersDto appSvcCgoDto:appSvcCgoDtos){
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

    public static List<AppSvcPrincipalOfficersDto> transferCgoToPsnDtoList(List<AppSvcPrincipalOfficersDto> appSvcCgoDtos){
        List<AppSvcPrincipalOfficersDto> psnDtos = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(appSvcCgoDtos)){
            return psnDtos;
        }
        for(AppSvcPrincipalOfficersDto appSvcCgoDto:appSvcCgoDtos){
            AppSvcPrincipalOfficersDto psnDto = MiscUtil.transferEntityDto(appSvcCgoDto,AppSvcPrincipalOfficersDto.class);
            psnDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            psnDtos.add(psnDto);
        }
        return psnDtos;
    }

    public static Map<String,AppSvcPersonAndExtDto> initSetPsnIntoSelMap(Map<String,AppSvcPersonAndExtDto> personMap, List<AppSvcPrincipalOfficersDto> psnDtos, String svcCode){
        if(IaisCommonUtils.isEmpty(psnDtos)){
            return personMap;
        }
        for(AppSvcPrincipalOfficersDto psnDto:psnDtos){
            if(!psnDoPartValidate(psnDto.getIdType(),psnDto.getIdNo(),psnDto.getName())){
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
                String designation = psnDto.getDesignation();
                if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnDto.getPsnType())) {
                    person.setDesignation(designation);
                    if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setProfessionBoard(psnDto.getProfessionBoard());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpecialtyGetDate(handleDate(psnDto.getSpecialtyGetDate(), psnDto.getSpecialtyGetDateStr()));
                    person.setSpecialtyGetDateStr(handleDateString(psnDto.getSpecialtyGetDate(), psnDto.getSpecialtyGetDateStr()));
                    person.setTypeOfCurrRegi(psnDto.getTypeOfCurrRegi());
                    person.setCurrRegiDate(handleDate(psnDto.getCurrRegiDate(), psnDto.getCurrRegiDateStr()));
                    person.setCurrRegiDateStr(handleDateString(psnDto.getCurrRegiDate(), psnDto.getCurrRegiDateStr()));
                    person.setPraCerEndDate(handleDate(psnDto.getPraCerEndDate(), psnDto.getPraCerEndDateStr()));
                    person.setPraCerEndDateStr(handleDateString(psnDto.getPraCerEndDate(), psnDto.getPraCerEndDateStr()));
                    person.setTypeOfRegister(psnDto.getTypeOfRegister());
                    person.setRelevantExperience(psnDto.getRelevantExperience());
                    person.setHoldCerByEMS(psnDto.getHoldCerByEMS());
                    person.setAclsExpiryDate(handleDate(psnDto.getAclsExpiryDate(), psnDto.getAclsExpiryDateStr()));
                    person.setAclsExpiryDateStr(handleDateString(psnDto.getAclsExpiryDate(), psnDto.getAclsExpiryDateStr()));
                    person.setBclsExpiryDate(handleDate(psnDto.getBclsExpiryDate(), psnDto.getBclsExpiryDateStr()));
                    person.setBclsExpiryDateStr(handleDateString(psnDto.getBclsExpiryDate(), psnDto.getBclsExpiryDateStr()));
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())){
                    person.setDesignation(designation);
                    if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)){
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setProfessionType(psnDto.getProfessionType());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpeciality(psnDto.getSpeciality());
                    person.setSpecialityOther(psnDto.getSpecialityOther());
                    person.setSubSpeciality(psnDto.getSubSpeciality());
                    person.setQualification(psnDto.getQualification());
                    person.setOtherQualification(psnDto.getOtherQualification());
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
                    person.setDesignation(psnDto.getDesignation());
                    if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)){
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())){
                    person.setDesignation(psnDto.getDesignation());
                    if(MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)){
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
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

    public static Date handleDate(Date date, String str) {
        Date newDate = null;
        if (date != null) {
            newDate = (Date) date.clone();
        } else {
            newDate = DateUtil.parseDate(str, Formatter.DATE);
        }
        return newDate;
    }

    public static String handleDateString(Date date, String str) {
        String newDate = null;
        if (date != null) {
            newDate = Formatter.formatDate(date);
        } else if (CommonValidator.isDate(str)) {
            newDate = str;
        }
        return newDate;
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
                        //person.setSpecialityOther(psnDto.getSpecialityOther());
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
                appSvcPersonDto.setOtherDesignation(feUserDto.getDesignationOther());
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
                    //person.setSpecialityOther(psnDto.getSpecialityOther());
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

    public static List<SelectOption> genEasMtsSpecialtySelectList(String svcCode){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        specialtySelectList.add(new SelectOption("-1", "Please Select"));
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(svcCode)){
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_EMERGENCY_MEDICINE, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_EMERGENCY_MEDICINE)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_GENERAL_SURGERY, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_GENERAL_SURGERY)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_ANAESTHESIA, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_ANAESTHESIA)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_INTENSIVE_CARE, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_INTENSIVE_CARE)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS)));

            }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(svcCode)){
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS)));
            }
        }
        return specialtySelectList;
    }

    public static List<SelectOption> genEasMtsDesignationSelectList(List<HcsaServiceDto> hcsaServiceDtos){
        List<SelectOption> designationSelectList = IaisCommonUtils.genNewArrayList();
        designationSelectList.add(new SelectOption("-1", "Please Select"));
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            boolean hasEasSvc = false;
            boolean hasMtsSvc = false;
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(hcsaServiceDto.getSvcCode())){
                    hasEasSvc = true;
                }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(hcsaServiceDto.getSvcCode())){
                    hasMtsSvc = true;
                }
            }
            SelectOption sp1 = new SelectOption(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_EAS, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_EAS));
            SelectOption sp2 = new SelectOption(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_MTS, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_MTS));
            SelectOption sp3 = new SelectOption(ApplicationConsts.EAS_MTS_DESIGNATION_EAS_AND_MTS, MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_DESIGNATION_EAS_AND_MTS));
            if(hasEasSvc && hasMtsSvc){
                designationSelectList.add(sp1);
                designationSelectList.add(sp2);
            }else if(hasEasSvc){
                designationSelectList.add(sp1);
            }else if(hasMtsSvc){
                designationSelectList.add(sp2);
            }
            designationSelectList.add(sp3);
        }
        return designationSelectList;
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
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList(), personMap, svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcCgoDtoList(), personMap, svcCode);
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
        SelectOption sp1 = new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new personnel");
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
        List<SelectOption> easAndMtsPremSel = getPremisesSel(appType);

        List<SelectOption> existingOnsitePrem = IaisCommonUtils.genNewArrayList();
        List<SelectOption> existingConvPrem = IaisCommonUtils.genNewArrayList();
        List<SelectOption> existingOffsitePrem = IaisCommonUtils.genNewArrayList();
        List<SelectOption> existingEasOrMtsPrem = IaisCommonUtils.genNewArrayList();
        if (licAppGrpPremisesDtoMap != null && !licAppGrpPremisesDtoMap.isEmpty()) {
            for (AppGrpPremisesDto item : licAppGrpPremisesDtoMap.values()) {
                SelectOption sp= new SelectOption(item.getPremisesSelect(), item.getAddress());
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())) {
                    existingOnsitePrem.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())){
                    existingConvPrem.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(item.getPremisesType())){
                    existingOffsitePrem.add(sp);
                }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(item.getPremisesType())){
                    existingEasOrMtsPrem.add(sp);
                }
            }
        }
        //sort
        doSortSelOption(existingOnsitePrem);
        doSortSelOption(existingConvPrem);
        doSortSelOption(existingOffsitePrem);
        doSortSelOption(existingEasOrMtsPrem);
        premisesSelect.addAll(existingOnsitePrem);
        conveyancePremSel.addAll(existingConvPrem);
        offSitePremSel.addAll(existingOffsitePrem);
        easAndMtsPremSel.addAll(existingEasOrMtsPrem);
        ParamUtil.setSessionAttr(request, "premisesSelect", (Serializable) premisesSelect);
        ParamUtil.setSessionAttr(request, "conveyancePremSel", (Serializable) conveyancePremSel);
        ParamUtil.setSessionAttr(request, "offSitePremSel", (Serializable) offSitePremSel);
        ParamUtil.setSessionAttr(request,"easMtsPremSel", (Serializable) easAndMtsPremSel);
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
                            //break;
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
                            String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                            if(!StringUtil.isEmpty(dupForPerson)){
                                appSvcDocDto.setDupForPerson(dupForPerson);
                                appSvcDocDto.setPersonType(getPsnType(dupForPerson));
                            }
                            //break;
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
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            premKey = IaisCommonUtils.genPremisesKey(appGrpPremisesDto.getEasMtsPostalCode(),appGrpPremisesDto.getEasMtsBlockNo(),appGrpPremisesDto.getEasMtsFloorNo(),appGrpPremisesDto.getEasMtsUnitNo());
        }
        return premKey;
    }

    public static String getPremHci(AppGrpPremisesDto appGrpPremisesDto){
        String premHci = "";
        String premKey = getPremKey(appGrpPremisesDto);
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = appGrpPremisesDto.getHciName()+ premKey;
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = appGrpPremisesDto.getConveyanceHciName() + appGrpPremisesDto.getConveyanceVehicleNo() + premKey;
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = appGrpPremisesDto.getOffSiteHciName() + premKey;
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            premHci = appGrpPremisesDto.getEasMtsHciName() + premKey;
        }
        return premHci;

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
            }else{
                personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
                if(personMap != null){
                    AppSvcPersonAndExtDto personAndExtDto = personMap.get(personKey);
                    AppSvcPrincipalOfficersDto personDto = genAppSvcPrincipalOfficersDto(personAndExtDto,svcCode,true);
                    if (personDto != null) {
                        appSvcPrincipalOfficersDto = personDto;
                    }
                }
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

    public static String getPersonView(String idType, String idNo, String name) {
        return name + ", " + idNo + " (" + MasterCodeUtil.getCodeDesc(idType) + ")";
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
            premisesHci = item.getHciName()+ item.getVehicleNo() + IaisCommonUtils.genPremisesKey(item.getPostalCode(), item.getBlkNo(), item.getFloorNo(), item.getUnitNo());
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(item.getPremisesType())) {
            premisesHci = item.getHciName() + IaisCommonUtils.genPremisesKey(item.getPostalCode(), item.getBlkNo(), item.getFloorNo(), item.getUnitNo());
        } else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(item.getPremisesType())){
            premisesHci = item.getHciName() + IaisCommonUtils.genPremisesKey(item.getPostalCode(), item.getBlkNo(), item.getFloorNo(), item.getUnitNo());
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
            appPsnEditDto.setOtherDesignation(true);
            appPsnEditDto.setSubSpeciality(true);
            appPsnEditDto.setSpecialityOther(true);
            appPsnEditDto.setSpeciality(true);
            appPsnEditDto.setProfRegNo(true);
            appPsnEditDto.setProfessionType(true);
            appPsnEditDto.setQualification(true);
            appPsnEditDto.setOtherQualification(true);
            appPsnEditDto.setProfessionBoard(true);
            appPsnEditDto.setSpecialtyGetDate(true);
            appPsnEditDto.setTypeOfCurrRegi(true);
            appPsnEditDto.setCurrRegiDate(true);
            appPsnEditDto.setPraCerEndDate(true);
            appPsnEditDto.setTypeOfRegister(true);
            appPsnEditDto.setRelevantExperience(true);
            appPsnEditDto.setHoldCerByEMS(true);
            appPsnEditDto.setAclsExpiryDate(true);
            appPsnEditDto.setBclsExpiryDate(true);
            appPsnEditDto.setNoRegWithProfBoard(true);
            appPsnEditDto.setTransportYear(true);

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

    public static String getLicenseeId(HttpServletRequest request){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        return licenseeId;
    }

    public static List<SelectOption> genGiroAccSel(List<OrgGiroAccountInfoDto> orgGiroAccountInfoDtos){
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(orgGiroAccountInfoDtos)){
            for(OrgGiroAccountInfoDto orgGiroAccountInfoDto:orgGiroAccountInfoDtos){
                selectOptionList.add(new SelectOption(orgGiroAccountInfoDto.getAcctNo(), orgGiroAccountInfoDto.getAcctNo()));
            }
        }
        return selectOptionList;
    }

    public static boolean newAndNotRfi(HttpServletRequest request,String appType){
        return !checkIsRfi(request) && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
    }

    public static AppSubmissionDto getOldSubmissionDto(HttpServletRequest request,String appType){
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        AppSubmissionDto appSubmissionDto  = null;
        if(isRfi){
            appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,"oldRenewAppSubmissionDto");
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
            appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        }
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
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
            }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                premisesHciPre = appGrpPremisesDto.getEasMtsHciName() + appGrpPremisesDto.getEasMtsPostalCode() + appGrpPremisesDto.getEasMtsBlockNo();
                premisesHciList.add(premisesHciPre + appGrpPremisesDto.getEasMtsFloorNo() + appGrpPremisesDto.getEasMtsUnitNo());
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
            }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
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

    public static AppGrpPremisesDto getAppGrpPremisesDto(List<AppGrpPremisesDto> appGrpPremisesDtos, String premIndexNo, String premType){
        AppGrpPremisesDto appGrpPremisesDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(AppGrpPremisesDto appGrpPremisesDto1:appGrpPremisesDtos){
                String currPremIndexNo = StringUtil.nullToEmptyStr(appGrpPremisesDto1.getPremisesIndexNo());
                String currPremType = StringUtil.nullToEmpty(appGrpPremisesDto1.getPremisesType());
                if(currPremIndexNo.equals(premIndexNo) && currPremType.equals(premType)){
                    appGrpPremisesDto = appGrpPremisesDto1;
                    break;
                }
            }
        }
        return appGrpPremisesDto;
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
                url= GatewayPayNowAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getPayNowRetUrl());break;
            default:
                url= GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, request, pmtReturnUrlDto.getOtherRetUrl());
        }
        return url;
    }

    public static List<AppGrpPrimaryDocDto> getAppGrpprimaryDocDto(String docId,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos){
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(docId)){
            for(AppGrpPrimaryDocDto docDto:appGrpPrimaryDocDtos){
                if(docDto.getSvcComDocId().equals(docId)){
                    appGrpPrimaryDocDtoList.add(docDto);
                }
            }
        }
        return appGrpPrimaryDocDtoList;
    }

    public static AppGrpPrimaryDocDto genEmptyPrimaryDocDto(String docConfigId){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
        appGrpPrimaryDocDto.setSvcComDocId(docConfigId);
        appGrpPrimaryDocDto.setSeqNum(-1);
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
        log.debug(StringUtil.changeForLog("remove Premise Empty Align Info start ..."));
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            log.debug(StringUtil.changeForLog("appGrpPremisesDtoList is empty ..."));
        }
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
                //remove empty align business info
                List<AppSvcBusinessDto> appSvcBusinessDtos = NewApplicationHelper.removeEmptyAlignBusiness(appGrpPremisesDtoList,appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcBusinessDtoList(appSvcBusinessDtos);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        log.debug(StringUtil.changeForLog("remove Premise Empty Align Info end ..."));
    }


    public static void updatePremisesAddress(AppSubmissionDto appSubmissionDto){
        log.debug(StringUtil.changeForLog("update Premise Address start ..."));
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)){
                    for(AppSvcLaboratoryDisciplinesDto laboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtos){
                        AppGrpPremisesDto appGrpPremisesDto = NewApplicationHelper.getAppGrpPremisesDto(appGrpPremisesDtoList,laboratoryDisciplinesDto.getPremiseVal(),laboratoryDisciplinesDto.getPremiseType());
                        if(appGrpPremisesDto != null){
                            laboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                        }
                    }
                }
                List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcBusinessDtos)){
                    for(AppSvcBusinessDto appSvcBusinessDto:appSvcBusinessDtos){
                        AppGrpPremisesDto appGrpPremisesDto = NewApplicationHelper.getAppGrpPremisesDto(appGrpPremisesDtoList, appSvcBusinessDto.getPremIndexNo(), appSvcBusinessDto.getPremType());
                        if(appGrpPremisesDto != null){
                            appSvcBusinessDto.setPremAddress(appGrpPremisesDto.getAddress());
                        }
                    }
                }
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        log.debug(StringUtil.changeForLog("update Premise Address end ..."));
    }


    public static void svcDocMandatoryValidate(List<HcsaSvcDocConfigDto> svcDocConfigDtos, List<AppSvcDocDto> appSvcDocDtos,List<AppGrpPremisesDto> appGrpPremisesDtos, AppSvcRelatedInfoDto appSvcRelatedInfoDto,Map<String, String> errorMap) {

        String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
        if(!IaisCommonUtils.isEmpty(svcDocConfigDtos)){
            int i = 0;
            String suffix = "Error";
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:svcDocConfigDtos){
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                String configId = hcsaSvcDocConfigDto.getId();
                String errKey = i+ "svcDoc"+ appSvcRelatedInfoDto.getServiceCode();
                Boolean isMandatory = hcsaSvcDocConfigDto.getIsMandatory();
                i++;
                if(!isMandatory){
                    continue;
                }
                boolean mandatoryFlag;
                if(isMandatory){
                    mandatoryFlag = false;
                }else{
                    mandatoryFlag = true;
                }
                if (IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                    appSvcDocDtos = IaisCommonUtils.genNewArrayList();
                }
                if("0".equals(dupForPrem)){
                    if(StringUtil.isEmpty(dupForPerson)){
                        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                            String svcDocId = appSvcDocDto.getSvcDocId();
                            if (hcsaSvcDocConfigDto.getId().equals(svcDocId)) {
                                mandatoryFlag = true;
                                break;
                            }
                        }
                        if (!mandatoryFlag) {
                            errorMap.put(errKey+suffix, err006);
                        }
                    }else{
                        List<AppSvcPrincipalOfficersDto> psnDtoList = NewApplicationHelper.getPsnByDupForPerson(appSvcRelatedInfoDto,dupForPerson);
                        for(AppSvcPrincipalOfficersDto psnDto:psnDtoList){
                            String psnIndexNo = psnDto.getCgoIndexNo();
                            AppSvcDocDto appSvcDocDto = getSvcDtoByConfigIdAndPsnIndexNo(appSvcDocDtos,configId,"","",psnIndexNo);
                            if(appSvcDocDto == null){
                                String specErrKey = errKey + psnIndexNo +suffix;
                                errorMap.put(specErrKey, err006);
                            }
                        }
                    }
                }else if("1".equals(dupForPrem)){
                    if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                            String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                            if(StringUtil.isEmpty(dupForPerson)){
                                AppSvcDocDto appSvcDocDto = getSvcDtoByConfigIdAndPsnIndexNo(appSvcDocDtos,configId,premIndexNo,appGrpPremisesDto.getPremisesType(),"");
                                if(appSvcDocDto == null){
                                    String specErrKey = errKey + premIndexNo + suffix;
                                    errorMap.put(specErrKey, err006);
                                }
                            }else{
                                List<AppSvcPrincipalOfficersDto> psnDtoList = NewApplicationHelper.getPsnByDupForPerson(appSvcRelatedInfoDto,dupForPerson);
                                for(AppSvcPrincipalOfficersDto psnDto:psnDtoList){
                                    String psnIndexNo = psnDto.getCgoIndexNo();
                                    AppSvcDocDto appSvcDocDto = getSvcDtoByConfigIdAndPsnIndexNo(appSvcDocDtos,configId,appGrpPremisesDto.getPremisesIndexNo(),appGrpPremisesDto.getPremisesType(),psnIndexNo);
                                    if(appSvcDocDto == null){
                                            String specErrKey = errKey + premIndexNo + psnIndexNo +suffix;
                                        errorMap.put(specErrKey, err006);
                                    }
                                }
                            }
                        }
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
            Iterator<String> iterator= Arrays.stream(ars).iterator();
            if(iterator.hasNext()){
                general_err0041=general_err0041.replace("{field}",iterator.next());
            }
            if(iterator.hasNext()){
                general_err0041=general_err0041.replace("{maxlength}",iterator.next());
            }

            return general_err0041;
        }else if(length==3){
            Iterator<String> iterator= Arrays.stream(ars).iterator();
            String ars0=iterator.hasNext()?iterator.next():"";
            String ars1=iterator.hasNext()?iterator.next():"";
            String messageDesc = MessageUtil.getMessageDesc(ars0);
            messageDesc=messageDesc.replace("{field}",ars0);
            messageDesc=messageDesc.replace("{maxlength}",ars1);
            return messageDesc;
        }else if(length==4){
            Iterator<String> iterator= Arrays.stream(ars).iterator();
            String ars0=iterator.hasNext()?iterator.next():"";
            String ars1=iterator.hasNext()?iterator.next():"";
            String ars2=iterator.hasNext()?iterator.next():"";
            String ars3=iterator.hasNext()?iterator.next():"";
            general_err0041=general_err0041.replace(ars0,ars1);
            general_err0041=general_err0041.replace(ars2,ars3);
            return general_err0041;
        }else if(length==5){
            Iterator<String> iterator= Arrays.stream(ars).iterator();
            String ars0=iterator.hasNext()?iterator.next():"";
            String messageDesc = MessageUtil.getMessageDesc(ars0);
            if(messageDesc!=null){
                String ars1=iterator.hasNext()?iterator.next():"";
                String ars2=iterator.hasNext()?iterator.next():"";
                String ars3=iterator.hasNext()?iterator.next():"";
                String ars4=iterator.hasNext()?iterator.next():"";
                messageDesc=messageDesc.replace(ars1,ars2);
                messageDesc=messageDesc.replace(ars3,ars4);
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
                        .append('\"');
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

    public static List<AppSvcPrincipalOfficersDto> getPsnByDupForPerson(AppSvcRelatedInfoDto appSvcRelatedInfoDto,String dupForPerson){
        List<AppSvcPrincipalOfficersDto> psnDtoList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> svcPsnDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        switch(dupForPerson){
            case ApplicationConsts.DUP_FOR_PERSON_CGO:
                List<AppSvcPrincipalOfficersDto> cgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                psnDtoList = NewApplicationHelper.transferCgoToPsnDtoList(cgoDtos);
                break;
            case ApplicationConsts.DUP_FOR_PERSON_PO:
                if(!IaisCommonUtils.isEmpty(svcPsnDtoList)){
                    for(AppSvcPrincipalOfficersDto svcPsnDto:svcPsnDtoList){
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(svcPsnDto.getPsnType())){
                            psnDtoList.add(svcPsnDto);
                        }
                    }
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_DPO:
                if(!IaisCommonUtils.isEmpty(svcPsnDtoList)){
                    for(AppSvcPrincipalOfficersDto svcPsnDto:svcPsnDtoList){
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(svcPsnDto.getPsnType())){
                            psnDtoList.add(svcPsnDto);
                        }
                    }
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_MAP:
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDto.getAppSvcMedAlertPersonList())){
                    psnDtoList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                List<AppSvcPersonnelDto> spDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
                if(!IaisCommonUtils.isEmpty(spDtos)){
                    for(AppSvcPersonnelDto spDto:spDtos){
                        AppSvcPrincipalOfficersDto psnDto = new AppSvcPrincipalOfficersDto();
                        psnDto.setCgoIndexNo(spDto.getCgoIndexNo());
                        psnDtoList.add(psnDto);
                    }
                }
                break;
            default:
                break;
        }
        return psnDtoList;
    }


    public static Map<String,List<AppGrpPrimaryDocDto>> genPrimaryDocReloadMap (List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos, List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos){
        Map<String,List<AppGrpPrimaryDocDto>> reloadMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos) && !IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocConfigDtos) {
                String configId = hcsaSvcDocConfigDto.getId();
                String configTitle = hcsaSvcDocConfigDto.getDocTitle();
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if("0".equals(dupForPrem)){
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = getAppGrpPrimaryDocDtoByConfigId(appGrpPrimaryDocDtos,configId,"");
                    setPrimaryDocDisplayTitle(appGrpPrimaryDocDtos1,configTitle);
                    reloadMap.put(configId,appGrpPrimaryDocDtos1);
                }else if("1".equals(dupForPrem)){
                    int premCount = 1;
                    String premTitleTemplate = "Premises ${premCount}: ${configTitle}";
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = getAppGrpPrimaryDocDtoByConfigId(appGrpPrimaryDocDtos,configId,appGrpPremisesDto.getPremisesIndexNo());
                        String displayTitle = premTitleTemplate.replace("${premCount}",String.valueOf(premCount)).replace("${configTitle}",configTitle);
                        setPrimaryDocDisplayTitle(appGrpPrimaryDocDtos1,displayTitle);
                        reloadMap.put(appGrpPremisesDto.getPremisesIndexNo()+configId,appGrpPrimaryDocDtos1);
                        premCount++;
                    }
                }
            }
        }
        //do sort
        if(!IaisCommonUtils.isEmpty(reloadMap)){
            reloadMap.forEach((k,v)->{
                if(v != null && v.size() > 1){
                    Collections.sort(v,(s1,s2)->s1.getSeqNum().compareTo(s2.getSeqNum()));
                }
            });
        }

        return reloadMap;
    }

    public static Map<String,List<AppSvcDocDto>> genSvcDocReloadMap(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,List<AppGrpPremisesDto> appGrpPremisesDtos,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        Map<String,List<AppSvcDocDto>> reloadMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && appSvcRelatedInfoDto != null && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocConfigDtos) {
                String configId = hcsaSvcDocConfigDto.getId();
                String configTitle = hcsaSvcDocConfigDto.getDocTitle();
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                if("0".equals(dupForPrem)){
                    setSvcDocDisplayTitle(dupForPrem,0,"",dupForPerson,configId,configTitle,appSvcDocDtos,appSvcRelatedInfoDto,reloadMap);
                }else if("1".equals(dupForPrem)){
                    int premCount = 1;
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        setSvcDocDisplayTitle(dupForPrem,premCount,appGrpPremisesDto.getPremisesIndexNo(),dupForPerson,configId,configTitle,appSvcDocDtos,appSvcRelatedInfoDto,reloadMap);
                        premCount++;
                    }
                }
            }
        }
        //do sort
        if(!IaisCommonUtils.isEmpty(reloadMap)){
            reloadMap.forEach((k,v)->{
                if(v != null && v.size() > 1){
                    Collections.sort(v,(s1,s2)->s1.getSeqNum().compareTo(s2.getSeqNum()));
                }
            });
        }
        return reloadMap;
    }
    //for single premises
    public static void addPremAlignForPrimaryDoc(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,List<AppGrpPremisesDto> appGrpPremisesDtos){
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(HcsaSvcDocConfigDto config:hcsaSvcDocConfigDtos){
                if("1".equals(config.getDupForPrem())){
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = NewApplicationHelper.getAppGrpprimaryDocDto(config.getId(),appGrpPrimaryDocDtos);
                    if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList) && appGrpPremisesDtos != null && appGrpPremisesDtos.size() > 0){
                        String premIndex = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                        String premType = appGrpPremisesDtos.get(0).getPremisesType();
                        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                            appGrpPrimaryDocDto.setPremisessName(premIndex);
                            appGrpPrimaryDocDto.setPremisessType(premType);
                        }
                    }
                }
            }
        }
    }

    //for single premises
    public static void addPremAlignForSvcDoc(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,List<AppSvcDocDto> appSvcDocDtos,List<AppGrpPremisesDto> appGrpPremisesDtos){
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(appSvcDocDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(HcsaSvcDocConfigDto config:hcsaSvcDocConfigDtos){
                if("1".equals(config.getDupForPrem())){
                    List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos,config.getId());
                    if(!IaisCommonUtils.isEmpty(appSvcDocDtoList) && appGrpPremisesDtos != null && appGrpPremisesDtos.size() > 0){
                        String premIndex = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                        String premType = appGrpPremisesDtos.get(0).getPremisesType();
                        for(AppSvcDocDto appSvcDocDto:appSvcDocDtoList){
                            appSvcDocDto.setPremisesType(premType);
                            appSvcDocDto.setPremisesVal(premIndex);
                        }
                    }
                }
            }
        }
    }

    public static void setDupForPersonAttr(HttpServletRequest request,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        if(appSvcRelatedInfoDto != null){
            ParamUtil.setRequestAttr(request, ClinicalLaboratoryDelegator.GOVERNANCEOFFICERSDTOLIST, appSvcRelatedInfoDto.getAppSvcCgoDtoList());
            List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
            assignPoDpoDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(),principalOfficersDtos,deputyPrincipalOfficersDtos);
            ParamUtil.setRequestAttr(request, "ReloadPrincipalOfficers", principalOfficersDtos);
            ParamUtil.setRequestAttr(request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);
            ParamUtil.setRequestAttr(request, "AppSvcMedAlertPsn", appSvcRelatedInfoDto.getAppSvcMedAlertPersonList());
            ParamUtil.setRequestAttr(request,"AppSvcPersonnelDtoList",appSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
        }
    }

    public static String genMutilSelectOpHtml(Map<String,String> attrMap, List<SelectOption> selectOptionList, String firestOption, List<String> checkedVals, boolean multiSelect){
        StringBuilder sBuffer = new StringBuilder(100);
        sBuffer.append("<select ");
        if(multiSelect){
            sBuffer.append("multiple=\"multiple\" ");
        }
        for(Map.Entry<String, String> entry : attrMap.entrySet()){
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
            if(!IaisCommonUtils.isEmpty(checkedVals)){
                if(checkedVals.contains(sp.getValue())){
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
        return sBuffer.toString();
    }

    public static String getPsnType(String dupForPerson){
        String psnType = "common";
        if(!StringUtil.isEmpty(dupForPerson)){
            switch(dupForPerson){
                case ApplicationConsts.DUP_FOR_PERSON_CGO:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_CGO;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_PO:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_PO;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_DPO:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_DPO;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_MAP:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_MAP;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL;
                    break;
                default:
                    break;
            }
        }
        return psnType;
    }

    public static List<AppSvcDocDto> getSvcDocumentByParams(List<AppSvcDocDto> appSvcDocDtos,String configId,String premIndex,String psnIndex){
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)){
            appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos,configId,premIndex,psnIndex);
        }
        return appSvcDocDtoList;
    }

    public static HcsaSvcDocConfigDto getHcsaSvcDocConfigDtoById(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos, String id){
        HcsaSvcDocConfigDto result = null;
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !StringUtil.isEmpty(id)){
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocConfigDtos){
                if(id.equals(hcsaSvcDocConfigDto.getId())){
                    result = hcsaSvcDocConfigDto;
                    break;
                }
            }
        }
        return result;
    }

    public static void assignPoDpoDto(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos, List<AppSvcPrincipalOfficersDto> principalOfficersDtos, List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos){
        if(!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)){
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
    }

    public static Map<String,String> psnMandatoryValidate(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList, String psnType, Map<String,String> errMap,int psnLength,String errName,String psnName){
        int mandatoryCount = getManDatoryCountByPsnType(hcsaSvcPersonnelList,psnType);
        if(psnLength < mandatoryCount){
            String mandatoryErrMsg = MessageUtil.getMessageDesc("NEW_ERR0025");
            mandatoryErrMsg = mandatoryErrMsg.replace("{psnType}",psnName);
            mandatoryErrMsg = mandatoryErrMsg.replace("{mandatoryCount}",String.valueOf(mandatoryCount));
            errMap.put(errName,mandatoryErrMsg);
        }
        return errMap;
    }

    public static boolean isMultiPremService(List<HcsaServiceDto> hcsaServiceDtos){
        boolean flag = true;
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                String svcCode = hcsaServiceDto.getSvcCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(svcCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(svcCode)){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static List<HcsaServiceDto> sortHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtoList) {
        List<HcsaServiceDto> baseList = new ArrayList();
        List<HcsaServiceDto> specifiedList = new ArrayList();
        List<HcsaServiceDto> subList = new ArrayList();
        List<HcsaServiceDto> otherList = new ArrayList();
        //class
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
            switch (hcsaServiceDto.getSvcType()) {
                case ApplicationConsts.SERVICE_CONFIG_TYPE_BASE:
                    baseList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED:
                    subList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED:
                    specifiedList.add(hcsaServiceDto);
                    break;
                default:
                    otherList.add(hcsaServiceDto);
                    break;
            }
        }
        //Sort
        sortService(baseList);
        sortService(specifiedList);
        sortService(subList);
        sortService(otherList);
        hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
        hcsaServiceDtoList.addAll(baseList);
        hcsaServiceDtoList.addAll(specifiedList);
        hcsaServiceDtoList.addAll(subList);
        hcsaServiceDtoList.addAll(otherList);
        return hcsaServiceDtoList;
    }

    public static List<SelectOption> genDesignationOpList(boolean needOthers){
        List<SelectOption> idTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        return idTypeSelectList;
    }

    //=============================================================================
    //private method
    //=============================================================================

    private static void sortService(List<HcsaServiceDto> list) {
        list.sort((h1, h2) -> h1.getSvcName().compareTo(h2.getSvcName()));
    }

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

    private static List<AppSvcBusinessDto> removeEmptyAlignBusiness(List<AppGrpPremisesDto> appGrpPremisesDtoList,AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcBusinessDtos)){
            List<AppSvcBusinessDto> newBusinessDtos = IaisCommonUtils.genNewArrayList();
            for(AppSvcBusinessDto appSvcBusinessDto:newBusinessDtos){
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    if (appGrpPremisesDto.getPremisesIndexNo().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                        newBusinessDtos.add(appSvcBusinessDto);
                        break;
                    }
                }
            }
        }
        return appSvcBusinessDtos;
    }

    private static List<SelectOption> getPremisesSel(String appType){
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        SelectOption cps1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        selectOptionList.add(cps1);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            SelectOption cps2 = new SelectOption("newPremise", "Moving to a new address");
            selectOptionList.add(cps2);
        }else{
            SelectOption cps2 = new SelectOption("newPremise", "Add a new mode of service delivery");
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

    private static AppSvcPrincipalOfficersDto transferCgoToPsnDto(AppSvcPrincipalOfficersDto appSvcCgoDto){
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

    private static void syncPsnDto(List<AppSvcPrincipalOfficersDto> appSvcCgoDtos, Map<String,AppSvcPersonAndExtDto> personMap, String svcCode){
        if(IaisCommonUtils.isEmpty(appSvcCgoDtos) || personMap == null || StringUtil.isEmpty(svcCode)){
            return;
        }
        for (AppSvcPrincipalOfficersDto person : appSvcCgoDtos) {
            boolean isLicPsn = person.isLicPerson();
            String personKey = getPersonKey(person.getIdType(), person.getIdNo());
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
                person.setDesignation(selPerson.getDesignation());
                person.setOtherDesignation(selPerson.getOtherDesignation());
                String professionType = selPerson.getProfessionType();
                if(!StringUtil.isEmpty(professionType)){
                    person.setProfessionType(professionType);
                }
                String profRegNo = selPerson.getProfRegNo();
                if(!StringUtil.isEmpty(profRegNo)){
                    person.setProfRegNo(profRegNo);
                }
                String speciality = selPerson.getSpeciality();
                if(!StringUtil.isEmpty(speciality)){
                    person.setSpeciality(speciality);
                }
                String specialityOther = selPerson.getSpecialityOther();
                if(!StringUtil.isEmpty(specialityOther)){
                    person.setSpecialityOther(specialityOther);
                }
                String subSpeciality = selPerson.getSubSpeciality();
                if(!StringUtil.isEmpty(subSpeciality)){
                    person.setSubSpeciality(subSpeciality);
                }
                String qualification = selPerson.getQualification();
                if(!StringUtil.isEmpty(qualification)){
                    person.setQualification(qualification);
                }
                String otherQualification = selPerson.getOtherQualification();
                if(!StringUtil.isEmpty(otherQualification)){
                    person.setOtherQualification(otherQualification);
                }
                String officeTelNo = selPerson.getOfficeTelNo();
                if (!StringUtil.isEmpty(officeTelNo)) {
                    person.setOfficeTelNo(officeTelNo);
                }

                person.setNeedSpcOptList(selPerson.isNeedSpcOptList());
                List<SelectOption> spcOptList = selPerson.getSpcOptList();
                if(!IaisCommonUtils.isEmpty(spcOptList)){
                    person.setSpcOptList(selPerson.getSpcOptList());
                }
                String specHtml = selPerson.getSpecialityHtml();
                if(!StringUtil.isEmpty(specHtml)){
                    person.setSpecialityHtml(specHtml);
                }
                String professionBoard = selPerson.getProfessionBoard();
                if (!StringUtil.isEmpty(professionBoard)) {
                    person.setProfessionBoard(professionBoard);
                }
                Date specialtyGetDate = selPerson.getSpecialtyGetDate();
                if (specialtyGetDate != null) {
                    person.setSpecialtyGetDate(specialtyGetDate);
                }
                String specialtyGetDateStr = selPerson.getSpecialtyGetDateStr();
                if (!StringUtil.isEmpty(specialtyGetDateStr)) {
                    person.setSpecialtyGetDateStr(specialtyGetDateStr);
                }
                String typeOfCurrRegi = selPerson.getTypeOfCurrRegi();
                if (!StringUtil.isEmpty(typeOfCurrRegi)) {
                    person.setTypeOfCurrRegi(typeOfCurrRegi);
                }
                Date currRegiDate = selPerson.getCurrRegiDate();
                if (currRegiDate != null) {
                    person.setCurrRegiDate(currRegiDate);
                }
                String currRegiDateStr = selPerson.getCurrRegiDateStr();
                if (!StringUtil.isEmpty(currRegiDateStr)) {
                    person.setCurrRegiDateStr(currRegiDateStr);
                }
                Date praCerEndDate = selPerson.getPraCerEndDate();
                if (praCerEndDate != null) {
                    person.setPraCerEndDate(praCerEndDate);
                }
                String praCerEndDateStr = selPerson.getPraCerEndDateStr();
                if (!StringUtil.isEmpty(praCerEndDateStr)) {
                    person.setPraCerEndDateStr(praCerEndDateStr);
                }
                String typeOfRegister = selPerson.getTypeOfRegister();
                if (!StringUtil.isEmpty(typeOfRegister)) {
                    person.setTypeOfRegister(typeOfRegister);
                }
                String relevantExperience = selPerson.getRelevantExperience();
                if (!StringUtil.isEmpty(relevantExperience)) {
                    person.setRelevantExperience(relevantExperience);
                }
                String holdCerByEMS = selPerson.getHoldCerByEMS();
                if (!StringUtil.isEmpty(holdCerByEMS)) {
                    person.setHoldCerByEMS(holdCerByEMS);
                }
                Date aclsExpiryDate = selPerson.getAclsExpiryDate();
                if (aclsExpiryDate != null) {
                    person.setAclsExpiryDate(aclsExpiryDate);
                }
                String aclsExpiryDateStr = selPerson.getAclsExpiryDateStr();
                if (!StringUtil.isEmpty(aclsExpiryDateStr)) {
                    person.setAclsExpiryDateStr(aclsExpiryDateStr);
                }
                //set lic person info
                person.setLicPerson(isLicPsn);
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
                String errMsg=MessageUtil.getMessageDesc("NEW_ERR0021");
                if(selectAllDay ||selectAllDay1){
                    errorMap.put(errorId+index+j,errMsg);
                    continue;
                }
                int time = getTime(list.get(i).getEndToHH(), list.get(i).getEndToMM());
                int   time1 = getTime(list.get(j).getStartFromHH(), list.get(j).getStartFromMM());
                if(time>=time1){
                    errorMap.put(errorId+index+j,errMsg);
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

    private static AppSvcDocDto getSvcDtoByConfigIdAndPsnIndexNo(List<AppSvcDocDto> appSvcDocDtos,String configId,String premIndexNo,String premType,String psnIndexNo){
        AppSvcDocDto appSvcDocDto = null;
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
            for(AppSvcDocDto appSvcDocDto1:appSvcDocDtos){
                String currConfigId = appSvcDocDto1.getSvcDocId();
                String cuurPremIndex = appSvcDocDto1.getPremisesVal();
                if(StringUtil.isEmpty(cuurPremIndex)){
                    cuurPremIndex = "";
                }
                String currPsnIndex = appSvcDocDto1.getPsnIndexNo();
                if(StringUtil.isEmpty(currPsnIndex)){
                    currPsnIndex = "";
                }
                String currPremType = appSvcDocDto1.getPremisesType();
                if(StringUtil.isEmpty(currPremType)){
                    currPremType = "";
                }
                if(currConfigId.equals(configId)
                        && cuurPremIndex.equals(premIndexNo)
                        && currPsnIndex.equals(psnIndexNo)
                        && currPremType.equals(premType)){
                    appSvcDocDto = appSvcDocDto1;
                    break;
                }
            }
        }

        return appSvcDocDto;
    }

    private static List<AppGrpPrimaryDocDto> getAppGrpPrimaryDocDtoByConfigId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String configId,String premIndex){
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !StringUtil.isEmpty(configId)){
            if(StringUtil.isEmpty(premIndex)){
                premIndex = "";
            }
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                String currPremIndex = appGrpPrimaryDocDto.getPremisessName();
                if(StringUtil.isEmpty(currPremIndex)){
                    currPremIndex ="";
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId()) && configId.equals(appGrpPrimaryDocDto.getSvcComDocId()) && premIndex.equals(currPremIndex)){
                    appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                }
            }
        }
        return appGrpPrimaryDocDtoList;
    }

    private static void setPrimaryDocDisplayTitle(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String displayTitle){
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !StringUtil.isEmpty(displayTitle)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                appGrpPrimaryDocDto.setDisplayTitle(displayTitle);
            }
        }
    }

    private static List<AppSvcDocDto> getAppSvcDocDtoByConfigId(List<AppSvcDocDto> appSvcDocDtos,String configId,String premIndex,String psnIndex){
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)){
            if(StringUtil.isEmpty(premIndex)){
                premIndex = "";
            }
            if(StringUtil.isEmpty(psnIndex)){
                psnIndex = "";
            }
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                String currPremIndex = appSvcDocDto.getPremisesVal();
                if(StringUtil.isEmpty(currPremIndex)){
                    currPremIndex = "";
                }
                String currPsnIndex = appSvcDocDto.getPsnIndexNo();
                if(StringUtil.isEmpty(currPsnIndex)){
                    currPsnIndex = "";
                }
                if(configId.equals(appSvcDocDto.getSvcDocId()) && premIndex.equals(currPremIndex) && psnIndex.equals(currPsnIndex)){
                    appSvcDocDtoList.add(appSvcDocDto);
                }
            }
        }
        return appSvcDocDtoList;
    }

    private static List<AppSvcDocDto> getAppSvcDocDtoByConfigId(List<AppSvcDocDto> appSvcDocDtos,String configId){
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)){
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                if(configId.equals(appSvcDocDto.getSvcDocId())){
                    appSvcDocDtoList.add(appSvcDocDto);
                }
            }
        }
        return appSvcDocDtoList;
    }

    private static void setSvcDocDisplayTitle(List<AppSvcDocDto> appSvcDocDtos,String displayTitle){
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(displayTitle)){
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                appSvcDocDto.setDisplayTitle(displayTitle);
            }
        }
    }

    private static String getDupForPersonName(String dupForPerson){
        String psnName = "";
        switch(dupForPerson){
            case ApplicationConsts.DUP_FOR_PERSON_CGO:
                psnName = "Clinical Governance Officer";
                break;
            case ApplicationConsts.DUP_FOR_PERSON_PO:
                psnName = "Principal Officer";
                break;
            case ApplicationConsts.DUP_FOR_PERSON_DPO:
                psnName = "Nominee";
                break;
            case ApplicationConsts.DUP_FOR_PERSON_MAP:
                psnName = "MedAlert Person";
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                psnName = "Service Personnel";
                break;
            default:
                break;
        }
        return psnName;
    }

    private static void setSvcDocDisplayTitle(String dupForPrem,int premCount,String premIndex,String dupForPerson,
                                              String configId,String configTitle,List<AppSvcDocDto> appSvcDocDtos,
                                              AppSvcRelatedInfoDto appSvcRelatedInfoDto,Map<String,List<AppSvcDocDto>> reloadMap){
        String titleTemplate = "${prem}${psn}"+configTitle;
        String reloadKey;
        if("1".equals(dupForPrem)){
            titleTemplate = titleTemplate.replace("${prem}","Premises "+premCount+": ");
            reloadKey = premIndex + configId;
        }else{
            titleTemplate = titleTemplate.replace("${prem}","");
            reloadKey = configId;
        }

        if(StringUtil.isEmpty(dupForPerson)){
            List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos,configId,premIndex,"");
            titleTemplate = titleTemplate.replace("${psn}","");
            setSvcDocDisplayTitle(appSvcDocDtoList,titleTemplate);
            reloadMap.put(reloadKey,appSvcDocDtoList);
        }else{
            String psnName = getDupForPersonName(dupForPerson);
            List<AppSvcPrincipalOfficersDto> psnList = getPsnByDupForPerson(appSvcRelatedInfoDto,dupForPerson);
            int psnCount = 1;
            for(AppSvcPrincipalOfficersDto psn:psnList){
                String psnIndex = psn.getCgoIndexNo();
                String displayTitle = titleTemplate.replace("${psn}",psnName+" "+psnCount+": ");
                List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos,configId,premIndex,psnIndex);
                setSvcDocDisplayTitle(appSvcDocDtoList,displayTitle);
                reloadMap.put(reloadKey+psnIndex,appSvcDocDtoList);
                psnCount++;
            }
        }

    }




    private static int getManDatoryCountByPsnType(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, String psnType){
        int mandatoryCount = 0;
        if(!IaisCommonUtils.isEmpty(hcsaSvcPersonnelDtos)){
            for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtos){
                if(hcsaSvcPersonnelDto.getPsnType().equals(psnType)){
                    mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
                    break;
                }
            }
        }
        return  mandatoryCount;
    }

    public static List<SelectOption> getReasonOption() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, "Not Profitable");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, "Retiring");
        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_REASON_OTHER, "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }
    public static List<SelectOption> getPatientsOption() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, "HCI");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, "Professional Regn. No.");
        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER, "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    public static String getRelatedAppId(String appId, String licenceId) {
        String orgAppId = appId;
        if (StringUtil.isEmpty(appId) && StringUtil.isNotEmpty(licenceId)) {
            LicenceClient licenceClient = SpringContextHelper.getContext().getBean(LicenceClient.class);
            List<LicAppCorrelationDto> licAppCorrDtos = licenceClient.getLicCorrBylicId(licenceId).getEntity();
            orgAppId = Optional.ofNullable(licAppCorrDtos).map(dtos -> dtos.get(0).getApplicationId()).orElseGet(() -> null);
        }
        return orgAppId;
    }

}
