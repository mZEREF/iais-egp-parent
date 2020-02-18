package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/2/5 9:41
 */
@Service
@Slf4j
public class AppealServiceImpl implements AppealService {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public List<String> reasonAppeal(String applicationNoOrLicenceNo) {
        ApplicationDto applicationDto = applicationClient.getApplicationDtoByVersion(applicationNoOrLicenceNo).getEntity();
        if(applicationDto!=null){
            String applicationType = applicationDto.getApplicationType();

        }


        return null;
    }

    @Override
    public String submitData(HttpServletRequest request) {

        String appealingFor = request.getParameter("appealingFor");


      /*  return     licencePresmises(request,appealingFor);*/
        return   applicationPresmies(request ,appealingFor);

    }

    @Override
    public String saveData(HttpServletRequest request) {
        String saveDraftId =(String) request.getSession().getAttribute("saveDraftNo");
        String appealingFor =  request.getParameter("appealingFor");
        String reasonSelect = request.getParameter("reasonSelect");
        String[] selectHciNames = request.getParameterValues("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        String licenceYear = request.getParameter("licenceYear");
        List<AppSvcCgoDto> appSvcCgoDtoList = reAppSvcCgo(request);
        ParamUtil.setRequestAttr(request, "CgoMandatoryCount", appSvcCgoDtoList.size());

        ParamUtil.setSessionAttr(request, "GovernanceOfficersList", (Serializable) appSvcCgoDtoList);

        String groupId =(String) request.getAttribute("groupId");
        AppealPageDto appealPageDto = reAppealPage(request);
        String s = JsonUtil.parseToJson(appealPageDto);
        AppPremiseMiscDto appPremiseMiscDto=new AppPremiseMiscDto();
        if(!StringUtil.isEmpty(saveDraftId)){
            AppSubmissionDto entity = applicationClient.draftNumberGet(saveDraftId).getEntity();
            entity.setAmountStr(s);
            entity.setAppGrpId(groupId);
            applicationClient.saveDraft(entity).getEntity();

            appPremiseMiscDto.setRemarks(remarks);
            appPremiseMiscDto.setReason(reasonSelect);
            if(!StringUtil.isEmpty(licenceYear)){
                appPremiseMiscDto.setNewLicYears(Integer.valueOf(licenceYear));
            }
            appPremiseMiscDto.setNewHciName(proposedHciName);
            request.setAttribute("appealingFor",appealingFor);
            request.setAttribute("appPremiseMiscDto",appPremiseMiscDto);
            return null;
        }
        AppSubmissionDto appSubmissionDto=new AppSubmissionDto();
        String apty = systemAdminClient.draftNumber("APTY001").getEntity();
        appSubmissionDto.setDraftNo(apty);
        appSubmissionDto.setAppGrpId(groupId);
        appSubmissionDto.setAmountStr(s);
        appSubmissionDto.setDraftStatus("CMSTAT001");
        appSubmissionDto.setAppType("APTY002");
        AppSubmissionDto entity = applicationClient.saveDraft(appSubmissionDto).getEntity();
        String draftNo = entity.getDraftNo();
        appPremiseMiscDto.setRemarks(remarks);
        appPremiseMiscDto.setReason(reasonSelect);
        if(!StringUtil.isEmpty(licenceYear)){
            appPremiseMiscDto.setNewLicYears(Integer.valueOf(licenceYear));
        }
        appPremiseMiscDto.setNewHciName(proposedHciName);
        request.setAttribute("appPremiseMiscDto",appPremiseMiscDto);
        request.setAttribute("appealingFor",appealingFor);
        request.getSession().setAttribute("saveDraftNo",draftNo);
        return null;
    }

    @Override
    public void getMessage(HttpServletRequest request) {
        String appealingFor =  request.getParameter("appealingFor");
        ApplicationDto applicationDto = applicationClient.getApplicationDtoByVersion("AN191226000315-03").getEntity();
    /*    LicenceDto licenceDto = licenceClient.getLicBylicNo("L/20CLB0156/CLB/001/201").getEntity();
        List<PremisesListQueryDto> premisesListQueryDtos = licenceClient.getPremises(licenceDto.getId()).getEntity();
        for(int i=0;i<premisesListQueryDtos.size();i++){
            String hciName = premisesListQueryDtos.get(i).getHciName();
            String address = premisesListQueryDtos.get(i).getAddress();

        }*/
        String serviceId = applicationDto.getServiceId();
        String id = applicationDto.getId();
        if(id!=null){
            AppInsRepDto entity = applicationClient.getHciNameAndAddress(id).getEntity();
            String hciName = entity.getHciName();
            String hciAddress = entity.getHciAddress();
            request.getSession().setAttribute("hciAddress",hciAddress);
            request.getSession().setAttribute("hciName",hciName);
        }

        List<String> list=new ArrayList<>();
        list.add(serviceId);
        List<HcsaServiceDto> entity = appConfigClient.getHcsaService(list).getEntity();
        for(int  i=0;i<entity.size();i++){
            String svcName = entity.get(i).getSvcName();
            request.getSession().setAttribute("serviceName",svcName);
        }
        String applicationNo = applicationDto.getApplicationNo();
        request.getSession().setAttribute("applicationNo",applicationNo);
        request.setAttribute("applicationDto",applicationDto);
        String status = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
            requetForInformationGetMessage(request);
        }

    }

