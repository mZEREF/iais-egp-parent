package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import sop.util.CopyUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * NewApplicationHelper
 *
 * @author suocheng
 * @date 2/24/2020
 */

public class NewApplicationHelper {
    public static Map<String,String> doValidateLaboratory(List<AppSvcChckListDto>  listDtos, String serviceId){
        Map<String,String> map=IaisCommonUtils.genNewHashMap();
        int count=0;

        if(listDtos.isEmpty()){
            /*   map.put("checkError","UC_CHKLMD001_ERR002");*/
        }else {
            for(int i=0;i<listDtos.size();i++){
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

        return map;

    }
    public static Map<String,String> doValidateGovernanceOfficers(List<AppSvcCgoDto> appSvcCgoList){

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
                if("-1".equals(idTyp)){
                    errMap.put("idTyp"+i, "UC_CHKLMD001_ERR001");
                }
                String salutation = appSvcCgoList.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    errMap.put("salutation"+i,"UC_CHKLMD001_ERR001");
                }
                String speciality = appSvcCgoList.get(i).getSpeciality();
                if("-1".equals(speciality)){
                    errMap.put("speciality"+i,"UC_CHKLMD001_ERR001");
                }
                String professionType = appSvcCgoList.get(i).getProfessionType();
                if(StringUtil.isEmpty(professionType)){
                    errMap.put("professionType"+i,"UC_CHKLMD001_ERR001");
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if(StringUtil.isEmpty(designation)){
                    errMap.put("designation"+i,"UC_CHKLMD001_ERR001");
                }
                String professionRegoNo = appSvcCgoList.get(i).getProfessionRegoNo();
                if(StringUtil.isEmpty(professionRegoNo)){
                    errMap.put("professionRegoNo"+i,"UC_CHKLMD001_ERR001");
                }
                String idNo = appSvcCgoList.get(i).getIdNo();
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

                    }
                    if("NRIC".equals(idTyp)){
                        boolean b1 = SgNoValidator.validateNric(idNo);
                        if(!b1){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                        stringBuilder1.append(idTyp).append(idNo);

                    }


                }
                //to do

                String Specialty = appSvcCgoList.get(i).getSpeciality();
                if (StringUtil.isEmpty(Specialty)) {
                    errMap.put("speciality"+i, "UC_CHKLMD001_ERR001");
                }

                String specialty = appSvcCgoList.get(i).getSpeciality();
                if(StringUtil.isEmpty(specialty)){
                    errMap.put("specialty"+i, "UC_CHKLMD001_ERR001");
                }
                String name = appSvcCgoList.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errMap.put("name"+i,"UC_CHKLMD001_ERR001");
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
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        errMap.put("emailAddr"+i, "CHKLMD001_ERR006");
                    }
                }


