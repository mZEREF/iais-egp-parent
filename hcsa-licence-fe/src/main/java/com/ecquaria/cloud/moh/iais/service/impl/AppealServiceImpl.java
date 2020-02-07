package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private LicenceClient licenceClient;
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
        ApplicationDto entity = applicationClient.getApplicationDtoByVersion(appealingFor).getEntity();
        AppInsRepDto appInsRepDto = applicationClient.getHciNameAndAddress(entity.getId()).getEntity();
        String hciName = appInsRepDto.getHciName();
        String hciAddress = appInsRepDto.getHciAddress();
        LicenceDto licenceDto = licenceClient.getLicenceByAppId(entity.getId()).getEntity();

        String licenceNo = licenceDto.getLicenceNo();
        String svcName = licenceDto.getSvcName();

        String reasonSelect = request.getParameter("reasonSelect");
        String selectHciNames = request.getParameter("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        List<AppSvcCgoDto> appSvcCgoDtos = reAppSvcCgo(request);
        AppliSpecialDocDto appliSpecialDocDto =new AppliSpecialDocDto();
        appliSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");

        AppealDto appealDto=new AppealDto();
        appealDto.setApplicationId(appealingFor);
        appealDto.setRemarks(remarks);
        appealDto.setAppealReason(reasonSelect);
        appealDto.setNewHciName(proposedHciName);
        appealDto.setRelateRecId("68F8BB01-F70C-EA11-BE7D-000C29F371DC");

        request.setAttribute("licenceNo",licenceNo);
        request.setAttribute("svcName",svcName);
        request.setAttribute("hciName",hciName);
        request.setAttribute("hciAddress",hciAddress);
        return "AN191206000138-03";
    }

    @Override
    public String saveData(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String appealingFor =  request.getParameter("appealingFor");
        String reasonSelect = request.getParameter("reasonSelect");
        String[] selectHciNames = request.getParameterValues("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");

        List appSvcCgoDtoList = reAppSvcCgo(request);
        ParamUtil.setRequestAttr(request, "CgoMandatoryCount", appSvcCgoDtoList.size());

        ParamUtil.setSessionAttr(request, "GovernanceOfficersList", (Serializable) appSvcCgoDtoList);

        return null;
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
     /*       String specialtyStr = specialty[i];
            appSvcCgoDto.setSpeciality(specialtyStr);
            if("other".equals(specialtyStr)){
                appSvcCgoDto.setSpecialityOther(specialtyOther[i]);
            }*/
            //qualification
            appSvcCgoDto.setQualification(qualification[i]);
            appSvcCgoDto.setMobileNo(mobileNo[i]);
            appSvcCgoDto.setEmailAddr(emailAddress[i]);
            appSvcCgoDto.setCgoIndexNo(cgoIndexNo);
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
        return appSvcCgoDtoList;
    }
}
