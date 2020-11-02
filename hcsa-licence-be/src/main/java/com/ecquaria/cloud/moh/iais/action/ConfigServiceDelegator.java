package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2020/2/11 16:19
 */
@Delegator("congfigSeriviceDelegator")
@Log4j
public class ConfigServiceDelegator {
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrganizationClient organizationClient;

    private static final String CLINICAL_GOVERNANCE_OFFICER ="Clinical Governance Officers";
    private static final String SERVICE_PERSONNEL="Service Personnel";
    private static final String PRINCIPAL_OFFICERS="Principal Officers";
    private static final String DOCUMENTS    =       "Documents";
    private static final String MEDALERT_PERSON  ="MedAlert Person";
    public void start(BaseProcessClass bpc){
        log.info("*********start***********");
        removeSession(bpc);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_SERVICE_CONFIGURATOR);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        OrgUserDto entity = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        bpc.request.getSession().setAttribute("orgUserDto",entity);

    }
    private void removeSession(BaseProcessClass bpc){
        bpc.request.getSession().removeAttribute("hcsaServiceCategoryDtos");
    }
    private Stack stack=new Stack();
    public void switchOr(BaseProcessClass bpc){
    log.info("*********switchOr  start***********");

    log.info("*********switchOr  end***********");
    }
    public void prepare(BaseProcessClass bpc){
        log.info("*********prepare  start***********");

    }

    public void list(BaseProcessClass bpc){
        log.info("*********list  start***********");
        configService.getAllHcsaServices(bpc.request);

    }
    public void addNewService(BaseProcessClass bpc){
        log.info("*********addNewService  start***********");
        bpc.request.getSession().removeAttribute("routingStage");
        configService.addNewService(bpc.request);
        Object individualPremises = bpc.request.getAttribute("individualPremises");
        if(individualPremises==null){
            bpc.request.setAttribute("individualPremises","0");
        }
    }
    public void saveOrUpdate(BaseProcessClass bpc) throws Exception{
        log.info("*********saveOrUpdate  start***********");
        HcsaServiceConfigDto dateOfHcsaService = getDateOfHcsaService(bpc.request);
        configService.saveOrUpdate(bpc.request,bpc.response,dateOfHcsaService);
    }
    public void saveDate(BaseProcessClass bpc){
        log.info("*********saveDate  start***********");
        configService.saData(bpc.request);
    }
    public void editOrDelete(BaseProcessClass bpc){
        log.info("*********editOrDelete  start***********");

    }

    public void edit(BaseProcessClass bpc){
        log.info("*********edit  start***********");
        configService.viewPageInfo(bpc.request);
        StringBuffer requestURL = bpc.request.getRequestURL();
        String queryString = bpc.request.getQueryString();
        String s = requestURL.append(queryString).toString();
        stack.push(s);
    }

    public void editOrSave(BaseProcessClass bpc){
        log.info("*********editOrSave  start***********");
        String crud_action_value = bpc.request.getParameter("crud_action_type");
        if("save".equals(crud_action_value)){
            bpc.request.setAttribute("crud_action_type","save");
        }
        if("edit".equals(crud_action_value)){
            bpc.request.setAttribute("crud_action_type","edit");
        }


    }
    public void editView(BaseProcessClass bpc){
        log.info("*********editView  start***********");

        configService.viewPageInfo(bpc.request);
    }

    public void delete(BaseProcessClass bpc){

        log.info("*********delete  start***********");

        configService.delete(bpc.request);

    }

    public  void selectVersionAsNewTem(BaseProcessClass bpc){

        log.info("*********selectVersionAsNewTem  start***********");

    }
    public void deleteOrCancel(BaseProcessClass bpc){

        log.info("*********deleteOrCancel  start***********");
        configService.deleteOrCancel(bpc.request,bpc.response);

    }

    public void update(BaseProcessClass bpc) throws  Exception{

        log.info("*********update  start***********");
        HcsaServiceConfigDto dateOfHcsaService = getDateOfHcsaService(bpc.request);
        configService.update(bpc.request,bpc.response,dateOfHcsaService);

    }

    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request)  {
        HcsaServiceConfigDto hcsaServiceConfigDto = new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = configService.getHcsaSvcRoutingStageDtos();
        //if service type is sub must to chose
        String[] subsumption = request.getParameterValues("Subsumption");
        String selectSubsumption = ParamUtil.getStringsToString(request, "Subsumption");
        // if service type is pre must be Choice
        String[] preRequisite = request.getParameterValues("Pre-requisite");
        String selectPreRequisite = ParamUtil.getStringsToString(request, "Pre-requisite");
        request.setAttribute("selectSubsumption",selectSubsumption);
        request.setAttribute("selectPreRequisite",selectPreRequisite);
        String serviceId = request.getParameter("serviceId");
        String selectCategoryId = request.getParameter("selectCategoryId");
        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String displayDescription = request.getParameter("displayDescription");
        String serviceCode = request.getParameter("serviceCode");
        String serviceType = request.getParameter("ServiceType");
        String oldEffectiveDate = request.getParameter("oldEffectiveDate");
        String oldEndDate = request.getParameter("oldEndDate");
        String serviceIsUse = request.getParameter("serviceIsUse");
        String[] premisesTypes = request.getParameterValues("PremisesType");
        String version = request.getParameter("version");
        String[] subTypes = request.getParameterValues("subType");
        String[] levels = request.getParameterValues("level");
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= IaisCommonUtils.genNewArrayList();
        Map<Integer ,String> level1=IaisCommonUtils.genNewHashMap();
        if(levels!=null){
            for(int i=0;i<levels.length;i++ ){
                level1.put(i,levels[i]);
            }
            for(int i=0;i<levels.length;i++){
                String s = level1.get(i);
                if("0".equals(s)){
                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto1=new HcsaSvcSubtypeOrSubsumedDto();
                    hcsaSvcSubtypeOrSubsumedDto1.setName(subTypes[i]);
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos2=IaisCommonUtils.genNewArrayList();
                    hcsaSvcSubtypeOrSubsumedDtos.add(hcsaSvcSubtypeOrSubsumedDto1);
                    for(int j=i+1;j<levels.length;j++){
                        String s1 = level1.get(j);
                        if("1".equals(s1)){
                            HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto2=new HcsaSvcSubtypeOrSubsumedDto();
                            hcsaSvcSubtypeOrSubsumedDto2.setName(subTypes[j]);
                            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos3=IaisCommonUtils.genNewArrayList();
                            hcsaSvcSubtypeOrSubsumedDtos2.add(hcsaSvcSubtypeOrSubsumedDto2);
                            hcsaSvcSubtypeOrSubsumedDto1.setList(hcsaSvcSubtypeOrSubsumedDtos2);
                            for(int k=j+1;k<levels.length;k++){
                                String s2 = level1.get(k);
                                if("2".equals(s2)){
                                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto3=new HcsaSvcSubtypeOrSubsumedDto();
                                    hcsaSvcSubtypeOrSubsumedDto3.setName(subTypes[k]);
                                    hcsaSvcSubtypeOrSubsumedDtos3.add(hcsaSvcSubtypeOrSubsumedDto3);
                                    hcsaSvcSubtypeOrSubsumedDto2.setList(hcsaSvcSubtypeOrSubsumedDtos3);
                                }else if(!"2".equals(s2)){

                                    break;
                                }
                            }
                        }else if(!"1".equals(s1)){
                            break;
                        }
                    }
                }
            }
        }


        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceSubTypeDto> list=IaisCommonUtils.genNewArrayList();
        if(ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED.equals(serviceType)){
            if(subsumption!=null){
                for(String str:subsumption){
                    if(!"".equals(str)){
                        HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                        hcsaServiceSubTypeDto.setServiceId(str);
                        list.add(hcsaServiceSubTypeDto);
                    }

                }
            }

        } else if (preRequisite != null && ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(serviceType)) {
            for(String str : preRequisite){
                if(!"".equals(str)){
                    HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                    hcsaServiceSubTypeDto.setServiceId(str);
                    list.add(hcsaServiceSubTypeDto);
                }
            }
        }
        hcsaServiceDto.setServiceSubTypeDtos(list);
        if (premisesTypes != null) {
            for (String str : premisesTypes) {
                HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto = new HcsaSvcSpePremisesTypeDto();
                hcsaSvcSpePremisesTypeDto.setPremisesType(str);
                hcsaSvcSpePremisesTypeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcSpePremisesTypeDtos.add(hcsaSvcSpePremisesTypeDto);
            }
        }

        String manprincipalOfficer = request.getParameter("man-principalOfficer");
        String mixprincipalOfficer = request.getParameter("mix-principalOfficer");
        String poId = request.getParameter("poId");
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = IaisCommonUtils.genNewArrayList();
        HcsaSvcPersonnelDto poDto = new HcsaSvcPersonnelDto();
        poDto.setPsnType("PO");
        try {
            if (!StringUtil.isEmpty(manprincipalOfficer)) {
                poDto.setMandatoryCount(Integer.parseInt(manprincipalOfficer));
                poDto.setPageMandatoryCount(manprincipalOfficer);
            }
        }catch (NumberFormatException e){
            poDto.setPageMandatoryCount(manprincipalOfficer);
        }
        try {
            if (!StringUtil.isEmpty(mixprincipalOfficer)) {
                poDto.setMaximumCount(Integer.parseInt(mixprincipalOfficer));
                poDto.setPageMaximumCount(mixprincipalOfficer);
            }
        }catch (NumberFormatException e){
            poDto.setPageMaximumCount(mixprincipalOfficer);
        }

        if (!StringUtil.isEmpty(poId)) {
            poDto.setServiceId(poId);
        }

        poDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaSvcPersonnelDtos.add(poDto);
        String dpoId = request.getParameter("dpoId");
        String mandeputyPrincipalOfficer = request.getParameter("man-DeputyPrincipalOfficer");
        String mixdeputyPrincipalOfficer = request.getParameter("mix-DeputyPrincipalOfficer");
        HcsaSvcPersonnelDto dpoDto = new HcsaSvcPersonnelDto();
        if (!StringUtil.isEmpty(dpoId)) {
            dpoDto.setId(dpoId);
        }
        dpoDto.setPsnType("DPO");
        try {
            if (!StringUtil.isEmpty(mandeputyPrincipalOfficer)) {
                dpoDto.setMandatoryCount(Integer.parseInt(mandeputyPrincipalOfficer));
                dpoDto.setPageMandatoryCount(mandeputyPrincipalOfficer);
            }
        }catch (NumberFormatException e){
            dpoDto.setPageMandatoryCount(mandeputyPrincipalOfficer);
        }
        try {
            if (!StringUtil.isEmpty(mixdeputyPrincipalOfficer)) {
                dpoDto.setMaximumCount(Integer.parseInt(mixdeputyPrincipalOfficer));
                dpoDto.setPageMaximumCount(mixdeputyPrincipalOfficer);
            }
        }catch (NumberFormatException e){
            dpoDto.setPageMaximumCount(mixdeputyPrincipalOfficer);
        }



        dpoDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaSvcPersonnelDtos.add(dpoDto);
        String cgoId = request.getParameter("cgoId");
        String manclinicalGovernanceOfficer = request.getParameter("man-ClinicalGovernanceOfficer");
        String mixclinicalGovernanceOfficer = request.getParameter("mix-ClinicalGovernanceOfficer");
        HcsaSvcPersonnelDto cgoDto = new HcsaSvcPersonnelDto();
        if (!StringUtil.isEmpty(cgoId)) {
            cgoDto.setId(cgoId);
        }
        cgoDto.setPsnType("CGO");
        try {
            if (!StringUtil.isEmpty(manclinicalGovernanceOfficer)) {
                cgoDto.setMandatoryCount(Integer.parseInt(manclinicalGovernanceOfficer));
                cgoDto.setPageMandatoryCount(manclinicalGovernanceOfficer);
            }
        }catch (NumberFormatException e){
            cgoDto.setPageMandatoryCount(manclinicalGovernanceOfficer);
        }
        try {
            if (!StringUtil.isEmpty(mixclinicalGovernanceOfficer)) {
                cgoDto.setMaximumCount(Integer.parseInt(mixclinicalGovernanceOfficer));
                cgoDto.setPageMaximumCount(mixclinicalGovernanceOfficer);
            }
        }catch (NumberFormatException e){
            cgoDto.setPageMaximumCount(mixclinicalGovernanceOfficer);
        }

        //todo is mandatory ,cannot
        int count=1;
        cgoDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaSvcPersonnelDtos.add(cgoDto);
        String svcpsnId = request.getParameter("svcpsnId");
        String manservicePersonnel = request.getParameter("man-ServicePersonnel");
        String mixservicePersonnel = request.getParameter("mix-ServicePersonnel");
        HcsaSvcPersonnelDto svcPersonnelDto = new HcsaSvcPersonnelDto();
        if (!StringUtil.isEmpty(svcpsnId)) {
            svcPersonnelDto.setId(svcpsnId);
        }
        svcPersonnelDto.setPsnType("SVCPSN");
        try {
            if (!StringUtil.isEmpty(manservicePersonnel)) {
                svcPersonnelDto.setMandatoryCount(Integer.parseInt(manservicePersonnel));
                svcPersonnelDto.setPageMandatoryCount(manservicePersonnel);
            }
        } catch (Exception e) {

            svcPersonnelDto.setPageMandatoryCount(manservicePersonnel);
        }
        try {
            if (!StringUtil.isEmpty(mixservicePersonnel)) {
                svcPersonnelDto.setMaximumCount(Integer.parseInt(mixservicePersonnel));
                svcPersonnelDto.setPageMaximumCount(mixservicePersonnel);
            }
        }catch (NumberFormatException e){
            svcPersonnelDto.setPageMaximumCount(mixservicePersonnel);
        }

        svcPersonnelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaSvcPersonnelDtos.add(svcPersonnelDto);
        String manMedalertPerson = request.getParameter("man-MedalertPerson");
        String mixMedalertPerson = request.getParameter("mix-MedalertPerson");
        HcsaSvcPersonnelDto mapPersonnelDto = new HcsaSvcPersonnelDto();
        mapPersonnelDto.setPsnType("MAP");
        try {
            if(!StringUtil.isEmpty(manMedalertPerson)){
                mapPersonnelDto.setMandatoryCount(Integer.parseInt(manMedalertPerson));
                mapPersonnelDto.setPageMandatoryCount(manMedalertPerson);
            }
        }catch (Exception e){
            mapPersonnelDto.setPageMandatoryCount(manMedalertPerson);
        }
        try {
            if(!StringUtil.isEmpty(mixMedalertPerson)){
                mapPersonnelDto.setMaximumCount(Integer.parseInt(mixMedalertPerson));
                mapPersonnelDto.setPageMaximumCount(mixMedalertPerson);
            }
        }catch (Exception e){
            mapPersonnelDto.setPageMaximumCount(mixMedalertPerson);
        }
        String pageName = request.getParameter("pageName");
        if(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty()){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST001");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(pageName);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        request.setAttribute("pageName",pageName);
        if(cgoDto.getMandatoryCount()>0&&cgoDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST002");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(CLINICAL_GOVERNANCE_OFFICER);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        if(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty()&&cgoDto.getMandatoryCount()>0&&cgoDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST003");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(pageName+" Allocation");
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        if(svcPersonnelDto.getMandatoryCount()>0&&svcPersonnelDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST006");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(SERVICE_PERSONNEL);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        if(poDto.getMandatoryCount()>0&&poDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST004");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(PRINCIPAL_OFFICERS);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }

        mapPersonnelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaSvcPersonnelDtos.add(mapPersonnelDto);
        if(mapPersonnelDto.getMandatoryCount()>0&&mapPersonnelDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST007");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(MEDALERT_PERSON);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfig=IaisCommonUtils.genNewArrayList();
        String numberDocument = request.getParameter("NumberDocument");
        String[] descriptionServiceDocs = request.getParameterValues("descriptionServiceDoc");
        String[] serviceDocMandatories = request.getParameterValues("serviceDocMandatory");
        String numberfields = request.getParameter("Numberfields");
        String individualPremises = request.getParameter("individualPremises");
        request.setAttribute("individualPremises",individualPremises);
        request.setAttribute("serviceDocSize",numberDocument);
        if(descriptionServiceDocs!=null){
            for(int i=0;i<descriptionServiceDocs.length;i++){
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto=new HcsaSvcDocConfigDto();
                hcsaSvcDocConfigDto.setDocDesc(descriptionServiceDocs[i]);
                hcsaSvcDocConfigDto.setDocTitle(descriptionServiceDocs[i]);
                hcsaSvcDocConfigDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcDocConfigDto.setDispOrder(0);
                hcsaSvcDocConfigDto.setServiceId("");
                hcsaSvcDocConfigDto.setDupForPrem("0");
                if("0".equals(serviceDocMandatories[i])){
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.FALSE);
                }else if("1".equals(serviceDocMandatories[i])){
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.TRUE);
                }
                hcsaSvcDocConfigDtos.add(hcsaSvcDocConfigDto);
                hcsaSvcDocConfig.add(hcsaSvcDocConfigDto);
            }
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            hcsaServiceStepSchemeDto.setStepCode("SVST005");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDto.setStepName(DOCUMENTS);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        request.setAttribute("serviceDoc",hcsaSvcDocConfigDtos);
        request.setAttribute("comDocSize",numberfields);
        String[] descriptionCommDocs = request.getParameterValues("descriptionCommDoc");
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtoList=IaisCommonUtils.genNewArrayList();
        String[] commDocMandatory = request.getParameterValues("commDocMandatory");
        String[] commDocIds = request.getParameterValues("commDocId");
        if(descriptionCommDocs!=null){
            for(int i=0;i<descriptionCommDocs.length;i++){
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto=new HcsaSvcDocConfigDto();
                hcsaSvcDocConfigDto.setDocTitle(descriptionCommDocs[i]);
                hcsaSvcDocConfigDto.setDocDesc(descriptionCommDocs[i]);
                hcsaSvcDocConfigDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcDocConfigDto.setDispOrder(0);
                if("0".equals(commDocMandatory[i])){
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.FALSE);
                }else if("1".equals(commDocMandatory[i])){
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.TRUE);
                }
                hcsaSvcDocConfigDto.setDupForPrem(individualPremises);
                if(!StringUtil.isEmpty(commDocIds[i])){
                    hcsaSvcDocConfigDto.setId(commDocIds[i]);
                }
                hcsaSvcDocConfig.add(hcsaSvcDocConfigDto);
                hcsaSvcDocConfigDtoList.add(hcsaSvcDocConfigDto);
            }
        }
        request.setAttribute("comDoc",hcsaSvcDocConfigDtoList);
        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
        List<String> type = configService.getType();
        List<HcsaSvcCateWrkgrpCorrelationDto> hcsaSvcCateWrkgrpCorrelationDto = configService.getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(selectCategoryId);
        Map<String ,HcsaSvcCateWrkgrpCorrelationDto> hashMap= Maps.newHashMapWithExpectedSize(hcsaSvcCateWrkgrpCorrelationDto.size());
        for(HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto : hcsaSvcCateWrkgrpCorrelationDto){
            hashMap.put(svcCateWrkgrpCorrelationDto.getStageId()+svcCateWrkgrpCorrelationDto.getSubOrder(),svcCateWrkgrpCorrelationDto);
        }
        Map<String,List<HcsaSvcSpecificStageWorkloadDto>> hcsaSvcSpecificStageWorkloadDtoMap=IaisCommonUtils.genNewHashMap();
        Map<String,List<HcsaSvcStageWorkingGroupDto>> hcsaSvcStageWorkingGroupDtoMap=IaisCommonUtils.genNewHashMap();
        Map<String, List<HcsaSvcSpeRoutingSchemeDto>> hcsaSvcSpeRoutingSchemeDtoMap=IaisCommonUtils.genNewHashMap();
        for(String every:type){
            List<HcsaSvcSpecificStageWorkloadDto> workloadDtos=IaisCommonUtils.genNewArrayList();
            List<HcsaSvcStageWorkingGroupDto> workingGroupDtos=IaisCommonUtils.genNewArrayList();
            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos=IaisCommonUtils.genNewArrayList();
           /* if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(every) ||
                    ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(every)){
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                //audi ins ,post ins
                HcsaSvcSpecificStageWorkloadDto workloadDto=new HcsaSvcSpecificStageWorkloadDto();
                workloadDto.setStageId("14848A70-820B-EA11-BE7D-000C29F371DC");
                workloadDto.setAppType(every);
                workloadDto.setManhourCount(1);
                workloadDto.setCanApprove("0");
                workloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                workloadDto.setIsMandatory("true");
                HcsaSvcSpeRoutingSchemeDto routingSchemeDto=new HcsaSvcSpeRoutingSchemeDto();
                routingSchemeDto.setStageId("14848A70-820B-EA11-BE7D-000C29F371DC");
                routingSchemeDto.setSchemeType("common");
                routingSchemeDto.setAppType(every);
                routingSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                routingSchemeDto.setIsMandatory("true");
                HcsaSvcStageWorkingGroupDto workingGroupDto=new HcsaSvcStageWorkingGroupDto();
                workingGroupDto.setStageId("14848A70-820B-EA11-BE7D-000C29F371DC");
                workingGroupDto.setSchemeType("common");
                workingGroupDto.setType(every);
                workingGroupDto.setOrder(1);
                workingGroupDto.setIsMandatory("true");
                HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto = hashMap.get("14848A70-820B-EA11-BE7D-000C29F371DC"+1);
                if(svcCateWrkgrpCorrelationDto!=null){
                    workingGroupDto.setStageWorkGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                }
            }*/
            boolean flag=false;
            if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(every) ||
                    ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(every)){
                flag=true;
            }
            for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
                HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto = new HcsaSvcSpeRoutingSchemeDto();
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                String stageCode = hcsaSvcRoutingStageDto.getStageCode();
                String id = hcsaSvcRoutingStageDto.getId();
              /*  if(flag&&"INS".equals(stageCode)){
                    hcsaSvcSpecificStageWorkloadDto.setStageId(id);
                    hcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpecificStageWorkloadDto.setAppType(every);
                    hcsaSvcSpecificStageWorkloadDto.setManhourCount(1);
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory("true");
                    hcsaSvcSpecificStageWorkloadDto.setCanApprove("0");
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory("true");
                    hcsaSvcSpeRoutingSchemeDto.setAppType(every);
                    hcsaSvcSpeRoutingSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType("common");
                    hcsaSvcStageWorkingGroupDto.setIsMandatory("true");
                    hcsaSvcStageWorkingGroupDto.setType(every);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    hcsaSvcStageWorkingGroupDto.setStageId(id);
                    HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto = hashMap.get(id+1);
                    if(svcCateWrkgrpCorrelationDto!=null){
                        hcsaSvcStageWorkingGroupDto.setStageWorkGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                        hcsaConfigPageDto.setWorkingGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                    }
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);
                }else if(flag&&!"INS".equals(stageCode)){


                }*/
                String routingScheme = request.getParameter("RoutingScheme" + stageCode+every);
                String workloadManhours = request.getParameter("WorkloadManhours" + stageCode+every);
                String workloadId = request.getParameter("workloadId" + stageCode+every);
                String stageId = request.getParameter("stageId" + stageCode+every);
                String workstageId = request.getParameter("workstageId" + stageCode+every);
                String isMandatory=  request.getParameter("isMandatory"+ stageCode+every);
                //todo can approve ao1 ao2
                String canApprove = request.getParameter("canApprove" + stageCode + every);
                if(flag&&"INS".equals(stageCode)){
                    routingScheme="common";
                    workloadManhours="1";
                    isMandatory="mandatory";
                }else if(flag&&!"INS".equals(stageCode)){
                    isMandatory="optional";
                }
                if (!StringUtil.isEmpty(workloadManhours)) {
                    hcsaSvcSpecificStageWorkloadDto.setStringManhourCount(workloadManhours);
                    hcsaConfigPageDto.setManhours(workloadManhours);
                    if(workloadManhours.matches("^[0-9]+$")){
                        hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.valueOf(workloadManhours));
                    }
                }
                hcsaConfigPageDto.setCanApprove(canApprove);
                hcsaSvcSpecificStageWorkloadDto.setCanApprove(canApprove);
                if(canApprove==null){
                    hcsaSvcSpecificStageWorkloadDto.setCanApprove("0");
                }
                hcsaConfigPageDto.setWorkloadId(workloadId);
                if (!StringUtil.isEmpty(stageId)) {
                    //todo delete
                    /*   hcsaSvcSpeRoutingSchemeDto.setId(stageId);*/
                    hcsaConfigPageDto.setRoutingSchemeId(stageId);
                }
                hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                hcsaSvcStageWorkingGroupDto.setOrder(1);
                HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto = hashMap.get(id+1);
                if(svcCateWrkgrpCorrelationDto!=null){
                    hcsaSvcStageWorkingGroupDto.setStageWorkGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                    hcsaConfigPageDto.setWorkingGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                }
                hcsaSvcStageWorkingGroupDto.setStageId(id);

                if(!StringUtil.isEmpty(workstageId)){
                    /*hcsaSvcStageWorkingGroupDto.setId(workstageId);*/
                    //todo delete
                    /*  hcsaSvcSpecificStageWorkloadDto.setId(workloadId);*/
                }
                if ("optional".equals(isMandatory)||"SVTP002".equals(serviceType)) {
                    hcsaConfigPageDto.setIsMandatory("false");
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory("false");
                    hcsaSvcStageWorkingGroupDto.setIsMandatory("false");
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory("false");
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    hcsaSvcSpecificStageWorkloadDto.setStringManhourCount(workloadManhours);
                    hcsaSvcSpecificStageWorkloadDto.setStageId(id);
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);

                }else if("mandatory".equals(isMandatory)){
                    hcsaSvcStageWorkingGroupDto.setIsMandatory("true");
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
                    hcsaSvcSpeRoutingSchemeDto.setAppType(every);
                    hcsaSvcSpeRoutingSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory("true");
                    hcsaSvcSpecificStageWorkloadDto.setStageId(id);
                    hcsaSvcSpecificStageWorkloadDto.setAppType(every);
                    hcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory("true");
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);
                    hcsaSvcSpecificStageWorkloadDtoList.add(hcsaSvcSpecificStageWorkloadDto);
                    hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    if("INS".equals(stageCode)){
                        HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto1=new HcsaSvcSpeRoutingSchemeDto();
                        HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto2=new HcsaSvcSpeRoutingSchemeDto();
                        hcsaSvcSpeRoutingSchemeDto1.setStageId(id);
                        hcsaSvcSpeRoutingSchemeDto1.setSchemeType(routingScheme);
                        hcsaSvcSpeRoutingSchemeDto1.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        hcsaSvcSpeRoutingSchemeDto1.setAppType(every);
                        hcsaSvcSpeRoutingSchemeDto2.setStageId(id);
                        hcsaSvcSpeRoutingSchemeDto2.setSchemeType(routingScheme);
                        hcsaSvcSpeRoutingSchemeDto2.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        hcsaSvcSpeRoutingSchemeDto2.setAppType(every);
                        HcsaSvcStageWorkingGroupDto order3=new HcsaSvcStageWorkingGroupDto();
                        HcsaSvcStageWorkingGroupDto order2=new HcsaSvcStageWorkingGroupDto();
                        order2.setOrder(2);
                        order2.setStageId(id);
                        //toto change
                        HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto1 = hashMap.get(id + 2);
                        if(svcCateWrkgrpCorrelationDto1!=null){
                            order2.setStageWorkGroupId(svcCateWrkgrpCorrelationDto1.getWrkGrpId());
                        }else {
                            order2.setStageWorkGroupId("DD2B0DB9-FA0C-EA11-BE7D-000C29F371DC");
                        }
                        if(svcCateWrkgrpCorrelationDto!=null){
                            order3.setStageWorkGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                        }
                        order3.setOrder(3);
                        order3.setStageId(id);
                        hcsaSvcStageWorkingGroupDtos.add(order2);
                        hcsaSvcStageWorkingGroupDtos.add(order3);
                        hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto1);
                        hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto2);
                    }
                    hcsaConfigPageDto.setIsMandatory("true");
                }else if("".equals(isMandatory)) {
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);

                    hcsaSvcStageWorkingGroupDto.setIsMandatory("");
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory("");
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory("");
                }
                hcsaConfigPageDto.setWorkStageId(workstageId);
                hcsaConfigPageDto.setRoutingSchemeName(routingScheme);
                hcsaConfigPageDto.setAppType(every);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }
            hcsaSvcSpecificStageWorkloadDtoMap.put(every,workloadDtos);
            hcsaSvcSpeRoutingSchemeDtoMap.put(every,hcsaSvcSpeRoutingSchemeDtos);
            hcsaSvcStageWorkingGroupDtoMap.put(every,workingGroupDtos);
        }

        String startDate = request.getParameter("StartDate");
        String endDate = request.getParameter("EndDate");

        //todo delete
        if (!StringUtil.isEmpty(serviceId)) {
            hcsaServiceDto.setId(serviceId);
        }
        hcsaServiceDto.setCategoryId(selectCategoryId);
        hcsaServiceDto.setSvcName(serviceName);
        hcsaServiceDto.setSvcCode(serviceCode);
        hcsaServiceDto.setOldEffectiveDate(oldEffectiveDate);
        if(!StringUtil.isEmpty(oldEndDate)){
            try {
                Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(oldEndDate);
                hcsaServiceDto.setOldEndDate(parse);
            }catch (Exception e){

            }
        }
        try {
            Date parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(startDate);
            String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(parse);
            hcsaServiceDto.setEffectiveDate(format);
            if(parse.after(new Date())){
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            }else {
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            }
        } catch (Exception e) {
          /*  Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            hcsaServiceDto.setEffectiveDate(startDate);
            if(parse.after(new Date())){
                hcsaServiceDto.setStatus("CMSTAT003");
            }else {
                hcsaServiceDto.setStatus("CMSTAT001");
            }*/
        }


        if (!StringUtil.isEmpty(endDate)) {
            try {
                hcsaServiceDto.setEndDate(new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(endDate));
            } catch (ParseException e) {
                hcsaServiceDto.setEndDate(new Date(99,1,1));
            }
        }

        hcsaServiceDto.setSvcDisplayDesc(displayDescription);
        hcsaServiceDto.setSvcDesc(description);
        hcsaServiceDto.setSvcType(serviceType);
        if (StringUtil.isEmpty(version)) {
            hcsaServiceDto.setVersion("1");
        } else {
            hcsaServiceDto.setVersion(version);
        }
        hcsaServiceDto.setServiceIsUsed("true".equals(serviceIsUse));
        hcsaServiceConfigDto.setHcsaSvcSubtypeOrSubsumedDtos(hcsaSvcSubtypeOrSubsumedDtos);
        hcsaServiceConfigDto.setHcsaSvcSpePremisesTypeDtos(hcsaSvcSpePremisesTypeDtos);
        hcsaServiceConfigDto.setHcsaSvcDocConfigDtos(hcsaSvcDocConfig);
        hcsaServiceConfigDto.setHcsaSvcPersonnelDtos(hcsaSvcPersonnelDtos);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
        hcsaServiceConfigDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtoList);
        hcsaServiceConfigDto.setHcsaSvcSpecificStageWorkloadDtos(hcsaSvcSpecificStageWorkloadDtoList);
        hcsaServiceConfigDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        hcsaServiceConfigDto.setHcsaSvcStageWorkingGroupDtos(hcsaSvcStageWorkingGroupDtos);
        request.setAttribute("hcsaConfigPageDtos", hcsaConfigPageDtos);
        request.setAttribute("hcsaServiceStepSchemeDtos", hcsaServiceStepSchemeDtos);
        hcsaServiceConfigDto.setHcsaSvcSpecificStageWorkloadDtoMap(hcsaSvcSpecificStageWorkloadDtoMap);
        hcsaServiceConfigDto.setHcsaSvcStageWorkingGroupDtoMap(hcsaSvcStageWorkingGroupDtoMap);
        hcsaServiceConfigDto.setHcsaSvcSpeRoutingSchemeDtoMap(hcsaSvcSpeRoutingSchemeDtoMap);
        hcsaServiceConfigDto.setComDocSize(numberDocument);
        hcsaServiceConfigDto.setServiceDocSize(numberfields);
        return hcsaServiceConfigDto;
    }



}