    @Override
    public Map<String,String> validate(HttpServletRequest request) {
        Map<String,String> errorMap=new HashMap<>();
        validae(request,errorMap);
        return errorMap;
    }


    private List<AppSvcCgoDto>  reAppSvcCgo(HttpServletRequest request){
        List<AppSvcCgoDto> appSvcCgoDtoList = new ArrayList<>();
        AppSvcCgoDto appSvcCgoDto = null;
        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size=0;
        if(assignSelect != null && assignSelect.length>0){
            size = assignSelect.length;
        }
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] designation = ParamUtil.getStrings(request, "designation");
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, "professionRegoNo");
        String[] specialty = ParamUtil.getStrings(request, "specialty");
        String[] specialtyOther = ParamUtil.getStrings(request, "specialtyOther");
        String[] qualification = ParamUtil.getStrings(request, "qualification");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");

        for(int i = 0; i<size; i++){
            appSvcCgoDto = new AppSvcCgoDto();
            //cgoIndexNo
            String cgoIndexNo = new StringBuffer().append("cgo-").append(i).append("-").toString();
            appSvcCgoDto.setAssignSelect(assignSelect[i]);
            appSvcCgoDto.setSalutation(salutation[i]);
            appSvcCgoDto.setName(name[i]);
            appSvcCgoDto.setIdType(idType[i]);
            appSvcCgoDto.setIdNo(idNo[i]);
            appSvcCgoDto.setDesignation(designation[i]);
            appSvcCgoDto.setProfessionType(professionType[i]);
            appSvcCgoDto.setProfessionRegoNo(professionRegoNo[i]);
            String specialtyStr = specialty[i];
            appSvcCgoDto.setSpeciality(specialtyStr);
            if("other".equals(specialtyStr)){
                appSvcCgoDto.setSpecialityOther(specialtyOther[i]);
            }
            //qualification
            appSvcCgoDto.setQualification(qualification[i]);
            appSvcCgoDto.setMobileNo(mobileNo[i]);
            appSvcCgoDto.setEmailAddr(emailAddress[i]);
            appSvcCgoDto.setCgoIndexNo(cgoIndexNo);
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
        return appSvcCgoDtoList;
    }


    public void validae(HttpServletRequest request, Map<String,String> map){
        AppealPageDto appealPageDto = reAppealPage(request);
        String remarks = appealPageDto.getRemarks();
        if (StringUtil.isEmpty(remarks)) {
            map.put("remarks","UC_CHKLMD001_ERR001");
        }
        String appealReason = appealPageDto.getAppealReason();
        if (StringUtil.isEmpty(appealReason)){
            map.put("reason","UC_CHKLMD001_ERR001");
        }else {
            if("MS003".equals(appealReason)){
                List<AppSvcCgoDto> appSvcCgoList = appealPageDto.getAppSvcCgoDto();
                StringBuilder stringBuilder =new StringBuilder();
                for(int i=0;i<appSvcCgoList.size();i++ ){
                    StringBuilder stringBuilder1=new StringBuilder();
                    String assignSelect = appSvcCgoList.get(i).getAssignSelect();
                    if("-1".equals(assignSelect)){
                        map.put("assignSelect"+i, "UC_CHKLMD001_ERR002");
                    }else {
                        String idTyp = appSvcCgoList.get(i).getIdType();
                        if("-1".equals(idTyp)){
                            map.put("idTyp"+i, "UC_CHKLMD001_ERR002");
                        }
                        String salutation = appSvcCgoList.get(i).getSalutation();
                        if(StringUtil.isEmpty(salutation)){
                            map.put("salutation"+i,"UC_CHKLMD001_ERR001");
                        }
                        String speciality = appSvcCgoList.get(i).getSpeciality();
                        if("-1".equals(speciality)){
                            map.put("speciality"+i,"UC_CHKLMD001_ERR002");
                        }
                        String professionType = appSvcCgoList.get(i).getProfessionType();
                        if(StringUtil.isEmpty(professionType)){
                            map.put("professionType"+i,"UC_CHKLMD001_ERR002");
                        }
                        String designation = appSvcCgoList.get(i).getDesignation();
                        if(StringUtil.isEmpty(designation)){
                            map.put("designation"+i,"UC_CHKLMD001_ERR001");
                        }
                        String professionRegoNo = appSvcCgoList.get(i).getProfessionRegoNo();
                        if(StringUtil.isEmpty(professionRegoNo)){
                            map.put("professionRegoNo"+i,"UC_CHKLMD001_ERR001");
                        }
                        String idNo = appSvcCgoList.get(i).getIdNo();
                        //to do
                        if(StringUtil.isEmpty(idNo)){
                            map.put("idNo"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            if("FIN".equals(idTyp)){
                                boolean b = SgNoValidator.validateFin(idNo);
                                if(!b){
                                    map.put("idNo"+i,"CHKLMD001_ERR005");
                                }
                                stringBuilder1.append(idTyp).append(idNo);

                            }
                            if("NRIC".equals(idTyp)){
                                boolean b1 = SgNoValidator.validateNric(idNo);
                                if(!b1){
                                    map.put("idNo"+i,"CHKLMD001_ERR005");
                                }
                                stringBuilder1.append(idTyp).append(idNo);

                            }

                        }
                        //to do

                        String Specialty = appSvcCgoList.get(i).getSpeciality();
                        if (StringUtil.isEmpty(Specialty)) {
                            map.put("speciality"+i, "UC_CHKLMD001_ERR002");
                        }

                        String specialty = appSvcCgoList.get(i).getSpeciality();
                        if(StringUtil.isEmpty(specialty)){
                            map.put("specialty"+i, "UC_CHKLMD001_ERR001");
                        }
                        String name = appSvcCgoList.get(i).getName();
                        if(StringUtil.isEmpty(name)){
                            map.put("name"+i,"UC_CHKLMD001_ERR001");
                        }

                        String mobileNo = appSvcCgoList.get(i).getMobileNo();
                        if(StringUtil.isEmpty(mobileNo)){
                            map.put("mobileNo"+i, "UC_CHKLMD001_ERR001");
                        }else if (!StringUtil.isEmpty(mobileNo)) {
                            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                                map.put("mobileNo"+i, "CHKLMD001_ERR004");
                            }
                        }
                        String emailAddr = appSvcCgoList.get(i).getEmailAddr();
                        if(StringUtil.isEmpty(emailAddr)){
                            map.put("emailAddr"+i, "UC_CHKLMD001_ERR001");
                        }else if (!StringUtil.isEmpty(emailAddr)) {
                            if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                                map.put("emailAddr"+i, "CHKLMD001_ERR006");
                            }
                        }
                        String s = stringBuilder.toString();
                        if(!StringUtil.isEmpty(stringBuilder1.toString())){
                            if(s.contains(stringBuilder1.toString())){
                                map.put("idNo","UC_CHKLMD001_ERR002");
                            }else {
                                stringBuilder.append(stringBuilder1.toString());
                            }
                        }

                    }

                }

            }

        }


    }

    private AppealPageDto reAppealPage(HttpServletRequest request){
        AppealPageDto appealPageDto=new AppealPageDto();
        List<AppSvcCgoDto> appSvcCgoDtos = reAppSvcCgo(request);

        String appealingFor =  request.getParameter("appealingFor");
        String reasonSelect = request.getParameter("reasonSelect");
        String licenceYear = request.getParameter("licenceYear");
        String selectHciNames = request.getParameter("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        appealPageDto.setAppealReason(reasonSelect);
        appealPageDto.setAppSvcCgoDto(appSvcCgoDtos);
        appealPageDto.setNewHciName(proposedHciName);
        appealPageDto.setRemarks(remarks);
        if(!StringUtil.isEmpty(licenceYear)){
            appealPageDto.setNewLicYears(Integer.parseInt(licenceYear));
        }

        return appealPageDto;
    }


    private String licencePresmises(HttpServletRequest request,String  licenceNo){

//        LicenceDto entity2 = licenceClient.getLicBylicNo("L/20CLB0156/CLB/001/201").getEntity();

        ApplicationDto entity1 = applicationClient.getApplicationsByLicenceId("7ECAE165-534A-EA11-BE7F-000C29F371DC").getEntity();

        LicenceDto entity=new LicenceDto();
                String id = entity.getId();
        String svcName = entity.getSvcName();
        List<ApplicationDto> applicationDtoListlist=new ArrayList<>();
        List<PremisesDto> premisess = licenceClient.getPremisesDto("7ECAE165-534A-EA11-BE7F-000C29F371DC").getEntity();
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        StringBuilder stringBuilder =new StringBuilder(appNo);
        String s = stringBuilder.append("-1").toString();
        List<AppGrpPremisesDto> premisesDtos=new ArrayList<>();
                for(PremisesDto every:premisess){
                    AppGrpPremisesDto appGrpPremisesDto = MiscUtil.transferEntityDto(every, AppGrpPremisesDto.class);
                    premisesDtos.add(appGrpPremisesDto);
                }

        List<AppSvcCgoDto> list=new ArrayList<>();
        for(AppGrpPremisesDto every:premisesDtos){
            AppSvcCgoDto appSvcCgoDto = MiscUtil.transferEntityDto(every, AppSvcCgoDto.class);
            list.add(appSvcCgoDto);
            ApplicationDto applicationDto=new ApplicationDto();
            applicationDto.setStatus("APST036");
            applicationDto.setApplicationType("APTY001");
            applicationDto.setVersion(1);
            if(entity1!=null){
                String status = entity1.getStatus();
                if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                    applicationDto.setVersion(entity1.getVersion()+1);
                    applicationDto.setStatus("APST023");
                    applicationGroupDto.setStatus("AGST007");

                }

            }
            applicationDto.setLicenceId("7ECAE165-534A-EA11-BE7F-000C29F371DC");
            applicationDto.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
            applicationDto.setApplicationNo(s);
            applicationDto.setOriginLicenceId("0750A47B-B327-EA11-BE7D-000C29F371DC");

            applicationDtoListlist.add(applicationDto);
        }
        //appealPageDto
        AppealPageDto appealDto=getAppealPageDto(request);
        String reasonSelect = appealDto.getAppealReason();

        List<AppSvcCgoDto> appSvcCgoDtos=null;
        if("MS003".equals(reasonSelect)){
            appSvcCgoDtos = reAppSvcCgo(request);
        }


        appealDto.setApplicationGroupDto(applicationGroupDto);

        appealDto.setAppId("7ECAE165-534A-EA11-BE7F-000C29F371DC");
        appealDto.setApplicationDto(applicationDtoListlist);
        appealDto.setAppealType("NEWLICEN");
        if(entity1!=null){
            String status = entity1.getStatus();
            if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                appealDto.setAppealType("RFI");
            }
        }


        if(appSvcCgoDtos!=null&&!appSvcCgoDtos.isEmpty()){
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }
        appealDto.setAppGrpPremisesDtos(premisesDtos);
        applicationClient.submitAppeal(appealDto);
        return s;
    }

    private ApplicationGroupDto getApplicationGroupDto(String appNo){

        ApplicationGroupDto applicationGroupDto=new ApplicationGroupDto();
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setGroupNo(appNo);
        applicationGroupDto.setStatus("AGST006");
        applicationGroupDto.setAmount(0.0);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setLicenseeId("36F8537B-FE17-EA11-BE78-000C29D29DB0");
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setIsGrpLic(0);
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return applicationGroupDto;

    }

    private String applicationPresmies(HttpServletRequest request, String applicationNo){

        ApplicationDto applicationDto = applicationClient.getApplicationDtoByVersion("AN191226000315-03").getEntity();

        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        StringBuilder stringBuilder =new StringBuilder(appNo);
        String s = stringBuilder.append("-1").toString();
        //appealPageDto
        AppealPageDto appealDto=getAppealPageDto(request);
        String reasonSelect = appealDto.getAppealReason();


        List<AppSvcCgoDto> appSvcCgoDtos=null;
        if("MS003".equals(reasonSelect)){
            appSvcCgoDtos = reAppSvcCgo(request);
        }

        AppliSpecialDocDto appliSpecialDocDto =new AppliSpecialDocDto();
        appliSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");

        //group
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        //info

        ApplicationDto applicationDto1 =new ApplicationDto();
        applicationDto1.setApplicationType("APTY001");
        applicationDto1.setApplicationNo(s);
        //info

        applicationDto1.setStatus("APST036");
        applicationDto1.setServiceId(applicationDto.getServiceId());
        String status = applicationDto.getStatus();
        applicationDto1.setVersion(1);

        applicationDto1.setLicenceId(applicationDto.getLicenceId());
        List<ApplicationDto> list=new ArrayList<>();
        list.add(applicationDto1);
        appealDto.setApplicationGroupDto(applicationGroupDto);

        appealDto.setAppId(applicationDto.getId());
        appealDto.setApplicationDto(list);
            appealDto.setAppealType("NEWAPP");
        //if infomation
        if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
            applicationDto1.setVersion(applicationDto.getVersion()+1);
            appealDto.setAppealType("RFI");
            applicationDto1.setStatus("APST023");
            applicationGroupDto.setStatus("AGST007");
        }

        if(appSvcCgoDtos!=null&&!appSvcCgoDtos.isEmpty()){
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }

        AppealPageDto appealPageDto = applicationClient.submitAppeal(appealDto).getEntity();
        ApplicationGroupDto applicationGroupDto1 = appealPageDto.getApplicationGroupDto();
        String id = applicationGroupDto1.getId();
        request.setAttribute("groupId",id);
        saveData(request);
        return s;
    }

    private void requetForInformationGetMessage(HttpServletRequest request){
        ApplicationDto applicationDto =(ApplicationDto) request.getAttribute("applicationDto");
        String id = applicationDto.getId();
        String appGrpId = applicationDto.getAppGrpId();
        List<String> appPremisCorreIds=new ArrayList<>();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.listAppPremisesCorrelation(id).getEntity();
        if(appPremisesCorrelationDtos!=null){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos){
                String appPremisesCorrelationDtoId = appPremisesCorrelationDto.getId();
                appPremisCorreIds.add(appPremisesCorrelationDtoId);
            }

        }

        AppPremiseMiscDto appPremiseMiscDto = applicationClient.getAppPremisesMisc("D9852E6A-2652-EA11-BE79-000C29D29DB0").getEntity();
        String reason = appPremiseMiscDto.getReason();
        if("MS003".equals(reason)){
            List<AppSvcCgoDto> appSvcCgoDtos=new ArrayList<>();
            List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationClient.getAppGrpPersonnelByGrpId("appGrpId").getEntity();
            for(AppGrpPersonnelDto appGrpPersonnelDto:appGrpPersonnelDtos){
                AppSvcCgoDto appSvcCgoDto = MiscUtil.transferEntityDto(appGrpPersonnelDto, AppSvcCgoDto.class);
                appSvcCgoDtos.add(appSvcCgoDto);
            }
            ParamUtil.setRequestAttr(request, "CgoMandatoryCount", appSvcCgoDtos.size());

            ParamUtil.setSessionAttr(request, "GovernanceOfficersList", (Serializable) appSvcCgoDtos);

        }
        request.setAttribute("appPremiseMiscDto",appPremiseMiscDto);
    }

    private AppealPageDto getAppealPageDto(HttpServletRequest request){
        String reasonSelect = request.getParameter("reasonSelect");
        String licenceYear = request.getParameter("licenceYear");
        String selectHciNames = request.getParameter("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        AppealPageDto appealDto=new AppealPageDto();
        appealDto.setRemarks(remarks);
        appealDto.setAppealReason(reasonSelect);
        appealDto.setNewHciName(proposedHciName);
        if(!StringUtil.isEmpty(licenceYear)){
            appealDto.setNewLicYears(Integer.valueOf(licenceYear));
        }
        return appealDto;
    }

}