                if(!StringUtil.isEmpty(stringBuilder1.toString())){
                    if(stringList.contains(stringBuilder1.toString())){
                        errMap.put("idNo","UC_CHKLMD001_ERR002");
                    }else {
                        stringList.add( stringBuilder1.toString());
                    }
                }

            }

        }
        return errMap;
    }

    public static  List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
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
                }
            }
        }
        //todo:change place
        Object rfi = ParamUtil.getSessionAttr(request,NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                || rfi != null){
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
            ParamUtil.setSessionAttr(request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
        }
        return appSubmissionDto;
    }
    //todo change
    public static Map<String,  String> doValidatePo(List<AppSvcPrincipalOfficersDto> poDto) {
        Map<String, String> oneErrorMap = IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder =new StringBuilder();
        int poIndex=0;
        int dpoIndex=0;
        if(IaisCommonUtils.isEmpty(poDto)){
            return oneErrorMap;
        }
        for (int i=0;i< poDto.size();i++) {
            StringBuilder stringBuilder1 =new StringBuilder();
            String psnType = poDto.get(i).getPsnType();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect)) {
                    oneErrorMap.put("assignSelect"+i, "UC_CHKLMD001_ERR001");
                } else {
                    //do by wenkang
                    String mobileNo = poDto.get(i).getMobileNo();
                    String officeTelNo = poDto.get(i).getOfficeTelNo();
                    String emailAddr = poDto.get(i).getEmailAddr();
                    String idNo = poDto.get(i).getIdNo();
                    String name = poDto.get(i).getName();
                    String salutation = poDto.get(i).getSalutation();
                    String designation = poDto.get(i).getDesignation();
                    String idType = poDto.get(i).getIdType();

                    if("-1".equals(idType)){
                        oneErrorMap.put("idType"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(name)){
                        oneErrorMap.put("name"+poIndex,"UC_CHKLMD001_ERR001");
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
                                oneErrorMap.put("NRICFIN","CHKLMD001_ERR005");
                            }else {
                                stringBuilder1.append(idType).append(idNo);
                            }
                        }
                        if("NRIC".equals(idType)){
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if(!b1){
                                oneErrorMap.put("NRICFIN","CHKLMD001_ERR005");
                            }else {
                                stringBuilder1.append(idType).append(idNo);
                            }
                        }
                    }else {
                        oneErrorMap.put("NRICFIN","UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(mobileNo)){
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo"+poIndex, "CHKLMD001_ERR004");
                        }
                    }else {
                        oneErrorMap.put("mobileNo"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(emailAddr)) {
                        if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                            oneErrorMap.put("emailAddr"+poIndex, "CHKLMD001_ERR006");
                        }
                    }else {
                        oneErrorMap.put("emailAddr"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(officeTelNo)) {
                        if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                            oneErrorMap.put("officeTelNo"+poIndex, "CHKLMD001_ERR007");
                        }
                    }else {
                        oneErrorMap.put("officeTelNo"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                }
                poIndex++;
            }

            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String modeOfMedAlert = poDto.get(i).getModeOfMedAlert();
                String designation = poDto.get(i).getDesignation();
                /*if(StringUtil.isEmpty(modeOfMedAlert)||"-1".equals(modeOfMedAlert)){
                    oneErrorMap.put("modeOfMedAlert"+dpoIndex,"UC_CHKLMD001_ERR001");
                }*/

                if(StringUtil.isEmpty(designation)||"-1".equals(designation)){
                    oneErrorMap.put("deputyDesignation"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(salutation)){
                    oneErrorMap.put("deputySalutation"+dpoIndex,"UC_CHKLMD001_ERR001");
                }

                if(StringUtil.isEmpty(idType)||"-1".equals(idType)){
                    oneErrorMap.put("deputyIdType"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(name)){
                    oneErrorMap.put("deputyName"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(idNo)){
                    oneErrorMap.put("deputyIdNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if("FIN".equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,"CHKLMD001_ERR005");
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }
                if("NRIC".equals(idType)){
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!b1){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,"CHKLMD001_ERR005");
                    }else {
                        stringBuilder1.append(idType).append(idNo);
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
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        oneErrorMap.put("deputyEmailAddr"+dpoIndex, "CHKLMD001_ERR006");
                    }
                }

                dpoIndex++;
            }
            String s = stringBuilder.toString();

            if(!StringUtil.isEmpty(stringBuilder1.toString())){
                if(s.contains(stringBuilder1.toString())){

                    oneErrorMap.put("NRICFIN","UC_CHKLMD001_ERR002");

                }else {
                    stringBuilder.append(stringBuilder1.toString());
                }

            }

        }
        return oneErrorMap;
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption){
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
            sBuffer.append(entry.getKey()+"=\""+entry.getValue()+"\" ");
        }
        sBuffer.append(" >");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<option value=\"\">"+ firestOption +"</option>");
        }
        for(SelectOption sp:selectOptionList){
            sBuffer.append("<option value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select "+className+"\" tabindex=\"0\">");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<span class=\"current\">"+firestOption+"</span>");
        }else{
            sBuffer.append("<span class=\"current\">"+selectOptionList.get(0).getText()+"</span>");
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");
        if(!StringUtil.isEmpty(firestOption)){
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
       if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
           for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
               setSvcScopeInfo(appSvcRelatedInfoDto,map);
           }
       }
    }
    //
    public static void setLaboratoryDisciplinesInfo(AppSvcRelatedInfoDto appSvcRelatedInfoDto,List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos){
        Map<String, HcsaSvcSubtypeOrSubsumedDto> map = IaisCommonUtils.genNewHashMap();
        turn(hcsaSvcSubtypeOrSubsumedDtos, map);
        if(appSvcRelatedInfoDto == null){
            return;
        }
        setSvcScopeInfo(appSvcRelatedInfoDto,map);
    }


    //for preview get one svc's DisciplineAllocation
    public static Map<String,List<AppSvcDisciplineAllocationDto>> getDisciplineAllocationDtoList(AppSubmissionDto appSubmissionDto,String svcId){
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
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String hciName = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getHciName();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getConveyanceVehicleNo();
            }

            if(!StringUtil.isEmpty(hciName) && allocationDto !=null && allocationDto.size()>0 ){
                for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                    List<AppSvcChckListDto> appSvcChckListDtoList = null;
                    if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                        String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                        String idNo = appSvcDisciplineAllocationDto.getIdNo();
                        //set chkLstName
                        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                        if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                    appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                }
                            }
                        }
                        if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                            for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                    appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                }
                            }
                        }
                        //set selCgoName
                        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                            for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                if(idNo.equals(appSvcCgoDto.getIdNo())){
                                    appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                }
                            }
                        }
                        reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                    }
                }
            }
            reloadDisciplineAllocationMap.put(hciName, reloadDisciplineAllocation);
        }

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
                    }
                }
            }
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



    //=============================================================================
    //private method
    //=============================================================================
    private static boolean checkCanEdit(AppEditSelectDto appEditSelectDto, String currentType){
        boolean pageCanEdit = false;
        if(appEditSelectDto != null){
            if(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION.equals(currentType)){
                pageCanEdit = appEditSelectDto.isPremisesEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_MEDALERT_PERSONNEL.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isMedAlertEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PRINCIPAL_OFFICER.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isPoEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_DEPUTY_PRINCIPAL_OFFICER.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isDocEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isServiceEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isDocEdit();
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

    private static void setSvcScopeInfo(AppSvcRelatedInfoDto appSvcRelatedInfoDto,Map<String, HcsaSvcSubtypeOrSubsumedDto> map){
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)){
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
}
