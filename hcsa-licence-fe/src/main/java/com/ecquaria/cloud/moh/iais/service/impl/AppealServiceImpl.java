package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

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
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private FileRepositoryClient fileRepositoryClient;
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
        String appealingFor = (String)  request.getSession().getAttribute("appealingFor");
        String type = (String)  request.getSession().getAttribute("type");
        if("licence".equals(type)){
              return     licencePresmises(request,appealingFor);
        } else if ("application".equals(type)) {

            return   applicationPresmies(request ,appealingFor);
        }else {
            return null;
        }


    }

    @Override
    public String saveData(HttpServletRequest req) {
        CommonsMultipartFile file =( CommonsMultipartFile) req.getSession().getAttribute("file");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String saveDraftId =(String) req.getSession().getAttribute("saveDraftNo");
        String appealingFor =  request.getParameter("appealingFor");
        String isDelete = request.getParameter("isDelete");
        String reasonSelect = request.getParameter("reasonSelect");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        String othersReason = request.getParameter("othersReason");
        CommonsMultipartFile selectedFile =(CommonsMultipartFile) request.getFile("selectedFile");
        if(selectedFile!=null&&selectedFile.getSize()>0){
            String filename = selectedFile.getOriginalFilename();
            req.getSession().setAttribute("file",selectedFile);
            req.setAttribute("filename",filename);
        }
        else if(file!=null&&file.getSize()>0){
            if("Y".equals(isDelete)){
                String filename = file.getOriginalFilename();
                req.getSession().setAttribute("file",file);
                req.setAttribute("filename",filename);
            }
        }
        if("N".equals(isDelete)){
            req.getSession().removeAttribute("appPremisesSpecialDocDto");
            req.getSession().removeAttribute("filename");
        }
        List<AppSvcCgoDto> appSvcCgoDtoList = reAppSvcCgo(request);
        ParamUtil.setRequestAttr(req, "CgoMandatoryCount", appSvcCgoDtoList.size());

        ParamUtil.setSessionAttr(req, "GovernanceOfficersList", (Serializable) appSvcCgoDtoList);

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

            if ("MS008".equals(reasonSelect)) {
                appPremiseMiscDto.setNewHciName(proposedHciName);
            }

            req.setAttribute("appealingFor",appealingFor);
            req.setAttribute("appPremiseMiscDto",appPremiseMiscDto);
            return null;
        }
        AppSubmissionDto appSubmissionDto=new AppSubmissionDto();
        String apty = systemAdminClient.draftNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        appSubmissionDto.setDraftNo(apty);
        appSubmissionDto.setAppGrpId(groupId);
        appSubmissionDto.setAmountStr(s);
        appSubmissionDto.setDraftStatus("CMSTAT001");
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        //todo
        appSubmissionDto.setLicenseeId("9ED45E34-B4E9-E911-BE76-000C29C8FBE4");
        AppSubmissionDto entity = applicationClient.saveDraft(appSubmissionDto).getEntity();
        String draftNo = entity.getDraftNo();
        appPremiseMiscDto.setRemarks(remarks);
        appPremiseMiscDto.setOtherReason(othersReason);
        appPremiseMiscDto.setReason(reasonSelect);
        appPremiseMiscDto.setNewHciName(proposedHciName);
        req.setAttribute("appPremiseMiscDto",appPremiseMiscDto);
        req.setAttribute("appealingFor",appealingFor);
        req.getSession().setAttribute("saveDraftNo",draftNo);
        return null;
    }

    @Override
    public void getMessage(HttpServletRequest request) {

        String appealingFor =  request.getParameter("appealingFor");
        String type = request.getParameter("type");
        if ("licence".equals(type)) {
            LicenceDto licenceDto = licenceClient.getLicBylicNo(appealingFor).getEntity();

            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            List<PremisesListQueryDto> premisesListQueryDtos = licenceClient.getPremises(licenceDto.getId()).getEntity();
            List<PremisesListQueryDto> listQueryDtos= IaisCommonUtils.genNewArrayList();
            for(int i=0;i<premisesListQueryDtos.size();i++){
                if(licenceDto.getId().equals(premisesListQueryDtos.get(i).getLicenceId())){
                    listQueryDtos.add(premisesListQueryDtos.get(i));
                }
            }
            List<String> hciNames=IaisCommonUtils.genNewArrayList();
            List<String> addresses=IaisCommonUtils.genNewArrayList();
            for(int i=0;i<listQueryDtos.size();i++){
                String hciName = listQueryDtos.get(i).getHciName();
                String address = listQueryDtos.get(i).getAddress();
                hciNames.add(hciName);
                addresses.add(address);
            }
            request.getSession().setAttribute("id",licenceDto.getId());
            request.getSession().setAttribute("hciAddress",addresses);
            request.getSession().setAttribute("hciNames",hciNames);
            request.getSession().setAttribute("serviceName",svcName);
            request.getSession().setAttribute("licenceNo",licenceNo);
        } else if ("application".equals(type)) {
            ApplicationDto applicationDto = applicationClient.getApplicationDtoByVersion(appealingFor).getEntity();

            String serviceId = applicationDto.getServiceId();
            String id = applicationDto.getId();
            if(id!=null){
                AppInsRepDto entity = applicationClient.getHciNameAndAddress(id).getEntity();
                String hciName = entity.getHciName();
                String hciAddres = entity.getHciAddress();
                List<String> hciNames=IaisCommonUtils.genNewArrayList();
                hciNames.add(hciName);
                List<String >hciAddress=IaisCommonUtils.genNewArrayList();
                hciAddress.add(hciAddres);
                request.getSession().setAttribute("hciAddress",hciAddress);
                request.getSession().setAttribute("hciNames",hciNames);
            }

            List<String> list=IaisCommonUtils.genNewArrayList();
            list.add(serviceId);
            List<HcsaServiceDto> entity = appConfigClient.getHcsaService(list).getEntity();
            for(int  i=0;i<entity.size();i++){
                String svcName = entity.get(i).getSvcName();
                request.getSession().setAttribute("serviceName",svcName);
            }
            String applicationNo = applicationDto.getApplicationNo();
            request.getSession().setAttribute("id",applicationDto.getId());
            request.getSession().setAttribute("applicationNo",applicationNo);
            request.setAttribute("applicationDto",applicationDto);
            String status = applicationDto.getStatus();
            if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                requetForInformationGetMessage(request);
            }

            request.getSession().setAttribute("serviceId",applicationDto.getServiceId());
        }
        request.getSession().setAttribute("appealingFor",appealingFor);
        request.getSession().setAttribute("type",type);

    }

    @Override
    public Map<String,String> validate(HttpServletRequest request) {
        Map<String,String> errorMap=IaisCommonUtils.genNewHashMap();
        validae(request,errorMap);
        return errorMap;
    }


    private List<AppSvcCgoDto>  reAppSvcCgo(HttpServletRequest req){
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        List<AppSvcCgoDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
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


    public void validae(HttpServletRequest req, Map<String,String> map){
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        //CGO mix can add
        String serviceId = (String)request.getSession().getAttribute("serviceId");
        if(serviceId!=null){
            appConfigClient.getServiceType(serviceId,"CGO");
        }
        String isDelete = request.getParameter("isDelete");
        CommonsMultipartFile sessionFile =( CommonsMultipartFile)  req.getSession().getAttribute("file");
        CommonsMultipartFile file=(CommonsMultipartFile) request.getFile("selectedFile");
        if(file!=null&&file.getSize()>0){
            long size = file.getSize()/1024;
            req.getSession().setAttribute("file",file);
            if(size>5*1024){
                map.put("file","UC_CHKLMD001_ERR007");
            }
            String filename = file.getOriginalFilename();
            String fileType=  filename.substring(filename.lastIndexOf(".")+1);
            //todo change
             if(!"PDF".equalsIgnoreCase(fileType)&&!"PNG".equalsIgnoreCase(fileType)&&
                    !"JPG".equalsIgnoreCase(fileType)&&!"DOC".equalsIgnoreCase(fileType)&&!"DOCX".equalsIgnoreCase(fileType)){
                map.put("file","Wrong file type");
            }

        }
        else if(sessionFile!=null&&sessionFile.getSize()>0){
            if ("Y".equals(isDelete)) {
                long size = sessionFile.getSize()/1024;
                if(size>5*1024){
                    map.put("file","UC_CHKLMD001_ERR007");
                }

                String filename = sessionFile.getOriginalFilename();
                String fileType=  filename.substring(filename.lastIndexOf(".")+1);
                //todo change
                if(!"PDF".equalsIgnoreCase(fileType)&&!"PNG".equalsIgnoreCase(fileType)&&
                        !"JPG".equalsIgnoreCase(fileType)&&!"DOC".equalsIgnoreCase(fileType)&&!"DOCX".equalsIgnoreCase(fileType)){
                    map.put("file","Wrong file type");
                }

            }

        }


        AppealPageDto appealPageDto = reAppealPage(request);
        String remarks = appealPageDto.getRemarks();
        if (StringUtil.isEmpty(remarks)) {
            map.put("remarks","UC_CHKLMD001_ERR001");
        }
        String appealReason = appealPageDto.getAppealReason();
        String id = (String)request.getSession().getAttribute("id");
        Boolean entity = applicationClient.isUseReason(id, appealReason).getEntity();
        if(!entity){
            map.put("reason","This reason can no longer be selected");
        }
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
                        map.put("assignSelect"+i, "UC_CHKLMD001_ERR001");
                    }else {
                        String idTyp = appSvcCgoList.get(i).getIdType();
                        if("-1".equals(idTyp)){
                            map.put("idTyp"+i, "UC_CHKLMD001_ERR001");
                        }
                        String salutation = appSvcCgoList.get(i).getSalutation();
                        if(StringUtil.isEmpty(salutation)){
                            map.put("salutation"+i,"UC_CHKLMD001_ERR001");
                        }
                        String speciality = appSvcCgoList.get(i).getSpeciality();
                        if("-1".equals(speciality)){
                            map.put("speciality"+i,"UC_CHKLMD001_ERR001");
                        }
                        String professionType = appSvcCgoList.get(i).getProfessionType();
                        if(StringUtil.isEmpty(professionType)){
                            map.put("professionType"+i,"UC_CHKLMD001_ERR001");
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

                            }else if("NRIC".equals(idTyp)){
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
                            map.put("speciality"+i, "UC_CHKLMD001_ERR001");
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

            else if("MS007".equals(appealReason)){
                String otherReason = request.getParameter("othersReason");
                if(StringUtil.isEmpty(otherReason)){
                    map.put("otherReason","UC_CHKLMD001_ERR001");
                }
            }
        }


    }

    private AppealPageDto reAppealPage(HttpServletRequest request){
        AppealPageDto appealPageDto=new AppealPageDto();
        List<AppSvcCgoDto> appSvcCgoDtos = reAppSvcCgo(request);
        String reasonSelect = request.getParameter("reasonSelect");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        appealPageDto.setAppealReason(reasonSelect);
        appealPageDto.setAppSvcCgoDto(appSvcCgoDtos);
        if ("MS008".equals(reasonSelect)) {
            appealPageDto.setNewHciName(proposedHciName);

        }
        appealPageDto.setRemarks(remarks);


        return appealPageDto;
    }


    private String licencePresmises(HttpServletRequest request,String  licenceNo){

        LicenceDto licenceDto = licenceClient.getLicBylicNo(licenceNo).getEntity();

        ApplicationDto entity1 = applicationClient.getApplicationsByLicenceId(licenceDto.getId()).getEntity();
        String licenseeId = licenceDto.getLicenseeId();
        LicenceDto entity=new LicenceDto();
                String id = entity.getId();
        String svcName = entity.getSvcName();
        List<ApplicationDto> applicationDtoListlist=IaisCommonUtils.genNewArrayList();
        List<PremisesDto> premisess = licenceClient.getPremisesDto(licenceDto.getId()).getEntity();
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        applicationGroupDto.setLicenseeId(licenseeId);
        StringBuilder stringBuilder =new StringBuilder(appNo);
        String s = stringBuilder.append("-01").toString();
        List<AppGrpPremisesDto> premisesDtos=IaisCommonUtils.genNewArrayList();
                for(PremisesDto every:premisess){
                    AppGrpPremisesDto appGrpPremisesDto = MiscUtil.transferEntityDto(every, AppGrpPremisesDto.class);
                    premisesDtos.add(appGrpPremisesDto);
                }

        List<AppSvcCgoDto> list=IaisCommonUtils.genNewArrayList();
        for(AppGrpPremisesDto every:premisesDtos){
            AppSvcCgoDto appSvcCgoDto = MiscUtil.transferEntityDto(every, AppSvcCgoDto.class);
            list.add(appSvcCgoDto);
            ApplicationDto applicationDto=new ApplicationDto();
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            applicationDto.setApplicationType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
            applicationDto.setVersion(1);
            if(entity1!=null){
                String status = entity1.getStatus();
                if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                    applicationDto.setVersion(entity1.getVersion()+1);
                    //if not need new group
                    applicationGroupDto.setId(entity1.getAppGrpId());
                    applicationGroupDto.setGroupNo(entity1.getApplicationNo().substring(0,entity1.getApplicationNo().lastIndexOf("-")));
                    applicationDto.setApplicationNo(entity1.getApplicationNo());
                    applicationGroupDto.setStatus("AGST001");
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY);
                    s=entity1.getApplicationNo();
                }

            }
            List<String> svcNames=IaisCommonUtils.genNewArrayList();
            svcNames.add(licenceDto.getSvcName());
            List<HcsaServiceDto> hcsaServiceDtos = appConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            applicationDto.setServiceId(hcsaServiceDtos.get(0).getId());
            applicationDto.setApplicationNo(s);
            applicationDto.setOriginLicenceId(licenceDto.getOriginLicenceId());

            applicationDtoListlist.add(applicationDto);
        }
        //appealPageDto
        AppealPageDto appealDto=getAppealPageDto(request);
        String reasonSelect = appealDto.getAppealReason();

        List<AppSvcCgoDto> appSvcCgoDtos=null;
        if("MS003".equals(reasonSelect)){
            appSvcCgoDtos = reAppSvcCgo(request);
        }
        if ("MS001".equals(reasonSelect)) {
        for(ApplicationDto application:applicationDtoListlist){
            application.setStatus(ApplicationConsts.APPLICATION_STATUS_APPEAL_APPROVE);
        }

        }

        appealDto.setApplicationGroupDto(applicationGroupDto);
        appealDto.setAppId(licenceDto.getId());
        appealDto.setApplicationDto(applicationDtoListlist);
        appealDto.setAppealType(ApplicationConsts.APPEAL_TYPE_LICENCE);
        if(entity1!=null){
            String status = entity1.getStatus();
            if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
                appealDto.setAppealType("APPEAL006");
            }
        }


        if(appSvcCgoDtos!=null&&!appSvcCgoDtos.isEmpty()){
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }
        appealDto.setAppGrpPremisesDtos(premisesDtos);
        applicationClient.submitAppeal(appealDto);
        ApplicationGroupDto applicationGroupDto1 = appealDto.getApplicationGroupDto();
        String groupId = applicationGroupDto1.getId();
        request.setAttribute("groupId",groupId);
        saveData(request);
        request.setAttribute("newApplicationNo",s);
        //todo send email

        return s;
    }

    private ApplicationGroupDto getApplicationGroupDto(String appNo){

        ApplicationGroupDto applicationGroupDto=new ApplicationGroupDto();
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setGroupNo(appNo);
        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        applicationGroupDto.setAmount(0.0);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setLicenseeId("36F8537B-FE17-EA11-BE78-000C29D29DB0");
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setGrpLic(false);
        applicationGroupDto.setPmtStatus("PMT05");
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return applicationGroupDto;

    }

    private String applicationPresmies(HttpServletRequest request, String applicationNo){

        ApplicationDto applicationDto = applicationClient.getApplicationDtoByVersion(applicationNo).getEntity();
        String grpId = applicationDto.getAppGrpId();

        ApplicationGroupDto entity = applicationClient.getApplicationGroup(grpId).getEntity();
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        StringBuilder stringBuilder =new StringBuilder(appNo);
        String s = stringBuilder.append("-01").toString();
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
        applicationGroupDto.setLicenseeId(entity.getLicenseeId());

        ApplicationDto applicationDto1 =new ApplicationDto();
        applicationDto1.setApplicationType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        applicationDto1.setApplicationNo(s);
        //info

        applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
        if("MS001".equals(reasonSelect)){
            applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_APPEAL_APPROVE);
        }
        applicationDto1.setServiceId(applicationDto.getServiceId());
        String status = applicationDto.getStatus();
        applicationDto1.setVersion(1);

        List<ApplicationDto> list=IaisCommonUtils.genNewArrayList();
        list.add(applicationDto1);
        appealDto.setApplicationGroupDto(applicationGroupDto);

        appealDto.setAppId(applicationDto.getId());
        appealDto.setApplicationDto(list);
            appealDto.setAppealType(ApplicationConsts.APPEAL_TYPE_APPLICAITON);
        //if infomation
        if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)){
            applicationDto1.setVersion(applicationDto.getVersion()+1);
            //if need new group
            applicationGroupDto.setId(applicationDto.getId());
            applicationGroupDto.setGroupNo(applicationDto.getAppGrpId().substring(0,applicationDto.getApplicationNo().lastIndexOf("-")));
            applicationDto1.setApplicationNo(applicationDto.getApplicationNo());
            appealDto.setAppealType("APPEAL006");
            applicationGroupDto.setStatus("AGST001");
            applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY);
            s=applicationDto.getApplicationNo();
        }

        if(appSvcCgoDtos!=null&&!appSvcCgoDtos.isEmpty()){
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }

        AppealPageDto appealPageDto = applicationClient.submitAppeal(appealDto).getEntity();
        ApplicationGroupDto applicationGroupDto1 = appealPageDto.getApplicationGroupDto();
        String groupId = applicationGroupDto1.getId();
        request.setAttribute("groupId",groupId);
        saveData(request);

        //todo send email
        return s;
    }

    private void requetForInformationGetMessage(HttpServletRequest request){
        ApplicationDto applicationDto =(ApplicationDto) request.getAttribute("applicationDto");
        String id = applicationDto.getId();
        String appGrpId = applicationDto.getAppGrpId();
        List<String> appPremisCorreIds=IaisCommonUtils.genNewArrayList();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.listAppPremisesCorrelation(id).getEntity();
        if(appPremisesCorrelationDtos!=null){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos){
                String appPremisesCorrelationDtoId = appPremisesCorrelationDto.getId();
                appPremisCorreIds.add(appPremisesCorrelationDtoId);
            }
        }
        String entity = applicationClient.getRequestForInfo(id).getEntity();
        AppPremiseMiscDto appPremiseMiscDto = applicationClient.getAppPremisesMisc(entity).getEntity();
        String reason = appPremiseMiscDto.getReason();
        String appPremCorreId = appPremiseMiscDto.getAppPremCorreId();
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = applicationClient.getAppPremisesSpecialDocDtoByCorreId(appPremCorreId).getEntity();
        if(appPremisesSpecialDocDto!=null){
            String fileRepoId = appPremisesSpecialDocDto.getFileRepoId();
            String docName = appPremisesSpecialDocDto.getDocName();
            request.getSession().setAttribute("filename",docName);
            request.getSession().setAttribute("appPremisesSpecialDocDto",appPremisesSpecialDocDto);
        }

        if("MS003".equals(reason)){
            List<AppSvcCgoDto> appSvcCgoDtos = applicationClient.getAppGrpPersonnelByGrpId(appGrpId).getEntity();
            ParamUtil.setRequestAttr(request, "CgoMandatoryCount", appSvcCgoDtos.size());
            List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
            SelectOption sp0 = new SelectOption("-1", "Select Personnel");
            cgoSelectList.add(sp0);
            SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
            cgoSelectList.add(sp1);
            ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);
            ParamUtil.setSessionAttr(request, "GovernanceOfficersList", (Serializable) appSvcCgoDtos);
        }
        request.setAttribute("appPremiseMiscDto",appPremiseMiscDto);
    }

    private AppealPageDto getAppealPageDto(HttpServletRequest req){
        AppealPageDto appealDto=new AppealPageDto();
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        AppPremisesSpecialDocDto sessionAppPremisesSpecialDoc =(AppPremisesSpecialDocDto) req.getSession().getAttribute("appPremisesSpecialDocDto");
        String reasonSelect = request.getParameter("reasonSelect");
        String licenceYear = request.getParameter("licenceYear");
        String selectHciNames = request.getParameter("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        CommonsMultipartFile sessionFile =( CommonsMultipartFile)  req.getSession().getAttribute("file");
        String isDelete = request.getParameter("isDelete");
        String remarks = request.getParameter("remarks");
        String othersReason = request.getParameter("othersReason");
        appealDto.setOtherReason(othersReason);
        CommonsMultipartFile selectedFile =(CommonsMultipartFile) request.getFile("selectedFile");
        if(selectedFile!=null&&selectedFile.getSize()>0){
            try {
                String fileToRepo = serviceConfigService.saveFileToRepo(selectedFile);
                Long size= selectedFile.getSize()/1024;
                String filename = selectedFile.getOriginalFilename();
                String s = FileUtil.genMd5FileChecksum(selectedFile.getBytes());
                AppPremisesSpecialDocDto appPremisesSpecialDocDto=new AppPremisesSpecialDocDto();
                appPremisesSpecialDocDto.setDocName(filename);
                appPremisesSpecialDocDto.setMd5Code(s);
                appPremisesSpecialDocDto.setFileRepoId(fileToRepo);
                appPremisesSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");
                appPremisesSpecialDocDto.setDocSize(Integer.parseInt(size.toString()));
                appealDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);

            } catch (IOException e) {
               log.error(e.getMessage(),e);
            }

        }
        else if(sessionFile!=null&&sessionFile.getSize()>0&&selectedFile!=null){
            if("Y".equals(isDelete)){
                try {
                    String fileToRepo = serviceConfigService.saveFileToRepo(selectedFile);
                    Long size= selectedFile.getSize()/1024;
                    String filename = selectedFile.getOriginalFilename();
                    String s = FileUtil.genMd5FileChecksum(selectedFile.getBytes());
                    AppPremisesSpecialDocDto appPremisesSpecialDocDto=new AppPremisesSpecialDocDto();
                    appPremisesSpecialDocDto.setDocName(filename);
                    appPremisesSpecialDocDto.setMd5Code(s);
                    appPremisesSpecialDocDto.setFileRepoId(fileToRepo);
                    appPremisesSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");
                    appPremisesSpecialDocDto.setDocSize(Integer.parseInt(size.toString()));
                    appealDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);

                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }else if(sessionAppPremisesSpecialDoc!=null&&"Y".equals(isDelete)){
            AppPremisesSpecialDocDto appPremisesSpecialDocDto=new AppPremisesSpecialDocDto();
            appPremisesSpecialDocDto.setDocName(sessionAppPremisesSpecialDoc.getDocName());
            appPremisesSpecialDocDto.setMd5Code(sessionAppPremisesSpecialDoc.getMd5Code());
            appPremisesSpecialDocDto.setFileRepoId(sessionAppPremisesSpecialDoc.getFileRepoId());
            appPremisesSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");
            appPremisesSpecialDocDto.setDocSize(sessionAppPremisesSpecialDoc.getDocSize());
            appealDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
        }

        appealDto.setRemarks(remarks);
        appealDto.setAppealReason(reasonSelect);
        if ("MS008".equals(reasonSelect)) {
            appealDto.setNewHciName(proposedHciName);
        }
        if ("MS004".equals(reasonSelect)) {
            if(!StringUtil.isEmpty(licenceYear)){
                appealDto.setNewLicYears(Integer.valueOf(licenceYear));
            }
        }

        return appealDto;
    }


    private void sendEmail(HttpServletRequest request) throws IOException, TemplateException {
        String newApplicationNo =(String) request.getAttribute("newApplicationNo");
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("applicationNo",newApplicationNo);
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("").getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto=new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("MOH IAIS –Submission of Appeal - Application Number");
        emailDto.setSender("MOH");
        emailDto.setClientQueryCode("sss");
        //need address form login
    }


    private void isMaxCGOnumber(ApplicationDto applicationDto){
        String serviceId = applicationDto.getServiceId();

        List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationClient.getAppSvcKeyPersonnel(applicationDto).getEntity();
        HcsaSvcPersonnelDto hcsaSvcPersonnelDto = appConfigClient.getHcsaSvcPersonnelDtoByServiceId(serviceId).getEntity();
        if(hcsaSvcPersonnelDto!=null){
            int maximumCount = hcsaSvcPersonnelDto.getMaximumCount();
            int size = appSvcKeyPersonnelDtos.size();
            if(size<maximumCount){

            }
        }


    }

}
